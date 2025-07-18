<!--
  Token检查工具组件
  
  用于开发和调试阶段检查用户登录状态和Token有效性
  提供Token查看、清除、测试请求等功能
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="token-checker">
    <h3>Token检查工具</h3>
    
    <!-- Token信息展示卡片 -->
    <el-card>
      <!-- 当前Token值显示 -->
      <div class="info-item">
        <span class="label">当前token值:</span>
        <span class="value">{{ token || '未设置' }}</span>
      </div>
      
      <!-- Token状态显示 -->
      <div class="info-item">
        <span class="label">Token状态:</span>
        <span :class="['value', token ? 'text-success' : 'text-danger']">
          {{ token ? '已设置' : '未设置或已过期' }}
        </span>
      </div>
      
      <!-- 操作按钮组 -->
      <el-button type="primary" @click="checkToken">检查Token</el-button>
      <el-button type="danger" @click="clearToken">清除Token</el-button>
      <el-button @click="testRequest">测试请求</el-button>
    </el-card>
    
    <!-- 请求结果显示弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="请求结果"
      width="80%"
    >
      <pre>{{ JSON.stringify(requestResult, null, 2) }}</pre>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { getDoctorInfo } from '@/api/doctor'
import { ElMessage } from 'element-plus'

/**
 * TokenChecker 组件
 * 
 * 开发调试工具，用于检查和管理用户认证Token
 * 主要功能：
 * - 显示当前Token状态
 * - 检查Token有效性
 * - 清除Token
 * - 测试API请求
 */
export default {
  name: 'TokenChecker',
  setup() {
    // 响应式数据定义
    const token = ref('')              // 当前Token值
    const dialogVisible = ref(false)   // 弹窗显示状态
    const requestResult = ref(null)    // 请求结果数据
    
    /**
     * 检查Token方法
     * 
     * 从本地存储获取Token并更新显示状态
     */
    const checkToken = () => {
      token.value = localStorage.getItem('doctorToken') || ''
      if (token.value) {
        ElMessage.success('Token已存在')
      } else {
        ElMessage.warning('Token不存在，请先登录')
      }
    }
    
    /**
     * 清除Token方法
     * 
     * 清除本地存储中的Token和用户信息
     */
    const clearToken = () => {
      localStorage.removeItem('doctorToken')
      localStorage.removeItem('doctorInfo')
      token.value = ''
      ElMessage.success('Token已清除')
    }
    
    /**
     * 测试请求方法
     * 
     * 发送API请求测试Token有效性
     * 并在弹窗中显示请求结果
     */
    const testRequest = async () => {
      try {
        const res = await getDoctorInfo()
        requestResult.value = res
        dialogVisible.value = true
        
        if (res.code === 200) {
          ElMessage.success('请求成功')
        } else {
          ElMessage.error(`请求失败: ${res.msg}`)
        }
      } catch (error) {
        // 捕获请求错误并显示详细信息
        requestResult.value = {
          error: error.message,
          stack: error.stack
        }
        dialogVisible.value = true
        ElMessage.error('请求出错')
      }
    }
    
    // 组件挂载时自动检查Token
    onMounted(() => {
      checkToken()
    })
    
    // 返回模板需要的数据和方法
    return {
      token,
      checkToken,
      clearToken,
      testRequest,
      dialogVisible,
      requestResult
    }
  }
}
</script>

<style scoped>
/* 组件根容器样式 */
.token-checker {
  padding: 20px;
}

/* 信息项样式 */
.info-item {
  margin-bottom: 15px;
}

/* 标签样式 */
.label {
  font-weight: bold;
  margin-right: 10px;
}

/* 值样式 */
.value {
  font-family: monospace;  /* 使用等宽字体 */
  word-break: break-all;   /* 长文本换行 */
}

/* 成功状态文本颜色 */
.text-success {
  color: #67C23A;
}

/* 错误状态文本颜色 */
.text-danger {
  color: #F56C6C;
}

/* 预格式化文本样式 */
pre {
  white-space: pre-wrap;   /* 保留空白字符并换行 */
  word-break: break-all;   /* 长单词换行 */
}
</style> 