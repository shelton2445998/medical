package com.forth.medicalreserve.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一API返回结果
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    private Result() {}

    /**
     * 成功返回结果
     * @param data 数据
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功返回结果
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 失败返回结果
     * @param code 状态码
     * @param message 消息
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败返回结果
     * @param message 消息
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 参数验证失败返回结果
     * @param message 消息
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return error(400, message);
    }

    /**
     * 未登录返回结果
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> unauthorized() {
        return error(401, "未登录或登录已过期");
    }

    /**
     * 无权限返回结果
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> forbidden() {
        return error(403, "没有相关权限");
    }
} 