<template>
  <view class="feedback-container">
    <!-- 反馈类型 -->
    <view class="feedback-section">
      <view class="section-title">反馈类型</view>
      <view class="type-list">
        <view 
          class="type-item" 
          v-for="(item, index) in feedbackTypes" 
          :key="index"
          :class="{ active: selectedType === item.value }"
          @click="selectType(item.value)"
        >
          <text class="type-name">{{item.name}}</text>
        </view>
      </view>
    </view>

    <!-- 反馈内容 -->
    <view class="feedback-section">
      <view class="section-title">反馈内容</view>
      <view class="content-box">
        <textarea 
          class="feedback-textarea" 
          v-model="feedbackContent"
          placeholder="请详细描述您遇到的问题或建议..."
          :maxlength="500"
          show-confirm-bar="false"
        ></textarea>
        <view class="word-count">{{feedbackContent.length}}/500</view>
      </view>
    </view>

    <!-- 联系方式 -->
    <view class="feedback-section">
      <view class="section-title">联系方式（选填）</view>
      <view class="contact-box">
        <input 
          class="contact-input" 
          v-model="contactInfo"
          placeholder="请留下您的手机号或邮箱，方便我们回复"
          type="text"
        />
      </view>
    </view>

    <!-- 图片上传 -->
    <view class="feedback-section">
      <view class="section-title">上传图片（选填，最多3张）</view>
      <view class="upload-box">
        <view class="image-list">
          <view 
            class="image-item" 
            v-for="(item, index) in imageList" 
            :key="index"
          >
            <image :src="item" class="upload-image" mode="aspectFill"></image>
            <view class="delete-btn" @click="deleteImage(index)">×</view>
          </view>
          <view 
            class="upload-btn" 
            v-if="imageList.length < 3"
            @click="chooseImage"
          >
            <text class="upload-icon">+</text>
            <text class="upload-text">添加图片</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="submit-section">
      <button 
        class="submit-btn" 
        :disabled="!canSubmit"
        @click="submitFeedback"
      >
        提交反馈
      </button>
    </view>

    <!-- 反馈记录 -->
    <view class="feedback-section">
      <view class="section-header">
        <text class="section-title">我的反馈</text>
        <text class="view-all" @click="viewAllFeedback">查看全部</text>
      </view>
      <view class="feedback-list">
        <view 
          class="feedback-item" 
          v-for="(item, index) in feedbackHistory" 
          :key="index"
          @click="viewFeedbackDetail(item)"
        >
          <view class="feedback-info">
            <view class="feedback-type">{{getTypeName(item.type)}}</view>
            <view class="feedback-status" :class="item.status">
              {{getStatusText(item.status)}}
            </view>
          </view>
          <view class="feedback-content">{{item.content}}</view>
          <view class="feedback-time">{{item.createTime}}</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedType: 'bug',
      feedbackContent: '',
      contactInfo: '',
      imageList: [],
      feedbackTypes: [
        { name: '功能异常', value: 'bug' },
        { name: '功能建议', value: 'suggestion' },
        { name: '界面优化', value: 'ui' },
        { name: '其他问题', value: 'other' }
      ],
      feedbackHistory: [
        {
          id: 1,
          type: 'bug',
          content: '预约功能无法正常使用',
          status: 'pending',
          createTime: '2025-01-15 10:30:00'
        },
        {
          id: 2,
          type: 'suggestion',
          content: '希望增加更多体检套餐',
          status: 'processing',
          createTime: '2025-01-14 15:20:00'
        },
        {
          id: 3,
          type: 'ui',
          content: '界面设计很美观',
          status: 'completed',
          createTime: '2025-01-13 09:15:00'
        }
      ]
    }
  },
  computed: {
    canSubmit() {
      return this.selectedType && this.feedbackContent.trim().length > 0;
    }
  },
  methods: {
    // 选择反馈类型
    selectType(type) {
      this.selectedType = type;
    },
    
    // 选择图片
    chooseImage() {
      uni.chooseImage({
        count: 3 - this.imageList.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.imageList = [...this.imageList, ...res.tempFilePaths];
        }
      });
    },
    
    // 删除图片
    deleteImage(index) {
      this.imageList.splice(index, 1);
    },
    
    // 提交反馈
    submitFeedback() {
      if (!this.canSubmit) {
        uni.showToast({
          title: '请填写反馈内容',
          icon: 'none'
        });
        return;
      }
      
      uni.showLoading({
        title: '提交中...'
      });
      
      // 模拟提交
      setTimeout(() => {
        uni.hideLoading();
        
        // 添加到反馈历史
        const newFeedback = {
          id: Date.now(),
          type: this.selectedType,
          content: this.feedbackContent,
          status: 'pending',
          createTime: new Date().toLocaleString()
        };
        
        this.feedbackHistory.unshift(newFeedback);
        
        // 清空表单
        this.feedbackContent = '';
        this.contactInfo = '';
        this.imageList = [];
        this.selectedType = 'bug';
        
        uni.showToast({
          title: '提交成功',
          icon: 'success'
        });
      }, 1500);
    },
    
    // 获取类型名称
    getTypeName(type) {
      const typeItem = this.feedbackTypes.find(item => item.value === type);
      return typeItem ? typeItem.name : '未知类型';
    },
    
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        pending: '待处理',
        processing: '处理中',
        completed: '已完成'
      };
      return statusMap[status] || '未知状态';
    },
    
    // 查看反馈详情
    viewFeedbackDetail(item) {
      uni.navigateTo({
        url: `/pages/feedback-detail/feedback-detail?id=${item.id}`
      });
    },
    
    // 查看全部反馈
    viewAllFeedback() {
      uni.navigateTo({
        url: '/pages/feedback-history/feedback-history'
      });
    }
  }
}
</script>

