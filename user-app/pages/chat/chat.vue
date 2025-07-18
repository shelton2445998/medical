<template>
  <view class="chat-container">
    <!-- 聊天头部 -->
    <view class="chat-header">
      <view class="header-content">
        <text class="header-title">AI医疗助手</text>
        <text class="header-subtitle">专业健康咨询，随时为您服务</text>
      </view>
      <view class="header-actions">
        <view class="action-btn" @click="clearChat">
          <text class="action-text">清空</text>
        </view>
      </view>
    </view>

    <!-- 聊天消息列表 -->
    <scroll-view 
      class="chat-messages" 
      scroll-y="true" 
      :scroll-top="scrollTop"
      :scroll-into-view="scrollIntoView"
      @scrolltoupper="loadMoreMessages"
    >
      <!-- 欢迎消息 -->
      <view class="message-item ai-message" v-if="messages.length === 0">
        <view class="message-avatar">
          <image src="/static/images/avatar1.jpg" class="avatar-img"></image>
        </view>
        <view class="message-content">
          <view class="message-bubble ai-bubble">
            <text class="message-text">您好！我是您的AI医疗助手，很高兴为您服务。我可以为您提供健康咨询、症状分析、用药建议等服务。请问有什么可以帮助您的吗？</text>
          </view>
          <text class="message-time">{{ getCurrentTime() }}</text>
        </view>
      </view>

      <!-- 消息列表 -->
      <view 
        class="message-item" 
        :class="message.isUser ? 'user-message' : 'ai-message'"
        v-for="(message, index) in messages" 
        :key="index"
        :id="'msg-' + index"
      >
        <view class="message-avatar" v-if="!message.isUser">
          <image src="/static/images/avatar1.jpg" class="avatar-img"></image>
        </view>
        <view class="message-content">
          <view class="message-bubble" :class="message.isUser ? 'user-bubble' : 'ai-bubble'">
            <text class="message-text">{{ message.content }}</text>
          </view>
          <text class="message-time">{{ message.time }}</text>
        </view>
        <view class="message-avatar" v-if="message.isUser">
          <image src="/static/images/user-avatar.jpg" class="avatar-img"></image>
        </view>
      </view>

      <!-- AI正在输入提示 -->
      <view class="message-item ai-message" v-if="isTyping">
        <view class="message-avatar">
          <image src="/static/images/avatar1.jpg" class="avatar-img"></image>
        </view>
        <view class="message-content">
          <view class="message-bubble ai-bubble typing-bubble">
            <view class="typing-dots">
              <view class="dot"></view>
              <view class="dot"></view>
              <view class="dot"></view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 输入区域 -->
    <view class="chat-input">
      <view class="input-container">
        <input 
          class="message-input" 
          v-model="inputMessage" 
          placeholder="请输入您的问题..."
          :disabled="isTyping"
          @confirm="sendMessage"
        />
        <view class="send-btn" :class="{ 'disabled': !inputMessage.trim() || isTyping }" @click="sendMessage">
          <text class="send-text">发送</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
// 导入AI聊天相关的API接口配置
import { chatApi } from '@/utils/api.js'
// 导入HTTP请求方法
import { post, get } from '@/utils/request.js'

