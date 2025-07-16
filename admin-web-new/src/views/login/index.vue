<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo-container">
          <img src="../../assets/logo.png" alt="Logo" class="logo" v-if="false" />
          <h2>医疗预约系统</h2>
        </div>
        <p>管理员登录</p>
      </div>
      <form @submit.prevent="login" class="login-form">
        <div class="form-item">
          <label>用户名</label>
          <div class="input-container">
            <el-icon class="input-icon"><User /></el-icon>
            <input
              type="text"
              v-model="ruleForm.username"
              placeholder="请输入用户名"
              class="form-input"
              autocomplete="off"
            />
          </div>
          <div v-if="formErrors.username" class="error-message">{{ formErrors.username }}</div>
        </div>
        <div class="form-item">
          <label>密码</label>
          <div class="input-container">
            <el-icon class="input-icon"><Lock /></el-icon>
            <input
              :type="passwordVisible ? 'text' : 'password'"
              v-model="ruleForm.password"
              placeholder="请输入密码"
              class="form-input"
              @keyup.enter="login"
              autocomplete="off"
            />
            <el-icon class="password-toggle" @click="passwordVisible = !passwordVisible">
              <View v-if="passwordVisible" />
              <Hide v-else />
            </el-icon>
          </div>
          <div v-if="formErrors.password" class="error-message">{{ formErrors.password }}</div>
        </div>
        <div class="form-item remember-me">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <a href="javascript:void(0)" class="forgot-password">忘记密码?</a>
        </div>
        <div class="form-item">
          <button
            type="submit"
            class="login-button"
            :disabled="loading"
          >
            <span v-if="!loading">登 录</span>
            <span v-else class="loading-spinner"></span>
          </button>
        </div>
      </form>
      <div class="login-footer">
        <p>医疗预约系统 &copy; {{ new Date().getFullYear() }}</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStoreHook } from "@/store/modules/user";
import md5 from "js-md5"
import { NextLoading } from "@/utils/loading"
import { User, Lock, View, Hide } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter();
const loading = ref(false);
const passwordVisible = ref(false);
const rememberMe = ref(false);

const ruleForm = reactive({
  username: '',
  password: ''
})

const formErrors = reactive({
  username: '',
  password: ''
})

const validateForm = () => {
  let isValid = true;
  
  // 清空错误信息
  formErrors.username = '';
  formErrors.password = '';
  
  if (!ruleForm.username.trim()) {
    formErrors.username = '请输入用户名';
    isValid = false;
  }
  
  if (!ruleForm.password) {
    formErrors.password = '请输入密码';
    isValid = false;
  }
  
  return isValid;
}

const login = () => {
  if (!validateForm()) return;
  
  loading.value = true;
  
  useUserStoreHook().login({
    username: ruleForm.username,
    password: md5(ruleForm.password)
  }).then(async () => {
    NextLoading.start();
    await router.push('/')
  }).catch(error => {
    ElMessage.error('登录失败，请检查用户名和密码');
  }).finally(() => {
    loading.value = false;
  })
}

// 页面加载时
onMounted(() => {
  NextLoading.done();
});
</script>

<style lang="scss" scoped>
@import '@/style/variables.scss';

.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: linear-gradient(to right, #4facfe 0%, #00f2fe 100%);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: fixed;
  top: 0;
  left: 0;
}

.login-card {
  width: 400px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  padding: 40px 30px;
  transition: transform 0.3s;
  
  &:hover {
    transform: translateY(-5px);
  }
  
  @media screen and (max-width: 480px) {
    width: 90%;
    padding: 30px 20px;
  }
}

.login-header {
  text-align: center;
  margin-bottom: 35px;
  
  .logo-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 15px;
    
    .logo {
      width: 80px;
      height: 80px;
      margin-bottom: 15px;
      object-fit: contain;
    }
  }
  
  h2 {
    font-size: 28px;
    color: $text-primary;
    margin: 0 0 10px;
    font-weight: 600;
  }
  
  p {
    font-size: 14px;
    color: $text-secondary;
    margin: 0;
  }
}

.login-form {
  margin-bottom: 25px;
}

.form-item {
  margin-bottom: 24px;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  &.remember-me {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  label {
    display: block;
    font-size: 14px;
    color: $text-regular;
    margin-bottom: 8px;
    font-weight: 500;
  }
}

.input-container {
  position: relative;
  
  .input-icon {
    position: absolute;
    left: 12px;
    top: 50%;
    transform: translateY(-50%);
    color: $text-secondary;
    font-size: 18px;
  }
  
  .password-toggle {
    position: absolute;
    right: 12px;
    top: 50%;
    transform: translateY(-50%);
    color: $text-secondary;
    font-size: 18px;
    cursor: pointer;
    
    &:hover {
      color: $primary-color;
    }
  }
}

.form-input {
  width: 100%;
  height: 48px;
  line-height: 48px;
  padding: 0 15px 0 40px;
  font-size: 14px;
  border: 1px solid $border-base;
  border-radius: 6px;
  transition: all 0.3s;
  box-sizing: border-box;
  background-color: #f9fafc;
  
  &:hover {
    border-color: $border-light;
  }
  
  &:focus {
    outline: none;
    border-color: $primary-color;
    background-color: #fff;
    box-shadow: 0 0 0 2px rgba($primary-color, 0.2);
  }
}

.login-button {
  width: 100%;
  height: 48px;
  background-color: $primary-color;
  border: none;
  border-radius: 6px;
  color: white;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    background-color: $primary-dark-color;
  }
  
  &:disabled {
    background-color: $border-light;
    cursor: not-allowed;
  }
}

.forgot-password {
  color: $primary-color;
  font-size: 14px;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

.error-message {
  color: $danger-color;
  font-size: 12px;
  margin-top: 5px;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  
  p {
    color: $text-secondary;
    font-size: 12px;
  }
}

.loading-spinner {
  display: inline-block;
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
