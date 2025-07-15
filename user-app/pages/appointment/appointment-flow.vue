<template>
  <view class="appointment-flow-content">
    <!-- 动态背景装饰 -->
    <view class="floating-shapes">
      <view class="shape shape-1"></view>
      <view class="shape shape-2"></view>
      <view class="shape shape-3"></view>
      <view class="shape shape-4"></view>
    </view>
    
    <!-- 页面头部 -->
    <view class="page-header">
      <view class="header-icon">🏥</view>
      <view class="header-title">体检预约</view>
      <view class="header-desc">简单几步，轻松完成预约</view>
    </view>
    
    <!-- 步骤指示器 -->
    <view class="step-indicator">
      <view v-for="(label, idx) in stepLabels" :key="idx" :class="['step', {active: step === idx+1, completed: step > idx+1}]">
        <view class="step-circle">
          <text class="step-number">{{idx + 1}}</text>
          <text class="step-check" v-if="step > idx+1">✓</text>
        </view>
        <text class="step-label">{{ label }}</text>
        <view v-if="idx < stepLabels.length - 1" class="step-arrow">→</view>
      </view>
    </view>

    <!-- Step 1: 选择医院 -->
    <view v-if="step === 1" class="step-panel">
      <view class="step-card">
        <view class="card-header">
          <view class="card-icon">🏥</view>
          <view class="card-title">选择医院</view>
          <view class="card-desc">请选择您要体检的医院</view>
        </view>
        
        <view class="hospital-selection">
          <view 
            class="hospital-item" 
            v-for="(hospital, index) in hospitalList" 
            :key="index"
            :class="{active: selectedHospital && selectedHospital.id === hospital.id}"
            @click="selectHospital(hospital)"
          >
            <view class="hospital-info">
              <text class="hospital-name">{{hospital.name}}</text>
              <text class="hospital-address">{{hospital.address || '地址信息待完善'}}</text>
            </view>
            <view class="hospital-check">
              <text class="check-icon" v-if="selectedHospital && selectedHospital.id === hospital.id">✓</text>
            </view>
          </view>
        </view>
        
        <button class="next-btn" @click="nextStep" :disabled="!selectedHospital">
          <text class="btn-icon">→</text>
          <text class="btn-text">下一步</text>
        </button>
      </view>
    </view>

    <!-- Step 2: 选择套餐 -->
    <view v-else-if="step === 2" class="step-panel">
      <view class="step-card">
        <view class="card-header">
          <view class="card-icon">📋</view>
          <view class="card-title">选择套餐</view>
          <view class="card-desc">请选择适合您的体检套餐</view>
        </view>
        
        <view class="package-selection">
          <view 
            class="package-item" 
            v-for="(pkg, index) in packageList" 
            :key="index"
            :class="{active: selectedPackage && selectedPackage.id === pkg.id}"
            @click="selectPackage(pkg)"
          >
            <view class="package-header">
              <text class="package-name">{{pkg.name}}</text>
              <view class="package-price">
                <text class="price-symbol">¥</text>
                <text class="price-value">{{pkg.discountPrice || pkg.price}}</text>
                <text class="price-original" v-if="pkg.discountPrice">¥{{pkg.price}}</text>
              </view>
            </view>
            <text class="package-desc">{{pkg.description}}</text>
            <view class="package-tags">
              <text class="package-tag">{{getPackageTypeName(pkg.type)}}</text>
              <text class="package-tag">{{pkg.suitableCrowd}}</text>
            </view>
            <view class="package-check">
              <text class="check-icon" v-if="selectedPackage && selectedPackage.id === pkg.id">✓</text>
            </view>
          </view>
        </view>
        
        <button class="next-btn" @click="nextStep" :disabled="!selectedPackage">
          <text class="btn-icon">→</text>
          <text class="btn-text">下一步</text>
        </button>
      </view>
    </view>

    <!-- Step 3: 填写信息 -->
    <view v-else-if="step === 3" class="step-panel">
      <view class="step-card">
        <view class="card-header">
          <view class="card-icon">📝</view>
          <view class="card-title">填写信息</view>
          <view class="card-desc">请填写您的预约信息</view>
        </view>
        
      <view v-if="memberName" class="member-info">
          <view class="member-badge">
            <text class="member-icon">👥</text>
            <text class="member-text">为家庭成员预约：{{memberName}}</text>
      </view>
        </view>
        
        <view class="form-section">
      <view class="form-item">
            <text class="form-label">姓名</text>
            <input class="form-input" v-model="name" placeholder="请输入姓名" />
      </view>
          
      <view class="form-item">
            <text class="form-label">患者性别</text>
            <picker :range="genderOptions" @change="onGenderChange">
              <view class="picker-display">
                <text class="picker-text" :class="{ 'placeholder': !patientGender }">
                  {{ patientGender || '请选择性别' }}
                </text>
                <text class="picker-icon">👤</text>
          </view>
        </picker>
      </view>
          
      <view class="form-item">
            <text class="form-label">预约日期</text>
        <picker mode="date" @change="onDateChange">
              <view class="picker-display">
                <text class="picker-text" :class="{ 'placeholder': !selectedDate }">
                  {{ selectedDate || '请选择预约日期' }}
                </text>
                <text class="picker-icon">📅</text>
          </view>
        </picker>
      </view>
          
      <view class="form-item">
            <text class="form-label">预约时间</text>
        <picker :range="timeSlots" @change="onTimeChange">
              <view class="picker-display">
                <text class="picker-text" :class="{ 'placeholder': !selectedTime }">
                  {{ selectedTime || '请选择预约时间' }}
                </text>
                <text class="picker-icon">⏰</text>
          </view>
        </picker>
      </view>
          
      <view class="form-item">
            <text class="form-label">选择医生</text>
        <picker :range="doctorList" range-key="name" @change="onDoctorChange">
              <view class="picker-display">
                <text class="picker-text" :class="{ 'placeholder': !selectedDoctor }">
                  {{ selectedDoctor ? `${selectedDoctor.name} (${selectedDoctor.title})` : '张医生 (主任医师)' }}
                </text>
                <text class="picker-icon">👨‍⚕️</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
            <text class="form-label">患者年龄</text>
            <input 
              class="form-input" 
              v-model="patientAge" 
              placeholder="请输入患者年龄"
              placeholder-class="input-placeholder"
              type="number" 
              maxlength="3" 
            />
      </view>

          <view class="form-item">
            <text class="form-label">患者手机号</text>
            <input 
              class="form-input" 
              v-model="patientPhone" 
              placeholder="请输入患者手机号"
              placeholder-class="input-placeholder"
              type="number" 
              maxlength="11" 
            />
      </view>

          <view class="form-item">
            <text class="form-label">备注信息</text>
            <textarea 
              class="form-textarea" 
              v-model="remark" 
              placeholder="请输入备注信息（可选）"
              placeholder-class="input-placeholder"
              maxlength="200"
            />
      </view>
        </view>
        
        <button class="submit-btn" @click="createOrder">
          <text class="btn-icon">💳</text>
          <text class="btn-text">创建订单并支付</text>
        </button>
      </view>
    </view>

    <!-- Step 4: 预约成功 -->
    <view v-else-if="step === 4" class="step-panel">
      <view class="success-card">
        <view class="success-icon">✅</view>
        <view class="success-title">预约成功！</view>
        <view class="success-desc">您的预约已提交，请按时到院体检</view>
        <view class="success-info">
          <view class="info-item">
            <text class="info-label">预约编号</text>
            <text class="info-value">{{orderNo}}</text>
      </view>
          <view class="info-item">
            <text class="info-label">体检医院</text>
            <text class="info-value">{{selectedHospital.name}}</text>
          </view>
          <view class="info-item">
            <text class="info-label">体检时间</text>
            <text class="info-value">{{selectedDate}} {{selectedTime}}</text>
          </view>
        </view>
        <button class="back-btn" @click="goBack">
          <text class="btn-icon">🏠</text>
          <text class="btn-text">返回首页</text>
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import { appointmentApi, hospitalApi } from '@/utils/api.js';
import { post, get } from '@/utils/request.js';

