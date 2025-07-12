/**
 * 路由权限控制
 *
 */

import {router} from "@/router/index";
import type {NavigationGuardNext, RouteLocationNormalized} from "vue-router";
import {closeNProgress, startNProgress} from "@/utils/nprogress";
import {getToken} from "@/utils/auth";
import {useUserStoreHook} from "@/store/modules/user";
import {NextLoading} from '@/utils/loading';
import {ElMessage, ElMessageBox} from "element-plus";
import {initStaticRoutes} from "@/router/staticRoutes";

/** 路由白名单 */
const whiteList = ["/login","/404"];

router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    startNProgress();
    if (whiteList.includes(to.path)) {
        next();
    } else if (getToken()) {
        // 如果是首次访问或刷新页面，需要初始化路由
        if (!router.hasRoute('doctor')) {
            // 界面 loading 动画开始执行
            if (window.nextLoading === undefined) NextLoading.start();
            
            // 使用静态路由替代后端路由
            await initStaticRoutes();
            
            // 路由初始化完成后跳转
            next({ path: to.fullPath, replace: true });
            
            // 完成加载
            NextLoading.done(600);
        } else {
            next();
        }
    } else {
        next(`/login?redirect=${to.fullPath}`)
    }
})

router.afterEach(() => {
    closeNProgress();
});
