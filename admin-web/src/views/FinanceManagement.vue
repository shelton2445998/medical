<template>
  <div class="finance-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <h2>财务管理</h2>
          <div class="header-actions">
            <el-button type="primary" @click="handleExport">导出数据</el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索过滤区域 -->
      <div class="filter-container">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="订单号">
            <el-input v-model="filterForm.orderNo" placeholder="请输入订单号" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="filterForm.username" placeholder="请输入用户名" clearable></el-input>
          </el-form-item>
          <el-form-item label="支付状态">
            <el-select v-model="filterForm.status" placeholder="请选择" clearable>
              <el-option label="已支付" value="PAID"></el-option>
              <el-option label="未支付" value="UNPAID"></el-option>
              <el-option label="已退款" value="REFUNDED"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="支付时间">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据统计卡片 -->
      <div class="stat-cards">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Money /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-title">总收入</div>
                  <div class="stat-value">¥{{ stats.totalIncome }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><ShoppingCart /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-title">订单总数</div>
                  <div class="stat-value">{{ stats.totalOrders }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Refresh /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-title">退款金额</div>
                  <div class="stat-value">¥{{ stats.refundAmount }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 数据表格 -->
      <div class="table-container">
        <el-table
          v-loading="tableLoading"
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
          <el-table-column prop="username" label="用户名" width="120"></el-table-column>
          <el-table-column prop="packageName" label="体检套餐" width="150"></el-table-column>
          <el-table-column prop="amount" label="金额" width="120">
            <template #default="scope">
              ¥{{ scope.row.amount.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="paymentMethod" label="支付方式" width="100"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column prop="payTime" label="支付时间" width="180"></el-table-column>
          <el-table-column label="操作" fixed="right" width="150">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
              <el-button 
                v-if="scope.row.status === 'PAID'" 
                type="danger" 
                size="small" 
                @click="handleRefund(scope.row)">退款</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange">
          </el-pagination>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Money, ShoppingCart, Refresh } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

// 筛选表单数据
const filterForm = reactive({
  orderNo: '',
  username: '',
  status: '',
  dateRange: []
})

// 数据统计
const stats = reactive({
  totalIncome: 0,
  totalOrders: 0,
  refundAmount: 0
})

// 表格数据
const tableData = ref([])
const tableLoading = ref(false)
const total = ref(0)
const pageSize = ref(10)
const currentPage = ref(1)

// 获取数据
const fetchData = () => {
  tableLoading.value = true
  
  // 模拟API请求
  setTimeout(() => {
    // 生成模拟数据
    const mockData = []
    for (let i = 1; i <= 30; i++) {
      const status = ['PAID', 'UNPAID', 'REFUNDED'][Math.floor(Math.random() * 3)]
      mockData.push({
        id: i,
        orderNo: `ORD${String(i).padStart(6, '0')}`,
        username: `user${i}`,
        packageName: `体检套餐${i % 5 + 1}`,
        amount: Math.floor(Math.random() * 1000) + 100,
        status: status,
        paymentMethod: ['微信支付', '支付宝', '银行卡'][i % 3],
        createTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleString(),
        payTime: status !== 'UNPAID' ? new Date(Date.now() - Math.random() * 20 * 24 * 60 * 60 * 1000).toLocaleString() : '--'
      })
    }
    
    // 更新表格数据
    tableData.value = mockData.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
    total.value = mockData.length
    
    // 更新统计数据
    stats.totalOrders = mockData.length
    stats.totalIncome = mockData.reduce((sum, item) => sum + (item.status === 'PAID' ? item.amount : 0), 0)
    stats.refundAmount = mockData.reduce((sum, item) => sum + (item.status === 'REFUNDED' ? item.amount : 0), 0)
    
    tableLoading.value = false
  }, 500)
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

// 重置过滤条件
const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    if (Array.isArray(filterForm[key])) {
      filterForm[key] = []
    } else {
      filterForm[key] = ''
    }
  })
  handleSearch()
}

// 处理页面大小变化
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchData()
}

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchData()
}

// 查看详情
const handleDetail = (row) => {
  ElMessage.info(`查看订单详情：${row.orderNo}`)
}

// 处理退款
const handleRefund = (row) => {
  ElMessageBox.confirm(`确定要为订单 ${row.orderNo} 办理退款吗？`, '退款确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage({
      type: 'success',
      message: '退款成功'
    })
    // 在实际应用中，这里会调用退款API
    row.status = 'REFUNDED'
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消退款'
    })
  })
}

// 导出数据
const handleExport = () => {
  ElMessage.success('数据导出成功')
}

// 获取状态标签类型
const getStatusType = (status) => {
  const types = {
    'PAID': 'success',
    'UNPAID': 'warning',
    'REFUNDED': 'info'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    'PAID': '已支付',
    'UNPAID': '未支付',
    'REFUNDED': '已退款'
  }
  return texts[status] || status
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.finance-container {
  padding: 20px;
}

.main-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-container {
  margin-bottom: 20px;
}

.stat-cards {
  margin-bottom: 30px;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  font-size: 24px;
  color: #409eff;
  margin-right: 16px;
}

.stat-info .stat-title {
  font-size: 14px;
  color: #909399;
}

.stat-info .stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-top: 5px;
}

.table-container {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 