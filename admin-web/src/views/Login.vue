<script>
export default {
  name: "AdminLogin"
}
</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <h2>医疗预约系统</h2>
        <p>管理员登录</p>
      </div>
      
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" status-icon label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" type="password" placeholder="请输入密码" @keyup.enter="handleLogin" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-button" @click="handleLogin">
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 演示模式提示 -->
      <div class="demo-tip">
        <p>演示模式：输入任意账号密码可登录系统</p>
        <p>默认账号: admin 密码: admin123</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/index'

const router = useRouter()
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

// 登录逻辑
const handleLogin = async () => {
  try {
    loading.value = true
    
    // 演示模式下，任何账号密码都可以登录
    const res = await login({
      username: loginForm.username || 'admin',
      password: loginForm.password || 'admin123'
    })
    
    // 存储token
    localStorage.setItem('admin-token', res.data.token || 'admin-token-example')
    
    // 显示成功消息
    ElMessage.success('登录成功')
    
    // 跳转到首页
    router.push('/')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  background-image: url('../assets/login-bg.svg');
  background-size: cover;
  background-position: center;
}

.login-card {
  width: 400px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.login-header p {
  font-size: 14px;
  color: #999;
}

.login-button {
  width: 100%;
}

.demo-tip {
  margin-top: 20px;
  text-align: center;
  padding: 10px;
  background-color: #f8f8f8;
  border-radius: 4px;
  color: #666;
  font-size: 13px;
}

.demo-tip p {
  margin: 5px 0;
}
</style> 