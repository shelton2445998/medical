/**
 * 全局类型声明文件
 * 定义项目中使用的全局类型和接口
 */
import type {RouteComponent} from "vue-router";

/**
 * 全局类型声明
 */
declare global {
    /**
     * 可空类型定义
     * 表示一个类型可以为null或undefined
     */
    declare type NullType<T> = T | null | undefined;

    /**
     * 路由配置表接口
     * 定义主路由的配置结构
     */
    interface RouteConfigsTable {
        /** 路由地址 `必填` */
        path: string;
        /** 路由名字（保持唯一）`可选` */
        name?: string;
        /** `Layout`组件 `可选` */
        component?: RouteComponent;
        /** 路由重定向 `可选` */
        redirect?: string;
        /** 路由元信息 `可选` */
        meta?: {
            /** 菜单名称`必填` */
            title: string;
            /** 菜单图标 `可选` */
            icon?: string;
            /** 是否在菜单中显示（默认`true`）`可选` */
            isShow?: boolean;
            /** 是否缓存路由（默认`false`）`可选` */
            isCache?: boolean;
            /** 是否是首页（默认`false`）`可选` */
            isHome?: boolean;
        };
        /** 子路由配置项 */
        children?: Array<RouteChildrenConfigsTable>;
    }

    /**
     * 子路由配置表接口
     * 定义子路由的配置结构
     */
    interface RouteChildrenConfigsTable {
        /** 子路由地址 `必填` */
        path: string;
        /** 路由名字（对应不要重复，和当前组件的`name`保持一致）`必填` */
        name?: string;
        /** 路由重定向 `可选` */
        redirect?: string;
        /** 按需加载组件 `可选` */
        component?: RouteComponent;
        /** 路由元信息 `可选` */
        meta?: {
            /** 菜单名称`必填` */
            title: string;
            /** 菜单图标 `可选` */
            icon?: string;
            /** 是否在菜单中显示（默认`true`）`可选` */
            isShow?: boolean;
            /** 是否缓存路由`可选` */
            isCache?: boolean;
            /** 是否是首页（默认`false`）`可选` */
            isHome?: boolean;
        };
        /** 子路由配置项 */
        children?: Array<RouteChildrenConfigsTable>;
    }

    /**
     * 表格数据类型接口
     * 定义表格数据的结构
     */
    interface TableDataType {
        data: any[]  // 表格数据数组
    }

    /**
     * 窗口对象扩展
     * 为window对象添加自定义属性
     */
    declare interface Window {
        nextLoading: boolean;  // 全局loading状态
    }
}


