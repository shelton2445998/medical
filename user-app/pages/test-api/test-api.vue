<template>
  <view class="test-api-container">
    <view class="section">
      <view class="section-title">预约功能测试</view>
      
      <!-- 创建预约测试 -->
      <view class="test-item">
        <view class="test-title">创建预约</view>
        <view class="form-item">
          <text class="label">套餐ID：</text>
          <input class="input" v-model="createData.setmealId" placeholder="请输入套餐ID" type="number" />
        </view>
        <view class="form-item">
          <text class="label">医院ID：</text>
          <input class="input" v-model="createData.hospitalId" placeholder="请输入医院ID" type="number" />
        </view>
        <view class="form-item">
          <text class="label">家庭成员ID：</text>
          <input class="input" v-model="createData.familyMemberId" placeholder="请输入家庭成员ID" type="number" />
        </view>
        <view class="form-item">
          <text class="label">预约日期：</text>
          <picker mode="date" @change="onDateChange">
            <view class="picker-box">
              <text>{{ createData.appointmentDate || '点击选择日期' }}</text>
            </view>
          </picker>
        </view>
        <view class="form-item">
          <text class="label">预约时间：</text>
          <picker :range="timeSlots" @change="onTimeChange">
            <view class="picker-box">
              <text>{{ createData.appointmentTime || '点击选择时间' }}</text>
            </view>
          </picker>
        </view>
        <view class="form-item">
          <text class="label">备注：</text>
          <input class="input" v-model="createData.remark" placeholder="请输入备注" />
        </view>
        <button class="test-btn" @click="testCreateAppointment">测试创建预约</button>
      </view>
      
      <!-- 获取预约列表测试 -->
      <view class="test-item">
        <view class="test-title">获取预约列表</view>
        <button class="test-btn" @click="testGetAppointmentList">测试获取预约列表</button>
      </view>
      
      <!-- 获取预约详情测试 -->
      <view class="test-item">
        <view class="test-title">获取预约详情</view>
        <view class="form-item">
          <text class="label">预约ID：</text>
          <input class="input" v-model="detailId" placeholder="请输入预约ID" type="number" />
        </view>
        <button class="test-btn" @click="testGetAppointmentDetail">测试获取预约详情</button>
      </view>
      
      <!-- 取消预约测试 -->
      <view class="test-item">
        <view class="test-title">取消预约</view>
        <view class="form-item">
          <text class="label">预约ID：</text>
          <input class="input" v-model="cancelId" placeholder="请输入预约ID" type="number" />
        </view>
        <button class="test-btn" @click="testCancelAppointment">测试取消预约</button>
      </view>
    </view>
    
    <!-- 结果显示 -->
    <view class="result-section">
      <view class="result-title">测试结果</view>
      <scroll-view class="result-content" scroll-y>
        <text class="result-text">{{ testResult }}</text>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { appointmentApi } from '@/utils/api.js';

