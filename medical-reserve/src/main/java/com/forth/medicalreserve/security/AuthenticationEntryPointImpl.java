package com.forth.medicalreserve.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forth.medicalreserve.common.Result;
import com.forth.medicalreserve.constant.CommonConstant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证异常处理器
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(
                Result.error(CommonConstant.UNAUTHORIZED_CODE, "未授权：" + e.getMessage())
        ));
        writer.flush();
        writer.close();
    }
} 