export default {
  data() {
    return {
      step: 1,
      stepLabels: ['选择医院', '选择套餐', '填写信息', '预约成功'],
      memberId: null,
      memberName: '',
      hospitalList: [
        { id: 1, name: '沈阳市云医院-和平分院', address: '沈阳市和平区南京南街123号' },
        { id: 2, name: '沈阳市云医院-沈河分院', address: '沈阳市沈河区文化路100号' }
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
      selectedDoctor: null,
      patientAge: '',
      patientGender: '',
      patientPhone: '',
      genderOptions: ['男', '女'],
      hospitalList: []
    }
  },
  computed: {
    totalPrice() {
      if (!this.selectedPackage) return 0;
      
      // 如果是普通项目预约（没有套餐ID），直接使用price字段
      if (!this.selectedPackage.id) {
        return this.selectedPackage.price ? this.selectedPackage.price.toFixed(2) : '0.00';
      }
      
      // 套餐预约：优先使用优惠价格，如果没有则使用原价减去优惠金额
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
    const customPackage = uni.getStorageSync('customPackage'); // 检查是否有定制套餐
    
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
    } else if (customPackage) {
      // 如果有定制套餐信息（从普通项目预约进入），设置定制套餐并直接跳到支付步骤
      this.selectedPackage = JSON.parse(customPackage);
      // 从定制套餐信息中获取医院信息
      if (this.selectedPackage && this.selectedPackage.hospitalId) {
        this.selectedHospital = {
          id: this.selectedPackage.hospitalId,
          name: this.selectedPackage.hospitalName,
          address: this.selectedPackage.hospitalAddress
        };
      }
      // 预填一些基本信息，使用定制套餐中的患者姓名
      this.name = this.selectedPackage.patientName || this.memberName || '用户';
      this.gender = '男'; // 默认性别
      this.selectedDate = this.getNextAvailableDate(); // 设置默认日期
      this.selectedTime = '上午(08:00-12:00)'; // 默认时间段
      this.selectedDoctor = { id: 3001, name: '张医生', title: '主任医师', department: '内科' }; // 默认医生
      
      this.step = 4; // 直接跳到支付步骤
    }
    
    // 初始化医生列表
    this.initDoctorList();
    
    // 获取医院列表
    this.getHospitalList();
    
    // 获取用户预约列表
    this.getAppointmentList();
  },
  methods: {
    selectHospital(hospital) {
      this.selectedHospital = hospital;
      // 选择医院后获取该医院的医生列表
      this.getDoctorList(this.selectedHospital.id);
    },
    selectPackage(pkg) {
      this.selectedPackage = pkg;
    },
    selectGender(gender) {
      this.gender = gender;
    },
    onTimeChange(e) {
      const index = e.detail.value;
      this.selectedTime = this.timeSlots[index];
    },
    onDoctorChange(e) {
      const index = e.detail.value;
      console.log('医生选择事件触发，index:', index, 'doctorList:', this.doctorList);
      if (index >= 0 && index < this.doctorList.length) {
        this.selectedDoctor = this.doctorList[index];
        console.log('选择医生:', this.selectedDoctor);
      } else {
        this.selectedDoctor = null;
        console.log('医生选择无效，设置为null');
      }
    },
    selectPayMethod(value) {
      this.payMethod = value;
    },
    onDateChange(e) {
      this.selectedDate = e.detail.value;
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
    createOrder() {
      // 验证必需字段
      if (!this.selectedHospital || !this.selectedHospital.id) {
        uni.showToast({
          title: '请选择医院',
          icon: 'none',
          duration: 2000
        });
        return;
      }
      
      // 如果没有选择医生，使用默认医生ID 3001
      if (!this.selectedDoctor || !this.selectedDoctor.id) {
        console.log('未选择医生，使用默认医生ID: 3001');
        this.selectedDoctor = { id: 3001, name: '张医生', title: '主任医师', department: '内科' };
      }
      
      if (!this.selectedDate) {
        uni.showToast({
          title: '请选择预约日期',
          icon: 'none',
          duration: 2000
        });
        return;
      }
      
      if (!this.selectedTime) {
        uni.showToast({
          title: '请选择预约时间',
          icon: 'none',
          duration: 2000
        });
        return;
      }
      
      // 显示加载状态
      uni.showLoading({ title: '创建订单中...' });
      
      // 构建App预约订单数据，对应后端AppOrdersDto结构
      const orderData = {
        setmealId: this.selectedPackage ? this.selectedPackage.id : null, // 套餐ID，普通项目预约时为null
        hospitalId: this.selectedHospital.id, // 医院ID
        doctorId: this.selectedDoctor ? this.selectedDoctor.id : 3001, // 医生ID，默认3001
        familyMemberId: this.memberId || 1, // 家庭成员ID，如果没有则为1
        appointmentDate: this.selectedDate, // 预约日期
        appointmentTime: this.selectedTime, // 预约时间段
        remark: this.remark || '', // 备注信息
        checkitemIds: this.selectedPackage ? (this.selectedPackage.checkitemIds || '') : '', // 检查项ID列表
        patientName: this.selectedPackage && this.selectedPackage.patientName ? this.selectedPackage.patientName : this.name, // 优先使用定制套餐中的患者姓名
        patientAge: this.selectedPackage && this.selectedPackage.patientAge ? this.selectedPackage.patientAge : (parseInt(this.patientAge) || 0), // 优先使用定制套餐中的患者年龄
        patientGender: this.selectedPackage && this.selectedPackage.patientGender ? this.selectedPackage.patientGender : this.convertGenderToNumber(this.patientGender || this.gender), // 优先使用定制套餐中的患者性别
        patientPhone: this.selectedPackage && this.selectedPackage.patientPhone ? this.selectedPackage.patientPhone : (this.patientPhone || '') // 优先使用定制套餐中的患者手机号
      };
      
      console.log('预约订单数据：', orderData);
      console.log('selectedDoctor:', this.selectedDoctor);
      console.log('selectedDoctor.id:', this.selectedDoctor ? this.selectedDoctor.id : 'null');
      console.log('套餐检查项目ID列表:', this.selectedPackage ? this.selectedPackage.checkitemIds : '无套餐');
      
      // 获取token
      const token = uni.getStorageSync('uniIdToken');
      
      // 调用后端App预约接口创建订单
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
            // 预约成功，创建订单
            uni.hideLoading();
            
            // 存储订单信息，供支付页面使用
            const orderInfo = res.data.data;
            uni.setStorageSync('currentOrder', JSON.stringify(orderInfo));
            
            // 清除存储的医院、套餐和定制套餐信息
            uni.removeStorageSync('selectedHospital');
            uni.removeStorageSync('selectedPackage');
            uni.removeStorageSync('customPackage');
            
            // 显示成功提示
            uni.showToast({
              title: '订单创建成功，即将跳转支付页面',
              icon: 'success',
              duration: 1500
            });
            
            // 跳转到支付页面
            setTimeout(() => {
              uni.navigateTo({
                url: '/pages/payment/payment'
              });
            }, 1500);
          } else {
            // 预约失败
            uni.hideLoading();
            uni.showToast({
              title: res.data.msg || '创建订单失败，请重试',
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
    goBack() {
      // 清除存储的医院和套餐信息
      uni.removeStorageSync('selectedHospital');
      uni.removeStorageSync('selectedPackage');
      uni.reLaunch({ url: '/pages/index/index' });
    },
    
    // 获取下一个可用日期（明天）
    getNextAvailableDate() {
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 1);
      const year = tomorrow.getFullYear();
      const month = String(tomorrow.getMonth() + 1).padStart(2, '0');
      const day = String(tomorrow.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
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
        console.log('初始化医生列表，默认选择:', this.selectedDoctor);
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
    },
    onGenderChange(e) {
      this.patientGender = this.genderOptions[e.detail.value];
    },
    
    // 将性别字符串转换为数字
    convertGenderToNumber(gender) {
      if (gender === '男' || gender === 'male' || gender === 1) {
        return 1;
      } else if (gender === '女' || gender === 'female' || gender === 0) {
        return 0;
      } else {
        // 默认返回1（男）
        return 1;
      }
    },
    onHospitalChange(e) {
      const index = e.detail.value;
      this.selectedHospital = this.hospitalList[index];
      // 选择医院后获取该医院的医生列表
      this.getDoctorList(this.selectedHospital.id);
    },
    // 获取医院列表
    async getHospitalList() {
      try {
        uni.showLoading({ title: '加载医院列表...' });
        
        // 检查是否有token
        const token = uni.getStorageSync('TOKEN_KEY');
        if (!token) {
          uni.hideLoading();
          uni.showToast({
            title: '请先登录',
            icon: 'none',
            duration: 2000
          });
          // 跳转到登录页
          uni.navigateTo({
            url: '/pages/login/login'
          });
          return;
        }
        
        // 使用后端AppHospitalController的接口
        const result = await get(hospitalApi.getHospitalList, {
          pageNum: 1,
          pageSize: 100
        });
        
        console.log('医院列表API响应:', result);
        console.log('医院数据详情:', result.data);
        
        if (result && result.data) {
          // 检查是否是分页数据结构
          if (result.data.records && Array.isArray(result.data.records)) {
            this.hospitalList = result.data.records;
            console.log('从records字段获取医院数据:', this.hospitalList);
          } else if (result.data.list && Array.isArray(result.data.list)) {
            this.hospitalList = result.data.list;
            console.log('从list字段获取医院数据:', this.hospitalList);
          } else if (Array.isArray(result.data)) {
            this.hospitalList = result.data;
            console.log('从data直接获取医院数据:', this.hospitalList);
          } else {
            // 如果不是数组，尝试获取data中的其他可能字段
            console.log('医院数据结构:', JSON.stringify(result.data, null, 2));
            this.hospitalList = [];
          }
          console.log('获取医院列表成功:', this.hospitalList);
        } else {
          console.error('获取医院列表失败:', result);
          // 使用测试数据
          this.hospitalList = [
            { id: 2001, name: '北京协和医院', address: '北京市东城区东单帅府园1号' },
            { id: 2002, name: '上海交通大学医学院附属瑞金医院', address: '上海市黄浦区瑞金二路197号' },
            { id: 2003, name: '中山大学附属第一医院', address: '广州市越秀区中山二路1号' },
            { id: 2004, name: '四川大学华西医院', address: '成都市武侯区国学巷37号' }
          ];
          uni.showToast({
            title: '使用测试医院数据',
            icon: 'none',
            duration: 2000
          });
        }
      } catch (error) {
        console.error('获取医院列表异常:', error);
        // 使用测试数据
        this.hospitalList = [
          { id: 2001, name: '北京协和医院', address: '北京市东城区东单帅府园1号' },
          { id: 2002, name: '上海交通大学医学院附属瑞金医院', address: '上海市黄浦区瑞金二路197号' },
          { id: 2003, name: '中山大学附属第一医院', address: '广州市越秀区中山二路1号' },
          { id: 2004, name: '四川大学华西医院', address: '成都市武侯区国学巷37号' }
        ];
        uni.showToast({
          title: '使用测试医院数据',
          icon: 'none',
          duration: 2000
        });
      } finally {
        uni.hideLoading();
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.flow-content {
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

@keyframes slideInDown {
  from {
    opacity: 0;
    transform: translateY(-30rpx);
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

@keyframes success {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    transform: scale(1.2);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.main-content {
  padding: 20rpx 40rpx 0 40rpx;
  width: 100%;
  box-sizing: border-box;
}

.success-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  margin-bottom: 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
  text-align: center;
  animation: slideInUp 0.8s ease-out;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
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
  
  .success-icon {
    font-size: 120rpx;
    color: #00b894;
    margin-bottom: 30rpx;
    animation: success 1s ease-out;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    
    &:hover {
      transform: scale(1.1);
      filter: drop-shadow(0 4rpx 8rpx rgba(0, 184, 148, 0.3));
    }
  }
  
  .success-title {
    font-size: 44rpx;
  font-weight: bold;
    color: #333333;
    margin-bottom: 20rpx;
    transition: all 0.3s ease;
  }
  
  .success-desc {
  font-size: 28rpx;
    color: #666666;
    line-height: 1.5;
    margin-bottom: 40rpx;
    transition: all 0.3s ease;
  }
  
  .appointment-info {
    background: rgba(116, 185, 255, 0.1);
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 40rpx;
    transition: all 0.3s ease;
    
    &:hover {
      background: rgba(116, 185, 255, 0.15);
      transform: translateY(-2rpx);
    }
    
    .info-item {
  display: flex;
      justify-content: space-between;
  align-items: center;
      margin-bottom: 20rpx;
      transition: all 0.3s ease;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &:hover {
        transform: translateX(5rpx);
}
      
      .info-label {
        font-size: 28rpx;
        color: #666666;
        transition: all 0.3s ease;
      }
      
      .info-value {
        font-size: 28rpx;
        color: #333333;
        font-weight: bold;
        transition: all 0.3s ease;
        
        &:hover {
          color: #0984e3;
}
      }
    }
  }
}

.action-buttons {
  display: flex;
  gap: 20rpx;
  margin-bottom: 40rpx;
  animation: slideInUp 0.8s ease-out 0.5s both;
  opacity: 0;
  transform: translateY(30rpx);
  
  .action-btn {
  flex: 1;
    height: 100rpx;
    border-radius: 50rpx;
    font-size: 28rpx;
    font-weight: bold;
  border: none;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    cursor: pointer;
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
      transition: left 0.6s ease;
    }
    
    &.primary-btn {
      background: linear-gradient(135deg, #74b9ff, #0984e3);
      color: #ffffff;
      box-shadow: 0 8rpx 24rpx rgba(116, 185, 255, 0.3);
      
      &:hover {
        transform: translateY(-8rpx) scale(1.02);
        box-shadow: 0 12rpx 32rpx rgba(116, 185, 255, 0.5);
        background: linear-gradient(135deg, #0984e3, #74b9ff);
        
        &::before {
          left: 100%;
        }
      }
      
      &:active {
        transform: translateY(-2rpx) scale(0.98);
}
    }
    
    &.secondary-btn {
      background: rgba(255, 255, 255, 0.9);
      color: #74b9ff;
      border: 2rpx solid #74b9ff;
      
      &:hover {
        background: rgba(116, 185, 255, 0.1);
        transform: translateY(-6rpx) scale(1.02);
        box-shadow: 0 8rpx 24rpx rgba(116, 185, 255, 0.2);
        
        &::before {
          left: 100%;
        }
      }
      
      &:active {
        transform: translateY(-1rpx);
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

/* New styles for appointment-flow.vue */
.page-header {
  text-align: center;
  padding: 80rpx 40rpx 40rpx;
  color: #ffffff;
  position: relative;
  z-index: 10;
  animation: slideInDown 0.8s ease-out;

  .header-icon {
    font-size: 100rpx;
    margin-bottom: 20rpx;
    animation: bounce 2s infinite;
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.1);
    }
  }

  .header-title {
    font-size: 56rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
    text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;
  }

  .header-desc {
  font-size: 28rpx;
    color: rgba(255, 255, 255, 0.9);
    text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;
  }
}

.step-panel {
  padding: 0 40rpx;
  margin-top: 40rpx;
  position: relative;
  z-index: 10;
  animation: slideInUp 0.8s ease-out 0.2s both;
  opacity: 0;
  transform: translateY(30rpx);
}

.step-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24rpx;
  padding: 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
    transition: left 0.6s ease;
  }

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(116, 185, 255, 0.1), rgba(9, 132, 227, 0.1));
    opacity: 0;
    transition: opacity 0.3s ease;
    pointer-events: none;
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

  &:hover {
    transform: translateY(-8rpx) scale(1.02);
    box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.2);
    background: rgba(255, 255, 255, 0.98);
    
    &::before {
      left: 100%;
    }
    
    &::after {
      opacity: 1;
    }
  }

  .card-header {
    display: flex;
    align-items: center;
  margin-bottom: 30rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    .card-icon {
      font-size: 60rpx;
      margin-right: 20rpx;
      color: #0984e3;
      transition: all 0.3s ease;
      
      &:hover {
        transform: scale(1.1);
      }
    }

    .card-title {
      font-size: 36rpx;
  font-weight: bold;
      color: #333333;
      transition: all 0.3s ease;
      
      &:hover {
        color: #0984e3;
      }
    }

    .card-desc {
      font-size: 24rpx;
      color: #666666;
      margin-top: 5rpx;
      transition: all 0.3s ease;
    }
}

  .hospital-selection, .package-selection {
  display: flex;
    flex-direction: column;
    gap: 20rpx;
    margin-bottom: 30rpx;
  }

  .hospital-item, .package-item {
    display: flex;
  align-items: center;
    justify-content: space-between;
    padding: 25rpx 20rpx;
    border-radius: 16rpx;
    background: rgba(255, 255, 255, 0.8);
    border: 1rpx solid rgba(0, 0, 0, 0.05);
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    position: relative;
    overflow: hidden;
    cursor: pointer;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(116, 185, 255, 0.2), transparent);
      transition: left 0.6s ease;
}

    &:hover {
      background: rgba(255, 255, 255, 0.95);
      transform: translateY(-4rpx) scale(1.02);
      box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
      
      &::before {
        left: 100%;
      }
    }

    &.active {
      background: rgba(116, 185, 255, 0.1);
      border: 2rpx solid #0984e3;
      box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.2);
      transform: translateY(-2rpx);
    }

    .hospital-info, .package-header {
  flex: 1;
      display: flex;
      flex-direction: column;
      transition: all 0.3s ease;
    }

    .hospital-name, .package-name {
      font-size: 32rpx;
      font-weight: bold;
      color: #333333;
      margin-bottom: 8rpx;
      transition: all 0.3s ease;
      
      &:hover {
        color: #0984e3;
      }
    }

    .hospital-address, .package-desc {
      font-size: 24rpx;
      color: #666666;
  line-height: 1.4;
      transition: all 0.3s ease;
}

    .package-price {
      display: flex;
      align-items: baseline;
      margin-top: 10rpx;
      transition: all 0.3s ease;

      .price-symbol {
        font-size: 28rpx;
  color: #ff5a5f;
  font-weight: bold;
        transition: all 0.3s ease;
}

      .price-value {
        font-size: 36rpx;
  font-weight: bold;
        color: #ff5a5f;
        margin: 0 5rpx;
        transition: all 0.3s ease;
        position: relative;
        
        &::after {
          content: '';
          position: absolute;
          bottom: -2rpx;
          left: 0;
          width: 0;
          height: 2rpx;
          background: linear-gradient(90deg, #ff5a5f, #ff6b6b);
          transition: width 0.3s ease;
        }
        
        &:hover::after {
          width: 100%;
        }
}

      .price-original {
        font-size: 24rpx;
        color: #999999;
        text-decoration: line-through;
        transition: all 0.3s ease;
      }
    }

    .package-tags {
      display: flex;
      gap: 10rpx;
      margin-top: 10rpx;

      .package-tag {
        font-size: 22rpx;
        color: #0984e3;
        background: rgba(9, 132, 227, 0.1);
        padding: 6rpx 12rpx;
        border-radius: 8rpx;
        transition: all 0.3s ease;
        
        &:hover {
          transform: scale(1.1);
          background: rgba(9, 132, 227, 0.2);
        }
      }
    }

    .package-check, .hospital-check {
      font-size: 40rpx;
      color: #0984e3;
      opacity: 0.7;
      transition: all 0.3s ease;
      
      &:hover {
        transform: scale(1.2);
        opacity: 1;
      }
    }
  }

  .next-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 90rpx;
    background: linear-gradient(135deg, #74b9ff, #0984e3);
    color: #ffffff;
    font-size: 32rpx;
  font-weight: bold;
    border-radius: 45rpx;
    border: none;
    box-shadow: 0 8rpx 24rpx rgba(116, 185, 255, 0.3);
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    cursor: pointer;
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
      transition: left 0.6s ease;
    }

    &:hover {
      transform: translateY(-6rpx) scale(1.02);
      box-shadow: 0 12rpx 32rpx rgba(116, 185, 255, 0.5);
      background: linear-gradient(135deg, #0984e3, #74b9ff);
      
      &::before {
        left: 100%;
      }
    }

    &:active {
      transform: translateY(-2rpx) scale(0.98);
    }

    &:disabled {
      background: #ccc;
      color: #888;
      cursor: not-allowed;
      box-shadow: none;
      
      &:hover {
        transform: none;
        box-shadow: none;
      }
    }

    .btn-icon {
      font-size: 40rpx;
      margin-right: 10rpx;
      transition: all 0.3s ease;
    }
  }
}

.step-indicator {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-top: 40rpx;
  padding: 0 40rpx;
  position: relative;
  z-index: 10;
  animation: slideInDown 0.8s ease-out 0.1s both;
  opacity: 0;
  transform: translateY(-20rpx);

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    width: 100%;
    height: 1rpx;
    background: rgba(255, 255, 255, 0.3);
    z-index: -1;
}

  .step {
    display: flex;
    align-items: center;
    position: relative;

    &.active {
      .step-circle {
        background: linear-gradient(135deg, #0984e3, #74b9ff);
        border: 1rpx solid #0984e3;
        box-shadow: 0 4rpx 12rpx rgba(9, 132, 227, 0.2);
      }
      .step-number {
        color: #ffffff;
      }
      .step-check {
        color: #ffffff;
      }
      .step-label {
        color: #ffffff;
  font-weight: bold;
      }
      .step-arrow {
        color: #ffffff;
}
    }

    &.completed {
      .step-circle {
        background: #0984e3;
        border: 1rpx solid #0984e3;
        box-shadow: 0 4rpx 12rpx rgba(9, 132, 227, 0.2);
      }
      .step-number {
        color: #ffffff;
      }
      .step-check {
        color: #ffffff;
      }
      .step-label {
        color: #ffffff;
  font-weight: bold;
}
      .step-arrow {
        color: #ffffff;
      }
    }

    .step-circle {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
      justify-content: center;
      border: 2rpx solid rgba(255, 255, 255, 0.3);
      transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
      position: relative;
      z-index: 1;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
      backdrop-filter: blur(10rpx);

      &:hover {
        transform: scale(1.1);
        box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.15);
      }
    }

    .step-number {
      font-size: 32rpx;
      font-weight: bold;
      color: #333333;
      transition: all 0.3s ease;
    }

    .step-check {
      font-size: 36rpx;
      color: #0984e3;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }

    .step-label {
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.9);
  margin-top: 10rpx;
      text-align: center;
      text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.3);
      transition: all 0.3s ease;
      font-weight: 500;
    }

    .step-arrow {
      font-size: 40rpx;
      color: rgba(255, 255, 255, 0.8);
      opacity: 0.7;
      margin: 0 20rpx;
      transition: all 0.3s ease;
      text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.3);
      
      &:hover {
        opacity: 1;
        transform: scale(1.1);
}
    }
  }
}

.form-section {
  margin-top: 30rpx;
  padding-top: 20rpx;
  border-top: 1rpx dashed rgba(0, 0, 0, 0.1);
  animation: slideInUp 0.8s ease-out 0.3s both;
  opacity: 0;
  transform: translateY(30rpx);

  .form-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 25rpx;
    position: relative;
    width: 100%;

    &:last-child {
      margin-bottom: 0;
    }

    .form-label {
  font-size: 28rpx;
      color: #333333;
      font-weight: bold;
      min-width: 120rpx;
      margin-right: 20rpx;
      flex-shrink: 0;
    }

    .form-input, .form-textarea {
      flex: 1;
      height: 80rpx;
      padding: 0 20rpx;
      font-size: 28rpx;
      color: #333333;
      border: 1rpx solid #e0e0e0;
      border-radius: 12rpx;
      background: rgba(255, 255, 255, 0.9);
      transition: all 0.3s ease;
      width: 100%;
      box-sizing: border-box;
      word-wrap: break-word;
      overflow-wrap: break-word;
      text-overflow: ellipsis;
      white-space: nowrap;

      &:focus {
        border-color: #0984e3;
        box-shadow: 0 0 15rpx rgba(9, 132, 227, 0.3);
        background: #ffffff;
        transform: translateY(-2rpx);
      }
    }

    .form-textarea {
      height: 150rpx;
      padding-top: 15rpx;
      resize: none;
      white-space: pre-wrap;
      word-break: break-word;
      overflow-y: auto;
    }

    .picker-display {
      display: flex;
      align-items: center;
      height: 80rpx;
      padding: 0 20rpx;
      font-size: 28rpx;
      color: #333333;
      border: 1rpx solid #e0e0e0;
      border-radius: 12rpx;
      background: rgba(255, 255, 255, 0.9);
      transition: all 0.3s ease;
      width: 100%;
      box-sizing: border-box;
      cursor: pointer;

      &:hover {
        border-color: #0984e3;
        background: rgba(255, 255, 255, 0.95);
        transform: translateY(-2rpx);
      }

      &:focus {
        border-color: #0984e3;
        box-shadow: 0 0 15rpx rgba(9, 132, 227, 0.3);
        background: #ffffff;
        transform: translateY(-2rpx);
      }

      .picker-text {
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        
        &.placeholder {
          color: #999999;
        }
      }

      .picker-icon {
        font-size: 36rpx;
        margin-left: 10rpx;
        flex-shrink: 0;
      }
    }

        .gender-selection {
      display: flex;
      gap: 20rpx;
      margin-top: 10rpx;
    }

    .gender-option {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 80rpx;
      background: rgba(255, 255, 255, 0.8);
      border: 1rpx solid #e0e0e0;
      border-radius: 12rpx;
      font-size: 28rpx;
      color: #333333;
      transition: all 0.3s ease;
      padding: 0 10rpx;
      box-sizing: border-box;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      cursor: pointer;

      &:hover {
        background: rgba(255, 255, 255, 0.95);
        border-color: #0984e3;
        transform: translateY(-2rpx);
      }
      
      &.active {
        background: rgba(116, 185, 255, 0.1);
        border: 2rpx solid #0984e3;
        box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.2);
        color: #0984e3;
        font-weight: bold;
      }
      
      .gender-icon {
        font-size: 36rpx;
        margin-right: 10rpx;
        transition: all 0.3s ease;
      }
      
      .gender-text {
        font-size: 28rpx;
        color: #333333;
        transition: all 0.3s ease;
      }
    }
    

  }
}

.confirm-section {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  margin-top: 30rpx;
  padding-top: 20rpx;
  border-top: 1rpx dashed rgba(0, 0, 0, 0.1);

  .info-card {
    background: #f9f9f9;
    border-radius: 16rpx;
    padding: 25rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;

    &:hover {
      background: #f0f0f0;
      transform: translateY(-2rpx);
    }

    .info-header {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
      padding-bottom: 10rpx;
      border-bottom: 1rpx dashed rgba(0, 0, 0, 0.1);

      .info-icon {
        font-size: 40rpx;
        color: #0984e3;
        margin-right: 10rpx;
      }

      .info-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333333;
      }
    }

    .info-content {
      .info-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10rpx;

        &:last-child {
          margin-bottom: 0;
        }

        .info-label {
  font-size: 28rpx;
          color: #666666;
        }

        .info-value {
          font-size: 28rpx;
          color: #333333;
          font-weight: bold;
        }
      }
    }
  }

  .price-card {
    background: #f9f9f9;
    border: 1rpx solid #e0e0e0;
    border-radius: 16rpx;
    padding: 25rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;

    &:hover {
      background: #f0f0f0;
      transform: translateY(-2rpx);
    }

    .info-row {
      .info-label {
        color: #666666;
      }
      .info-value {
        color: #0984e3;
        font-weight: bold;
      }
      .price-original {
        color: #999999;
      }
      .price-discount {
        color: #0984e3;
        font-weight: bold;
      }
      .total-row {
        .info-label {
          color: #666666;
        }
        .info-value {
          color: #0984e3;
          font-weight: bold;
        }
      }
    }
  }
}

.payment-section {
  margin-top: 30rpx;
  padding-top: 20rpx;
  border-top: 1rpx dashed rgba(0, 0, 0, 0.1);

  .payment-header {
    display: flex;
    align-items: center;
    margin-bottom: 15rpx;
    padding-bottom: 10rpx;
    border-bottom: 1rpx dashed rgba(0, 0, 0, 0.1);

    .payment-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333333;
    }
  }

  .payment-methods {
    display: flex;
    flex-wrap: wrap;
    gap: 15rpx;
    justify-content: space-around;
  }

  .payment-method {
    display: flex;
    align-items: center;
    width: 30%; /* Adjust as needed for 3 columns */
    padding: 15rpx 10rpx;
  background: #f5f5f5;
    border: 1rpx solid #e0e0e0;
    border-radius: 12rpx;
    transition: all 0.3s ease;

    &:hover {
      background: #e0e0e0;
      transform: translateY(-2rpx);
}

    &.active {
      background: #e0f7fa;
      border: 1rpx solid #0984e3;
      box-shadow: 0 4rpx 12rpx rgba(9, 132, 227, 0.2);
}

    .method-icon {
      font-size: 40rpx;
      margin-right: 10rpx;
      color: #0984e3;
    }

    .method-name {
      font-size: 28rpx;
      color: #333333;
  font-weight: bold;
    }

    .method-check {
      font-size: 40rpx;
      color: #0984e3;
      opacity: 0.7;
      margin-left: 10rpx;
    }
  }
}

.pay-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #0984e3, #74b9ff);
  color: #ffffff;
  font-size: 32rpx;
  font-weight: bold;
  border-radius: 45rpx;
  border: none;
  box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-5rpx) scale(1.02);
    box-shadow: 0 12rpx 32rpx rgba(9, 132, 227, 0.5);
    background: linear-gradient(135deg, #74b9ff, #0984e3);
  }

  &:active {
    transform: translateY(-2rpx) scale(0.98);
}

  &:disabled {
    background: #ccc;
    color: #888;
    cursor: not-allowed;
    box-shadow: none;
  }

  .btn-icon {
    font-size: 40rpx;
    margin-right: 10rpx;
  }
}

