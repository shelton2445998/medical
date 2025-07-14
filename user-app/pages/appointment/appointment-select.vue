<template>
  <view class="select-content">
    <!-- 动态背景装饰 -->
    <view class="floating-shapes">
      <view class="shape shape-1"></view>
      <view class="shape shape-2"></view>
      <view class="shape shape-3"></view>
      <view class="shape shape-4"></view>
    </view>
    
    <!-- 主要内容区域 -->
    <view class="main-content">
      <!-- 欢迎提示 -->
      <view class="welcome-section">
        <view class="welcome-icon">🏥</view>
        <view class="welcome-title">欢迎使用体检预约</view>
        <view class="welcome-desc">请选择您喜欢的预约方式</view>
      </view>
      
      <!-- 选择卡片列表 -->
      <view class="select-list">
        <!-- 普通项目预约卡片 -->
        <view class="select-card" @click="goToNormal" :style="{ animationDelay: '0.1s' }">
          <view class="card-header">
            <view class="card-icon normal-icon">🔬</view>
            <view class="card-badge">推荐</view>
          </view>
          <view class="card-content">
            <view class="card-title">普通项目预约</view>
            <view class="card-desc">选择单项体检项目进行预约，灵活自由</view>
            <view class="card-features">
              <view class="feature-item">
                <text class="feature-dot">•</text>
                <text>自由选择检查项目</text>
              </view>
              <view class="feature-item">
                <text class="feature-dot">•</text>
                <text>价格透明实惠</text>
              </view>
              <view class="feature-item">
                <text class="feature-dot">•</text>
                <text>适合个性化需求</text>
              </view>
            </view>
          </view>
          <view class="card-arrow">
            <text class="arrow-icon">→</text>
          </view>
          
          <!-- 添加按钮区域 -->
          <view class="card-button">
            <button class="action-btn primary-btn" @click.stop="goToNormal">
              选择普通项目
            </button>
          </view>
        </view>
        
        <!-- 套餐预约卡片 -->
        <view class="select-card" @click="goToPackage" :style="{ animationDelay: '0.3s' }">
          <view class="card-header">
            <view class="card-icon package-icon">📋</view>
            <view class="card-badge premium">精选</view>
          </view>
          <view class="card-content">
            <view class="card-title">套餐预约</view>
            <view class="card-desc">选择体检套餐进行预约，全面健康检查</view>
            <view class="card-features">
              <view class="feature-item">
                <text class="feature-dot">•</text>
                <text>专业医生定制</text>
              </view>
              <view class="feature-item">
                <text class="feature-dot">•</text>
                <text>检查项目全面</text>
              </view>
              <view class="feature-item">
                <text class="feature-dot">•</text>
                <text>性价比更高</text>
              </view>
            </view>
          </view>
          <view class="card-arrow">
            <text class="arrow-icon">→</text>
          </view>
          
          <!-- 添加按钮区域 -->
          <view class="card-button">
            <button class="action-btn primary-btn" @click.stop="goToPackage">
              选择套餐预约
            </button>
          </view>
        </view>
      </view>
      
      <!-- 底部提示 -->
      <view class="bottom-tips">
        <view class="tip-item">
          <text class="tip-icon">💡</text>
          <text class="tip-text">首次预约建议选择套餐，检查更全面</text>
    </view>
        <view class="tip-item">
          <text class="tip-icon">⏰</text>
          <text class="tip-text">预约成功后请按时到院体检</text>
</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      memberId: null,
      memberName: ''
    }
  },
  onLoad(options) {
    // 获取家庭成员信息
    if (options.memberId) {
      this.memberId = options.memberId;
    }
    if (options.memberName) {
      this.memberName = options.memberName;
    }
  },
  methods: {
    goToNormal() {
      // 构建跳转URL，包含家庭成员信息
      let normalUrl = '/pages/appointment/normal-appointment';
      if (this.memberId && this.memberName) {
        normalUrl += `?memberId=${this.memberId}&memberName=${this.memberName}`;
      }
      uni.navigateTo({ url: normalUrl });
    },
    goToPackage() {
      // 构建跳转URL，包含家庭成员信息
      let packageUrl = '/pages/package/package';
      if (this.memberId && this.memberName) {
        packageUrl += `?memberId=${this.memberId}&memberName=${this.memberName}`;
      }
      uni.navigateTo({ url: packageUrl });
    }
  }
}
</script>

