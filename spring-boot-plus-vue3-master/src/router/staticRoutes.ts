import { RouteRecordRaw } from 'vue-router';
import { router } from '@/router/index';
import { dynamicRoutes, notFoundRoutes } from '@/router/route';
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
    
    // 添加路由
    await addStaticRoutes([...doctorRoutes, ...dynamicRoutes]);
    
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

  // 添加路由
  routes.forEach((route: RouteRecordRaw) => {
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
    // 保留基础路由，不重置
    if (name !== 'Login' && name !== '404' && name !== undefined) {
      router.hasRoute(name as string) && router.removeRoute(name as string);
    }
  });
} 