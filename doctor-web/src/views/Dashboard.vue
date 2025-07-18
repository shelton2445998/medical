<!--
  @fileoverview 医生工作台页面组件
  @description 提供医生工作台的概览信息，包括今日排班、待处理报告、最近体检报告等，为医生提供快速了解工作状态的入口
  @author 医生端项目组
  @version 1.0.0
  @since 2024-01-01
  
  @features
  - 工作台概览信息
  - 今日排班状态
  - 待处理报告统计
  - 最近体检报告列表
  - 今日工作时间线
  - 系统通知展示
  - 实时时钟显示
  - 响应式布局设计
  
  @dependencies
  - Vue 3: 前端框架
  - Element Plus: UI组件库
  - Vue Router: 路由管理
  - doctor API: 医生相关接口
  
  @usage
  作为医生端的主要工作台页面，提供工作状态概览
  
  @performance
  - 数据懒加载
  - 组件按需渲染
  - 定时器优化
  - 响应式设计
  
  @maintenance
  - 模块化组件设计
  - 清晰的数据流
  - 便于功能扩展
-->
<template>
  <div class="dashboard-container">
    <!-- 头部区域 -->
    <div class="header-section">
      <!-- 欢迎信息区域 -->
      <div class="welcome-section">
        <h2 class="page-title">医生工作台</h2>
        <div class="welcome-message">
          欢迎回来，<span class="doctor-name">{{ doctorName }}</span>！今天是<span class="current-date">{{ currentDate }}</span>
        </div>
      </div>
      
      <!-- 时间显示区域 -->
      <div class="date-time-section">
        <div class="time">{{ currentTime }}</div>
      </div>
    </div>
    
    <!-- 摘要信息卡片 -->
    <div class="summary-cards">
      <!-- 今日排班卡片 -->
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
      
      <!-- 待处理报告卡片 -->
      <div class="summary-card">
        <div class="summary-icon report-icon">
          <el-icon><document /></el-icon>
        </div>
        <div class="summary-info">
          <div class="summary-label">待处理报告</div>
          <div class="summary-value">{{ stats.pendingReports || 0 }} <span class="summary-unit">份</span></div>
        </div>
      </div>
      
      <!-- 本月体检报告卡片 -->
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
      <!-- 左侧区域 -->
      <el-col :xs="24" :sm="24" :md="16" :lg="16" class="left-section">
        <!-- 最近体检报告卡片 -->
        <el-card shadow="hover" class="dash-card recent-reports-card">
          <template #header>
            <div class="card-header">
              <span>最近体检报告</span>
              <el-button text @click="$router.push('/reports')">查看全部</el-button>
            </div>
          </template>
          
          <!-- 空数据状态 -->
          <div v-if="recentReports.length === 0" class="empty-data">
            <el-empty description="暂无最近的体检报告"></el-empty>
          </div>
          
          <!-- 体检报告列表 -->
          <el-table v-else :data="recentReports" style="width: 100%" class="custom-table">
            <!-- 患者姓名列 -->
            <el-table-column prop="patientName" label="患者姓名" width="120">
              <template #default="scope">
                <span class="patient-name">{{ scope.row.patientName }}</span>
              </template>
            </el-table-column>
            
            <!-- 检查项目列 -->
            <el-table-column prop="itemName" label="检查项目" />
            
            <!-- 日期列 -->
            <el-table-column prop="createTime" label="日期" width="180">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            
            <!-- 状态列 -->
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
      
      <!-- 右侧区域 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" class="right-section">
        <!-- 今日工作卡片 -->
        <el-card shadow="hover" class="dash-card today-work-card">
          <template #header>
            <div class="card-header">
              <span>今日工作</span>
            </div>
          </template>
          
          <!-- 工作时间线 -->
          <div class="work-timeline">
            <div class="timeline-item" v-for="(item, index) in workItems" :key="index">
              <div class="timeline-item-dot" :class="item.type"></div>
              <div class="timeline-item-content">
                <div class="timeline-item-title">{{ item.title }}</div>
                <div class="timeline-item-time">{{ item.time }}</div>
              </div>
            </div>
            
            <!-- 空数据状态 -->
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
          
          <!-- 空数据状态 -->
          <div v-if="notices.length === 0" class="empty-data">
            <el-empty description="暂无系统通知"></el-empty>
          </div>
          
          <!-- 通知列表 -->
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

    <!-- 工作台页脚 -->
    <div class="dashboard-footer">
      <div class="update-info">数据最后更新：{{ lastUpdateTime }}</div>
    </div>
  </div>
</template>

<script>
/**
 * 医生工作台页面逻辑
 * 
 * 处理工作台数据获取、时钟显示、统计信息等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, onMounted, computed, onUnmounted } from 'vue'
// 导入API接口函数
import { getDoctorDashboard } from '@/api/doctor'
// 导入路由相关函数
import { useRouter } from 'vue-router'
// 导入 Element Plus 组件
import { ElMessage } from 'element-plus'
// 导入 Element Plus 图标组件
import { Calendar, Document, DataAnalysis } from '@element-plus/icons-vue'

/**
 * 医生工作台组件导出
 * 
 * 定义组件的基本信息和逻辑
 */
