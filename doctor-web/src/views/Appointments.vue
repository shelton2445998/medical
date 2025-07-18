<!--
  预约管理页面组件
  
  提供预约信息的查询、查看、状态管理功能
  支持按日期、患者姓名、状态筛选预约记录
  可以查看预约详情、更新预约状态
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="appointments-container">
    <!-- 页面标题 -->
    <h2 class="page-title">预约管理</h2>
    
    <!-- 搜索区域 -->
    <el-card class="mb-20">
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <!-- 预约日期范围选择 -->
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
        <!-- 患者姓名搜索 -->
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.patientName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <!-- 状态筛选 -->
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <!-- 搜索按钮 -->
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 预约列表 -->
    <el-card>
      <!-- 卡片头部 -->
      <template #header>
        <div class="card-header">
          <span>预约列表</span>
          <el-button-group>
            <el-button type="primary" size="small" @click="exportAppointments">导出</el-button>
            <!-- 移除医生端新增预约按钮（文档中医生端无此接口） -->
          </el-button-group>
        </div>
      </template>
      
      <!-- 预约信息表格 -->
      <el-table :data="appointmentsList" style="width: 100%" v-loading="loading">
        <!-- 预约编号列 -->
        <el-table-column prop="id" label="预约编号" width="120" />
        <!-- 患者姓名列 -->
        <el-table-column prop="userName" label="患者姓名" width="100" />
        <!-- 联系电话列 -->
        <el-table-column prop="userPhone" label="联系电话" width="130" />
        <!-- 套餐名称列 -->
        <el-table-column prop="setmealName" label="套餐名称" width="150" />
        <!-- 医院名称列 -->
        <el-table-column prop="hospitalName" label="医院名称" width="150" />
        <!-- 医生姓名列 -->
        <el-table-column prop="doctorName" label="医生" width="100" />
        <!-- 预约日期列 -->
        <el-table-column prop="appointmentDate" label="预约日期" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.appointmentDate) }}
          </template>
        </el-table-column>
        <!-- 预约时段列 -->
        <el-table-column prop="timeSlot" label="预约时段" width="120" />
        <!-- 状态列 -->
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <!-- 确认预约按钮 -->
            <el-button 
              v-if="scope.row.status === 1" 
              type="primary" 
              size="small" 
              @click="updateAppointmentStatus(scope.row, 2)"
            >
              确认
            </el-button>
            <!-- 完成预约按钮 -->
            <el-button 
              v-if="scope.row.status === 2" 
              type="success" 
              size="small" 
              @click="updateAppointmentStatus(scope.row, 3)"
            >
              完成
            </el-button>
            <!-- 取消预约按钮 -->
            <el-button 
              v-if="[1, 2].includes(scope.row.status)" 
              type="danger" 
              size="small" 
              @click="updateAppointmentStatus(scope.row, 0, true)"
            >
              取消
            </el-button>
            <!-- 查看详情按钮 -->
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
      <!-- 分页区域 -->
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
      <!-- 详情描述列表 -->
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
      <!-- 对话框底部按钮 -->
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
      <!-- 取消原因表单 -->
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
      <!-- 取消对话框底部按钮 -->
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
/**
 * 预约管理页面逻辑
 * 
 * 处理预约信息的查询、状态更新、详情查看等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, reactive, onMounted } from 'vue'
// 导入 Element Plus 组件
import { ElMessage, ElMessageBox, ElRouter } from 'element-plus'
// 导入API方法
import { getAppointmentList } from '@/api/doctor'

/**
 * 预约管理组件导出
 * 
 * 定义组件的基本信息和逻辑
 */
