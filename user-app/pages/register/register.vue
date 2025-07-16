<template>
  <view class="register-content">
    <!-- 动态背景装饰 -->
    <view class="floating-shapes">
      <view class="shape shape-1"></view>
      <view class="shape shape-2"></view>
      <view class="shape shape-3"></view>
      <view class="shape shape-4"></view>
    </view>
    
    <!-- 页面头部 -->
    <view class="page-header">
      <view class="header-icon">👤</view>
      <view class="header-title">用户注册</view>
      <view class="header-desc">欢迎加入熙康健康</view>
    </view>
    
    <!-- 注册表单 -->
    <view class="register-form">
      <view class="form-card">
        <view class="card-header">
          <view class="card-icon">📝</view>
          <view class="card-title">填写注册信息</view>
          <view class="card-desc">请填写您的个人信息</view>
        </view>
        
        <view class="form-section">
          <!-- 用户名 -->
          <view class="form-item">
            <text class="form-label">用户名</text>
            <input 
              class="form-input" 
              v-model="registerForm.username" 
              placeholder="请输入用户名（至少5个字符）" 
              placeholder-class="input-placeholder"
            />
          </view>
          
          <!-- 密码 -->
          <view class="form-item">
            <text class="form-label">登录密码</text>
            <input 
              class="form-input" 
              v-model="registerForm.password" 
              placeholder="请设置登录密码" 
              placeholder-class="input-placeholder"
              type="password" 
              password
            />
          </view>
          
          <!-- 确认密码 -->
          <view class="form-item">
            <text class="form-label">确认密码</text>
            <input 
              class="form-input" 
              v-model="registerForm.confirmPassword" 
              placeholder="请确认登录密码" 
              placeholder-class="input-placeholder"
              type="password" 
              password
            />
          </view>
        </view>
        
        <!-- 协议同意 -->
        <view class="agreement-section">
          <checkbox-group @change="changeAgree">
            <label class="agreement-label">
              <checkbox :checked="isAgree" color="#0984e3" style="transform:scale(0.8)" />
              <text class="agreement-text">我已阅读并同意</text>
              <text class="agreement-link" @click.stop="showAgreement('user')">《用户协议》</text>
              <text class="agreement-text">和</text>
              <text class="agreement-link" @click.stop="showAgreement('privacy')">《隐私政策》</text>
            </label>
          </checkbox-group>
        </view>
        
        <!-- 注册按钮 -->
        <button class="register-btn" :disabled="!isAgree || registering" @click="handleRegister">
          <text class="btn-icon" v-if="!registering">👤</text>
          <text class="btn-loading" v-else>⏳</text>
          <text class="btn-text">{{ registering ? '注册中...' : '立即注册' }}</text>
        </button>
        
        <!-- 登录链接 -->
        <view class="login-link">
          <text class="link-text">已有账号？</text>
          <text class="link-btn" @click="navigateTo('/pages/login/login')">立即登录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { userApi } from '../../utils/api.js';
import { post } from '../../utils/request.js';

