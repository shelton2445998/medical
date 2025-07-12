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
        // 获取token
        String token = TokenUtil.getToken(request);
        
        // 获取请求路径
        String servletPath = request.getServletPath();
        
        if (StringUtils.isBlank(token)) {
            // 对于APP路径，尝试从其他地方获取token
            if (servletPath.startsWith("/app")) {
                log.debug("APP路径请求: {}", servletPath);
                // 尝试从其他地方获取token（如果需要）
            }
            return true;
        }
        
        // 设置token值到当前线程中，避免重复获取
        TokenCache.set(token);
        
        // 记录日志
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken(token);
        if (systemType != null) {
            log.debug("请求路径: {}, Token类型: {}", servletPath, systemType.getDesc());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TokenCache.remove();
    }
}
