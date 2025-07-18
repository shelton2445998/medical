<!--
  医生端登录页面组件
  
  提供医生用户登录功能，包含表单验证、登录逻辑处理
  具有美观的UI设计和动画效果
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="login-container">
    <!-- 医疗系统背景图 -->
    <div class="bg-image"></div>
    
    <!-- 动态背景装饰层 - 提供视觉层次和美观效果 -->
    <div class="bg-decoration">
      <div class="bg-circle circle-1"></div>
      <div class="bg-circle circle-2"></div>
      <div class="bg-circle circle-3"></div>
      <div class="light-spot spot-1"></div>
      <div class="light-spot spot-2"></div>
    </div>
    
    <!-- 登录表单容器 -->
    <div class="login-box">
		
      <!-- 品牌Logo区域 - 展示系统标识和名称 -->
      <div class="brand-area">
        <div class="logo">
          <!-- 圆形logo图片 -->
          <img src="../assets/images/new_logo.png" alt="Logo" class="logo-image" />
        </div>
        <div class="title">医生工作站</div>
        <div class="subtitle">医疗预约管理系统</div>
      </div>
      
      <!-- 演示模式提示（已注释） -->
      <!-- <div class="demo-tip">
        <el-icon class="tip-icon"><InfoFilled /></el-icon>
        <span>演示模式：API不可用时，可使用任意手机号和密码登录</span>
      </div> -->
      
      <!-- 登录表单 - 包含手机号和密码输入 -->
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <!-- 手机号输入框 -->
        <el-form-item prop="mobile">
          <el-input 
            v-model="loginForm.mobile" 
            prefix-icon="el-icon-phone" 
            placeholder="请输入手机号"
            class="form-input"
          ></el-input>
        </el-form-item>
        
        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            prefix-icon="el-icon-lock" 
            type="password" 
            placeholder="请输入密码"
            class="form-input"
          ></el-input>
        </el-form-item>
        
        <!-- 表单操作区域 -->
        <div class="form-actions">
          <el-checkbox v-model="rememberMe" class="remember-checkbox">记住密码</el-checkbox>
          <el-link type="primary" class="forgot-link" :underline="false">忘记密码?</el-link>
        </div>
        
        <!-- 登录按钮 -->
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
      
      <!-- 登录页脚信息 -->
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
/**
 * 登录页面逻辑
 * 
 * 处理用户登录验证、表单提交、状态管理等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, reactive } from 'vue'
// 导入路由相关函数
import { useRouter } from 'vue-router'
// 导入 Element Plus 消息提示组件
import { ElMessage } from 'element-plus'
// 导入 axios 进行HTTP请求
import axios from 'axios'
// 导入 Element Plus 图标组件
import { Hospital, InfoFilled, Shield } from '@element-plus/icons-vue'

// 后端API基础地址配置
const BACKEND_BASE_URL = 'http://localhost:8888'

// 创建axios实例，配置基础URL和认证信息
const request = axios.create({
  baseURL: BACKEND_BASE_URL,
  withCredentials: true  // 支持跨域请求携带cookie
})

/**
 * 登录页面组件导出
 * 
 * 定义组件的基本信息和组合式API逻辑
 */
export default {
  name: 'LoginView',
  components: { Hospital, InfoFilled, Shield },
  setup() {
    // 路由实例，用于页面跳转
    const router = useRouter()
    
    // 表单引用，用于表单验证
    const loginFormRef = ref(null)
    
    // 登录状态管理
    const loading = ref(false)          // 登录中状态
    const rememberMe = ref(false)       // 记住密码状态

    /**
     * 登录表单数据
     * 
     * 包含用户输入的手机号和密码
     */
    const loginForm = reactive({
      mobile: '',     // 手机号
      password: ''    // 密码
    })

    /**
     * 表单验证规则
     * 
     * 定义手机号和密码的验证规则
     */
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

    /**
     * 处理登录逻辑
     * 
     * 验证表单、发送登录请求、处理响应结果
     */
    const handleLogin = () => {
      if (!loginFormRef.value) return
      
      // 验证表单
      loginFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            loading.value = true
            
            // 发送登录请求
            const { data: res } = await request.post('/api/doctor/login', loginForm)
            
            if (res.code === 200) {
              // 登录成功，保存token和用户信息
              localStorage.setItem('doctorToken', res.data.token)
              
              if (res.data.doctorInfo) {
                localStorage.setItem('doctorInfo', JSON.stringify(res.data.doctorInfo))
                
                // 处理记住密码功能
                if (rememberMe.value) {
                  localStorage.setItem('rememberedMobile', loginForm.mobile)
                } else {
                  localStorage.removeItem('rememberedMobile')
                }
              }
              
              ElMessage.success('登录成功')
              // 跳转到工作台页面
              router.push('/home/dashboard')
            } else {
              ElMessage.error(res.message || '登录失败，请检查手机号和密码')
            }
          } catch (error) {
            // 错误处理
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

    /**
     * 初始化表单
     * 
     * 如果之前选择了记住密码，则自动填充手机号
     */
    const initForm = () => {
      const rememberedMobile = localStorage.getItem('rememberedMobile')
      if (rememberedMobile) {
        loginForm.mobile = rememberedMobile
        rememberMe.value = true
      }
    }
    
    // 组件初始化时调用
    initForm()

    // 返回模板需要的数据和方法
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

<!--
  登录页面样式
  
  定义登录页面的视觉设计和交互效果
  包含响应式设计和动画效果
-->
<style scoped>
/* ==================== 基础容器样式 ==================== */

/**
 * 登录页面主容器
 * 
 * 提供全屏布局和居中对齐
 */
.login-container {
  height: 100vh;              /* 全屏高度 */
  display: flex;              /* 弹性布局 */
  justify-content: center;    /* 水平居中 */
  align-items: center;        /* 垂直居中 */
  position: relative;         /* 相对定位，为子元素提供定位基准 */
  overflow: hidden;           /* 隐藏超出部分 */
  background: inherit;        /* 继承父元素背景 */
}

/* ==================== 背景样式 ==================== */

/**
 * 医疗系统背景图样式
 * 
 * 提供页面背景图片和渐变遮罩效果
 */
.bg-image {
  position: absolute;         /* 绝对定位 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* 使用医疗相关的背景图片 */
  background-image: url('https://ts1.tc.mm.bing.net/th/id/R-C.45adb4a35bc45f4066564f64c7e95b9e?rik=hj9vuhsfmtEnpQ&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50046%2f2979.jpg_wh1200.jpg&ehk=j9ibHicjwtZ7WKASI1IYOM6ankqf3fiVzP7kjnno%2ffU%3d&risl=&pid=ImgRaw&r=0');
  background-size: cover;     /* 覆盖整个容器 */
  background-position: center;/* 居中显示 */
  background-repeat: no-repeat; /* 不重复 */
  z-index: -2;               /* 层级最低 */
  
  /* 渐变遮罩层 */
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    /* 淡蓝色渐变遮罩，提升视觉层次 */
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