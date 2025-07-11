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

    // 创建axios实例
    const api = axios.create({
      baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8888',
      timeout: 10000,
      withCredentials: true,
      headers: {
        'Content-Type': 'application/json'
      }
    })

    const handleLogin = async () => {
      try {
        loading.value = true;
        
        // 先检查后端是否可用
        if (!await checkBackendAvailability()) {
          useDemoMode();
          return;
        }
        
        const { data: res } = await api.post('/doctor/login', loginForm);
        
        if (res.code === 200) {
          // 存储token和医生信息
          localStorage.setItem('doctorToken', res.data.token);
          localStorage.setItem('doctorInfo', JSON.stringify(res.data.doctorInfo));
          
          // 设置全局axios请求头
          api.defaults.headers.common['Authorization'] = `Bearer ${res.data.token}`;
          
          ElMessage.success('登录成功');
          router.push('/home/dashboard');
        } else {
          ElMessage.error(res.message || '登录失败');
        }
      } catch (error) {
        handleApiError(error);
      } finally {
        loading.value = false;
      }
    }
    
    // 检查后端是否可用
    const checkBackendAvailability = async () => {
      try {
        await api.get('/health');
        return true;
      } catch (error) {
        return false;
      }
    }
    
    // 统一的API错误处理
    const handleApiError = (error) => {
      if (error.response) {
        const status = error.response.status;
        const msg = error.response.data?.message || '请求错误';
        
        if (status === 401) {
          ElMessage.error('认证失败: ' + msg);
        } else if (status === 403) {
          ElMessage.error('权限不足: ' + msg);
        } else if (status >= 500) {
          ElMessage.error('服务器错误: ' + msg);
        } else {
          ElMessage.error(`请求错误 (${status}): ${msg}`);
        }
      } else if (error.request) {
        ElMessage.error('网络错误，无法连接到服务器');
        useDemoMode();
      } else {
        ElMessage.error('请求配置错误: ' + error.message);
      }
    }
    
    // 演示模式专用函数
    const useDemoMode = () => {
      const mockDoctorInfo = {
        doctorId: 'D2023001',
        name: loginForm.username || '演示医生',
        department: 'internal',
        departmentName: '内科',
        title: '主治医师',
        phone: '13800138000'
      };
      
      const mockToken = 'demo_' + Math.random().toString(36).slice(2);
      localStorage.setItem('doctorToken', mockToken);
      localStorage.setItem('doctorInfo', JSON.stringify(mockDoctorInfo));
      
      // 设置模拟请求头
      api.defaults.headers.common['Authorization'] = `Bearer ${mockToken}`;
      
      ElMessage.warning('API不可用，已进入演示模式');
      router.push('/home/dashboard');
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