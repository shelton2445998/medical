<template>
  <view class="normal-appointment-content">
    <view class="header">普通项目预约</view>
    <view class="form-group">
      <picker :range="hospitalList" range-key="name" @change="onHospitalChange">
        <view class="form-item">
          <text class="label">选择医院：</text>
          <text class="value">{{ selectedHospital ? selectedHospital.name : '请选择' }}</text>
        </view>
      </picker>
      <picker mode="date" @change="onDateChange">
        <view class="form-item">
          <text class="label">预约日期：</text>
          <text class="value">{{ selectedDate || '请选择' }}</text>
        </view>
      </picker>
      <picker :range="projectList" range-key="name" @change="onProjectChange">
        <view class="form-item">
          <text class="label">体检项目：</text>
          <text class="value">{{ selectedProject ? selectedProject.name : '请选择' }}</text>
        </view>
      </picker>
      <view class="form-item">
        <text class="label">姓名：</text>
        <input class="input" v-model="name" placeholder="请输入姓名" />
      </view>
      <view class="form-item">
        <text class="label">手机号：</text>
        <input class="input" v-model="phone" placeholder="请输入手机号" type="number" maxlength="11" />
      </view>
    </view>
    <view class="btn-group">
      <button class="submit-btn" @click="submitAppointment">提交预约</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      hospitalList: [
        { id: 1, name: '沈阳市云医院-和平分院' },
        { id: 2, name: '沈阳市云医院-沈河分院' }
      ],
      projectList: [
        { id: 1, name: '血常规' },
        { id: 2, name: '尿常规' },
        { id: 3, name: '肝功能' },
        { id: 4, name: '肾功能' },
        { id: 5, name: '心电图' },
        { id: 6, name: '胸部X光' },
        { id: 7, name: 'B超' }
      ],
      selectedHospital: null,
      selectedDate: '',
      selectedProject: null,
      name: '',
      phone: ''
    }
  },
  methods: {
    onHospitalChange(e) {
      this.selectedHospital = this.hospitalList[e.detail.value];
    },
    onDateChange(e) {
      this.selectedDate = e.detail.value;
    },
    onProjectChange(e) {
      this.selectedProject = this.projectList[e.detail.value];
    },
    submitAppointment() {
      if (!this.selectedHospital || !this.selectedDate || !this.selectedProject || !this.name.trim() || !this.phone.trim()) {
        uni.showToast({ title: '请完善预约信息', icon: 'none' });
        return;
      }
      uni.showToast({ title: '预约成功！', icon: 'success' });
    }
  }
}
</script>

<style scoped>
.normal-appointment-content {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
  min-height: 100vh;
  padding-bottom: 40rpx;
}
.header {
  font-size: 36rpx;
  font-weight: bold;
  color: #ffffff;
  text-align: center;
  padding: 40rpx 0 30rpx 0;
  text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
}
.form-group {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16rpx;
  margin: 0 30rpx 30rpx 30rpx;
  padding: 24rpx 20rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
}
.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}
.label {
  font-size: 26rpx;
  color: #333;
  width: 160rpx;
}
.value {
  font-size: 26rpx;
  color: #0984e3;
}
.input {
  flex: 1;
  height: 60rpx;
  border-radius: 30rpx;
  background: #f5f5f5;
  border: none;
  font-size: 26rpx;
  padding: 0 20rpx;
}
.btn-group {
  margin: 40rpx 30rpx 0 30rpx;
}
.submit-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #74b9ff, #0984e3);
  color: #fff;
  font-size: 30rpx;
  border-radius: 45rpx;
  border: none;
  box-shadow: 0 4rpx 12rpx rgba(116, 185, 255, 0.3);
}
</style> 