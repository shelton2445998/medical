<template>
  <view class="consult-content">
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
        <view class="header-icon">👨‍⚕️</view>
        <view class="header-title">在线专家咨询</view>
        <view class="header-desc">选择专业医生进行在线咨询</view>
      </view>
      
      <!-- 搜索框 -->
      <view class="search-section">
        <view class="search-box">
          <text class="search-icon">🔍</text>
          <input 
            class="search-input" 
            v-model="searchKeyword" 
            placeholder="搜索医生姓名或科室"
            placeholder-class="search-placeholder"
            @input="onSearchInput"
          />
          <text class="search-clear" v-if="searchKeyword" @click="clearSearch">✕</text>
        </view>
      </view>
      
      <!-- 科室分类标签 -->
      <view class="department-tabs">
        <view 
          class="tab-item" 
          v-for="(item, index) in departmentTypes" 
          :key="index"
          :class="{active: currentDepartment === item.id}"
          @click="switchDepartment(item.id)"
        >
          <text class="tab-icon">{{item.icon}}</text>
          <text class="tab-text">{{item.name}}</text>
        </view>
      </view>
      
      <!-- 专家列表 -->
      <view class="expert-list">
        <view 
          class="expert-item" 
          v-for="(expert, index) in filteredExperts" 
          :key="expert.id" 
          @click="consultExpert(expert)"
          :style="{ animationDelay: index * 0.1 + 's' }"
        >
          <!-- 在线状态标签 -->
          <view class="online-badge" v-if="expert.online">
            <text class="badge-icon">🟢</text>
            <text class="badge-text">在线</text>
          </view>
          
          <!-- 专家头部 -->
          <view class="expert-header">
            <view class="expert-avatar-section">
              <image class="expert-avatar" :src="expert.avatar" mode="aspectFill" />
              <view class="expert-status" :class="{online: expert.online}"></view>
            </view>
            <view class="expert-info">
              <view class="expert-name">{{ expert.name }}</view>
              <view class="expert-title">{{ expert.title }}</view>
              <view class="expert-dept">{{ expert.department }}</view>
            </view>
            <view class="expert-rating">
              <text class="rating-stars">⭐⭐⭐⭐⭐</text>
              <text class="rating-score">4.9</text>
            </view>
          </view>
          
          <!-- 专家描述 -->
          <view class="expert-desc">
            <text>{{ expert.description }}</text>
          </view>
          
          <!-- 专家统计 -->
          <view class="expert-stats">
            <view class="stat-item">
              <text class="stat-icon">👥</text>
              <text class="stat-text">咨询 {{ expert.consultCount }} 次</text>
            </view>
            <view class="stat-item">
              <text class="stat-icon">⭐</text>
              <text class="stat-text">好评率 {{ expert.satisfaction }}%</text>
            </view>
            <view class="stat-item">
              <text class="stat-icon">⏰</text>
              <text class="stat-text">响应时间 {{ expert.responseTime }}</text>
            </view>
          </view>
          
          <!-- 专家底部 -->
          <view class="expert-footer">
            <view class="expert-tags">
              <text class="expert-tag" v-for="(tag, tagIndex) in expert.tags" :key="tagIndex">{{ tag }}</text>
            </view>
                         <view class="expert-actions">
               <button class="consult-btn" @click.stop="consultExpert(expert)">
                 <text class="btn-icon">💬</text>
                 <text class="btn-text">咨询</text>
               </button>
             </view>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="filteredExperts.length === 0">
          <view class="empty-icon">👨‍⚕️</view>
          <view class="empty-title">暂无相关专家</view>
          <view class="empty-desc">试试调整筛选条件或搜索关键词</view>
          <button class="reset-btn" @click="resetFilters">
            <text class="btn-icon">🔄</text>
            <text class="btn-text">重置筛选</text>
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentDepartment: 0, // 0表示全部
      departmentTypes: [
        { id: 0, name: '全部', icon: '🏥' },
        { id: 1, name: '内科', icon: '🫀' },
        { id: 2, name: '外科', icon: '🔪' },
        { id: 3, name: '专科', icon: '🎯' }
      ],
      searchKeyword: '',
      experts: [
        {
          id: 1,
          name: '王医生',
          title: '主任医师',
          department: '心内科',
          avatar: '/static/images/avatar1.jpg',
          description: '从事心血管疾病诊疗20余年，擅长冠心病、高血压、心律失常等疾病的诊断和治疗。',
          online: true,
          consultCount: 1250,
          satisfaction: 98,
          responseTime: '5分钟',
          tags: ['冠心病', '高血压', '心律失常']
        },
        {
          id: 2,
          name: '李医生',
          title: '副主任医师',
          department: '肿瘤科',
          avatar: '/static/images/avatar2.jpg',
          description: '专注于肿瘤早期筛查和综合治疗，在肺癌、乳腺癌等常见肿瘤诊疗方面经验丰富。',
          online: false,
          consultCount: 890,
          satisfaction: 96,
          responseTime: '10分钟',
          tags: ['肺癌', '乳腺癌', '早期筛查']
        },
        {
          id: 3,
          name: '赵医生',
          title: '主治医师',
          department: '内分泌科',
          avatar: '/static/images/avatar3.jpg',
          description: '擅长糖尿病、甲状腺疾病等内分泌代谢性疾病的诊断和治疗，注重个体化治疗方案。',
          online: true,
          consultCount: 680,
          satisfaction: 97,
          responseTime: '8分钟',
          tags: ['糖尿病', '甲状腺', '代谢疾病']
        },
        {
          id: 4,
          name: '钱医生',
          title: '副主任医师',
          department: '老年医学科',
          avatar: '/static/images/avatar4.jpg',
          description: '专注于老年常见疾病的综合诊疗，在老年慢性病管理和健康指导方面有丰富经验。',
          online: true,
          consultCount: 420,
          satisfaction: 99,
          responseTime: '6分钟',
          tags: ['老年病', '慢性病', '健康指导']
        }
      ],
      filteredExperts: []
    }
  },
  onLoad() {
    this.filteredExperts = this.experts;
  },
  methods: {
    // 切换科室
    switchDepartment(deptId) {
      this.currentDepartment = deptId;
      this.applyFilters();
    },
    // 搜索输入
    onSearchInput() {
      this.applyFilters();
    },
    // 清除搜索
    clearSearch() {
      this.searchKeyword = '';
      this.applyFilters();
    },
    // 应用筛选
    applyFilters() {
      let filtered = this.experts;
      
      // 按科室筛选
      if (this.currentDepartment !== 0) {
        const deptMap = { 1: '内科', 2: '外科', 3: '专科' };
        filtered = filtered.filter(expert => expert.department.includes(deptMap[this.currentDepartment]));
      }
      
      // 按关键词搜索
      if (this.searchKeyword) {
        const keyword = this.searchKeyword.toLowerCase();
        filtered = filtered.filter(expert => 
          expert.name.toLowerCase().includes(keyword) ||
          expert.department.toLowerCase().includes(keyword) ||
          expert.description.toLowerCase().includes(keyword)
        );
      }
      
      this.filteredExperts = filtered;
    },
    // 重置筛选
    resetFilters() {
      this.currentDepartment = 0;
      this.searchKeyword = '';
      this.filteredExperts = this.experts;
    },
    
    // 咨询专家
    consultExpert(expert) {
      uni.showToast({
        title: `已进入与${expert.name}的咨询`,
        icon: 'success',
        duration: 1000
      });
      setTimeout(() => {
        uni.navigateTo({ url: `/pages/chat/chat?expertId=${expert.id}` });
      }, 1000);
    }
  }
}
</script>

