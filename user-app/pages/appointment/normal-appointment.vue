<template>
  <view class="appointment-content">
    <!-- 动态背景装饰 -->
    <view class="floating-shapes">
      <view class="shape shape-1"></view>
      <view class="shape shape-2"></view>
      <view class="shape shape-3"></view>
      <view class="shape shape-4"></view>
    </view>
    
    <view class="main-content">
      <!-- 页面标题 -->
      <view class="page-header">
        <view class="header-icon">🏥</view>
        <view class="header-title">普通项目预约</view>
        <view class="header-desc">请填写您的预约信息</view>
      </view>
      
      <!-- 预约表单 -->
      <view class="form-section">
        <view class="section-title">
          <text class="title-icon">📋</text>
          <text>预约信息</text>
        </view>
        
        <!-- 医院选择 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">🏥</text>
            <text>选择医院</text>
          </view>
          <picker :range="hospitalList" range-key="name" @change="onHospitalChange">
            <view class="form-picker">
              <text class="picker-text" :class="{ 'placeholder': !selectedHospital }">
                {{ selectedHospital ? selectedHospital.name : '请选择医院' }}
              </text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <!-- 预约日期 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">📅</text>
            <text>预约日期</text>
          </view>
          <picker mode="date" @change="onDateChange">
            <view class="form-picker">
              <text class="picker-text" :class="{ 'placeholder': !selectedDate }">
                {{ selectedDate || '请选择日期' }}
              </text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <!-- 体检项目 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">🔬</text>
            <text>体检项目</text>
          </view>
          <picker :range="projectList" range-key="name" @change="onProjectChange">
            <view class="form-picker">
              <text class="picker-text" :class="{ 'placeholder': !selectedProject }">
                {{ selectedProject ? selectedProject.name : '请选择项目' }}
              </text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <!-- 姓名 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">👤</text>
            <text>姓名</text>
          </view>
          <input 
            class="form-input" 
            v-model="name" 
            placeholder="请输入您的姓名"
            placeholder-class="input-placeholder"
          />
        </view>
        
        <!-- 手机号 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">📱</text>
            <text>手机号</text>
          </view>
          <input 
            class="form-input" 
            v-model="phone" 
            placeholder="请输入您的手机号"
            placeholder-class="input-placeholder"
            type="number" 
            maxlength="11" 
          />
        </view>
        
        <!-- 备注信息 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">📝</text>
            <text>备注信息</text>
          </view>
          <textarea 
            class="form-textarea" 
            v-model="remark" 
            placeholder="请输入备注信息（可选）"
            placeholder-class="textarea-placeholder"
            maxlength="200"
          />
          <view class="textarea-counter">{{ remark.length }}/200</view>
        </view>
      </view>
      
      <!-- 提交按钮 -->
      <view class="submit-section">
        <button 
          class="submit-btn" 
          @click="submitAppointment"
          :disabled="!isFormValid"
        >
          <text class="btn-icon">✅</text>
          <text>提交预约</text>
        </button>
      </view>
      
      <!-- 温馨提示 -->
      <view class="tips-section">
        <view class="tips-title">温馨提示</view>
        <view class="tips-list">
          <view class="tip-item">
            <text class="tip-icon">💡</text>
            <text class="tip-text">请提前15分钟到达医院</text>
          </view>
          <view class="tip-item">
            <text class="tip-icon">💡</text>
            <text class="tip-text">请携带有效身份证件</text>
          </view>
          <view class="tip-item">
            <text class="tip-icon">💡</text>
            <text class="tip-text">空腹项目请提前8小时禁食</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 加载遮罩 -->
    <view class="loading-overlay" v-if="isLoading">
      <view class="loading-content">
        <view class="loading-spinner"></view>
        <view class="loading-text">正在提交预约...</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      hospitalList: [
        { id: 1, name: '沈阳市云医院-和平分院' },
        { id: 2, name: '沈阳市云医院-沈河分院' },
        { id: 3, name: '沈阳市云医院-铁西分院' },
        { id: 4, name: '沈阳市云医院-皇姑分院' }
      ],
      projectList: [
        { id: 1, name: '血常规检查' },
        { id: 2, name: '尿常规检查' },
        { id: 3, name: '肝功能检查' },
        { id: 4, name: '肾功能检查' },
        { id: 5, name: '心电图检查' },
        { id: 6, name: '胸部X光检查' },
        { id: 7, name: 'B超检查' },
        { id: 8, name: '血压测量' },
        { id: 9, name: '血糖检查' },
        { id: 10, name: '血脂检查' }
      ],
      selectedHospital: null,
      selectedDate: '',
      selectedProject: null,
      name: '',
      phone: '',
      remark: '',
      isLoading: false
    }
  },
  computed: {
    isFormValid() {
      return this.selectedHospital && 
             this.selectedDate && 
             this.selectedProject && 
             this.name.trim() && 
             this.phone.trim() &&
             this.phone.length === 11;
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
    async submitAppointment() {
      if (!this.isFormValid) {
        uni.showToast({ 
          title: '请完善预约信息', 
          icon: 'none',
          duration: 2000
        });
        return;
      }
      
      // 验证手机号格式
      const phoneRegex = /^1[3-9]\d{9}$/;
      if (!phoneRegex.test(this.phone)) {
        uni.showToast({ 
          title: '请输入正确的手机号', 
          icon: 'none',
          duration: 2000
        });
        return;
      }
      
      this.isLoading = true;
      
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 2000));
        
        uni.showToast({ 
          title: '预约成功！', 
          icon: 'success',
          duration: 2000
        });
        
        // 跳转到预约成功页面
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/appointment/appointment-flow'
          });
        }, 2000);
        
      } catch (error) {
        uni.showToast({ 
          title: '预约失败，请重试', 
          icon: 'none',
          duration: 2000
        });
      } finally {
        this.isLoading = false;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.appointment-content {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
  min-height: 100vh;
  padding-top: 0;
  padding-bottom: 40rpx;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
    animation: flow 20s linear infinite;
    pointer-events: none;
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.05) 50%, transparent 70%);
    animation: shimmer 8s ease-in-out infinite;
    pointer-events: none;
  }
}

