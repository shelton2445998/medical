/**
 * 标签页状态管理模块
 * 负责管理浏览器标签页的状态，包括标签页的添加、关闭、切换等操作
 */
import {defineStore} from "pinia";
import {store} from "@/store";
import {router} from "@/router";

/**
 * 标签页状态接口
 */
interface TabsViewState {
    /** 标签页列表 */
    tabsList: any[];
}

/**
 * 不需要出现在标签页中的路由黑名单
 * 这些路由不会被添加到标签页列表中
 */
export const blackList = ['404'] as const;

/**
 * 标签页Store
 */
export const useTabsStore = defineStore({
    id: 'tabs',
    
    /**
     * 状态定义
     * @returns 返回标签页状态对象
     */
    state: (): TabsViewState => ({
        tabsList: [],
    }),
    
    /**
     * 持久化配置
     * 将标签页状态持久化到localStorage
     */
    persist: {
        enabled: true,
        strategies: [
            {
                storage: localStorage,
                key: "tabsList",
                paths: ['tabsList']
            },
        ]
    },
    
    /**
     * 计算属性
     */
    getters: {
        /**
         * 获取过滤后的标签页列表
         * 过滤掉隐藏的标签页，并按sort字段排序
         * @param state 状态对象
         * @returns 返回过滤并排序后的标签页列表
         */
        getTabsList: (state) => {
            console.log(state.tabsList);
            return state.tabsList.filter((item:any) => {
                return item.meta?.isShow;
            }).sort((a:{meta:{sort:number}},b:{meta:{sort:number}})=>{
                return (a.meta.sort||0)-(b.meta.sort||0);
            });
        },
        
        /**
         * 获取当前活动的标签页
         * 根据当前路由匹配对应的标签页
         * @param state 状态对象
         * @returns 返回当前活动的标签页
         */
        getCurrentTab: (state) => {
            const currentRoute = router.currentRoute.value!;
            return state.tabsList.find((item:any) => {
                return item.meta?.isShow && item.fullPath === currentRoute.fullPath;
            });
        },
    },
    
    /**
     * 动作方法
     */
    actions: {
        /**
         * 初始化标签页
         * 使用路由数据初始化标签页列表
         * @param routes 路由数据
         */
        initTabs(routes: any) {
            this.tabsList = routes;
        },
        
        /**
         * 添加标签页
         * 添加新的标签页到列表中，如果已存在则不添加
         * @param route 路由对象
         * @returns 返回是否添加成功
         */
        addTabs(route: any): boolean {
            // 检查是否在黑名单中
            if (blackList.includes(route.name)) return false;
            // 检查是否已存在
            const isExists = this.tabsList.some((item:any) => item.fullPath == route.fullPath);
            if (!isExists) {
                this.tabsList.push(route);
            }
            return true;
        },
        
        /**
         * 关闭当前标签页
         * 关闭指定名称的标签页，如果是当前活动标签页则重定向到其他页面
         * @param name 标签页名称
         */
        closeTab(name: string) {
            const index = this.tabsList.findIndex((item:any) => item.name == name);
            const isDelCurrentTab = Object.is(this.getCurrentTab, this.tabsList[index]);
            this.tabsList.splice(index, 1);
            // 如果关闭的tab就是当前激活的tab，则重定向页面
            if (isDelCurrentTab) {
                const currentRoute = this.tabsList[Math.max(0, this.tabsList.length - 1)];
                router.push(currentRoute.fullPath);
            }
        },
        
        /**
         * 关闭左侧标签页
         * 关闭指定标签页左侧的所有标签页，保留首页
         * @param route 路由对象
         */
        closeLeftTabs(route: any) {
            const Home=this.tabsList.find((item:any) => item.meta.isHome);
            const index = this.tabsList.findIndex((item:any) => item.name == route.name);
            if(index!=0){
                this.tabsList.splice(0, index)
                Home&&this.tabsList.unshift(Home)
            }
        },
        
        /**
         * 关闭右侧标签页
         * 关闭指定标签页右侧的所有标签页
         * @param route 路由对象
         */
        closeRightTabs(route: any) {
            const index = this.tabsList.findIndex((item:any) => item.name == route.name);
            this.tabsList.splice(index + 1)
        },
        
        /**
         * 关闭其他标签页
         * 关闭除指定标签页和首页外的所有标签页
         * @param route 路由对象
         */
        closeOtherTabs(route: any) {
            const Home=this.tabsList.find((item:any) => item.meta.isHome);
            const index = this.tabsList.findIndex((item:any) => item.name == route.name);
            if (index !== -1) {
                this.tabsList = this.tabsList.splice(index, 1);
                if(index!=0){
                    this.tabsList.unshift(Home)
                }
            }
        },
        
        /**
         * 关闭全部标签页
         * 关闭所有标签页，只保留首页
         */
        closeAllTabs() {
            const Home=this.tabsList.find((item:any) => item.meta.isHome);
            this.tabsList = [Home];
        },
    }
});

/**
 * 获取标签页Store实例的Hook函数
 * @returns 返回标签页Store实例
 */
export function useTabsStoreHook() {
    return useTabsStore(store);
}
