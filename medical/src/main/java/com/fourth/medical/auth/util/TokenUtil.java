package com.fourth.medical.auth.util;

import com.fourth.medical.auth.cache.TokenCache;
import com.fourth.medical.auth.exception.LoginTokenException;
import com.fourth.medical.common.constant.LoginConstant;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.util.CookieUtil;
import com.fourth.medical.util.HttpServletRequestUtil;
import com.fourth.medical.util.SystemTypeUtil;
import com.fourth.medical.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Token工具类
 *
 * @author geekidea
 * @date 2022-1-05
 **/
@Slf4j
@Component
public class TokenUtil {

    /**
     * 生成Admin后台Token
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public static String generateAdminToken(Long userId) {
        String userMd5 = DigestUtils.md5Hex(userId.toString());
        String adminToken = LoginConstant.ADMIN_TOKEN_PREFIX + userMd5 + "." + UUIDUtil.getUuid();
        return adminToken;
    }

    /**
     * 生成用户端Token
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public static String generateAppToken(Long userId) {
        String userMd5 = DigestUtils.md5Hex(userId.toString());
        String appToken = LoginConstant.APP_TOKEN_PREFIX + userMd5 + "." + UUIDUtil.getUuid();
        return appToken;
    }

    /**
     * 生成医生端Token
     *
     * @param userId
     * @return
     */
    public static String generateDoctorToken(Long userId) {
        String userMd5 = DigestUtils.md5Hex(userId.toString());
        String doctorToken = LoginConstant.DOCTOR_TOKEN_PREFIX + userMd5 + "." + UUIDUtil.getUuid();
        return doctorToken;
    }

    /**
     * 获取短的ID
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public static String getShortId(Long userId) {
        // 将数字转换成数字加字母变为更短的字符串
        // 36 表示基数(10 位数字 + 26 个字符)
        String string = Long.toString(userId, 36);
        return string;
    }

    /**
     * 解析短的ID
     *
     * @param shorUserId
     * @return
     * @throws Exception
     */
    public static Long parseShortId(String shorUserId) {
        long userId = Long.parseLong(shorUserId, 36);
        return userId;
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        // 从当前线程获取
        String token = TokenCache.get();
        
        if (StringUtils.isBlank(token)) {
            log.warn("当前线程中没有找到token，可能TokenInterceptor未正确设置");
            
            // 尝试从请求中获取
            try {
                HttpServletRequest request = HttpServletRequestUtil.getRequest();
                if (request != null) {
                    token = getToken(request);
                    if (StringUtils.isNotBlank(token)) {
                        // 获取到token后设置到ThreadLocal中
                        TokenCache.set(token);
                        log.info("从请求中获取token成功，已设置到ThreadLocal: {}", token);
                    }
                }
            } catch (Exception e) {
                log.error("尝试获取当前请求对象失败", e);
            }
        }

        return token;
    }

    /**
     * 从请求头或者请求参数中获取token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {

        if (request == null) {

            throw new IllegalArgumentException("request不能为空");
        }
        
        // 从请求头中获取token
        String headerToken = request.getHeader(LoginConstant.TOKEN_NAME);

        
        String token = headerToken;
        
        if (StringUtils.isBlank(token)) {
            // 从请求参数中获取token
            String paramToken = request.getParameter(LoginConstant.TOKEN_NAME);

            token = paramToken;
        }
        
        String servletPath = request.getServletPath();

        
        SystemType systemType = SystemTypeUtil.getSystemTypeByPath(servletPath);

        
        if (StringUtils.isBlank(token)) {
            // 从cookie中获取token

            String cookieToken = getTokenByCookie(request, systemType);

            
            token = cookieToken;
            if (StringUtils.isNotBlank(token) && !token.startsWith(LoginConstant.TOKEN_PREFIX)) {

                token = null;
            }
        }
        
        if (StringUtils.isBlank(token)) {

            return null;
        }
        
        // 如果是非认证路径（不以/admin、/doctor、/user开头），则不校验token格式
        if (systemType == null) {

            return token;
        }
        
        // 校验token

        if (!token.startsWith(LoginConstant.TOKEN_PREFIX)) {

            log.error("token错误:" + token);
            throw new LoginTokenException("token错误");
        }
        
        try {
            if (SystemType.ADMIN == systemType) {

                checkAdminToken(token);
            } else if (SystemType.APP == systemType) {

                checkAppToken(token);
            } else if (SystemType.DOCTOR == systemType) {

                checkDoctorToken(token);
            }

        } catch (Exception e) {

            throw e;
        }
        

        return token;
    }

    /**
     * 从Cookie中获取token
     *
     * @param request
     * @return
     */
    public static String getTokenByCookie(HttpServletRequest request, SystemType systemType) {

        Cookie[] cookies = request.getCookies();

        
        if (ArrayUtils.isEmpty(cookies)) {

            return null;
        }
        
        // 打印所有cookie便于调试
        if (cookies != null) {

            for (Cookie cookie : cookies) {

            }
        }
        
        String result = null;
        if (SystemType.ADMIN == systemType) {

            // 管理系统token的cookie可以通过接口文档传递或者浏览器页面传递
            result = CookieUtil.getCookieValueByName(cookies, LoginConstant.ADMIN_COOKIE_TOKEN_NAME);
        } else if (SystemType.APP == systemType) {

            // 判断是否是接口文档请求，是则从cookie中获取，否则不获取，app接口只能通过接口文档传递token的cookie
            boolean isDocRequest = HttpServletRequestUtil.isDocRequest();

            if (isDocRequest) {

                result = CookieUtil.getCookieValueByName(cookies, LoginConstant.APP_COOKIE_TOKEN_NAME);
            } else {

                result = null;
            }
        } else if (SystemType.DOCTOR == systemType) {

            // 医生端token的cookie
            result = CookieUtil.getCookieValueByName(cookies, LoginConstant.DOCTOR_COOKIE_TOKEN_NAME);
        } else {

            result = CookieUtil.getCookieValueByName(cookies, LoginConstant.ADMIN_COOKIE_TOKEN_NAME);

            
            if (StringUtils.isBlank(result)) {
                result = CookieUtil.getCookieValueByName(cookies, LoginConstant.DOCTOR_COOKIE_TOKEN_NAME);

                
                if (StringUtils.isBlank(result) && HttpServletRequestUtil.isDocRequest()) {
                    result = CookieUtil.getCookieValueByName(cookies, LoginConstant.APP_COOKIE_TOKEN_NAME);

                }
            }
        }
        

        return result;
    }

    /**
     * 校验是否是admin的token
     *
     * @param token
     * @throws Exception
     */
    public static void checkAdminToken(String token) {
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken(token);
        if (SystemType.ADMIN != systemType) {
            throw new LoginTokenException("非管理后台token");
        }
    }

    /**
     * 校验是否是app的token
     *
     * @param token
     * @throws Exception
     */
    public static void checkAppToken(String token) {
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken(token);
        if (SystemType.APP != systemType) {
            throw new LoginTokenException("非移动端token");
        }
    }

    /**
     * 校验是否是医生端的token
     *
     * @param token
     */
    public static void checkDoctorToken(String token) {
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken(token);
        if (SystemType.DOCTOR != systemType) {
            throw new LoginTokenException("非医生端token");
        }
    }
}
