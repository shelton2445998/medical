/**
 * 基础路由配置
 * 包含登录、404错误页面、医院管理、预约管理等基础路由
 */
export default [
    {
        path: "/login",
        name: 'Login',
        component: () => import("@/views/login/index.vue"),
        meta: {
            title: '登录'
        },
    },
    {
        path: '/404',
        name: '404',
        component: () => import('@/views/error/404.vue'),
        meta: {
            title: "404",
        }
    },
    // 医院管理模块路由
    {
        path: '/hospital',
        name: 'Hospital',
        component: () => import('@/layout/index.vue'),
        meta: {
            title: "医院管理",
        },
        children: [
            {
                path: 'doctor',
                name: 'HospitalDoctor',
                component: () => import('@/views/hospital/doctor/index.vue'),
                meta: {
                    title: "医生管理",
                },
            },
            {
                path: 'list',
                name: 'HospitalList',
                component: () => import('@/views/hospital/list/index.vue'),
                meta: {
                    title: "医院列表",
                },
            },
        ]
    },
    // 预约管理模块路由
    {
        path: '/appointment',
        name: 'Appointment',
        component: () => import('@/layout/index.vue'),
        meta: {
            title: "预约管理",
        },
        children: [
            {
                path: 'list',
                name: 'AppointmentList',
                component: () => import('@/views/appointment/list/index.vue'),
                meta: {
                    title: "预约列表",
                },
            },
            {
                path: 'user',
                name: 'AppointmentUser',
                component: () => import('@/views/appointment/user/index.vue'),
                meta: {
                    title: "用户管理",
                },
            },
        ]
    },
] as Array<RouteConfigsTable>;
