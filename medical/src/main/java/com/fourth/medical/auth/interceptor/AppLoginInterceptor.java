package com.fourth.medical.auth.interceptor;

import com.fourth.medical.auth.annotation.AppUserRole;
import com.fourth.medical.auth.cache.AppLoginCache;
import com.fourth.medical.auth.enums.LoginInterceptStrategy;
import com.fourth.medical.auth.util.AppLoginUtil;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.AppLoginVo;
import com.fourth.medical.config.properties.LoginAppProperties;
import com.fourth.medical.framework.exception.AuthException;
import com.fourth.medical.framework.exception.LoginTokenException;
import com.fourth.medical.framework.interceptor.BaseExcludeMethodInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author geekidea
 * @date 2022/6/26
 **/
@Slf4j
public class AppLoginInterceptor extends BaseExcludeMethodInterceptor {

    @Autowired
    private LoginAppProperties loginAppProperties;

    @Override
    protected boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        try {
            // 从请求中获取token，而不是使用无参数方法
            String token = TokenUtil.getToken(request);
            log.debug("APP拦截器从请求中获取到token: {}", token);
            
            // 直接从请求中获取用户信息
            AppLoginVo appLoginVo = AppLoginUtil.getLoginVo(request);
            
            if (appLoginVo != null) {
                log.debug("APP拦截器从请求中获取到用户信息: userId={}, username={}", 
                    appLoginVo.getUserId(), appLoginVo.getUsername());
            } else {
                log.warn("APP拦截器无法从请求中获取用户信息，尝试从token获取");
                
                // 如果从请求中获取失败，尝试从token获取
        if (StringUtils.isNotBlank(token)) {
            appLoginVo = AppLoginUtil.getLoginVo(token);
            if (appLoginVo != null) {
                // 将APP移动端的登录信息保存到当前线程中
                AppLoginCache.set(appLoginVo);
                        log.debug("APP拦截器从token中获取到用户信息: userId={}", appLoginVo.getUserId());
                    } else {
                        log.warn("APP拦截器无法从token获取用户信息: {}", token);
                    }
                }
            }
            
        // 判断登录校验策略
        LoginInterceptStrategy loginInterceptStrategy = loginAppProperties.getLoginInterceptStrategy();
        if (LoginInterceptStrategy.LOGIN == loginInterceptStrategy) {
            // 默认都需要登录，此时判断是否有@IgnoreLogin注解，如果有，则跳过，否则，则验证登录
            boolean existsIgnoreLoginAnnotation = existsIgnoreLoginAnnotation(handlerMethod);
            if (existsIgnoreLoginAnnotation) {
                    log.debug("APP请求: 跳过登录验证(IgnoreLogin注解)");
                return true;
            }
            // 是否是排除登录的路径，如果是，则跳过，否则，则验证
            boolean isIgnoreLoginPath = isIgnoreLoginPath(request, loginAppProperties.getIgnoreLoginPaths());
            if (isIgnoreLoginPath) {
                    log.debug("APP请求: 跳过登录验证(忽略路径)");
                return true;
            }
        } else {
            // 默认都忽略登录，此时判断是否有@Login注解，如果有，则验证登录，否则，则跳过
            boolean existsLoginAnnotation = existsLoginAnnotation(handlerMethod);
            if (!existsLoginAnnotation) {
                // 如果不是要校验的路径，则跳过
                boolean isCheckLoginPath = isCheckLoginPath(request, loginAppProperties.getCheckLoginPaths());
                if (!isCheckLoginPath) {
                        log.debug("APP请求: 跳过登录验证(无需检查的路径)");
                    return true;
                }
            }
        }
            
        // 登录校验
        if (StringUtils.isBlank(token)) {
                log.warn("APP请求需要登录: token为空");
            throw new LoginTokenException("请登录后再操作");
        }
            
        // 校验登录用户信息
        if (appLoginVo == null) {
                // 再尝试从缓存获取
                appLoginVo = AppLoginCache.get();
                
                // 如果缓存中也没有，最后再次尝试从token获取
                if (appLoginVo == null) {
                    appLoginVo = AppLoginUtil.getLoginVo(token);
                    if (appLoginVo != null) {
                        // 找到了，保存到缓存
                        AppLoginCache.set(appLoginVo);
                        log.info("APP拦截器重新获取到用户信息: userId={}", appLoginVo.getUserId());
                    } else {
                        log.error("APP请求验证失败: 无法获取用户信息, token={}", token);
            throw new LoginTokenException("登录已过期或登录信息不存在，请重新登录");
                    }
                }
        }
        
        return true;
        } catch (LoginTokenException e) {
            // 直接抛出登录异常
            throw e;
        } catch (Exception e) {
            // 记录异常日志
            log.error("APP鉴权拦截器异常: " + e.getMessage(), e);
            throw new LoginTokenException("登录验证异常，请重新登录");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
        AppLoginCache.remove();
        } catch (Exception e) {
            log.error("清理APP登录缓存异常", e);
        }
    }

}
