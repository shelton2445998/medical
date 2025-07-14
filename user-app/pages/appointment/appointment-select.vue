<template>
  <view class="select-content">
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
        <view class="select-card" @click="goToNormal">
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
        <view class="select-card" @click="goToPackage">
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
  padding-bottom: 40rpx;
}



.main-content {
  padding: 40rpx 40rpx 0 40rpx;
}

.welcome-section {
  text-align: center;
  margin-bottom: 60rpx;
  padding: 40rpx 0;
  
  .welcome-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
    filter: drop-shadow(0 4rpx 8rpx rgba(0, 0, 0, 0.2));
  }
  
  .welcome-title {
    font-size: 44rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 16rpx;
    text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
  }
  
  .welcome-desc {
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.select-list {
  margin-bottom: 60rpx;
}

.select-card {
  background: #ffffff;
  border-radius: 24rpx;
  margin-bottom: 40rpx;
  padding: 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 6rpx;
    background: linear-gradient(90deg, #74b9ff, #0984e3);
  }
  
  &:active {
    transform: translateY(-4rpx);
    box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .card-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 20rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 40rpx;
      
      &.normal-icon {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
        color: #ffffff;
      }
      
      &.package-icon {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
        color: #ffffff;
      }
    }
    
    .card-badge {
      padding: 8rpx 20rpx;
      border-radius: 20rpx;
      font-size: 22rpx;
      font-weight: bold;
      color: #ffffff;
      background: linear-gradient(135deg, #74b9ff, #0984e3);
      
      &.premium {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
      }
    }
  }
  
  .card-content {
    .card-title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333333;
      margin-bottom: 16rpx;
    }
    
    .card-desc {
      font-size: 28rpx;
      color: #666666;
      margin-bottom: 30rpx;
      line-height: 1.5;
    }
    
    .card-features {
      .feature-item {
        display: flex;
        align-items: center;
        margin-bottom: 12rpx;
        
        .feature-dot {
          color: #74b9ff;
          font-size: 24rpx;
          margin-right: 12rpx;
          font-weight: bold;
        }
        
        text {
          font-size: 26rpx;
          color: #666666;
        }
      }
    }
  }
  
  .card-arrow {
    position: absolute;
    right: 40rpx;
    top: 50%;
    transform: translateY(-50%);
    
    .arrow-icon {
      font-size: 40rpx;
      color: #74b9ff;
      font-weight: bold;
    }
  }
  
  // 添加按钮样式
  .card-button {
    margin-top: 30rpx;
    padding: 20rpx 0;
    border-top: 1rpx solid #f0f0f0;
    
    .action-btn {
      width: 100%;
      height: 80rpx;
      border-radius: 40rpx;
      font-size: 28rpx;
      font-weight: bold;
      border: none;
      transition: all 0.3s ease;
      
      &.primary-btn {
        background: linear-gradient(135deg, #74b9ff, #0984e3);
        color: #ffffff;
        box-shadow: 0 4rpx 12rpx rgba(116, 185, 255, 0.3);
        
        &:active {
          transform: translateY(-2rpx);
          box-shadow: 0 6rpx 16rpx rgba(116, 185, 255, 0.4);
        }
      }
      
      &.secondary-btn {
        background: #ffffff;
        color: #74b9ff;
        border: 2rpx solid #74b9ff;
        
        &:active {
          background: rgba(116, 185, 255, 0.1);
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
  
  .tip-item {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .tip-icon {
      font-size: 28rpx;
      margin-right: 16rpx;
    }
    
    .tip-text {
      font-size: 26rpx;
      color: rgba(255, 255, 255, 0.9);
      line-height: 1.4;
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