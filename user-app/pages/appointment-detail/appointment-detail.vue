<template>
  <view class="content">
    <!-- 动态背景装饰 -->
    <view class="floating-shapes">
      <view class="shape shape-1"></view>
      <view class="shape shape-2"></view>
      <view class="shape shape-3"></view>
      <view class="shape shape-4"></view>
    </view>
    
    <view class="main-content">
      <!-- 页面头部 -->
      <view class="page-header">
        <view class="header-icon">📋</view>
        <view class="header-title">预约详情</view>
        <view class="header-desc">查看您的体检预约详细信息</view>
      </view>
      
      <!-- 加载状态 -->
      <view class="loading-container" v-if="loading">
        <view class="loading-card">
          <view class="loading-icon">⏳</view>
          <view class="loading-text">正在加载预约详情...</view>
        </view>
      </view>
      
      <!-- 错误状态 -->
      <view class="error-container" v-else-if="error">
        <view class="error-card">
          <view class="error-icon">❌</view>
          <view class="error-text">{{ error }}</view>
          <button class="retry-btn" @click="loadAppointmentDetail">
            <text class="btn-icon">🔄</text>
            <text>重新加载</text>
          </button>
        </view>
      </view>
      
      <!-- 预约详情内容 -->
      <view v-else-if="appointmentDetail" class="detail-content">
        <!-- 状态卡片 -->
        <view class="detail-card">
          <view class="card-header">
            <view class="card-icon">📊</view>
            <view class="card-title">预约状态</view>
          </view>
          <view class="info-section">
            <view class="info-row">
              <text class="info-label">订单号</text>
              <text class="info-value">{{ appointmentDetail.orderNumber }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">预约状态</text>
              <text class="info-value status" :class="'status-' + appointmentDetail.status">
                {{ getStatusName(appointmentDetail.status) }}
              </text>
            </view>
            <view class="info-row">
              <text class="info-label">创建时间</text>
              <text class="info-value">{{ formatDate(appointmentDetail.createTime) }}</text>
            </view>
          </view>
        </view>
        
        <!-- 患者信息卡片 -->
        <view class="detail-card">
          <view class="card-header">
            <view class="card-icon">👤</view>
            <view class="card-title">患者信息</view>
          </view>
          <view class="info-section">
            <view class="info-row">
              <text class="info-label">患者姓名</text>
              <text class="info-value">{{ appointmentDetail.patientName }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">患者年龄</text>
              <text class="info-value">{{ appointmentDetail.patientAge }}岁</text>
            </view>
            <view class="info-row">
              <text class="info-label">患者性别</text>
              <text class="info-value">{{ appointmentDetail.patientGender === 1 ? '男' : '女' }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">联系电话</text>
              <text class="info-value">{{ appointmentDetail.patientPhone }}</text>
            </view>
          </view>
        </view>
        
        <!-- 预约信息卡片 -->
        <view class="detail-card">
          <view class="card-header">
            <view class="card-icon">📅</view>
            <view class="card-title">预约信息</view>
          </view>
          <view class="info-section">
            <view class="info-row">
              <text class="info-label">预约日期</text>
              <text class="info-value">{{ formatDate(appointmentDetail.appointmentDate) }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">预约时间</text>
              <text class="info-value">{{ appointmentDetail.timeSlot }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">医院名称</text>
              <text class="info-value">{{ appointmentDetail.hospitalName }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">医生姓名</text>
              <text class="info-value">{{ appointmentDetail.doctorName || '待分配' }}</text>
            </view>
          </view>
        </view>
        
        <!-- 套餐信息卡片 -->
        <view class="detail-card" v-if="appointmentDetail.setmealName">
          <view class="card-header">
            <view class="card-icon">📋</view>
            <view class="card-title">套餐信息</view>
          </view>
          <view class="info-section">
            <view class="info-row">
              <text class="info-label">套餐名称</text>
              <text class="info-value">{{ appointmentDetail.setmealName }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">套餐价格</text>
              <text class="info-value price">¥{{ appointmentDetail.price }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">订单金额</text>
              <text class="info-value price">¥{{ appointmentDetail.amount }}</text>
            </view>
          </view>
        </view>
        
        <!-- 备注信息卡片 -->
        <view class="detail-card" v-if="appointmentDetail.remark">
          <view class="card-header">
            <view class="card-icon">📝</view>
            <view class="card-title">备注信息</view>
          </view>
          <view class="info-section">
            <view class="info-row">
              <text class="info-label">备注内容</text>
              <text class="info-value">{{ appointmentDetail.remark }}</text>
            </view>
          </view>
        </view>
        
        <!-- 操作按钮 -->
        <view class="action-buttons">
          <button 
            class="action-btn danger-btn" 
            v-if="appointmentDetail.status === 1"
            @click="cancelAppointment"
          >
            <text class="btn-icon">❌</text>
            <text>取消预约</text>
          </button>
          <button 
            class="action-btn primary-btn" 
            @click="goBack"
          >
            <text class="btn-icon">←</text>
            <text>返回列表</text>
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { get } from '@/utils/request.js';
import { appointmentApi } from '@/utils/api.js';

export default {
  data() {
    return {
      appointmentId: null,
      appointmentDetail: null,
      loading: true,
      error: null
    }
  },
  
  onLoad(options) {
    if (options.id) {
      this.appointmentId = options.id;
      this.loadAppointmentDetail();
    } else {
      this.error = '预约ID不存在';
      this.loading = false;
    }
  },
  
  methods: {
    // 加载预约详情
    async loadAppointmentDetail() {
      try {
        this.loading = true;
        this.error = null;
        
        console.log('开始获取预约详情，ID:', this.appointmentId);
        console.log('API地址:', appointmentApi.getAppointmentDetail(this.appointmentId));
        
        const result = await get(appointmentApi.getAppointmentDetail(this.appointmentId));
        
        console.log('预约详情API响应:', result);
        
        if (result && result.code === 200) {
          this.appointmentDetail = result.data;
          console.log('预约详情数据:', this.appointmentDetail);
        } else {
          this.error = result.msg || '获取预约详情失败';
          console.error('API返回错误:', result);
        }
      } catch (err) {
        console.error('获取预约详情失败：', err);
        this.error = '网络错误，请检查网络连接';
      } finally {
        this.loading = false;
      }
    },
    
    // 获取状态名称
    getStatusName(status) {
      const statusMap = {
        0: '已取消',
        1: '待支付',
        2: '已支付',
        3: '已完成'
      };
      return statusMap[status] || '未知状态';
    },
    
    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '未知';
      const date = new Date(dateStr);
      return date.toLocaleDateString('zh-CN');
    },
    
    // 取消预约
    async cancelAppointment() {
      try {
        uni.showModal({
          title: '确认取消',
          content: '确定要取消这个预约吗？',
          success: async (res) => {
            if (res.confirm) {
              uni.showLoading({ title: '正在取消...' });
              
              const result = await get(appointmentApi.cancelAppointment(this.appointmentId));
              
              uni.hideLoading();
              
              if (result && result.code === 200) {
                uni.showToast({
                  title: '取消成功',
                  icon: 'success'
                });
                
                // 重新加载详情
                this.loadAppointmentDetail();
              } else {
                uni.showToast({
                  title: result.msg || '取消失败',
                  icon: 'none'
                });
              }
            }
          }
        });
      } catch (err) {
        uni.hideLoading();
        console.error('取消预约失败：', err);
        uni.showToast({
          title: '网络错误',
          icon: 'none'
        });
      }
    },
    
    // 返回列表
    goBack() {
      uni.navigateBack();
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
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
  padding: 20rpx 40rpx 0 40rpx;
  position: relative;
  z-index: 1;
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

.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  
  .loading-card {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 24rpx;
    padding: 60rpx 40rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10rpx);
    text-align: center;
    animation: fadeInUp 0.8s ease-out;
    
    .loading-icon {
      font-size: 80rpx;
      margin-bottom: 30rpx;
      animation: spin 2s linear infinite;
    }
    
    .loading-text {
      font-size: 28rpx;
      color: #666666;
    }
  }
}

.error-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  
  .error-card {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 24rpx;
    padding: 60rpx 40rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10rpx);
    text-align: center;
    animation: fadeInUp 0.8s ease-out;
    
    .error-icon {
      font-size: 80rpx;
      margin-bottom: 30rpx;
      animation: pulse 2s infinite;
    }
    
    .error-text {
      font-size: 28rpx;
      color: #ff5a5f;
      margin-bottom: 40rpx;
      display: block;
    }
    
    .retry-btn {
      background: linear-gradient(135deg, #0984e3, #74b9ff);
      color: #ffffff;
      border: none;
      border-radius: 50rpx;
      padding: 20rpx 40rpx;
      font-size: 28rpx;
      font-weight: bold;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto;
      transition: all 0.3s ease;
      box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
      
      &:hover {
        transform: translateY(-4rpx);
        box-shadow: 0 12rpx 32rpx rgba(9, 132, 227, 0.4);
      }
      
      .btn-icon {
        font-size: 24rpx;
        margin-right: 10rpx;
      }
    }
  }
}

.detail-content {
  animation: fadeInUp 0.8s ease-out;
}

.detail-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
  transition: all 0.3s ease;
  animation: fadeInUp 0.8s ease-out;
  
  &:hover {
    transform: translateY(-4rpx);
    box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
  }
  
  .card-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    padding-bottom: 20rpx;
    border-bottom: 2rpx solid rgba(9, 132, 227, 0.1);
    
    .card-icon {
      font-size: 40rpx;
      margin-right: 20rpx;
      color: #0984e3;
      transition: all 0.3s ease;
    }
    
    .card-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333333;
      transition: all 0.3s ease;
    }
  }
  
  .info-section {
    .info-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 15rpx 0;
      border-bottom: 1rpx solid #f0f0f0;
      transition: all 0.3s ease;
      
      &:last-child {
        border-bottom: none;
      }
      
      &:hover {
        background: rgba(9, 132, 227, 0.05);
        border-radius: 8rpx;
        padding-left: 10rpx;
        padding-right: 10rpx;
      }
      
      .info-label {
        font-size: 28rpx;
        color: #666666;
        font-weight: 500;
      }
      
      .info-value {
        font-size: 28rpx;
        color: #333333;
        font-weight: bold;
        
        &.status {
          padding: 6rpx 16rpx;
          border-radius: 20rpx;
          font-size: 24rpx;
          font-weight: bold;
          
          &.status-1 {
            background: linear-gradient(135deg, #ffa726, #ff9800);
            color: #ffffff;
          }
          
          &.status-2 {
            background: linear-gradient(135deg, #66bb6a, #4caf50);
            color: #ffffff;
          }
          
          &.status-3 {
            background: linear-gradient(135deg, #42a5f5, #2196f3);
            color: #ffffff;
          }
        }
        
        &.price {
          color: #ff5a5f;
          font-weight: bold;
        }
      }
    }
  }
}

.action-buttons {
  display: flex;
  gap: 20rpx;
  margin-top: 40rpx;
  animation: fadeInUp 0.8s ease-out 0.2s both;
  
  .action-btn {
    flex: 1;
    height: 88rpx;
    border-radius: 44rpx;
    font-size: 28rpx;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    border: none;
    
    .btn-icon {
      font-size: 24rpx;
      margin-right: 10rpx;
    }
    
    &.primary-btn {
      background: linear-gradient(135deg, #0984e3, #74b9ff);
      color: #ffffff;
      box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
      
      &:hover {
        transform: translateY(-2rpx);
        box-shadow: 0 12rpx 32rpx rgba(9, 132, 227, 0.4);
      }
    }
    
    &.secondary-btn {
      background: rgba(255, 255, 255, 0.9);
      color: #0984e3;
      border: 2rpx solid #0984e3;
      
      &:hover {
        background: rgba(9, 132, 227, 0.1);
        transform: translateY(-2rpx);
        box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.2);
      }
    }
    
    &.danger-btn {
      background: linear-gradient(135deg, #ff7675, #fd79a8);
      color: #ffffff;
      box-shadow: 0 8rpx 24rpx rgba(255, 118, 117, 0.3);
      
      &:hover {
        transform: translateY(-2rpx);
        box-shadow: 0 12rpx 32rpx rgba(255, 118, 117, 0.4);
      }
    }
  }
}

// 动画定义
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

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30rpx);
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

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style> 