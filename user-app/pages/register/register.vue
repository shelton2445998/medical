<template>
  <!-- 注册页面容器 -->
  <view class="register-content">
    <!-- 动态背景装饰 -->
    <view class="floating-shapes">
      <!-- 装饰形状1 -->
      <view class="shape shape-1"></view>
      <!-- 装饰形状2 -->
      <view class="shape shape-2"></view>
      <!-- 装饰形状3 -->
      <view class="shape shape-3"></view>
      <!-- 装饰形状4 -->
      <view class="shape shape-4"></view>
    </view>
    
    <!-- 页面头部 -->
    <view class="page-header">
      <!-- 头部图标 -->
      <view class="header-icon">👤</view>
      <!-- 头部标题 -->
      <view class="header-title">用户注册</view>
      <!-- 头部描述 -->
      <view class="header-desc">欢迎加入东软熙心健康</view>
    </view>
    
    <!-- 注册表单 -->
    <view class="register-form">
      <!-- 表单卡片 -->
      <view class="form-card">
        <!-- 卡片头部 -->
        <view class="card-header">
          <!-- 卡片图标 -->
          <view class="card-icon">📝</view>
          <!-- 卡片标题 -->
          <view class="card-title">填写注册信息</view>
          <!-- 卡片描述 -->
          <view class="card-desc">请填写您的个人信息</view>
        </view>
        
        <!-- 表单区域 -->
        <view class="form-section">
          <!-- 用户名输入项 -->
          <view class="form-item">
            <!-- 表单标签 -->
            <text class="form-label">用户名</text>
            <!-- 用户名输入框 -->
            <input 
              class="form-input" 
              v-model="registerForm.username" 
              placeholder="请输入用户名" 
              placeholder-class="input-placeholder"
            />
          </view>
          
          <!-- 密码输入项 -->
          <view class="form-item">
            <!-- 表单标签 -->
            <text class="form-label">登录密码</text>
            <!-- 密码输入框 -->
            <input 
              class="form-input" 
              v-model="registerForm.password" 
              placeholder="请设置登录密码" 
              placeholder-class="input-placeholder"
              type="password" 
              password
            />
          </view>
          
          <!-- 确认密码输入项 -->
          <view class="form-item">
            <!-- 表单标签 -->
            <text class="form-label">确认密码</text>
            <!-- 确认密码输入框 -->
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
        
        <!-- 协议同意区域 -->
        <view class="agreement-section">
          <!-- 复选框组 -->
          <checkbox-group @change="changeAgree">
            <!-- 协议同意标签 -->
            <label class="agreement-label">
              <!-- 复选框 -->
              <checkbox :checked="isAgree" color="#0984e3" style="transform:scale(0.8)" />
              <!-- 协议文本 -->
              <text class="agreement-text">我已阅读并同意</text>
              <!-- 用户协议链接 -->
              <text class="agreement-link" @click.stop="showAgreement('user')">《用户协议》</text>
              <!-- 连接词 -->
              <text class="agreement-text">和</text>
              <!-- 隐私政策链接 -->
              <text class="agreement-link" @click.stop="showAgreement('privacy')">《隐私政策》</text>
            </label>
          </checkbox-group>
        </view>
        
        <!-- 注册按钮 -->
        <button class="register-btn" :disabled="!isAgree || registering" @click="handleRegister">
          <!-- 按钮图标（非注册状态） -->
          <text class="btn-icon" v-if="!registering">👤</text>
          <!-- 加载图标（注册状态） -->
          <text class="btn-loading" v-else>⏳</text>
          <!-- 按钮文本 -->
          <text class="btn-text">{{ registering ? '注册中...' : '立即注册' }}</text>
        </button>
        
        <!-- 登录链接 -->
        <view class="login-link">
          <!-- 链接文本 -->
          <text class="link-text">已有账号？</text>
          <!-- 登录按钮 -->
          <text class="link-btn" @click="navigateTo('/pages/login/login')">立即登录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
// 导入API配置
import { userApi } from '../../utils/api.js';
// 导入HTTP请求方法
import { post } from '../../utils/request.js';

