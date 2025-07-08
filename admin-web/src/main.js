import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'
import router from './router'

const app = createApp(App)

// 使用Element Plus
app.use(ElementPlus)
// 使用路由
app.use(router)

// 配置axios
axios.defaults.baseURL = 'http://localhost:8080/api'
// 请求拦截器，添加token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('admin-token')
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`
  }
  return config
})
app.config.globalProperties.$http = axios

app.mount('#app')
