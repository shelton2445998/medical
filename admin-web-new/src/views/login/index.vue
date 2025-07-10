<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>医疗预约系统</h2>
        <p>管理员登录</p>
      </div>
      <form @submit.prevent="login" class="login-form">
        <div class="form-item">
          <label>用户名</label>
          <input
            type="text"
            v-model="ruleForm.username"
            placeholder="请输入用户名"
            class="form-input"
          />
        </div>
        <div class="form-item">
          <label>密码</label>
          <input
            type="password"
            v-model="ruleForm.password"
            placeholder="请输入密码"
            class="form-input"
            @keyup.enter="login"
          />
        </div>
        <div class="form-item">
          <button
            type="submit"
            class="login-button"
          >
            登录
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {useUserStoreHook} from "@/store/modules/user";
import md5 from "js-md5"
import {NextLoading} from "@/utils/loading"

const router = useRouter();

const ruleForm = reactive({
  username: '',
  password: ''
})
const rules = reactive({
  username: [{
    required: true,
    message: '请输入用户名',
    trigger: 'blur',
  },],
  password: [{
    required: true,
    message: '请输入密码',
    trigger: 'blur',
  },],
})
const login = () => {
  useUserStoreHook().login({
    username: ruleForm.username,
    password: md5(ruleForm.password)
  }).then(async () => {
    NextLoading.start();
    await router.push('/')
  })
}
// 页面加载时
onMounted(() => {
  NextLoading.done();
});
</script>

<style scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("../../assets/login-bg.png");
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
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
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

.login-form {
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 20px;
}

.form-item label {
  display: block;
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.form-input {
  width: 100%;
  height: 40px;
  line-height: 40px;
  padding: 0 15px;
  font-size: 14px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-input:hover {
  border-color: #c0c4cc;
}

.form-input:focus {
  outline: none;
  border-color: #409eff;
}

.login-button {
  width: 100%;
  height: 40px;
  background-color: #409eff;
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.login-button:hover {
  background-color: #66b1ff;
}

.login-button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}
</style>
