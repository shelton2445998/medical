import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'

const app = createApp(App)

// 使用Element Plus
app.use(ElementPlus)

// 配置axios
axios.defaults.baseURL = 'http://localhost:8080/api'
app.config.globalProperties.$http = axios

app.mount('#app')
