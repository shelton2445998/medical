package com.fourth.medical.framework.exception;

import com.fourth.medical.framework.response.ApiCode;
import com.fourth.medical.framework.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * 全局异常处理器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供系统全局异常的统一处理机制</li>
 *   <li>将各种异常转换为标准化的API响应格式</li>
 *   <li>确保系统异常不会直接暴露给客户端</li>
 *   <li>提供详细的异常日志记录</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>统一处理：捕获并处理所有未处理的异常</li>
 *   <li>分类处理：针对不同类型的异常提供专门的处理逻辑</li>
 *   <li>标准化响应：将异常转换为统一的ApiResult格式</li>
 *   <li>日志记录：自动记录异常详细信息</li>
 *   <li>安全保护：防止敏感异常信息泄露</li>
 *   <li>参数校验：专门处理参数验证异常</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>系统运行时异常处理</li>
 *   <li>参数验证失败处理</li>
 *   <li>数据库操作异常处理</li>
 *   <li>认证授权异常处理</li>
 *   <li>业务逻辑异常处理</li>
 *   <li>HTTP请求异常处理</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于Spring Boot @RestControllerAdvice注解</li>
 *   <li>使用@ExceptionHandler进行异常拦截</li>
 *   <li>集成Lombok日志功能</li>
 *   <li>统一的ApiResult响应格式</li>
 *   <li>HTTP状态码标准化处理</li>
 * </ul>
 * 
 * <p><strong>异常类型支持：</strong></p>
 * <ul>
 *   <li>LoginException：登录相关异常</li>
 *   <li>NotAuthException：权限不足异常</li>
 *   <li>BusinessException：业务逻辑异常</li>
 *   <li>MethodArgumentNotValidException：参数验证异常</li>
 *   <li>HttpMessageNotReadableException：请求参数解析异常</li>
 *   <li>MethodArgumentTypeMismatchException：参数类型错误</li>
 *   <li>DuplicateKeyException：数据唯一约束异常</li>
 *   <li>DataIntegrityViolationException：数据完整性异常</li>
 *   <li>HttpRequestMethodNotSupportedException：HTTP方法不支持</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展更多异常类型处理</li>
 *   <li>可添加异常监控和告警</li>
 *   <li>可支持异常分类统计</li>
 *   <li>可集成第三方异常处理服务</li>
 * </ul>
 *
 * @author geekidea
 * @date 2021-4-13
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 静态异常处理方法
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>提供静态方法用于异常处理</li>
     *   <li>根据异常类型进行分类处理</li>
     *   <li>返回标准化的API响应结果</li>
     *   <li>支持在非Controller环境中使用</li>
     * </ul>
     * 
     * <p><strong>处理逻辑：</strong></p>
     * <ol>
     *   <li>检查异常类型并进行分类</li>
     *   <li>提取异常的关键信息</li>
     *   <li>生成对应的错误消息</li>
     *   <li>返回标准化的ApiResult</li>
     * </ol>
     * 
     * <p><strong>异常分类处理：</strong></p>
     * <ul>
     *   <li>认证异常：返回TOKEN_EXCEPTION状态码</li>
     *   <li>权限异常：返回权限不足消息</li>
     *   <li>业务异常：返回业务错误消息</li>
     *   <li>参数异常：提取第一个字段错误信息</li>
     *   <li>数据异常：返回数据库操作错误</li>
     *   <li>HTTP异常：返回请求方法错误</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * try {
     *     // 业务逻辑
     * } catch (Exception e) {
     *     return GlobalExceptionHandler.handle(e);
     * }
     * }</pre>
     *
     * @param exception 要处理的异常
     * @return 标准化的API响应结果
     */
    public static ApiResult handle(Throwable exception) {
        if (exception instanceof LoginException) {
            return ApiResult.fail(ApiCode.TOKEN_EXCEPTION, exception.getMessage());
        } else if (exception instanceof NotAuthException) {
            return ApiResult.fail(exception.getMessage());
        } else if (exception instanceof BusinessException) {
            return ApiResult.fail(exception.getMessage());
        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;
            BindingResult bindingResult = ex.getBindingResult();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            FieldError fieldError = fieldErrors.get(0);
            String errorMessage = fieldError.getDefaultMessage();
            log.error("参数校验错误" + "：" + errorMessage);
            return ApiResult.fail(errorMessage);
        } else if (exception instanceof HttpMessageNotReadableException) {
            return ApiResult.fail("请求参数解析异常");
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            return ApiResult.fail("请求参数数据类型错误");
        } else if (exception instanceof DuplicateKeyException) {
            return ApiResult.fail("数据违反唯一约束");
        } else if (exception instanceof DataIntegrityViolationException) {
            return ApiResult.fail("数据完整性异常");
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return ApiResult.fail(exception.getMessage());
        }
        return ApiResult.fail();
    }

    /**
     * 全局异常处理器
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>拦截所有Controller中抛出的异常</li>
     *   <li>自动记录异常日志信息</li>
     *   <li>返回标准化的错误响应</li>
     *   <li>确保HTTP状态码为200</li>
     * </ul>
     * 
     * <p><strong>处理流程：</strong></p>
     * <ol>
     *   <li>捕获Controller抛出的异常</li>
     *   <li>记录详细的异常日志</li>
     *   <li>调用静态处理方法进行分类处理</li>
     *   <li>返回标准化的API响应</li>
     * </ol>
     * 
     * <p><strong>注解说明：</strong></p>
     * <ul>
     *   <li>@ExceptionHandler：指定处理的异常类型</li>
     *   <li>@ResponseStatus：设置HTTP响应状态码</li>
     *   <li>HttpStatus.OK：确保返回200状态码</li>
     * </ul>
     * 
     * <p><strong>日志记录：</strong></p>
     * <ul>
     *   <li>记录异常堆栈信息</li>
     *   <li>便于问题排查和调试</li>
     *   <li>支持日志级别配置</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * @GetMapping("/user/{id}")
     * public ApiResult getUser(@PathVariable Long id) {
     *     if (id == null) {
     *         throw new BusinessException("用户ID不能为空");
     *     }
     *     // 该方法抛出的异常会被自动捕获并处理
     * }
     * }</pre>
     *
     * @param exception 捕获的异常
     * @return 标准化的API响应结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult exceptionHandle(Exception exception) {
        log.error("exception:", exception);
        return handle(exception);
    }
}