.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;

  .shape {
    position: absolute;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    filter: blur(50px);
    animation: float 15s infinite ease-in-out;
    transition: all 0.3s ease;
    
    &::before {
      content: '';
      position: absolute;
      top: -10%;
      left: -10%;
      width: 120%;
      height: 120%;
      background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, transparent 70%);
      border-radius: 50%;
      animation: pulse 3s ease-in-out infinite;
    }

    &.shape-1 {
      width: 100px;
      height: 100px;
      top: 10%;
      left: 10%;
      animation-delay: -2s;
    }
    &.shape-2 {
      width: 150px;
      height: 150px;
      top: 70%;
      left: 30%;
      animation-delay: -5s;
    }
    &.shape-3 {
      width: 120px;
      height: 120px;
      top: 20%;
      right: 20%;
      animation-delay: -8s;
    }
    &.shape-4 {
      width: 180px;
      height: 180px;
      bottom: 10%;
      right: 50%;
      animation-delay: -10s;
    }
  }
}

.main-content {
  padding: 40rpx 40rpx 0 40rpx;
}

.page-header {
  text-align: center;
  margin-bottom: 40rpx;
  animation: fadeInDown 0.8s ease-out;
  
  .header-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
    animation: bounce 2s infinite;
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.2);
    }
  }
  
  .header-title {
    font-size: 44rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 16rpx;
    text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;
  }
  
  .header-desc {
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
    transition: all 0.3s ease;
  }
}

.form-section {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
  animation: slideInUp 0.8s ease-out;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
    animation: shimmer 3s ease-in-out infinite;
  }
  
  &:hover {
    transform: translateY(-2rpx);
    box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
    background: rgba(255, 255, 255, 0.98);
  }
  
  /* 流动治愈感背景 */
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(116, 185, 255, 0.1) 0%, transparent 70%);
    animation: flow 15s linear infinite;
    pointer-events: none;
  }
  
  .section-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 30rpx;
    display: flex;
    align-items: center;
    transition: all 0.3s ease;
    
    .title-icon {
      font-size: 40rpx;
      margin-right: 15rpx;
      color: #74b9ff;
      transition: all 0.3s ease;
      animation: pulse 2s infinite;
    }
    
    &:hover {
      color: #0984e3;
      
      .title-icon {
        transform: scale(1.1);
      }
    }
  }
  
  .form-item {
    margin-bottom: 30rpx;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateX(5rpx);
    }
    
    .form-label {
      font-size: 28rpx;
      color: #333333;
      margin-bottom: 15rpx;
      display: flex;
      align-items: center;
      transition: all 0.3s ease;
      
      .label-icon {
        font-size: 32rpx;
        margin-right: 10rpx;
        transition: all 0.3s ease;
      }
      
      &:hover {
        color: #0984e3;
        
        .label-icon {
          transform: scale(1.1);
        }
      }
    }
    
    .form-input {
      width: 100%;
      height: 80rpx;
      border: 2rpx solid #e0e0e0;
      border-radius: 12rpx;
      padding: 0 20rpx;
      font-size: 28rpx;
      color: #333333;
      background: #ffffff;
      transition: all 0.3s ease;
      
      &:focus {
        border-color: #74b9ff;
        box-shadow: 0 0 0 4rpx rgba(116, 185, 255, 0.1);
        transform: translateY(-2rpx);
      }
      
      &:hover {
        border-color: #74b9ff;
        transform: translateY(-1rpx);
      }
    }
    
    .form-picker {
      width: 100%;
      height: 80rpx;
      border: 2rpx solid #e0e0e0;
      border-radius: 12rpx;
      padding: 0 20rpx;
      font-size: 28rpx;
      color: #333333;
      background: #ffffff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      transition: all 0.3s ease;
      
      &:hover {
        border-color: #74b9ff;
        transform: translateY(-1rpx);
      }
      
      .picker-text {
        color: #333333;
        transition: all 0.3s ease;
        
        &.placeholder {
          color: #999999;
        }
      }
      
      .picker-arrow {
        font-size: 24rpx;
        color: #999999;
        transition: all 0.3s ease;
      }
    }
    
    .form-textarea {
      width: 100%;
      min-height: 120rpx;
      border: 2rpx solid #e0e0e0;
      border-radius: 12rpx;
      padding: 20rpx;
      font-size: 28rpx;
      color: #333333;
      background: #ffffff;
      transition: all 0.3s ease;
      
      &:focus {
        border-color: #74b9ff;
        box-shadow: 0 0 0 4rpx rgba(116, 185, 255, 0.1);
        transform: translateY(-2rpx);
      }
      
      &:hover {
        border-color: #74b9ff;
        transform: translateY(-1rpx);
      }
    }
    
    .textarea-counter {
      text-align: right;
      font-size: 24rpx;
      color: #999999;
      margin-top: 10rpx;
      transition: all 0.3s ease;
    }
  }
}

