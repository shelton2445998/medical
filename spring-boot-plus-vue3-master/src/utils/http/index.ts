import Axios, {AxiosInstance, AxiosRequestConfig, AxiosResponse, CustomParamsSerializer} from 'axios'
import {stringify} from 'qs'
import {ElMessage, ElMessageBox} from "element-plus";
import {getToken} from "@/utils/auth";

import {PureHttpRequestConfig, RequestMethods} from "./types.d";
import {ResultType} from "@/api/types";
import {useUserStore} from "@/store/modules/user";
import {router} from "@/router";

const defaultConfig: AxiosRequestConfig = {
    // 请求超时时间
    timeout: 30000,
    baseURL: import.meta.env.VITE_BASE_URL,
    headers: {},
    paramsSerializer: {
        serialize: stringify as unknown as CustomParamsSerializer
    }
};

class Http {
    constructor() {
        this.httpInterceptorsRequest();
        this.httpInterceptorsResponse();
    }

    /** 初始化配置对象 */
    private static initConfig: PureHttpRequestConfig = {};

    /** 保存当前Axios实例对象 */
    private static axiosInstance: AxiosInstance = Axios.create(defaultConfig);

    /** 请求拦截 */
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
                
                // 医生端禁止访问/admin开头的API
                const url = config.url || '';
                if (url.startsWith('/admin')) {
                    console.error('医生端不允许访问管理员API:', url);
                    return Promise.reject(new Error('医生端不允许访问管理员API'));
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

    /** 响应拦截 */
    private httpInterceptorsResponse(): void {
        Http.axiosInstance.interceptors.response.use(
            (response: AxiosResponse) => {
                const $config = response.config;
                // 处理响应
                return response;
            },
            (error) => {
                const $error = error;
                $error.isCancelRequest = Axios.isCancel($error);
                return Promise.reject($error);
            }
        );
    }

    /** 通用请求工具函数 */
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

        // 单独处理自定义请求/响应回调
        return new Promise((resolve, reject) => {
            Http.axiosInstance
                .request(config)
                .then((response: AxiosResponse<ResultType>) => {
                    const data = response.data;
                    const code = data.code;
                    const msg = data.msg;
                    switch (code) {
                        case 200:
                            resolve(data as unknown as T);
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

    /** 单独抽离的post工具函数 */
    public post<T>(
        url: string,
        params?: any,
        config?: PureHttpRequestConfig
    ): Promise<T> {
        return this.request<T>("post", url, {data: params}, config);
    }

    /** 单独抽离的get工具函数 */
    public get<T>(
        url: string,
        params?: any,
        config?: PureHttpRequestConfig
    ): Promise<T> {
        return this.request<T>("get", url, {params}, config);
    }
}

export const http = new Http();
