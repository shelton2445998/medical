<template>
  <view class="appointment-flow-content">
    <view class="step-indicator">
      <view v-for="(label, idx) in stepLabels" :key="idx" :class="['step', {active: step === idx+1}]">
        <text>{{ label }}</text>
        <view v-if="idx < stepLabels.length" class="step-arrow">→</view>
      </view>
    </view>

    <!-- Step 1: 选择医院 -->
    <view v-if="step === 1" class="step-panel">
      <view class="step-title">请选择医院</view>
      <picker :range="hospitalList" range-key="name" @change="onHospitalChange">
        <view class="picker-box">
          <text>{{ selectedHospital ? selectedHospital.name : '点击选择医院' }}</text>
        </view>
      </picker>
      <button class="next-btn" @click="nextStep" :disabled="!selectedHospital">下一步</button>
    </view>

    <!-- Step 2: 选择套餐 -->
    <view v-else-if="step === 2" class="step-panel">
      <view class="step-title">请选择体检套餐</view>
      <picker :range="packageList" range-key="name" @change="onPackageChange">
        <view class="picker-box">
          <text>{{ selectedPackage ? selectedPackage.name : '点击选择套餐' }}</text>
        </view>
      </picker>
      <button class="next-btn" @click="nextStep" :disabled="!selectedPackage">下一步</button>
    </view>

    <!-- Step 3: 填写信息 -->
    <view v-else-if="step === 3" class="step-panel">
      <view class="step-title">请填写预约信息</view>
      <view class="form-item">
        <text class="label">姓名：</text>
        <input class="input" v-model="name" placeholder="请输入姓名" />
      </view>
      <view class="form-item">
        <text class="label">性别：</text>
        <picker :range="['男','女']" @change="onGenderChange">
          <view class="picker-box">
            <text>{{ gender || '请选择性别' }}</text>
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">手机号：</text>
        <input class="input" v-model="phone" placeholder="请输入手机号" type="number" maxlength="11" />
      </view>
      <view class="form-item">
        <text class="label">身份证号：</text>
        <input class="input" v-model="idCard" placeholder="请输入身份证号" maxlength="18" />
      </view>
      <view class="form-item">
        <text class="label">预约日期：</text>
        <picker mode="date" @change="onDateChange">
          <view class="picker-box">
            <text>{{ selectedDate || '点击选择日期' }}</text>
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">备注：</text>
        <input class="input" v-model="remark" placeholder="可填写特殊需求或备注" />
      </view>
      <button class="next-btn" @click="nextStep" :disabled="!name || !phone || !selectedDate || !gender || !idCard">下一步</button>
    </view>

    <!-- Step 4: 支付预约 -->
    <view v-else-if="step === 4" class="step-panel">
      <view class="step-title">请确认预约信息并支付</view>
      <view class="confirm-info">
        <view>医院：{{ selectedHospital.name }}</view>
        <view>套餐：{{ selectedPackage.name }}</view>
        <view>姓名：{{ name }}</view>
        <view>性别：{{ gender }}</view>
        <view>手机号：{{ phone }}</view>
        <view>身份证号：{{ idCard }}</view>
        <view>日期：{{ selectedDate }}</view>
        <view>备注：{{ remark }}</view>
        <view>套餐价格：¥{{ selectedPackage.price }}</view>
        <view>优惠：¥{{ discount }}</view>
        <view class="total-price">应付总额：<text class="price">¥{{ totalPrice }}</text></view>
      </view>
      <view class="pay-method-box">
        <view class="pay-method-title">请选择支付方式：</view>
        <radio-group @change="onPayMethodChange">
          <view class="pay-method-list">
            <label v-for="(item, idx) in payMethods" :key="idx" class="pay-method-item">
              <radio :value="item" :checked="payMethod === item" />
              <text>{{ item }}</text>
            </label>
          </view>
        </radio-group>
      </view>
      <view class="pay-btn-box">
        <button class="pay-btn" @click="showPayModal" :disabled="!payMethod">支付预约</button>
      </view>
      <!-- 支付二维码弹窗 -->
      <view v-if="showPay" class="pay-modal-mask" @click.self="closePayModal">
        <view class="pay-modal">
          <view class="pay-modal-title">{{ payMethod }}扫码支付</view>
          <image class="qrcode" src="/static/images/qrcode-demo.png" mode="aspectFit"></image>
          <button class="pay-confirm-btn" @click="pay">已支付</button>
          <button class="pay-cancel-btn" @click="closePayModal">取消</button>
        </view>
      </view>
    </view>

    <!-- 支付成功 -->
    <view v-else-if="step === 5" class="step-panel">
      <view class="success-icon">✔</view>
      <view class="success-text">预约支付成功！</view>
      <view class="order-info">
        <view>预约单号：{{ orderNo }}</view>
        <image class="qrcode" src="/static/images/qrcode-demo.png" mode="aspectFit"></image>
        <view class="success-tips">请凭预约单号或二维码到医院前台报到</view>
        <view class="success-tips">如需改期/取消，请在预约记录中操作</view>
      </view>
      <button class="back-btn" @click="goHome">返回首页</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      step: 1,
      stepLabels: ['选择医院', '选择套餐', '填写信息', '支付预约'],
      hospitalList: [
        { id: 1, name: '沈阳市云医院-和平分院' },
        { id: 2, name: '沈阳市云医院-沈河分院' }
      ],
      packageList: [
        { id: 1, name: '标准体检套餐', price: 299 },
        { id: 2, name: '高级体检套餐', price: 699 },
        { id: 3, name: '女性专项体检套餐', price: 399 },
        { id: 4, name: '儿童体检套餐', price: 199 }
      ],
      selectedHospital: null,
      selectedPackage: null,
      name: '',
      gender: '',
      phone: '',
      idCard: '',
      selectedDate: '',
      remark: '',
      payMethods: ['微信支付', '支付宝', '医保支付'],
      payMethod: '',
      showPay: false,
      orderNo: '',
      discount: 0
    }
  },
  computed: {
    totalPrice() {
      // 可根据套餐和优惠计算
      return (this.selectedPackage ? (this.selectedPackage.price - this.discount) : 0).toFixed(2);
    }
  },
  onLoad() {
    // 检查是否有已选择的医院和套餐
    const selectedHospital = uni.getStorageSync('selectedHospital');
    const selectedPackage = uni.getStorageSync('selectedPackage');
    
    if (selectedHospital && selectedPackage) {
      // 如果已有选择的医院和套餐，直接跳到填写信息步骤
      this.selectedHospital = JSON.parse(selectedHospital);
      this.selectedPackage = JSON.parse(selectedPackage);
      this.step = 3; // 直接跳到填写信息步骤
    } else if (selectedPackage) {
      // 如果只有套餐信息（从套餐详情页进入），设置套餐并跳到填写信息步骤
      this.selectedPackage = JSON.parse(selectedPackage);
      // 从套餐信息中获取医院信息
      if (this.selectedPackage.hospitalId) {
        this.selectedHospital = {
          id: this.selectedPackage.hospitalId,
          name: this.selectedPackage.hospitalName,
          address: this.selectedPackage.hospitalAddress
        };
      }
      this.step = 3; // 直接跳到填写信息步骤
    }
  },
  methods: {
    onHospitalChange(e) {
      this.selectedHospital = this.hospitalList[e.detail.value];
    },
    onPackageChange(e) {
      this.selectedPackage = this.packageList[e.detail.value];
    },
    onDateChange(e) {
      this.selectedDate = e.detail.value;
    },
    onGenderChange(e) {
      this.gender = ['男', '女'][e.detail.value];
    },
    onPayMethodChange(e) {
      this.payMethod = e.detail.value;
    },
    nextStep() {
      if (this.step < 4) {
        this.step++;
      }
    },
    showPayModal() {
      this.showPay = true;
    },
    closePayModal() {
      this.showPay = false;
    },
    pay() {
      // 模拟支付
      this.showPay = false;
      uni.showLoading({ title: '支付中...' });
      setTimeout(() => {
        uni.hideLoading();
        this.orderNo = 'YY' + Date.now();
        this.step = 5;
        // 清除存储的医院和套餐信息
        uni.removeStorageSync('selectedHospital');
        uni.removeStorageSync('selectedPackage');
        // 支付成功后2秒自动返回首页
        setTimeout(() => {
          uni.reLaunch({ url: '/pages/index/index' });
        }, 2000);
      }, 1500);
    },
    goHome() {
      // 清除存储的医院和套餐信息
      uni.removeStorageSync('selectedHospital');
      uni.removeStorageSync('selectedPackage');
      uni.reLaunch({ url: '/pages/index/index' });
    }
  }
}
</script>

