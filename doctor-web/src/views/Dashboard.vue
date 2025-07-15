<template>
  <div class="dashboard-container">
    <div class="header-section">
      <h2 class="page-title">医生工作台</h2>
      <div class="welcome-message">欢迎回来，{{ doctorName }}医生！今天是{{ currentDate }}</div>
    </div>
    
    <!-- 状态卡片 -->
    <el-row :gutter="20" class="status-row">
      <!-- 左侧状态卡片 -->
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card class="status-card schedule-status" shadow="hover">
          <div class="status-card-content">
            <div class="status-card-icon">
              <i :class="stats.hasTodaySchedule ? 'el-icon-check' : 'el-icon-close'"></i>
            </div>
            <div class="status-card-info">
              <div class="status-card-title">今日排班状态</div>
              <div :class="['status-card-value', stats.hasTodaySchedule ? 'text-success' : 'text-warning']">
                {{ stats.hasTodaySchedule ? '今日有排班' : '今日无排班' }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧待处理报告卡片 -->
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card class="status-card pending-reports" shadow="hover">
          <div class="status-card-content">
            <div class="status-card-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="status-card-info">
              <div class="status-card-title">待处理报告</div>
              <div class="status-card-value">{{ stats.pendingReports || 0 }} 份</div>
              <el-button v-if="stats.pendingReports > 0" size="small" type="primary" @click="goToReports">
                去处理
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 报告统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="24" :md="24" :lg="24">
        <el-card class="stat-card month-reports" shadow="hover">
          <div class="stat-card-content">
            <div class="stat-card-title">本月体检报告数</div>
            <div class="stat-card-value">{{ stats.monthReports || 0 }}</div>
            <div class="stat-card-time">最后更新: {{ lastUpdateTime }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统通知 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="notice-card" shadow="hover">
          <template #header>
            <div class="notice-header">
              <span>系统通知</span>
              <el-button type="text">查看全部</el-button>
            </div>
          </template>
          <div v-if="notices.length === 0" class="empty-notice">
            <i class="el-icon-bell"></i>
            <p>暂无通知</p>
          </div>
          <div v-else class="notice-list">
            <div v-for="(notice, index) in notices" :key="index" class="notice-item">
              <div class="notice-content">
                <div class="notice-title">{{ notice.title }}</div>
                <div class="notice-desc">{{ notice.content }}</div>
              </div>
              <div class="notice-time">{{ notice.time }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { getDoctorDashboard } from '@/api/doctor'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

export default {
  name: 'DashboardView',
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const doctorName = ref('医生')
    const lastUpdateTime = ref(new Date().toLocaleTimeString())
    const stats = ref({
      pendingReports: 0,
      hasTodaySchedule: false,
      monthReports: 0
    })
    
    // 模拟通知数据
    const notices = ref([])

    const currentDate = computed(() => {
      const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }
      return new Date().toLocaleDateString('zh-CN', options)
    })

    const fetchDashboardData = async () => {
      loading.value = true
      try {
        const res = await getDoctorDashboard()
        if (res.code === 200) {
          // 确保后端返回了所有需要的字段
          stats.value = {
            pendingReports: res.data.pendingReports || 0,
            hasTodaySchedule: res.data.hasTodaySchedule || false,
            monthReports: res.data.monthReports || 0
          }
          lastUpdateTime.value = new Date().toLocaleTimeString()
        }
      } catch (error) {
        console.error('获取仪表盘数据失败:', error)
        ElMessage.error('获取仪表盘数据失败，请检查网络或登录状态')
        
        // 如果是401错误，可能是token失效，跳转到登录页
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          localStorage.removeItem('doctorToken')
          localStorage.removeItem('doctorInfo')
          setTimeout(() => {
            router.push('/login')
          }, 1500)
        }
      } finally {
        loading.value = false
      }
    }
    
    const goToReports = () => {
      router.push('/reports/pending')
    }

    // 每5分钟自动刷新数据
    onMounted(() => {
      // 获取登录医生的姓名
      const doctorInfoStr = localStorage.getItem('doctorInfo')
      if (doctorInfoStr) {
        try {
          const doctorInfo = JSON.parse(doctorInfoStr)
          if (doctorInfo && doctorInfo.username) {
            doctorName.value = doctorInfo.username
          }
        } catch (e) {
          console.error('解析医生信息失败', e)
        }
      }
      
      // 加载仪表盘数据
      fetchDashboardData()
      
      // 设置自动刷新
      const refreshInterval = setInterval(fetchDashboardData, 5 * 60 * 1000)
      
      // 组件卸载时清除定时器
      return () => clearInterval(refreshInterval)
    })

    return {
      loading,
      stats,
      doctorName,
      currentDate,
      lastUpdateTime,
      goToReports,
      notices
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 64px);
}

.header-section {
  margin-bottom: 24px;
  
  .page-title {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: #303133;
    line-height: 1.5;
  }
  
  .welcome-message {
    font-size: 14px;
    color: #909399;
    margin-top: 8px;
  }
}

.status-row {
  margin-bottom: 20px;
  
  .el-col {
    margin-bottom: 20px;
  }
}

.status-card {
  height: 100px;
  border-radius: 8px;
  border: none;
  
  .status-card-content {
    display: flex;
    align-items: center;
    height: 100%;
    
    .status-card-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background-color: rgba(64, 158, 255, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20px;
      
      i {
        font-size: 30px;
        color: #409EFF;
      }
    }
    
    .status-card-info {
      flex: 1;
      
      .status-card-title {
        font-size: 16px;
        color: #909399;
        margin-bottom: 10px;
      }
      
      .status-card-value {
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 10px;
      }
      
      .text-success {
        color: #67C23A;
      }
      
      .text-warning {
        color: #E6A23C;
      }
    }
  }
  
  &.schedule-status .status-card-icon {
    background-color: rgba(103, 194, 58, 0.1);
    
    i {
      color: #67C23A;
    }
  }
  
  &.pending-reports .status-card-icon {
    background-color: rgba(230, 162, 60, 0.1);
    
    i {
      color: #E6A23C;
    }
  }
}

.stat-row {
  margin-bottom: 20px;
  
  .el-col {
    margin-bottom: 20px;
  }
}

.stat-card {
  height: 120px;
  border-radius: 8px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  
  .stat-card-content {
    text-align: center;
    
    .stat-card-title {
      font-size: 16px;
      color: #909399;
      margin-bottom: 12px;
    }
    
    .stat-card-value {
      font-size: 32px;
      font-weight: bold;
      margin-bottom: 8px;
      line-height: 1;
    }
    
    .stat-card-time {
      font-size: 12px;
      color: #c0c4cc;
    }
  }
  
  &.month-reports {
    background: linear-gradient(135deg, #fff0f6, #ffffff);
    border-left: 4px solid #F56C6C;
    
    .stat-card-value {
      color: #F56C6C;
    }
  }
}

.notice-card {
  margin-bottom: 20px;
  border-radius: 8px;
  
  .notice-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    span {
      font-size: 16px;
      font-weight: 600;
    }
  }
  
  .empty-notice {
    text-align: center;
    padding: 30px 0;
    color: #909399;
    
    i {
      font-size: 40px;
      margin-bottom: 10px;
    }
  }
  
  .notice-list {
    .notice-item {
      display: flex;
      justify-content: space-between;
      padding: 12px 0;
      border-bottom: 1px solid #EBEEF5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .notice-content {
        .notice-title {
          font-size: 14px;
          color: #303133;
          margin-bottom: 5px;
        }
        
        .notice-desc {
          font-size: 12px;
          color: #909399;
        }
      }
      
      .notice-time {
        font-size: 12px;
        color: #C0C4CC;
      }
    }
  }
}

@media (max-width: 768px) {
  .stat-card {
    height: 100px;
    
    .stat-card-value {
      font-size: 28px;
    }
  }
  
  .status-card {
    height: auto;
    padding: 15px;
    
    .status-card-content {
      flex-direction: column;
      text-align: center;
      
      .status-card-icon {
        margin-right: 0;
        margin-bottom: 10px;
      }
    }
  }
}
</style>