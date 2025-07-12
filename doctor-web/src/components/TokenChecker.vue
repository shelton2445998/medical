<template>
  <div class="token-checker">
    <h3>Token检查工具</h3>
    <el-card>
      <div class="info-item">
        <span class="label">当前token值:</span>
        <span class="value">{{ token || '未设置' }}</span>
      </div>
      <div class="info-item">
        <span class="label">Token状态:</span>
        <span :class="['value', token ? 'text-success' : 'text-danger']">
          {{ token ? '已设置' : '未设置或已过期' }}
        </span>
      </div>
      <el-button type="primary" @click="checkToken">检查Token</el-button>
      <el-button type="danger" @click="clearToken">清除Token</el-button>
      <el-button @click="testRequest">测试请求</el-button>
    </el-card>
    
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

export default {
  name: 'TokenChecker',
  setup() {
    const token = ref('')
    const dialogVisible = ref(false)
    const requestResult = ref(null)
    
    const checkToken = () => {
      token.value = localStorage.getItem('doctorToken') || ''
      if (token.value) {
        ElMessage.success('Token已存在')
      } else {
        ElMessage.warning('Token不存在，请先登录')
      }
    }
    
    const clearToken = () => {
      localStorage.removeItem('doctorToken')
      localStorage.removeItem('doctorInfo')
      token.value = ''
      ElMessage.success('Token已清除')
    }
    
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
        requestResult.value = {
          error: error.message,
          stack: error.stack
        }
        dialogVisible.value = true
        ElMessage.error('请求出错')
      }
    }
    
    onMounted(() => {
      checkToken()
    })
    
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
.token-checker {
  padding: 20px;
}
.info-item {
  margin-bottom: 15px;
}
.label {
  font-weight: bold;
  margin-right: 10px;
}
.value {
  font-family: monospace;
  word-break: break-all;
}
.text-success {
  color: #67C23A;
}
.text-danger {
  color: #F56C6C;
}
pre {
  white-space: pre-wrap;
  word-break: break-all;
}
</style> 