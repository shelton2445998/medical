// 定义包名，指定当前类所在的包路径
package com.fourth.medical.auth.util;

// 导入Token缓存工具类，用于管理Token的线程本地存储
import com.fourth.medical.auth.cache.TokenCache;
// 导入登录常量类，定义登录相关的常量值
import com.fourth.medical.common.constant.LoginConstant;
// 导入系统类型枚举，定义不同的系统类型
import com.fourth.medical.common.enums.SystemType;
// 导入登录令牌异常类，用于抛出Token相关异常
import com.fourth.medical.framework.exception.LoginTokenException;
// 导入Cookie工具类，用于处理Cookie相关操作
import com.fourth.medical.util.CookieUtil;
// 导入HTTP请求工具类，用于获取当前请求对象
import com.fourth.medical.util.HttpServletRequestUtil;
// 导入系统类型工具类，用于根据路径判断系统类型
import com.fourth.medical.util.SystemTypeUtil;
// 导入UUID工具类，用于生成唯一标识符
import com.fourth.medical.util.UUIDUtil;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入Apache Commons Digest工具类，用于MD5加密
import org.apache.commons.codec.digest.DigestUtils;
// 导入Apache Commons Array工具类，用于数组操作
import org.apache.commons.lang3.ArrayUtils;
// 导入Apache Commons String工具类，用于字符串操作
import org.apache.commons.lang3.StringUtils;
// 导入Spring组件注解，标识这是一个Spring组件
import org.springframework.stereotype.Component;

// 导入Servlet Cookie接口
import javax.servlet.http.Cookie;
// 导入Servlet HTTP请求接口
import javax.servlet.http.HttpServletRequest;

/**
 * Token工具类
 * 提供Token的生成、解析、验证等功能，支持不同系统类型的Token管理
 * 包括Admin后台、App用户端、Doctor医生端的Token处理
 *
 * @author geekidea
 * @date 2022-1-05
 **/
@Slf4j  // 自动生成日志对象log
@Component  // 标识这是一个Spring组件，会被Spring容器管理
public class TokenUtil {

    /**
     * 生成Admin后台Token
     * 为管理员用户生成登录令牌，包含用户ID的MD5值和UUID
     *
     * @param userId 管理员用户ID
     * @return String 返回生成的Admin Token字符串
     * @throws Exception 可能抛出的异常
     */
    public static String generateAdminToken(Long userId) {
        // 将用户ID转换为MD5哈希值，用于Token的唯一性标识
        String userMd5 = DigestUtils.md5Hex(userId.toString());
        // 拼接Admin Token前缀、用户MD5值和UUID，形成完整的Token
        String adminToken = LoginConstant.ADMIN_TOKEN_PREFIX + userMd5 + "." + UUIDUtil.getUuid();
        // 返回生成的Admin Token
        return adminToken;
    }

    /**
     * 生成用户端Token
     * 为App用户生成登录令牌，包含用户ID的MD5值和UUID
     *
     * @param userId App用户ID
     * @return String 返回生成的App Token字符串
     * @throws Exception 可能抛出的异常
     */
    public static String generateAppToken(Long userId) {
        // 将用户ID转换为MD5哈希值，用于Token的唯一性标识
        String userMd5 = DigestUtils.md5Hex(userId.toString());
        // 拼接App Token前缀、用户MD5值和UUID，形成完整的Token
        String appToken = LoginConstant.APP_TOKEN_PREFIX + userMd5 + "." + UUIDUtil.getUuid();
        // 返回生成的App Token
        return appToken;
    }

    /**
     * 生成医生端Token
     * 为医生用户生成登录令牌，包含用户ID的MD5值和UUID
     *
     * @param userId 医生用户ID
     * @return String 返回生成的Doctor Token字符串
     */
    public static String generateDoctorToken(Long userId) {
        // 将用户ID转换为MD5哈希值，用于Token的唯一性标识
        String userMd5 = DigestUtils.md5Hex(userId.toString());
        // 拼接Doctor Token前缀、用户MD5值和UUID，形成完整的Token
        String doctorToken = LoginConstant.DOCTOR_TOKEN_PREFIX + userMd5 + "." + UUIDUtil.getUuid();
        // 返回生成的Doctor Token
        return doctorToken;
    }

    /**
     * 获取短的ID
     * 将长整型用户ID转换为36进制的短字符串，用于URL友好显示
     *
     * @param userId 长整型用户ID
     * @return String 返回36进制的短字符串ID
     * @throws Exception 可能抛出的异常
     */
    public static String getShortId(Long userId) {
        // 将数字转换成数字加字母变为更短的字符串
        // 36 表示基数(10 位数字 + 26 个字符)，可以表示更大的数值范围
        String string = Long.toString(userId, 36);
        // 返回转换后的短ID字符串
        return string;
    }

    /**
     * 解析短的ID
     * 将36进制的短字符串ID转换回长整型用户ID
     *
     * @param shorUserId 36进制的短字符串ID
     * @return Long 返回解析后的长整型用户ID
     * @throws Exception 可能抛出的异常
     */
    public static Long parseShortId(String shorUserId) {
        // 将36进制的字符串解析为长整型数值
        long userId = Long.parseLong(shorUserId, 36);
        // 返回解析后的用户ID
        return userId;
    }

    /**
     * 获取token
     * 优先从当前线程的ThreadLocal中获取Token，如果为空则尝试从当前请求中获取
     *
     * @return String 返回当前请求的Token字符串，如果不存在则返回null
     */
    public static String getToken() {
        // 从当前线程的ThreadLocal中获取Token
        String token = TokenCache.get();
        
        // 如果ThreadLocal中没有Token，尝试从当前请求中获取
        if (StringUtils.isBlank(token)) {
            // 记录警告日志，提示TokenInterceptor可能未正确设置
            log.warn("当前线程中没有找到token，可能TokenInterceptor未正确设置");
            
            // 尝试从当前请求中获取Token
            try {
                // 获取当前HTTP请求对象
                HttpServletRequest request = HttpServletRequestUtil.getRequest();
                if (request != null) {
                    // 从请求中解析Token
                    token = getToken(request);
                    if (StringUtils.isNotBlank(token)) {
                        // 获取到Token后设置到ThreadLocal中，供后续使用
                        TokenCache.set(token);
                        log.info("从请求中获取token成功，已设置到ThreadLocal: {}", token);
                    }
                }
            } catch (Exception e) {
                // 记录获取请求对象失败的异常日志
                log.error("尝试获取当前请求对象失败", e);
            }
        }

        // 返回获取到的Token
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
