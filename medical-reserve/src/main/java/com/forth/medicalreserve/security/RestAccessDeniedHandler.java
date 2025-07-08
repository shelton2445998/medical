package com.forth.medicalreserve.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forth.medicalreserve.common.Result;
import com.forth.medicalreserve.constant.CommonConstant;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义访问拒绝处理器
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(
                Result.error(CommonConstant.ERROR_CODE, "没有权限访问该资源")
        ));
        writer.flush();
        writer.close();
    }
} 