.pay-modal-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  backdrop-filter: blur(5rpx);
  animation: fadeIn 0.3s ease-out;
}

.pay-modal {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 40rpx;
  width: 80%;
  max-width: 500rpx;
  text-align: center;
  box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.2);
  animation: slideInUp 0.3s ease-out;
  position: relative;

  .modal-header {
  display: flex;
    justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
    padding-bottom: 15rpx;
    border-bottom: 1rpx solid #eee;

    .modal-title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333333;
    }

    .modal-close {
      font-size: 40rpx;
      color: #999999;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        color: #0984e3;
      }
    }
  }

  .modal-content {
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx dashed #eee;

.pay-amount {
      font-size: 36rpx;
  font-weight: bold;
      color: #0984e3;
      margin-bottom: 15rpx;
    }

    .qrcode {
      width: 200rpx;
      height: 200rpx;
      margin: 0 auto 15rpx;
      border-radius: 10rpx;
      overflow: hidden;
      border: 1rpx solid #e0e0e0;
}

.pay-tips {
  font-size: 24rpx;
      color: #666666;
      line-height: 1.5;
    }
  }

  .modal-actions {
    display: flex;
    justify-content: space-around;
    gap: 20rpx;
  }

  .pay-confirm-btn, .pay-cancel-btn {
    flex: 1;
    height: 80rpx;
    border-radius: 40rpx;
    font-size: 32rpx;
    font-weight: bold;
    border: none;
    transition: all 0.3s ease;
    cursor: pointer;

    &:hover {
      transform: translateY(-3rpx);
      box-shadow: 0 6rpx 15rpx rgba(0, 0, 0, 0.1);
    }

    &:active {
      transform: translateY(-1rpx);
}
  }

