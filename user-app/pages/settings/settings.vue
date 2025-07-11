<template>
  <view class="settings-container">
    <!-- 设置列表 -->
    <view class="settings-list">
      <!-- 账户设置 -->
      <view class="settings-section">
        <view class="section-title">账户设置</view>
        <view class="settings-item" @click="navigateTo('/pages/profile/profile')">
          <view class="item-left">
            <image src="/static/images/icons/icon-profile.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">个人资料</text>
          </view>
          <text class="iconfont icon-right"></text>
        </view>
        <view class="settings-item" @click="navigateTo('/pages/change-password/change-password')">
          <view class="item-left">
            <image src="/static/images/icons/icon-password.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">修改密码</text>
          </view>
          <text class="iconfont icon-right"></text>
        </view>
        <view class="settings-item" @click="navigateTo('/pages/privacy/privacy')">
          <view class="item-left">
            <image src="/static/images/icons/icon-privacy.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">隐私设置</text>
          </view>
          <text class="iconfont icon-right"></text>
        </view>
      </view>

      <!-- 通知设置 -->
      <view class="settings-section">
        <view class="section-title">通知设置</view>
        <view class="settings-item">
          <view class="item-left">
            <image src="/static/images/icons/icon-notification.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">推送通知</text>
          </view>
          <switch :checked="notificationEnabled" @change="onNotificationChange" color="#1296db" />
        </view>
        <view class="settings-item">
          <view class="item-left">
            <image src="/static/images/icons/icon-sound.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">声音提醒</text>
          </view>
          <switch :checked="soundEnabled" @change="onSoundChange" color="#1296db" />
        </view>
        <view class="settings-item">
          <view class="item-left">
            <image src="/static/images/icons/icon-vibration.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">震动提醒</text>
          </view>
          <switch :checked="vibrationEnabled" @change="onVibrationChange" color="#1296db" />
        </view>
      </view>

      <!-- 应用设置 -->
      <view class="settings-section">
        <view class="section-title">应用设置</view>
        <view class="settings-item" @click="clearCache">
          <view class="item-left">
            <image src="/static/images/icons/icon-cache.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">清除缓存</text>
          </view>
          <view class="item-right">
            <text class="cache-size">{{cacheSize}}</text>
            <text class="iconfont icon-right"></text>
          </view>
        </view>
        <view class="settings-item" @click="checkUpdate">
          <view class="item-left">
            <image src="/static/images/icons/icon-update.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">检查更新</text>
          </view>
          <view class="item-right">
            <text class="version-text">{{currentVersion}}</text>
            <text class="iconfont icon-right"></text>
          </view>
        </view>
        <view class="settings-item" @click="navigateTo('/pages/help/help')">
          <view class="item-left">
            <image src="/static/images/icons/icon-help.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">帮助中心</text>
          </view>
          <text class="iconfont icon-right"></text>
        </view>
      </view>

      <!-- 其他设置 -->
      <view class="settings-section">
        <view class="section-title">其他设置</view>
        <view class="settings-item" @click="navigateTo('/pages/terms/terms')">
          <view class="item-left">
            <image src="/static/images/icons/icon-terms.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">用户协议</text>
          </view>
          <text class="iconfont icon-right"></text>
        </view>
        <view class="settings-item" @click="navigateTo('/pages/privacy-policy/privacy-policy')">
          <view class="item-left">
            <image src="/static/images/icons/icon-policy.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">隐私政策</text>
          </view>
          <text class="iconfont icon-right"></text>
        </view>
        <view class="settings-item" @click="contactService">
          <view class="item-left">
            <image src="/static/images/icons/icon-service.png" class="item-icon" mode="aspectFit"></image>
            <text class="item-name">联系客服</text>
          </view>
          <text class="iconfont icon-right"></text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      notificationEnabled: true,
      soundEnabled: true,
      vibrationEnabled: false,
      cacheSize: '2.5MB',
      currentVersion: 'v1.0.0'
    }
  },
  onLoad() {
    this.loadSettings();
  },
  methods: {
    // 加载设置
    loadSettings() {
      // 从本地存储加载设置
      const settings = uni.getStorageSync('appSettings');
      if (settings) {
        this.notificationEnabled = settings.notificationEnabled !== undefined ? settings.notificationEnabled : true;
        this.soundEnabled = settings.soundEnabled !== undefined ? settings.soundEnabled : true;
        this.vibrationEnabled = settings.vibrationEnabled !== undefined ? settings.vibrationEnabled : false;
      }
      
      // 获取缓存大小
      this.getCacheSize();
    },
    
    // 保存设置
    saveSettings() {
      const settings = {
        notificationEnabled: this.notificationEnabled,
        soundEnabled: this.soundEnabled,
        vibrationEnabled: this.vibrationEnabled
      };
      uni.setStorageSync('appSettings', settings);
    },
    
    // 推送通知开关
    onNotificationChange(e) {
      this.notificationEnabled = e.detail.value;
      this.saveSettings();
      
      if (this.notificationEnabled) {
        uni.showToast({
          title: '已开启推送通知',
          icon: 'success'
        });
      } else {
        uni.showToast({
          title: '已关闭推送通知',
          icon: 'none'
        });
      }
    },
    
    // 声音提醒开关
    onSoundChange(e) {
      this.soundEnabled = e.detail.value;
      this.saveSettings();
      
      if (this.soundEnabled) {
        uni.showToast({
          title: '已开启声音提醒',
          icon: 'success'
        });
      } else {
        uni.showToast({
          title: '已关闭声音提醒',
          icon: 'none'
        });
      }
    },
    
    // 震动提醒开关
    onVibrationChange(e) {
      this.vibrationEnabled = e.detail.value;
      this.saveSettings();
      
      if (this.vibrationEnabled) {
        uni.showToast({
          title: '已开启震动提醒',
          icon: 'success'
        });
      } else {
        uni.showToast({
          title: '已关闭震动提醒',
          icon: 'none'
        });
      }
    },
    
    // 清除缓存
    clearCache() {
      uni.showModal({
        title: '提示',
        content: '确定要清除缓存吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '清除中...'
            });
            
            setTimeout(() => {
              uni.hideLoading();
              this.cacheSize = '0KB';
              uni.showToast({
                title: '缓存已清除',
                icon: 'success'
              });
            }, 1500);
          }
        }
      });
    },
    
    // 检查更新
    checkUpdate() {
      uni.showLoading({
        title: '检查中...'
      });
      
      setTimeout(() => {
        uni.hideLoading();
        uni.showModal({
          title: '检查更新',
          content: '当前已是最新版本',
          showCancel: false
        });
      }, 1000);
    },
    
    // 联系客服
    contactService() {
      uni.showActionSheet({
        itemList: ['在线客服', '电话客服', '邮件客服'],
        success: (res) => {
          switch (res.tapIndex) {
            case 0:
              // 在线客服
              uni.navigateTo({
                url: '/pages/chat/chat'
              });
              break;
            case 1:
              // 电话客服
              uni.makePhoneCall({
                phoneNumber: '400-123-4567'
              });
              break;
            case 2:
              // 邮件客服
              uni.setClipboardData({
                data: 'service@medical.com',
                success: () => {
                  uni.showToast({
                    title: '邮箱已复制',
                    icon: 'success'
                  });
                }
              });
              break;
          }
        }
      });
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
.settings-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20rpx;
}

.settings-list {
  .settings-section {
    background-color: #ffffff;
    border-radius: 20rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    
    .section-title {
      padding: 30rpx;
      font-size: 28rpx;
      color: #666666;
      background-color: #f8f9fa;
      border-bottom: 1px solid #e9ecef;
    }
    
    .settings-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1px solid #f5f5f5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .item-left {
        display: flex;
        align-items: center;
        
        .item-icon {
          width: 40rpx;
          height: 40rpx;
          margin-right: 20rpx;
        }
        
        .item-name {
          font-size: 28rpx;
          color: #333333;
        }
      }
      
      .item-right {
        display: flex;
        align-items: center;
        
        .cache-size, .version-text {
          font-size: 24rpx;
          color: #999999;
          margin-right: 10rpx;
        }
        
        .iconfont {
          font-size: 24rpx;
          color: #999999;
        }
      }
      
      .iconfont {
        font-size: 24rpx;
        color: #999999;
      }
    }
  }
}
</style> 