<style scoped>
.consult-content {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  
  &::before {
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
  z-index: -1;
  
  .shape {
    position: absolute;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    animation: float 10s infinite ease-in-out;
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.2);
      background: rgba(255, 255, 255, 0.2);
    }
    
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
  }
  
  .shape-1 {
    width: 200px;
    height: 200px;
    top: 10%;
    left: 10%;
    animation-delay: 0s;
  }
  
  .shape-2 {
    width: 150px;
    height: 150px;
    bottom: 20%;
    right: 20%;
    animation-delay: 1s;
  }
  
  .shape-3 {
    width: 100px;
    height: 100px;
    top: 70%;
    left: 70%;
    animation-delay: 2s;
  }
  
  .shape-4 {
    width: 120px;
    height: 120px;
    bottom: 50%;
    right: 50%;
    animation-delay: 3s;
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

.search-section {
  margin-bottom: 30rpx;
  animation: slideInDown 0.8s ease-out 0.1s both;
  opacity: 0;
  transform: translateY(-20rpx);
  
  .search-box {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 50rpx;
    padding: 20rpx 30rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10rpx);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2rpx);
      box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
    }
    
    .search-icon {
      font-size: 32rpx;
      color: #999999;
      margin-right: 20rpx;
      transition: all 0.3s ease;
    }
    
           .search-input {
         flex: 1;
         font-size: 28rpx;
         color: #333333;
         background: transparent;
         border: none;
         transition: all 0.3s ease;
         
         &:focus {
           color: #0984e3;
         }
       }
    
    .search-clear {
      font-size: 24rpx;
      color: #999999;
      padding: 10rpx;
      border-radius: 50%;
      transition: all 0.3s ease;
      cursor: pointer;
      
      &:hover {
        background: rgba(0, 0, 0, 0.1);
        color: #666666;
      }
    }
  }
}

