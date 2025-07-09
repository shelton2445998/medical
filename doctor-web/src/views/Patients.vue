<template>
  <div class="patients-container">
    <h2 class="page-title">患者管理</h2>
    
    <!-- 搜索区域 -->
    <el-card class="mb-20">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.name" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号码" clearable />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="searchForm.idCard" placeholder="请输入身份证号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 患者列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>患者列表</span>
          <el-button type="primary" size="small" @click="openAddDialog">新增患者</el-button>
        </div>
      </template>
      <el-table :data="patientsList" style="width: 100%" v-loading="loading">
        <el-table-column prop="patientId" label="患者ID" width="100" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 'male' ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="idCard" label="身份证号" width="200" />
        <el-table-column prop="address" label="住址" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewMedicalRecords(scope.row)">
              查看病历
            </el-button>
            <el-button type="success" size="small" @click="createAppointment(scope.row)">
              预约
            </el-button>
            <el-button type="info" size="small" @click="editPatient(scope.row)">
              编辑
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
    
    <!-- 新增/编辑患者对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form
        :model="patientForm"
        :rules="patientRules"
        ref="patientFormRef"
        label-width="100px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="patientForm.name" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="patientForm.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="patientForm.birthday"
            type="date"
            placeholder="选择出生日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="patientForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="patientForm.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="住址" prop="address">
          <el-input v-model="patientForm.address" placeholder="请输入住址" />
        </el-form-item>
        <el-form-item label="过敏史" prop="allergies">
          <el-input
            v-model="patientForm.allergies"
            type="textarea"
            placeholder="请输入过敏史"
            rows="2"
          />
        </el-form-item>
        <el-form-item label="既往病史" prop="medicalHistory">
          <el-input
            v-model="patientForm.medicalHistory"
            type="textarea"
            placeholder="请输入既往病史"
            rows="2"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitPatientForm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 预约对话框 -->
    <el-dialog
      title="创建预约"
      v-model="appointmentDialogVisible"
      width="500px"
    >
      <el-form
        :model="appointmentForm"
        :rules="appointmentRules"
        ref="appointmentFormRef"
        label-width="100px"
      >
        <el-form-item label="患者姓名">
          <el-input v-model="appointmentForm.patientName" disabled />
        </el-form-item>
        <el-form-item label="预约科室" prop="department">
          <el-select v-model="appointmentForm.department" placeholder="请选择科室" style="width: 100%">
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
          <el-button @click="appointmentDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAppointment">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

