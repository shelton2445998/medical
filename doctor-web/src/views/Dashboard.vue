<template>
  <div class="dashboard-container">
    <h2 class="page-title">医生工作台</h2>
    
    <!-- Token检查工具 -->
    <TokenChecker />
    
    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-card-title">今日预约</div>
          <div class="stat-card-value">{{ stats.todayAppointments || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-card-title">待处理预约</div>
          <div class="stat-card-value">{{ stats.pendingAppointments || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-card-title">本周预约</div>
          <div class="stat-card-value">{{ stats.weekAppointments || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-card-title">本月预约</div>
          <div class="stat-card-value">{{ stats.monthAppointments || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { getDoctorDashboard } from '@/api/doctor'
import TokenChecker from '@/components/TokenChecker.vue'

export default {
  name: 'DashboardView',
  components: {
    TokenChecker
  },
  setup() {
    const loading = ref(false)
    const stats = ref({
      todayAppointments: 0,
      pendingAppointments: 0,
      weekAppointments: 0,
      monthAppointments: 0
    })

    const fetchDashboardData = async () => {
      loading.value = true
      try {
        const res = await getDoctorDashboard()
        if (res.code === 200) {
          stats.value = res.data
        }
      } catch (error) {
        console.error('获取仪表盘数据失败:', error)
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      fetchDashboardData()
    })

    return {
      loading,
      stats
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.stat-row {
  margin-top: 20px;
}

.stat-card {
  text-align: center;
}

.stat-card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-card-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}
</style>