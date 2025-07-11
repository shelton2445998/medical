<template>
  <view class="about-container">
    <!-- 应用信息 -->
    <view class="app-info-section">
      <view class="app-logo">
        <image src="/static/img/logo.png" class="logo-image" mode="aspectFit"></image>
      </view>
      <view class="app-name">医疗体检预约系统</view>
      <view class="app-version">版本 {{appVersion}}</view>
      <view class="app-desc">专业的医疗体检预约平台，为您提供便捷的体检服务</view>
    </view>

    <!-- 功能特色 -->
    <view class="feature-section">
      <view class="section-title">功能特色</view>
      <view class="feature-list">
        <view class="feature-item" v-for="(item, index) in features" :key="index">
          <image :src="item.icon" class="feature-icon" mode="aspectFit"></image>
          <view class="feature-content">
            <text class="feature-title">{{item.title}}</text>
            <text class="feature-desc">{{item.desc}}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 团队介绍 -->
    <view class="team-section">
      <view class="section-title">开发团队</view>
      <view class="team-info">
        <view class="team-item" v-for="(item, index) in teamMembers" :key="index">
          <image :src="item.avatar" class="member-avatar" mode="aspectFill"></image>
          <view class="member-info">
            <text class="member-name">{{item.name}}</text>
            <text class="member-role">{{item.role}}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 联系方式 -->
    <view class="contact-section">
      <view class="section-title">联系我们</view>
      <view class="contact-list">
        <view class="contact-item" @click="copyEmail">
          <view class="contact-left">
            <image src="/static/images/icons/icon-email.png" class="contact-icon" mode="aspectFit"></image>
            <text class="contact-label">邮箱</text>
          </view>
          <view class="contact-right">
            <text class="contact-value">support@medical.com</text>
            <text class="iconfont icon-right"></text>
          </view>
        </view>
        <view class="contact-item" @click="makePhoneCall">
          <view class="contact-left">
            <image src="/static/images/icons/icon-phone.png" class="contact-icon" mode="aspectFit"></image>
            <text class="contact-label">电话</text>
          </view>
          <view class="contact-right">
            <text class="contact-value">400-123-4567</text>
            <text class="iconfont icon-right"></text>
          </view>
        </view>
        <view class="contact-item" @click="copyWechat">
          <view class="contact-left">
            <image src="/static/images/icons/icon-wechat.png" class="contact-icon" mode="aspectFit"></image>
            <text class="contact-label">微信</text>
          </view>
          <view class="contact-right">
            <text class="contact-value">medical_service</text>
            <text class="iconfont icon-right"></text>
          </view>
        </view>
        <view class="contact-item" @click="openWebsite">
          <view class="contact-left">
            <image src="/static/images/icons/icon-website.png" class="contact-icon" mode="aspectFit"></image>
            <text class="contact-label">官网</text>
          </view>
          <view class="contact-right">
            <text class="contact-value">www.medical.com</text>
            <text class="iconfont icon-right"></text>
          </view>
        </view>
      </view>
    </view>

    <!-- 版权信息 -->
    <view class="copyright-section">
      <view class="copyright-text">
        <text>? 2025 医疗体检预约系统</text>
        <text>版权所有</text>
      </view>
      <view class="copyright-links">
        <text class="link-item" @click="navigateTo('/pages/privacy-policy/privacy-policy')">隐私政策</text>
        <text class="link-separator">|</text>
        <text class="link-item" @click="navigateTo('/pages/terms/terms')">用户协议</text>
        <text class="link-separator">|</text>
        <text class="link-item" @click="navigateTo('/pages/feedback/feedback')">意见反馈</text>
      </view>
    </view>

    <!-- 更新日志 -->
    <view class="update-section">
      <view class="section-title">更新日志</view>
      <view class="update-list">
        <view class="update-item" v-for="(item, index) in updateLogs" :key="index">
          <view class="update-header">
            <text class="update-version">{{item.version}}</text>
            <text class="update-date">{{item.date}}</text>
          </view>
          <view class="update-content">
            <text class="update-title">{{item.title}}</text>
            <view class="update-details">
              <text class="detail-item" v-for="(detail, detailIndex) in item.details" :key="detailIndex">
                ? {{detail}}
              </text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      appVersion: 'v1.0.0',
      features: [
        {
          icon: '/static/images/icons/icon-appointment.png',
          title: '在线预约',
          desc: '便捷的体检预约服务，支持多种套餐选择'
        },
        {
          icon: '/static/images/icons/icon-report.png',
          title: '体检报告',
          desc: '详细的体检报告查看，健康数据一目了然'
        },
        {
          icon: '/static/images/icons/icon-health.png',
          title: '健康管理',
          desc: '全面的健康档案管理，持续关注您的健康'
        },
        {
          icon: '/static/images/icons/icon-consult.png',
          title: '在线咨询',
          desc: '专业的医疗咨询服务，解答您的健康疑问'
        }
      ],
      teamMembers: [
        {
          avatar: '/static/images/avatars/avatar1.png',
          name: '张医生',
          role: '产品经理'
        },
        {
          avatar: '/static/images/avatars/avatar2.png',
          name: '李工程师',
          role: '技术总监'
        },
        {
          avatar: '/static/images/avatars/avatar3.png',
          name: '王设计师',
          role: 'UI设计师'
        },
        {
          avatar: '/static/images/avatars/avatar4.png',
          name: '刘测试',
          role: '测试工程师'
        }
      ],
      updateLogs: [
        {
          version: 'v1.0.0',
          date: '2025-01-15',
          title: '首次发布',
          details: [
            '完成基础预约功能',
            '实现体检报告查看',
            '添加健康档案管理',
            '优化用户界面体验'
          ]
        },
        {
          version: 'v0.9.0',
          date: '2025-01-10',
          title: '测试版本',
          details: [
            '修复已知问题',
            '优化性能表现',
            '完善错误处理',
            '增加用户反馈功能'
          ]
        },
        {
          version: 'v0.8.0',
          date: '2025-01-05',
          title: '开发版本',
          details: [
            '完成核心功能开发',
            '实现数据存储',
            '添加用户认证',
            '集成支付功能'
          ]
        }
      ]
    }
  },
  methods: {
    // 复制邮箱
    copyEmail() {
      uni.setClipboardData({
        data: 'support@medical.com',
        success: () => {
          uni.showToast({
            title: '邮箱已复制',
            icon: 'success'
          });
        }
      });
    },
    
    // 拨打电话
    makePhoneCall() {
      uni.makePhoneCall({
        phoneNumber: '400-123-4567'
      });
    },
    
    // 复制微信号
    copyWechat() {
      uni.setClipboardData({
        data: 'medical_service',
        success: () => {
          uni.showToast({
            title: '微信号已复制',
            icon: 'success'
          });
        }
      });
    },
    
    // 打开官网
    openWebsite() {
      // 在H5环境下打开链接
      // #ifdef H5
      window.open('https://www.medical.com', '_blank');
      // #endif
      
      // 在其他环境下复制链接
      // #ifndef H5
      uni.setClipboardData({
        data: 'https://www.medical.com',
        success: () => {
          uni.showToast({
            title: '网址已复制',
            icon: 'success'
          });
        }
      });
      // #endif
    },
    
    // 页面跳转
    navigateTo(url) {
      uni.navigateTo({
        url: url
      });
    }
  }
}
</script>