// 导出注册页面组件配置
export default {
  // 组件数据
  data() {
    return {
      // 注册表单数据
      registerForm: {
        username: '', // 用户名
        password: '', // 密码
        confirmPassword: '' // 确认密码
      },
      isAgree: false, // 是否同意协议
      registering: false // 是否正在注册
    }
  },
  // 组件方法
  methods: {
    /**
     * 验证表单数据的方法
     * 检查用户名、密码和协议同意状态
     * @returns {Boolean} 验证是否通过
     */
    validateForm() {
      // 验证用户名不为空
      if (!this.registerForm.username) {
        uni.showToast({
          title: '请输入用户名', // 提示信息
          icon: 'none' // 不显示图标
        });
        return false; // 验证失败
      }
      
      // 验证用户名不为空字符串
      if (!this.registerForm.username.trim()) {
        uni.showToast({
          title: '请输入用户名', // 提示信息
          icon: 'none' // 不显示图标
        });
        return false; // 验证失败
      }
      
      // 验证密码不为空
      if (!this.registerForm.password) {
        uni.showToast({
          title: '请设置登录密码', // 提示信息
          icon: 'none' // 不显示图标
        });
        return false; // 验证失败
      }
      
      // 验证密码长度
      if (this.registerForm.password.length < 6) {
        uni.showToast({
          title: '密码长度不能少于6位', // 提示信息
          icon: 'none' // 不显示图标
        });
        return false; // 验证失败
      }
      
      // 验证两次密码输入一致性
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        uni.showToast({
          title: '两次密码输入不一致', // 提示信息
          icon: 'none' // 不显示图标
        });
        return false; // 验证失败
      }
      
      // 验证是否同意协议
      if (!this.isAgree) {
        uni.showToast({
          title: '请阅读并同意用户协议和隐私政策', // 提示信息
          icon: 'none' // 不显示图标
        });
        return false; // 验证失败
      }
      
      return true; // 验证通过
    },
    
    /**
     * 处理注册请求的异步方法
     * 验证表单数据并调用后端注册接口
     */
    async handleRegister() {
      // 验证表单数据
      if (!this.validateForm()) {
        return; // 验证失败，退出
      }
      
      // 设置注册状态
      this.registering = true;
      
      try {
        // 调用后端注册接口，只发送后端支持的字段
        const response = await post(userApi.register, {
          username: this.registerForm.username, // 用户名
          password: this.registerForm.password // 密码
        });
        
        // 判断注册是否成功
        if (response.code === 200) {
          // 显示成功提示
          uni.showToast({
            title: '注册成功', // 提示信息
            icon: 'success' // 成功图标
          });
          
          // 注册成功后跳转到登录页
          setTimeout(() => {
            uni.redirectTo({
              url: '/pages/login/login' // 登录页面路径
            });
          }, 1500); // 延迟1.5秒
        } else {
          // 显示错误提示
          uni.showToast({
            title: response.msg || '注册失败', // 错误信息
            icon: 'none' // 不显示图标
          });
        }
      } catch (error) {
        // 捕获异常并输出错误日志
        console.error('注册失败:', error);
        // 显示通用错误提示
        uni.showToast({
          title: '注册失败，请稍后重试', // 错误提示信息
          icon: 'none' // 不显示图标
        });
      } finally {
        // 无论成功还是失败，都重置注册状态
        this.registering = false;
      }
    },
    
    /**
     * 切换协议同意状态的方法
     * 根据复选框的选中状态更新协议同意状态
     * @param {Object} e - 复选框change事件对象
     */
    changeAgree(e) {
      // 根据复选框的值长度判断是否同意协议
      this.isAgree = e.detail.value.length > 0;
    },
    
    /**
     * 显示协议页面的方法
     * 根据协议类型跳转到相应的协议页面
     * @param {String} type - 协议类型（'user'用户协议 或 'privacy'隐私政策）
     */
    showAgreement(type) {
      // 根据类型确定协议标题
      const title = type === 'user' ? '用户协议' : '隐私政策';
      // 跳转到协议页面
      uni.navigateTo({
        url: `/pages/agreement/agreement?type=${type}&title=${title}` // 协议页面路径
      });
    },
    
    /**
     * 页面跳转通用方法
     * 使用uni.navigateTo进行页面跳转
     * @param {String} url - 目标页面URL
     */
    navigateTo(url) {
      // 使用uni.navigateTo进行页面跳转
      uni.navigateTo({
        url: url // 目标页面URL
      });
    }
  }
}
</script>

