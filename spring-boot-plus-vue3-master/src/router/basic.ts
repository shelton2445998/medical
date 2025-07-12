export default [
    {
        path: "/",
        redirect: "/doctor/home",
    },
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
    {
        path: '/test',
        name: 'Test',
        component: () => import('@/views/test.vue'),
        meta: {
            title: "测试页面",
        }
    },
] as Array<RouteConfigsTable>;
