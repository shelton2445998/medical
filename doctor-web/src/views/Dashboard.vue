<template>
  <div class="dashboard-container">
    <div class="header-section">
      <div class="welcome-section">
        <h2 class="page-title">医生工作台</h2>
        <div class="welcome-message">
          欢迎回来，<span class="doctor-name">{{ doctorName }}</span>！今天是<span class="current-date">{{ currentDate }}</span>
        </div>
      </div>
      <div class="date-time-section">
        <div class="time">{{ currentTime }}</div>
      </div>
    </div>
    
    <!-- 摘要信息卡片 -->
    <div class="summary-cards">
      <div class="summary-card">
        <div class="summary-icon" :class="{'active': stats.hasTodaySchedule}">
          <el-icon><calendar /></el-icon>
        </div>
        <div class="summary-info">
          <div class="summary-label">今日排班</div>
          <div class="summary-value" :class="{'success': stats.hasTodaySchedule, 'warning': !stats.hasTodaySchedule}">
            {{ stats.hasTodaySchedule ? '已排班' : '无排班' }}
          </div>
        </div>
      </div>
      
      <div class="summary-card">
        <div class="summary-icon report-icon">
          <el-icon><document /></el-icon>
        </div>
        <div class="summary-info">
          <div class="summary-label">待处理报告</div>
          <div class="summary-value">{{ stats.pendingReports || 0 }} <span class="summary-unit">份</span></div>
        </div>
      </div>
      
      <div class="summary-card">
        <div class="summary-icon month-icon">
          <el-icon><data-analysis /></el-icon>
        </div>
        <div class="summary-info">
          <div class="summary-label">本月体检报告</div>
          <div class="summary-value">{{ stats.monthReports || 0 }} <span class="summary-unit">份</span></div>
        </div>
      </div>
    </div>
    
    <!-- 主要内容区域 -->
    <el-row :gutter="24" class="main-content">
      <el-col :xs="24" :sm="24" :md="16" :lg="16" class="left-section">
        <!-- 最近体检报告 -->
        <el-card shadow="hover" class="dash-card recent-reports-card">
          <template #header>
            <div class="card-header">
              <span>最近体检报告</span>
              <el-button text @click="$router.push('/reports')">查看全部</el-button>
            </div>
          </template>
          <div v-if="recentReports.length === 0" class="empty-data">
            <el-empty description="暂无最近的体检报告"></el-empty>
          </div>
          <el-table v-else :data="recentReports" style="width: 100%" class="custom-table">
            <el-table-column prop="patientName" label="患者姓名" width="120">
              <template #default="scope">
                <span class="patient-name">{{ scope.row.patientName }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="itemName" label="检查项目" />
            <el-table-column prop="createTime" label="日期" width="180">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.conclusion ? 'success' : 'warning'" effect="light" size="small">
                  {{ scope.row.conclusion ? '已完成' : '待处理' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="24" :md="8" :lg="8" class="right-section">
        <!-- 今日工作卡片 -->
        <el-card shadow="hover" class="dash-card today-work-card">
          <template #header>
            <div class="card-header">
              <span>今日工作</span>
            </div>
          </template>
          <div class="work-timeline">
            <div class="timeline-item" v-for="(item, index) in workItems" :key="index">
              <div class="timeline-item-dot" :class="item.type"></div>
              <div class="timeline-item-content">
                <div class="timeline-item-title">{{ item.title }}</div>
                <div class="timeline-item-time">{{ item.time }}</div>
              </div>
            </div>
            
            <div v-if="workItems.length === 0" class="empty-data">
              <el-empty description="今日暂无安排"></el-empty>
            </div>
          </div>
        </el-card>
        
        <!-- 系统通知卡片 -->
        <el-card shadow="hover" class="dash-card notice-card">
          <template #header>
            <div class="card-header">
              <span>系统通知</span>
              <el-button text>全部</el-button>
            </div>
          </template>
          <div v-if="notices.length === 0" class="empty-data">
            <el-empty description="暂无系统通知"></el-empty>
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

    <div class="dashboard-footer">
      <div class="update-info">数据最后更新：{{ lastUpdateTime }}</div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, onUnmounted } from 'vue'
import { getDoctorDashboard } from '@/api/doctor'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Calendar, Document, DataAnalysis } from '@element-plus/icons-vue'

export default {
  name: 'DashboardView',
  components: {
    Calendar,
    Document,
    DataAnalysis
  },
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const doctorName = ref('')
    const lastUpdateTime = ref(new Date().toLocaleTimeString())
    const stats = ref({
      pendingReports: 0,
      hasTodaySchedule: false,
      monthReports: 0
    })
    
    // 时钟
    const currentTime = ref(new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }))
    const clockTimer = ref(null)
    
    // 数据
    const recentReports = ref([])
    const workItems = ref([])
    const notices = ref([])

    const currentDate = computed(() => {
      const date = new Date();
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const weekdayMap = ['日', '一', '二', '三', '四', '五', '六'];
      const weekday = weekdayMap[date.getDay()];
      return `${year}年${month}月${day}日 星期${weekday}`;
    })

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '-';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${padZero(date.getMonth() + 1)}-${padZero(date.getDate())}`;
    }
    
    const padZero = (num) => {
      return num < 10 ? `0${num}` : num;
    }

    // 从localStorage读取医生姓名
    const readDoctorName = () => {
      const doctorInfoStr = localStorage.getItem('doctorInfo')
      if (doctorInfoStr) {
        try {
          const doctorInfo = JSON.parse(doctorInfoStr)
          doctorName.value = doctorInfo && doctorInfo.username ? doctorInfo.username : '医生'
        } catch (e) {
          doctorName.value = '医生'
        }
      } else {
        doctorName.value = '医生'
      }
    }

    // 更新时钟
    const updateClock = () => {
      currentTime.value = new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
    }

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
          
          // 这里可以添加其他数据的处理
          if (res.data.recentReports) {
            recentReports.value = res.data.recentReports;
          }
          
          lastUpdateTime.value = new Date().toLocaleTimeString()
        }
      } catch (error) {
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

    onMounted(() => {
      // 读取医生姓名
      readDoctorName()
      
      // 加载仪表盘数据
      fetchDashboardData()
      
      // 启动时钟
      updateClock()
      clockTimer.value = setInterval(updateClock, 60000) // 每分钟更新一次
      
      // 设置自动刷新
      const refreshInterval = setInterval(fetchDashboardData, 5 * 60 * 1000)
      
      // 组件卸载时清除定时器
      onUnmounted(() => {
        clearInterval(clockTimer.value)
        clearInterval(refreshInterval)
      })
    })

    return {
      loading,
      stats,
      doctorName,
      currentDate,
      currentTime,
      lastUpdateTime,
      recentReports,
      workItems,
      notices,
      formatDate
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 24px;
  background-color: var(--background-color);
  min-height: calc(100vh - 84px);
  position: relative;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  
  .welcome-section {
    .page-title {
      margin: 0;
      font-size: 24px;
      font-weight: 600;
      color: var(--text-primary);
      line-height: 1.4;
    }
    
    .welcome-message {
      font-size: 14px;
      color: var(--text-secondary);
      margin-top: 8px;
      
      .doctor-name, .current-date {
        font-weight: 500;
        color: var(--primary-color);
      }
    }
  }
  
  .date-time-section {
    .time {
      font-size: 28px;
      font-weight: 600;
      color: var(--text-primary);
      background: linear-gradient(to right, var(--primary-color), #5B8EF9);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
}

// 摘要卡片样式
.summary-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  margin-bottom: 32px;
}

.summary-card {
  flex: 1;
  min-width: 220px;
  background: #fff;
  border-radius: var(--border-radius-md);
  padding: 24px;
  display: flex;
  align-items: center;
  box-shadow: var(--box-shadow-light);
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: var(--box-shadow);
  }
  
  .summary-icon {
    width: 56px;
    height: 56px;
    border-radius: 12px;
    background: rgba(62, 123, 250, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    
    i, svg {
      font-size: 28px;
      color: var(--primary-color);
    }
    
    &.active {
      background: rgba(82, 196, 26, 0.1);
      
      i, svg {
        color: var(--success-color);
      }
    }
    
    &.report-icon {
      background: rgba(250, 173, 20, 0.1);
      
      i, svg {
        color: var(--warning-color);
      }
    }
    
    &.month-icon {
      background: rgba(245, 34, 45, 0.1);
      
      i, svg {
        color: var(--danger-color);
      }
    }
  }
  
  .summary-info {
    .summary-label {
      font-size: 14px;
      color: var(--text-secondary);
      margin-bottom: 8px;
    }
    
    .summary-value {
      font-size: 24px;
      font-weight: 600;
      color: var(--text-primary);
      
      .summary-unit {
        font-size: 14px;
        font-weight: normal;
        color: var(--text-secondary);
      }
      
      &.success {
        color: var(--success-color);
      }
      
      &.warning {
        color: var(--warning-color);
      }
    }
  }
}

// 主要内容区域样式
.main-content {
  margin-bottom: 24px;
}

.dash-card {
  margin-bottom: 24px;
  border-radius: var(--border-radius-md);
  border: none;
  overflow: hidden;
  box-shadow: var(--box-shadow-light) !important;
  transition: transform 0.3s, box-shadow 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--box-shadow) !important;
  }
  
  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom: 1px solid var(--border-light);
    background-color: var(--background-light);
  }
  
  :deep(.el-card__body) {
    padding: 20px;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    span {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
    }
  }
}

.recent-reports-card {
  height: 100%;
  min-height: 400px;
  
  .custom-table {
    :deep(.el-table__row) {
      cursor: pointer;
      transition: background-color 0.2s;
      
      &:hover {
        background-color: var(--background-hover) !important;
      }
    }
    
    .patient-name {
      font-weight: 500;
      color: var(--primary-color);
    }
  }
}

// 工作时间线样式
.work-timeline {
  padding: 0 10px;
  
  .timeline-item {
    display: flex;
    margin-bottom: 20px;
    position: relative;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    &:not(:last-child):before {
      content: '';
      position: absolute;
      top: 24px;
      left: 6px;
      bottom: -20px;
      width: 1px;
      background: var(--border-color);
    }
    
    .timeline-item-dot {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: var(--primary-color);
      margin-right: 16px;
      margin-top: 6px;
      
      &.primary {
        background: var(--primary-color);
      }
      
      &.success {
        background: var(--success-color);
      }
      
      &.warning {
        background: var(--warning-color);
      }
      
      &.danger {
        background: var(--danger-color);
      }
    }
    
    .timeline-item-content {
      flex: 1;
      
      .timeline-item-title {
        font-size: 14px;
        color: var(--text-primary);
        margin-bottom: 4px;
      }
      
      .timeline-item-time {
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
}

// 通知列表样式
.notice-list {
  .notice-item {
    display: flex;
    justify-content: space-between;
    padding: 12px 0;
    border-bottom: 1px solid var(--border-light);
    
    &:last-child {
      border-bottom: none;
      padding-bottom: 0;
    }
    
    .notice-content {
      flex: 1;
      margin-right: 12px;
      
      .notice-title {
        font-size: 14px;
        color: var(--text-primary);
        margin-bottom: 6px;
        font-weight: 500;
      }
      
      .notice-desc {
        font-size: 12px;
        color: var(--text-secondary);
        line-height: 1.5;
      }
    }
    
    .notice-time {
      font-size: 12px;
      color: var(--text-placeholder);
      white-space: nowrap;
    }
  }
}

// 空数据样式
.empty-data {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  background-color: var(--background-light);
  border-radius: var(--border-radius-md);
}

// 页脚
.dashboard-footer {
  text-align: center;
  margin-top: 24px;
  
  .update-info {
    font-size: 12px;
    color: var(--text-secondary);
  }
}

// 响应式布局
@media (max-width: 768px) {
  .summary-cards {
    flex-direction: column;
    
    .summary-card {
      min-width: 100%;
    }
  }
  
  .header-section {
    flex-direction: column;
    align-items: flex-start;
    
    .date-time-section {
      margin-top: 16px;
    }
  }
}
</style>