<style lang="scss">
/* 注册页面容器样式 */
.register-content {
  min-height: 100vh; /* 最小高度为视口高度 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 渐变背景 */
  position: relative; /* 相对定位 */
  overflow: hidden; /* 隐藏溢出内容 */
}

/* 动态背景装饰样式 */
.floating-shapes {
  position: absolute; /* 绝对定位 */
  top: 0; /* 顶部对齐 */
  left: 0; /* 左侧对齐 */
  width: 100%; /* 宽度100% */
  height: 100%; /* 高度100% */
  pointer-events: none; /* 不响应指针事件 */
  z-index: 1; /* 层级设置 */
  
  /* 装饰形状通用样式 */
  .shape {
    position: absolute; /* 绝对定位 */
    border-radius: 50%; /* 圆形 */
    background: rgba(255, 255, 255, 0.1); /* 半透明白色背景 */
    animation: float 6s ease-in-out infinite; /* 浮动动画 */
    
    /* 装饰形状1 */
    &.shape-1 {
      width: 80rpx; /* 宽度 */
      height: 80rpx; /* 高度 */
      top: 10%; /* 距离顶部10% */
      left: 10%; /* 距离左侧10% */
      animation-delay: 0s; /* 动画延迟 */
    }
    
    /* 装饰形状2 */
    &.shape-2 {
      width: 120rpx; /* 宽度 */
      height: 120rpx; /* 高度 */
      top: 20%; /* 距离顶部20% */
      right: 15%; /* 距离右侧15% */
      animation-delay: 2s; /* 动画延迟2秒 */
    }
    
    /* 装饰形状3 */
    &.shape-3 {
      width: 60rpx; /* 宽度 */
      height: 60rpx; /* 高度 */
      bottom: 30%; /* 距离底部30% */
      left: 20%; /* 距离左侧20% */
      animation-delay: 4s; /* 动画延迟4秒 */
    }
    
    /* 装饰形状4 */
    &.shape-4 {
      width: 100rpx; /* 宽度 */
      height: 100rpx; /* 高度 */
      bottom: 20%; /* 距离底部20% */
      right: 10%; /* 距离右侧10% */
      animation-delay: 1s; /* 动画延迟1秒 */
    }
  }
}

/* 浮动动画效果 */
@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg); /* 初始和结束位置 */
  }
  50% {
    transform: translateY(-20px) rotate(180deg); /* 中间位置 */
  }
}

/* 页面头部样式 */
.page-header {
  position: relative; /* 相对定位 */
  z-index: 2; /* 层级设置 */
  text-align: center; /* 文本居中 */
  padding: 60rpx 40rpx 40rpx; /* 内边距 */
  
  /* 头部图标样式 */
  .header-icon {
    font-size: 80rpx; /* 字体大小 */
    margin-bottom: 20rpx; /* 底部边距 */
    animation: bounce 2s ease-in-out infinite; /* 弹跳动画 */
  }
  
  /* 头部标题样式 */
  .header-title {
    font-size: 48rpx; /* 字体大小 */
    font-weight: bold; /* 字体粗细 */
    color: #ffffff; /* 文字颜色 */
    margin-bottom: 10rpx; /* 底部边距 */
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3); /* 文字阴影 */
  }
  
  /* 头部描述样式 */
  .header-desc {
    font-size: 28rpx; /* 字体大小 */
    color: rgba(255, 255, 255, 0.8); /* 半透明白色 */
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3); /* 文字阴影 */
  }
}

/* 弹跳动画效果 */
@keyframes bounce {
  0%, 100% {
    transform: translateY(0); /* 初始和结束位置 */
  }
  50% {
    transform: translateY(-10rpx); /* 中间位置 */
  }
}

