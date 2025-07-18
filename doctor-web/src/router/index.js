/**
 * 路由配置文件
 * 
 * 定义医生端应用的路由规则和导航守卫
 * 包含登录页面、主页面及各功能模块的路由配置
 * 
 * @author 医生端项目组
 * @date 2024
 * @version 1.0.0
 */

import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由配置数组
 * 
 * 定义应用的所有路由规则，包括页面路径、组件映射和嵌套路由
 */
const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    // 嵌套路由配置 - 主页面下的各功能模块
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'patients',
        name: 'Patients',
        component: () => import('../views/Patients.vue')
      },
      {
        path: 'appointments',
        name: 'Appointments',
        component: () => import('../views/Appointments.vue')
      },
      {
        path: 'medical-records',
        name: 'MedicalRecords',
        component: () => import('../views/MedicalRecords.vue')
      },
      {
        path: 'prescriptions',
        name: 'Prescriptions',
        component: () => import('../views/Prescriptions.vue')
      },
      {
        path: 'examination-reports',
        name: 'ExaminationReports',
        component: () => import('../views/ExaminationReports.vue')
      },
      {
        path: 'schedule',
        name: 'Schedule',
        component: () => import('../views/Schedule.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue')
      }
    ]
  },
  {
    // 404 页面处理 - 捕获所有未匹配的路由并重定向到首页
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

/**
 * 创建路由实例
 * 
 * 使用 HTML5 history 模式，支持无哈希的 URL
 */
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

/**
 * 全局前置守卫
 * 
 * 在每次路由跳转前执行，主要用于：
 * - 检查用户登录状态
 * - 未登录用户重定向到登录页
 * - 记录导航日志
 * 
 * @param {Object} to - 即将要进入的目标路由对象
 * @param {Object} from - 当前导航正要离开的路由对象
 * @param {Function} next - 进行管道中的下一个钩子
 */
router.beforeEach((to, from, next) => {
  // 从本地存储获取医生登录 token
  const token = localStorage.getItem('doctorToken')
  
  // 记录导航日志，便于调试
  console.log('路由拦截器 - 当前路径:', to.path, '是否有token:', !!token)
  
  // 访问非登录页面时检查登录状态
  if (to.name !== 'Login' && !token) {
    console.log('未登录，重定向到登录页')
    // 未登录用户重定向到登录页
    next({ name: 'Login' })
  } else {
    // 已登录或访问登录页，允许继续导航
    next()
  }
})

// 导出路由实例
export default router 