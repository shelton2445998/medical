/**
 * 医生端路由配置
 */
import { RouteRecordRaw } from 'vue-router';

// 布局组件
const Layout = () => import('@/layout/index.vue');

// 医生端路由
export const doctorRoutes: Array<RouteRecordRaw> = [
  {
    path: '/doctor',
    name: 'doctor',
    component: Layout,
    redirect: '/doctor/home',
    meta: {
      title: '医生工作站',
      icon: 'ele-MedicalFilled',
      isAffix: false,
      isKeepAlive: true,
    },
    children: [
      {
        path: 'home',
        name: 'DoctorHome',
        component: () => import('@/views/home/index.vue'),
        meta: {
          title: '首页',
          icon: 'ele-HomeFilled',
          isAffix: true,
          isKeepAlive: true,
        },
      },
      {
        path: 'patient',
        name: 'DoctorPatient',
        component: () => import('@/views/doctor/patient/index.vue'),
        meta: {
          title: '患者管理',
          icon: 'ele-User',
          isKeepAlive: true,
        },
      },
      {
        path: 'appointment',
        name: 'DoctorAppointment',
        component: () => import('@/views/doctor/appointment/index.vue'),
        meta: {
          title: '预约管理',
          icon: 'ele-Calendar',
          isKeepAlive: true,
        },
      },
      {
        path: 'report',
        name: 'DoctorReport',
        component: () => import('@/views/doctor/report/index.vue'),
        meta: {
          title: '体检报告',
          icon: 'ele-Document',
          isKeepAlive: true,
        },
      },
      {
        path: 'report/input/:id',
        name: 'DoctorReportInput',
        component: () => import('@/views/doctor/report/input.vue'),
        meta: {
          title: '体检结果录入',
          icon: 'ele-EditPen',
          isHide: true,
          isKeepAlive: false,
          activeMenu: '/doctor/report',
        },
      },
      {
        path: 'report/detail/:id',
        name: 'DoctorReportDetail',
        component: () => import('@/views/doctor/report/detail.vue'),
        meta: {
          title: '体检报告详情',
          icon: 'ele-Document',
          isHide: true,
          isKeepAlive: false,
          activeMenu: '/doctor/report',
        },
      },
      {
        path: 'profile',
        name: 'DoctorProfile',
        component: () => import('@/views/doctor/profile/index.vue'),
        meta: {
          title: '个人信息',
          icon: 'ele-UserFilled',
          isKeepAlive: true,
        },
      },
    ],
  },
]; 