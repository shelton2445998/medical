<template>
  <div class="schedule-container">
    <div class="page-header">
      <h2>排班管理</h2>
      <div class="action-buttons">
        <el-button type="primary" @click="handleAddSchedule">
          <el-icon><Plus /></el-icon> 新增排班
        </el-button>
      </div>
    </div>

    <el-card>
      <div class="filter-container">
        <el-date-picker
          v-model="filterDate"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
        <el-button type="primary" @click="fetchScheduleData">查询</el-button>
      </div>

      <el-calendar v-model="selectedDate">
        <template #date-cell="{ data }">
          <div class="calendar-day">
            <p class="date">{{ data.day.split('-').slice(2).join('-') }}</p>
            <p class="date-debug">{{ data.day }}</p>
            <div class="schedule-info">
              <div
                v-for="schedule in getSchedulesByDate(data.day)"
                :key="schedule.id"
                class="schedule-item"
                :class="{ 'schedule-disabled': schedule.status === 0 }"
              >
                <div class="time-slot">{{ schedule.timeSlot }}</div>
                <div class="patient-count">
                  <el-tag :type="getPatientCountTagType(schedule)">
                    {{ schedule.reservedNumber }} / {{ schedule.maxNumber }}
                  </el-tag>
                </div>
                <div class="schedule-actions">
                  <el-button
                    v-if="schedule.status === 1"
                    type="danger"
                    size="small"
                    @click.stop="handleCancelSchedule(schedule)"
                  >
                    停诊
                  </el-button>
                  <el-button
                    v-else
                    type="success"
                    size="small"
                    @click.stop="handleResumeSchedule(schedule)"
                  >
                    恢复
                  </el-button>
                  <el-button
                    type="primary"
                    size="small"
                    @click.stop="handleEditSchedule(schedule)"
                  >
                    编辑
                  </el-button>
                  <el-button
                    v-if="canDeleteSchedule(schedule)"
                    type="danger"
                    size="small"
                    @click.stop="handleDeleteSchedule(schedule)"
                  >
                    删除
                  </el-button>
                </div>
              </div>
              <div v-if="getSchedulesByDate(data.day).length === 0" class="no-schedule">
                无排班
              </div>
            </div>
          </div>
        </template>
      </el-calendar>
    </el-card>

    <!-- 排班表单弹窗 -->
    <el-dialog
      v-model="scheduleDialogVisible"
      :title="isEdit ? '编辑排班' : '新增排班'"
      width="500px"
    >
      <el-form :model="scheduleForm" label-width="100px" :rules="rules" ref="scheduleFormRef">
        <el-form-item label="日期" prop="workDate">
          <el-date-picker
            v-model="scheduleForm.workDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            :disabled="isEdit"
            :disabledDate="disabledDate"
          />
        </el-form-item>
        <el-form-item label="时间段" prop="timeSlot">
          <el-select v-model="scheduleForm.timeSlot" placeholder="请选择时间段" :disabled="isEdit">
            <el-option label="上午(08:00-12:00)" value="上午(08:00-12:00)" />
            <el-option label="下午(14:00-18:00)" value="下午(14:00-18:00)" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大可预约人数" prop="maxNumber">
          <el-input-number v-model="scheduleForm.maxNumber" :min="1" :max="50" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="scheduleForm.status"
            :active-value="1"
            :inactive-value="0"
            active-text="正常"
            inactive-text="停诊"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span>
          <el-button @click="scheduleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitScheduleForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getDoctorSchedules, addDoctorSchedule, updateDoctorSchedule, deleteDoctorSchedule } from '@/api/doctor'

