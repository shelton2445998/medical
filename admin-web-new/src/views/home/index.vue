<!--
/**
 * @fileoverview 管理后台首页
 * @description 显示系统概览信息，包括统计数据、图表展示、快速操作等
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 系统欢迎界面
 * - 统计数据卡片展示
 * - 预约趋势图表
 * - 待办事项列表
 * - 快速操作按钮
 * - 实时数据刷新
 * - 响应式布局
 */
-->
<template>
  <div class="home-container">
    <div class="welcome-section">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card shadow="hover" class="welcome-card">
            <div class="welcome-content">
              <div class="welcome-text">
                <h2>欢迎使用医疗预约系统管理后台</h2>
                <p>{{ currentTime }}</p>
              </div>
              <div class="welcome-action">
                <el-button type="primary" @click="refreshData">
                  <el-icon class="mr-5"><Refresh /></el-icon>刷新数据
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="12" :lg="6" v-for="(item, index) in statCards" :key="index">
          <el-card shadow="hover" class="stat-card" :class="`stat-card-${index}`">
            <div class="stat-card-content">
              <div class="stat-icon">
                <el-icon><component :is="item.icon" /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ item.value }}</div>
                <div class="stat-title">{{ item.title }}</div>
              </div>
            </div>
            <div class="stat-footer">
              <span>{{ item.change >= 0 ? '↑' : '↓' }} {{ Math.abs(item.change) }}% 较上周</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :xs="24" :md="12">
          <el-card shadow="hover" class="chart-card">
            <template #header>
              <div class="card-header">
                <span>预约趋势</span>
                <el-radio-group v-model="appointmentChartPeriod" size="small">
                  <el-radio-button label="week">本周</el-radio-button>
                  <el-radio-button label="month">本月</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-placeholder">
              <div class="chart-bars">
                <div v-for="(value, index) in appointmentTrend" :key="index" class="chart-bar">
                  <div class="bar-value" :style="{height: `${value}%`}"></div>
                  <div class="bar-label">{{ getDayLabel(index) }}</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :md="12">
          <el-card shadow="hover" class="chart-card">
            <template #header>
              <div class="card-header">
                <span>科室分布</span>
                <el-button type="text" @click="refreshData">详情</el-button>
              </div>
            </template>
            <div class="chart-placeholder pie-chart">
              <div class="pie-segments">
                <div class="pie-segment" style="transform: rotate(0deg); background-color: #409EFF; clip-path: polygon(50% 50%, 50% 0%, 100% 0%, 100% 50%)"></div>
                <div class="pie-segment" style="transform: rotate(90deg); background-color: #67C23A; clip-path: polygon(50% 50%, 50% 0%, 100% 0%, 100% 30%)"></div>
                <div class="pie-segment" style="transform: rotate(135deg); background-color: #E6A23C; clip-path: polygon(50% 50%, 50% 0%, 100% 0%, 100% 55%)"></div>
                <div class="pie-segment" style="transform: rotate(230deg); background-color: #F56C6C; clip-path: polygon(50% 50%, 50% 0%, 100% 0%, 80% 40%)"></div>
              </div>
              <div class="pie-legend">
                <div class="legend-item" v-for="(item, index) in departmentData" :key="index">
                  <div class="legend-color" :style="{backgroundColor: item.color}"></div>
                  <div class="legend-label">{{ item.name }}</div>
                  <div class="legend-value">{{ item.value }}%</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="recent-section">
      <el-row :gutter="20">
        <el-col :xs="24" :lg="12">
          <el-card shadow="hover" class="recent-card">
            <template #header>
              <div class="card-header">
                <span>最近预约</span>
                <el-button type="text" @click="viewMoreAppointments">查看更多</el-button>
              </div>
            </template>
            <el-table :data="recentAppointments" style="width: 100%" size="large">
              <el-table-column prop="name" label="患者姓名" min-width="100" />
              <el-table-column prop="department" label="科室" min-width="120" />
              <el-table-column prop="doctor" label="医生" min-width="100" />
              <el-table-column prop="time" label="预约时间" min-width="160" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag
                    :type="getStatusType(scope.row.status)"
                    effect="light"
                  >
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :xs="24" :lg="12">
          <el-card shadow="hover" class="recent-card">
            <template #header>
              <div class="card-header">
                <span>系统公告</span>
                <el-button type="text" @click="viewMoreNotices">全部公告</el-button>
              </div>
            </template>
            <div class="notice-list">
              <div v-for="(notice, index) in systemNotices" :key="index" class="notice-item">
                <div class="notice-title">{{ notice.title }}</div>
                <div class="notice-content">{{ notice.content }}</div>
                <div class="notice-meta">
                  <span class="notice-time">{{ notice.time }}</span>
                  <el-button type="text" size="small">查看详情</el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { User, Ticket, FirstAidKit, Collection, Refresh } from '@element-plus/icons-vue';