// 导出AI聊天页面组件配置
export default {
  // 组件数据定义
  data() {
    return {
      /**
       * 聊天消息列表
       * @type {Array} 包含所有聊天消息的数组
       */
      messages: [],
      
      /**
       * 用户输入的消息内容
       * @type {String} 当前输入框中的文本
       */
      inputMessage: '',
      
      /**
       * AI是否正在输入状态标志
       * @type {Boolean} 控制AI输入提示的显示
       */
      isTyping: false,
      
      /**
       * 滚动视图的滚动位置
       * @type {Number} 用于控制聊天消息列表的滚动位置
       */
      scrollTop: 0,
      
      /**
       * 滚动到指定元素的ID
       * @type {String} 用于自动滚动到最新消息
       */
      scrollIntoView: '',
      
      /**
       * 加载状态标志
       * @type {Boolean} 控制加载提示的显示
       */
      loading: false
    }
  },
  
  /**
   * 页面加载时的生命周期钩子
   * 在页面加载时自动加载聊天历史记录
   */
  onLoad() {
    this.loadChatHistory()
  },
  
  // 组件方法定义
  methods: {
    /**
     * 发送消息方法
     * 处理用户发送消息的逻辑，包括输入验证、消息发送和AI回复
     */
    async sendMessage() {
      // 检查输入内容是否为空或AI正在输入
      if (!this.inputMessage.trim() || this.isTyping) {
        return
      }
      
      // 获取用户输入的消息内容并清空输入框
      const userMessage = this.inputMessage.trim()
      this.inputMessage = ''
      
      // 将用户消息添加到消息列表
      this.addMessage(userMessage, true)
      
      // 设置AI正在输入状态并滚动到底部
      this.isTyping = true
      this.scrollToBottom()
      
      try {
        // 调用后端API发送消息
        const response = await post(chatApi.sendMessage, {
          message: userMessage
        })
        
        // 检查响应状态
        if (response.code === 200) {
          // 添加AI回复消息
          this.addMessage(response.data, false)
        } else {
          // 添加错误提示消息
          this.addMessage('抱歉，我暂时无法回答您的问题，请稍后再试。', false)
        }
      } catch (error) {
        // 处理网络错误
        console.error('发送消息失败:', error)
        this.addMessage('抱歉，网络连接出现问题，请检查网络后重试。', false)
      } finally {
        // 结束AI输入状态并滚动到底部
        this.isTyping = false
        this.scrollToBottom()
      }
    },
    
    /**
     * 添加消息到列表方法
     * @param {String} content - 消息内容
     * @param {Boolean} isUser - 是否为用户消息
     */
    addMessage(content, isUser) {
      this.messages.push({
        content: content, // 消息内容
        isUser: isUser, // 是否为用户消息
        time: this.getCurrentTime() // 消息时间
      })
    },
    
    /**
     * 滚动到底部方法
     * 在发送消息后自动滚动到最新消息位置
     */
    scrollToBottom() {
      this.$nextTick(() => {
        // 使用uni-app的选择器查询API获取聊天消息容器的高度
        const query = uni.createSelectorQuery().in(this)
        query.select('.chat-messages').boundingClientRect(data => {
          if (data) {
            // 设置滚动位置为容器高度，实现滚动到底部
            this.scrollTop = data.height
          }
        }).exec()
      })
    },
    
    /**
     * 获取当前时间方法
     * @returns {String} 格式化的时间字符串 (HH:mm)
     */
    getCurrentTime() {
      const now = new Date()
      // 获取小时并补零
      const hours = now.getHours().toString().padStart(2, '0')
      // 获取分钟并补零
      const minutes = now.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    },
    
    /**
     * 加载聊天历史记录方法
     * 从服务器获取用户的历史聊天记录
     */
    async loadChatHistory() {
      try {
        // 调用获取聊天历史的API
        const response = await get(chatApi.getChatHistory)
        
        // 检查响应状态和数据
        if (response.code === 200 && response.data) {
          // 将服务器返回的历史记录转换为本地消息格式
          this.messages = response.data.map(item => ({
            content: item.content, // 消息内容
            isUser: item.isUser, // 是否为用户消息
            time: item.time // 消息时间
          }))
        }
      } catch (error) {
        // 处理加载历史记录失败的情况
        console.error('加载聊天历史失败:', error)
      }
    },
    
    /**
     * 清空聊天记录方法
     * 显示确认对话框，用户确认后清空所有聊天记录
     */
    async clearChat() {
      // 显示确认对话框
      uni.showModal({
        title: '确认清空', // 对话框标题
        content: '确定要清空所有聊天记录吗？', // 对话框内容
        success: async (res) => {
          // 用户点击确认按钮
          if (res.confirm) {
            try {
              // 调用清空聊天记录的API
              const response = await post(chatApi.clearChatHistory)
              
              // 检查API响应状态
              if (response.code === 200) {
                // 清空本地消息列表
                this.messages = []
                // 显示成功提示
                uni.showToast({
                  title: '聊天记录已清空',
                  icon: 'success'
                })
              }
            } catch (error) {
              // 处理清空失败的情况
              console.error('清空聊天记录失败:', error)
              uni.showToast({
                title: '清空失败',
                icon: 'error'
              })
            }
          }
        }
      })
    },
    
    /**
     * 加载更多消息方法
     * 当用户滚动到顶部时触发，可以用于加载更多历史消息
     */
    loadMoreMessages() {
      // 这里可以实现加载更多历史消息的逻辑
      // 例如：分页加载更早的聊天记录
      console.log('加载更多消息')
    }
  }
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
}

