<template>
  <div class="container">
    <el-card shadow="never" class="card-box">
      <!-- 搜索表单 -->
      <el-form :model="searchForm" label-width="80px" class="search-form">
        <el-row :gutter="20">
          <el-col :sm="12" :md="8" :lg="6">
            <el-form-item label="预约日期">
              <el-date-picker
                v-model="searchForm.appointmentDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :sm="12" :md="8" :lg="6">
            <el-form-item label="状态">
              <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                <el-option label="已取消" :value="0" />
                <el-option label="待支付" :value="1" />
                <el-option label="已支付" :value="2" />
                <el-option label="已完成" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :sm="24" :md="8" :lg="6">
            <el-form-item>
              <el-button type="primary" @click="searchAppointments">
                <el-icon><Search /></el-icon>查询
              </el-button>
              <el-button @click="resetSearch">
                <el-icon><Refresh /></el-icon>重置
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
        row-key="id"
      >
        <el-table-column prop="id" label="订单ID" width="90" align="center" />
        <el-table-column prop="userId" label="用户ID" width="90" align="center" />
        <el-table-column prop="setmealId" label="套餐ID" width="90" align="center" />
        <el-table-column prop="hospitalId" label="医院ID" width="90" align="center" />
        <el-table-column prop="doctorId" label="医生ID" width="90" align="center" />
        <el-table-column label="预约日期" align="center" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.appointmentDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="timeSlot" label="时间段" width="100" align="center" />
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="金额" align="center" width="100">
          <template #default="scope">
            {{ formatPrice(scope.row.amount) }}
          </template>
        </el-table-column>
        <el-table-column label="支付时间" align="center" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.payTime) }}
          </template>
        </el-table-column>
        <el-table-column label="支付方式" align="center" width="100">
          <template #default="scope">
            {{ getPayTypeText(scope.row.payType) }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" align="center" width="220">
          <template #default="scope">
            <el-button link type="primary" @click="viewDetail(scope.row)">
              <el-icon><View /></el-icon>详情
            </el-button>
            <el-button 
              v-if="scope.row.status === 1" 
              link 
              type="primary" 
              @click="updateStatus(scope.row, 2)"
            >
              <el-icon><Check /></el-icon>确认支付
            </el-button>
            <el-button 
              v-if="scope.row.status === 2" 
              link 
              type="success" 
              @click="updateStatus(scope.row, 3)"
            >
              <el-icon><Check /></el-icon>完成预约
            </el-button>
            <el-button 
              v-if="scope.row.status === 1" 
              link 
              type="danger" 
              @click="updateStatus(scope.row, 0)"
            >
              <el-icon><Close /></el-icon>取消预约
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageInfo.current"
          v-model:page-size="pageInfo.size"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageInfo.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 详情弹窗 -->
      <el-dialog
        v-model="detailVisible"
        title="预约详情"
        width="600px"
        destroy-on-close
      >
        <div v-if="currentAppointment">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单ID">{{ currentAppointment.id }}</el-descriptions-item>
            <el-descriptions-item label="用户ID">{{ currentAppointment.userId }}</el-descriptions-item>
            <el-descriptions-item label="套餐ID">{{ currentAppointment.setmealId }}</el-descriptions-item>
            <el-descriptions-item label="医院ID">{{ currentAppointment.hospitalId }}</el-descriptions-item>
            <el-descriptions-item label="医生ID">{{ currentAppointment.doctorId }}</el-descriptions-item>
            <el-descriptions-item label="预约日期">{{ formatDate(currentAppointment.appointmentDate) }}</el-descriptions-item>
            <el-descriptions-item label="时间段">{{ currentAppointment.timeSlot }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ getStatusText(currentAppointment.status) }}</el-descriptions-item>
            <el-descriptions-item label="订单金额">{{ formatPrice(currentAppointment.amount) }}</el-descriptions-item>
            <el-descriptions-item label="支付时间">{{ formatDateTime(currentAppointment.payTime) }}</el-descriptions-item>
            <el-descriptions-item label="支付方式">{{ getPayTypeText(currentAppointment.payType) }}</el-descriptions-item>
            <el-descriptions-item label="交易号">{{ currentAppointment.transactionId }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDateTime(currentAppointment.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ formatDateTime(currentAppointment.updateTime) }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Refresh, View, Check, Close } from '@element-plus/icons-vue';
import { http } from '@/utils/http';
import { formatDate as formatDateUtil, formatDateTime as formatDateTimeUtil } from '@/utils/date';

// 加载状态
const loading = ref(false);

// 表格数据
const tableData = ref([]);

// 分页信息
const pageInfo = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 搜索表单
const searchForm = reactive({
  appointmentDate: '',
  status: ''
});

// 详情弹窗
const detailVisible = ref(false);
const currentAppointment = ref(null);

// 获取预约列表
const getAppointmentList = async () => {
  loading.value = true;
  try {
    const params = {
      ...searchForm,
      pageNum: pageInfo.current,
      pageSize: pageInfo.size
    };
    const res = await http.get('/admin/appointment/list', { params });
    tableData.value = res.list || [];
    pageInfo.total = res.total || 0;
  } catch (error) {
    console.error('获取预约列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索预约
const searchAppointments = () => {
  pageInfo.current = 1;
  getAppointmentList();
};

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = '';
  });
  pageInfo.current = 1;
  getAppointmentList();
};

// 分页事件
const handleSizeChange = (val: number) => {
  pageInfo.size = val;
  getAppointmentList();
};

const handleCurrentChange = (val: number) => {
  pageInfo.current = val;
  getAppointmentList();
};

// 查看详情
const viewDetail = (row: any) => {
  currentAppointment.value = row;
  detailVisible.value = true;
};

// 更新预约状态
const updateStatus = async (row: any, status: number) => {
  try {
    const statusText = getStatusText(status);
    await ElMessageBox.confirm(
      `确定要将此预约状态更新为"${statusText}"吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    await http.put('/admin/appointment/status', {
      id: row.id,
      status: status
    });
    
    ElMessage.success(`状态已更新为"${statusText}"`);
    getAppointmentList();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新状态失败:', error);
    }
  }
};

// 格式化日期
const formatDate = (date: string | number | Date) => {
  if (!date) return '';
  return formatDateUtil(date);
};

// 格式化日期时间
const formatDateTime = (date: string | number | Date) => {
  if (!date) return '';
  return formatDateTimeUtil(date);
};

// 格式化价格
const formatPrice = (price: number) => {
  if (!price) return '¥0.00';
  return `¥${price.toFixed(2)}`;
};

// 获取状态文本
const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '已取消';
    case 1: return '待支付';
    case 2: return '已支付';
    case 3: return '已完成';
    default: return '未知状态';
  }
};

// 获取状态标签类型
const getStatusType = (status: number) => {
  switch (status) {
    case 0: return 'danger';
    case 1: return 'warning';
    case 2: return 'primary';
    case 3: return 'success';
    default: return 'info';
  }
};

// 获取支付方式文本
const getPayTypeText = (payType: number) => {
  switch (payType) {
    case 1: return '支付宝';
    case 2: return '微信';
    default: return '未支付';
  }
};

onMounted(() => {
  getAppointmentList();
});
</script>

<style scoped>
.container {
  padding: 20px;
}
.card-box {
  width: 100%;
}
.search-form {
  margin-bottom: 20px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 