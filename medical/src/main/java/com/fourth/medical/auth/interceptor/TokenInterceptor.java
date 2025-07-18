// 定义包名，指定当前类所在的包路径
package com.fourth.medical.auth.interceptor;

// 导入Token缓存类，用于缓存Token到ThreadLocal中
import com.fourth.medical.auth.cache.TokenCache;
// 导入Token工具类，用于从请求中获取Token
import com.fourth.medical.auth.util.TokenUtil;
// 导入系统类型枚举，用于区分不同的系统类型
import com.fourth.medical.common.enums.SystemType;
// 导入基础排除方法拦截器，提供排除特定方法的功能
import com.fourth.medical.framework.interceptor.BaseExcludeMethodInterceptor;
// 导入系统类型工具类，用于根据Token判断系统类型
import com.fourth.medical.util.SystemTypeUtil;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入Apache Commons字符串工具类
import org.apache.commons.lang3.StringUtils;
// 导入Spring处理器方法类，用于获取处理器方法信息
import org.springframework.web.method.HandlerMethod;

// 导入Servlet HTTP请求接口
import javax.servlet.http.HttpServletRequest;
// 导入Servlet HTTP响应接口
import javax.servlet.http.HttpServletResponse;

/**
 * Token拦截器
 * 
 * 功能说明：
 * 负责在请求处理前从HTTP请求中提取Token，并将Token缓存到ThreadLocal中，
 * 供后续的认证和授权流程使用。
 * 
 * 主要职责：
 * 1. 从HTTP请求中提取Token（支持Header、Cookie、参数等多种方式）
 * 2. 将Token缓存到ThreadLocal中，避免重复获取
 * 3. 记录Token相关的调试日志
 * 4. 在请求完成后清理ThreadLocal中的Token缓存
 * 
 * 工作流程：
 * 1. 请求到达时，从请求中提取Token
 * 2. 如果Token存在，将其存储到ThreadLocal中
 * 3. 记录相关日志信息
 * 4. 请求处理完成后，清理ThreadLocal缓存
 * 
 * 适用场景：
 * - 所有需要Token认证的请求
 * - 管理后台(/admin/**)、移动端(/app/**)、医生端(/doctor/**)等不同系统
 * - 支持多种Token获取方式（Header、Cookie、参数）
 * 
 * 注意事项：
 * - 该拦截器不进行Token验证，只负责Token提取和缓存
 * - 实际的Token验证由后续的登录拦截器完成
 * - 使用ThreadLocal存储Token，确保线程安全
 * - 请求完成后必须清理ThreadLocal，防止内存泄漏
 * 
 * @author geekidea
 * @date 2023/12/03
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j // 自动生成日志对象log
public class TokenInterceptor extends BaseExcludeMethodInterceptor {

    /**
     * 预处理方法 - 在请求处理前执行
     * 
     * 主要功能：
     * 1. 从HTTP请求中提取Token
     * 2. 将Token缓存到ThreadLocal中
     * 3. 记录相关日志信息
     * 
     * @param request HTTP请求对象，包含请求信息
     * @param response HTTP响应对象，包含响应信息
     * @param handlerMethod 处理器方法对象，包含要执行的方法信息
     * @return boolean 返回true表示继续处理，false表示中断处理
     * @throws Exception 可能抛出的异常
     */
    @Override
    protected boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        // 获取请求路径，用于日志记录和路径分析
        String servletPath = request.getServletPath();
        
        // 从请求中获取Token，支持多种获取方式：
        // 1. Authorization Header
        // 2. Cookie中的Token
        // 3. 请求参数中的Token
        String token = TokenUtil.getToken(request);
        
        // 如果Token为空，进行相应处理
        if (StringUtils.isBlank(token)) {
            // 对于APP路径，记录调试日志，便于问题排查
            if (servletPath.startsWith("/app")) {
                log.debug("APP路径请求: {}, 但未获取到token", servletPath);
            }
            // 返回true，允许请求继续处理
            // 注意：这里不拦截无Token的请求，由后续的登录拦截器处理
            return true;
        }
        
        // 将Token值设置到ThreadLocal缓存中
        // 这样在同一个请求的处理过程中，可以随时获取Token而不需要重复解析
        TokenCache.set(token);
        
        // 根据Token判断系统类型，并记录相关日志
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken(token);
        if (systemType != null) {
            // 记录调试日志，包含请求路径、Token类型和Token值
            log.debug("请求路径: {}, Token类型: {}, Token值: {}", servletPath, systemType.getDesc(), token);
            
            // 对于APP请求，额外记录信息日志以帮助诊断问题
            if (servletPath.startsWith("/app") && SystemType.APP == systemType) {
                log.info("APP请求验证: 路径={}, token已设置到ThreadLocal", servletPath);
            }
        }

        // 返回true，允许请求继续处理
        return true;
    }

    /**
     * 请求完成后的处理方法
     * 
     * 主要功能：
     * 1. 清理ThreadLocal中的Token缓存
     * 2. 防止内存泄漏
     * 
     * 重要性：
     * - ThreadLocal如果不及时清理，会导致内存泄漏
     * - 在Web应用中，线程会被重用，不清理会导致数据污染
     * - 必须在finally块中或请求完成后执行清理操作
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器对象
     * @param ex 执行过程中的异常（如果有）
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal中的Token缓存
        // 这是必须的操作，防止内存泄漏和数据污染
        TokenCache.remove();
    }
}
