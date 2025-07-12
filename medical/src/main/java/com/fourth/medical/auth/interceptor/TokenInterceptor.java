package com.fourth.medical.auth.interceptor;

import com.fourth.medical.auth.cache.TokenCache;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.framework.interceptor.BaseExcludeMethodInterceptor;
import com.fourth.medical.util.SystemTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token拦截器
 *
 * @author geekidea
 * @date 2023/12/03
 **/
@Slf4j
public class TokenInterceptor extends BaseExcludeMethodInterceptor {

    @Override
    protected boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        // 获取请求路径
        String servletPath = request.getServletPath();
        
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        
        if (StringUtils.isBlank(token)) {
            // 对于APP路径，尝试从其他地方获取token
            if (servletPath.startsWith("/app")) {
                log.debug("APP路径请求: {}, 但未获取到token", servletPath);
            }
            return true;
        }
        
        // 设置token值到当前线程中，避免重复获取
        TokenCache.set(token);
        
        // 记录日志
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken(token);
        if (systemType != null) {
            log.debug("请求路径: {}, Token类型: {}, Token值: {}", servletPath, systemType.getDesc(), token);
            
            // 对于APP请求，额外记录日志以帮助诊断
            if (servletPath.startsWith("/app") && SystemType.APP == systemType) {
                log.info("APP请求验证: 路径={}, token已设置到ThreadLocal", servletPath);
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TokenCache.remove();
    }
}
