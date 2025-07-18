/**
 * 全局配置状态管理模块
 * 负责管理应用的全局配置，包括侧边栏状态、主题色、布局模式等
 */
import {defineStore} from "pinia";
import {store} from "@/store";
import {configStoreType,configType} from "@/store/types";

/**
 * 全局配置Store
 */
export const useConfigStore = defineStore({
    id: 'config',
    
    /**
     * 状态定义
     * @returns 返回配置状态对象
     */
    state: (): configStoreType => ({
        configure: {
            isCollapse: false,              // 侧边栏是否折叠
            themeColor: '#2e5cf6',          // 主题色
            sidebarBgColor:'#f00',          // 侧边栏背景色
            isGrey:false,                   // 是否灰色模式
            isDark:false,                   // 是否暗色模式
            isWeakness:false,               // 是否色弱模式
            locale:'zh',                    // 语言环境
            layoutMode:0,                   // 布局模式
        }
    }),
    
    /**
     * 计算属性
     */
    getters: {
        /**
         * 获取侧边栏状态
         * @returns 返回侧边栏是否折叠
         */
        getSidebarStatus(): boolean {
            return this.configure.isCollapse;
        },
    },
    
    /**
     * 动作方法
     */
    actions: {
        /**
         * 切换菜单展开状态
         * 切换侧边栏的折叠/展开状态
         */
        toggleSidebar(): void {
            this.storageConfigureChange('isCollapse',!this.configure.isCollapse);
        },
        
        /**
         * 修改全局配置
         * @param key 配置项的键名
         * @param val 配置项的值
         */
        storageConfigureChange<T>(key: keyof configType, val: T): void {
            // @ts-ignore
            this.configure[key] = val;
            // 将配置保存到本地存储
            localStorage.setItem('configure', JSON.stringify(this.configure))
        },
        
        /**
         * 获取本地全局配置
         * 从本地存储中恢复配置
         */
        setConfigure(){
            this.configure=JSON.parse(localStorage.getItem('configure') as string);;
        },
    }
});

/**
 * 获取配置Store实例的Hook函数
 * @returns 返回配置Store实例
 */
export function useConfigStoreHook() {
    return useConfigStore(store);
}
