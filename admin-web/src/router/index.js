import { createRouter, createWebHistory } from 'vue-router'

// 导入视图组件
const Login = () => import('../views/Login.vue')
const Layout = () => import('../views/Layout.vue')
const Dashboard = () => import('../views/Dashboard.vue')
const UserManagement = () => import('../views/UserManagement.vue')
const DoctorManagement = () => import('../views/DoctorManagement.vue')
const PackageManagement = () => import('../views/PackageManagement.vue')
const CheckItemManagement = () => import('../views/CheckItemManagement.vue')
const HospitalManagement = () => import('../views/HospitalManagement.vue')
const AppointmentManagement = () => import('../views/AppointmentManagement.vue')
const FinanceManagement = () => import('../views/FinanceManagement.vue')
const SystemSettings = () => import('../views/SystemSettings.vue')

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { title: '仪表盘', icon: 'el-icon-s-data' }
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: UserManagement,
        meta: { title: '用户管理', icon: 'el-icon-user' }
      },
      {
        path: 'doctors',
        name: 'DoctorManagement',
        component: DoctorManagement,
        meta: { title: '医生管理', icon: 'el-icon-first-aid-kit' }
      },
      {
        path: 'packages',
        name: 'PackageManagement',
        component: PackageManagement,
        meta: { title: '体检套餐管理', icon: 'el-icon-suitcase' }
      },
      {
        path: 'checkItems',
        name: 'CheckItemManagement',
        component: CheckItemManagement,
        meta: { title: '体检项目管理', icon: 'el-icon-document-checked' }
      },
      {
        path: 'hospitals',
        name: 'HospitalManagement',
        component: HospitalManagement,
        meta: { title: '医院管理', icon: 'el-icon-office-building' }
      },
      {
        path: 'appointments',
        name: 'AppointmentManagement',
        component: AppointmentManagement,
        meta: { title: '预约管理', icon: 'el-icon-date' }
      },
      {
        path: 'finance',
        name: 'FinanceManagement',
        component: FinanceManagement,
        meta: { title: '财务管理', icon: 'el-icon-money' }
      },
      {
        path: 'settings',
        name: 'SystemSettings',
        component: SystemSettings,
        meta: { title: '系统设置', icon: 'el-icon-setting' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  // 判断是否需要登录
  const token = localStorage.getItem('admin-token')
  
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router 