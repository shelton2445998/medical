<template>
  <div class="appointment-management-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <h2>预约管理</h2>
        </div>
      </template>
      
      <!-- 搜索过滤区域 -->
      <div class="filter-container">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="预约号">
            <el-input v-model="filterForm.appointmentNo" placeholder="请输入预约号" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="filterForm.username" placeholder="请输入用户名" clearable></el-input>
          </el-form-item>
          <el-form-item label="医院">
            <el-select v-model="filterForm.hospital" placeholder="请选择" clearable>
              <el-option v-for="item in hospitals" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择" clearable>
              <el-option label="待确认" value="PENDING"></el-option>
              <el-option label="已确认" value="CONFIRMED"></el-option>
              <el-option label="已完成" value="COMPLETED"></el-option>
              <el-option label="已取消" value="CANCELLED"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="预约日期">
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
      
      <!-- 数据表格 -->
      <div class="table-container">
        <el-table
          v-loading="tableLoading"
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column prop="appointmentNo" label="预约号" width="120"></el-table-column>
          <el-table-column prop="username" label="用户名" width="100"></el-table-column>
          <el-table-column prop="packageName" label="体检套餐" width="120"></el-table-column>
          <el-table-column prop="hospital" label="医院" width="120"></el-table-column>
          <el-table-column prop="appointmentDate" label="预约日期" width="120"></el-table-column>
          <el-table-column prop="appointmentTime" label="预约时间" width="100"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" fixed="right" width="220">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
              <el-button 
                v-if="scope.row.status === 'PENDING'" 
                type="success" 
                size="small" 
                @click="handleConfirm(scope.row)">确认</el-button>
              <el-button 
                v-if="['PENDING', 'CONFIRMED'].includes(scope.row.status)" 
                type="warning" 
                size="small" 
                @click="handleComplete(scope.row)">完成</el-button>
              <el-button 
                v-if="['PENDING', 'CONFIRMED'].includes(scope.row.status)" 
                type="danger" 
                size="small" 
                @click="handleCancel(scope.row)">取消</el-button>
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
    
    <!-- 预约详情对话框 -->
    <el-dialog
      title="预约详情"
      v-model="detailDialogVisible"
      width="600px">
      <div v-if="currentAppointment" class="appointment-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="预约号" :span="2">{{ currentAppointment.appointmentNo }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ currentAppointment.username }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentAppointment.phone }}</el-descriptions-item>
          <el-descriptions-item label="体检套餐">{{ currentAppointment.packageName }}</el-descriptions-item>
          <el-descriptions-item label="套餐价格">¥{{ currentAppointment.price }}</el-descriptions-item>
          <el-descriptions-item label="预约医院">{{ currentAppointment.hospital }}</el-descriptions-item>
          <el-descriptions-item label="医院地址">{{ currentAppointment.hospitalAddress }}</el-descriptions-item>
          <el-descriptions-item label="预约日期">{{ currentAppointment.appointmentDate }}</el-descriptions-item>
          <el-descriptions-item label="预约时间">{{ currentAppointment.appointmentTime }}</el-descriptions-item>
          <el-descriptions-item label="预约状态">
            <el-tag :type="getStatusType(currentAppointment.status)">{{ getStatusText(currentAppointment.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentAppointment.createTime }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentAppointment.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 筛选表单数据
const filterForm = reactive({
  appointmentNo: '',
  username: '',
  hospital: '',
  status: '',
  dateRange: []
})

// 医院列表
const hospitals = [
  '北京协和医院',
  '上海交通大学医学院附属仁济医院',
  '广州中医药大学第一附属医院',
  '武汉同济医院',
  '四川大学华西医院'
]

// 表格数据
const tableData = ref([])
const tableLoading = ref(false)
const total = ref(0)
const pageSize = ref(10)
const currentPage = ref(1)

// 预约详情
const detailDialogVisible = ref(false)
const currentAppointment = ref(null)

// 获取数据
const fetchData = () => {
  tableLoading.value = true
  
  // 模拟API请求
  setTimeout(() => {
    // 生成模拟数据
    const mockData = []
    const statuses = ['PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED']
    const packages = ['基础体检套餐', '全面体检套餐', '妇科体检套餐', '男性体检套餐', '老年体检套餐']
    const times = ['08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '14:00', '14:30', '15:00']
    
    for (let i = 1; i <= 50; i++) {
      const status = statuses[Math.floor(Math.random() * statuses.length)]
      const hospital = hospitals[Math.floor(Math.random() * hospitals.length)]
      const packageName = packages[Math.floor(Math.random() * packages.length)]
      const today = new Date()
      const appointmentDate = new Date(today)
      appointmentDate.setDate(today.getDate() + Math.floor(Math.random() * 30) - 10)
      
      mockData.push({
        id: i,
        appointmentNo: `APT${String(i).padStart(6, '0')}`,
        username: `user${i}`,
        phone: `1${Math.floor(Math.random() * 9 + 1)}${String(Math.floor(Math.random() * 1000000000)).padStart(9, '0')}`,
        packageName: packageName,
        price: Math.floor(Math.random() * 1000) + 200,
        hospital: hospital,
        hospitalAddress: `${hospital.substring(0, 2)}市医院路${Math.floor(Math.random() * 100) + 1}号`,
        appointmentDate: appointmentDate.toISOString().split('T')[0],
        appointmentTime: times[Math.floor(Math.random() * times.length)],
        status: status,
        createTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleString(),
        remark: Math.random() > 0.7 ? '用户备注信息' : ''
      })
    }
    
    // 应用筛选条件
    let filteredData = [...mockData]
    if (filterForm.appointmentNo) {
      filteredData = filteredData.filter(item => item.appointmentNo.includes(filterForm.appointmentNo))
    }
    if (filterForm.username) {
      filteredData = filteredData.filter(item => item.username.includes(filterForm.username))
    }
    if (filterForm.hospital) {
      filteredData = filteredData.filter(item => item.hospital === filterForm.hospital)
    }
    if (filterForm.status) {
      filteredData = filteredData.filter(item => item.status === filterForm.status)
    }
    if (filterForm.dateRange && filterForm.dateRange.length === 2) {
      const startDate = new Date(filterForm.dateRange[0])
      const endDate = new Date(filterForm.dateRange[1])
      filteredData = filteredData.filter(item => {
        const itemDate = new Date(item.appointmentDate)
        return itemDate >= startDate && itemDate <= endDate
      })
    }
    
    // 更新表格数据
    total.value = filteredData.length
    tableData.value = filteredData.slice(
      (currentPage.value - 1) * pageSize.value,
      currentPage.value * pageSize.value
    )
    
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
  currentAppointment.value = row
  detailDialogVisible.value = true
}

// 确认预约
const handleConfirm = (row) => {
  ElMessageBox.confirm(`确认此预约？预约号：${row.appointmentNo}`, '确认操作', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = 'CONFIRMED'
    ElMessage.success('已确认预约')
  }).catch(() => {
    // 用户取消操作
  })
}

// 完成预约
const handleComplete = (row) => {
  ElMessageBox.confirm(`将此预约标记为已完成？预约号：${row.appointmentNo}`, '完成操作', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = 'COMPLETED'
    ElMessage.success('已将预约标记为完成')
  }).catch(() => {
    // 用户取消操作
  })
}

// 取消预约
const handleCancel = (row) => {
  ElMessageBox.confirm(`确定要取消此预约？预约号：${row.appointmentNo}`, '取消操作', {
    confirmButtonText: '确认',
    cancelButtonText: '返回',
    type: 'danger'
  }).then(() => {
    row.status = 'CANCELLED'
    ElMessage.success('已取消预约')
  }).catch(() => {
    // 用户取消操作
  })
}

// 获取状态标签类型
const getStatusType = (status) => {
  const types = {
    'PENDING': 'info',
    'CONFIRMED': 'success',
    'COMPLETED': '',
    'CANCELLED': 'danger'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return texts[status] || status
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.appointment-management-container {
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

.table-container {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.appointment-detail {
  padding: 10px;
}
</style> 