const router = useRouter();
const appointmentChartPeriod = ref('week');

// 当前时间
const currentTime = computed(() => {
  const now = new Date();
  const hours = now.getHours();
  let greeting = '早上好';
  if (hours >= 12 && hours < 18) {
    greeting = '下午好';
  } else if (hours >= 18) {
    greeting = '晚上好';
  }
  return `${greeting}，管理员，今天是 ${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日`;
});

// 统计卡片数据
const statCards = reactive([
  { title: '今日预约数', value: '128', change: 12.5, icon: 'Ticket' },
  { title: '科室总数', value: '15', change: 0, icon: 'First-aid-kit' },
  { title: '医生总数', value: '58', change: 5.2, icon: 'User' },
  { title: '月度访问量', value: '3,154', change: -2.8, icon: 'Collection' }
]);

// 预约趋势数据
const appointmentTrend = reactive([30, 45, 55, 40, 60, 75, 65]);

// 获取日期标签
const getDayLabel = (index: number) => {
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
  return days[index];
};

// 科室分布数据
const departmentData = reactive([
  { name: '内科', value: 35, color: '#409EFF' },
  { name: '外科', value: 20, color: '#67C23A' },
  { name: '妇产科', value: 25, color: '#E6A23C' },
  { name: '儿科', value: 20, color: '#F56C6C' }
]);

// 最近预约数据
const recentAppointments = reactive([
  { name: '张三', department: '内科', doctor: '李医生', time: '2023-06-01 09:30', status: '已完成' },
  { name: '李四', department: '外科', doctor: '王医生', time: '2023-06-01 10:30', status: '已取消' },
  { name: '王五', department: '妇产科', doctor: '赵医生', time: '2023-06-02 14:00', status: '待就诊' },
  { name: '赵六', department: '儿科', doctor: '钱医生', time: '2023-06-03 16:30', status: '待就诊' }
]);

// 系统公告
const systemNotices = reactive([
  { 
    title: '系统更新通知', 
    content: '系统将于2023年6月10日晚上22:00-24:00进行维护更新，请提前做好安排。', 
    time: '2023-06-01' 
  },
  { 
    title: '门诊时间调整', 
    content: '因设备维护，心内科门诊时间调整为上午9:00-11:30，请知悉。', 
    time: '2023-05-30' 
  }
]);

// 获取状态类型
const getStatusType = (status: string) => {
  switch (status) {
    case '已完成': return 'success';
    case '待就诊': return 'primary';
    case '已取消': return 'danger';
    default: return 'info';
  }
};

// 刷新数据
const refreshData = () => {
  // 实际项目中这里会调用API获取最新数据
  console.log('刷新数据');
};

// 查看更多预约
const viewMoreAppointments = () => {
  // 跳转到预约管理页面
  router.push('/medical/appointment');
};

// 查看更多公告
const viewMoreNotices = () => {
  // 跳转到公告页面
  router.push('/system/notice');
};

onMounted(() => {
  refreshData();
});
</script>

<style lang="scss" scoped>
@import '@/style/variables.scss';

.home-container {
  padding: 20px;
}

.welcome-section {
  margin-bottom: 20px;
}

.welcome-card {
  border-radius: 8px;
  border: none;
  
  .welcome-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    @media (max-width: 768px) {
      flex-direction: column;
      align-items: flex-start;
      
      .welcome-action {
        margin-top: 16px;
      }
    }
  }
  
  .welcome-text {
    h2 {
      margin: 0 0 8px;
      font-size: 20px;
      color: $text-primary;
    }
    
    p {
      margin: 0;
      color: $text-secondary;
      font-size: 14px;
    }
  }
}

