import { defineStore } from 'pinia';

/**
 * 路由列表
 * @methods setColumnsMenuHover 设置分栏布局菜单鼠标移入 boolean
 * @methods setColumnsNavHover 设置分栏布局最左侧导航鼠标移入 boolean
 */
export const useRoutesList = defineStore({
  id: 'routesList',
  state: () => ({
    // 路由列表
    routesList: [],
    // 分栏布局菜单鼠标移入
    isColumnsMenuHover: false,
    // 分栏布局最左侧导航鼠标移入
    isColumnsNavHover: false,
  }),
  actions: {
    // 设置分栏布局菜单鼠标移入
    async setColumnsMenuHover(bool: boolean) {
      this.isColumnsMenuHover = bool;
    },
    // 设置分栏布局最左侧导航鼠标移入
    async setColumnsNavHover(bool: boolean) {
      this.isColumnsNavHover = bool;
    },
  },
}); 