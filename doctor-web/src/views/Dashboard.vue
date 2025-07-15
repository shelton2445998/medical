<template>
  <div class="dashboard-container">
    <div class="header-section">
      <h2 class="page-title">医生工作台</h2>
      <div class="welcome-message">欢迎回来，{{ doctorName }}医生！今天是{{ currentDate }}</div>
    </div>
    
    <!-- Token检查工具 -->
    <TokenChecker />
    
    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card today-appointments" shadow="hover">
          <div class="stat-card-content">
            <div class="stat-card-title">今日预约</div>
            <div class="stat-card-value">{{ stats.todayAppointments || 0 }}</div>
            <div class="stat-card-time">最后更新: {{ lastUpdateTime }}</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card pending-appointments" shadow="hover">
          <div class="stat-card-content">
            <div class="stat-card-title">待处理预约</div>
            <div class="stat-card-value">{{ stats.pendingAppointments || 0 }}</div>
            <div class="stat-card-time">最后更新: {{ lastUpdateTime }}</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card week-appointments" shadow="hover">
          <div class="stat-card-content">
            <div class="stat-card-title">本周预约</div>
            <div class="stat-card-value">{{ stats.weekAppointments || 0 }}</div>
            <div class="stat-card-time">最后更新: {{ lastUpdateTime }}</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card month-appointments" shadow="hover">
          <div class="stat-card-content">
            <div class="stat-card-title">本月预约</div>
            <div class="stat-card-value">{{ stats.monthAppointments || 0 }}</div>
            <div class="stat-card-time">最后更新: {{ lastUpdateTime }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { getDoctorDashboard } from '@/api/doctor'
import TokenChecker from '@/components/TokenChecker.vue'

export default {
  name: 'DashboardView',
  components: {
    TokenChecker
  },
  setup() {
    const loading = ref(false)
    const doctorName = ref('医生')
    const lastUpdateTime = ref(new Date().toLocaleTimeString())
    const stats = ref({
      todayAppointments: 0,
      pendingAppointments: 0,
      weekAppointments: 0,
      monthAppointments: 0
    })

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
            todayAppointments: res.data.todayAppointments || 0,
            pendingAppointments: res.data.pendingAppointments || 0,
            weekAppointments: res.data.weekAppointments || 0,
            monthAppointments: res.data.monthAppointments || 0
          }
          lastUpdateTime.value = new Date().toLocaleTimeString()
        }
      } catch (error) {
        console.error('获取仪表盘数据失败:', error)
        // 可以添加错误提示
      } finally {
        loading.value = false
      }
    }

    // 每5分钟自动刷新数据
    onMounted(() => {
      fetchDashboardData()
      const refreshInterval = setInterval(fetchDashboardData, 5 * 60 * 1000)
      
      // 组件卸载时清除定时器
      return () => clearInterval(refreshInterval)
    })

    return {
      loading,
      stats,
      doctorName,
      currentDate,
      lastUpdateTime
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
  
  &.today-appointments {
    background: linear-gradient(135deg, #f0f7ff, #ffffff);
    border-left: 4px solid #409EFF;
    
    .stat-card-value {
      color: #409EFF;
    }
  }
  
  &.pending-appointments {
    background: linear-gradient(135deg, #fff8f0, #ffffff);
    border-left: 4px solid #E6A23C;
    
    .stat-card-value {
      color: #E6A23C;
    }
  }
  
  &.week-appointments {
    background: linear-gradient(135deg, #f0fff4, #ffffff);
    border-left: 4px solid #67C23A;
    
    .stat-card-value {
      color: #67C23A;
    }
  }
  
  &.month-appointments {
    background: linear-gradient(135deg, #fff0f6, #ffffff);
    border-left: 4px solid #F56C6C;
    
    .stat-card-value {
      color: #F56C6C;
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
}
</style>