/**
 * 医生端应用程序主入口文件
 * 负责初始化 Vue 应用、配置全局组件、插件和路由
 * @author 医生端项目组
 * @date 2024
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
