package com.fourth.medical.framework.exception;

/**
 * 业务逻辑异常类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>用于表示业务逻辑执行过程中的异常情况</li>
 *   <li>继承RuntimeException，属于非受检异常</li>
 *   <li>提供业务相关的错误信息</li>
 *   <li>支持全局异常处理器统一处理</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>非受检异常：继承RuntimeException，无需显式声明</li>
 *   <li>业务导向：专门用于业务逻辑异常</li>
 *   <li>消息传递：支持自定义错误消息</li>
 *   <li>统一处理：可被GlobalExceptionHandler捕获处理</li>
 *   <li>链式传播：支持异常链的传播</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>业务规则验证失败</li>
 *   <li>数据状态不符合业务要求</li>
 *   <li>业务操作权限不足</li>
 *   <li>业务流程中断</li>
 *   <li>业务数据冲突</li>
 *   <li>业务配置错误</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>继承RuntimeException</li>
 *   <li>提供字符串消息构造函数</li>
 *   <li>支持异常消息传递</li>
 *   <li>与全局异常处理器集成</li>
 * </ul>
 * 
 * <p><strong>异常处理流程：</strong></p>
 * <ul>
 *   <li>业务层抛出BusinessException</li>
 *   <li>GlobalExceptionHandler捕获异常</li>
 *   <li>提取异常消息</li>
 *   <li>返回标准化的ApiResult</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展错误码支持</li>
 *   <li>可添加异常分类</li>
 *   <li>可支持国际化消息</li>
 *   <li>可集成异常监控</li>
 * </ul>
 *
 * @author geekidea
 * @date 2018-11-08
 */
public class BusinessException extends RuntimeException {

    /**
     * 业务异常构造函数
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>创建业务异常实例</li>
     *   <li>设置业务相关的错误消息</li>
     *   <li>支持异常信息的传递</li>
     * </ul>
     * 
     * <p><strong>使用场景：</strong></p>
     * <ul>
     *   <li>业务规则验证失败时抛出</li>
     *   <li>数据状态异常时抛出</li>
     *   <li>业务操作失败时抛出</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * // 用户不存在
     * if (user == null) {
     *     throw new BusinessException("用户不存在");
     * }
     * 
     * // 余额不足
     * if (balance < amount) {
     *     throw new BusinessException("账户余额不足");
     * }
     * 
     * // 状态不允许操作
     * if (!OrderStatus.PENDING.equals(order.getStatus())) {
     *     throw new BusinessException("订单状态不允许此操作");
     * }
     * }</pre>
     * 
     * <p><strong>异常处理：</strong></p>
     * <ul>
     *   <li>异常会被GlobalExceptionHandler捕获</li>
     *   <li>返回标准化的错误响应</li>
     *   <li>客户端收到友好的错误消息</li>
     * </ul>
     *
     * @param message 业务异常消息，描述具体的业务错误
     */
    public BusinessException(String message) {
        super(message);
    }

}
