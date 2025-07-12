import { RouteRecordRaw } from 'vue-router';
import Layout from '@/layout/index.vue';

// 根路由
export const rootRoutes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'root',
    component: Layout,
    redirect: '/home',
    children: [],
  },
];

// 动态路由
export const dynamicRoutes: Array<RouteRecordRaw> = [
  {
    path: '/personal',
    component: Layout,
    redirect: '/personal/info',
    name: 'personal',
    meta: {
      title: '个人中心',
      isHide: true,
    },
    children: [
      {
        path: 'info',
        name: 'personalInfo',
        component: () => import('@/views/personal/info.vue'),
        meta: {
          title: '个人信息',
          isHide: true,
        },
      },
      {
        path: 'password',
        name: 'personalPassword',
        component: () => import('@/views/personal/password.vue'),
        meta: {
          title: '修改密码',
          isHide: true,
        },
      },
    ],
  },
];

// 404页面路由
export const notFoundRoutes: Array<RouteRecordRaw> = [
  {
    path: '/:path(.*)*',
    name: 'notFound',
    component: () => import('@/views/error/404.vue'),
    meta: {
      title: '找不到此页面',
      isHide: true,
    },
  },
]; 