.submit-section {
  padding: 40rpx;
  animation: slideInUp 0.8s ease-out 0.2s both;
  opacity: 0;
  transform: translateY(30rpx);
  
  .submit-btn {
    width: 100%;
    height: 100rpx;
    background: linear-gradient(135deg, #74b9ff, #0984e3);
    color: #ffffff;
    border: none;
    border-radius: 50rpx;
    font-size: 32rpx;
    font-weight: bold;
    box-shadow: 0 8rpx 24rpx rgba(116, 185, 255, 0.3);
    transition: all 0.3s ease;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .btn-icon {
      font-size: 36rpx;
      margin-right: 10rpx;
      transition: all 0.3s ease;
    }
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
      transition: left 0.6s ease;
    }
    
    &:hover {
      transform: translateY(-6rpx) scale(1.02);
      box-shadow: 0 12rpx 32rpx rgba(116, 185, 255, 0.5);
      background: linear-gradient(135deg, #0984e3, #74b9ff);
      
      &::before {
        left: 100%;
      }
      
      .btn-icon {
        transform: scale(1.1);
      }
    }
    
    &:active {
      transform: translateY(-2rpx) scale(0.98);
    }
    
    &:disabled {
      background: #cccccc;
      box-shadow: none;
      cursor: not-allowed;
      
      &:hover {
        transform: none;
        box-shadow: none;
      }
    }
  }
}

.tips-section {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20rpx;
  padding: 30rpx;
  backdrop-filter: blur(10rpx);
  animation: slideInUp 0.8s ease-out 0.4s both;
  opacity: 0;
  transform: translateY(30rpx);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    animation: shimmer 4s ease-in-out infinite;
  }
  
  &:hover {
    background: rgba(255, 255, 255, 0.15);
    transform: translateY(-2rpx);
  }
  
  .tips-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 20rpx;
    text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;
  }
  
  .tips-list {
    .tip-item {
      display: flex;
      align-items: center;
      margin-bottom: 15rpx;
      transition: all 0.3s ease;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &:hover {
        transform: translateX(5rpx);
      }
      
      .tip-icon {
        font-size: 24rpx;
        margin-right: 10rpx;
        animation: pulse 2s infinite;
        transition: all 0.3s ease;
      }
      
      .tip-text {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.9);
        transition: all 0.3s ease;
      }
    }
  }
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  backdrop-filter: blur(5rpx);
  animation: fadeIn 0.3s ease-out;
  
  .loading-content {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 20rpx;
    padding: 40rpx;
    text-align: center;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.2);
    animation: slideInUp 0.3s ease-out;
    
    .loading-spinner {
      width: 60rpx;
      height: 60rpx;
      border: 4rpx solid rgba(116, 185, 255, 0.3);
      border-top: 4rpx solid #74b9ff;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin: 0 auto 20rpx;
    }
    
    .loading-text {
      font-size: 28rpx;
      color: #333333;
    }
  }
}

/* 占位符样式 */
.input-placeholder {
  color: #999999;
}

.textarea-placeholder {
  color: #999999;
}

/* Animation keyframes */
@keyframes float {
  0% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
  25% {
    transform: translateY(-20px) translateX(20px) scale(1.1);
    opacity: 0.9;
  }
  50% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
  75% {
    transform: translateY(20px) translateX(-20px) scale(1.1);
    opacity: 0.9;
  }
  100% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
}

@keyframes flow {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes shimmer {
  0%, 100% {
    opacity: 0.3;
    transform: translateX(-100%);
  }
  50% {
    opacity: 0.6;
    transform: translateX(100%);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(50rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10rpx);
  }
  60% {
    transform: translateY(-5rpx);
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style> 