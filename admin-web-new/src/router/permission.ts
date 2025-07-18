/**
 * 路由权限控制模块
 * 实现路由级别的权限验证和访问控制
 */

import {router} from "@/router/index";
import type {NavigationGuardNext, RouteLocationNormalized} from "vue-router";
import {closeNProgress, startNProgress} from "@/utils/nprogress";
import {getToken} from "@/utils/auth";
import {initBackEndControlRoutes} from "@/router/backEnd"
import {useUserStoreHook} from "@/store/modules/user";
import {NextLoading} from '@/utils/loading';
import {ElMessage, ElMessageBox} from "element-plus";

/**
 * 路由白名单
 * 这些路由不需要进行权限验证
 */
const whiteList = ["/login","/404"];

/**
 * 路由前置守卫
 * 在每次路由跳转前进行权限验证
 */
router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    // 开始进度条
    startNProgress();
    
    // 检查是否在白名单中
    if (whiteList.includes(to.path)) {
        next();
    } else if (getToken()) {
        // 已登录状态
        if (useUserStoreHook().menuList.length > 0) {
            // 已有菜单数据，直接放行
            next();
        } else {
            // 没有菜单数据，需要初始化后端控制的路由
            // 开始界面loading动画
            if (window.nextLoading === undefined) NextLoading.start();
            
            // 初始化后端控制的路由
            const routers = await initBackEndControlRoutes();
            
            if (routers.length > 0) {
                // 路由初始化成功，重新跳转到目标路由
                next({path: to.path, query: to.query});
            } else {
                // 权限不足，显示提示信息
                ElMessageBox.confirm(
                    '当前账号权限不足，请联系管理员分配权限。',
                    '提示',
                    {
                        confirmButtonText: '确定',
                        showCancelButton:false,
                        type: 'warning',
                    })
                    .then(async () => {
                        // 确定按钮回调
                    }).catch(() => {
                        // 取消按钮回调
                    })
                
                // 结束loading动画
                NextLoading.done(600);
                // 跳转到登录页面
                next(`/login`)
            }
        }
    } else {
        // 未登录状态，跳转到登录页面并保存当前路由
        next(`/login?redirect=${to.fullPath}`)
    }
})

/**
 * 路由后置守卫
 * 在每次路由跳转后执行清理工作
 */
router.afterEach(() => {
    // 关闭进度条
    closeNProgress();
});
