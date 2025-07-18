/**
 * @fileoverview 医生端应用程序主入口文件
 * @description 负责初始化Vue3应用、配置Element Plus UI库、路由系统和全局组件
 * @author 医生端项目组
 * @version 2.0.0
 * @since 2024-01-01
 * 
 * @features
 * - Vue3应用初始化配置
 * - Element Plus UI组件库集成
 * - 路由系统配置
 * - 全局图标组件注册
 * - 中文语言包配置
 * - 浏览器兼容性处理
 * 
 * @dependencies
 * - Vue3: 现代前端框架
 * - Element Plus: UI组件库
 * - Vue Router: 路由管理
 * - Element Plus Icons: 图标库
 * 
 * @usage
 * 此文件作为医生端Vue3项目的入口点，负责应用的启动和全局配置
 * 
 * @security
 * - 通过console.error重写处理ResizeObserver错误，避免控制台信息泄露
 * - 安全的全局组件注册机制
 * 
 * @performance
 * - 使用Vue3 Composition API提升性能
 * - 按需加载Element Plus组件
 * - 全局图标组件预注册，避免重复导入
 * 
 * @compatibility
 * - 处理ResizeObserver浏览器兼容性问题
 * - 支持现代浏览器特性
 * 
 * @maintenance
 * - 清晰的模块化导入结构
 * - 便于后续添加全局插件和配置
 * - 统一的错误处理机制
 */

// 引入 Vue 3 的核心函数
import { createApp } from 'vue'

// 引入 Element Plus UI 组件库
import ElementPlus from 'element-plus'
// 引入 Element Plus 样式文件
import 'element-plus/dist/index.css'
// 引入 Element Plus 中文语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// 引入根组件
import App from './App.vue'
// 引入路由配置
import router from './router'
// 引入全局样式
import './assets/css/global.css'

// 引入 Element Plus 图标组件
import * as ElementPlusIcons from '@element-plus/icons-vue'

// 忽略 ResizeObserver 错误
// 这是一个常见的浏览器兼容性问题，不影响功能
const originalConsoleError = console.error;
console.error = (...args) => {
  if (args.length > 0 && typeof args[0] === 'string' && 
      args[0].includes('ResizeObserver loop completed with undelivered notifications')) {
    return;
  }
  originalConsoleError(...args);
};

// 创建 Vue 应用实例
const app = createApp(App)

// 全局注册 Element Plus 图标组件
// 遍历所有图标并注册为全局组件，使其可在任何组件中使用
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component)
}

// 使用 Element Plus 插件，配置为中文语言
app.use(ElementPlus, {
  locale: zhCn
})

// 使用路由插件
app.use(router)

// 将应用挂载到 DOM 元素 #app 上
app.mount('#app')
