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
        
        <!-- 检查项选择 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">🔬</text>
            <text>选择检查项</text>
          </view>
          <picker :range="checkitemList" range-key="name" @change="onCheckitemChange">
            <view class="form-picker">
              <text class="picker-text" :class="{ 'placeholder': checkitemList.length === 0 }">
                {{ checkitemList.length > 0 ? '请选择检查项' : '加载中...' }}
              </text>
              <text class="picker-arrow">▼</text>
        </view>
      </picker>
        </view>
        
        <!-- 已选择的检查项 -->
        <view class="form-item" v-if="selectedCheckitems.length > 0">
          <view class="form-label">
            <text class="label-icon">✅</text>
            <text>已选择的检查项</text>
          </view>
          <view class="selected-items">
            <view 
              class="selected-item" 
              v-for="(item, index) in selectedCheckitems" 
              :key="item.id"
            >
              <view class="item-info">
                <text class="item-name">{{ item.name }}</text>
                <text class="item-price">¥{{ item.price || 0 }}</text>
              </view>
              <text class="remove-btn" @click="removeCheckitem(index)">×</text>
            </view>
            <view class="total-price">
              <text class="total-label">总价：</text>
              <text class="total-value">¥{{ totalPrice }}</text>
            </view>
          </view>
        </view>
        
        <!-- 定制套餐按钮 -->
        <!-- <view class="form-item" v-if="selectedCheckitems.length > 0">
          <button class="custom-package-btn" @click="showCustomPackageModal">
            <text class="btn-icon">🎯</text>
            <text>创建定制套餐</text>
          </button>
        </view> -->
        
        <!-- 姓名 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">👤</text>
            <text>患者姓名</text>
          </view>
          <input 
            class="form-input" 
            v-model="patientName" 
            placeholder="请输入患者姓名"
            placeholder-class="input-placeholder"
          />
        </view>
        
        <!-- 年龄 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">🎂</text>
            <text>患者年龄</text>
          </view>
          <input 
            class="form-input" 
            v-model="patientAge" 
            placeholder="请输入患者年龄"
            placeholder-class="input-placeholder"
            type="number" 
            maxlength="3" 
          />
        </view>
        
        <!-- 性别 -->
        <view class="form-item">
          <view class="form-label">
            <text class="label-icon">👥</text>
            <text>患者性别</text>
          </view>
          <picker :range="genderOptions" @change="onGenderChange">
            <view class="form-picker">
              <text class="picker-text" :class="{ 'placeholder': !patientGender }">
                {{ patientGender || '请选择性别' }}
              </text>
              <text class="picker-arrow">▼</text>
        </view>
      </picker>
        </view>
        
        <!-- 手机号 -->
      <view class="form-item">
          <view class="form-label">
            <text class="label-icon">📱</text>
            <text>患者手机号</text>
      </view>
          <input 
            class="form-input" 
            v-model="patientPhone" 
            placeholder="请输入患者手机号"
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
          <text>点击支付</text>
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
    
    <!-- 定制套餐模态框 -->
    <!-- <view class="modal-overlay" v-if="showCustomPackage" @click="hideCustomPackageModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">创建定制套餐</text>
          <text class="modal-close" @click="hideCustomPackageModal">×</text>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">套餐名称</text>
            <input 
              class="form-input" 
              v-model="customPackageName" 
              placeholder="请输入套餐名称"
              maxlength="50"
            />
          </view>
          <view class="form-item">
            <text class="form-label">套餐描述</text>
            <textarea 
              class="form-textarea" 
              v-model="customPackageDescription" 
              placeholder="请输入套餐描述（可选）"
              maxlength="200"
            />
          </view>
          <view class="selected-items-summary">
            <text class="summary-title">已选择的检查项：</text>
            <view class="summary-list">
              <text 
                class="summary-item" 
                v-for="item in selectedCheckitems" 
                :key="item.id"
              >
                {{ item.name }}
              </text>
            </view>
            <view class="summary-total">
              <text class="total-label">总价：</text>
              <text class="total-value">¥{{ totalPrice }}</text>
            </view>
          </view>
        </view>
        <view class="modal-footer">
          <button class="modal-btn cancel-btn" @click="hideCustomPackageModal">取消</button>
          <button class="modal-btn confirm-btn" @click="createCustomPackage">创建套餐</button>
        </view>
      </view>
    </view> -->
    
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
import { checkitemApi, appointmentApi, hospitalApi } from '@/utils/api.js';
import { get, post } from '@/utils/request.js';