export default {
  name: 'AppointmentsView',
  setup() {
    // 加载状态
    const loading = ref(false)
    const patientLoading = ref(false)
    
    /**
     * 分页参数
     * 
     * 控制预约列表的分页显示
     */
    const currentPage = ref(1)         // 当前页码
    const pageSize = ref(10)           // 每页显示数量
    const total = ref(0)               // 总记录数
    
    /**
     * 搜索表单响应式对象
     * 
     * 存储搜索条件的表单数据
     */
    const searchForm = reactive({
      date: [],          // 日期范围
      patientName: '',   // 患者姓名
      status: ''         // 预约状态
    })
    
    /**
     * 预约列表数据
     * 
     * 存储从服务器获取的预约信息列表
     */
    const appointmentsList = ref([])
    
    /**
     * 科室选项列表
     * 
     * 用于显示科室名称的映射
     */
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
    
    /**
     * 预约时段选项列表
     * 
     * 用于显示时段名称的映射
     */
    const timeSlots = [
      { value: 'morning', label: '上午(8:00-12:00)' },
      { value: 'afternoon', label: '下午(14:00-18:00)' }
    ]
    
    /**
     * 状态选项列表
     * 
     * 用于搜索筛选和状态显示
     */
    const statusOptions = [
      { value: 0, label: '已取消' },
      { value: 1, label: '待支付' },
      { value: 2, label: '已支付' },
      { value: 3, label: '已完成' }
    ]
    
    /**
     * 详情对话框相关状态
     * 
     * 控制预约详情对话框的显示和数据
     */
    const detailDialogVisible = ref(false)
    const currentAppointment = reactive({
      appointmentId: '',    // 预约ID
      patientName: '',      // 患者姓名
      patientPhone: '',     // 患者电话
      department: '',       // 科室
      doctorName: '',       // 医生姓名
      appointmentDate: '',  // 预约日期
      timeSlot: '',         // 时段
      symptoms: '',         // 症状描述
      status: '',           // 状态
      createTime: '',       // 创建时间
      remark: ''            // 备注
    })
    
    /**
     * 取消预约相关状态
     * 
     * 控制取消预约对话框和表单数据
     */
    const cancelDialogVisible = ref(false)
    const cancelFormRef = ref(null)
    const cancelForm = reactive({
      appointmentId: '',    // 预约ID
      reason: ''            // 取消原因
    })
    const pendingStatusUpdate = ref({}) // 临时存储待取消的预约信息
    
    /**
     * 组件挂载时的初始化
     * 
     * 页面加载时自动获取预约列表
     */
    onMounted(() => {
      fetchAppointmentsList()
    })
    
    /**
     * 获取科室名称
     * 
     * 根据科室值获取对应的显示名称
     * 
     * @param {string} value - 科室值
     * @returns {string} 科室名称
     */
    const getDepartmentLabel = (value) => {
      const dept = departments.find(d => d.value === value)
      return dept ? dept.label : value
    }
    
    /**
     * 获取时段名称
     * 
     * 根据时段值获取对应的显示名称
     * 
     * @param {string} value - 时段值
     * @returns {string} 时段名称
     */
    const getTimeSlotLabel = (value) => {
      const slot = timeSlots.find(s => s.value === value)
      return slot ? slot.label : value
    }
    
    /**
     * 获取状态标签
     * 
     * 根据状态值获取对应的显示文本
     * 
     * @param {number} status - 状态值
     * @returns {string} 状态名称
     */
    const getStatusLabel = (status) => {
      const statusMap = {
        0: '已取消',
        1: '待支付',
        2: '已支付',
        3: '已完成'
      }
      return statusMap[status] || '未知'
    }
    
    /**
     * 获取状态类型
     * 
     * 根据状态值获取对应的Element Plus标签类型
     * 
     * @param {number} status - 状态值
     * @returns {string} 标签类型
     */
    const getStatusType = (status) => {
      const statusTypeMap = {
        0: 'info',
        1: 'warning',
        2: 'primary',
        3: 'success'
      }
      return statusTypeMap[status] || 'info'
    }
    
    /**
     * 获取预约列表
     * 
     * 从服务器获取预约信息列表，支持分页和搜索
     */
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
    
    /**
     * 模拟预约数据
     * 
     * 在网络请求失败时提供模拟数据
     */
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
    
    /**
     * 搜索处理函数
     * 
     * 根据搜索条件重新获取预约列表
     */
    const handleSearch = () => {
      currentPage.value = 1
      fetchAppointmentsList()
    }
    
    /**
     * 重置搜索条件
     * 
     * 清空所有搜索条件并重新加载数据
     */
    const resetSearch = () => {
      searchForm.date = []
      searchForm.patientName = ''
      searchForm.status = ''
      currentPage.value = 1
      fetchAppointmentsList()
    }
    
    /**
     * 页码变化处理
     * 
     * 分页器页码改变时的处理函数
     * 
     * @param {number} val - 新的页码
     */
    const handleCurrentChange = (val) => {
      currentPage.value = val
      fetchAppointmentsList()
    }
    
    /**
     * 每页条数变化处理
     * 
     * 分页器每页条数改变时的处理函数
     * 
     * @param {number} val - 新的每页条数
     */
    const handleSizeChange = (val) => {
      pageSize.value = val
      currentPage.value = 1
      fetchAppointmentsList()
    }
    
    /**
     * 统一处理预约状态更新
     * 
     * 处理预约状态的更新，包括确认、完成、取消等操作
     * 
     * @param {Object} row - 预约数据对象
     * @param {number} targetStatus - 目标状态值
     * @param {boolean} needReason - 是否需要填写原因
     */
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
    
    /**
     * 确认取消预约
     * 
     * 验证取消原因并提交取消请求
     */
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
    
    /**
     * 查看预约详情
     * 
     * 显示预约的详细信息对话框
     * 
     * @param {Object} row - 预约数据对象
     */
    const viewAppointment = (row) => {
      Object.assign(currentAppointment, row)
      detailDialogVisible.value = true
    }
    
    /**
     * 导出预约数据
     * 
     * 导出预约列表到Excel文件
     */
    const exportAppointments = () => {
      ElMessage.success('导出预约数据功能待实现（需后端提供对应接口）')
    }

    /**
     * 格式化日期
     * 
     * 将日期字符串格式化为 YYYY-MM-DD 格式
     * 
     * @param {string} dateStr - 日期字符串
     * @returns {string} 格式化后的日期
     */
    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    }
    
    /**
     * 返回模板需要的数据和方法
     * 
     * 将所有响应式数据和方法暴露给模板使用
     */
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

<!--
  预约管理页面样式
  
  定义预约管理页面的视觉设计和布局
  使用 scoped 限制样式作用域
-->
<style scoped>
/**
 * 页面主容器样式
 * 
 * 设置页面的基础间距
 */
.appointments-container {
  padding: 20px;
}

/**
 * 搜索表单样式
 * 
 * 设置搜索表单的布局和换行
 */
.search-form {
  display: flex;
  flex-wrap: wrap;
}

/**
 * 卡片头部样式
 * 
 * 设置卡片头部的布局和对齐方式
 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/**
 * 分页容器样式
 * 
 * 设置分页组件的位置和间距
 */
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/**
 * 通用边距样式
 * 
 * 设置组件的底部边距
 */
.mb-20 {
  margin-bottom: 20px;
}

/**
 * 页面标题样式
 * 
 * 设置页面主标题的外观
 */
.page-title {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

/**
 * 对话框底部样式
 * 
 * 设置对话框底部按钮的布局
 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>