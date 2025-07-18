/**
 * 路由配置文件
 * 负责创建和配置Vue Router路由系统
 */
// @ts-ignore
import type {App} from 'vue';
import {createRouter, createWebHashHistory} from "vue-router";
import basicRoutes from './basic'

/**
 * 创建路由器实例
 * 使用Hash模式路由，包含基础路由配置
 */
export const router = createRouter({
    // 使用Hash模式路由（URL包含#）
    history: createWebHashHistory(),
    // 合并所有路由配置
    routes: [...basicRoutes],
    // 严格模式，确保路由匹配的准确性
    strict: true,
});
