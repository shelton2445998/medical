package com.fourth.medical.auth.interceptor;

import com.fourth.medical.framework.exception.NotAuthException;
import com.fourth.medical.framework.interceptor.BaseExcludeMethodInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 没权限拦截器，项目演示用，生成项目根据情况而定
 *
 * @author geekidea
 * @date 2022/6/26
 **/
@Slf4j
public class NotAuthInterceptor extends BaseExcludeMethodInterceptor {

    @Override
    protected boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        log.info("演示环境没有权限访问的url：" + request.getServletPath());
        throw new NotAuthException("演示环境不允许此操作");
    }

}