<style lang="scss">
.about-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20rpx;
}

.app-info-section {
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 40rpx;
  text-align: center;
  margin-bottom: 20rpx;
  
  .app-logo {
    margin-bottom: 20rpx;
    
    .logo-image {
      width: 120rpx;
      height: 120rpx;
      border-radius: 20rpx;
    }
  }
  
  .app-name {
    font-size: 36rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 10rpx;
  }
  
  .app-version {
    font-size: 24rpx;
    color: #999999;
    margin-bottom: 20rpx;
  }
  
  .app-desc {
    font-size: 26rpx;
    color: #666666;
    line-height: 1.5;
  }
}

.feature-section, .team-section, .contact-section, .update-section {
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 30rpx;
    position: relative;
    padding-left: 20rpx;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 6rpx;
      height: 30rpx;
      background-color: #1296db;
      border-radius: 3rpx;
    }
  }
}

.feature-list {
  .feature-item {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .feature-icon {
      width: 80rpx;
      height: 80rpx;
      margin-right: 20rpx;
    }
    
    .feature-content {
      flex: 1;
      
      .feature-title {
        font-size: 28rpx;
        font-weight: bold;
        color: #333333;
        margin-bottom: 8rpx;
        display: block;
      }
      
      .feature-desc {
        font-size: 24rpx;
        color: #666666;
        line-height: 1.4;
      }
    }
  }
}

.team-info {
  .team-item {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .member-avatar {
      width: 80rpx;
      height: 80rpx;
      border-radius: 50%;
      margin-right: 20rpx;
    }
    
    .member-info {
      flex: 1;
      
      .member-name {
        font-size: 28rpx;
        font-weight: bold;
        color: #333333;
        margin-bottom: 8rpx;
        display: block;
      }
      
      .member-role {
        font-size: 24rpx;
        color: #666666;
      }
    }
  }
}

.contact-list {
  .contact-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 25rpx 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .contact-left {
      display: flex;
      align-items: center;
      
      .contact-icon {
        width: 40rpx;
        height: 40rpx;
        margin-right: 20rpx;
      }
      
      .contact-label {
        font-size: 28rpx;
        color: #333333;
      }
    }
    
    .contact-right {
      display: flex;
      align-items: center;
      
      .contact-value {
        font-size: 26rpx;
        color: #666666;
        margin-right: 10rpx;
      }
      
      .iconfont {
        font-size: 24rpx;
        color: #999999;
      }
    }
  }
}

.copyright-section {
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx;
  text-align: center;
  margin-bottom: 20rpx;
  
  .copyright-text {
    margin-bottom: 20rpx;
    
    text {
      font-size: 24rpx;
      color: #999999;
      display: block;
      margin-bottom: 5rpx;
    }
  }
  
  .copyright-links {
    .link-item {
      font-size: 24rpx;
      color: #1296db;
      margin: 0 10rpx;
    }
    
    .link-separator {
      font-size: 24rpx;
      color: #cccccc;
    }
  }
}

.update-list {
  .update-item {
    margin-bottom: 30rpx;
    padding-bottom: 30rpx;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      margin-bottom: 0;
      padding-bottom: 0;
      border-bottom: none;
    }
    
    .update-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15rpx;
      
      .update-version {
        font-size: 28rpx;
        font-weight: bold;
        color: #333333;
      }
      
      .update-date {
        font-size: 24rpx;
        color: #999999;
      }
    }
    
    .update-content {
      .update-title {
        font-size: 26rpx;
        color: #666666;
        margin-bottom: 15rpx;
        display: block;
      }
      
      .update-details {
        .detail-item {
          font-size: 24rpx;
          color: #999999;
          line-height: 1.6;
          display: block;
          margin-bottom: 8rpx;
        }
      }
    }
  }
}
</style> 