<template>
  <div class="login-container">
    <!-- 医疗系统背景图 -->
    <div class="bg-image"></div>
    
    <!-- 动态背景装饰层 -->
    <div class="bg-decoration">
      <div class="bg-circle circle-1"></div>
      <div class="bg-circle circle-2"></div>
      <div class="bg-circle circle-3"></div>
      <div class="light-spot spot-1"></div>
      <div class="light-spot spot-2"></div>
    </div>
    
    <!-- 登录表单容器 -->
    <div class="login-box">
		
      <!-- 品牌Logo区域 - 圆形logo处理 -->
      <div class="brand-area">
        <div class="logo">
          <!-- 圆形logo图片 -->
          <img src="../assets/images/new_logo.png" alt="Logo" class="logo-image" />
        </div>
        <div class="title">医生工作站</div>
        <div class="subtitle">医疗预约管理系统</div>
      </div>
      
      <div class="demo-tip">
        <el-icon class="tip-icon"><InfoFilled /></el-icon>
        <span>演示模式：API不可用时，可使用任意手机号和密码登录</span>
      </div>
      
      <!-- 登录表单 -->
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
        
        <div class="form-actions">
          <el-checkbox v-model="rememberMe" class="remember-checkbox">记住密码</el-checkbox>
          <el-link type="primary" class="forgot-link" :underline="false">忘记密码?</el-link>
        </div>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            class="login-button" 
            @click="handleLogin"
          >
            <template #default>
              <span v-if="!loading">登录</span>
              <el-loading v-else spinner="el-icon-loading" size="16"></el-loading>
            </template>
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <div class="copyright">© 2025 医疗预约管理系统 版权所有</div>
        <div class="security-info">
          <el-icon class="security-icon"><Shield /></el-icon>
          <span>加密传输 · 安全登录</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 脚本部分保持不变
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { Hospital, InfoFilled, Shield } from '@element-plus/icons-vue'

const BACKEND_BASE_URL = 'http://localhost:8888'
const request = axios.create({
  baseURL: BACKEND_BASE_URL,
  withCredentials: true
})

export default {
  name: 'LoginView',
  components: { Hospital, InfoFilled, Shield },
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
                if (rememberMe.value) {
                  localStorage.setItem('rememberedMobile', loginForm.mobile)
                } else {
                  localStorage.removeItem('rememberedMobile')
                }
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

    const initForm = () => {
      const rememberedMobile = localStorage.getItem('rememberedMobile')
      if (rememberedMobile) {
        loginForm.mobile = rememberedMobile
        rememberMe.value = true
      }
    }
    
    initForm()

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
/* 基础容器样式 */
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  background: inherit;
}

/* 医疗系统背景图样式 */
.bg-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('https://ts1.tc.mm.bing.net/th/id/R-C.45adb4a35bc45f4066564f64c7e95b9e?rik=hj9vuhsfmtEnpQ&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50046%2f2979.jpg_wh1200.jpg&ehk=j9ibHicjwtZ7WKASI1IYOM6ankqf3fiVzP7kjnno%2ffU%3d&risl=&pid=ImgRaw&r=0');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -2;
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, rgba(240, 248, 255, 0.4), rgba(245, 249, 250, 0.4));
  }
}

/* 动态背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0; 
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

/* 浮动圆形装饰 */
.bg-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.3;
  animation: float 25s infinite ease-in-out;
}

/* 动态光斑效果 */
.light-spot {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255,255,255,0.6) 0%, rgba(255,255,255,0) 70%);
  animation: pulse 15s infinite ease-in-out;
}

.spot-1 {
  width: 300px;
  height: 300px;
  top: 20%;
  right: 15%;
  animation-delay: 0s;
}

.spot-2 {
  width: 250px;
  height: 250px;
  bottom: 15%;
  left: 10%;
  animation-delay: 7s;
}

/* 登录盒子样式 */
.login-box {
  width: 420px;
  padding: 50px 45px;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 16px;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
  position: relative;
  z-index: 1;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  overflow: hidden;
}

/* 登录框内的背景图映射效果 */
.login-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url('https://ts1.tc.mm.bing.net/th/id/R-C.45adb4a35bc45f4066564f64c7e95b9e?rik=hj9vuhsfmtEnpQ&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50046%2f2979.jpg_wh1200.jpg&ehk=j9ibHicjwtZ7WKASI1IYOM6ankqf3fiVzP7kjnno%2ffU%3d&risl=&pid=ImgRaw&r=0');
  background-size: cover;
  background-position: center;
  filter: blur(20px);
  -webkit-filter: blur(20px);
  transform: scale(0.9);
  z-index: -1;
  opacity: 0.4;
}

.login-box:hover {
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  transform: translateY(-3px);
}

/* 品牌区域样式 */
.brand-area {
  text-align: center;
  margin-bottom: 35px;
  position: relative;
  z-index: 2;
}

/* Logo容器样式 */
.logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 圆形logo图片样式 - 核心修改 */
.logo-image {
  width: 80px;
  height: 80px;
  object-fit: cover; /* 保持图片比例，裁剪多余部分 */
  border-radius: 50%; /* 关键：将图片处理为圆形 */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); /* 轻微阴影增强立体感 */
  transition: transform 0.3s ease; /* 悬停动画效果 */
}

/* 鼠标悬停时的微动效 */
.logo-image:hover {
  transform: scale(1.05);
}

.title {
  font-size: 28px;
  color: #1f2329;
  font-weight: 700;
  margin-bottom: 10px;
  letter-spacing: 0.5px;
}

.subtitle {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

/* 演示提示样式 */
.demo-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #333;
  padding: 10px 15px;
  background-color: rgba(255, 255, 255, 0.6);
  border-radius: 8px;
  border-left: 3px solid #337ab7;
  margin-bottom: 35px;
  position: relative;
  z-index: 2;
}

/* 表单样式优化 */
.login-form {
  margin-top: 15px;
  position: relative;
  z-index: 2;
}

.form-input {
  border-radius: 10px;
  border-color: rgba(255, 255, 255, 0.5);
  background-color: rgba(255, 255, 255, 0.7);
  height: 50px;
  font-size: 15px;
  transition: all 0.3s;
}

.form-input:focus {
  border-color: #337ab7;
  box-shadow: 0 0 0 4px rgba(51, 122, 183, 0.15);
  background-color: rgba(255, 255, 255, 0.9);
}

/* 表单操作区 */
.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 15px 0 30px;
  font-size: 14px;
  color: #333;
}

.forgot-link {
  color: #286090;
}

/* 登录按钮样式 */
.login-button {
  width: 100%;
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px;
  background-color: #337ab7;
  border-color: #337ab7;
  transition: all 0.3s;
}

/* 底部信息样式 */
.login-footer {
  margin-top: 40px;
  text-align: center;
  position: relative;
  z-index: 2;
}

.copyright {
  font-size: 13px;
  color: #555;
  margin-bottom: 10px;
}

.security-info {
  font-size: 12px;
  color: #555;
}

/* 动画定义 */
@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-40px) rotate(5deg);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 0.9;
    transform: scale(1.2);
  }
}
</style>