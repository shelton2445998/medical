<template>
  <div class="login-container">
    <!-- 背景装饰层 -->
    <div class="bg-decoration">
      <div class="bg-circle circle-1"></div>
      <div class="bg-circle circle-2"></div>
      <div class="bg-circle circle-3"></div>
      <div class="bg-pattern"></div>
    </div>
    
    <!-- 左侧品牌区域 -->
    <div class="brand-section">
      <div class="brand-content">
        <div class="logo-container">
          <img src="../assets/images/new_logo.png" alt="Logo" class="brand-logo" />
        </div>
        <h1 class="brand-title">医生工作站</h1>
        <p class="brand-description">提供专业高效的医疗信息管理平台<br/>助力医生提供更优质的医疗服务</p>
        <div class="brand-features">
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><DataBoard /></el-icon>
            </div>
            <div class="feature-text">数据可视化</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="feature-text">患者管理</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="feature-text">报告生成</div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 登录表单容器 -->
    <div class="login-box">
      <div class="login-header">
        <div class="title">欢迎登录</div>
        <div class="subtitle">医疗预约管理系统 - 医生工作站</div>
      </div>
      
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <el-form-item prop="mobile">
          <el-input 
            v-model="loginForm.mobile" 
            class="form-input"
            placeholder="请输入手机号"
          >
            <template #prefix>
              <el-icon class="input-icon"><Iphone /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password"
            type="password" 
            show-password
            class="form-input"
            placeholder="请输入密码"
          >
            <template #prefix>
              <el-icon class="input-icon"><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <div class="form-options">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <a href="javascript:;" class="forgot-password">忘记密码?</a>
        </div>
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
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { Iphone, Lock, DataBoard, User, Document, InfoFilled } from '@element-plus/icons-vue'

const BACKEND_BASE_URL = 'http://localhost:8888'
const request = axios.create({
  baseURL: BACKEND_BASE_URL,
  withCredentials: true
})

export default {
  name: 'LoginView',
  components: {
    Iphone,
    Lock,
    InfoFilled,
    DataBoard,
    User,
    Document
  },
  setup() {
    const router = useRouter()
    const loginFormRef = ref(null)
    const loading = ref(false)
    const rememberMe = ref(false)

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
            ElMessage.error('登录失败，请检查网络连接或联系管理员')
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
      rememberMe,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #f3f9fe 0%, #e6f0ff 100%);
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
  background: radial-gradient(circle, rgba(62, 123, 250, 0.1) 0%, rgba(62, 123, 250, 0) 70%);
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
  background: radial-gradient(circle, rgba(82, 196, 26, 0.08) 0%, rgba(82, 196, 26, 0) 70%);
  animation-delay: 5s;
}

.circle-3 {
  width: 300px;
  height: 300px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: radial-gradient(circle, rgba(153, 102, 255, 0.08) 0%, rgba(153, 102, 255, 0) 70%);
  animation-delay: 8s;
}

/* 网格纹理 */
.bg-pattern {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(62, 123, 250, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(62, 123, 250, 0.03) 1px, transparent 1px);
  background-size: 20px 20px;
}

/* 左侧品牌区域 */
.brand-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--primary-color);
  background-image: linear-gradient(135deg, #3E7BFA 0%, #2E5ED9 100%);
  color: white;
  position: relative;
  overflow: hidden;
  padding: 40px;
}

.brand-content {
  max-width: 500px;
  position: relative;
  z-index: 2;
  text-align: center;
}

.logo-container {
  margin-bottom: 24px;
}

.brand-logo {
  width: 100px;
  height: 100px;
  object-fit: contain;
  filter: brightness(0) invert(1);
}

.brand-title {
  font-size: 40px;
  font-weight: 700;
  margin: 0 0 16px;
  letter-spacing: 1px;
}

.brand-description {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 40px;
  opacity: 0.9;
}

.brand-features {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 40px;
}

.feature-item {
  text-align: center;
}

.feature-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
}

.feature-icon .el-icon {
  font-size: 24px;
}

.feature-text {
  font-size: 14px;
  font-weight: 500;
}

/* 登录盒子 */
.login-box {
  width: 480px;
  padding: 60px;
  background-color: #ffffff;
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-shadow: -10px 0 30px rgba(0, 0, 0, 0.05);
}

/* 标题样式优化 */
.login-header {
  margin-bottom: 32px;
}

.title {
  font-size: 32px;
  color: var(--text-primary);
  font-weight: 700;
  margin-bottom: 12px;
}

.subtitle {
  font-size: 16px;
  color: var(--text-secondary);
  font-weight: normal;
}

/* 表单样式 */
.login-form {
  margin-bottom: 24px;
}

.form-input {
  height: 48px;
  border-radius: var(--border-radius-md);
}

.input-icon {
  font-size: 18px;
  color: var(--text-secondary);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  font-size: 14px;
}

.forgot-password {
  color: var(--primary-color);
  text-decoration: none;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: var(--border-radius-md);
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  transition: all 0.3s;
}

.login-button:hover {
  background-color: var(--primary-dark);
  border-color: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(62, 123, 250, 0.2);
}

/* 底部信息 */
.login-footer {
  margin-top: auto;
  text-align: center;
  font-size: 12px;
  color: var(--text-secondary);
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

/* 响应式调整 */
@media (max-width: 992px) {
  .login-container {
    flex-direction: column;
  }
  
  .brand-section {
    display: none;
  }
  
  .login-box {
    width: 100%;
    padding: 40px 20px;
    box-shadow: none;
  }
}
</style>