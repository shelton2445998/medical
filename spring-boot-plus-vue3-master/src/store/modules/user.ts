import {defineStore} from 'pinia'
import {GetLoginProfileResult} from "@/api/types/userTypes"
import {LoginParamsType} from "@/api/types/systemTypes"
import {ElMessage} from "element-plus"
import {setToken} from "@/utils/auth"
import {formatGreet} from "@/utils/index"
import {computeDate} from "@/utils/tools"
// 导入新的医生端API
import {
    loginApi,
    getProfileApi,
    logoutApi,
    updatePasswordApi,
    updateProfileApi
} from '@/api/doctor'

export interface userType {
    userinfo: GetLoginProfileResult;
    menuList: Array<any>
}

export const useUserStore = defineStore({
    id: 'user',
    state: (): userType => ({
        userinfo: {
            id: '',
            name: '',
            mobile: '',
            email: '',
            gender: 0,
            hospitalId: null,
            hospitalName: null,
            departmentId: null,
            departmentName: null,
            title: '',
            introduction: '',
            avatarUrl: '',
            status: true,
            createTime: '',
            permissions: []
        },
        menuList: []
    }),
    persist: {
        enabled: true,
        strategies: [
            {
                storage: localStorage,
                key: "userinfo",
                paths: ['userinfo']
            },
        ]
    },
    actions: {
        /**
         * @description:登录
         * */
        async login(data: LoginParamsType) {
            return new Promise((resolve) => {
                // 使用医生API登录
                loginApi(data).then((res) => {
                    if (res) {
                        setToken({
                            token: res.token,
                            expires: computeDate(3, 3)
                        });
                        this.getLoginUserInfo().then((userRes:any) => {
                            ElMessage.success(`${formatGreet(new Date())}，${userRes.name}`);
                            resolve(res);
                        });
                    }
                })
            })
        },
        /**
         * @description:获取登录用户信息
         * */
        async getLoginUserInfo() {
            return new Promise((resolve) => {
                // 使用医生API获取个人信息
                getProfileApi().then((res) => {
                    if (res) {
                        this.userinfo = res;
                        this.menuList = []
                        resolve(res);
                    }
                })
            })
        },

        /**
         * @description:登出
         * */
        async logout() {
            return new Promise((resolve) => {
                // 使用医生API登出
                logoutApi().then((res) => {
                    if (res) {
                        resolve(res);
                    }
                })
            })
        },

        /**
         * @description:清除用户信息
         * */
        clear() {
            localStorage.clear();
            sessionStorage.clear();
        },

        /**
         * @description:设置菜单信息
         * */
        setMenu(data: Array<any>) {
            this.menuList = data
        }
    }
})

// 方便外部使用
export function useUserStoreHook() {
    return useUserStore()
}