<style lang="scss">
.feedback-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20rpx;
}

.feedback-section {
  background-color: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  padding: 30rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 20rpx;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    
    .view-all {
      font-size: 26rpx;
      color: #1296db;
    }
  }
}

.type-list {
  display: flex;
  flex-wrap: wrap;
  
  .type-item {
    padding: 20rpx 30rpx;
    background-color: #f8f9fa;
    border-radius: 30rpx;
    margin-right: 20rpx;
    margin-bottom: 20rpx;
    border: 2rpx solid transparent;
    
    &.active {
      background-color: #e6f3ff;
      border-color: #1296db;
    }
    
    .type-name {
      font-size: 26rpx;
      color: #333333;
    }
  }
}

.content-box {
  position: relative;
  
  .feedback-textarea {
    width: 100%;
    height: 200rpx;
    padding: 20rpx;
    border: 2rpx solid #e9ecef;
    border-radius: 10rpx;
    font-size: 28rpx;
    color: #333333;
    background-color: #ffffff;
  }
  
  .word-count {
    position: absolute;
    bottom: 10rpx;
    right: 20rpx;
    font-size: 24rpx;
    color: #999999;
  }
}

.contact-box {
  .contact-input {
    width: 100%;
    height: 80rpx;
    padding: 0 20rpx;
    border: 2rpx solid #e9ecef;
    border-radius: 10rpx;
    font-size: 28rpx;
    color: #333333;
    background-color: #ffffff;
  }
}

.upload-box {
  .image-list {
    display: flex;
    flex-wrap: wrap;
    
    .image-item {
      position: relative;
      width: 200rpx;
      height: 200rpx;
      margin-right: 20rpx;
      margin-bottom: 20rpx;
      
      .upload-image {
        width: 100%;
        height: 100%;
        border-radius: 10rpx;
      }
      
      .delete-btn {
        position: absolute;
        top: -10rpx;
        right: -10rpx;
        width: 40rpx;
        height: 40rpx;
        background-color: #ff5a5f;
        color: #ffffff;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24rpx;
      }
    }
    
    .upload-btn {
      width: 200rpx;
      height: 200rpx;
      border: 2rpx dashed #d9d9d9;
      border-radius: 10rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: #fafafa;
      
      .upload-icon {
        font-size: 48rpx;
        color: #999999;
        margin-bottom: 10rpx;
      }
      
      .upload-text {
        font-size: 24rpx;
        color: #999999;
      }
    }
  }
}

.submit-section {
  padding: 30rpx;
  
  .submit-btn {
    width: 100%;
    height: 90rpx;
    line-height: 90rpx;
    background-color: #1296db;
    color: #ffffff;
    font-size: 30rpx;
    border-radius: 45rpx;
    border: none;
    
    &:disabled {
      background-color: #cccccc;
    }
  }
}

.feedback-list {
  .feedback-item {
    padding: 20rpx 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .feedback-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10rpx;
      
      .feedback-type {
        font-size: 26rpx;
        color: #333333;
        font-weight: bold;
      }
      
      .feedback-status {
        font-size: 24rpx;
        padding: 4rpx 12rpx;
        border-radius: 20rpx;
        
        &.pending {
          background-color: #fff7e6;
          color: #fa8c16;
        }
        
        &.processing {
          background-color: #e6f7ff;
          color: #1296db;
        }
        
        &.completed {
          background-color: #f6ffed;
          color: #52c41a;
        }
      }
    }
    
    .feedback-content {
      font-size: 26rpx;
      color: #666666;
      margin-bottom: 10rpx;
      line-height: 1.4;
    }
    
    .feedback-time {
      font-size: 24rpx;
      color: #999999;
    }
  }
}
</style> 