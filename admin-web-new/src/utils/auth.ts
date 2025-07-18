/**
 * 认证工具模块
 * 负责用户Token的存储、获取和管理
 */
import Cookies from "js-cookie";

/**
 * 数据信息接口
 * 定义Token相关数据的结构
 */
export interface DataInfo<T> {
    /** 访问令牌 */
    token: string;
    /** Token的过期时间（时间戳） */
    expires: T;
}

/** Token在Cookie中的存储键名 */
export const TokenKey = "accessToken";

/**
 * 获取Token信息
 * @returns 返回Token和过期时间信息
 */
export function getToken(): DataInfo<number> {
    return Cookies.get(TokenKey) && JSON.parse(<string>Cookies.get(TokenKey));
}

/**
 * 设置Token以及过期时间
 * @param data Token数据，包含token和过期时间
 */
export function setToken(data: DataInfo<Date|string>) {
    let expires = 0;
    const {token} = data;
    // 将过期时间转换为时间戳
    expires = new Date(data.expires).getTime();
    // 将Token信息序列化为JSON字符串
    const cookieString = JSON.stringify({token, expires});
    // 根据过期时间设置Cookie
    expires > 0
        ? Cookies.set(TokenKey, cookieString, {
            // 计算剩余天数
            expires: (expires - Date.now()) / 86400000
        })
        : Cookies.set(TokenKey, cookieString);
}

/**
 * 删除Token
 * 清除本地存储的Token信息
 */
export function removeToken() {
    Cookies.remove(TokenKey);
}