<style lang="scss" scoped>
.select-content {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
  min-height: 100vh;
  padding-top: 0;
  padding-bottom: 40rpx;
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
    transition: all 0.3s ease;
    
    &::before {
      content: '';
      position: absolute;
      top: -10%;
      left: -10%;
      width: 120%;
      height: 120%;
      background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, transparent 70%);
      border-radius: 50%;
      animation: pulse 3s ease-in-out infinite;
    }

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

@keyframes float {
  0% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
  25% {
    transform: translateY(-20px) translateX(20px) scale(1.1);
    opacity: 0.9;
  }
  50% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
  75% {
    transform: translateY(20px) translateX(-20px) scale(1.1);
    opacity: 0.9;
  }
  100% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
}

@keyframes flow {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes shimmer {
  0%, 100% {
    opacity: 0.3;
    transform: translateX(-100%);
  }
  50% {
    opacity: 0.6;
    transform: translateX(100%);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(50rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
}
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
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

.main-content {
  padding: 20rpx 40rpx 0 40rpx;
}

.welcome-section {
  text-align: center;
  margin-bottom: 20rpx;
  padding: 15rpx 0;
  animation: fadeInDown 1s ease-out;
  
  .welcome-icon {
    font-size: 50rpx;
    margin-bottom: 10rpx;
    filter: drop-shadow(0 4rpx 8rpx rgba(0, 0, 0, 0.2));
    animation: bounce 2s infinite;
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.2);
    }
  }
  
  .welcome-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 8rpx;
    text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;
  }
  
  .welcome-desc {
    font-size: 22rpx;
    color: rgba(255, 255, 255, 0.8);
    transition: all 0.3s ease;
  }
}

.select-list {
  margin-bottom: 30rpx;
}

