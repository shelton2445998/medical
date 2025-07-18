package com.fourth.medical.framework.response;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fourth.medical.common.constant.CommonConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.Date;

/**
 * REST API 统一响应结果封装类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供统一的API响应结果格式</li>
 *   <li>封装响应状态码、消息、数据等信息</li>
 *   <li>支持链式调用和构建器模式</li>
 *   <li>集成日志链路追踪功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>统一格式：标准化的响应结构</li>
 *   <li>类型安全：支持泛型数据封装</li>
 *   <li>链式调用：支持方法链式调用</li>
 *   <li>构建器模式：使用Builder模式创建实例</li>
 *   <li>链路追踪：集成MDC日志链路ID</li>
 *   <li>时间戳：自动添加响应时间</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>Controller层统一响应格式</li>
 *   <li>API接口标准化输出</li>
 *   <li>前端数据交互格式</li>
 *   <li>微服务间通信响应</li>
 *   <li>第三方接口集成</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于Lombok注解简化代码</li>
 *   <li>集成Swagger文档注解</li>
 *   <li>支持FastJSON和Jackson序列化</li>
 *   <li>使用MDC进行日志链路追踪</li>
 * </ul>
 * 
 * <p><strong>响应结构：</strong></p>
 * <ul>
 *   <li>code：响应状态码（200成功，500失败）</li>
 *   <li>success：响应成功标志</li>
 *   <li>msg：响应消息</li>
 *   <li>data：响应数据</li>
 *   <li>time：响应时间</li>
 *   <li>traceId：日志链路ID</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展更多响应字段</li>
 *   <li>可添加响应数据加密</li>
 *   <li>可支持国际化消息</li>
 *   <li>可集成性能监控指标</li>
 * </ul>
 *
 * @author geekidea
 * @since 2022-3-16
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@Schema(description = "响应结果")
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 7594052194764993562L;

    @Schema(description = "响应编码 200：成功，500：失败")
    private int code;

    @Schema(description = "响应结果 true：成功，false：失败")
    private boolean success;

    @Schema(description = "响应消息")
    private String msg;

    @Schema(description = "响应结果数据")
    private T data;

    @Schema(description = "响应时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    @Schema(description = "日志链路ID")
    private String traceId;

    /**
     * 创建成功响应（无数据）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>创建成功响应结果，不包含数据</li>
     *   <li>使用默认成功状态码和消息</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * return ApiResult.success();
     * }</pre>
     *
     * @return 成功响应结果
     */
    public static ApiResult success() {
        return success(null);
    }

    /**
     * 创建成功响应（带数据）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>创建成功响应结果，包含响应数据</li>
     *   <li>使用默认成功状态码和消息</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * User user = userService.getUserById(1L);
     * return ApiResult.success(user);
     * }</pre>
     *
     * @param data 响应数据
     * @return 成功响应结果
     */
    public static ApiResult success(Object data) {
        return result(ApiCode.SUCCESS, data);
    }

    /**
     * 创建失败响应（默认失败）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>创建失败响应结果</li>
     *   <li>使用默认失败状态码和消息</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * return ApiResult.fail();
     * }</pre>
     *
     * @return 失败响应结果
     */
    public static ApiResult fail() {
        return fail(ApiCode.FAIL);
    }

    /**
     * 创建失败响应（自定义消息）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>创建失败响应结果</li>
     *   <li>使用自定义失败消息</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * return ApiResult.fail("用户不存在");
     * }</pre>
     *
     * @param message 失败消息
     * @return 失败响应结果
     */
    public static ApiResult fail(String message) {
        return fail(ApiCode.FAIL, message);
    }

    /**
     * 创建失败响应（自定义状态码）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>创建失败响应结果</li>
     *   <li>使用自定义失败状态码</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * return ApiResult.fail(ApiCode.PARAM_ERROR);
     * }</pre>
     *
     * @param apiCode 失败状态码
     * @return 失败响应结果
     */
    public static ApiResult fail(ApiCode apiCode) {
        return fail(apiCode, null);
    }

    /**
     * 创建失败响应（自定义状态码和消息）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>创建失败响应结果</li>
     *   <li>使用自定义失败状态码和消息</li>
     *   <li>验证状态码不能为成功状态</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>验证逻辑：</strong></p>
     * <ul>
     *   <li>检查状态码不能为成功状态</li>
     *   <li>抛出运行时异常防止误用</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * return ApiResult.fail(ApiCode.PARAM_ERROR, "参数格式不正确");
     * }</pre>
     *
     * @param apiCode 失败状态码
     * @param message 失败消息
     * @return 失败响应结果
     */
    public static ApiResult fail(ApiCode apiCode, String message) {
        if (ApiCode.SUCCESS == apiCode) {
            throw new RuntimeException("失败结果状态码不能为" + ApiCode.SUCCESS.getCode());
        }
        return result(apiCode, message, null);
    }

    /**
     * 根据布尔值创建响应结果
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>根据布尔值自动创建成功或失败响应</li>
     *   <li>true返回成功响应，false返回失败响应</li>
     *   <li>使用默认状态码和消息</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * boolean result = userService.deleteUser(1L);
     * return ApiResult.result(result);
     * }</pre>
     *
     * @param flag 操作结果标志
     * @return 响应结果
     */
    public static ApiResult result(boolean flag) {
        if (flag) {
            return success();
        }
        return fail();
    }

    /**
     * 创建响应结果（自定义状态码）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>根据状态码创建响应结果</li>
     *   <li>不包含响应数据</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * return ApiResult.result(ApiCode.SUCCESS);
     * }</pre>
     *
     * @param apiCode 响应状态码
     * @return 响应结果
     */
    public static ApiResult result(ApiCode apiCode) {
        return result(apiCode, null);
    }

    /**
     * 创建响应结果（自定义状态码和数据）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>根据状态码和数据创建响应结果</li>
     *   <li>使用状态码默认消息</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * List<User> users = userService.getUserList();
     * return ApiResult.result(ApiCode.SUCCESS, users);
     * }</pre>
     *
     * @param apiCode 响应状态码
     * @param data 响应数据
     * @return 响应结果
     */
    public static ApiResult result(ApiCode apiCode, Object data) {
        return result(apiCode, null, data);
    }

    /**
     * 创建响应结果（完整参数）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>根据状态码、消息和数据创建响应结果</li>
     *   <li>验证状态码不能为空</li>
     *   <li>自动判断成功标志</li>
     *   <li>自动添加时间戳和链路ID</li>
     * </ul>
     * 
     * <p><strong>处理逻辑：</strong></p>
     * <ul>
     *   <li>验证状态码不能为空</li>
     *   <li>根据状态码判断成功标志</li>
     *   <li>优先使用自定义消息，否则使用状态码默认消息</li>
     *   <li>从MDC获取链路ID</li>
     *   <li>使用Builder模式构建响应对象</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * return ApiResult.result(ApiCode.SUCCESS, "操作成功", userData);
     * }</pre>
     *
     * @param apiCode 响应状态码
     * @param message 响应消息
     * @param data 响应数据
     * @return 响应结果
     */
    public static ApiResult result(ApiCode apiCode, String message, Object data) {
        if (apiCode == null) {
            throw new RuntimeException("结果状态码不能为空");
        }
        boolean success = false;
        int code = apiCode.getCode();
        if (ApiCode.SUCCESS.getCode() == code) {
            success = true;
        }
        String outMessage;
        if (StringUtils.isBlank(message)) {
            outMessage = apiCode.getMsg();
        } else {
            outMessage = message;
        }
        String traceId = MDC.get(CommonConstant.TRACE_ID);
        return ApiResult.builder()
                .code(code)
                .msg(outMessage)
                .data(data)
                .success(success)
                .time(new Date())
                .traceId(traceId)
                .build();
    }

}
