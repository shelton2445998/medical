import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import './assets/css/global.css'

// 配置axios
axios.defaults.baseURL = '/api'  // 修改为使用vue.config.js中配置的代理前缀
axios.defaults.timeout = 8000 // 设置8秒超时时间

// 请求拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('doctorToken')
  if (token) {
    // 设置正确格式的token，确保使用标准的头部名称
    config.headers.Authorization = token
    // 确保请求头中包含正确的Content-Type
    if (config.method === 'post') {
      config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    }
  }
  return config
})

// 响应拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    console.error('API请求错误:', error)
    // 在控制台显示一个友好的消息
    console.info('请求失败，请检查网络连接或联系管理员')
    // 将错误继续向下传递，让各组件自行处理
    return Promise.reject(error)
  }
)

const app = createApp(App)
app.use(ElementPlus, {
  locale: zhCn
})
app.use(router)
app.mount('#app')