export default {
  data() {
    return {
      hospitalList: [], // 从后端获取的医院列表
      checkitemList: [], // 从后端获取的检查项列表
      selectedHospital: null,
      selectedDate: '',
      selectedCheckitems: [], // 选中的检查项列表
      patientName: '',
      patientAge: '',
      patientGender: '',
      patientPhone: '',
      remark: '',
      isLoading: false,
      totalPrice: 0, // 总价格
      genderOptions: ['男', '女'],
      // showCustomPackage: false, // 是否显示定制套餐界面
      // customPackageName: '', // 定制套餐名称
      // customPackageDescription: '' // 定制套餐描述
    }
  },
  computed: {
    isFormValid() {
      return this.selectedHospital && 
             this.selectedDate && 
             this.selectedCheckitems.length > 0 && 
             this.patientName.trim() && 
             this.patientAge && 
             this.patientGender && 
             this.patientPhone.trim() &&
             this.patientPhone.length === 11;
    }
  },
  onLoad() {
    this.getHospitalList();
    this.getCheckitemList();
  },
  onShow() {
    // 检查是否有已选择的医院信息
    this.checkSelectedHospital();
  },
  methods: {
    // 检查已选择的医院信息
    checkSelectedHospital() {
      const selectedHospital = uni.getStorageSync('selectedHospital');
      
      if (selectedHospital) {
        try {
          const hospitalInfo = JSON.parse(selectedHospital);
          console.log('发现已选择的医院信息:', hospitalInfo);
          
          // 确保医院ID是数字类型
          if (hospitalInfo.id) {
            hospitalInfo.id = parseInt(hospitalInfo.id);
          }
          
          // 更新选择的医院信息
          this.selectedHospital = hospitalInfo;
        } catch (e) {
          console.error('解析已选择的医院信息失败:', e);
        }
      } else {
        console.log('没有发现已选择的医院信息');
      }
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
    },
    
    // 获取检查项列表
    async getCheckitemList() {
      try {
        uni.showLoading({ title: '加载检查项列表...' });
        
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
        
        // 使用后端AppCheckitemController的接口
        const result = await post(checkitemApi.getCheckitemList, {
          pageNum: 1,
          pageSize: 100
        });
        
        console.log('检查项列表API响应:', result);
        
        if (result && result.data) {
          // 检查是否是分页数据结构
          if (result.data.records && Array.isArray(result.data.records)) {
            this.checkitemList = result.data.records;
          } else if (result.data.list && Array.isArray(result.data.list)) {
            this.checkitemList = result.data.list;
          } else if (Array.isArray(result.data)) {
            this.checkitemList = result.data;
          } else {
            // 如果不是数组，尝试获取data中的其他可能字段
            console.log('检查项数据详情:', result.data);
            this.checkitemList = [];
          }
          console.log('获取检查项列表成功:', this.checkitemList);
        } else {
          console.error('获取检查项列表失败:', result);
          // 使用测试数据
          this.checkitemList = [
            { id: 7001, name: '血常规检查', price: 35.00, description: '检测血液中各种细胞成分及数量，包括白细胞、红细胞、血小板等' },
            { id: 7002, name: '尿常规检查', price: 25.00, description: '检测尿液中的各项生化指标，包括蛋白质、糖分、酮体等' },
            { id: 7003, name: '肝功能检查', price: 55.00, description: '检测肝脏功能相关指标，包括转氨酶、胆红素、蛋白质等' },
            { id: 7004, name: '肾功能检查', price: 55.00, description: '检测肾脏功能相关指标，包括肌酐、尿素氮、尿酸等' },
            { id: 7005, name: '心电图检查', price: 45.00, description: '检测心脏电活动情况，评估心脏功能和心律' },
            { id: 7006, name: '胸部X光检查', price: 65.00, description: '检测胸部器官情况，包括肺部、心脏、纵隔等' },
            { id: 7007, name: '腹部B超检查', price: 85.00, description: '检测腹部器官情况，包括肝脏、胆囊、胰腺、肾脏等' },
            { id: 7008, name: '血脂检查', price: 45.00, description: '检测血液中脂质含量，包括胆固醇、甘油三酯等' },
            { id: 7009, name: '血糖检查', price: 30.00, description: '检测血液中葡萄糖含量，评估糖代谢情况' },
            { id: 7010, name: '甲状腺功能检查', price: 75.00, description: '检测甲状腺激素水平，评估甲状腺功能状态' }
          ];
          uni.showToast({
            title: '使用测试检查项数据',
            icon: 'none',
            duration: 2000
          });
        }
      } catch (error) {
        console.error('获取检查项列表异常:', error);
        // 使用测试数据
        this.checkitemList = [
          { id: 7001, name: '血常规检查', price: 35.00, description: '检测血液中各种细胞成分及数量，包括白细胞、红细胞、血小板等' },
          { id: 7002, name: '尿常规检查', price: 25.00, description: '检测尿液中的各项生化指标，包括蛋白质、糖分、酮体等' },
          { id: 7003, name: '肝功能检查', price: 55.00, description: '检测肝脏功能相关指标，包括转氨酶、胆红素、蛋白质等' },
          { id: 7004, name: '肾功能检查', price: 55.00, description: '检测肾脏功能相关指标，包括肌酐、尿素氮、尿酸等' },
          { id: 7005, name: '心电图检查', price: 45.00, description: '检测心脏电活动情况，评估心脏功能和心律' },
          { id: 7006, name: '胸部X光检查', price: 65.00, description: '检测胸部器官情况，包括肺部、心脏、纵隔等' },
          { id: 7007, name: '腹部B超检查', price: 85.00, description: '检测腹部器官情况，包括肝脏、胆囊、胰腺、肾脏等' },
          { id: 7008, name: '血脂检查', price: 45.00, description: '检测血液中脂质含量，包括胆固醇、甘油三酯等' },
          { id: 7009, name: '血糖检查', price: 30.00, description: '检测血液中葡萄糖含量，评估糖代谢情况' },
          { id: 7010, name: '甲状腺功能检查', price: 75.00, description: '检测甲状腺激素水平，评估甲状腺功能状态' }
        ];
        uni.showToast({
          title: '使用测试检查项数据',
          icon: 'none',
          duration: 2000
        });
      } finally {
        uni.hideLoading();
      }
    },
    
    onHospitalChange(e) {
      this.selectedHospital = this.hospitalList[e.detail.value];
    },
    onDateChange(e) {
      this.selectedDate = e.detail.value;
    },
    // 选择检查项
    onCheckitemChange(e) {
      const index = e.detail.value;
      if (index >= 0 && index < this.checkitemList.length) {
        const checkitem = this.checkitemList[index];
        // 检查是否已经选中
        const existingIndex = this.selectedCheckitems.findIndex(item => item.id === checkitem.id);
        if (existingIndex === -1) {
          this.selectedCheckitems.push(checkitem);
          this.calculateTotalPrice();
        } else {
          uni.showToast({
            title: '该检查项已选择',
            icon: 'none',
            duration: 1500
          });
        }
      }
    },
    // 移除选中的检查项
    removeCheckitem(index) {
      this.selectedCheckitems.splice(index, 1);
      this.calculateTotalPrice();
    },
    
    // 性别选择
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
    
    // 计算总价格
    calculateTotalPrice() {
      this.totalPrice = this.selectedCheckitems.reduce((total, item) => {
        return total + (item.price || 0);
      }, 0);
    },
    // 显示定制套餐界面
    // showCustomPackageModal() {
    //   this.showCustomPackage = true;
    // },
    // 隐藏定制套餐界面
    // hideCustomPackageModal() {
    //   this.showCustomPackage = false;
    // },
    // 创建定制套餐
    // createCustomPackage() {
    //   if (!this.customPackageName.trim()) {
    //     uni.showToast({
    //       title: '请输入套餐名称',
    //       icon: 'none',
    //       duration: 2000
    //     });
    //     return;
    //   }
    //   
    //   if (this.selectedCheckitems.length === 0) {
    //     uni.showToast({
    //       title: '请至少选择一个检查项',
    //       icon: 'none',
    //       duration: 2000
    //     });
    //     return;
    //   }
    //   
    //   // 创建定制套餐
    //   const customPackage = {
    //     id: Date.now(), // 临时ID
    //     name: this.customPackageName,
    //     description: this.customPackageDescription,
    //     price: this.totalPrice,
    //     checkItems: this.selectedCheckitems.map(item => item.name),
    //     checkitemIds: this.selectedCheckitems.map(item => item.id).join(',')
    //   };
    //   
    //   // 存储定制套餐信息
    //   uni.setStorageSync('customPackage', customPackage);
    //   
    //   this.hideCustomPackageModal();
    //   this.customPackageName = '';
    //   this.customPackageDescription = '';
    //   
    //   uni.showToast({
    //     title: '定制套餐创建成功',
    //     icon: 'success',
    //     duration: 2000
    //   });
    // },
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
      if (!phoneRegex.test(this.patientPhone)) {
        uni.showToast({ 
          title: '请输入正确的手机号', 
          icon: 'none',
          duration: 2000
        });
        return;
      }
      
      // 检查是否已选择医院
      if (!this.selectedHospital) {
        uni.showModal({
          title: '提示',
          content: '请先选择体检医院',
          confirmText: '去选择',
          cancelText: '取消',
          success: (res) => {
            if (res.confirm) {
              // 跳转到医院选择页面
              uni.navigateTo({
                url: '/pages/hospital/hospital'
              });
            }
          }
        });
        return;
      }
      
      this.isLoading = true;
      
      try {
        // 检查是否有token
        const token = uni.getStorageSync('TOKEN_KEY');
        if (!token) {
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
        
        // 构建预约数据
        const appointmentData = {
          setmealId: null, // 普通项目预约，套餐ID为空
          hospitalId: this.selectedHospital.id,
          doctorId: 3001, // 默认医生ID
          familyMemberId: 1, // 默认家庭成员ID
          appointmentDate: this.selectedDate,
          appointmentTime: '上午(08:00-12:00)', // 默认时间段
          checkitemIds: this.selectedCheckitems.map(item => item.id).join(','),
          patientName: this.patientName,
          patientAge: parseInt(this.patientAge),
          patientGender: this.convertGenderToNumber(this.patientGender),
          patientPhone: this.patientPhone,
          remark: this.remark || ''
        };
        
        console.log('提交的预约数据:', appointmentData);
        
        // 存储定制套餐信息，供payment页面使用
        const customPackage = {
          id: null, // 普通项目预约没有套餐ID
          name: '定制体检套餐',
          description: '根据您选择的检查项目定制的体检套餐',
          price: this.totalPrice,
          checkitemIds: this.selectedCheckitems.map(item => item.id).join(','),
          hospitalId: this.selectedHospital.id,
          hospitalName: this.selectedHospital.name,
          hospitalAddress: this.selectedHospital.address,
          hospitalImage: this.selectedHospital.image,
          patientName: this.patientName,
          patientAge: parseInt(this.patientAge),
          patientGender: this.convertGenderToNumber(this.patientGender),
          patientPhone: this.patientPhone,
          appointmentDate: this.selectedDate,
          appointmentTime: '上午(08:00-12:00)',
          doctorName: '张医生',
          checkitems: this.selectedCheckitems,
          remark: this.remark,
          totalAmount: this.totalPrice
        };
        
        // 存储定制套餐信息
        uni.setStorageSync('customPackage', JSON.stringify(customPackage));
        
        // 不调用API，只存储数据，让支付页面统一处理
        uni.showToast({ 
          title: '信息已保存，即将跳转支付页面', 
          icon: 'success',
          duration: 1500
        });
        
        // 清除选中的检查项
        this.selectedCheckitems = [];
        this.totalPrice = 0;
        
        // 直接跳转到支付页面
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/appointment/appointment-flow'
          });
        }, 1500);
        
      } catch (error) {
        console.error('预约失败:', error);
        uni.showToast({ 
          title: error.message || '预约失败，请重试', 
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
  padding: 20rpx 40rpx 0 40rpx;
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

/* 已选择检查项样式 */
.selected-items {
  margin-top: 20rpx;
  
  .selected-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: rgba(116, 185, 255, 0.1);
    border-radius: 12rpx;
    padding: 20rpx;
    margin-bottom: 15rpx;
    border: 2rpx solid rgba(116, 185, 255, 0.2);
    transition: all 0.3s ease;
    
    &:hover {
      background: rgba(116, 185, 255, 0.15);
      border-color: rgba(116, 185, 255, 0.3);
      transform: translateX(5rpx);
    }
    
    .item-info {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .item-name {
        font-size: 28rpx;
        color: #333333;
        font-weight: 500;
      }
      
      .item-price {
        font-size: 28rpx;
        color: #0984e3;
        font-weight: bold;
      }
    }
    
    .remove-btn {
      width: 40rpx;
      height: 40rpx;
      background: #ff4757;
      color: #ffffff;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24rpx;
      font-weight: bold;
      margin-left: 20rpx;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        background: #ff3742;
        transform: scale(1.1);
      }
    }
  }
  
  .total-price {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 20rpx 0;
    border-top: 2rpx solid rgba(116, 185, 255, 0.2);
    margin-top: 10rpx;
    
    .total-label {
      font-size: 28rpx;
      color: #666666;
      margin-right: 10rpx;
    }
    
    .total-value {
      font-size: 32rpx;
      color: #0984e3;
      font-weight: bold;
    }
  }
}

/* 定制套餐按钮样式 */
.custom-package-btn {
  width: 100%;
  height: 80rpx;
  background: linear-gradient(135deg, #00b894, #00a085);
  color: #ffffff;
  border: none;
  border-radius: 40rpx;
  font-size: 28rpx;
  font-weight: bold;
  box-shadow: 0 6rpx 20rpx rgba(0, 184, 148, 0.3);
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20rpx;
  
  .btn-icon {
    font-size: 32rpx;
    margin-right: 10rpx;
    transition: all 0.3s ease;
  }
  
  &:hover {
    transform: translateY(-4rpx) scale(1.02);
    box-shadow: 0 10rpx 28rpx rgba(0, 184, 148, 0.5);
    background: linear-gradient(135deg, #00a085, #00b894);
    
    .btn-icon {
      transform: scale(1.1);
    }
  }
  
  &:active {
    transform: translateY(-2rpx) scale(0.98);
  }
}

/* 模态框样式 */
.modal-overlay {
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
  
  .modal-content {
    background: #ffffff;
    border-radius: 24rpx;
    width: 90%;
    max-width: 600rpx;
    max-height: 80vh;
    overflow-y: auto;
    box-shadow: 0 12rpx 48rpx rgba(0, 0, 0, 0.3);
    animation: slideInUp 0.3s ease-out;
    
    .modal-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx 40rpx;
      border-bottom: 2rpx solid #f0f0f0;
      
      .modal-title {
        font-size: 36rpx;
        font-weight: bold;
        color: #333333;
      }
      
      .modal-close {
        width: 50rpx;
        height: 50rpx;
        background: #f0f0f0;
        color: #666666;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 32rpx;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          background: #e0e0e0;
          color: #333333;
        }
      }
    }
    
    .modal-body {
      padding: 40rpx;
      
      .form-item {
        margin-bottom: 30rpx;
        
        .form-label {
          font-size: 28rpx;
          color: #333333;
          margin-bottom: 15rpx;
          font-weight: 500;
        }
        
        .form-input {
          width: 100%;
          height: 80rpx;
          border: 2rpx solid #e0e0e0;
          border-radius: 12rpx;
  padding: 0 20rpx;
          font-size: 28rpx;
          color: #333333;
          transition: all 0.3s ease;
          
          &:focus {
            border-color: #74b9ff;
            box-shadow: 0 0 0 4rpx rgba(116, 185, 255, 0.1);
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
          resize: none;
          transition: all 0.3s ease;
          
          &:focus {
            border-color: #74b9ff;
            box-shadow: 0 0 0 4rpx rgba(116, 185, 255, 0.1);
          }
        }
      }
      
      .selected-items-summary {
        background: rgba(116, 185, 255, 0.05);
        border-radius: 12rpx;
        padding: 30rpx;
        margin-top: 30rpx;
        
        .summary-title {
          font-size: 28rpx;
          color: #333333;
          font-weight: 500;
          margin-bottom: 20rpx;
        }
        
        .summary-list {
          display: flex;
          flex-wrap: wrap;
          gap: 10rpx;
          margin-bottom: 20rpx;
          
          .summary-item {
            background: rgba(116, 185, 255, 0.1);
            color: #0984e3;
            padding: 8rpx 16rpx;
            border-radius: 20rpx;
            font-size: 24rpx;
            border: 2rpx solid rgba(116, 185, 255, 0.2);
          }
        }
        
        .summary-total {
          display: flex;
          align-items: center;
          justify-content: flex-end;
          padding-top: 20rpx;
          border-top: 2rpx solid rgba(116, 185, 255, 0.2);
          
          .total-label {
            font-size: 28rpx;
            color: #666666;
            margin-right: 10rpx;
          }
          
          .total-value {
            font-size: 32rpx;
            color: #0984e3;
            font-weight: bold;
          }
        }
      }
    }
    
    .modal-footer {
      display: flex;
      gap: 20rpx;
      padding: 30rpx 40rpx;
      border-top: 2rpx solid #f0f0f0;
      
      .modal-btn {
        flex: 1;
        height: 80rpx;
  border: none;
        border-radius: 40rpx;
        font-size: 28rpx;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &.cancel-btn {
          background: #f0f0f0;
          color: #666666;
          
          &:hover {
            background: #e0e0e0;
            color: #333333;
          }
        }
        
        &.confirm-btn {
          background: linear-gradient(135deg, #00b894, #00a085);
          color: #ffffff;
          box-shadow: 0 6rpx 20rpx rgba(0, 184, 148, 0.3);
          
          &:hover {
            transform: translateY(-2rpx);
            box-shadow: 0 8rpx 24rpx rgba(0, 184, 148, 0.5);
          }
        }
      }
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