<template>
  <view class="select-content">
    <view class="header-bar">
      <view class="back-btn" @click="goHome">
        <text class="iconfont icon-back"></text>
      </view>
      <view class="header">请选择预约方式</view>
    </view>
    <view class="select-list">
      <view class="select-item" @click="goToNormal" style="display: flex; flex-direction: column;">
  <view class="select-title">普通项目预约</view>
  <view class="select-desc">选择单项体检项目进行预约</view>
</view>
      <view class="select-item" @click="goToPackage" style="display: flex; flex-direction: column;">
        <view class="select-title">套餐预约</view>
        <view class="select-desc">选择体检套餐进行预约，可查看套餐详情</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    goHome() {
      uni.reLaunch({ url: '/pages/index/index' });
    },
    goToNormal() {
      uni.navigateTo({ url: '/pages/appointment/appointment' });
    },
    goToPackage() {
      uni.navigateTo({ url: '/pages/package/package' });
    }
  }
}
</script>

<style scoped>
.select-content {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 40rpx;
}
.header-bar {
  display: flex;
  align-items: center;
  padding: 40rpx 0 20rpx 0;
  position: relative;
}
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
}
.iconfont.icon-back::before {
  content: '\e65c';
  font-family: 'iconfont' !important;
  font-size: 40rpx;
  color: #1296db;
}
.header {
  flex: 1;
  font-size: 36rpx;
  font-weight: bold;
  color: #1296db;
  text-align: center;
}
.select-list {
  padding: 0 40rpx;
}
.select-item {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 40rpx;
  padding: 40rpx 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
  display: flex;
  align-items: center;
  transition: all 0.2s;
}
.select-item:active {
  opacity: 0.8;
}
.select-icon {
  width: 100rpx;
  height: 100rpx;
  margin-right: 30rpx;
}
.select-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}
.select-desc {
  font-size: 24rpx;
  color: #999;
}
</style> 