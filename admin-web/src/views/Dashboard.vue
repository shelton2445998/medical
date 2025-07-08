<script>
export default {
  name: "AdminDashboard"
}
</script>

<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card class="stat-card blue-card">
          <div class="stat-icon">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-title">今日预约</p>
            <h2 class="stat-value">{{ dashboardData.todayAppointments }}</h2>
            <p class="stat-desc">总预约量 {{ dashboardData.totalAppointments }}</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card class="stat-card green-card">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-title">用户总数</p>
            <h2 class="stat-value">{{ dashboardData.userCount }}</h2>
            <p class="stat-desc">医生数量 {{ dashboardData.doctorCount }}</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card class="stat-card orange-card">
          <div class="stat-icon">
            <el-icon><Box /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-title">体检套餐</p>
            <h2 class="stat-value">{{ dashboardData.packageCount }}</h2>
            <p class="stat-desc">合作医院 {{ dashboardData.hospitalCount }}</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card class="stat-card purple-card">
          <div class="stat-icon">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-title">今日收入</p>
            <h2 class="stat-value">¥{{ formatNumber(dashboardData.todayIncome) }}</h2>
            <p class="stat-desc">总收入 ¥{{ formatNumber(dashboardData.totalIncome) }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <!-- 预约趋势图 -->
      <el-col :xs="24" :sm="24" :md="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>预约趋势</span>
              <el-radio-group v-model="dateRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">全年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container">
            <!-- 此处展示模拟数据图表 -->
            <div class="chart-data-display">
              <div class="chart-legend">
                <div v-for="(day, index) in dashboardData.appointmentChart.xData" :key="index" class="chart-day">
                  {{ day }}
                </div>
              </div>
              <div class="chart-bars">
                <div v-for="(value, index) in dashboardData.appointmentChart.yData" :key="index" class="chart-bar-container">
                  <div class="chart-bar" :style="{ height: value * 2 + 'px' }">{{ value }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 套餐预约分布 -->
      <el-col :xs="24" :sm="24" :md="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>套餐预约分布</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="rank-list">
              <div v-for="(item, index) in dashboardData.packageRank" :key="index" class="rank-item">
                <span class="rank-name">{{ item.name }}</span>
                <div class="rank-bar-container">
                  <div class="rank-bar" :style="{ width: (item.value / maxPackageRank * 100) + '%' }"></div>
                </div>
                <span class="rank-value">{{ item.value }}人</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 医院排名 -->
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>医院预约排名</span>
              <el-button type="text" @click="toHospitals">查看全部</el-button>
            </div>
          </template>
          <div class="rank-list">
            <div v-for="(item, index) in dashboardData.hospitalRank" :key="index" class="rank-item">
              <span class="rank-index">{{ index + 1 }}</span>
              <span class="rank-name">{{ item.name }}</span>
              <div class="rank-bar-container">
                <div class="rank-bar" :style="{ width: (item.value / maxHospitalRank * 100) + '%' }"></div>
              </div>
              <span class="rank-value">{{ item.value }}人</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 系统通知 -->
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>系统通知</span>
              <el-button type="text">全部已读</el-button>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item v-for="(notice, index) in systemNotices" :key="index" :timestamp="notice.time" placement="top">
              <div class="notice-content">
                <h4>{{ notice.title }}</h4>
                <p>{{ notice.content }}</p>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <!-- 演示模式提示 -->
    <div class="demo-mode-tip">
      <el-alert
        title="演示模式：当前显示的是静态演示数据"
        type="info"
        description="本系统目前处于演示模式，所有数据均为静态模拟数据，仅用于展示界面效果。"
        show-icon
        :closable="false"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { Calendar, User, Box, Money } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getDashboardData } from '../api/index'

const router = useRouter()
const dateRange = ref('week')
const dashboardData = reactive({
  todayAppointments: 0,
  totalAppointments: 0,
  userCount: 0,
  packageCount: 0,
  hospitalCount: 0,
  doctorCount: 0,
  todayIncome: 0,
  totalIncome: 0,
  appointmentChart: {
    xData: [],
    yData: []
  },
  packageRank: [],
  hospitalRank: []
})

// 计算最大值用于比例显示
const maxPackageRank = computed(() => {
  if (dashboardData.packageRank.length === 0) return 100
  return Math.max(...dashboardData.packageRank.map(item => item.value))
})

const maxHospitalRank = computed(() => {
  if (dashboardData.hospitalRank.length === 0) return 100
  return Math.max(...dashboardData.hospitalRank.map(item => item.value))
})

// 系统通知数据
const systemNotices = reactive([
  {
    title: '系统升级通知',
    content: '系统将于今晚23:00-次日凌晨2:00进行升级维护，请安排好工作',
    time: '10分钟前'
  },
  {
    title: '新增体检套餐',
    content: '系统已新增"青少年体检套餐"，请各科室做好准备',
    time: '2小时前'
  },
  {
    title: '端午节放假通知',
    content: '根据国家规定，端午节放假调休3天，具体安排请查看详情',
    time: '1天前'
  }
])

// 格式化数字（添加千位分隔符）
const formatNumber = (num) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

// 跳转到医院管理页面
const toHospitals = () => {
  router.push('/hospitals')
}

// 获取仪表盘数据
const fetchDashboardData = async () => {
  try {
    const res = await getDashboardData()
    if (res.data) {
      Object.assign(dashboardData, res.data)
    }
  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  border-radius: 8px;
}

.stat-icon {
  font-size: 48px;
  color: #fff;
  margin-right: 20px;
  width: 60px;
  text-align: center;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #fff;
  margin: 0;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
  margin: 8px 0;
}

.stat-desc {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.blue-card {
  background: linear-gradient(135deg, #1e88e5, #42a5f5);
}

.green-card {
  background: linear-gradient(135deg, #43a047, #66bb6a);
}

.orange-card {
  background: linear-gradient(135deg, #fb8c00, #ffa726);
}

.purple-card {
  background: linear-gradient(135deg, #8e24aa, #ab47bc);
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card, .data-card {
  margin-bottom: 20px;
  height: 350px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 280px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notice-content h4 {
  margin: 0 0 8px;
  font-weight: bold;
}

.notice-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

/* 图表样式 */
.chart-data-display {
  width: 100%;
  height: 100%;
  padding: 10px;
}

.chart-legend {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.chart-day {
  font-size: 12px;
  color: #666;
  text-align: center;
  width: 14%;
}

.chart-bars {
  display: flex;
  justify-content: space-between;
  height: 220px;
  align-items: flex-end;
}

.chart-bar-container {
  width: 14%;
  text-align: center;
}

.chart-bar {
  background: linear-gradient(180deg, #409eff, #a0cfff);
  border-radius: 4px 4px 0 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 5px;
  font-size: 12px;
  color: #fff;
  transition: height 0.5s;
  min-height: 20px;
}

/* 排名样式 */
.rank-list {
  width: 100%;
  padding: 10px;
}

.rank-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.rank-index {
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  background-color: #f0f0f0;
  border-radius: 50%;
  margin-right: 10px;
  font-size: 12px;
}

.rank-item:nth-child(1) .rank-index {
  background-color: #f44336;
  color: white;
}

.rank-item:nth-child(2) .rank-index {
  background-color: #ff9800;
  color: white;
}

.rank-item:nth-child(3) .rank-index {
  background-color: #ffc107;
  color: white;
}

.rank-name {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
}

.rank-bar-container {
  width: 40%;
  height: 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 10px;
}

.rank-bar {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #a0cfff);
  transition: width 0.5s;
}

.rank-value {
  width: 60px;
  text-align: right;
  font-size: 14px;
  color: #666;
}

.demo-mode-tip {
  margin-top: 20px;
}
</style> 