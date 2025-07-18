/**
 * 医疗管理路由模块
 * 定义医疗管理相关的路由配置，包括套餐管理、项目详情、医生管理等
 */
import { RouteRecordRaw } from 'vue-router'

const Layout = () => import('@/layout/index.vue')

/**
 * 医疗模块路由配置
 * 包含医疗业务相关的所有页面路由
 */
const medicalRoutes = {
  path: '/medical',
  name: 'Medical',
  component: Layout,
  redirect: '/medical/setmeal',
  meta: {
    title: '医疗管理',
    icon: 'el-icon-s-tools',
    isShow: true
  },
  children: [
    {
      path: 'setmeal',
      name: 'Setmeal',
      component: () => import('@/views/medical/setmeal/index.vue'),
      meta: {
        title: '套餐管理',
        icon: 'el-icon-s-management',
        isShow: true
      }
    },
    {
      path: 'setmeal-detail',
      name: 'SetmealDetail',
      component: () => import('@/views/medical/setmeal-detail/index.vue'),
      meta: {
        title: '套餐详情',
        icon: 'el-icon-s-order',
        isShow: true
      }
    },
    {
      path: 'checkitem',
      name: 'Checkitem',
      component: () => import('@/views/medical/checkitem/index.vue'),
      meta: {
        title: '项目详情',
        icon: 'el-icon-s-order',
        isShow: true
      }
    },
    {
      path: 'doctor',
      name: 'Doctor',
      component: () => import('@/views/medical/doctor/index.vue'),
      meta: {
        title: '医生管理',
        icon: 'el-icon-user',
        isShow: true
      }
    }
  ]
} as RouteConfigsTable;

export default medicalRoutes 