.department-tabs {
  display: flex;
  background-color: rgba(255, 255, 255, 0.95);
  padding: 20rpx;
  margin-bottom: 30rpx;
  border-radius: 20rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
  animation: slideInDown 0.8s ease-out 0.2s both;
  opacity: 0;
  transform: translateY(-20rpx);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2rpx);
    box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
  }
  
  .tab-item {
    flex: 1;
    height: 80rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: relative;
    transition: all 0.3s ease;
    cursor: pointer;
    border-radius: 12rpx;
    
    .tab-icon {
      font-size: 32rpx;
      margin-bottom: 8rpx;
      transition: all 0.3s ease;
    }
    
    .tab-text {
      font-size: 24rpx;
      color: #666666;
      transition: all 0.3s ease;
    }
    
         &.active {
       background: linear-gradient(135deg, #0984e3, #74b9ff);
       transform: translateY(-2rpx);
       
       .tab-icon, .tab-text {
         color: #ffffff;
       }
     }
     
     &:hover:not(.active) {
       background: rgba(9, 132, 227, 0.1);
       transform: translateY(-1rpx);
     }
  }
}

.expert-list {
  .expert-item {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 20rpx;
    margin-bottom: 30rpx;
    padding: 30rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10rpx);
    position: relative;
    overflow: hidden;
    animation: fadeInUp 0.6s ease-out;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-4rpx);
      box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
    }
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
      transition: left 0.6s ease;
    }
    
    &:hover::before {
      left: 100%;
    }
    
    .online-badge {
      position: absolute;
      top: 20rpx;
      right: 20rpx;
      background: linear-gradient(135deg, #00b894, #00cec9);
      border-radius: 20rpx;
      padding: 8rpx 16rpx;
      display: flex;
      align-items: center;
      animation: bounce 2s infinite;
      transition: all 0.3s ease;
      z-index: 10;
      
      .badge-icon {
        font-size: 20rpx;
        margin-right: 6rpx;
        transition: all 0.3s ease;
      }
      
      .badge-text {
        font-size: 20rpx;
        color: #ffffff;
        font-weight: bold;
        transition: all 0.3s ease;
      }
      
      &:hover {
        transform: scale(1.1);
      }
    }
    
    .expert-header {
      display: flex;
      align-items: center;
      margin-bottom: 25rpx;
      transition: all 0.3s ease;
      
      .expert-avatar-section {
        position: relative;
        margin-right: 24rpx;
        
                 .expert-avatar {
           width: 120rpx;
           height: 120rpx;
           border-radius: 50%;
           border: 4rpx solid rgba(9, 132, 227, 0.2);
           transition: all 0.3s ease;
         }
        
        .expert-status {
          position: absolute;
          bottom: 8rpx;
          right: 8rpx;
          width: 24rpx;
          height: 24rpx;
          border-radius: 50%;
          background: #cccccc;
          border: 3rpx solid #ffffff;
          transition: all 0.3s ease;
          
          &.online {
            background: #00b894;
            animation: pulse 2s infinite;
          }
        }
      }
      
      .expert-info {
        flex: 1;
        
        .expert-name {
          font-size: 32rpx;
          font-weight: bold;
          color: #333333;
          margin-bottom: 8rpx;
          transition: all 0.3s ease;
        }
        
                 .expert-title {
           font-size: 26rpx;
           color: #0984e3;
           margin-bottom: 4rpx;
           transition: all 0.3s ease;
         }
        
        .expert-dept {
          font-size: 24rpx;
          color: #999999;
          transition: all 0.3s ease;
        }
      }
      
      .expert-rating {
        text-align: right;
        
        .rating-stars {
          font-size: 24rpx;
          color: #ffa500;
          display: block;
          margin-bottom: 4rpx;
          transition: all 0.3s ease;
        }
        
        .rating-score {
          font-size: 24rpx;
          color: #ffa500;
          font-weight: bold;
          transition: all 0.3s ease;
        }
      }
    }
    
    .expert-desc {
      font-size: 28rpx;
      color: #666666;
      margin-bottom: 25rpx;
      line-height: 1.6;
      transition: all 0.3s ease;
    }
    
    .expert-stats {
      display: flex;
      justify-content: space-between;
      margin-bottom: 25rpx;
      transition: all 0.3s ease;
      
      .stat-item {
        display: flex;
        align-items: center;
        flex: 1;
        justify-content: center;
        
        .stat-icon {
          font-size: 24rpx;
          margin-right: 8rpx;
          transition: all 0.3s ease;
        }
        
        .stat-text {
          font-size: 24rpx;
          color: #666666;
          transition: all 0.3s ease;
        }
      }
    }
    
    .expert-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      transition: all 0.3s ease;
      
      .expert-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 10rpx;
        flex: 1;
        margin-right: 20rpx;
        
                 .expert-tag {
           font-size: 22rpx;
           color: #0984e3;
           background-color: rgba(9, 132, 227, 0.1);
           padding: 6rpx 12rpx;
           border-radius: 8rpx;
           transition: all 0.3s ease;
         }
      }
      
               .expert-actions {
           display: flex;
           justify-content: flex-end;
           
           .consult-btn {
             font-size: 24rpx;
             color: #ffffff;
             border: none;
             border-radius: 30rpx;
             padding: 0 24rpx;
             height: 60rpx;
             display: flex;
             align-items: center;
             transition: all 0.3s ease;
             cursor: pointer;
             
             .btn-icon {
               font-size: 24rpx;
               margin-right: 6rpx;
               transition: all 0.3s ease;
             }
             
             .btn-text {
               font-size: 24rpx;
               transition: all 0.3s ease;
             }
             
             &::after {
               border: none;
             }
             
             background: linear-gradient(135deg, #0984e3, #74b9ff);
             
             &:hover {
               transform: translateY(-3rpx) scale(1.05);
               box-shadow: 0 6rpx 20rpx rgba(9, 132, 227, 0.5);
               background: linear-gradient(135deg, #74b9ff, #0984e3);
             }
           }
         }
    }
  }
}

