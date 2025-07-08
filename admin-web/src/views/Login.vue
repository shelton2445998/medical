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
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

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
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6个字符', trigger: 'blur' }
  ]
}

// 登录逻辑
const handleLogin = () => {
  loading.value = true
  
  // 这里模拟API登录请求
  setTimeout(() => {
    // 假设登录成功
    if (loginForm.username === 'admin' && loginForm.password === 'admin123') {
      // 存储token
      localStorage.setItem('admin-token', 'admin-token-example')
      // 显示成功消息
      ElMessage.success('登录成功')
      // 跳转到首页
      router.push('/')
    } else {
      // 登录失败
      ElMessage.error('用户名或密码错误')
    }
    loading.value = false
  }, 1000)
  
  // 实际项目中应该使用axios调用后端API
  /*
  axios.post('/admin/login', {
    username: loginForm.username,
    password: loginForm.password
  }).then(response => {
    const { data } = response
    if (data.code === 200) {
      localStorage.setItem('admin-token', data.token)
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(data.message || '登录失败')
    }
  }).catch(error => {
    console.error('登录请求错误:', error)
    ElMessage.error('网络错误，请稍后再试')
  }).finally(() => {
    loading.value = false
  })
  */
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
</style> 