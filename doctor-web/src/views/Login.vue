<template>
  <div class="login-container">
    <div class="login-box">
      <div class="title">医生工作站</div>
      <div class="subtitle">医疗预约管理系统</div>
      <div class="demo-tip">演示模式：API不可用时，可使用任意用户名密码登录</div>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名"></el-input>
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

export default {
  name: 'LoginView',
  setup() {
    const router = useRouter()
    const loginFormRef = ref(null)
    const loading = ref(false)

    const loginForm = reactive({
      username: '',
      password: ''
    })

    const loginRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度应在3到20个字符之间', trigger: 'blur' }
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
            const { data: res } = await axios.post('/doctor/login', loginForm)
            if (res.code === 200) {
              localStorage.setItem('doctorToken', res.data.token)
              localStorage.setItem('doctorInfo', JSON.stringify(res.data.doctorInfo))
              ElMessage.success('登录成功')
              router.push('/home/dashboard')
            } else {
              ElMessage.error(res.message || '登录失败，请检查用户名和密码')
            }
          } catch (error) {
            console.error('登录出错：', error)
            // API失效时使用静态演示数据登录
            const mockDoctorInfo = {
              doctorId: 'D2023001',
              name: loginForm.username || '演示医生',
              department: 'internal',
              departmentName: '内科',
              title: '主治医师',
              phone: '13800138000'
            }
            // 生成模拟token
            const mockToken = 'mock_token_' + Math.random().toString(36).substring(2)
            localStorage.setItem('doctorToken', mockToken)
            localStorage.setItem('doctorInfo', JSON.stringify(mockDoctorInfo))
            ElMessage.success('登录成功(演示模式)')
            router.push('/home/dashboard')
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