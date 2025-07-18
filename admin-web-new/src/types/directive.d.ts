/**
 * Vue指令类型声明文件
 * 扩展Vue组件的自定义属性类型
 */

/**
 * 扩展Vue运行时核心模块
 * 为组件实例添加自定义属性的类型支持
 */
declare module '@vue/runtime-core' {
    /**
     * 组件自定义属性接口
     * 允许组件实例拥有任意类型的自定义属性
     */
    interface ComponentCustomProperties {
        [key: string]: any;
    }
}
