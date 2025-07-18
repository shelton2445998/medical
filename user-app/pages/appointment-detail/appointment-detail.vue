<!--
@name: 预约详情页面
@description: 显示体检预约的详细信息，包括预约状态、患者信息、预约时间等
@author: 医疗系统开发团队
@created: 2024年医疗系统开发
@features:
- 显示预约状态（待支付、已支付、已完成、已取消）
- 显示患者基本信息（姓名、年龄、性别、联系方式）
- 显示预约详细信息（预约日期、时间、科室等）
- 支持取消预约功能
- 支持跳转到支付页面
- 动态背景效果和加载状态显示
@页面路径: /pages/appointment-detail/appointment-detail
@参数说明:
- id: 预约ID，用于获取预约详情
-->

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
      <!-- 
        页面头部
        显示页面标题和描述信息
      -->
      <view class="page-header">
        <view class="header-icon">📋</view>
        <view class="header-title">预约详情</view>
        <view class="header-desc">查看您的体检预约详细信息</view>
      </view>
      
      <!-- 
        加载状态
        当数据加载中时显示加载提示
      -->
      <view class="loading-container" v-if="loading">
        <view class="loading-card">
          <view class="loading-icon">⏳</view>
          <view class="loading-text">正在加载预约详情...</view>
        </view>
      </view>
      
      <!-- 
        错误状态
        当数据加载失败时显示错误信息和重试按钮
      -->
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
      
      <!-- 
        预约详情内容
        显示预约的详细信息，包括状态、患者信息、预约信息等
      -->
      <view v-else-if="appointmentDetail" class="detail-content">
        <!-- 
          状态卡片
          显示预约的基本状态信息
        -->
        <view class="detail-card">
          <view class="card-header">
            <view class="card-icon">📊</view>
            <view class="card-title">预约状态</view>
          </view>
          <view class="info-section">
            <view class="info-row">
              <text class="info-label">订单号</text>
              <text class="info-value">{{ appointmentDetail.id }}</text>
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
        
        <!-- 
          患者信息卡片
          显示患者的基本信息
        -->
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
              <text class="info-value">{{ appointmentDetail.patientGender === 1 ? '男' : appointmentDetail.patientGender === 0 ? '女' : '未知' }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">联系电话</text>
              <text class="info-value">{{ appointmentDetail.patientPhone }}</text>
            </view>
          </view>
        </view>
        
        <!-- 
          预约信息卡片
          显示预约的详细信息
        -->
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
              <text class="info-value">{{ appointmentDetail.appointmentTime }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">体检套餐</text>
              <text class="info-value">{{ appointmentDetail.packageName }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">体检医院</text>
              <text class="info-value">{{ appointmentDetail.hospitalName }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">套餐价格</text>
              <text class="info-value price">￥{{ appointmentDetail.packagePrice }}</text>
            </view>
          </view>
        </view>
        
        <!-- 
          操作按钮区域
          根据预约状态显示不同的操作按钮
        -->
        <view class="action-section">
          <!-- 待支付状态：显示取消和支付按钮 -->
          <view v-if="appointmentDetail.status === 1" class="action-buttons">
            <button class="btn btn-cancel" @click="cancelAppointment">
              <text class="btn-icon">❌</text>
              <text>取消预约</text>
            </button>
            <button class="btn btn-pay" @click="goToPayment">
              <text class="btn-icon">💳</text>
              <text>立即支付</text>
            </button>
          </view>
          
          <!-- 已支付状态：显示取消按钮 -->
          <view v-else-if="appointmentDetail.status === 2" class="action-buttons">
            <button class="btn btn-cancel" @click="cancelAppointment">
              <text class="btn-icon">❌</text>
              <text>取消预约</text>
            </button>
          </view>
          
          <!-- 其他状态：显示返回按钮 -->
          <view v-else class="action-buttons">
            <button class="btn btn-back" @click="goBack">
              <text class="btn-icon">🔙</text>
              <text>返回列表</text>
            </button>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
// 引入API接口
import { get } from '@/utils/request';
import appointmentApi from '@/api/appointment';

export default {
  name: 'AppointmentDetail',
  
  /**
   * 组件数据
   * @returns {Object} 组件数据对象
   */
  data() {
    return {
      // 预约ID
      appointmentId: null,
      // 预约详情数据
      appointmentDetail: null,
      // 加载状态
      loading: false,
      // 错误信息
      error: null
    }
  },
  
  /**
   * 页面加载时的处理
   * @param {Object} options 页面参数
   */
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
    /**
     * 加载预约详情
     * 调用API获取预约的详细信息
     */
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
    
    /**
     * 获取状态名称
     * @param {number} status 状态码
     * @returns {string} 状态名称
     */
    getStatusName(status) {
      const statusMap = {
        0: '已取消',
        1: '待支付',
        2: '已支付',
        3: '已完成'
      };
      return statusMap[status] || '未知状态';
    },
    
    /**
     * 格式化日期
     * @param {string} dateStr 日期字符串
     * @returns {string} 格式化后的日期
     */
    formatDate(dateStr) {
      if (!dateStr) return '未知';
      const date = new Date(dateStr);
      return date.toLocaleDateString('zh-CN');
    },
    
    /**
     * 取消预约
     * 显示确认对话框，确认后调用取消预约API
     */
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
    
    /**
     * 跳转到支付页面
     * 将当前订单信息存储到本地存储，然后跳转到支付页面
     */
    goToPayment() {
      // 将订单信息存储到本地，供支付页面使用
      uni.setStorageSync('currentOrder', JSON.stringify(this.appointmentDetail));
      
      // 跳转到支付页面
      uni.navigateTo({
        url: '/pages/payment/payment'
      });
    },
    
    /**
     * 返回列表
     * 返回上一页
     */
    goBack() {
      uni.navigateBack();
    }
  }
}
</script>

<style lang="scss" scoped>
/* 
  页面整体样式
  设置渐变背景和基本布局
*/
.content {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
  min-height: 100vh;
  padding-top: 0;
  padding-bottom: 40rpx;
  position: relative;
  overflow: hidden;
  
  /* 背景动画效果 */
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

/* 
  动态背景装饰
  添加浮动的装饰性元素
*/
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

/* 
  主要内容区域
  设置内容的定位和层级
*/
.main-content {
  padding: 20rpx 40rpx 0 40rpx;
  position: relative;
  z-index: 1;
}

/* 
  页面头部样式
  设置标题和描述的样式
*/
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

/* 
  加载状态样式
  显示加载中的提示信息
*/
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

/* 
  错误状态样式
  显示错误信息和重试按钮
*/
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

/* 
  详情内容样式
  设置详情内容的动画效果
*/
.detail-content {
  animation: fadeInUp 0.8s ease-out;
}

/* 
  详情卡片样式
  设置信息卡片的样式和交互效果
*/
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
        flex: 1;
      }
      
      .info-value {
        font-size: 28rpx;
        color: #333333;
        font-weight: 500;
        text-align: right;
        
        &.status {
          padding: 8rpx 16rpx;
          border-radius: 20rpx;
          font-weight: bold;
          font-size: 24rpx;
          
          &.status-0 {
            background: #ff5a5f;
            color: #ffffff;
          }
          
          &.status-1 {
            background: #fd9644;
            color: #ffffff;
          }
          
          &.status-2 {
            background: #0984e3;
            color: #ffffff;
          }
          
          &.status-3 {
            background: #20bf6b;
            color: #ffffff;
          }
        }
        
        &.price {
          color: #ff5a5f;
          font-weight: bold;
          font-size: 32rpx;
        }
      }
    }
  }
}