export default {
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      isAgree: false,
      registering: false
    }
  },
  methods: {
    // 验证表单
    validateForm() {
      if (!this.registerForm.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        });
        return false;
      }
      
      if (this.registerForm.username.length < 5) {
        uni.showToast({
          title: '用户名长度不能少于5个字符',
          icon: 'none'
        });
        return false;
      }
      
      if (!this.registerForm.password) {
        uni.showToast({
          title: '请设置登录密码',
          icon: 'none'
        });
        return false;
      }
      
      if (this.registerForm.password.length < 6) {
        uni.showToast({
          title: '密码长度不能少于6位',
          icon: 'none'
        });
        return false;
      }
      
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        uni.showToast({
          title: '两次密码输入不一致',
          icon: 'none'
        });
        return false;
      }
      
      if (!this.isAgree) {
        uni.showToast({
          title: '请阅读并同意用户协议和隐私政策',
          icon: 'none'
        });
        return false;
      }
      
      return true;
    },
    
    // 处理注册
    async handleRegister() {
      if (!this.validateForm()) {
        return;
      }
      
      this.registering = true;
      
      try {
        // 调用后端注册接口，只发送后端支持的字段
        const response = await post(userApi.register, {
          username: this.registerForm.username,
          password: this.registerForm.password
        });
        
        if (response.code === 200) {
          uni.showToast({
            title: '注册成功',
            icon: 'success'
          });
          
          // 注册成功后跳转到登录页
          setTimeout(() => {
            uni.redirectTo({
              url: '/pages/login/login'
            });
          }, 1500);
        } else {
          uni.showToast({
            title: response.msg || '注册失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('注册失败:', error);
        uni.showToast({
          title: '注册失败，请稍后重试',
          icon: 'none'
        });
      } finally {
        this.registering = false;
      }
    },
    
    // 切换协议同意状态
    changeAgree(e) {
      this.isAgree = e.detail.value.length > 0;
    },
    
    // 显示协议
    showAgreement(type) {
      const title = type === 'user' ? '用户协议' : '隐私政策';
      uni.navigateTo({
        url: `/pages/agreement/agreement?type=${type}&title=${title}`
      });
    },
    
    // 页面跳转
    navigateTo(url) {
      uni.navigateTo({
        url: url
      });
    }
  }
}
</script>

<style lang="scss">
.register-content {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

// 动态背景装饰
.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  
  .shape {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
    animation: float 6s ease-in-out infinite;
    
    &.shape-1 {
      width: 80rpx;
      height: 80rpx;
      top: 10%;
      left: 10%;
      animation-delay: 0s;
    }
    
    &.shape-2 {
      width: 120rpx;
      height: 120rpx;
      top: 20%;
      right: 15%;
      animation-delay: 2s;
    }
    
    &.shape-3 {
      width: 60rpx;
      height: 60rpx;
      bottom: 30%;
      left: 20%;
      animation-delay: 4s;
    }
    
    &.shape-4 {
      width: 100rpx;
      height: 100rpx;
      bottom: 20%;
      right: 10%;
      animation-delay: 1s;
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

// 页面头部
.page-header {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 60rpx 40rpx 40rpx;
  
  .header-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
    animation: bounce 2s ease-in-out infinite;
  }
  
  .header-title {
    font-size: 48rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 10rpx;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  }
  
  .header-desc {
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10rpx);
  }
}

// 注册表单
.register-form {
  position: relative;
  z-index: 2;
  padding: 0 40rpx 60rpx;
  
  .form-card {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 30rpx;
    padding: 40rpx;
    box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10rpx);
    animation: slideUp 0.6s ease-out;
  }
  
  .card-header {
    text-align: center;
    margin-bottom: 40rpx;
    
    .card-icon {
      font-size: 60rpx;
      margin-bottom: 20rpx;
    }
    
    .card-title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333333;
      margin-bottom: 10rpx;
    }
    
    .card-desc {
      font-size: 26rpx;
      color: #666666;
    }
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(50rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 表单区域
.form-section {
  .form-item {
    margin-bottom: 30rpx;
    
    .form-label {
      display: block;
      font-size: 28rpx;
      color: #333333;
      margin-bottom: 15rpx;
      font-weight: 500;
    }
    
    .form-input {
      width: 100%;
      height: 80rpx;
      background: #f8f9fa;
      border: 2rpx solid #e9ecef;
      border-radius: 15rpx;
      padding: 0 20rpx;
      font-size: 28rpx;
      color: #333333;
      transition: all 0.3s ease;
      
      &:focus {
        border-color: #0984e3;
        background: #ffffff;
        box-shadow: 0 0 0 3rpx rgba(9, 132, 227, 0.1);
      }
    }
    
    .form-textarea {
      width: 100%;
      min-height: 120rpx;
      background: #f8f9fa;
      border: 2rpx solid #e9ecef;
      border-radius: 15rpx;
      padding: 20rpx;
      font-size: 28rpx;
      color: #333333;
      transition: all 0.3s ease;
      
      &:focus {
        border-color: #0984e3;
        background: #ffffff;
        box-shadow: 0 0 0 3rpx rgba(9, 132, 227, 0.1);
      }
    }
    
    .picker-display {
      width: 100%;
      height: 80rpx;
      background: #f8f9fa;
      border: 2rpx solid #e9ecef;
      border-radius: 15rpx;
      padding: 0 20rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      transition: all 0.3s ease;
      
      &:active {
        border-color: #0984e3;
        background: #ffffff;
        box-shadow: 0 0 0 3rpx rgba(9, 132, 227, 0.1);
      }
      
      .picker-text {
        font-size: 28rpx;
        color: #333333;
        
        &.placeholder {
          color: #999999;
        }
      }
      
      .picker-icon {
        font-size: 32rpx;
        color: #666666;
      }
    }
  }
}

.input-placeholder {
  color: #999999 !important;
}

// 协议区域
.agreement-section {
  margin: 40rpx 0;
  
  .agreement-label {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    font-size: 26rpx;
    color: #666666;
    line-height: 1.5;
  }
  
  .agreement-text {
    margin: 0 5rpx;
  }
  
  .agreement-link {
    color: #0984e3;
    text-decoration: underline;
  }
}

// 注册按钮
.register-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #0984e3 0%, #00a8ff 100%);
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  color: #ffffff;
  font-weight: bold;
  margin: 40rpx 0;
  box-shadow: 0 10rpx 30rpx rgba(9, 132, 227, 0.3);
  transition: all 0.3s ease;
  
  &:active {
    transform: translateY(2rpx);
    box-shadow: 0 5rpx 15rpx rgba(9, 132, 227, 0.4);
  }
  
  &:disabled {
    background: #cccccc;
    box-shadow: none;
    transform: none;
  }
  
  .btn-icon {
    font-size: 32rpx;
    margin-right: 10rpx;
  }
  
  .btn-loading {
    font-size: 32rpx;
    margin-right: 10rpx;
    animation: spin 1s linear infinite;
  }
  
  .btn-text {
    font-size: 32rpx;
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

// 登录链接
.login-link {
  text-align: center;
  margin-top: 30rpx;
  
  .link-text {
    font-size: 28rpx;
    color: #666666;
  }
  
  .link-btn {
    font-size: 28rpx;
    color: #0984e3;
    margin-left: 10rpx;
    text-decoration: underline;
  }
}
</style> 