export default {
  name: 'DashboardView',
  // 注册使用的图标组件
  components: {
    Calendar,
    Document,
    DataAnalysis
  },
  setup() {
    // 路由实例
    const router = useRouter()
    
    // 基础状态
    const loading = ref(false)                                        // 加载状态
    const doctorName = ref('')                                        // 医生姓名
    const lastUpdateTime = ref(new Date().toLocaleTimeString())       // 最后更新时间
    
    /**
     * 统计信息响应式对象
     * 
     * 存储工作台的各项统计数据
     */
    const stats = ref({
      pendingReports: 0,      // 待处理报告数量
      hasTodaySchedule: false, // 今日是否有排班
      monthReports: 0         // 本月体检报告数量
    })
    
    // 时钟相关
    const currentTime = ref(new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }))  // 当前时间
    const clockTimer = ref(null)                                      // 时钟定时器
    
    // 数据列表
    const recentReports = ref([])                                     // 最近体检报告
    const workItems = ref([])                                         // 今日工作项
    const notices = ref([])                                           // 系统通知

    /**
     * 当前日期计算属性
     * 
     * 格式化当前日期为中文格式
     */
    const currentDate = computed(() => {
      const date = new Date();
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const weekdayMap = ['日', '一', '二', '三', '四', '五', '六'];
      const weekday = weekdayMap[date.getDay()];
      return `${year}年${month}月${day}日 星期${weekday}`;
    })

    /**
     * 格式化日期
     * 
     * 将日期字符串格式化为 YYYY-MM-DD 格式
     * 
     * @param {string} dateString - 日期字符串
     * @returns {string} 格式化后的日期
     */
    const formatDate = (dateString) => {
      if (!dateString) return '-';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${padZero(date.getMonth() + 1)}-${padZero(date.getDate())}`;
    }
    
    /**
     * 数字补零
     * 
     * 将单位数字前面补零
     * 
     * @param {number} num - 要补零的数字
     * @returns {string} 补零后的字符串
     */
    const padZero = (num) => {
      return num < 10 ? `0${num}` : num;
    }

    /**
     * 从localStorage读取医生姓名
     * 
     * 读取本地存储中的医生信息并设置姓名
     */
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

    /**
     * 更新时钟
     * 
     * 更新当前时间显示
     */
    const updateClock = () => {
      currentTime.value = new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
    }

    /**
     * 获取工作台数据
     * 
     * 从服务器获取工作台的统计数据和报告列表
     */
    const fetchDashboardData = async () => {
      loading.value = true
      try {
        const res = await getDoctorDashboard()
        if (res.code === 200) {
          // 确保后端返回了所有需要的字段，设置统计数据
          stats.value = {
            pendingReports: res.data.pendingReports || 0,
            hasTodaySchedule: res.data.hasTodaySchedule || false,
            monthReports: res.data.monthReports || 0
          }
          
          // 处理最近体检报告数据
          if (res.data.recentReports) {
            recentReports.value = res.data.recentReports;
          }
          
          // 更新数据更新时间
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

    /**
     * 组件挂载时的初始化逻辑
     * 
     * 初始化医生姓名、加载数据、启动时钟和自动刷新
     */
    onMounted(() => {
      // 读取医生姓名
      readDoctorName()
      
      // 加载仪表盘数据
      fetchDashboardData()
      
      // 启动时钟
      updateClock()
      clockTimer.value = setInterval(updateClock, 60000) // 每分钟更新一次
      
      // 设置自动刷新（每5分钟）
      const refreshInterval = setInterval(fetchDashboardData, 5 * 60 * 1000)
      
      // 组件卸载时清除定时器
      onUnmounted(() => {
        clearInterval(clockTimer.value)
        clearInterval(refreshInterval)
      })
    })

    // 返回模板需要的数据和方法
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

<!--
  医生工作台页面样式
  
  定义工作台页面的视觉设计和布局
  使用 SCSS 语法编写样式
-->
<style scoped lang="scss">
/* ==================== 页面容器样式 ==================== */

/**
 * 工作台页面主容器
 * 
 * 提供页面的基础布局和背景
 */
.dashboard-container {
  padding: 24px;                                   /* 内边距 */
  background-color: var(--background-color);       /* 背景色 */
  min-height: calc(100vh - 84px);                  /* 最小高度 */
  position: relative;                              /* 相对定位 */
}

/* ==================== 头部区域样式 ==================== */

/**
 * 头部区域样式
 * 
 * 包含欢迎信息和时间显示
 */
.header-section {
  display: flex;                                   /* 弹性布局 */
  justify-content: space-between;                  /* 两端对齐 */
  align-items: center;                             /* 垂直居中 */
  margin-bottom: 32px;                             /* 底部边距 */
  
  /**
   * 欢迎信息区域样式
   * 
   * 显示页面标题和欢迎消息
   */
  .welcome-section {
    /**
     * 页面标题样式
     * 
     * 工作台页面的主标题
     */
    .page-title {
      margin: 0;                                   /* 无边距 */
      font-size: 24px;                            /* 字体大小 */
      font-weight: 600;                            /* 字体粗细 */
      color: var(--text-primary);                 /* 主要文本颜色 */
      line-height: 1.4;                           /* 行高 */
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