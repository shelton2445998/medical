<template>
  <view class="content">
    <view class="page-header">
      <view class="back-btn" @click="goBack">
        <text class="iconfont icon-back"></text>
      </view>
      <!-- <text class="page-title">资讯详情</text> -->
    </view>
    
    <!-- 加载进度条 -->
    <view class="loading-container" v-if="loading">
      <view class="loading-content">
        <view class="progress-bar">
          <view class="progress-inner" :style="{ width: progressWidth + '%' }">
            <view class="progress-glow"></view>
          </view>
        </view>
        <text class="loading-text">正在加载 {{ Math.floor(progressWidth) }}%</text>
      </view>
    </view>
    
    <!-- 只有在不加载时才显示web-view -->
    <web-view v-if="!loading" :src="webUrl" @onLoad="onWebViewLoaded" @onError="onWebViewError"></web-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      webUrl: '',
      loading: true,
      progressWidth: 0,
      progressInterval: null,
      webViewReady: false
    }
  },
  onLoad(options) {
    if (options.url) {
      this.webUrl = decodeURIComponent(options.url);
      // 启动模拟进度条
      this.startProgressAnimation();
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },
    // 启动进度条动画
    startProgressAnimation() {
      this.loading = true;
      this.progressWidth = 0;
      
      // 清除可能存在的旧定时器
      if (this.progressInterval) {
        clearInterval(this.progressInterval);
      }
      
      // 创建一个模拟进度的定时器，使进度增长更自然
      this.progressInterval = setInterval(() => {
        // 允许进度条持续增长，但增长速度随进度增加而减慢
        let increment;
        
        if (this.progressWidth < 30) {
          increment = 1.5; // 开始阶段快速增长
        } else if (this.progressWidth < 60) {
          increment = 0.8; // 中间阶段中速增长
        } else if (this.progressWidth < 90) {
          increment = 0.3; // 后期缓慢增长
        } else if (this.progressWidth < 98) {
          increment = 0.05; // 接近完成时极慢增长
        } else {
          increment = 0; // 到达98%后停止自动增长，等待加载完成
        }
        
        this.progressWidth += increment;
      }, 100);
      
      // 添加超时保护，确保即使WebView加载异常也能完成进度条
      setTimeout(() => {
        if (this.loading && this.progressWidth < 100) {
          console.log('WebView加载超时，自动完成进度条');
          this.completeProgress();
        }
      }, 15000); // 15秒超时
    },
    // WebView加载完成
    onWebViewLoaded() {
      console.log('WebView加载完成');
      this.webViewReady = true;
      // 快速完成进度条
      this.completeProgress();
    },
    // WebView加载出错
    onWebViewError(err) {
      console.error('WebView加载出错', err);
      // 显示错误信息
      uni.showToast({
        title: '加载失败，请检查网络',
        icon: 'none',
        duration: 2000
      });
      // 完成进度条
      this.completeProgress();
    },
    // 完成进度条
    completeProgress() {
      // 快速将进度条完成到100%
      const finishAnimation = setInterval(() => {
        this.progressWidth += (100 - this.progressWidth) / 10;
        if (this.progressWidth >= 99.5) {
          this.progressWidth = 100;
          clearInterval(finishAnimation);
          
          // 短暂延迟后隐藏进度条，显示网页内容
          setTimeout(() => {
            this.loading = false;
          }, 500);
        }
      }, 30);
      
      // 清除主进度条定时器
      if (this.progressInterval) {
        clearInterval(this.progressInterval);
        this.progressInterval = null;
      }
    }
  },
  // 组件销毁时清除定时器
  beforeDestroy() {
    if (this.progressInterval) {
      clearInterval(this.progressInterval);
      this.progressInterval = null;
    }
  }
}
</script>

<style lang="scss">
/* 样式与你的其他页面保持一致 */
.page-header {
  position: relative;
  background-color: #1296db;
  padding: 20rpx 0;
  text-align: center;
  
  .back-btn {
    position: absolute;
    left: 30rpx;
    top: 50%;
    transform: translateY(-50%);
    width: 60rpx;
    height: 60rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    
    .iconfont {
      color: #ffffff;
      font-size: 40rpx;
    }
  }
  
  .page-title {
    color: #ffffff;
    font-size: 36rpx;
    font-weight: bold;
  }
}

/* 加载容器样式 */
.loading-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ffffff;
  z-index: 999;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  
  .loading-content {
    width: 80%;
    max-width: 600rpx;
  }
  
  .progress-bar {
    height: 8rpx;
    background-color: #f0f0f0;
    border-radius: 4rpx;
    overflow: hidden;
    box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
    margin-bottom: 20rpx;
    
    .progress-inner {
      height: 100%;
      background: linear-gradient(90deg, #1296db, #39c5bb);
      border-radius: 4rpx;
      transition: width 0.2s ease;
      position: relative;
      
      .progress-glow {
        position: absolute;
        top: 0;
        right: 0;
        height: 100%;
        width: 20rpx;
        background: linear-gradient(90deg, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.8));
        animation: glow 1.5s infinite;
      }
    }
  }
  
  .loading-text {
    font-size: 26rpx;
    color: #666666;
    text-align: center;
  }
}

@keyframes glow {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(500%);
  }
}
</style>