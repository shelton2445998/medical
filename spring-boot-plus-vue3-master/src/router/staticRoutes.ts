import { RouteRecordRaw } from 'vue-router';
import { formatFlatteningRoutes, formatTwoStageRoutes, router } from '@/router/index';
import { dynamicRoutes, notFoundRoutes, rootRoutes } from '@/router/route';
import { storeToRefs } from 'pinia';
import { useRoutesList } from '@/store/modules/routesList';
import { useUserStore } from '@/store/modules/user';
import { doctorRoutes } from './modules/doctorRoutes';

/**
 * 初始化静态路由
 */
export async function initStaticRoutes() {
  try {
    // 保存到用户菜单
    useUserStore().setMenu(doctorRoutes);

    // 设置根路由的子路由
    rootRoutes[0].children = doctorRoutes[0].children;
    
    // 添加路由
    await addStaticRoutes([...rootRoutes, ...dynamicRoutes]);
    
    return Promise.resolve(doctorRoutes);
  } catch (error) {
    console.error("初始化静态路由失败:", error);
    return Promise.reject(error);
  }
}

/**
 * 添加静态路由
 */
const addStaticRoutes = async (routes: Array<RouteRecordRaw>) => {
  const { isColumnsMenuHover } = storeToRefs(useRoutesList());
  
  // 重置路由
  await resetRouter();
  
  isColumnsMenuHover.value = true;

  const finallyRoutes = formatTwoStageRoutes(formatFlatteningRoutes(routes));
  
  // 添加路由
  finallyRoutes.forEach((route: RouteRecordRaw) => {
    router.addRoute(route);
  });

  // 添加404路由
  notFoundRoutes.forEach((route: RouteRecordRaw) => {
    router.addRoute(route);
  });
};

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