.pay-confirm-btn {
    background: linear-gradient(135deg, #0984e3, #74b9ff);
    color: #ffffff;
    box-shadow: 0 6rpx 15rpx rgba(9, 132, 227, 0.3);

    &:hover {
      background: linear-gradient(135deg, #74b9ff, #0984e3);
}
  }

.pay-cancel-btn {
    background: #f5f5f5;
    color: #333333;
    border: 1rpx solid #e0e0e0;

    &:hover {
      background: #e0e0e0;
      border-color: #ccc;
    }
  }
}

.success-panel {
  padding: 0 40rpx;
  margin-top: 40rpx;
  position: relative;
  z-index: 10;
  animation: slideInUp 0.8s ease-out 0.4s both;
  opacity: 0;
  transform: translateY(30rpx);
}

.success-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  margin-bottom: 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
  text-align: center;
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
  
  .success-icon {
    font-size: 120rpx;
    color: #00b894;
    margin-bottom: 30rpx;
    animation: success 1s ease-out;
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.1);
      filter: drop-shadow(0 4rpx 8rpx rgba(0, 184, 148, 0.3));
    }
  }
  
  .success-title {
    font-size: 44rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 20rpx;
    transition: all 0.3s ease;
  }
  
  .success-desc {
    font-size: 28rpx;
    color: #666666;
    line-height: 1.5;
    margin-bottom: 40rpx;
    transition: all 0.3s ease;
  }
  
  .success-info {
    display: flex;
    flex-direction: column;
    gap: 15rpx;
    margin-bottom: 30rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx dashed #eee;

    .info-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 28rpx;
      color: #333333;

      .info-label {
        font-weight: bold;
      }

      .info-value {
        font-weight: bold;
        color: #0984e3;
      }
    }
  }

  .back-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 90rpx;
    background: #f5f5f5;
    color: #333333;
    font-size: 32rpx;
    font-weight: bold;
    border-radius: 45rpx;
    border: 1rpx solid #e0e0e0;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;
    cursor: pointer;

    &:hover {
      background: #e0e0e0;
      transform: translateY(-2rpx);
    }

    &:active {
      transform: translateY(-1rpx);
    }

    .btn-icon {
      font-size: 40rpx;
      margin-right: 10rpx;
    }
  }
}

.appointment-flow-content {
  min-height: 100vh;
  background: linear-gradient(135deg, #74b9ff, #0984e3);
  position: relative;
  overflow: hidden;
  width: 100%;
  box-sizing: border-box;
}
</style> 