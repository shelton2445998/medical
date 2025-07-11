package com.fourth.medical.framework.filter;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fourth.medical.common.constant.CommonConstant;
import com.fourth.medical.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author geekidea
 * @date 2023/6/22
 **/
@Slf4j
public class TraceIdLogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            // 设置日志链路ID
            String traceId = IdWorker.getIdStr();
            // 设置请求IP
            String ip = IpUtil.getRequestIp(request);
            try {
                MDC.put(CommonConstant.TRACE_ID, traceId);
                MDC.put(CommonConstant.IP, ip);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            // 执行
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            throw e;
        } finally {
            // 移除日志链路ID
            try {
                MDC.remove(CommonConstant.TRACE_ID);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
