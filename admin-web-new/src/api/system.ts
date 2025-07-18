/**
 * 系统认证API接口模块
 * 提供登录、登出、用户信息获取等系统认证功能
 */
import {http} from '@/utils/http'
import {LoginParamsType,LoginResult,LoginUserInfoResult} from './types/systemTypes'

/**
 * 系统认证API接口枚举
 * 定义所有系统认证相关的API端点
 */
enum Api {
    Login = '/admin/login',      // 用户登录接口
    Logout = '/admin/logout',    // 用户登出接口
    GetProfile = '/admin/info',  // 获取用户信息接口
}

/**
 * 用户登录接口
 * @param data 登录参数，包含用户名和密码
 * @returns 返回登录结果，包含Token等信息
 */
export function loginApi(data:LoginParamsType) {
    return http.post<LoginResult>(Api.Login,data)
}

/**
 * 获取当前登录用户信息
 * @returns 返回当前登录用户的详细信息
 */
export function getLoginUserInfoApi() {
    return http.get<LoginUserInfoResult>(Api.GetProfile)
}

/**
 * 用户登出接口
 * @returns 返回登出操作结果
 */
export function logoutApi() {
    return http.post(Api.Logout)
}

