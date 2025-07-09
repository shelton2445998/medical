<template>
  <view class="chat-content">
    <view class="chat-header">
      <image class="doctor-avatar" :src="doctor.avatar" mode="aspectFill" />
      <view class="doctor-info">
        <view class="doctor-name">{{ doctor.name }}</view>
        <view class="doctor-dept">{{ doctor.department }}</view>
      </view>
    </view>
    <view class="chat-body">
      <view v-for="(msg, idx) in messages" :key="idx" :class="['chat-msg', msg.from === 'me' ? 'from-me' : 'from-doctor']">
        <image v-if="msg.from === 'doctor'" class="msg-avatar" :src="doctor.avatar" mode="aspectFill" />
        <view class="msg-bubble">{{ msg.text }}</view>
        <image v-if="msg.from === 'me'" class="msg-avatar" src="/static/images/user.png" mode="aspectFill" />
      </view>
    </view>
    <view class="chat-footer">
      <input class="chat-input" v-model="inputMsg" placeholder="请输入咨询内容..." />
      <button class="send-btn" @click="sendMsg">发送</button>
    </view>
  </view>
</template>

<script>
const DOCTORS = [
  { id: 1, name: '王医生', department: '心内科', avatar: '/static/images/avatar1.jpg' },
  { id: 2, name: '李医生', department: '肿瘤科', avatar: '/static/images/avatar2.jpg' },
  { id: 3, name: '赵医生', department: '内分泌科', avatar: '/static/images/avatar1.jpg' },
  { id: 4, name: '钱医生', department: '老年医学科', avatar: '/static/images/avatar2.jpg' }
];
export default {
  data() {
    return {
      doctor: {},
      messages: [
        { from: 'doctor', text: '您好，我是您的主治医生，有什么可以帮您？' }
      ],
      inputMsg: ''
    }
  },
  onLoad(options) {
    const id = options && options.expertId ? Number(options.expertId) : 1;
    this.doctor = DOCTORS.find(d => d.id === id) || DOCTORS[0];
  },
  methods: {
    sendMsg() {
      if (!this.inputMsg.trim()) return;
      this.messages.push({ from: 'me', text: this.inputMsg });
      // 演示自动回复
      setTimeout(() => {
        this.messages.push({ from: 'doctor', text: '收到您的消息，请稍等...' });
      }, 800);
      this.inputMsg = '';
      this.$nextTick(() => {
        const query = uni.createSelectorQuery().in(this);
        query.select('.chat-body').boundingClientRect();
        query.exec(res => {
          uni.pageScrollTo({ scrollTop: 99999, duration: 300 });
        });
      });
    }
  }
}
</script>

<style scoped>
.chat-content {
  background: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.chat-header {
  display: flex;
  align-items: center;
  background: #1296db;
  padding: 30rpx 20rpx;
}
.doctor-avatar {
  width: 90rpx;
  height: 90rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  border: 2rpx solid #fff;
}
.doctor-info {
  color: #fff;
}
.doctor-name {
  font-size: 32rpx;
  font-weight: bold;
}
.doctor-dept {
  font-size: 24rpx;
  margin-top: 6rpx;
}
.chat-body {
  flex: 1;
  padding: 30rpx 20rpx 30rpx 20rpx;
  overflow-y: auto;
  background: #f5f5f5;
}
.chat-msg {
  display: flex;
  align-items: flex-end;
  margin-bottom: 24rpx;
}
.from-doctor {
  flex-direction: row;
}
.from-me {
  flex-direction: row-reverse;
}
.msg-avatar {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  margin: 0 12rpx;
}
.msg-bubble {
  max-width: 60vw;
  padding: 18rpx 26rpx;
  border-radius: 24rpx;
  font-size: 28rpx;
  background: #fff;
  color: #333;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04);
  word-break: break-all;
}
.from-me .msg-bubble {
  background: #1296db;
  color: #fff;
}
.chat-footer {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #fff;
  border-top: 1rpx solid #eee;
}
.chat-input {
  flex: 1;
  height: 70rpx;
  border-radius: 35rpx;
  background: #f5f5f5;
  border: none;
  font-size: 28rpx;
  padding: 0 24rpx;
  margin-right: 20rpx;
}
.send-btn {
  background: #1296db;
  color: #fff;
  border-radius: 35rpx;
  font-size: 28rpx;
  padding: 0 36rpx;
  height: 70rpx;
  line-height: 70rpx;
  border: none;
}
</style> 