package com.fourth.medical.auth.util;

import com.fourth.medical.auth.cache.AppLoginCache;
import com.fourth.medical.auth.service.AppLoginRedisService;
import com.fourth.medical.auth.vo.AppLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author geekidea
 * @date 2022/6/26
 **/
@Slf4j
@Component
public class AppLoginUtil {

    private static AppLoginRedisService appLoginRedisService;

    public AppLoginUtil(AppLoginRedisService appLoginRedisService) {
        AppLoginUtil.appLoginRedisService = appLoginRedisService;
    }

    /**
     * 根据token从redis中获取登录用户信息
     *
     * @param token
     * @return
     * @
     */
    public static AppLoginVo getLoginVo(String token) {
        if (StringUtils.isBlank(token)) {
            log.warn("获取APP登录信息失败: token为空");
            return null;
        }
        
        try {
        AppLoginVo appLoginVo = appLoginRedisService.getLoginVo(token);
            if (appLoginVo == null) {
                log.warn("从Redis获取APP登录信息失败: token={}", token);
            } else {
                log.debug("成功获取APP用户信息: userId={}, username={}", appLoginVo.getUserId(), appLoginVo.getUsername());
            }
        return appLoginVo;
        } catch (Exception e) {
            log.error("从Redis获取APP登录信息异常: token=" + token, e);
            return null;
        }
    }

    /**
     * 从当前线程中获取登录用户信息
     *
     * @return
     */
    public static AppLoginVo getLoginVo() {
        AppLoginVo appLoginVo = AppLoginCache.get();
        if (appLoginVo == null) {
            // 如果线程缓存中没有，尝试从token中获取
            String token = TokenUtil.getToken();
            if (StringUtils.isNotBlank(token)) {
                appLoginVo = getLoginVo(token);
                // 如果获取到了，放入线程缓存
                if (appLoginVo != null) {
                    AppLoginCache.set(appLoginVo);
                    log.info("成功从token获取APP用户并保存到线程: userId={}", appLoginVo.getUserId());
                }
            }
        }
        return appLoginVo;
    }
    
    /**
     * 从请求中获取登录用户信息
     * 
     * @param request HTTP请求
     * @return 登录用户信息
     */
    public static AppLoginVo getLoginVo(HttpServletRequest request) {
        if (request == null) {
            log.warn("获取APP登录信息失败: request为空");
            return null;
        }
        
        // 先从请求中获取token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            log.warn("从请求中获取APP登录信息失败: token为空");
            return null;
        }
        
        // 通过token获取用户信息
        AppLoginVo appLoginVo = getLoginVo(token);
        if (appLoginVo != null) {
            // 保存到线程本地变量中
            AppLoginCache.set(appLoginVo);
        }
        
        return appLoginVo;
    }

    /**
     * 获取登录用户ID
     *
     * @return
     */
    public static Long getUserId() {
        AppLoginVo appLoginVo = getLoginVo();
        if (appLoginVo != null) {
            return appLoginVo.getUserId();
        }
        return null;
    }

    /**
     * 获取登录用户ID字符串
     *
     * @return
     * @
     */
    public static String getUserIdString() {
        Long userId = getUserId();
        if (userId == null) {
            return null;
        }
        return userId.toString();
    }

    /**
     * 获取登录用户名
     *
     * @return
     */
    public static String getUsername() {
        AppLoginVo appLoginVo = getLoginVo();
        if (appLoginVo != null) {
            return appLoginVo.getUsername();
        }
        return null;
    }

    /**
     * 获取用户角色ID
     *
     * @return
     */
    public static Long getUserRoleId() {
        AppLoginVo appLoginVo = getLoginVo();
        if (appLoginVo != null) {
            return appLoginVo.getUserRoleId();
        }
        return null;
    }

    /**
     * 获取用户角色编码
     *
     * @return
     */
    public static String getUserRoleCode() {
        AppLoginVo appLoginVo = getLoginVo();
        if (appLoginVo != null) {
            return appLoginVo.getUserRoleCode();
        }
        return null;
    }

}
