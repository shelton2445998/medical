import { createRouter, createWebHistory } from 'vue-router'

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
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('doctorToken')
  console.log('路由拦截器 - 当前路径:', to.path, '是否有token:', !!token)
  
  if (to.name !== 'Login' && !token) {
    console.log('未登录，重定向到登录页')
    next({ name: 'Login' })
  } else {
    next()
  }
})

export default router 