/* 
  操作按钮区域样式
  设置操作按钮的样式和布局
*/
.action-section {
  margin-top: 40rpx;
  
  .action-buttons {
    display: flex;
    gap: 20rpx;
    
    .btn {
      flex: 1;
      height: 88rpx;
      border: none;
      border-radius: 44rpx;
      font-size: 28rpx;
      font-weight: bold;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
      box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
      
      .btn-icon {
        font-size: 24rpx;
        margin-right: 10rpx;
      }
      
      &:hover {
        transform: translateY(-4rpx);
        box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.15);
      }
      
      &.btn-cancel {
        background: linear-gradient(135deg, #ff5a5f, #ff7675);
        color: #ffffff;
      }
      
      &.btn-pay {
        background: linear-gradient(135deg, #0984e3, #74b9ff);
        color: #ffffff;
      }
      
      &.btn-back {
        background: linear-gradient(135deg, #636e72, #74b9ff);
        color: #ffffff;
      }
    }
  }
}

/* 
  动画效果定义
  定义各种动画效果
*/
@keyframes flow {
  0%, 100% { transform: translate(-50%, -50%) rotate(0deg); }
  50% { transform: translate(-50%, -50%) rotate(180deg); }
}

@keyframes shimmer {
  0%, 100% { opacity: 0.3; }
  50% { opacity: 0.8; }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  33% { transform: translateY(-30px) rotate(120deg); }
  66% { transform: translateY(-15px) rotate(240deg); }
}

@keyframes pulse {
  0%, 100% { opacity: 0.8; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.05); }
}

@keyframes fadeInDown {
  from { opacity: 0; transform: translateY(-30px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-10px); }
  60% { transform: translateY(-5px); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 