<style scoped>
.appointment-flow-content {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 40rpx;
}
.step-indicator {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 40rpx 0 30rpx 0;
}
.step {
  font-size: 28rpx;
  color: #999;
  font-weight: bold;
  margin: 0 10rpx;
}
.step.active {
  color: #1296db;
}
.step-arrow {
  margin: 0 10rpx;
  color: #ccc;
}
.step-panel {
  background: #fff;
  border-radius: 16rpx;
  margin: 0 30rpx 30rpx 30rpx;
  padding: 40rpx 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}
.step-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1296db;
  margin-bottom: 30rpx;
}
.picker-box {
  background: #f5f5f5;
  border-radius: 10rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 30rpx;
}
.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}
.label {
  width: 120rpx;
  font-size: 26rpx;
  color: #333;
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
.next-btn, .pay-btn, .back-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: #1296db;
  color: #fff;
  font-size: 30rpx;
  border-radius: 45rpx;
  border: none;
  margin-top: 20rpx;
}
.pay-btn {
  background: #ff5a5f;
}
.success-icon {
  font-size: 80rpx;
  color: #52c41a;
  text-align: center;
  margin-bottom: 30rpx;
}
.success-text {
  font-size: 32rpx;
  color: #1296db;
  text-align: center;
  margin-bottom: 30rpx;
}
.confirm-info {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 30rpx;
  line-height: 2;
}
.pay-btn-box {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10rpx;
}
.pay-btn {
  background: #ff5a5f;
  width: 80%;
  margin: 0 auto;
}
.pay-method-box {
  margin-top: 30rpx;
  padding: 20rpx;
  background: #f5f5f5;
  border-radius: 10rpx;
}
.pay-method-title {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 20rpx;
}
.pay-method-list {
  display: flex;
  flex-direction: column;
}
.pay-method-item {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}
.pay-method-item text {
  margin-left: 20rpx;
  font-size: 28rpx;
  color: #333;
}
.order-info {
  margin-top: 30rpx;
  padding: 20rpx;
  background: #f5f5f5;
  border-radius: 10rpx;
  text-align: center;
}
.qrcode {
  width: 200rpx;
  height: 200rpx;
  margin: 20rpx auto;
}
.success-tips {
  font-size: 24rpx;
  color: #666;
  margin-top: 10rpx;
}
.total-price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff5a5f;
  margin-top: 20rpx;
  text-align: right;
}
.price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff5a5f;
}
.pay-modal-mask {
  position: fixed;
  left: 0; right: 0; top: 0; bottom: 0;
  background: rgba(0,0,0,0.4);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
}
.pay-modal {
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx 30rpx;
  width: 500rpx;
  box-shadow: 0 2rpx 20rpx rgba(0,0,0,0.15);
  display: flex;
  flex-direction: column;
  align-items: center;
}
.pay-modal-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}
.pay-confirm-btn {
  background: #1296db;
  color: #fff;
  border-radius: 8rpx;
  margin-top: 30rpx;
  width: 100%;
}
.pay-cancel-btn {
  background: #eee;
  color: #333;
  border-radius: 8rpx;
  margin-top: 20rpx;
  width: 100%;
}
</style> 