/* 聊天头部 */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.header-content {
  flex: 1;
}

.header-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  display: block;
}

.header-subtitle {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  display: block;
  margin-top: 8rpx;
}

.header-actions {
  display: flex;
  align-items: center;
}

.action-btn {
  padding: 12rpx 24rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20rpx;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.action-text {
  font-size: 24rpx;
  color: #fff;
}

/* 聊天消息列表 */
.chat-messages {
  flex: 1;
  padding: 20rpx;
  overflow-y: auto;
}

.message-item {
  display: flex;
  margin-bottom: 30rpx;
  animation: fadeInUp 0.3s ease-out;
}

.user-message {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 80rpx;
  height: 80rpx;
  margin: 0 20rpx;
  flex-shrink: 0;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 3rpx solid rgba(255, 255, 255, 0.3);
}

.message-content {
  flex: 1;
  max-width: 70%;
}

.message-bubble {
  padding: 20rpx 24rpx;
  border-radius: 24rpx;
  position: relative;
  word-wrap: break-word;
}

.user-bubble {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 100%);
  color: #fff;
  border-bottom-right-radius: 8rpx;
}

.ai-bubble {
  background: rgba(255, 255, 255, 0.9);
  color: #333;
  border-bottom-left-radius: 8rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.typing-bubble {
  background: rgba(255, 255, 255, 0.8);
}

.message-text {
  font-size: 28rpx;
  line-height: 1.5;
  display: block;
}

.message-time {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 8rpx;
  display: block;
}

.user-message .message-time {
  text-align: right;
  color: rgba(255, 255, 255, 0.8);
}

/* 输入区域 */
.chat-input {
  padding: 20rpx 30rpx;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.input-container {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 30rpx;
  padding: 8rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.message-input {
  flex: 1;
  padding: 20rpx 24rpx;
  font-size: 28rpx;
  color: #333;
  background: transparent;
  border: none;
  outline: none;
}

.send-btn {
  padding: 16rpx 32rpx;
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 100%);
  border-radius: 24rpx;
  margin-left: 16rpx;
  transition: all 0.3s ease;
}

.send-btn.disabled {
  background: rgba(102, 126, 234, 0.5);
  opacity: 0.6;
}

.send-text {
  font-size: 28rpx;
  color: #fff;
  font-weight: 500;
}

/* 打字动画 */
.typing-dots {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.dot {
  width: 12rpx;
  height: 12rpx;
  background: #667eea;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out;
}

.dot:nth-child(1) {
  animation-delay: -0.32s;
}

.dot:nth-child(2) {
  animation-delay: -0.16s;
}

.dot:nth-child(3) {
  animation-delay: 0s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 750rpx) {
  .message-content {
    max-width: 80%;
  }
  
  .header-title {
    font-size: 32rpx;
  }
  
  .header-subtitle {
    font-size: 22rpx;
  }
}
</style> 