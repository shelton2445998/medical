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
        <div class="week-navigation">
          <el-button type="default" @click="prevWeek">
            <el-icon><ArrowLeft /></el-icon> 上一周
          </el-button>
          <span class="week-display">{{ weekRangeDisplay }}</span>
          <el-button type="default" @click="nextWeek">
            下一周 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        <el-button type="primary" @click="fetchScheduleData">刷新</el-button>
      </div>

      <div class="week-calendar">
        <div class="week-header">
          <div v-for="(day, index) in weekDays" :key="index" class="week-day-header">
            {{ day.dayOfWeek }}<br>{{ day.dateStr }}
          </div>
        </div>
        <div class="week-body">
          <div 
            v-for="(day, index) in weekDays" 
            :key="index" 
            class="week-day" 
            :class="{ 
              'has-schedule': hasSchedule(day.fullDate),
              'selected-day': selectedDay === day.fullDate
            }"
            @click="selectDay(day.fullDate)"
          >
            <div v-for="schedule in getSchedulesByDate(day.fullDate)" :key="schedule.id" class="schedule-item-mini">
              <span :class="{ 'disabled-schedule': schedule.status === false }">
                {{ schedule.timeSlot.substring(0, 2) }} ({{ schedule.reservedNumber }}/{{ schedule.maxNumber }})
              </span>
            </div>
            <div v-if="getSchedulesByDate(day.fullDate).length === 0" class="no-schedule-mini">
              无排班
            </div>
          </div>
        </div>
      </div>

      <div v-if="selectedDay" class="day-detail">
        <div class="day-detail-header">
          <h3>{{ formatSelectedDay }} 排班详情</h3>
          <el-button type="primary" size="small" @click="handleAddScheduleForDay">
            <el-icon><Plus /></el-icon> 添加排班
          </el-button>
        </div>
        <div v-if="getSchedulesByDate(selectedDay).length === 0" class="no-schedule-detail">
          该日无排班，请点击添加排班按钮创建新排班。
        </div>
        <div v-else class="schedule-list">
          <div 
            v-for="schedule in getSchedulesByDate(selectedDay)" 
            :key="schedule.id" 
            class="schedule-detail-item"
            :class="{ 'schedule-disabled': schedule.status === false }"
          >
            <div class="schedule-detail-info">
              <div class="time-slot">{{ schedule.timeSlot }}</div>
              <div class="patient-count">
                <el-tag :type="getPatientCountTagType(schedule)">
                  已预约: {{ schedule.reservedNumber }} / {{ schedule.maxNumber }}
                </el-tag>
                <el-tag :type="schedule.status === true ? 'success' : 'danger'" class="status-tag">
                  {{ schedule.status === true ? '正常' : '停诊' }}
                </el-tag>
              </div>
            </div>
            <div class="schedule-actions">
              <el-button
                v-if="schedule.status === true"
                type="danger"
                size="small"
                @click="handleCancelSchedule(schedule)"
              >
                停诊
              </el-button>
              <el-button
                v-else
                type="success"
                size="small"
                @click="handleResumeSchedule(schedule)"
              >
                恢复
              </el-button>
              <el-button
                type="primary"
                size="small"
                @click="handleEditSchedule(schedule)"
              >
                编辑
              </el-button>
              <el-button
                v-if="canDeleteSchedule(schedule)"
                type="danger"
                size="small"
                @click="handleDeleteSchedule(schedule)"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>
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
import { Plus, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { getDoctorSchedules, addDoctorSchedule, updateDoctorSchedule, deleteDoctorSchedule } from '@/api/doctor'

export default {
  name: 'Schedule',
  components: {
    Plus,
    ArrowLeft,
    ArrowRight
  },
  setup() {
    const loading = ref(false)
    const scheduleList = ref([])
    const selectedDate = ref(new Date())
    const filterDate = ref('')
    const scheduleDialogVisible = ref(false)
    const isEdit = ref(false)
    const scheduleFormRef = ref(null)
    const selectedDay = ref(null) // 当前选中的日期
    const currentWeekStart = ref(new Date())
    
    // 计算当前显示的周日期
    const weekDays = computed(() => {
      const days = []
      const startOfWeek = new Date(currentWeekStart.value)
      
      const weekdayNames = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
      
      for (let i = 0; i < 7; i++) {
        const currentDate = new Date(startOfWeek)
        currentDate.setDate(startOfWeek.getDate() + i)
        
        const year = currentDate.getFullYear()
        const month = String(currentDate.getMonth() + 1).padStart(2, '0')
        const day = String(currentDate.getDate()).padStart(2, '0')
        const dateStr = `${month}-${day}`
        const fullDate = `${year}-${month}-${day}`
        const dayOfWeek = weekdayNames[currentDate.getDay()]
        
        days.push({
          date: currentDate,
          dateStr,
          fullDate,
          dayOfWeek
        })
      }
      
      return days
    })
    
    // 当前周显示的日期范围
    const weekRangeDisplay = computed(() => {
      if (weekDays.value.length === 0) return ''
      
      const firstDay = weekDays.value[0].date
      const lastDay = weekDays.value[6].date
      
      const firstMonth = firstDay.getMonth() + 1
      const firstDate = firstDay.getDate()
      const lastMonth = lastDay.getMonth() + 1
      const lastDate = lastDay.getDate()
      
      return `${firstMonth}月${firstDate}日 - ${lastMonth}月${lastDate}日`
    })
    
    // 格式化选中日期显示
    const formatSelectedDay = computed(() => {
      if (!selectedDay.value) return ''
      
      const date = new Date(selectedDay.value)
      const month = date.getMonth() + 1
      const day = date.getDate()
      const weekday = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][date.getDay()]
      
      return `${month}月${day}日 (${weekday})`
    })
    
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
    
    // 设置周的开始时间为本周的周一
    const setWeekToCurrentMonday = () => {
      const now = new Date()
      const dayOfWeek = now.getDay() // 0是周日，1是周一
      const diff = dayOfWeek === 0 ? -6 : 1 - dayOfWeek // 调整到周一
      
      const monday = new Date(now)
      monday.setDate(now.getDate() + diff)
      monday.setHours(0, 0, 0, 0)
      
      currentWeekStart.value = monday
    }
    
    // 上一周
    const prevWeek = () => {
      const newStart = new Date(currentWeekStart.value)
      newStart.setDate(newStart.getDate() - 7)
      currentWeekStart.value = newStart
      selectedDay.value = null // 切换周时清除选中日期
      fetchScheduleDataForCurrentWeek()
    }
    
    // 下一周
    const nextWeek = () => {
      const newStart = new Date(currentWeekStart.value)
      newStart.setDate(newStart.getDate() + 7)
      currentWeekStart.value = newStart
      selectedDay.value = null // 切换周时清除选中日期
      fetchScheduleDataForCurrentWeek()
    }
    
    // 选择日期
    const selectDay = (day) => {
      selectedDay.value = day
    }
    
    // 判断某天是否有排班
    const hasSchedule = (dateStr) => {
      return getSchedulesByDate(dateStr).length > 0
    }
    
    // 为选中的日期添加排班
    const handleAddScheduleForDay = () => {
      if (!selectedDay.value) return
      
      isEdit.value = false
      scheduleForm.id = ''
      scheduleForm.workDate = selectedDay.value
      scheduleForm.timeSlot = ''
      scheduleForm.maxNumber = 20
      scheduleForm.status = true
      scheduleDialogVisible.value = true
    }
    
    // 日期禁用函数 - 禁用今天之前的日期
    const disabledDate = (time) => {
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      return time.getTime() < today.getTime()
    }
    
    // 获取指定周的排班数据
    const fetchScheduleDataForCurrentWeek = async () => {
      const startDay = new Date(currentWeekStart.value)
      const endDay = new Date(currentWeekStart.value)
      endDay.setDate(endDay.getDate() + 6)
      
      // 确保日期格式为 YYYY-MM-DD
      filterDate.value = [
        startDay.toISOString().split('T')[0],
        endDay.toISOString().split('T')[0]
      ]
      
      await fetchScheduleData()
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
      return scheduleList.value.filter(item => {
        // 优先使用格式化后的日期，如果没有则拆分原始日期
        const itemDate = item.formattedDate || (item.workDate && item.workDate.split(' ')[0])
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
      scheduleForm.status = true
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
          fetchScheduleDataForCurrentWeek()
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
            status: false
          }
          await updateDoctorSchedule(data)
          ElMessage.success('操作成功')
          fetchScheduleDataForCurrentWeek()
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
            status: true
          }
          await updateDoctorSchedule(data)
          ElMessage.success('操作成功')
          fetchScheduleDataForCurrentWeek()
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
              workDate: scheduleForm.workDate,
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
            fetchScheduleDataForCurrentWeek()
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
      setWeekToCurrentMonday()
      fetchScheduleDataForCurrentWeek()
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
      submitScheduleForm,
      weekDays,
      weekRangeDisplay,
      prevWeek,
      nextWeek,
      selectedDay,
      selectDay,
      formatSelectedDay,
      hasSchedule,
      handleAddScheduleForDay
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
  justify-content: space-between;
  align-items: center;
}

.week-navigation {
  display: flex;
  align-items: center;
  gap: 15px;
}

.week-display {
  font-size: 16px;
  font-weight: 500;
  min-width: 180px;
  text-align: center;
}

.week-calendar {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.week-header {
  display: flex;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.week-day-header {
  flex: 1;
  text-align: center;
  padding: 10px;
  font-weight: 500;
}

.week-body {
  display: flex;
  height: 120px;
}

.week-day {
  flex: 1;
  border-right: 1px solid #ebeef5;
  padding: 8px;
  cursor: pointer;
  background-color: white;
  overflow-y: auto;
  position: relative;
}

.week-day:last-child {
  border-right: none;
}

.week-day:hover {
  background-color: #f5f7fa;
}

.has-schedule {
  background-color: #ecf5ff;
}

.selected-day {
  background-color: #b3d8ff !important;
}

.schedule-item-mini {
  font-size: 12px;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.no-schedule-mini {
  color: #909399;
  font-size: 12px;
  text-align: center;
  margin-top: 30px;
}

.disabled-schedule {
  color: #f56c6c;
  text-decoration: line-through;
}

.day-detail {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  margin-top: 20px;
}

.day-detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.no-schedule-detail {
  text-align: center;
  color: #909399;
  padding: 20px;
}

.schedule-detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 10px;
  background-color: #f5f7fa;
}

.schedule-disabled {
  background-color: rgba(245, 108, 108, 0.1);
}

.schedule-detail-info {
  flex: 1;
}

.status-tag {
  margin-left: 8px;
}

.time-slot {
  font-weight: bold;
  margin-bottom: 8px;
}

.schedule-actions {
  display: flex;
  gap: 5px;
}
</style> 