export default {
  data() {
    return {
      createData: {
        setmealId: 1,
        hospitalId: 1,
        familyMemberId: 1,
        appointmentDate: '',
        appointmentTime: '',
        remark: '测试预约'
      },
      detailId: '',
      cancelId: '',
      testResult: '',
      timeSlots: [
        '上午(08:00-12:00)',
        '下午(14:00-18:00)',
        '晚上(19:00-21:00)'
      ]
    }
  },
  methods: {
    onDateChange(e) {
      this.createData.appointmentDate = e.detail.value;
    },
    onTimeChange(e) {
      this.createData.appointmentTime = this.timeSlots[e.detail.value];
    },
    
    // 测试创建预约
    testCreateAppointment() {
      this.logResult('开始测试创建预约...');
      
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      if (!token) {
        this.logResult('错误：未找到token，请先登录');
        return;
      }
      
      // 验证必填字段
      if (!this.createData.setmealId || !this.createData.hospitalId || 
          !this.createData.appointmentDate || !this.createData.appointmentTime) {
        this.logResult('错误：请填写完整的预约信息');
        return;
      }
      
      uni.request({
        url: appointmentApi.createAppointment,
        method: 'POST',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token || ''
        },
        data: this.createData,
        success: (res) => {
          this.logResult(`创建预约响应：\n状态码：${res.statusCode}\n数据：${JSON.stringify(res.data, null, 2)}`);
          
          if (res.statusCode === 200 && res.data.code === 200) {
            this.logResult('✅ 创建预约成功！');
          } else {
            this.logResult('❌ 创建预约失败');
          }
        },
        fail: (err) => {
          this.logResult(`❌ 创建预约请求失败：${JSON.stringify(err)}`);
        }
      });
    },
    
    // 测试获取预约列表
    testGetAppointmentList() {
      this.logResult('开始测试获取预约列表...');
      
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      if (!token) {
        this.logResult('错误：未找到token，请先登录');
        return;
      }
      
      uni.request({
        url: appointmentApi.getAppointmentList,
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token || ''
        },
        success: (res) => {
          this.logResult(`获取预约列表响应：\n状态码：${res.statusCode}\n数据：${JSON.stringify(res.data, null, 2)}`);
          
          if (res.statusCode === 200 && res.data.code === 200) {
            this.logResult('✅ 获取预约列表成功！');
          } else {
            this.logResult('❌ 获取预约列表失败');
          }
        },
        fail: (err) => {
          this.logResult(`❌ 获取预约列表请求失败：${JSON.stringify(err)}`);
        }
      });
    },
    
    // 测试获取预约详情
    testGetAppointmentDetail() {
      if (!this.detailId) {
        this.logResult('错误：请输入预约ID');
        return;
      }
      
      this.logResult(`开始测试获取预约详情，ID：${this.detailId}...`);
      
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      if (!token) {
        this.logResult('错误：未找到token，请先登录');
        return;
      }
      
      uni.request({
        url: appointmentApi.getAppointmentDetail(this.detailId),
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token || ''
        },
        success: (res) => {
          this.logResult(`获取预约详情响应：\n状态码：${res.statusCode}\n数据：${JSON.stringify(res.data, null, 2)}`);
          
          if (res.statusCode === 200 && res.data.code === 200) {
            this.logResult('✅ 获取预约详情成功！');
          } else {
            this.logResult('❌ 获取预约详情失败');
          }
        },
        fail: (err) => {
          this.logResult(`❌ 获取预约详情请求失败：${JSON.stringify(err)}`);
        }
      });
    },
    
    // 测试取消预约
    testCancelAppointment() {
      if (!this.cancelId) {
        this.logResult('错误：请输入预约ID');
        return;
      }
      
      this.logResult(`开始测试取消预约，ID：${this.cancelId}...`);
      
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      if (!token) {
        this.logResult('错误：未找到token，请先登录');
        return;
      }
      
      uni.request({
        url: appointmentApi.cancelAppointment(this.cancelId),
        method: 'PUT',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token || ''
        },
        success: (res) => {
          this.logResult(`取消预约响应：\n状态码：${res.statusCode}\n数据：${JSON.stringify(res.data, null, 2)}`);
          
          if (res.statusCode === 200 && res.data.code === 200) {
            this.logResult('✅ 取消预约成功！');
          } else {
            this.logResult('❌ 取消预约失败');
          }
        },
        fail: (err) => {
          this.logResult(`❌ 取消预约请求失败：${JSON.stringify(err)}`);
        }
      });
    },
    
    // 记录测试结果
    logResult(message) {
      const timestamp = new Date().toLocaleTimeString();
      this.testResult = `[${timestamp}] ${message}\n\n${this.testResult}`;
    }
  }
}
</script>

<style scoped>
.test-api-container {
  padding: 20rpx;
}

.section {
  margin-bottom: 40rpx;
}

.section-title {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
}

.test-item {
  background: #f8f9fa;
  padding: 20rpx;
  margin-bottom: 20rpx;
  border-radius: 10rpx;
}

.test-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
}

.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.label {
  width: 200rpx;
  font-size: 28rpx;
  color: #666;
}

.input {
  flex: 1;
  height: 60rpx;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
}

.picker-box {
  flex: 1;
  height: 60rpx;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
  font-size: 28rpx;
  background: #fff;
}

.test-btn {
  background: #007aff;
  color: white;
  border: none;
  border-radius: 8rpx;
  padding: 20rpx;
  font-size: 28rpx;
  margin-top: 20rpx;
}

.test-btn:active {
  background: #0056b3;
}

.result-section {
  margin-top: 40rpx;
}

.result-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
}

.result-content {
  background: #f8f9fa;
  padding: 20rpx;
  border-radius: 10rpx;
  height: 400rpx;
}

.result-text {
  font-size: 24rpx;
  color: #333;
  line-height: 1.5;
  white-space: pre-wrap;
}
</style> 