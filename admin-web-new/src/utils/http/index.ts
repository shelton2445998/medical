/**
 * @fileoverview HTTP请求工具模块
 * @description 基于Axios封装的HTTP请求工具，提供统一的请求拦截、响应拦截和错误处理
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 统一的HTTP请求封装
 * - 请求拦截器（自动添加Token）
 * - 响应拦截器（统一错误处理）
 * - Token过期自动跳转登录
 * - 支持自定义请求/响应回调
 * - 支持GET、POST、PUT、DELETE等请求方法
 * - 统一的错误提示处理
 */
import Axios, {AxiosInstance, AxiosRequestConfig, AxiosResponse, CustomParamsSerializer} from 'axios'
import {stringify} from 'qs'
import {ElMessage, ElMessageBox} from "element-plus";
import {getToken} from "@/utils/auth";

import {PureHttpRequestConfig, RequestMethods} from "./types.d";
import {ResultType} from "@/api/types";
import {useUserStore} from "@/store/modules/user";
import {router} from "@/router";

/**
 * Axios默认配置
 * 设置请求超时时间、基础URL、请求头等
 */
const defaultConfig: AxiosRequestConfig = {
    // 请求超时时间
    timeout: 30000,
    baseURL: import.meta.env.VITE_BASE_URL,
    headers: {},
    paramsSerializer: {
        serialize: stringify as unknown as CustomParamsSerializer
    }
};

/**
 * HTTP请求类
 * 封装Axios实例，提供统一的请求处理逻辑
 */
class Http {
    constructor() {
        this.httpInterceptorsRequest();
        this.httpInterceptorsResponse();
    }

    /** 初始化配置对象 */
    private static initConfig: PureHttpRequestConfig = {};

    /** 保存当前Axios实例对象 */
    private static axiosInstance: AxiosInstance = Axios.create(defaultConfig);

    /**
     * 请求拦截器
     * 在请求发送前自动添加Token等认证信息
     */
    private httpInterceptorsRequest(): void {
        Http.axiosInstance.interceptors.request.use(
            async (config: any) => {
                if (typeof config.beforeRequestCallback === "function") {
                    config.beforeRequestCallback(config);
                    return config;
                }
                if (Http.initConfig.beforeRequestCallback) {
                    Http.initConfig.beforeRequestCallback(config);
                    return config;
                }
                return new Promise(resolve => {
                    const data: any = getToken();
                    if (data) {
                        config.headers["Authorization"] = data.token;
                    }
                    resolve(config);
                });
            },
            error => {
                return Promise.reject(error);
            }
        );
    }

    /**
     * 响应拦截器
     * 统一处理响应数据和错误情况
     */
    private httpInterceptorsResponse(): void {
        const instance = Http.axiosInstance;
        instance.interceptors.response.use(
            (response) => {
                const $config = response.config as PureHttpRequestConfig;
                if (typeof $config.beforeResponseCallback === "function") {
                    $config.beforeResponseCallback(response);
                    return response;
                }
                if (Http.initConfig.beforeResponseCallback) {
                    Http.initConfig.beforeResponseCallback(response);
                    return response;
                }
                return response;
            },
            (error: AxiosResponse) => {
                return Promise.reject(error);
            }
        );
    }

    /**
     * 通用请求函数
     * @param method 请求方法（GET、POST、PUT、DELETE等）
     * @param url 请求URL
     * @param param 请求参数配置
     * @param axiosConfig 自定义Axios配置
     * @returns Promise<T> 返回请求结果
     */
    public request<T>(
        method: RequestMethods,
        url: string,
        param?: AxiosRequestConfig,
        axiosConfig?: PureHttpRequestConfig
    ): Promise<T> {
        const config = {
            method,
            url,
            ...param,
            ...axiosConfig
        } as PureHttpRequestConfig;
        // 单独处理自定义请求/响应回掉
        return new Promise((resolve, reject) => {
            Http.axiosInstance
                .request(config)
                .then((response: AxiosResponse<ResultType>) => {
                    const {code, data, msg} = response.data;
                    if(!code){
                        resolve(response.data as any);
                        return;
                    }
                    switch (code) {
                        case 200:
                            resolve(data);
                            break;
                        case 5001:
                            ElMessageBox.close();
                            ElMessageBox.confirm(
                                '登录已过期或登录信息不存在，请重新登录',
                                '提示',
                                {
                                    confirmButtonText: '重新登录',
                                    cancelButtonText: '取消',
                                    type: 'warning',
                                })
                                .then(async () => {
                                    await useUserStore().clear();
                                    await router.push({
                                        path:"/login",
                                        query: {
                                            redirect: router.currentRoute.value.fullPath,
                                        },
                                    });
                                }).catch(() => {
                            })
                            break;
                        default:
                            ElMessage.error(msg || '网络错误');
                            reject(response);
                    }
                })
                .catch(error => {
                    reject(error);
                });
        });
    }

    /**
     * POST请求方法
     * @param url 请求URL
     * @param data 请求数据
     * @param config 自定义配置
     * @returns Promise<P> 返回请求结果
     */
    public post<P>(
        url: string,
        data?: Record<string, any>,
        config?: PureHttpRequestConfig
    ): Promise<P> {
        return this.request<P>("post", url, {data}, config);
    }

    /**
     * GET请求方法
     * @param url 请求URL
     * @param params 查询参数
     * @param config 自定义配置
     * @returns Promise<P> 返回请求结果
     */
    public get<P>(
        url: string,
        params?: Record<string, any>,
        config?: PureHttpRequestConfig
    ): Promise<P> {
        return this.request<P>("get", url, {params}, config);
    }

    /**
     * PUT请求方法
     * @param url 请求URL
     * @param data 请求数据
     * @param config 自定义配置
     * @returns Promise<P> 返回请求结果
     */
    public put<P>(
        url: string,
        data?: Record<string, any>,
        config?: PureHttpRequestConfig
    ): Promise<P> {
        return this.request<P>("put", url, {data}, config);
    }

    /**
     * DELETE请求方法
     * @param url 请求URL
     * @param config 自定义配置
     * @returns Promise<P> 返回请求结果
     */
    public delete<P>(
        url: string,
        config?: PureHttpRequestConfig
    ): Promise<P> {
        return this.request<P>("delete", url, {}, config);
    }
}

/**
 * 导出HTTP实例
 * 供其他模块使用
 */
export const http = new Http();
