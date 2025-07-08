<script>
export default {
  name: "AdminDashboard"
}
</script>

<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6" v-for="(card, index) in statCards" :key="index">
        <el-card class="stat-card" :class="card.class">
          <div class="stat-icon">
            <component :is="card.icon"></component>
          </div>
          <div class="stat-info">
            <p class="stat-title">{{ card.title }}</p>
            <h2 class="stat-value">{{ card.value }}</h2>
            <p class="stat-desc">{{ card.desc }}</p>
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
            <!-- 此处可集成ECharts等图表库 -->
            <div class="placeholder-chart">预约趋势图表</div>
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
            <!-- 此处可集成ECharts等图表库 -->
            <div class="placeholder-chart">套餐分布图表</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 最新预约 -->
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>最新预约</span>
              <el-button type="text">查看更多</el-button>
            </div>
          </template>
          <el-table :data="latestAppointments" stripe style="width: 100%" size="small">
            <el-table-column prop="userName" label="用户" width="100" />
            <el-table-column prop="packageName" label="套餐" />
            <el-table-column prop="appointmentTime" label="预约时间" width="180" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

// 日期范围
const dateRange = ref('week')

// 统计卡片数据
const statCards = reactive([
  {
    title: '今日预约',
    value: '128',
    desc: '同比增长 24%',
    class: 'blue-card',
    icon: 'el-icon-date'
  },
  {
    title: '用户总数',
    value: '8,846',
    desc: '本月新增 235',
    class: 'green-card',
    icon: 'el-icon-user'
  },
  {
    title: '体检完成',
    value: '93',
    desc: '完成率 72.6%',
    class: 'orange-card',
    icon: 'el-icon-finished'
  },
  {
    title: '月营收',
    value: '¥89,325',
    desc: '同比增长 12%',
    class: 'purple-card',
    icon: 'el-icon-money'
  }
])

// 最新预约数据
const latestAppointments = reactive([
  {
    userName: '张三',
    packageName: '全面体检套餐',
    appointmentTime: '2023-07-15 09:00',
    status: '待体检'
  },
  {
    userName: '李四',
    packageName: '入职体检套餐',
    appointmentTime: '2023-07-15 10:30',
    status: '已完成'
  },
  {
    userName: '王五',
    packageName: '高级体检套餐',
    appointmentTime: '2023-07-16 08:00',
    status: '已取消'
  },
  {
    userName: '赵六',
    packageName: '女性体检套餐',
    appointmentTime: '2023-07-16 14:30',
    status: '待支付'
  },
  {
    userName: '钱七',
    packageName: '心脑血管检查',
    appointmentTime: '2023-07-17 09:30',
    status: '待体检'
  }
])

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

// 获取状态对应的Tag类型
const getStatusType = (status) => {
  const statusMap = {
    '待支付': 'info',
    '待体检': 'warning',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return statusMap[status] || 'info'
}

// 组件挂载时加载数据
onMounted(() => {
  // 这里可以调用API获取实时数据
  console.log('仪表盘组件已挂载，可以请求实时数据')
})
</script>

<style scoped>
.dashboard-container {
  padding: 10px;
}

.stat-card {
  height: 120px;
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  color: white;
  border-radius: 8px;
}

.blue-card {
  background: linear-gradient(90deg, #1890ff, #36cbcb);
}

.green-card {
  background: linear-gradient(90deg, #52c41a, #7cb305);
}

.orange-card {
  background: linear-gradient(90deg, #fa8c16, #ffd666);
}

.purple-card {
  background: linear-gradient(90deg, #722ed1, #b37feb);
}

.stat-icon {
  width: 60px;
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 32px;
  margin: 0 10px;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  margin: 0;
}

.stat-value {
  font-size: 24px;
  margin: 5px 0;
}

.stat-desc {
  font-size: 12px;
  opacity: 0.8;
  margin: 0;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card, .data-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.placeholder-chart {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 16px;
  border-radius: 4px;
}

.notice-content {
  margin-bottom: 10px;
}

.notice-content h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
}

.notice-content p {
  margin: 0;
  color: #606266;
  font-size: 12px;
}
</style> 