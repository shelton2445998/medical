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
      <view class="progress-bar">
        <view class="progress-inner" :style="{ width: progressWidth + '%' }"></view>
      </view>
      <text class="loading-text">正在加载 {{ Math.floor(progressWidth) }}%</text>
    </view>
    
    <web-view :src="webUrl" @onLoad="onWebViewLoaded" @onError="onWebViewError"></web-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      webUrl: '',
      loading: true,
      progressWidth: 0,
      progressInterval: null
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
      
      // 创建一个模拟进度的定时器
      this.progressInterval = setInterval(() => {
        if (this.progressWidth < 90) {
          // 进度增加速度随着进度增大而减慢
          const increment = (100 - this.progressWidth) / 50;
          this.progressWidth += increment;
        }
      }, 100);
    },
    // WebView加载完成
    onWebViewLoaded() {
      console.log('WebView加载完成');
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
      this.progressWidth = 100;
      
      // 清除进度条定时器
      if (this.progressInterval) {
        clearInterval(this.progressInterval);
        this.progressInterval = null;
      }
      
      // 短暂延迟后隐藏进度条
      setTimeout(() => {
        this.loading = false;
      }, 300);
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
</style>