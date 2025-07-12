package com.fourth.medical.auth.interceptor;

import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.framework.exception.LoginTokenException;
import com.fourth.medical.framework.interceptor.BaseMethodInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 基于路径的认证拦截器
 * 用于区分需要认证和不需要认证的路径
 */
@Slf4j
public class PathBasedAuthInterceptor extends BaseMethodInterceptor {

    // 需要进行认证的路径前缀
    // private static final List<String> AUTH_PATH_PREFIXES = Arrays.asList("/doctor", "/admin", "/user");
    private static final List<String> AUTH_PATH_PREFIXES = Arrays.asList("/doctor", "/admin", "/user");
    // 公开API路径（即使以上述前缀开头也不需要认证）
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/doctor/login", 
            "/admin/login", 
            "/user/login",
            "/doctor/register",
            "/admin/register",
            "/user/register" 
    );

    @Override
    protected boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        String servletPath = request.getServletPath();
        log.debug("当前请求路径: {}", servletPath);
        
        // 检查是否是公开路径，如果是则跳过认证
        for (String publicPath : PUBLIC_PATHS) {
            if (servletPath.startsWith(publicPath)) {
                log.debug("公开路径，跳过认证: {}", servletPath);
                return true;
            }
        }
        
        // 检查是否需要认证
        boolean needAuth = false;
        for (String prefix : AUTH_PATH_PREFIXES) {
            if (servletPath.startsWith(prefix)) {
                needAuth = true;
                break;
            }
        }
        
        // 如果不需要认证，直接通过
        if (!needAuth) {
            log.debug("非认证路径，跳过认证: {}", servletPath);
            return true;
        }
        
        // 需要认证的路径，检查token
        String token = TokenUtil.getToken(request);
        if (StringUtils.isBlank(token)) {
            log.warn("需要认证的路径，但未提供token: {}", servletPath);
            throw new LoginTokenException("请登录后再操作");
        }
        
        // token验证由其他拦截器完成
        return true;
    }
} 