export default {
  name: 'Schedule',
  components: {
    Plus
  },
  setup() {
    const loading = ref(false)
    const scheduleList = ref([])
    const selectedDate = ref(new Date())
    const filterDate = ref('')
    const scheduleDialogVisible = ref(false)
    const isEdit = ref(false)
    const scheduleFormRef = ref(null)
    
    const scheduleForm = reactive({
      id: '',
      workDate: '',
      timeSlot: '',
      maxNumber: 20,
      status: 1
    })
    
    const rules = {
      workDate: [{ required: true, message: '请选择日期', trigger: 'blur' }],
      timeSlot: [{ required: true, message: '请选择时间段', trigger: 'change' }],
      maxNumber: [{ required: true, message: '请输入最大可预约人数', trigger: 'blur' }]
    }
    
    // 日期禁用函数 - 禁用今天之前的日期
    const disabledDate = (time) => {
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      return time.getTime() < today.getTime()
    }
    
    // 获取医生排班数据
    const fetchScheduleData = async () => {
      loading.value = true
      try {
        let params = {}
        if (filterDate.value && filterDate.value.length === 2) {
          params = {
            startDate: filterDate.value[0],
            endDate: filterDate.value[1]
          }
        }
        
        console.log('请求排班数据参数:', params)
        const res = await getDoctorSchedules(params)
        console.log('获取排班数据响应:', res)
        
        scheduleList.value = res.data || []
        
        // 处理日期格式，确保与日历组件兼容
        scheduleList.value.forEach(item => {
          if (item.workDate && item.workDate.includes(' ')) {
            // 保留原始日期，同时添加格式化后的日期用于比较
            item.formattedDate = item.workDate.split(' ')[0]
          }
        })
      } catch (error) {
        console.error('获取排班数据失败', error)
        ElMessage.error('获取排班数据失败')
      } finally {
        loading.value = false
      }
    }
    
    // 根据日期获取排班列表
    const getSchedulesByDate = (dateStr) => {
      console.log('当前日期:', dateStr, '所有排班:', scheduleList.value)
      return scheduleList.value.filter(item => {
        // 优先使用格式化后的日期，如果没有则拆分原始日期
        const itemDate = item.formattedDate || (item.workDate && item.workDate.split(' ')[0])
        console.log(`比较: ${itemDate} === ${dateStr} => ${itemDate === dateStr}`)
        return itemDate === dateStr
      })
    }
    
    // 获取患者数量标签类型
    const getPatientCountTagType = (schedule) => {
      const ratio = schedule.reservedNumber / schedule.maxNumber
      if (ratio >= 0.8) return 'danger'
      if (ratio >= 0.5) return 'warning'
      return 'success'
    }
    
    // 检查是否可以删除排班 (7天后的排班并且没有人预约)
    const canDeleteSchedule = (schedule) => {
      // 如果已有预约，不能删除
      if (schedule.reservedNumber > 0) {
        return false
      }
      
      // 计算7天后的日期
      const today = new Date()
      const sevenDaysLater = new Date(today)
      sevenDaysLater.setDate(today.getDate() + 7)
      sevenDaysLater.setHours(0, 0, 0, 0)
      
      // 获取排班日期
      const scheduleDate = new Date(schedule.workDate)
      scheduleDate.setHours(0, 0, 0, 0)
      
      // 只能删除7天后的排班
      return scheduleDate.getTime() >= sevenDaysLater.getTime()
    }
    
    // 新增排班
    const handleAddSchedule = () => {
      isEdit.value = false
      scheduleForm.id = ''
      scheduleForm.workDate = ''
      scheduleForm.timeSlot = ''
      scheduleForm.maxNumber = 20
      scheduleForm.status = 1
      scheduleDialogVisible.value = true
    }
    
    // 编辑排班
    const handleEditSchedule = (schedule) => {
      isEdit.value = true
      scheduleForm.id = schedule.id
      scheduleForm.workDate = schedule.workDate
      scheduleForm.timeSlot = schedule.timeSlot
      scheduleForm.maxNumber = schedule.maxNumber
      scheduleForm.status = schedule.status
      scheduleDialogVisible.value = true
    }
    
    // 删除排班
    const handleDeleteSchedule = (schedule) => {
      if (!canDeleteSchedule(schedule)) {
        ElMessage.warning('只能删除7天后且无预约的排班')
        return
      }
      
      ElMessageBox.confirm('确定要删除该排班吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteDoctorSchedule(schedule.id)
          ElMessage.success('删除成功')
          fetchScheduleData()
        } catch (error) {
          console.error('删除失败', error)
          if (error.response && error.response.data && error.response.data.msg) {
            ElMessage.error(error.response.data.msg)
          } else {
            ElMessage.error('删除失败')
          }
        }
      }).catch(() => {})
    }
    
    // 停诊
    const handleCancelSchedule = (schedule) => {
      ElMessageBox.confirm('确定要将该排班设置为停诊吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const data = {
            id: schedule.id,
            status: 0
          }
          await updateDoctorSchedule(data)
          ElMessage.success('操作成功')
          fetchScheduleData()
        } catch (error) {
          console.error('操作失败', error)
          ElMessage.error('操作失败')
        }
      }).catch(() => {})
    }
    
    // 恢复排班
    const handleResumeSchedule = (schedule) => {
      ElMessageBox.confirm('确定要恢复该排班吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const data = {
            id: schedule.id,
            status: 1
          }
          await updateDoctorSchedule(data)
          ElMessage.success('操作成功')
          fetchScheduleData()
        } catch (error) {
          console.error('操作失败', error)
          ElMessage.error('操作失败')
        }
      }).catch(() => {})
    }
    
    // 提交排班表单
    const submitScheduleForm = async () => {
      if (!scheduleFormRef.value) return
      
      await scheduleFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const data = {
              workDate: scheduleForm.workDate, // 直接使用字符串格式的日期，后端会正确解析
              timeSlot: scheduleForm.timeSlot,
              maxNumber: scheduleForm.maxNumber,
              status: scheduleForm.status
            }
            
            console.log('提交排班数据:', data)
            
            if (isEdit.value) {
              data.id = scheduleForm.id
              await updateDoctorSchedule(data)
            } else {
              await addDoctorSchedule(data)
            }
            
            ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
            scheduleDialogVisible.value = false
            fetchScheduleData()
          } catch (error) {
            console.error('操作失败', error)
            if (error.response && error.response.data && error.response.data.msg) {
              ElMessage.error(error.response.data.msg)
            } else {
              ElMessage.error('操作失败')
            }
          }
        } else {
          return false
        }
      })
    }
    
    onMounted(() => {
      console.log('组件挂载，初始化日期范围')
      const now = new Date()
      const start = new Date(now)
      const end = new Date(now)
      end.setDate(end.getDate() + 30)
      
      // 确保日期格式为 YYYY-MM-DD
      filterDate.value = [
        start.toISOString().split('T')[0],
        end.toISOString().split('T')[0]
      ]
      
      console.log('初始日期范围:', filterDate.value)
      fetchScheduleData()
    })
    
    return {
      loading,
      scheduleList,
      selectedDate,
      filterDate,
      scheduleDialogVisible,
      isEdit,
      scheduleForm,
      scheduleFormRef,
      rules,
      disabledDate,
      fetchScheduleData,
      getSchedulesByDate,
      getPatientCountTagType,
      canDeleteSchedule,
      handleAddSchedule,
      handleEditSchedule,
      handleDeleteSchedule,
      handleCancelSchedule,
      handleResumeSchedule,
      submitScheduleForm
    }
  }
}
</script>

<style scoped>
.schedule-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-container {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.calendar-day {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.date {
  text-align: right;
  margin: 5px;
  font-size: 14px;
}

.date-debug {
  text-align: right;
  margin: 0 5px;
  font-size: 10px;
  color: #999;
}

.schedule-info {
  flex: 1;
  padding: 5px;
  overflow-y: auto;
}

.schedule-item {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 5px;
  margin-bottom: 5px;
  font-size: 12px;
}

.schedule-disabled {
  background-color: #f56c6c20;
  color: #f56c6c;
}

.time-slot {
  font-weight: bold;
  margin-bottom: 5px;
}

.patient-count {
  margin-bottom: 5px;
}

.schedule-actions {
  display: flex;
  justify-content: flex-end;
  gap: 5px;
  margin-top: 5px;
}

.no-schedule {
  color: #909399;
  text-align: center;
  font-size: 12px;
  padding: 10px;
}
</style> 