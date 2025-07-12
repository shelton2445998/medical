// @ts-ignore
import type {App} from 'vue';
import {createRouter, createWebHashHistory, Router, RouteRecordRaw} from 'vue-router';
import basicRoutes from "@/router/basic";

export const router: Router = createRouter({
    history: createWebHashHistory(),
    routes: [...basicRoutes] as RouteRecordRaw[]
})

/**
 * 扁平化路由
 * @param routes 需要扁平化的路由
 * @returns 扁平化后的路由
 */
export function formatFlatteningRoutes(routes: RouteRecordRaw[]) {
    const flattenRoutes: RouteRecordRaw[] = [];
    routes.forEach((route) => {
        if (route.children) {
            flattenRoutes.push(route);
            flattenRoutes.push(...formatFlatteningRoutes(route.children));
            route.children = [];
        } else {
            flattenRoutes.push(route);
        }
    });
    return flattenRoutes;
}

/**
 * 格式化为二级路由
 * @param routes 需要格式化的路由
 * @returns 格式化后的路由
 */
export function formatTwoStageRoutes(routes: RouteRecordRaw[]) {
    const twoStageRoutes: RouteRecordRaw[] = [];
    routes.forEach((route) => {
        if (route.path === '/') {
            twoStageRoutes.push(route);
        } else {
            twoStageRoutes.push({
                path: route.path,
                component: route.component,
                name: route.name,
                meta: route.meta,
                redirect: route.redirect,
                children: route.children,
            });
        }
    });
    return twoStageRoutes;
}

export function setupRouter(app: App<Element>) {
    app.use(router);
}
