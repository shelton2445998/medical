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
      <view v-if="memberName" class="member-info">
        <text class="member-label">为家庭成员预约：{{memberName}}</text>
      </view>
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
        <text class="label">预约时间：</text>
        <picker :range="timeSlots" @change="onTimeChange">
          <view class="picker-box">
            <text>{{ selectedTime || '点击选择时间' }}</text>
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">选择医生：</text>
        <picker :range="doctorList" range-key="name" @change="onDoctorChange">
          <view class="picker-box">
            <text>{{ selectedDoctor ? selectedDoctor.name : '点击选择医生' }}</text>
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">备注：</text>
        <input class="input" v-model="remark" placeholder="可填写特殊需求或备注" />
      </view>
      <button class="next-btn" @click="nextStep" :disabled="!name || !phone || !selectedDate || !gender || !idCard || !selectedDoctor">下一步</button>
    </view>

    <!-- Step 4: 支付预约 -->
    <view v-else-if="step === 4" class="step-panel">
      <view class="step-title">请确认预约信息并支付</view>
      <view class="confirm-info">
        <view class="info-section">
          <view class="section-title">医院信息</view>
          <view class="info-item">
            <text class="label">医院名称：</text>
            <text class="value">{{ selectedHospital.name }}</text>
          </view>
          <view class="info-item">
            <text class="label">医院地址：</text>
            <text class="value">{{ selectedHospital.address || '暂无地址信息' }}</text>
          </view>
        </view>
        
        <view class="info-section">
          <view class="section-title">套餐信息</view>
          <view class="info-item">
            <text class="label">套餐名称：</text>
            <text class="value">{{ selectedPackage ? selectedPackage.name : '暂无套餐信息' }}</text>
          </view>
          <view class="info-item">
            <text class="label">套餐类型：</text>
            <text class="value">{{ selectedPackage ? getPackageTypeName(selectedPackage.type) : '暂无类型信息' }}</text>
          </view>
          <view class="info-item">
            <text class="label">套餐介绍：</text>
            <text class="value">{{ selectedPackage ? selectedPackage.description : '暂无介绍信息' }}</text>
          </view>
          <view class="info-item">
            <text class="label">适用人群：</text>
            <text class="value">{{ selectedPackage ? selectedPackage.suitableCrowd : '暂无适用人群信息' }}</text>
          </view>
          <view class="info-item">
            <text class="label">检查项目：</text>
            <text class="value">{{ selectedPackage.checkItems ? selectedPackage.checkItems.join('、') : '暂无检查项目信息' }}</text>
          </view>
          <view class="info-item">
            <text class="label">预约须知：</text>
            <text class="value">{{ selectedPackage ? selectedPackage.appointmentNotice : '暂无预约须知' }}</text>
          </view>
          <view class="info-item">
            <text class="label">套餐原价：</text>
            <text class="value price">¥{{ selectedPackage ? selectedPackage.price : 0 }}</text>
          </view>
          <view class="info-item" v-if="selectedPackage && selectedPackage.discountPrice">
            <text class="label">优惠价格：</text>
            <text class="value discount-price">¥{{ selectedPackage.discountPrice }}</text>
          </view>
        </view>
        
        <view class="info-section">
          <view class="section-title">个人信息</view>
          <view class="info-item">
            <text class="label">姓名：</text>
            <text class="value">{{ name }}</text>
          </view>
          <view class="info-item">
            <text class="label">性别：</text>
            <text class="value">{{ gender }}</text>
          </view>
          <view class="info-item">
            <text class="label">手机号：</text>
            <text class="value">{{ phone }}</text>
          </view>
          <view class="info-item">
            <text class="label">身份证号：</text>
            <text class="value">{{ idCard }}</text>
          </view>
        </view>
        
        <view class="info-section">
          <view class="section-title">预约信息</view>
          <view class="info-item">
            <text class="label">预约日期：</text>
            <text class="value">{{ selectedDate }}</text>
          </view>
          <view class="info-item">
            <text class="label">预约时间：</text>
            <text class="value">{{ selectedTime || '上午(08:00-12:00)' }}</text>
          </view>
          <view class="info-item" v-if="selectedDoctor">
            <text class="label">选择医生：</text>
            <text class="value">{{ selectedDoctor.name }} ({{ selectedDoctor.title }})</text>
          </view>
          <view class="info-item" v-if="remark">
            <text class="label">备注：</text>
            <text class="value">{{ remark }}</text>
          </view>
        </view>
        
        <view class="info-section">
          <view class="section-title">费用信息</view>
          <view class="info-item">
            <text class="label">套餐原价：</text>
            <text class="value">¥{{ selectedPackage ? selectedPackage.price : 0 }}</text>
          </view>
          <view class="info-item" v-if="selectedPackage && selectedPackage.discountPrice">
            <text class="label">优惠价格：</text>
            <text class="value discount-price">¥{{ selectedPackage.discountPrice }}</text>
          </view>
          <view class="info-item" v-else-if="discount > 0">
            <text class="label">优惠金额：</text>
            <text class="value discount">-¥{{ discount }}</text>
          </view>
          <view class="info-item total">
            <text class="label">应付总额：</text>
            <text class="value total-price">¥{{ totalPrice }}</text>
          </view>
        </view>
      </view>
      
      <view class="pay-method-box">
        <view class="pay-method-title">请选择支付方式：</view>
        <radio-group @change="onPayMethodChange">
          <view class="pay-method-list">
            <label v-for="(item, idx) in payMethods" :key="idx" class="pay-method-item">
              <radio :value="item.value" :checked="payMethod === item.value" />
              <text>{{ item.name }}</text>
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
          <view class="pay-modal-title">{{ getPayMethodName(payMethod) }}扫码支付</view>
          <view class="pay-amount">支付金额：¥{{ totalPrice }}</view>
          <image class="qrcode" src="/static/images/qrcode-demo.png" mode="aspectFit"></image>
          <view class="pay-tips">请使用{{ getPayMethodName(payMethod) }}扫描二维码完成支付</view>
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
import { appointmentApi } from '@/utils/api.js';

