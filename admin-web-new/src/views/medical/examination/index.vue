<template>
  <el-card shadow="never" class="card-box">
    <div class="dashboard-container">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>体检套餐总数</span>
              </div>
            </template>
            <div class="card-body">
              <div class="card-value">{{ statistics.setmealCount || 0 }}</div>
              <div class="card-icon">
                <el-icon><Calendar /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>体检项目总数</span>
              </div>
            </template>
            <div class="card-body">
              <div class="card-value">{{ statistics.checkitemCount || 0 }}</div>
              <div class="card-icon">
                <el-icon><List /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>本月体检人数</span>
              </div>
            </template>
            <div class="card-body">
              <div class="card-value">{{ statistics.monthExamCount || 0 }}</div>
              <div class="card-icon">
                <el-icon><User /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>本月预约数</span>
              </div>
            </template>
            <div class="card-body">
              <div class="card-value">{{ statistics.monthOrderCount || 0 }}</div>
              <div class="card-icon">
                <el-icon><Timer /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="mt20">
        <el-col :span="12">
          <el-card class="chart-card">
            <div ref="monthChartRef" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <div ref="categoryChartRef" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="mt20">
        <el-col :span="24">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <span>最近预约记录</span>
              </div>
            </template>
            <el-table :data="recentOrders" style="width: 100%" border>
              <el-table-column type="index" label="序号" width="60" align="center" />
              <el-table-column prop="orderNo" label="订单号" width="180" />
              <el-table-column prop="userName" label="用户姓名" width="120" />
              <el-table-column prop="setmealName" label="套餐名称" min-width="150" />
              <el-table-column prop="hospitalName" label="预约医院" min-width="150" />
              <el-table-column prop="examDate" label="预约日期" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag v-if="scope.row.status === 0" type="info">待付款</el-tag>
                  <el-tag v-else-if="scope.row.status === 1" type="warning">待体检</el-tag>
                  <el-tag v-else-if="scope.row.status === 2" type="primary">已体检</el-tag>
                  <el-tag v-else-if="scope.row.status === 3" type="success">已完成</el-tag>
                  <el-tag v-else type="danger">已取消</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue';
import { Calendar, List, User, Timer } from '@element-plus/icons-vue';
import { http } from '@/utils/http';
import * as echarts from 'echarts';

const statistics = reactive({
  setmealCount: 0,
  checkitemCount: 0,
  monthExamCount: 0,
  monthOrderCount: 0
});

const recentOrders = ref([]);
const monthChartRef = ref();
const categoryChartRef = ref();
let monthChart: echarts.ECharts | null = null;
let categoryChart: echarts.ECharts | null = null;

// 获取统计数据
const getStatisticsData = async () => {
  try {
    const res = await http.get('/admin/examination/statistics');
    Object.assign(statistics, res || {});
  } catch (error) {
    console.error('获取统计数据失败:', error);
  }
};

// 获取最近预约记录
const getRecentOrders = async () => {
  try {
    const res = await http.get('/admin/examination/recent-orders');
    recentOrders.value = res || [];
  } catch (error) {
    console.error('获取最近预约记录失败:', error);
    // 设置示例数据
    recentOrders.value = [];
  }
};

// 初始化月度体检人数图表
const initMonthChart = async () => {
  try {
    const res = await http.get('/admin/examination/month-data');
    nextTick(() => {
      if (monthChartRef.value) {
        if (monthChart) {
          monthChart.dispose();
        }
        monthChart = echarts.init(monthChartRef.value);
        const option = {
          title: {
            text: '近六个月体检人数统计',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: res?.months || []
          },
          yAxis: {
            type: 'value',
            name: '人数'
          },
          series: [
            {
              name: '体检人数',
              type: 'bar',
              data: res?.counts || [],
              itemStyle: {
                color: '#5470c6'
              }
            }
          ]
        };
        monthChart.setOption(option);
      }
    });
  } catch (error) {
    console.error('获取月度数据失败:', error);
    // 设置模拟数据
    nextTick(() => {
      if (monthChartRef.value) {
        if (monthChart) {
          monthChart.dispose();
        }
        monthChart = echarts.init(monthChartRef.value);
        const option = {
          title: {
            text: '近六个月体检人数统计',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: ['1月', '2月', '3月', '4月', '5月', '6月']
          },
          yAxis: {
            type: 'value',
            name: '人数'
          },
          series: [
            {
              name: '体检人数',
              type: 'bar',
              data: [120, 150, 180, 210, 190, 230],
              itemStyle: {
                color: '#5470c6'
              }
            }
          ]
        };
        monthChart.setOption(option);
      }
    });
  }
};

// 初始化套餐分类图表
const initCategoryChart = async () => {
  try {
    const res = await http.get('/admin/examination/category-data');
    nextTick(() => {
      if (categoryChartRef.value) {
        if (categoryChart) {
          categoryChart.dispose();
        }
        categoryChart = echarts.init(categoryChartRef.value);
        const option = {
          title: {
            text: '套餐预约分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '预约数量',
              type: 'pie',
              radius: '60%',
              data: res || [],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        categoryChart.setOption(option);
      }
    });
  } catch (error) {
    console.error('获取分类数据失败:', error);
    // 设置模拟数据
    nextTick(() => {
      if (categoryChartRef.value) {
        if (categoryChart) {
          categoryChart.dispose();
        }
        categoryChart = echarts.init(categoryChartRef.value);
        const option = {
          title: {
            text: '套餐预约分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '预约数量',
              type: 'pie',
              radius: '60%',
              data: [
                { value: 120, name: '常规体检' },
                { value: 80, name: '入职体检' },
                { value: 60, name: '婚前体检' },
                { value: 40, name: '孕前体检' }
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        categoryChart.setOption(option);
      }
    });
  }
};

// 窗口大小改变时重新渲染图表
const resizeCharts = () => {
  if (monthChart) {
    monthChart.resize();
  }
  if (categoryChart) {
    categoryChart.resize();
  }
};

onMounted(() => {
  getStatisticsData();
  getRecentOrders();
  initMonthChart();
  initCategoryChart();
  window.addEventListener('resize', resizeCharts);
});
</script>

<style scoped>
.dashboard-container {
  padding: 10px;
}
.box-card {
  height: 120px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}
.card-value {
  font-size: 28px;
  font-weight: bold;
}
.card-icon {
  font-size: 30px;
  color: #5470c6;
}
.mt20 {
  margin-top: 20px;
}
.chart-card {
  height: 350px;
}
.chart {
  height: 100%;
  width: 100%;
}
</style> 