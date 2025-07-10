<template>
  <view class="content">
    <view class="header">健康档案</view>
    <view class="desc">这里是健康档案功能页面，可展示个人健康档案、历史体检记录等。</view>
    <button class="back-btn" @click="goBack">返回</button>
  </view>
</template>
<script>
export default {
  methods: {
    goBack() {
      uni.navigateBack();
    }
  }
}
</script>
<style scoped>
.content { padding: 40rpx; }
.header { font-size: 36rpx; font-weight: bold; margin-bottom: 30rpx; }
.desc { font-size: 28rpx; color: #666; margin-bottom: 40rpx; }
.back-btn { background: #1296db; color: #fff; border-radius: 8rpx; padding: 20rpx 0; width: 100%; }
</style> 