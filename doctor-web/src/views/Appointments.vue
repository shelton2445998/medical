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
            <el-button type="success" size="small" @click="openAddDialog">新增预约</el-button>
          </el-button-group>
        </div>
      </template>
      <el-table :data="appointmentsList" style="width: 100%" v-loading="loading">
        <el-table-column prop="appointmentId" label="预约编号" width="120" />
        <el-table-column prop="patientName" label="患者姓名" width="100" />
        <el-table-column prop="patientPhone" label="联系电话" width="130" />
        <el-table-column prop="department" label="科室" width="100">
          <template #default="scope">
            {{ getDepartmentLabel(scope.row.department) }}
          </template>
        </el-table-column>
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120" />
        <el-table-column prop="timeSlot" label="预约时段" width="150">
          <template #default="scope">
            {{ getTimeSlotLabel(scope.row.timeSlot) }}
          </template>
        </el-table-column>
        <el-table-column prop="symptoms" label="症状描述" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 'waiting'" 
              type="primary" 
              size="small" 
              @click="handleConfirm(scope.row)"
            >
              确认
            </el-button>
            <el-button 
              v-if="scope.row.status === 'confirmed'" 
              type="success" 
              size="small" 
              @click="handleComplete(scope.row)"
            >
              完成
            </el-button>
            <el-button 
              v-if="['waiting', 'confirmed'].includes(scope.row.status)" 
              type="danger" 
              size="small" 
              @click="handleCancel(scope.row)"
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
    
    <!-- 新增预约对话框 -->
    <el-dialog
      title="新增预约"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form
        :model="appointmentForm"
        :rules="appointmentRules"
        ref="appointmentFormRef"
        label-width="100px"
      >
        <el-form-item label="患者信息" prop="patientId">
          <el-select
            v-model="appointmentForm.patientId"
            filterable
            remote
            reserve-keyword
            placeholder="请输入患者姓名或手机号查询"
            :remote-method="searchPatients"
            :loading="patientLoading"
            style="width: 100%"
          >
            <el-option
              v-for="item in patientOptions"
              :key="item.patientId"
              :label="`${item.name} (${item.phone})`"
              :value="item.patientId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预约科室" prop="department">
          <el-select v-model="appointmentForm.department" placeholder="请选择科室" style="width: 100%" @change="handleDepartmentChange">
            <el-option v-for="dept in departments" :key="dept.value" :label="dept.label" :value="dept.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预约医生" prop="doctorId">
          <el-select v-model="appointmentForm.doctorId" placeholder="请选择医生" style="width: 100%">
            <el-option v-for="doctor in doctors" :key="doctor.id" :label="doctor.name" :value="doctor.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预约日期" prop="appointmentDate">
          <el-date-picker
            v-model="appointmentForm.appointmentDate"
            type="date"
            placeholder="选择预约日期"
            style="width: 100%"
            :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="预约时段" prop="timeSlot">
          <el-select v-model="appointmentForm.timeSlot" placeholder="请选择时段" style="width: 100%">
            <el-option v-for="slot in timeSlots" :key="slot.value" :label="slot.label" :value="slot.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="症状描述" prop="symptoms">
          <el-input
            v-model="appointmentForm.symptoms"
            type="textarea"
            placeholder="请输入症状描述"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAppointment">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    
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
    
    <!-- 取消预约对话框 -->
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
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export default {
  name: 'AppointmentsView',
  setup() {
    // 加载状态
    const loading = ref(false)
    const patientLoading = ref(false)
    
    // 分页参数
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    
    // 搜索表单
    const searchForm = reactive({
      date: [],
      patientName: '',
      status: ''
    })
    
    // 预约列表
    const appointmentsList = ref([])
    
    // 新增预约表单
    const appointmentFormRef = ref(null)
    const dialogVisible = ref(false)
    const appointmentForm = reactive({
      patientId: '',
      patientName: '',
      department: '',
      doctorId: '',
      doctorName: '',
      appointmentDate: '',
      timeSlot: '',
      symptoms: ''
    })
    
    // 表单校验规则
    const appointmentRules = {
      patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
      department: [{ required: true, message: '请选择科室', trigger: 'change' }],
      doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
      appointmentDate: [{ required: true, message: '请选择预约日期', trigger: 'blur' }],
      timeSlot: [{ required: true, message: '请选择预约时段', trigger: 'change' }],
      symptoms: [{ required: true, message: '请输入症状描述', trigger: 'blur' }]
    }
    
    // 患者选项
    const patientOptions = ref([])
    
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
    
    // 医生列表
    const doctors = ref([])
    
    // 预约时段
    const timeSlots = [
      { value: 'morning', label: '上午(8:00-12:00)' },
      { value: 'afternoon', label: '下午(14:00-18:00)' }
    ]
    
    // 状态选项
    const statusOptions = [
      { value: 'waiting', label: '待确认' },
      { value: 'confirmed', label: '已确认' },
      { value: 'completed', label: '已完成' },
      { value: 'cancelled', label: '已取消' }
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
    
    // 取消预约对话框
    const cancelDialogVisible = ref(false)
    const cancelFormRef = ref(null)
    const cancelForm = reactive({
      appointmentId: '',
      reason: ''
    })
    
    // 不允许选择过去日期
    const disabledDate = (date) => {
      return date.getTime() < Date.now() - 8.64e7
    }
    
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
    
    // 获取状态名称和类型
    const getStatusLabel = (status) => {
      const statusMap = {
        waiting: '待确认',
        confirmed: '已确认',
        completed: '已完成',
        cancelled: '已取消'
      }
      return statusMap[status] || status
    }
    
    const getStatusType = (status) => {
      const typeMap = {
        waiting: 'warning',
        confirmed: 'primary',
        completed: 'success',
        cancelled: 'info'
      }
      return typeMap[status] || ''
    }
    
    // 获取预约列表
    const fetchAppointmentsList = async () => {
      loading.value = true
      try {
        const params = {
          page: currentPage.value,
          limit: pageSize.value,
          ...searchForm
        }
        const { data: res } = await axios.get('/doctor/appointments/list', { params })
        if (res.code === 200) {
          appointmentsList.value = res.data.list
          total.value = res.data.total
        } else {
          ElMessage.error(res.message || '获取预约列表失败')
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
          appointmentId: 'AP20230814001',
          patientName: '王五',
          patientPhone: '13800138003',
          department: 'internal',
          doctorName: '王医生',
          appointmentDate: '2023-08-14',
          timeSlot: 'morning',
          symptoms: '咳嗽，咽喉疼痛',
          status: 'completed',
          createTime: '2023-08-12 10:20:00'
        },
        {
          appointmentId: 'AP20230814002',
          patientName: '赵六',
          patientPhone: '13800138004',
          department: 'dermatology',
          doctorName: '张医生',
          appointmentDate: '2023-08-14',
          timeSlot: 'afternoon',
          symptoms: '皮疹，瘙痒',
          status: 'cancelled',
          createTime: '2023-08-11 16:40:00',
          remark: '患者自行取消，改期'
        }
      ]
      total.value = 4
    }
    
    // 搜索患者
    const searchPatients = async (query) => {
      if (query.length < 2) return
      patientLoading.value = true
      try {
        const { data: res } = await axios.get('/doctor/patients/search', { params: { query } })
        if (res.code === 200) {
          patientOptions.value = res.data
        }
      } catch (error) {
        console.error('搜索患者失败:', error)
        // 模拟数据
        patientOptions.value = [
          { patientId: 'P20230001', name: '张三', phone: '13800138001' },
          { patientId: 'P20230002', name: '李四', phone: '13800138002' },
          { patientId: 'P20230003', name: '王五', phone: '13800138003' }
        ]
      } finally {
        patientLoading.value = false
      }
    }
    
    // 科室变更时获取医生列表
    const handleDepartmentChange = async (department) => {
      if (!department) return
      try {
        const { data: res } = await axios.get('/doctor/doctors/list', { params: { department } })
        if (res.code === 200) {
          doctors.value = res.data
        }
      } catch (error) {
        console.error('获取医生列表失败:', error)
        // 模拟数据
        doctors.value = [
          { id: 'D001', name: '王医生' },
          { id: 'D002', name: '李医生' },
          { id: 'D003', name: '张医生' }
        ]
      }
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
    
    // 打开添加预约对话框
    const openAddDialog = () => {
      Object.keys(appointmentForm).forEach(key => {
        appointmentForm[key] = ''
      })
      dialogVisible.value = true
    }
    
    // 提交预约
    const submitAppointment = () => {
      if (!appointmentFormRef.value) return
      
      appointmentFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const { data: res } = await axios.post('/doctor/appointments/create', appointmentForm)
            if (res.code === 200) {
              ElMessage.success('预约创建成功')
              dialogVisible.value = false
              fetchAppointmentsList()
            } else {
              ElMessage.error(res.message || '创建预约失败')
            }
          } catch (error) {
            console.error('创建预约失败:', error)
            ElMessage.error('创建失败，请稍后再试')
          }
        }
      })
    }
    
    // 确认预约
    const handleConfirm = (row) => {
      ElMessageBox.confirm('确认这个预约吗?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const { data: res } = await axios.post('/doctor/appointments/confirm', { appointmentId: row.appointmentId })
          if (res.code === 200) {
            ElMessage.success('预约已确认')
            fetchAppointmentsList()
          } else {
            ElMessage.error(res.message || '操作失败')
          }
        } catch (error) {
          console.error('确认预约失败:', error)
          ElMessage.error('操作失败，请稍后再试')
        }
      }).catch(() => {})
    }
    
    // 完成预约
    const handleComplete = (row) => {
      ElMessageBox.confirm('标记该预约为已完成?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const { data: res } = await axios.post('/doctor/appointments/complete', { appointmentId: row.appointmentId })
          if (res.code === 200) {
            ElMessage.success('预约已完成')
            fetchAppointmentsList()
          } else {
            ElMessage.error(res.message || '操作失败')
          }
        } catch (error) {
          console.error('完成预约失败:', error)
          ElMessage.error('操作失败，请稍后再试')
        }
      }).catch(() => {})
    }
    
    // 打开取消预约对话框
    const handleCancel = (row) => {
      cancelForm.appointmentId = row.appointmentId
      cancelForm.reason = ''
      cancelDialogVisible.value = true
    }
    
    // 确认取消预约
    const confirmCancel = () => {
      if (!cancelFormRef.value) return
      
      cancelFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const { data: res } = await axios.post('/doctor/appointments/cancel', cancelForm)
            if (res.code === 200) {
              ElMessage.success('预约已取消')
              cancelDialogVisible.value = false
              fetchAppointmentsList()
            } else {
              ElMessage.error(res.message || '取消失败')
            }
          } catch (error) {
            console.error('取消预约失败:', error)
            ElMessage.error('取消失败，请稍后再试')
          }
        }
      })
    }
    
    // 查看预约详情
    const viewAppointment = (row) => {
      Object.assign(currentAppointment, row)
      detailDialogVisible.value = true
    }
    
    // 导出预约数据
    const exportAppointments = () => {
      ElMessage.success('导出预约数据功能待实现')
      // TODO: 实现导出功能
    }
    
    return {
      loading,
      patientLoading,
      currentPage,
      pageSize,
      total,
      searchForm,
      appointmentsList,
      dialogVisible,
      appointmentForm,
      appointmentRules,
      appointmentFormRef,
      patientOptions,
      departments,
      doctors,
      timeSlots,
      statusOptions,
      detailDialogVisible,
      currentAppointment,
      cancelDialogVisible,
      cancelForm,
      cancelFormRef,
      disabledDate,
      getDepartmentLabel,
      getTimeSlotLabel,
      getStatusLabel,
      getStatusType,
      handleSearch,
      resetSearch,
      handleCurrentChange,
      handleSizeChange,
      openAddDialog,
      searchPatients,
      handleDepartmentChange,
      submitAppointment,
      handleConfirm,
      handleComplete,
      handleCancel,
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