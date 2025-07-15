import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import App from './App.vue'
import router from './router'
import './assets/css/global.css'

// 引入图标
import * as ElementPlusIcons from '@element-plus/icons-vue'

// 忽略ResizeObserver错误
const originalConsoleError = console.error;
console.error = (...args) => {
  if (args.length > 0 && typeof args[0] === 'string' && 
      args[0].includes('ResizeObserver loop completed with undelivered notifications')) {
    return;
  }
  originalConsoleError(...args);
};

const app = createApp(App)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component)
}

app.use(ElementPlus, {
  locale: zhCn
})
app.use(router)
app.mount('#app')
