/**
 * 用户状态管理模块
 * 负责管理用户认证、用户信息、菜单权限等状态
 */
import {defineStore} from "pinia";
import {store} from "@/store";
import {LoginParamsType, LoginUserInfoResult} from "@/api/types/systemTypes";
import {loginApi, logoutApi,getLoginUserInfoApi} from '@/api/system'
import {ElMessage} from "element-plus";
import {removeToken, setToken} from "@/utils/auth";
import {computeDate, formatGreet} from "@/utils";
import {router} from "@/router";
import {useTabsStore} from "@/store/modules/tabs";

/**
 * 用户状态接口
 */
interface userType {
    userinfo: LoginUserInfoResult,          // 用户信息
    menuList: Array<RouteConfigsTable>,     // 菜单列表
    isDemo: boolean                         // 是否演示模式
}

/**
 * 用户Store
 */
export const useUserStore = defineStore({
    id: 'user',
    
    /**
     * 状态定义
     * @returns 返回用户状态对象
     */
    state: (): userType => ({
        userinfo: {
            deptId: null,
            deptName: null,
            nickname: '',
            roleIds: [],
            roleNames: [],
            id: '',
            username: '',
            head:'',
            gender:0,
            email:'',
            phone:'',
            createTime:'',
            admin:false,
            permissions:[]
        },
        menuList: [],
        isDemo: false
    }),
    
    /**
     * 持久化配置
     * 将用户信息和演示模式状态持久化到localStorage
     */
    persist: {
        enabled: true,
        strategies: [
            {
                storage: localStorage,
                key: "userinfo",
                paths: ['userinfo', 'isDemo']
            },
        ]
    },
    
    /**
     * 动作方法
     */
    actions: {
        /**
         * 用户登录
         * 支持正常登录和演示模式登录
         * @param data 登录参数，包含用户名和密码
         * @returns 返回登录结果的Promise
         */
        async login(data: LoginParamsType) {
            return new Promise((resolve) => {
                // 尝试正常登录
                loginApi(data).then((res) => {
                    if (res) {
                        this.isDemo = false;
                        // 设置Token，有效期3天
                        setToken({
                            token: res.token,
                            expires: computeDate(3, 3)
                        });
                        // 获取用户信息并显示欢迎消息
                        this.getLoginUserInfo().then((userRes:any) => {
                            ElMessage.success(`${formatGreet(new Date())}，${userRes.nickname}`);
                            resolve(res);
                        });
                    }
                }).catch(() => {
                    // 演示模式：使用模拟数据
                    this.isDemo = true;
                    const token = "demo_" + Math.random().toString(36).substr(2);
                    setToken({
                        token: token,
                        expires: computeDate(1, 0)
                    });
                    
                    // 根据用户名设置不同角色
                    let isAdmin = data.username === 'admin';
                    let role = isAdmin ? '超级管理员' : '医院管理员';
                    
                    // 模拟用户数据
                    const demoUser: LoginUserInfoResult = {
                        deptId: null,
                        deptName: null,
                        nickname: isAdmin ? '系统管理员' : '医院管理员',
                        roleIds: isAdmin ? ['1'] : ['2'],
                        roleNames: [role],
                        id: isAdmin ? '1' : '2',
                        username: data.username,
                        head: null,
                        gender: 1,
                        email: 'demo@example.com',
                        phone: '13800138000',
                        createTime: new Date().toISOString(),
                        admin: isAdmin,
                        permissions: []
                    };
                    
                    this.userinfo = demoUser;
                    ElMessage.success(`${formatGreet(new Date())}，${demoUser.nickname}（演示模式）`);
                    resolve({token});
                });
            })
        },
        /**
         * 获取登录用户信息
         * 支持正常模式和演示模式
         * @returns 返回用户信息的Promise
         */
        async getLoginUserInfo() {
            return new Promise((resolve) => {
                if (this.isDemo) {
                    // 演示模式下，直接返回已有的用户信息
                    resolve(this.userinfo);
                    return;
                }
                // 调用API获取用户信息
                getLoginUserInfoApi().then((res) => {
                    this.userinfo = res;
                    resolve(res);
                }).catch(() => {
                    // API调用失败时，切换到演示模式
                    this.isDemo = true;
                    ElMessage.warning('切换到演示模式');
                    resolve(this.userinfo);
                });
            })
        },

        /**
         * 清除登录相关信息
         * 清除Token、重置状态、清除本地存储
         */
        async clear(){
            removeToken();
            this.$reset();
            useTabsStore().$reset();
            localStorage.removeItem('userinfo');
            localStorage.removeItem('tabsList');
        },

        /**
         * 用户登出
         * 调用登出API并清除本地数据，跳转到登录页面
         */
        async logout() {
            if (this.isDemo) {
                // 演示模式下直接清除数据并跳转
                this.clear();
                router.push("/login");
                return;
            }
            
            // 调用登出API
            logoutApi().then(() => {
                this.clear();
                router.push("/login");
            }).catch(() => {
                // API调用失败时也清除数据并跳转
                this.clear();
                router.push("/login");
            });
        },

        /**
         * 设置菜单路由
         * 设置用户可访问的菜单列表
         * @param data 菜单路由数据
         */
        setMenu(data: Array<RouteConfigsTable>) {
            this.menuList = data;
        },
    }
});

/**
 * 获取用户Store实例的Hook函数
 * @returns 返回用户Store实例
 */
export function useUserStoreHook() {
    return useUserStore(store);
}
