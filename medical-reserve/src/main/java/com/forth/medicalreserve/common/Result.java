package com.forth.medicalreserve.common;

import com.forth.medicalreserve.constant.CommonConstant;

import java.io.Serializable;

/**
 * 统一返回结果类
 * @param <T> 数据类型
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result<>(CommonConstant.SUCCESS_CODE, "操作成功");
    }

    /**
     * 成功返回结果
     * @param data 返回的数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(CommonConstant.SUCCESS_CODE, "操作成功", data);
    }

    /**
     * 成功返回结果
     * @param message 返回的信息
     * @param data 返回的数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(CommonConstant.SUCCESS_CODE, message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> error() {
        return new Result<>(CommonConstant.ERROR_CODE, "操作失败");
    }

    /**
     * 失败返回结果
     * @param message 返回的信息
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(CommonConstant.ERROR_CODE, message);
    }

    /**
     * 失败返回结果
     * @param code 状态码
     * @param message 返回的信息
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(CommonConstant.UNAUTHORIZED_CODE, message);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
} 