.empty-state {
  text-align: center;
  padding: 80rpx 40rpx;
  animation: fadeInUp 0.6s ease-out;
  
  .empty-icon {
    font-size: 120rpx;
    margin-bottom: 30rpx;
    animation: bounce 2s infinite;
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.2);
    }
  }
  
  .empty-title {
    font-size: 36rpx;
    color: #333333;
    margin-bottom: 15rpx;
    font-weight: bold;
    transition: all 0.3s ease;
  }
  
  .empty-desc {
    font-size: 28rpx;
    color: #666666;
    margin-bottom: 40rpx;
    transition: all 0.3s ease;
  }
  
     .reset-btn {
     background: linear-gradient(135deg, #0984e3, #74b9ff);
     color: #ffffff;
     border: none;
     border-radius: 50rpx;
     padding: 20rpx 40rpx;
     font-size: 28rpx;
     display: flex;
     align-items: center;
     justify-content: center;
     transition: all 0.3s ease;
     cursor: pointer;
     
     .btn-icon {
       font-size: 28rpx;
       margin-right: 10rpx;
       transition: all 0.3s ease;
     }
     
     .btn-text {
       font-size: 28rpx;
       transition: all 0.3s ease;
     }
     
     &:hover {
       transform: translateY(-3rpx) scale(1.05);
       box-shadow: 0 6rpx 20rpx rgba(9, 132, 227, 0.5);
     }
   }
}

/* 占位符样式 */
.search-placeholder {
  color: #999999;
}

/* Animation keyframes */
@keyframes float {
  0% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
  25% {
    transform: translateY(-10px) translateX(10px) scale(1.05);
    opacity: 0.9;
  }
  50% {
    transform: translateY(5px) translateX(-5px) scale(1.02);
    opacity: 1;
  }
  75% {
    transform: translateY(-5px) translateX(5px) scale(1.03);
    opacity: 0.9;
  }
  100% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
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
    transform: translateY(-5rpx);
  }
  60% {
    transform: translateY(-3rpx);
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
</style>