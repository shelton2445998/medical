import {defineStore} from "pinia";
import {store} from "@/store";
import {LoginParamsType, LoginUserInfoResult} from "@/api/types/systemTypes";
import {loginApi, logoutApi,getLoginUserInfoApi} from '@/api/system'
import {ElMessage} from "element-plus";
import {removeToken, setToken} from "@/utils/auth";
import {computeDate, formatGreet} from "@/utils";
import {router} from "@/router";
import {useTabsStore} from "@/store/modules/tabs";

interface userType {
    userinfo: LoginUserInfoResult,
    menuList: Array<RouteConfigsTable>,
    isDemo: boolean
}

export const useUserStore = defineStore({
    id: 'user',
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
    actions: {
        /**
         * @description:登录
         * */
        async login(data: LoginParamsType) {
            return new Promise((resolve) => {
                // 尝试正常登录
                loginApi(data).then((res) => {
                    if (res) {
                        this.isDemo = false;
                        setToken({
                            token: res.token,
                            expires: computeDate(3, 3)
                        });
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
         * @description:获取登录用户信息
         * */
        async getLoginUserInfo() {
            return new Promise((resolve) => {
                if (this.isDemo) {
                    // 演示模式下，直接返回已有的用户信息
                    resolve(this.userinfo);
                    return;
                }
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

        // 清除登录修改信息
        async clear(){
            removeToken();
            this.$reset();
            useTabsStore().$reset();
            localStorage.removeItem('userinfo');
            localStorage.removeItem('tabsList');
        },

        /**
         * @description:退出登录
         * */
        async logout() {
            if (this.isDemo) {
                // 演示模式下直接清除数据并跳转
                this.clear();
                router.push("/login");
                return;
            }
            
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
         * @description:设置菜单路由
         * */
        setMenu(data: Array<RouteConfigsTable>) {
            this.menuList = data;
        },
    }
});

export function useUserStoreHook() {
    return useUserStore(store);
}
