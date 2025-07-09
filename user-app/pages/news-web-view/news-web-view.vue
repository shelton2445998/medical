<template>
  <view class="content">
    <view class="page-header">
      <view class="back-btn" @click="goBack">
        <text class="iconfont icon-back"></text>
      </view>
      <text class="page-title">资讯详情</text>
    </view>
    <web-view :src="webUrl"></web-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      webUrl: ''
    }
  },
  onLoad(options) {
    if (options.url) {
      this.webUrl = decodeURIComponent(options.url);
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
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