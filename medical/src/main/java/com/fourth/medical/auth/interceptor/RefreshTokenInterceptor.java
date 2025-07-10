package com.fourth.medical.auth.interceptor;

import com.fourth.medical.auth.service.AppLoginRedisService;
import com.fourth.medical.auth.service.LoginRedisService;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.framework.interceptor.BaseExcludeMethodInterceptor;
import com.fourth.medical.util.SystemTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author geekidea
 * @date 2022/6/26
 **/
@Slf4j
public class RefreshTokenInterceptor extends BaseExcludeMethodInterceptor {

    @Autowired
    private LoginRedisService loginRedisService;

    @Autowired
    private AppLoginRedisService appLoginRedisService;

    @Override
    protected boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        SystemType systemType = SystemTypeUtil.getSystemTypeByPath(request);
        if (SystemType.ADMIN == systemType) {
            loginRedisService.refreshToken();
        } else if (SystemType.APP == systemType) {
            appLoginRedisService.refreshToken();
        }
        return true;
    }

}
