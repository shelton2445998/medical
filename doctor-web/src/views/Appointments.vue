<template>
  <div class="appointments-container">
    <h2 class="page-title">预约管理</h2>
    
    <!-- 搜索区域 -->
    <el-card class="mb-20">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="预约日期">
          <el-date-picker
            v-model="searchForm.date"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.patientName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 预约列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>预约列表</span>
          <el-button-group>
            <el-button type="primary" size="small" @click="exportAppointments">导出</el-button>
            <!-- 移除医生端新增预约按钮（文档中医生端无此接口） -->
          </el-button-group>
        </div>
      </template>
      <el-table :data="appointmentsList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="预约编号" width="120" />
        <el-table-column prop="userName" label="患者姓名" width="100" />
        <el-table-column prop="userPhone" label="联系电话" width="130" />
        <el-table-column prop="setmealName" label="套餐名称" width="150" />
        <el-table-column prop="hospitalName" label="医院名称" width="150" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.appointmentDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="timeSlot" label="预约时段" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 1" 
              type="primary" 
              size="small" 
              @click="updateAppointmentStatus(scope.row, 2)"
            >
              确认
            </el-button>
            <el-button 
              v-if="scope.row.status === 2" 
              type="success" 
              size="small" 
              @click="updateAppointmentStatus(scope.row, 3)"
            >
              完成
            </el-button>
            <el-button 
              v-if="[1, 2].includes(scope.row.status)" 
              type="danger" 
              size="small" 
              @click="updateAppointmentStatus(scope.row, 0, true)"
            >
              取消
            </el-button>
            <el-button 
              type="info" 
              size="small" 
              @click="viewAppointment(scope.row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 预约详情对话框 -->
    <el-dialog
      title="预约详情"
      v-model="detailDialogVisible"
      width="500px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="预约编号">{{ currentAppointment.appointmentId }}</el-descriptions-item>
        <el-descriptions-item label="患者姓名">{{ currentAppointment.patientName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentAppointment.patientPhone }}</el-descriptions-item>
        <el-descriptions-item label="预约科室">{{ getDepartmentLabel(currentAppointment.department) }}</el-descriptions-item>
        <el-descriptions-item label="主治医生">{{ currentAppointment.doctorName }}</el-descriptions-item>
        <el-descriptions-item label="预约日期">{{ currentAppointment.appointmentDate }}</el-descriptions-item>
        <el-descriptions-item label="预约时段">{{ getTimeSlotLabel(currentAppointment.timeSlot) }}</el-descriptions-item>
        <el-descriptions-item label="症状描述">{{ currentAppointment.symptoms }}</el-descriptions-item>
        <el-descriptions-item label="预约状态">
          <el-tag :type="getStatusType(currentAppointment.status)">{{ getStatusLabel(currentAppointment.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentAppointment.createTime }}</el-descriptions-item>
        <el-descriptions-item v-if="currentAppointment.remark" label="备注">{{ currentAppointment.remark }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 取消预约原因对话框 -->
    <el-dialog
      title="取消预约"
      v-model="cancelDialogVisible"
      width="400px"
    >
      <el-form :model="cancelForm" ref="cancelFormRef">
        <el-form-item label="取消原因" prop="reason" :rules="[{ required: true, message: '请输入取消原因', trigger: 'blur' }]">
          <el-input
            v-model="cancelForm.reason"
            type="textarea"
            placeholder="请输入取消预约的原因"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelDialogVisible = false">取 消</el-button>
          <el-button type="danger" @click="confirmCancel">确认取消预约</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElRouter } from 'element-plus'
import { getAppointmentList } from '@/api/doctor' // 导入API方法

export default {
  name: 'AppointmentsView',
  setup() {
    // 加载状态
    const loading = ref(false)
    const patientLoading = ref(false)
    
    // 分页参数（与文档一致：pageNum、pageSize）
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    
    // 搜索表单
    const searchForm = reactive({
      date: [], // 日期范围
      patientName: '',
      status: ''
    })
    
    // 预约列表
    const appointmentsList = ref([])
    
    // 科室列表
    const departments = [
      { value: 'internal', label: '内科' },
      { value: 'surgery', label: '外科' },
      { value: 'pediatrics', label: '儿科' },
      { value: 'gynecology', label: '妇产科' },
      { value: 'stomatology', label: '口腔科' },
      { value: 'dermatology', label: '皮肤科' },
      { value: 'ophthalmology', label: '眼科' },
      { value: 'ent', label: '耳鼻喉科' },
      { value: 'neurology', label: '神经科' }
    ]
    
    // 预约时段
    const timeSlots = [
      { value: 'morning', label: '上午(8:00-12:00)' },
      { value: 'afternoon', label: '下午(14:00-18:00)' }
    ]
    
    // 状态选项（与后端状态一致）
    const statusOptions = [
      { value: 0, label: '已取消' },
      { value: 1, label: '待支付' },
      { value: 2, label: '已支付' },
      { value: 3, label: '已完成' }
    ]
    
    // 详情对话框
    const detailDialogVisible = ref(false)
    const currentAppointment = reactive({
      appointmentId: '',
      patientName: '',
      patientPhone: '',
      department: '',
      doctorName: '',
      appointmentDate: '',
      timeSlot: '',
      symptoms: '',
      status: '',
      createTime: '',
      remark: ''
    })
    
    // 取消预约相关
    const cancelDialogVisible = ref(false)
    const cancelFormRef = ref(null)
    const cancelForm = reactive({
      appointmentId: '',
      reason: ''
    })
    const pendingStatusUpdate = ref({}) // 临时存储待取消的预约信息
    
    // 初始化
    onMounted(() => {
      fetchAppointmentsList()
    })
    
    // 获取科室名称
    const getDepartmentLabel = (value) => {
      const dept = departments.find(d => d.value === value)
      return dept ? dept.label : value
    }
    
    // 获取时段名称
    const getTimeSlotLabel = (value) => {
      const slot = timeSlots.find(s => s.value === value)
      return slot ? slot.label : value
    }
    
    // 获取状态标签
    const getStatusLabel = (status) => {
      const statusMap = {
        0: '已取消',
        1: '待支付',
        2: '已支付',
        3: '已完成'
      }
      return statusMap[status] || '未知'
    }
    
    // 获取状态类型
    const getStatusType = (status) => {
      const statusTypeMap = {
        0: 'info',
        1: 'warning',
        2: 'primary',
        3: 'success'
      }
      return statusTypeMap[status] || 'info'
    }
    
    // 获取预约列表（修正URL和分页参数）
    const fetchAppointmentsList = async () => {
      loading.value = true
      try {
        const params = {
          pageIndex: currentPage.value, // 修正为后端BasePageQuery使用的参数名
          pageSize: pageSize.value
        }
        
        // 只有当日期有值时才添加日期参数
        if (searchForm.date && searchForm.date[0]) {
          params.startDate = searchForm.date[0]
        }
        
        if (searchForm.date && searchForm.date[1]) {
          params.endDate = searchForm.date[1]
        }
        
        // 只有当状态有值时才添加状态参数
        if (searchForm.status !== '') {
          params.status = searchForm.status
        }
        
        // 使用统一的API方法获取数据
        const res = await getAppointmentList(params)
        if (res.code === 200) {
          appointmentsList.value = res.data.list 
          total.value = res.data.total      
        } else {
          ElMessage.error(res.msg || '获取预约列表失败')
        }
      } catch (error) {
        console.error('获取预约列表失败:', error)
        // 使用模拟数据
        mockAppointmentsList()
        ElMessage.warning('获取数据失败，显示模拟数据')
      } finally {
        loading.value = false
      }
    }
    
    // 模拟预约数据
    const mockAppointmentsList = () => {
      appointmentsList.value = [
        {
          appointmentId: 'AP20230815001',
          patientName: '张三',
          patientPhone: '13800138001',
          department: 'internal',
          doctorName: '王医生',
          appointmentDate: '2023-08-15',
          timeSlot: 'morning',
          symptoms: '头痛，发热，全身乏力',
          status: 'waiting',
          createTime: '2023-08-12 14:30:00'
        },
        {
          appointmentId: 'AP20230815002',
          patientName: '李四',
          patientPhone: '13800138002',
          department: 'surgery',
          doctorName: '李医生',
          appointmentDate: '2023-08-15',
          timeSlot: 'afternoon',
          symptoms: '右腿疼痛，行走困难',
          status: 'confirmed',
          createTime: '2023-08-13 09:15:00'
        },
		 {
		      appointmentId: 'AP20230815003',
		      patientId: 'P20230003',
		      patientName: '王五',
		      patientPhone: '13800138003',
		      department: 'pediatrics',
		      doctorName: '赵医生',
		      appointmentDate: '2023-08-16',
		      timeSlot: 'morning',
		      symptoms: '咳嗽，流鼻涕',
		      status: 'waiting',
		      createTime: '2023-08-14 16:40:00'
		    }
      ]
      total.value = 2
    }
    
    // 搜索
    const handleSearch = () => {
      currentPage.value = 1
      fetchAppointmentsList()
    }
    
    // 重置搜索
    const resetSearch = () => {
      searchForm.date = []
      searchForm.patientName = ''
      searchForm.status = ''
      currentPage.value = 1
      fetchAppointmentsList()
    }
    
    // 页码变化
    const handleCurrentChange = (val) => {
      currentPage.value = val
      fetchAppointmentsList()
    }
    
    // 每页条数变化
    const handleSizeChange = (val) => {
      pageSize.value = val
      currentPage.value = 1
      fetchAppointmentsList()
    }
    
    // 统一处理预约状态更新（使用文档中的状态更新接口）
    const updateAppointmentStatus = (row, targetStatus, needReason = false) => {
      if (needReason) {
        // 取消预约需要原因，先记录待处理的预约
        pendingStatusUpdate.value = { ...row, targetStatus }
        cancelForm.appointmentId = row.appointmentId
        cancelForm.reason = ''
        cancelDialogVisible.value = true
        return
      }
      
      // 非取消操作，直接更新状态
      const statusText = getStatusLabel(targetStatus)
      ElMessageBox.confirm(`确认将该预约改为【${statusText}】状态吗?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
			 // const backendStatus = statusMap[targetStatus] || targetStatus
          // 文档中医生端更新预约状态的接口：PUT /api/doctor/appointment/status
   //        const { data: res } = await axios.put('/api/doctor/appointment/status', {
   //          appointmentId: row.appointmentId,
   //          status: targetStatus,
   //          remark: '' ,// 非取消操作无需备注
			
   //        })
          if (res.code === 200) {
            ElMessage.success(`预约已${statusText}`)
			const index = appointmentsList.value.findIndex(item => item.appointmentId === row.appointmentId)
			        if (index !== -1) {
			          appointmentsList.value[index] = { ...appointmentsList.value[index], status: targetStatus }
			        }
            fetchAppointmentsList()
          } else {
            ElMessage.error(res.message || '更新状态失败')
          }
        } catch (error) {
          console.error(`更新预约状态为${targetStatus}失败:`, error)
          ElMessage.error('操作失败，请稍后再试')
        }
      }).catch(() => {})
    }
    
    // 确认取消预约（带原因）
    const confirmCancel = () => {
      if (!cancelFormRef.value) return
      
      cancelFormRef.value.validate(async (valid) => {
        if (valid) {
          const { appointmentId, reason } = cancelForm
          const { targetStatus } = pendingStatusUpdate.value
          try {
            // 调用状态更新接口，传递取消原因
            const { data: res } = await axios.put('/api/doctor/appointment/status', {
              appointmentId,
              status: targetStatus,
              remark: reason
            })
            if (res.code === 200) {
              ElMessage.success('预约已取消')
              cancelDialogVisible.value = false
              fetchAppointmentsList()
            } else {
              ElMessage.error(res.message || '取消预约失败')
            }
          } catch (error) {
            console.error('取消预约失败:', error)
            ElMessage.error('操作失败，请稍后再试')
          }
        }
      })
    }
    
    // 查看预约详情
    const viewAppointment = (row) => {
      Object.assign(currentAppointment, row)
      detailDialogVisible.value = true
    }
    
    // 导出预约数据（文档中未定义，保留功能但提示待实现）
    const exportAppointments = () => {
      ElMessage.success('导出预约数据功能待实现（需后端提供对应接口）')
    }

    // 格式化日期
    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    }
    
    return {
      loading,
      currentPage,
      pageSize,
      total,
      searchForm,
      appointmentsList,
      departments,
      timeSlots,
      statusOptions,
      detailDialogVisible,
      currentAppointment,
      cancelDialogVisible,
      cancelForm,
      cancelFormRef,
      getDepartmentLabel,
      getTimeSlotLabel,
      getStatusLabel,
      getStatusType,
      formatDate,
      handleSearch,
      resetSearch,
      handleCurrentChange,
      handleSizeChange,
      updateAppointmentStatus,
      confirmCancel,
      viewAppointment,
      exportAppointments
    }
  }
}
</script>

<style scoped>
.appointments-container {
  padding: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>