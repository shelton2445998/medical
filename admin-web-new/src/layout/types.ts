/**
 * 布局组件类型定义文件
 * 定义布局组件中使用的类型和接口
 */

/**
 * 子路由类型定义
 * 定义布局中子路由的结构
 */
export type childrenType = {
    path?: string;                  // 路由路径
    children?: childrenType[];      // 子路由列表
    meta?: {                        // 路由元信息
        icon?: string;              // 菜单图标
        title?: string;             // 菜单标题
    };
};
