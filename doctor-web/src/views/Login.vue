<template>
  <div class="login-container">
    <div class="login-box">
      <div class="title">医生工作站</div>
      <div class="subtitle">医疗预约管理系统</div>
      <div class="demo-tip">演示模式：API不可用时，可使用任意手机号和密码登录</div>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <el-form-item prop="mobile">
          <el-input v-model="loginForm.mobile" prefix-icon="el-icon-phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-button" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 1. 配置后端基础地址（需与后端服务地址一致，如http://localhost:8080）
const BACKEND_BASE_URL = 'http://localhost:8888'

// 2. 创建axios实例，配置跨域携带凭证
const request = axios.create({
  baseURL: BACKEND_BASE_URL,
  withCredentials: true // 允许跨域请求携带Cookie，与后端配置匹配
})

export default {
  name: 'LoginView',
  setup() {
    const router = useRouter()
    const loginFormRef = ref(null)
    const loading = ref(false)

    const loginForm = reactive({
      mobile: '',
      password: ''
    })

    const loginRules = {
      mobile: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度应在6到20个字符之间', trigger: 'blur' }
      ]
    }

    const handleLogin = () => {
      if (!loginFormRef.value) return
      
      loginFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            loading.value = true
            // 3. 补充/api前缀，与文档接口路径一致
            const { data: res } = await request.post('/api/doctor/login', loginForm)
            
            if (res.code === 200) {
              localStorage.setItem('doctorToken', res.data.token)
              if (res.data.doctorInfo) {
                localStorage.setItem('doctorInfo', JSON.stringify(res.data.doctorInfo))
              }
              ElMessage.success('登录成功')
              router.push('/home/dashboard')
            } else {
              ElMessage.error(res.message || '登录失败，请检查手机号和密码')
            }
          } catch (error) {
            console.error('登录出错：', error)
            if (error.response?.status === 404) {
              ElMessage.error('接口不存在，请检查路径是否正确')
            } else if (error.message.includes('Network Error')) {
              ElMessage.error('服务器未启动或地址错误')
            } else {
              ElMessage.error('登录失败，服务器连接异常')
            }
          } finally {
            loading.value = false
          }
        }
      })
    }

    return {
      loginFormRef,
      loginForm,
      loginRules,
      loading,
      handleLogin
    }
  }
}
</script>

<style scoped>
/* 样式保持不变 */
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  background-image: url('../assets/images/bg.jpg');
  background-size: cover;
  background-position: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 28px;
  color: #409EFF;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 18px;
  color: #606266;
  text-align: center;
  margin-bottom: 15px;
}

.demo-tip {
  font-size: 14px;
  color: #909399;
  text-align: center;
  margin-bottom: 20px;
  padding: 6px;
  background-color: #f0f9eb;
  border-radius: 4px;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
}
</style>