.stats-section {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
  border: none;
  overflow: hidden;
  transition: transform 0.3s;
  height: 100%;
  
  &:hover {
    transform: translateY(-5px);
  }
  
  .stat-card-content {
    display: flex;
    align-items: center;
    padding: 8px 0;
  }
  
  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background-color: rgba($primary-color, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    
    .el-icon {
      font-size: 30px;
      color: $primary-color;
    }
  }
  
  .stat-info {
    flex: 1;
  }
  
  .stat-value {
    font-size: 28px;
    font-weight: 600;
    color: $text-primary;
    line-height: 1.2;
  }
  
  .stat-title {
    font-size: 14px;
    color: $text-secondary;
    margin-top: 4px;
  }
  
  .stat-footer {
    margin-top: 12px;
    font-size: 12px;
    color: $text-secondary;
    border-top: 1px solid $border-lighter;
    padding-top: 8px;
  }
}

.stat-card-0 .stat-icon {
  background-color: rgba($primary-color, 0.1);
  .el-icon { color: $primary-color; }
}

.stat-card-1 .stat-icon {
  background-color: rgba($success-color, 0.1);
  .el-icon { color: $success-color; }
}

.stat-card-2 .stat-icon {
  background-color: rgba($warning-color, 0.1);
  .el-icon { color: $warning-color; }
}

.stat-card-3 .stat-icon {
  background-color: rgba($danger-color, 0.1);
  .el-icon { color: $danger-color; }
}

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 8px;
  border: none;
  height: 100%;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    span {
      font-size: 16px;
      font-weight: 600;
      color: $text-primary;
    }
  }
  
  .chart-placeholder {
    height: 300px;
    display: flex;
    justify-content: center;
    align-items: center;
    
    &.pie-chart {
      position: relative;
    }
  }
  
  .chart-bars {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    width: 100%;
    height: 100%;
    padding: 0 20px;
    
    .chart-bar {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      height: 100%;
      position: relative;
      margin: 0 8px;
      padding-top: 30px;
      
      .bar-value {
        width: 30px;
        background-color: $primary-color;
        border-radius: 4px 4px 0 0;
        position: absolute;
        bottom: 24px;
      }
      
      .bar-label {
        position: absolute;
        bottom: 0;
        font-size: 12px;
        color: $text-secondary;
      }
    }
  }
  
  .pie-segments {
    width: 200px;
    height: 200px;
    position: relative;
    border-radius: 50%;
    overflow: hidden;
  }
  
  .pie-segment {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }
  
  .pie-legend {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    flex-direction: column;
    
    .legend-item {
      display: flex;
      align-items: center;
      margin-bottom: 10px;
      
      .legend-color {
        width: 16px;
        height: 16px;
        border-radius: 4px;
        margin-right: 8px;
      }
      
      .legend-label {
        font-size: 14px;
        color: $text-regular;
        margin-right: 16px;
      }
      
      .legend-value {
        font-size: 14px;
        font-weight: 600;
        color: $text-primary;
      }
    }
  }
}

.recent-section {
  margin-bottom: 20px;
}

.recent-card {
  border-radius: 8px;
  border: none;
  height: 100%;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    span {
      font-size: 16px;
      font-weight: 600;
      color: $text-primary;
    }
  }
  
  .notice-list {
    .notice-item {
      padding: 16px 0;
      border-bottom: 1px solid $border-lighter;
      
      &:last-child {
        border-bottom: none;
      }
      
      .notice-title {
        font-size: 16px;
        font-weight: 500;
        color: $text-primary;
        margin-bottom: 8px;
      }
      
      .notice-content {
        font-size: 14px;
        color: $text-regular;
        margin-bottom: 8px;
        line-height: 1.5;
      }
      
      .notice-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .notice-time {
          font-size: 12px;
          color: $text-secondary;
        }
      }
    }
  }
}

.mr-5 {
  margin-right: 5px;
}
</style>