.select-card {
  background: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 25rpx;
  padding: 25rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  animation: slideInUp 0.8s ease-out forwards;
  opacity: 0;
  transform: translateY(50rpx);
  cursor: pointer;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
    transition: left 0.6s ease;
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(116, 185, 255, 0.1), rgba(9, 132, 227, 0.1));
    opacity: 0;
    transition: opacity 0.3s ease;
    pointer-events: none;
  }
  
  /* 流动治愈感背景 */
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(116, 185, 255, 0.1) 0%, transparent 70%);
    animation: flow 15s linear infinite;
    pointer-events: none;
  }
  
  &:active {
    transform: translateY(-4rpx) scale(0.98);
    box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
  }
  
  &:hover {
    transform: translateY(-12rpx) scale(1.02);
    box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.25);
    background: linear-gradient(135deg, #ffffff, #f8f9ff);
    
    &::before {
      left: 100%;
    }
    
    &::after {
      opacity: 1;
    }
    
    .card-title {
      color: #0984e3;
      transform: translateX(5rpx);
    }
    
    .card-icon {
      transform: scale(1.1) rotate(5deg);
    }
    
    .card-badge {
      transform: scale(1.1);
    }
    
    .arrow-icon {
      transform: translateX(10rpx);
      color: #0984e3;
    }
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    transition: all 0.3s ease;
    
    .card-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 15rpx;
  display: flex;
  align-items: center;
      justify-content: center;
      font-size: 30rpx;
      transition: all 0.3s ease;
      
      &.normal-icon {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
        color: #ffffff;
        box-shadow: 0 4rpx 12rpx rgba(116, 185, 255, 0.3);
}
      
      &.package-icon {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
        color: #ffffff;
        box-shadow: 0 4rpx 12rpx rgba(116, 185, 255, 0.3);
      }
    }
    
    .card-badge {
      padding: 6rpx 16rpx;
      border-radius: 15rpx;
      font-size: 20rpx;
      font-weight: bold;
      color: #ffffff;
      background: linear-gradient(135deg, #74b9ff, #0984e3);
      transition: all 0.3s ease;
      animation: pulse 2s infinite;
      
      &.premium {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
      }
    }
  }
  
  .card-content {
    transition: all 0.3s ease;
    
    .card-title {
      font-size: 32rpx;
  font-weight: bold;
      color: #333333;
      margin-bottom: 12rpx;
      transition: all 0.3s ease;
    }
    
    .card-desc {
      font-size: 24rpx;
      color: #666666;
      margin-bottom: 20rpx;
      line-height: 1.4;
      transition: all 0.3s ease;
    }
    
    .card-features {
      transition: all 0.3s ease;
      
      .feature-item {
        display: flex;
        align-items: center;
  margin-bottom: 8rpx;
        transition: all 0.3s ease;
        
        &:hover {
          transform: translateX(5rpx);
        }
        
        .feature-dot {
          color: #74b9ff;
          font-size: 20rpx;
          margin-right: 10rpx;
          font-weight: bold;
          transition: all 0.3s ease;
        }
        
        text {
          font-size: 22rpx;
          color: #666666;
          transition: all 0.3s ease;
        }
      }
    }
  }
  
  .card-arrow {
    position: absolute;
    right: 25rpx;
    top: 50%;
    transform: translateY(-50%);
    transition: all 0.3s ease;
    
    .arrow-icon {
      font-size: 32rpx;
      color: #74b9ff;
      font-weight: bold;
      transition: all 0.3s ease;
    }
  }
  
  // 添加按钮样式
  .card-button {
    margin-top: 20rpx;
    padding: 15rpx 0;
    border-top: 1rpx solid #f0f0f0;
    transition: all 0.3s ease;
    
    .action-btn {
      width: 100%;
      height: 70rpx;
      border-radius: 35rpx;
      font-size: 26rpx;
      font-weight: bold;
      border: none;
      transition: all 0.3s ease;
      cursor: pointer;
      
      &.primary-btn {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
        color: #ffffff;
        box-shadow: 0 4rpx 12rpx rgba(116, 185, 255, 0.3);
        
        &:hover {
          transform: translateY(-3rpx) scale(1.02);
          box-shadow: 0 8rpx 20rpx rgba(116, 185, 255, 0.5);
          background: linear-gradient(135deg, #0984e3, #74b9ff);
        }
        
        &:active {
          transform: translateY(-1rpx) scale(0.98);
        }
      }
      
      &.secondary-btn {
        background: #ffffff;
        color: #74b9ff;
        border: 2rpx solid #74b9ff;
        
        &:hover {
          background: rgba(116, 185, 255, 0.1);
          transform: translateY(-2rpx);
        }
      }
    }
  }
}

.bottom-tips {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20rpx;
  padding: 30rpx;
  backdrop-filter: blur(10rpx);
  animation: slideInUp 0.8s ease-out 0.4s both;
  opacity: 0;
  transform: translateY(30rpx);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    animation: shimmer 4s ease-in-out infinite;
  }
  
  &:hover {
    background: rgba(255, 255, 255, 0.15);
    transform: translateY(-2rpx);
  }
  
  .tip-item {
    display: flex;
    align-items: center;
    margin-bottom: 12rpx;
    transition: all 0.3s ease;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    &:hover {
      transform: translateX(5rpx);
    }
    
    .tip-icon {
      font-size: 24rpx;
      margin-right: 12rpx;
      filter: drop-shadow(0 2rpx 4rpx rgba(0, 0, 0, 0.2));
      transition: all 0.3s ease;
    }
    
    .tip-text {
      font-size: 22rpx;
      color: rgba(255, 255, 255, 0.8);
      transition: all 0.3s ease;
    }
  }
}

// 响应式设计
@media (max-width: 750rpx) {
  .select-card {
    padding: 30rpx;
    
    .card-content {
      .card-title {
        font-size: 32rpx;
      }
      
      .card-desc {
        font-size: 26rpx;
      }
      
      .card-features {
        .feature-item {
          text {
  font-size: 24rpx;
          }
        }
      }
    }
  }
}
</style> 