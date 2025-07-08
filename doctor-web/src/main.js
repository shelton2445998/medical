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
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('doctorToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

const app = createApp(App)
app.use(ElementPlus, {
  locale: zhCn
})
app.use(router)
app.mount('#app')
