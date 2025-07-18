/**
 * 系统管理路由模块
 * 定义系统管理相关的路由配置，包括用户管理、部门管理、角色管理、菜单管理等
 */
const Layout = () => import("@/layout/index.vue");

export default {
    path: "/system",
    name:"System",
    component: Layout,
    redirect: "/role",
    meta: {
        title: "系统管理",
        icon:'ele-setting'
    },
    children: [
        {
            path: "/user",
            name: "User",
            component: () => import("@/views/system/user/index.vue"),
            meta: {
                title: "用户管理",
                icon:'ele-user'
            }
        },
        {
            path: "/dept",
            name: "Dept",
            component: () => import("@/views/system/dept/index.vue"),
            meta: {
                title: "部门管理",
                icon:'ele-user'
            }
        },
        {
            path: "/role",
            name: "Role",
            component: () => import("@/views/system/role/index.vue"),
            meta: {
                title: "角色管理",
                icon:'ele-user'
            }
        },
        {
            path: "/menu",
            name: "Menu",
            component: () => import("@/views/system/menu/index.vue"),
            meta: {
                title: "菜单管理",
                icon:'ele-user'
            }
        },
    ]
} as RouteConfigsTable;
