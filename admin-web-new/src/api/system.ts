import {http} from '@/utils/http'
import {LoginParamsType,LoginResult,LoginUserInfoResult} from './types/systemTypes'

enum Api {
    Login = '/admin/login',
    Logout = '/admin/logout',
    GetProfile = '/admin/info',  // 修改为正确的后端API路径
}

/** 登录*/
export function loginApi(data:LoginParamsType) {
    return http.post<LoginResult>(Api.Login,data)
}

/** 获取登录用户信息*/
export function getLoginUserInfoApi() {
    return http.get<LoginUserInfoResult>(Api.GetProfile)  // 修改为GET请求
}

/** 退出登录*/
export function logoutApi() {
    return http.post(Api.Logout)
}

