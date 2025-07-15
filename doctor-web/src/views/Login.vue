<template>
  <div class="login-container">
    <!-- 背景装饰层 -->
    <div class="bg-decoration">
      <div class="bg-circle circle-1"></div>
      <div class="bg-circle circle-2"></div>
      <div class="bg-circle circle-3"></div>
      <div class="bg-pattern"></div>
    </div>
    
    <!-- 登录表单容器 -->
    <div class="login-box">
      <div class="title">医生工作站</div>
      <div class="subtitle">医疗预约管理系统</div>
      <div class="demo-tip">演示模式：API不可用时，可使用任意手机号和密码登录</div>
      
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <el-form-item prop="mobile">
          <el-input 
            v-model="loginForm.mobile" 
            prefix-icon="el-icon-phone" 
            placeholder="请输入手机号"
            class="form-input"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            prefix-icon="el-icon-lock" 
            type="password" 
            placeholder="请输入密码"
            class="form-input"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            class="login-button" 
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 底部装饰元素 -->
      <div class="login-footer">
        <span>© 2025 医疗预约管理系统</span>
      </div>
    </div>
  </div>
</template>

<script>
// 原有脚本逻辑保持不变
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const BACKEND_BASE_URL = 'http://localhost:8888'
const request = axios.create({
  baseURL: BACKEND_BASE_URL,
  withCredentials: true
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
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  /* 基础背景色 */
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f0ff 100%);
}

/* 背景装饰层 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

/* 圆形装饰元素 */
.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(64, 158, 255, 0.15) 0%, rgba(64, 158, 255, 0) 70%);
  animation: float 20s infinite ease-in-out;
}

.circle-1 {
  width: 600px;
  height: 600px;
  top: -200px;
  left: -100px;
  animation-delay: 0s;
}

.circle-2 {
  width: 500px;
  height: 500px;
  bottom: -150px;
  right: -100px;
  background: radial-gradient(circle, rgba(103, 194, 58, 0.1) 0%, rgba(103, 194, 58, 0) 70%);
  animation-delay: 5s;
}

.circle-3 {
  width: 300px;
  height: 300px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: radial-gradient(circle, rgba(153, 102, 255, 0.1) 0%, rgba(153, 102, 255, 0) 70%);
  animation-delay: 8s;
}

/* 网格纹理 */
.bg-pattern {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(64, 158, 255, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(64, 158, 255, 0.05) 1px, transparent 1px);
  background-size: 20px 20px;
}

/* 登录盒子 */
.login-box {
  width: 420px;
  padding: 45px;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  position: relative;
  z-index: 1;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.8);
}

/* 标题样式优化 */
.title {
  font-size: 32px;
  color: #337ab7;
  font-weight: 700;
  text-align: center;
  margin-bottom: 12px;
  letter-spacing: 1px;
}

.subtitle {
  font-size: 18px;
  color: #666;
  text-align: center;
  margin-bottom: 20px;
  font-weight: 500;
}

.demo-tip {
  font-size: 13px;
  color: #666;
  text-align: center;
  margin-bottom: 30px;
  padding: 8px 12px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border-left: 3px solid #409EFF;
}

/* 表单样式 */
.login-form {
  margin-top: 25px;
}

.form-input {
  border-radius: 8px;
  border-color: #e5e6eb;
  transition: all 0.3s;
}

.form-input:focus {
  border-color: #409EFF;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 8px;
  background-color: #337ab7;
  border-color: #337ab7;
  transition: all 0.3s;
}

.login-button:hover {
  background-color: #286090;
  border-color: #286090;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(51, 122, 183, 0.2);
}

/* 底部信息 */
.login-footer {
  margin-top: 30px;
  text-align: center;
  font-size: 12px;
  color: #999;
}

/* 浮动动画 */
@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-30px) rotate(5deg);
  }
}
</style>