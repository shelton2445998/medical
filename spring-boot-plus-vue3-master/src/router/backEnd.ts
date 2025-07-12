import { RouteRecordRaw } from 'vue-router';
import { storeToRefs } from 'pinia';
import { formatFlatteningRoutes, formatTwoStageRoutes, router } from '@/router/index';
import { dynamicRoutes, notFoundRoutes, rootRoutes } from '@/router/route';
import * as pinia from '@/store/index';
import { useUserStore } from '@/store/modules/user';
import { useRoutesList } from '@/store/modules/routesList';
import { NextLoading } from '@/utils/loading';

// 导入医生API获取菜单
import { getNavMenuTreeListApi } from '@/api/doctor';

// 定义的一些类型
interface RouteItem {
    componentPath: string;
    icon: string;
    id: string;
    isCache: boolean;
    isHome: boolean;
    isShow: boolean;
    name: string;
    parentId: string;
    routeName: string;
    routeUrl: string;
    redirect?: string;
    component?: any;
    children?: RouteItem[];
}

/**
 * 加载组件的函数
 * @param componentPath 组件路径
 * @returns 动态导入的组件
 */
const loadComponent = (componentPath: string) => {
    // 预定义一些常用组件的映射
    const componentMap: Record<string, any> = {
        'home/index': () => import('../views/home/index.vue'),
        // 只保留已确认存在的组件
    };
    
    // 如果有预定义的组件，直接返回
    if (componentMap[componentPath]) {
        return componentMap[componentPath];
    }
    
    // 否则尝试动态导入
    try {
        console.log(`尝试动态导入组件: ${componentPath}`);
        // 使用更安全的方式处理动态导入
        return () => {
            console.log(`正在加载组件: ${componentPath}`);
            try {
                // 使用相对路径，避免使用变量部分作为路径的开头
                if (componentPath.startsWith('/')) {
                    componentPath = componentPath.substring(1);
                }
                // 先检查是否存在于views目录下
                try {
                    return import(`../views/${componentPath}.vue`);
                } catch (e) {
                    console.warn(`组件不在views目录下: ${componentPath}`, e);
                    // 如果不在views目录下，尝试直接导入
                    return import(`../${componentPath}.vue`);
                }
            } catch (error) {
                console.error(`加载组件失败: ${componentPath}`, error);
                // 如果加载失败，返回404页面
                return import('../views/error/404.vue');
            }
        };
    } catch (error) {
        console.error(`组件路径解析失败: ${componentPath}`, error);
        return () => import('../views/error/404.vue');
    }
};

/**
 * 获取组件
 * @param {*} component 
 * @returns 
 */
const backEndComponent = (routes: any): any => {
    if (!routes) return;
    return routes.map((item: RouteItem) => {
        if (item.componentPath) {
            item.component = loadComponent(item.componentPath);
        }
        return item;
    });
}

/**
 * 把后台路由转换成前端路由
 * @param {*} routes 
 * @returns 
 */
const formatRoute = (routes: RouteItem[]): any => {
    if (!routes) return;
    return routes.map((item: RouteItem) => {
        return {
            path: item.routeUrl,
            name: item.routeName,
            component: item.componentPath ? loadComponent(item.componentPath) : null,
            meta: {
                title: item.name,
                icon: item.icon,
                isHide: !item.isShow,
                isKeepAlive: !!item.isCache,
                isAffix: !!item.isHome,
            },
            redirect: item.redirect || '',
            children: item.children ? formatRoute(item.children) : []
        }
    });
}

/**
 * 后端控制路由：初始化方法，防止刷新时路由丢失
 */
export async function initBackEndControlRoutes() {
    try {
        // 获取路由菜单数据 - 使用医生API
        const response = await getNavMenuTreeListApi({});
        
        // 确保我们处理的是数组
        let menuList = [];
        if (response && response.data) {
            // 如果response.data是数组，直接使用
            if (Array.isArray(response.data)) {
                menuList = response.data;
            } 
            // 如果response.data不是数组但有list属性，使用list
            else if (response.data.list && Array.isArray(response.data.list)) {
                menuList = response.data.list;
            }
            // 如果response.data是单个对象，放入数组
            else if (typeof response.data === 'object') {
                menuList = [response.data];
            }
        }
        
        // 添加首页
        menuList.unshift({
            componentPath: "home/index",
            icon: "ele-house",
            id: "0",
            isCache: true,
            isHome: true,
            isShow: true,
            name: "首页",
            parentId: "0",
            routeName: "Home",
            routeUrl: "/home",
        });
        
        console.log("菜单列表:", menuList);
        const result = backEndComponent(formatRoute(menuList));
        useUserStore().setMenu(result);
        rootRoutes[0].children = result;
        await addDynamicRoutes(rootRoutes);
        return Promise.resolve(menuList);
    } catch (error) {
        console.error("初始化路由失败:", error);
        return Promise.reject(error);
    }
}

/**
 * 添加动态路由
 */
const addDynamicRoutes = async (routes: Array<RouteRecordRaw>) => {
    const { isColumnsMenuHover } = storeToRefs(useRoutesList());
    await resetRouter();
    isColumnsMenuHover.value = true;

    const finallyRoutes = formatTwoStageRoutes(formatFlatteningRoutes(routes));
    
    // 添加动态路由
    finallyRoutes.forEach((route: RouteRecordRaw) => {
        router.addRoute(route);
    });

    // 添加404路由
    notFoundRoutes.forEach((route: RouteRecordRaw) => {
        router.addRoute(route);
    });
}

/**
 * 重置路由
 */
export async function resetRouter() {
    const routes = router.getRoutes();
    routes.forEach((route) => {
        const { name } = route;
        router.hasRoute(name as string) && router.removeRoute(name as string);
    });
}