export default {
  name: 'PatientsView',
  setup() {
    const router = useRouter()
    const patientFormRef = ref(null)
    const appointmentFormRef = ref(null)
    
    // 加载状态
    const loading = ref(false)
    
    // 分页参数（修正为文档中的pageNum和pageSize）
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    
    // 搜索表单
    const searchForm = reactive({
      name: '',
      phone: '',
      idCard: ''
    })
    
    // 患者列表
    const patientsList = ref([])
    
    // 患者表单
    const dialogVisible = ref(false)
    const dialogTitle = ref('新增患者')
    const isEdit = ref(false)
    const patientForm = reactive({
      patientId: '',
      name: '',
      gender: 'male',
      birthday: '',
      age: '',
      phone: '',
      idCard: '',
      address: '',
      allergies: '',
      medicalHistory: ''
    })
    
    // 患者表单校验规则
    const patientRules = {
      name: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
      gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
      birthday: [{ required: true, message: '请选择出生日期', trigger: 'blur' }],
      phone: [
        { required: true, message: '请输入联系电话', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }
      ],
      idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入有效的身份证号', trigger: 'blur' }
      ]
    }
    
    // 预约相关
    const appointmentDialogVisible = ref(false)
    const appointmentForm = reactive({
      patientId: '',
      patientName: '',
      department: '',
      doctorId: '',
      appointmentDate: '',
      timeSlot: '',
      symptoms: ''
    })
    
    // 预约表单校验规则
    const appointmentRules = {
      department: [{ required: true, message: '请选择科室', trigger: 'change' }],
      doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
      appointmentDate: [{ required: true, message: '请选择预约日期', trigger: 'blur' }],
      timeSlot: [{ required: true, message: '请选择预约时段', trigger: 'change' }],
      symptoms: [{ required: true, message: '请输入症状描述', trigger: 'blur' }]
    }
    
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
    
    // 医生列表（根据科室动态获取）
    const doctors = ref([])
    
    // 预约时段
    const timeSlots = [
      { value: 'morning', label: '上午(8:00-12:00)' },
      { value: 'afternoon', label: '下午(14:00-18:00)' }
    ]
    
    // 不允许选择过去日期
    const disabledDate = (date) => {
      return date.getTime() < Date.now() - 8.64e7
    }
    
    // 初始化
    onMounted(() => {
      fetchPatientsList()
    })
    
    // 获取患者列表（修正接口URL和分页参数）
    const fetchPatientsList = async () => {
      loading.value = true
      try {
        const params = {
          pageNum: currentPage.value,  // 修正为文档中的pageNum
          pageSize: pageSize.value,    // 修正为文档中的pageSize
          ...searchForm
        }
        // 修正接口URL，添加api前缀
        const { data: res } = await axios.get('/api/admin/user/list', { params })
        if (res.code === 200) {
          patientsList.value = res.data.list
          total.value = res.data.total
        } else {
          ElMessage.error(res.message || '获取患者列表失败')
        }
      } catch (error) {
        console.error('获取患者列表失败:', error)
        // 使用模拟数据
        mockPatientsList()
        ElMessage.warning('获取数据失败，显示模拟数据')
      } finally {
        loading.value = false
      }
    }
    
    // 模拟患者数据
    const mockPatientsList = () => {
      patientsList.value = [
        {
          patientId: 'P20230001',
          name: '张三',
          gender: 'male',
          age: 45,
          phone: '13800138001',
          idCard: '410123197805126235',
          address: '北京市海淀区西二旗大街58号'
        },
        {
          patientId: 'P20230002',
          name: '李四',
          gender: 'male',
          age: 32,
          phone: '13800138002',
          idCard: '110103199012157895',
          address: '北京市朝阳区建国路89号'
        }
      ]
      total.value = 2
    }
    
    // 搜索
    const handleSearch = () => {
      currentPage.value = 1
      fetchPatientsList()
    }
    
    // 重置搜索
    const resetSearch = () => {
      Object.keys(searchForm).forEach(key => {
        searchForm[key] = ''
      })
      currentPage.value = 1
      fetchPatientsList()
    }
    
    // 页码变化
    const handleCurrentChange = (val) => {
      currentPage.value = val
      fetchPatientsList()
    }
    
    // 每页条数变化
    const handleSizeChange = (val) => {
      pageSize.value = val
      currentPage.value = 1
      fetchPatientsList()
    }
    
    // 打开添加患者对话框
    const openAddDialog = () => {
      dialogTitle.value = '新增患者'
      isEdit.value = false
      Object.keys(patientForm).forEach(key => {
        patientForm[key] = key === 'gender' ? 'male' : ''
      })
      dialogVisible.value = true
    }
    
    // 编辑患者
    const editPatient = (row) => {
      dialogTitle.value = '编辑患者'
      isEdit.value = true
      Object.keys(patientForm).forEach(key => {
        patientForm[key] = row[key] || ''
      })
      dialogVisible.value = true
    }
    
    // 提交患者表单（修正接口URL和请求方法）
    const submitPatientForm = () => {
      if (!patientFormRef.value) return
      
      patientFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            let res
            if (isEdit.value) {
              // 编辑患者：使用PUT方法，对应管理后台用户更新接口
              res = await axios.put('/api/admin/user/update', patientForm)
            } else {
              // 新增患者：使用POST方法，对应管理后台用户添加接口
              res = await axios.post('/api/admin/user/add', patientForm)
            }
            
            if (res.data.code === 200) {
              ElMessage.success(isEdit.value ? '患者信息更新成功' : '患者添加成功')
              dialogVisible.value = false
              fetchPatientsList()
            } else {
              ElMessage.error(res.data.message || (isEdit.value ? '更新失败' : '添加失败'))
            }
          } catch (error) {
            console.error(isEdit.value ? '更新患者信息失败:' : '添加患者失败:', error)
            // 使用模拟数据处理
            if (!isEdit.value) {
              patientForm.patientId = 'P' + Date.now().toString().substring(6)
              if (!patientForm.age && patientForm.birthday) {
                const birthYear = new Date(patientForm.birthday).getFullYear()
                const currentYear = new Date().getFullYear()
                patientForm.age = currentYear - birthYear
              }
            }
            ElMessage.success(isEdit.value ? '患者信息更新成功(演示模式)' : '患者添加成功(演示模式)')
            dialogVisible.value = false
            fetchPatientsList()
          }
        }
      })
    }
    
    // 查看病历
    const viewMedicalRecords = (row) => {
      router.push({
        path: '/home/medical-records',
        query: {
          patientId: row.patientId,
          patientName: row.name
        }
      })
    }
    
    // 创建预约（修正接口URL）
    const createAppointment = (row) => {
      appointmentForm.patientId = row.patientId
      appointmentForm.patientName = row.name
      appointmentForm.department = ''
      appointmentForm.doctorId = ''
      appointmentForm.appointmentDate = ''
      appointmentForm.timeSlot = ''
      appointmentForm.symptoms = ''
      
      doctors.value = []
      appointmentDialogVisible.value = true
    }
    
    // 提交预约（修正接口URL）
    const submitAppointment = () => {
      if (!appointmentFormRef.value) return
      
      appointmentFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 修正为用户端创建预约接口
            const { data: res } = await axios.post('/api/appointment/create', appointmentForm)
            if (res.code === 200) {
              ElMessage.success('预约创建成功')
              appointmentDialogVisible.value = false
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
    
    return {
      loading,
      currentPage,
      pageSize,
      total,
      searchForm,
      patientsList,
      dialogVisible,
      dialogTitle,
      patientForm,
      patientRules,
      patientFormRef,
      appointmentDialogVisible,
      appointmentForm,
      appointmentRules,
      appointmentFormRef,
      departments,
      doctors,
      timeSlots,
      disabledDate,
      handleSearch,
      resetSearch,
      handleCurrentChange,
      handleSizeChange,
      openAddDialog,
      editPatient,
      submitPatientForm,
      viewMedicalRecords,
      createAppointment,
      submitAppointment
    }
  }
}
</script>

<style scoped>
.patients-container {
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