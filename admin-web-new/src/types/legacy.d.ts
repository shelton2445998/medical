/**
 * 遗留模块类型声明文件
 * 为第三方库和特殊文件类型提供类型支持
 */

/**
 * js-md5库类型声明
 * 为MD5加密库提供类型支持
 */
declare module 'js-md5';

/**
 * Vue单文件组件类型声明
 * 为.vue文件提供TypeScript类型支持
 */
declare module '*.vue' {
    import { DefineComponent } from 'vue';
    // 声明.vue文件导出的组件类型
    const component: DefineComponent<{}, {}, any>;
    export default component;
}
