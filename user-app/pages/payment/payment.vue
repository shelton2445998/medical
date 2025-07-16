<template>
  <view class="payment-content">
    <!-- 动态背景装饰 -->
    <view class="floating-shapes">
      <view class="shape shape-1"></view>
      <view class="shape shape-2"></view>
      <view class="shape shape-3"></view>
      <view class="shape shape-4"></view>
    </view>
    
    <!-- 页面头部 -->
    <view class="page-header">
      <view class="header-icon">💳</view>
      <view class="header-title">订单支付</view>
      <view class="header-desc">请选择支付方式完成支付</view>
    </view>
    
    <!-- 订单信息 -->
    <view class="order-info">
      <view class="info-card">
        <view class="info-header">
          <text class="info-icon">📋</text>
          <text class="info-title">订单信息</text>
        </view>
        <view class="info-content">
          <view class="info-row">
            <text class="info-label">订单编号</text>
            <text class="info-value">{{orderInfo.id}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">医院名称</text>
            <text class="info-value">{{orderInfo.hospitalName}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">预约日期</text>
            <text class="info-value">{{orderInfo.appointmentDate}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">预约时间</text>
            <text class="info-value">{{orderInfo.appointmentTime}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">患者姓名</text>
            <text class="info-value">{{orderInfo.patientName}}</text>
          </view>
          <view class="info-row">
            <text class="info-label">订单金额</text>
            <text class="info-value price">¥{{orderInfo.amount}}</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 支付方式选择 -->
    <view class="payment-methods">
      <view class="section-title">
        <text class="title-icon">💳</text>
        <text>选择支付方式</text>
      </view>
      
      <view class="method-list">
        <view 
          class="method-item" 
          v-for="(method, index) in payMethods" 
          :key="index"
          :class="{active: selectedPayMethod === method.value}"
          @click="selectPayMethod(method.value)"
        >
          <view class="method-info">
            <text class="method-icon">{{method.icon}}</text>
            <text class="method-name">{{method.name}}</text>
          </view>
          <view class="method-check">
            <text class="check-icon" v-if="selectedPayMethod === method.value">✓</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 支付按钮 -->
    <view class="payment-actions">
      <button class="pay-btn" @click="confirmPayment" :disabled="!selectedPayMethod || isPaying">
        <text class="btn-icon">{{isPaying ? '⏳' : '💳'}}</text>
        <text class="btn-text">{{isPaying ? '支付中...' : '立即支付'}}</text>
      </button>
    </view>
  </view>
</template>

<script>
import { appointmentApi } from '@/utils/api.js';

export default {
  data() {
    return {
      orderInfo: {},
      payMethods: [
        { name: '微信支付', value: '2', icon: '💚' },
        { name: '支付宝', value: '1', icon: '💙' },
        { name: '医保支付', value: '3', icon: '🏥' }
      ],
      selectedPayMethod: '',
      isPaying: false
    }
  },
  onLoad(options) {
    // 获取订单信息
    const orderData = uni.getStorageSync('currentOrder');
    if (orderData) {
      this.orderInfo = JSON.parse(orderData);
      console.log('支付页面获取到订单信息:', this.orderInfo);
    } else {
      uni.showToast({
        title: '订单信息不存在',
        icon: 'none',
        duration: 2000
      });
      setTimeout(() => {
        uni.navigateBack();
      }, 2000);
    }
  },
  methods: {
    selectPayMethod(value) {
      this.selectedPayMethod = value;
    },
    
    async confirmPayment() {
      if (!this.selectedPayMethod) {
        uni.showToast({
          title: '请选择支付方式',
          icon: 'none',
          duration: 2000
        });
        return;
      }
      
      this.isPaying = true;
      uni.showLoading({ title: '支付中...' });
      
      try {
        // 获取token
        const token = uni.getStorageSync('uniIdToken');
        
        // 调用支付确认接口
        const response = await new Promise((resolve, reject) => {
          uni.request({
            url: appointmentApi.confirmPayment(this.orderInfo.id),
            method: 'PUT',
            header: {
              'Content-Type': 'application/json',
              'Authorization': token || ''
            },
            success: resolve,
            fail: reject
          });
        });
        
        console.log('支付确认接口响应：', response);
        
        if (response.statusCode === 200 && response.data.code === 200) {
          // 支付成功
          uni.hideLoading();
          
          // 清除订单信息
          uni.removeStorageSync('currentOrder');
          
          // 显示成功提示
          uni.showToast({
            title: '支付成功！',
            icon: 'success',
            duration: 2000
          });
          
          // 支付成功后2秒返回首页
          setTimeout(() => {
            uni.reLaunch({ url: '/pages/index/index' });
          }, 2000);
        } else {
          throw new Error(response.data.msg || '支付失败，请重试');
        }
      } catch (error) {
        console.error('支付失败:', error);
        uni.hideLoading();
        uni.showToast({
          title: error.message || '支付失败，请重试',
          icon: 'none',
          duration: 2000
        });
      } finally {
        this.isPaying = false;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.payment-content {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
  min-height: 100vh;
  padding: 20rpx 40rpx 40rpx 40rpx;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
    animation: flow 20s linear infinite;
    pointer-events: none;
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.05) 50%, transparent 70%);
    animation: shimmer 8s ease-in-out infinite;
    pointer-events: none;
  }
}

.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;

  .shape {
    position: absolute;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    filter: blur(50px);
    animation: float 15s infinite ease-in-out;
    
    &.shape-1 {
      width: 100px;
      height: 100px;
      top: 10%;
      left: 10%;
      animation-delay: -2s;
    }
    &.shape-2 {
      width: 150px;
      height: 150px;
      top: 70%;
      left: 30%;
      animation-delay: -5s;
    }
    &.shape-3 {
      width: 120px;
      height: 120px;
      top: 20%;
      right: 20%;
      animation-delay: -8s;
    }
    &.shape-4 {
      width: 180px;
      height: 180px;
      bottom: 10%;
      right: 50%;
      animation-delay: -10s;
    }
  }
}

.page-header {
  text-align: center;
  margin-bottom: 40rpx;
  animation: fadeInDown 0.8s ease-out;
  
  .header-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
    animation: bounce 2s infinite;
  }
  
  .header-title {
    font-size: 44rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 10rpx;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  }
  
  .header-desc {
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.order-info {
  margin-bottom: 40rpx;
  
  .info-card {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 20rpx;
    padding: 30rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10rpx);
    animation: slideInUp 0.6s ease-out;
    
    .info-header {
      display: flex;
      align-items: center;
      margin-bottom: 30rpx;
      
      .info-icon {
        font-size: 32rpx;
        margin-right: 15rpx;
      }
      
      .info-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
      }
    }
    
    .info-content {
      .info-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15rpx 0;
        border-bottom: 1px solid rgba(0, 0, 0, 0.05);
        
        &:last-child {
          border-bottom: none;
        }
        
        .info-label {
          font-size: 28rpx;
          color: #666;
        }
        
        .info-value {
          font-size: 28rpx;
          color: #333;
          font-weight: 500;
          
          &.price {
            color: #e74c3c;
            font-weight: bold;
            font-size: 32rpx;
          }
        }
      }
    }
  }
}

.payment-methods {
  margin-bottom: 40rpx;
  
  .section-title {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .title-icon {
      font-size: 32rpx;
      margin-right: 15rpx;
    }
    
    text {
      font-size: 32rpx;
      font-weight: bold;
      color: #fff;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    }
  }
  
  .method-list {
    .method-item {
      background: rgba(255, 255, 255, 0.95);
      border-radius: 16rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;
      animation: slideInUp 0.6s ease-out;
      
      &:hover {
        transform: translateY(-2rpx);
        box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
      }
      
      &.active {
        background: linear-gradient(135deg, #0984e3, #74b9ff);
        color: #fff;
        box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
        
        .method-name {
          color: #fff;
        }
        
        .check-icon {
          color: #fff;
        }
      }
      
      .method-info {
        display: flex;
        align-items: center;
        
        .method-icon {
          font-size: 40rpx;
          margin-right: 20rpx;
        }
        
        .method-name {
          font-size: 32rpx;
          font-weight: 500;
          color: #333;
        }
      }
      
      .method-check {
        .check-icon {
          font-size: 32rpx;
          color: #0984e3;
          font-weight: bold;
        }
      }
    }
  }
}

.payment-actions {
  .pay-btn {
    width: 100%;
    height: 100rpx;
    background: linear-gradient(135deg, #e74c3c, #c0392b);
    border-radius: 50rpx;
    border: none;
    color: #fff;
    font-size: 32rpx;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(231, 76, 60, 0.3);
    transition: all 0.3s ease;
    animation: slideInUp 0.8s ease-out;
    
    &:hover {
      transform: translateY(-2rpx);
      box-shadow: 0 12rpx 32rpx rgba(231, 76, 60, 0.4);
    }
    
    &:disabled {
      background: #bdc3c7;
      box-shadow: none;
      transform: none;
    }
    
    .btn-icon {
      font-size: 32rpx;
      margin-right: 15rpx;
    }
    
    .btn-text {
      font-size: 32rpx;
    }
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10rpx);
  }
  60% {
    transform: translateY(-5rpx);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  33% {
    transform: translateY(-20px) rotate(120deg);
  }
  66% {
    transform: translateY(10px) rotate(240deg);
  }
}

@keyframes flow {
  0% {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  100% {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

@keyframes shimmer {
  0%, 100% {
    opacity: 0.05;
  }
  50% {
    opacity: 0.1;
  }
}
</style> 