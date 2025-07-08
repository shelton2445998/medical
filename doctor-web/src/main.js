import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import './assets/css/global.css'

// 配置axios
axios.defaults.baseURL = 'http://localhost:3000/api'
axios.defaults.timeout = 8000 // 设置8秒超时时间

// 请求拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('doctorToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
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
    console.info('系统当前处于演示模式，使用静态数据展示功能')
    // 将错误继续向下传递，让各组件自行处理静态数据
    return Promise.reject(error)
  }
)

const app = createApp(App)
app.use(ElementPlus, {
  locale: zhCn
})
app.use(router)
app.mount('#app')