/* 注册表单样式 */
.register-form {
  position: relative; /* 相对定位 */
  z-index: 2; /* 层级设置 */
  padding: 0 40rpx 60rpx; /* 内边距 */
  
  /* 表单卡片样式 */
  .form-card {
    background: rgba(255, 255, 255, 0.95); /* 半透明白色背景 */
    border-radius: 30rpx; /* 圆角 */
    padding: 40rpx; /* 内边距 */
    box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.1); /* 阴影效果 */
    backdrop-filter: blur(10rpx); /* 背景模糊效果 */
    animation: slideUp 0.6s ease-out; /* 滑入动画 */
  }
  
  /* 卡片头部样式 */
  .card-header {
    text-align: center; /* 文本居中 */
    margin-bottom: 40rpx; /* 底部边距 */
    
    /* 卡片图标样式 */
    .card-icon {
      font-size: 60rpx; /* 字体大小 */
      margin-bottom: 20rpx; /* 底部边距 */
    }
    
    /* 卡片标题样式 */
    .card-title {
      font-size: 36rpx; /* 字体大小 */
      font-weight: bold; /* 字体粗细 */
      color: #333333; /* 文字颜色 */
      margin-bottom: 10rpx; /* 底部边距 */
    }
    
    /* 卡片描述样式 */
    .card-desc {
      font-size: 26rpx; /* 字体大小 */
      color: #666666; /* 文字颜色 */
    }
  }
}

/* 滑入动画效果 */
@keyframes slideUp {
  from {
    opacity: 0; /* 初始透明度 */
    transform: translateY(50rpx); /* 初始位置 */
  }
  to {
    opacity: 1; /* 最终透明度 */
    transform: translateY(0); /* 最终位置 */
  }
}

/* 表单区域样式 */
.form-section {
  /* 表单项样式 */
  .form-item {
    margin-bottom: 30rpx; /* 底部边距 */
    
    /* 表单标签样式 */
    .form-label {
      display: block; /* 块级元素 */
      font-size: 28rpx; /* 字体大小 */
      color: #333333; /* 文字颜色 */
      margin-bottom: 15rpx; /* 底部边距 */
      font-weight: 500; /* 字体粗细 */
    }
    
    /* 表单输入框样式 */
    .form-input {
      width: 100%; /* 宽度100% */
      height: 80rpx; /* 高度 */
      background: #f8f9fa; /* 背景色 */
      border: 2rpx solid #e9ecef; /* 边框 */
      border-radius: 15rpx; /* 圆角 */
      padding: 0 20rpx; /* 内边距 */
      font-size: 28rpx; /* 字体大小 */
      color: #333333; /* 文字颜色 */
      transition: all 0.3s ease; /* 过渡效果 */
      
      /* 获得焦点时的样式 */
      &:focus {
        border-color: #0984e3; /* 边框颜色 */
        background: #ffffff; /* 背景色 */
        box-shadow: 0 0 0 3rpx rgba(9, 132, 227, 0.1); /* 阴影效果 */
      }
    }
    
    /* 表单多行文本框样式 */
    .form-textarea {
      width: 100%; /* 宽度100% */
      min-height: 120rpx; /* 最小高度 */
      background: #f8f9fa; /* 背景色 */
      border: 2rpx solid #e9ecef; /* 边框 */
      border-radius: 15rpx; /* 圆角 */
      padding: 20rpx; /* 内边距 */
      font-size: 28rpx; /* 字体大小 */
      color: #333333; /* 文字颜色 */
      transition: all 0.3s ease; /* 过渡效果 */
      
      /* 获得焦点时的样式 */
      &:focus {
        border-color: #0984e3; /* 边框颜色 */
        background: #ffffff; /* 背景色 */
        box-shadow: 0 0 0 3rpx rgba(9, 132, 227, 0.1); /* 阴影效果 */
      }
    }
    
    /* 选择器显示样式 */
    .picker-display {
      width: 100%; /* 宽度100% */
      height: 80rpx; /* 高度 */
      background: #f8f9fa; /* 背景色 */
      border: 2rpx solid #e9ecef; /* 边框 */
      border-radius: 15rpx; /* 圆角 */
      padding: 0 20rpx; /* 内边距 */
      display: flex; /* 弹性布局 */
      align-items: center; /* 垂直居中 */
      justify-content: space-between; /* 两端对齐 */
      transition: all 0.3s ease; /* 过渡效果 */
      
      /* 按下时的样式 */
      &:active {
        border-color: #0984e3; /* 边框颜色 */
        background: #ffffff; /* 背景色 */
        box-shadow: 0 0 0 3rpx rgba(9, 132, 227, 0.1); /* 阴影效果 */
      }
      
      /* 选择器文本样式 */
      .picker-text {
        font-size: 28rpx; /* 字体大小 */
        color: #333333; /* 文字颜色 */
        
        /* 占位符样式 */
        &.placeholder {
          color: #999999; /* 占位符颜色 */
        }
      }
      
      /* 选择器图标样式 */
      .picker-icon {
        font-size: 32rpx; /* 字体大小 */
        color: #666666; /* 图标颜色 */
      }
    }
  }
}