export default {
  data() {
    return {
      step: 1,
      stepLabels: ['选择医院', '选择套餐', '填写信息', '支付预约'],
      memberId: null,
      memberName: '',
      hospitalList: [
        { id: 1, name: '沈阳市云医院-和平分院' },
        { id: 2, name: '沈阳市云医院-沈河分院' }
      ],
      packageList: [
        { 
          id: 1, 
          name: '标准体检套餐', 
          price: 299,
          discountPrice: 269,
          type: '1',
          description: '包含常规体检项目，适合一般健康检查',
          checkItems: ['血常规', '尿常规', '血压测量', '心电图'],
          suitableCrowd: '18-60岁健康人群',
          appointmentNotice: '请空腹8小时以上，避免剧烈运动'
        },
        { 
          id: 2, 
          name: '高级体检套餐', 
          price: 699,
          discountPrice: 599,
          type: '2',
          description: '包含基础套餐及更多专项检查，适合中老年人',
          checkItems: ['血常规', '尿常规', '肝功能', '肾功能', '心电图', '彩超'],
          suitableCrowd: '40岁以上中老年人',
          appointmentNotice: '请空腹12小时以上，避免饮酒'
        },
        { 
          id: 3, 
          name: '女性专项体检套餐', 
          price: 399,
          discountPrice: 359,
          type: '3',
          description: '针对女性健康的专项检查，包含乳腺、妇科检查等',
          checkItems: ['血常规', '尿常规', '妇科检查', '乳腺彩超', '宫颈涂片'],
          suitableCrowd: '18岁以上女性',
          appointmentNotice: '避开月经期，请空腹8小时以上'
        },
        { 
          id: 4, 
          name: '儿童体检套餐', 
          price: 199,
          discountPrice: 179,
          type: '3',
          description: '专为儿童设计的体检套餐，检查项目适合儿童',
          checkItems: ['血常规', '尿常规', '身高体重', '视力检查', '听力检查'],
          suitableCrowd: '3-12岁儿童',
          appointmentNotice: '请家长陪同，避免空腹时间过长'
        }
      ],
      selectedHospital: null,
      selectedPackage: null,
      name: '',
      gender: '',
      phone: '',
      idCard: '',
      selectedDate: '',
      remark: '',
      payMethods: [
        { name: '微信支付', value: '2' },
        { name: '支付宝', value: '1' },
        { name: '医保支付', value: '3' }
      ],
      payMethod: '',
      showPay: false,
      orderNo: '',
      discount: 0,
      selectedTime: '上午(08:00-12:00)',
      timeSlots: [
        '上午(08:00-12:00)',
        '下午(14:00-18:00)',
        '晚上(19:00-21:00)'
      ],
      doctorList: [],
      selectedDoctor: null
    }
  },
  computed: {
    totalPrice() {
      if (!this.selectedPackage) return 0;
      // 优先使用优惠价格，如果没有则使用原价减去优惠金额
      const finalPrice = this.selectedPackage.discountPrice || (this.selectedPackage.price - this.discount);
      return finalPrice.toFixed(2);
    }
  },
  onLoad(options) {
    // 获取家庭成员信息
    if (options.memberId) {
      this.memberId = options.memberId;
    }
    if (options.memberName) {
      this.memberName = options.memberName;
    }
    
    // 检查是否有已选择的医院和套餐
    const selectedHospital = uni.getStorageSync('selectedHospital');
    const selectedPackage = uni.getStorageSync('selectedPackage');
    
    if (selectedHospital && selectedPackage) {
      // 如果已有选择的医院和套餐，直接跳到填写信息步骤
      this.selectedHospital = JSON.parse(selectedHospital);
      this.selectedPackage = JSON.parse(selectedPackage);
      this.step = 3; // 直接跳到填写信息步骤
      
      // 如果是为家庭成员预约，预填家庭成员信息
      if (this.memberName) {
        this.name = this.memberName;
      }
    } else if (selectedPackage) {
      // 如果只有套餐信息（从套餐详情页进入），设置套餐并跳到填写信息步骤
      this.selectedPackage = JSON.parse(selectedPackage);
      // 从套餐信息中获取医院信息
      if (this.selectedPackage && this.selectedPackage.hospitalId) {
        this.selectedHospital = {
          id: this.selectedPackage.hospitalId,
          name: this.selectedPackage.hospitalName,
          address: this.selectedPackage.hospitalAddress
        };
      }
      this.step = 3; // 直接跳到填写信息步骤
      
      // 如果是为家庭成员预约，预填家庭成员信息
      if (this.memberName) {
        this.name = this.memberName;
      }
    }
    
    // 初始化医生列表
    this.initDoctorList();
    
    // 获取用户预约列表
    this.getAppointmentList();
  },
  methods: {
    onHospitalChange(e) {
      this.selectedHospital = this.hospitalList[e.detail.value];
      // 选择医院后获取该医院的医生列表
      this.getDoctorList(this.selectedHospital.id);
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
    onTimeChange(e) {
      this.selectedTime = this.timeSlots[e.detail.value];
    },
    onDoctorChange(e) {
      this.selectedDoctor = this.doctorList[e.detail.value];
    },
    onPayMethodChange(e) {
      this.payMethod = e.detail.value;
    },
    getPackageTypeName(type) {
      const typeMap = {
        1: '基础套餐',
        2: '高级套餐', 
        3: '专项套餐'
      };
      return typeMap[type] || '未知类型';
    },
    getPayMethodName(value) {
      const method = this.payMethods.find(item => item.value == value);
      return method ? method.name : '未知支付方式';
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
      // 显示支付中状态
      this.showPay = false;
      uni.showLoading({ title: '支付中...' });
      
      // 构建App预约订单数据，对应后端AppOrdersDto结构
      const orderData = {
        setmealId: this.selectedPackage ? this.selectedPackage.id : null, // 套餐ID
        hospitalId: this.selectedHospital ? this.selectedHospital.id : null, // 医院ID
        doctorId: this.selectedDoctor ? this.selectedDoctor.id : null, // 医生ID
        familyMemberId: this.memberId || 1, // 家庭成员ID，如果没有则为1
        appointmentDate: this.selectedDate, // 预约日期
        appointmentTime: this.selectedTime, // 预约时间段
        remark: this.remark // 备注信息
      };
      
      console.log('预约订单数据：', orderData);
      
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      
      // 使用新的API配置调用后端App预约接口
      uni.request({
        url: appointmentApi.createAppointment,
        method: 'POST',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token || ''
        },
        data: orderData,
        success: (res) => {
          console.log('预约接口响应：', res);
          if (res.statusCode === 200 && res.data.code === 200) {
            // 预约成功
            uni.hideLoading();
            this.orderNo = res.data.data.orderNumber || 'YY' + Date.now();
            this.step = 5;
            
            // 清除存储的医院和套餐信息
            uni.removeStorageSync('selectedHospital');
            uni.removeStorageSync('selectedPackage');
            
            // 显示成功提示
            uni.showToast({
              title: '预约成功！',
              icon: 'success',
              duration: 2000
            });
            
            // 支付成功后2秒自动返回首页
            setTimeout(() => {
              uni.reLaunch({ url: '/pages/index/index' });
            }, 2000);
          } else {
            // 预约失败
            uni.hideLoading();
            uni.showToast({
              title: res.data.msg || '预约失败，请重试',
              icon: 'none',
              duration: 2000
            });
          }
        },
        fail: (err) => {
          console.error('预约接口调用失败：', err);
          uni.hideLoading();
          uni.showToast({
            title: '网络错误，请检查网络连接',
            icon: 'none',
            duration: 2000
          });
        }
      });
    },
    goHome() {
      // 清除存储的医院和套餐信息
      uni.removeStorageSync('selectedHospital');
      uni.removeStorageSync('selectedPackage');
      uni.reLaunch({ url: '/pages/index/index' });
    },
    
    // 获取医生列表（根据医院ID获取对应医生）
    getDoctorList(hospitalId) {
      if (!hospitalId) return;
      
      // 暂时使用固定的医生列表，后续可以根据医院ID筛选
      // 这里可以根据hospitalId来筛选对应医院的医生
      this.doctorList = [
        { id: 3001, name: '张医生', title: '主任医师', department: '内科' },
        { id: 3002, name: '李医生', title: '副主任医师', department: '外科' },
        { id: 3003, name: '王医生', title: '主治医师', department: '妇产科' }
      ];
      
      // 默认选择第一个医生
      if (this.doctorList.length > 0) {
        this.selectedDoctor = this.doctorList[0];
      }
    },
    
    // 初始化医生列表
    initDoctorList() {
      // 设置默认医生列表
      this.doctorList = [
        { id: 3001, name: '张医生', title: '主任医师', department: '内科' },
        { id: 3002, name: '李医生', title: '副主任医师', department: '外科' },
        { id: 3003, name: '王医生', title: '主治医师', department: '妇产科' }
      ];
      
      // 默认选择第一个医生
      if (this.doctorList.length > 0) {
        this.selectedDoctor = this.doctorList[0];
      }
    },
    
    // 获取用户预约列表
    getAppointmentList() {
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      
      uni.request({
        url: appointmentApi.getAppointmentList,
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token || ''
        },
        success: (res) => {
          console.log('预约列表响应：', res);
          if (res.statusCode === 200 && res.data.code === 200) {
            // 处理预约列表数据
            const appointmentList = res.data.data.records || [];
            console.log('预约列表：', appointmentList);
            // 这里可以将数据存储到本地或传递给其他组件
            uni.setStorageSync('appointmentList', appointmentList);
          }
        },
        fail: (err) => {
          console.error('获取预约列表失败：', err);
        }
      });
    },
    
    // 获取预约详情
    getAppointmentDetail(id) {
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      
      uni.request({
        url: appointmentApi.getAppointmentDetail(id),
        method: 'GET',
        header: {
          'Content-Type': 'application/json',
          'Authorization': token || ''
        },
        success: (res) => {
          console.log('预约详情响应：', res);
          if (res.statusCode === 200 && res.data.code === 200) {
            const appointmentDetail = res.data.data;
            console.log('预约详情：', appointmentDetail);
            // 处理预约详情数据
          }
        },
        fail: (err) => {
          console.error('获取预约详情失败：', err);
        }
      });
    },
    
    // 取消预约
    cancelAppointment(id) {
      uni.showModal({
        title: '确认取消',
        content: '确定要取消这个预约吗？',
        success: (res) => {
          if (res.confirm) {
            // 获取token
            const token = uni.getStorageSync('uniIdToken');
            
            uni.request({
              url: appointmentApi.cancelAppointment(id),
              method: 'PUT',
              header: {
                'Content-Type': 'application/json',
                'Authorization': token || ''
              },
              success: (res) => {
                console.log('取消预约响应：', res);
                if (res.statusCode === 200 && res.data.code === 200) {
                  uni.showToast({
                    title: '取消成功',
                    icon: 'success',
                    duration: 2000
                  });
                  // 刷新预约列表
                  this.getAppointmentList();
                } else {
                  uni.showToast({
                    title: res.data.msg || '取消失败',
                    icon: 'none',
                    duration: 2000
                  });
                }
              },
              fail: (err) => {
                console.error('取消预约失败：', err);
                uni.showToast({
                  title: '网络错误',
                  icon: 'none',
                  duration: 2000
                });
              }
            });
          }
        }
      });
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

.member-info {
  background: #e6f7ff;
  border: 1rpx solid #91d5ff;
  border-radius: 8rpx;
  padding: 20rpx;
  margin-bottom: 30rpx;
  text-align: center;
}

.member-label {
  font-size: 28rpx;
  color: #1296db;
  font-weight: bold;
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
}

.info-section {
  margin-bottom: 30rpx;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 10rpx;
  border-left: 4rpx solid #1296db;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #1296db;
  margin-bottom: 15rpx;
  padding-bottom: 10rpx;
  border-bottom: 1rpx solid #e9ecef;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
  padding: 8rpx 0;
}

.info-item .label {
  color: #666;
  font-size: 26rpx;
  min-width: 120rpx;
}

.info-item .value {
  color: #333;
  font-size: 26rpx;
  text-align: right;
  flex: 1;
  word-break: break-all;
  line-height: 1.4;
}

.info-item .price {
  color: #ff5a5f;
  font-weight: bold;
}

.info-item .discount-price {
  color: #52c41a;
  font-weight: bold;
}

.info-item .discount {
  color: #52c41a;
  font-weight: bold;
}

.info-item.total {
  border-top: 1rpx solid #e9ecef;
  padding-top: 15rpx;
  margin-top: 15rpx;
}

.info-item.total .label {
  font-weight: bold;
  color: #333;
}

.info-item.total .total-price {
  color: #ff5a5f;
  font-size: 32rpx;
  font-weight: bold;
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
  margin-bottom: 20rpx;
  text-align: center;
}

.pay-amount {
  font-size: 28rpx;
  color: #ff5a5f;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20rpx;
}

.pay-tips {
  font-size: 24rpx;
  color: #666;
  text-align: center;
  margin: 20rpx 0;
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