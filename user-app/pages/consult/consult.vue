<template>
  <view class="consult-content">
    <view class="consult-header">选择专家进行咨询</view>
    <view class="expert-list">
      <view class="expert-item" v-for="expert in experts" :key="expert.id" @click="consultExpert(expert)">
        <image class="expert-avatar" :src="expert.avatar" mode="aspectFill" />
        <view class="expert-info">
          <view class="expert-name">{{ expert.name }}</view>
          <view class="expert-title">{{ expert.title }}</view>
          <view class="expert-dept">{{ expert.department }}</view>
        </view>
        <view class="consult-btn">咨询</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      experts: [
        {
          id: 1,
          name: '王医生',
          title: '主任医师',
          department: '心内科',
          avatar: '/static/images/avatar1.jpg'
        },
        {
          id: 2,
          name: '李医生',
          title: '副主任医师',
          department: '肿瘤科',
          avatar: '/static/images/avatar2.jpg'
        },
        {
          id: 3,
          name: '赵医生',
          title: '主治医师',
          department: '内分泌科',
          avatar: '/static/images/avatar3.jpg'
        },
        {
          id: 4,
          name: '钱医生',
          title: '副主任医师',
          department: '老年医学科',
          avatar: '/static/images/avatar4.jpg'
        }
      ]
    }
  },
  methods: {
    consultExpert(expert) {
      uni.showToast({
        title: `已进入与${expert.name}的咨询（演示）`,
        icon: 'success',
        duration: 1000
      });
      setTimeout(() => {
        uni.navigateTo({ url: `/pages/chat/chat?expertId=${expert.id}` });
      }, 1000);
    }
  }
}
</script>

<style scoped>
.consult-content {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 40rpx;
}
.consult-header {
  font-size: 36rpx;
  font-weight: bold;
  color: #1296db;
  text-align: center;
  padding: 40rpx 0 20rpx 0;
}
.expert-list {
  padding: 0 30rpx;
}
.expert-item {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 30rpx;
  padding: 24rpx 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
  transition: all 0.2s;
}
.expert-item:active {
  opacity: 0.8;
}
.expert-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  margin-right: 24rpx;
}
.expert-info {
  flex: 1;
}
.expert-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}
.expert-title {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 4rpx;
}
.expert-dept {
  font-size: 22rpx;
  color: #999;
}
.consult-btn {
  color: #fff;
  background: #1296db;
  border-radius: 30rpx;
  padding: 12rpx 32rpx;
  font-size: 26rpx;
  margin-left: 20rpx;
}
</style>