/* 输入框占位符样式 */
.input-placeholder {
  color: #999999 !important; /* 占位符颜色 */
}

/* 协议区域样式 */
.agreement-section {
  margin: 40rpx 0; /* 上下边距 */
  
  /* 协议标签样式 */
  .agreement-label {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    flex-wrap: wrap; /* 允许换行 */
    font-size: 26rpx; /* 字体大小 */
    color: #666666; /* 文字颜色 */
    line-height: 1.5; /* 行高 */
  }
  
  /* 协议文本样式 */
  .agreement-text {
    margin: 0 5rpx; /* 左右边距 */
  }
  
  /* 协议链接样式 */
  .agreement-link {
    color: #0984e3; /* 链接颜色 */
    text-decoration: underline; /* 下划线 */
  }
}

/* 注册按钮样式 */
.register-btn {
  width: 100%; /* 宽度100% */
  height: 90rpx; /* 高度 */
  background: linear-gradient(135deg, #0984e3 0%, #00a8ff 100%); /* 渐变背景 */
  border-radius: 45rpx; /* 圆角 */
  border: none; /* 无边框 */
  display: flex; /* 弹性布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  font-size: 32rpx; /* 字体大小 */
  color: #ffffff; /* 文字颜色 */
  font-weight: bold; /* 字体粗细 */
  margin: 40rpx 0; /* 上下边距 */
  box-shadow: 0 10rpx 30rpx rgba(9, 132, 227, 0.3); /* 阴影效果 */
  transition: all 0.3s ease; /* 过渡效果 */
  
  /* 按下时的样式 */
  &:active {
    transform: translateY(2rpx); /* 向下偏移 */
    box-shadow: 0 5rpx 15rpx rgba(9, 132, 227, 0.4); /* 阴影效果 */
  }
  
  /* 禁用状态样式 */
  &:disabled {
    background: #cccccc; /* 背景色 */
    box-shadow: none; /* 无阴影 */
    transform: none; /* 无变换 */
  }
  
  /* 按钮图标样式 */
  .btn-icon {
    font-size: 32rpx; /* 字体大小 */
    margin-right: 10rpx; /* 右边距 */
  }
  
  /* 加载图标样式 */
  .btn-loading {
    font-size: 32rpx; /* 字体大小 */
    margin-right: 10rpx; /* 右边距 */
    animation: spin 1s linear infinite; /* 旋转动画 */
  }
  
  /* 按钮文本样式 */
  .btn-text {
    font-size: 32rpx; /* 字体大小 */
  }
}

/* 旋转动画效果 */
@keyframes spin {
  from {
    transform: rotate(0deg); /* 初始角度 */
  }
  to {
    transform: rotate(360deg); /* 最终角度 */
  }
}

/* 登录链接样式 */
.login-link {
  text-align: center; /* 文本居中 */
  
  /* 链接文本样式 */
  .link-text {
    font-size: 26rpx; /* 字体大小 */
    color: #666666; /* 文字颜色 */
  }
  
  /* 登录按钮样式 */
  .link-btn {
    font-size: 26rpx; /* 字体大小 */
    color: #0984e3; /* 文字颜色 */
    margin-left: 10rpx; /* 左边距 */
  }
}
</style> 