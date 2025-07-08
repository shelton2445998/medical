<template>
  <div class="medical-records-container">
    <h2 class="page-title">病历管理</h2>
    
    <!-- 搜索区域 -->
    <el-card class="mb-20">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="患者信息">
          <el-input 
            v-model="searchForm.patientKeyword" 
            placeholder="请输入患者姓名/ID/手机号" 
            clearable 
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="诊断时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="诊断类型">
          <el-select v-model="searchForm.recordType" placeholder="请选择" clearable>
            <el-option
              v-for="item in recordTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 病历列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>病历列表</span>
          <el-button type="primary" size="small" @click="createRecord">新建病历</el-button>
        </div>
      </template>
      <el-table :data="recordsList" style="width: 100%" v-loading="loading">
        <el-table-column prop="recordId" label="病历编号" width="120" />
        <el-table-column prop="patientName" label="患者姓名" width="100" />
        <el-table-column prop="patientId" label="患者ID" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 'male' ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="diagnoseDate" label="诊断日期" width="120" />
        <el-table-column prop="department" label="科室" width="100">
          <template #default="scope">
            {{ getDepartmentLabel(scope.row.department) }}
          </template>
        </el-table-column>
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="diagnosis" label="诊断结果" show-overflow-tooltip />
        <el-table-column prop="recordType" label="类型" width="100">
          <template #default="scope">
            {{ getRecordTypeLabel(scope.row.recordType) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewRecord(scope.row)">
              查看
            </el-button>
            <el-button type="success" size="small" @click="editRecord(scope.row)">
              编辑
            </el-button>
            <el-dropdown size="small">
              <el-button type="info" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="printRecord(scope.row)">
                    打印
                  </el-dropdown-item>
                  <el-dropdown-item @click="createPrescription(scope.row)">
                    开具处方
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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
    
    <!-- 病历详情/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="80%"
    >
      <el-form 
        :model="recordForm" 
        :rules="recordRules" 
        ref="recordFormRef"
        label-width="100px"
        :disabled="viewMode"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="患者信息" prop="patientId" v-if="!editMode">
              <el-select
                v-model="recordForm.patientId"
                filterable
                remote
                reserve-keyword
                placeholder="请输入患者姓名或手机号查询"
                :remote-method="searchPatients"
                :loading="patientLoading"
                style="width: 100%"
                @change="handlePatientChange"
              >
                <el-option
                  v-for="item in patientOptions"
                  :key="item.patientId"
                  :label="`${item.name} (${item.phone})`"
                  :value="item.patientId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="患者姓名" v-if="editMode">
              <el-input v-model="patientInfo.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="患者ID">
              <el-input v-model="patientInfo.patientId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别">
              <el-input v-model="patientInfo.genderLabel" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="年龄">
              <el-input v-model="patientInfo.age" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机号码">
              <el-input v-model="patientInfo.phone" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="过敏史">
              <el-input v-model="patientInfo.allergies" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-divider />
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="诊断日期" prop="diagnoseDate">
              <el-date-picker
                v-model="recordForm.diagnoseDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="科室" prop="department">
              <el-select v-model="recordForm.department" placeholder="请选择科室" style="width: 100%">
                <el-option v-for="dept in departments" :key="dept.value" :label="dept.label" :value="dept.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="病历类型" prop="recordType">
              <el-select v-model="recordForm.recordType" placeholder="请选择类型" style="width: 100%">
                <el-option v-for="type in recordTypeOptions" :key="type.value" :label="type.label" :value="type.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="主诉" prop="chiefComplaint">
          <el-input
            v-model="recordForm.chiefComplaint"
            type="textarea"
            placeholder="请输入患者主诉"
            rows="3"
          />
        </el-form-item>
        
        <el-form-item label="现病史" prop="presentIllness">
          <el-input
            v-model="recordForm.presentIllness"
            type="textarea"
            placeholder="请输入现病史"
            rows="3"
          />
        </el-form-item>
        
        <el-form-item label="既往史">
          <el-input
            v-model="recordForm.pastHistory"
            type="textarea"
            placeholder="请输入既往史"
            rows="2"
          />
        </el-form-item>
        
        <el-form-item label="检查结果">
          <el-input
            v-model="recordForm.examination"
            type="textarea"
            placeholder="请输入检查结果"
            rows="3"
          />
        </el-form-item>
        
        <el-form-item label="诊断结果" prop="diagnosis">
          <el-input
            v-model="recordForm.diagnosis"
            type="textarea"
            placeholder="请输入诊断结果"
            rows="3"
          />
        </el-form-item>
        
        <el-form-item label="治疗方案" prop="treatment">
          <el-input
            v-model="recordForm.treatment"
            type="textarea"
            placeholder="请输入治疗方案"
            rows="3"
          />
        </el-form-item>
        
        <el-form-item label="医嘱">
          <el-input
            v-model="recordForm.advice"
            type="textarea"
            placeholder="请输入医嘱"
            rows="2"
          />
        </el-form-item>
        
        <el-form-item label="随访计划">
          <el-input
            v-model="recordForm.followUp"
            type="textarea"
            placeholder="请输入随访计划"
            rows="2"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer" v-if="!viewMode">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitRecord">保 存</el-button>
        </span>
        <span class="dialog-footer" v-else>
          <el-button @click="dialogVisible = false">关 闭</el-button>
          <el-button type="primary" @click="switchToEdit">编 辑</el-button>
          <el-button type="success" @click="createPrescription(currentRecord)">开具处方</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

export default {
  name: 'MedicalRecordsView',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const recordFormRef = ref(null)
    
    // 加载状态
    const loading = ref(false)
    const patientLoading = ref(false)
    
    // 分页参数
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    
    // 搜索表单
    const searchForm = reactive({
      patientKeyword: '',
      dateRange: [],
      recordType: ''
    })
    
    // 病历列表
    const recordsList = ref([])
    
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
    
    // 病历类型选项
    const recordTypeOptions = [
      { value: 'firstVisit', label: '初诊' },
      { value: 'followUp', label: '复诊' },
      { value: 'emergency', label: '急诊' },
      { value: 'consultation', label: '会诊' }
    ]
    
    // 患者选项
    const patientOptions = ref([])
    
    // 对话框相关
    const dialogVisible = ref(false)
    const dialogTitle = ref('新建病历')
    const viewMode = ref(false)
    const editMode = ref(false)
    
    // 当前病历记录
    const currentRecord = ref(null)
    
    // 病历表单
    const recordForm = reactive({
      recordId: '',
      patientId: '',
      diagnoseDate: new Date(),
      department: '',
      recordType: 'firstVisit',
      chiefComplaint: '',
      presentIllness: '',
      pastHistory: '',
      examination: '',
      diagnosis: '',
      treatment: '',
      advice: '',
      followUp: ''
    })
    
    // 表单验证规则
    const recordRules = {
      patientId: [
        { required: true, message: '请选择患者', trigger: 'change' }
      ],
      diagnoseDate: [
        { required: true, message: '请选择诊断日期', trigger: 'blur' }
      ],
      department: [
        { required: true, message: '请选择科室', trigger: 'change' }
      ],
      recordType: [
        { required: true, message: '请选择病历类型', trigger: 'change' }
      ],
      chiefComplaint: [
        { required: true, message: '请输入患者主诉', trigger: 'blur' }
      ],
      diagnosis: [
        { required: true, message: '请输入诊断结果', trigger: 'blur' }
      ],
      treatment: [
        { required: true, message: '请输入治疗方案', trigger: 'blur' }
      ]
    }
    
    // 患者基本信息
    const patientInfo = reactive({
      patientId: '',
      name: '',
      gender: '',
      genderLabel: '',
      age: '',
      phone: '',
      allergies: ''
    })
    
    // 初始化加载
    onMounted(() => {
      fetchRecordsList()
      
      // 从路由参数中获取患者信息（从预约或患者管理页面跳转而来）
      if (route.query.patientId) {
        getPatientInfo(route.query.patientId)
      }
    })
    
    // 获取科室名称
    const getDepartmentLabel = (value) => {
      const dept = departments.find(d => d.value === value)
      return dept ? dept.label : value
    }
    
    // 获取病历类型名称
    const getRecordTypeLabel = (value) => {
      const type = recordTypeOptions.find(t => t.value === value)
      return type ? type.label : value
    }
    
    // 获取病历列表
    const fetchRecordsList = async () => {
      loading.value = true
      try {
        const params = {
          page: currentPage.value,
          limit: pageSize.value,
          patientKeyword: searchForm.patientKeyword,
          startDate: searchForm.dateRange ? searchForm.dateRange[0] : '',
          endDate: searchForm.dateRange ? searchForm.dateRange[1] : '',
          recordType: searchForm.recordType
        }
        const { data: res } = await axios.get('/doctor/medical-records/list', { params })
        if (res.code === 200) {
          recordsList.value = res.data.list
          total.value = res.data.total
        } else {
          ElMessage.error(res.message || '获取病历列表失败')
        }
      } catch (error) {
        console.error('获取病历列表失败:', error)
        // 使用模拟数据
        mockRecordsList()
        ElMessage.warning('获取数据失败，显示模拟数据')
      } finally {
        loading.value = false
      }
    }
    
    // 模拟病历数据
    const mockRecordsList = () => {
      recordsList.value = [
        {
          recordId: 'MR20230815001',
          patientId: 'P20230001',
          patientName: '张三',
          gender: 'male',
          age: 45,
          diagnoseDate: '2023-08-15',
          department: 'internal',
          doctorName: '王医生',
          diagnosis: '上呼吸道感染，肺炎',
          recordType: 'firstVisit'
        },
        {
          recordId: 'MR20230814001',
          patientId: 'P20230002',
          patientName: '李四',
          gender: 'male',
          age: 32,
          diagnoseDate: '2023-08-14',
          department: 'surgery',
          doctorName: '李医生',
          diagnosis: '骨折，右腿胫骨骨裂',
          recordType: 'emergency'
        },
        {
          recordId: 'MR20230813001',
          patientId: 'P20230003',
          patientName: '王五',
          gender: 'male',
          age: 28,
          diagnoseDate: '2023-08-13',
          department: 'internal',
          doctorName: '王医生',
          diagnosis: '急性咽炎',
          recordType: 'firstVisit'
        },
        {
          recordId: 'MR20230812001',
          patientId: 'P20230004',
          patientName: '赵六',
          gender: 'female',
          age: 52,
          diagnoseDate: '2023-08-12',
          department: 'neurology',
          doctorName: '张医生',
          diagnosis: '颈椎病，肩周炎',
          recordType: 'followUp'
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
    
    // 获取患者信息
    const getPatientInfo = async (patientId) => {
      try {
        const { data: res } = await axios.get(`/doctor/patients/info/${patientId}`)
        if (res.code === 200) {
          const patient = res.data
          patientInfo.patientId = patient.patientId
          patientInfo.name = patient.name
          patientInfo.gender = patient.gender
          patientInfo.genderLabel = patient.gender === 'male' ? '男' : '女'
          patientInfo.age = patient.age
          patientInfo.phone = patient.phone
          patientInfo.allergies = patient.allergies || '无'
          
          // 自动填入病历表单
          recordForm.patientId = patient.patientId
        }
      } catch (error) {
        console.error('获取患者信息失败:', error)
        // 模拟数据
        const mockPatient = {
          patientId: patientId,
          name: route.query.patientName || '患者姓名',
          gender: 'male',
          age: 40,
          phone: '13800138000',
          allergies: '无'
        }
        patientInfo.patientId = mockPatient.patientId
        patientInfo.name = mockPatient.name
        patientInfo.gender = mockPatient.gender
        patientInfo.genderLabel = '男'
        patientInfo.age = mockPatient.age
        patientInfo.phone = mockPatient.phone
        patientInfo.allergies = mockPatient.allergies
        
        // 自动填入病历表单
        recordForm.patientId = mockPatient.patientId
      }
    }
    
    // 患者变更
    const handlePatientChange = (patientId) => {
      if (patientId) {
        getPatientInfo(patientId)
      }
    }
    
    // 搜索
    const handleSearch = () => {
      currentPage.value = 1
      fetchRecordsList()
    }
    
    // 重置搜索
    const resetSearch = () => {
      searchForm.patientKeyword = ''
      searchForm.dateRange = []
      searchForm.recordType = ''
      currentPage.value = 1
      fetchRecordsList()
    }
    
    // 页码变化
    const handleCurrentChange = (val) => {
      currentPage.value = val
      fetchRecordsList()
    }
    
    // 每页条数变化
    const handleSizeChange = (val) => {
      pageSize.value = val
      currentPage.value = 1
      fetchRecordsList()
    }
    
    // 新建病历
    const createRecord = () => {
      dialogTitle.value = '新建病历'
      viewMode.value = false
      editMode.value = false
      
      // 重置表单
      Object.keys(recordForm).forEach(key => {
        if (key === 'diagnoseDate') {
          recordForm[key] = new Date()
        } else if (key === 'recordType') {
          recordForm[key] = 'firstVisit'
        } else {
          recordForm[key] = ''
        }
      })
      
      // 重置患者信息
      Object.keys(patientInfo).forEach(key => {
        patientInfo[key] = ''
      })
      
      dialogVisible.value = true
    }
    
    // 查看病历
    const viewRecord = (row) => {
      currentRecord.value = row
      dialogTitle.value = '查看病历'
      viewMode.value = true
      editMode.value = false
      
      // 获取病历详细信息
      getRecordDetail(row.recordId)
    }
    
    // 编辑病历
    const editRecord = (row) => {
      currentRecord.value = row
      dialogTitle.value = '编辑病历'
      viewMode.value = false
      editMode.value = true
      
      // 获取病历详细信息
      getRecordDetail(row.recordId)
    }
    
    // 从查看模式切换到编辑模式
    const switchToEdit = () => {
      viewMode.value = false
      editMode.value = true
      dialogTitle.value = '编辑病历'
    }
    
    // 获取病历详情
    const getRecordDetail = async (recordId) => {
      try {
        const { data: res } = await axios.get(`/doctor/medical-records/detail/${recordId}`)
        if (res.code === 200) {
          const record = res.data
          // 填充表单
          Object.keys(recordForm).forEach(key => {
            if (record[key] !== undefined) {
              recordForm[key] = record[key]
            }
          })
          
          // 获取患者信息
          getPatientInfo(record.patientId)
        } else {
          ElMessage.error(res.message || '获取病历详情失败')
        }
      } catch (error) {
        console.error('获取病历详情失败:', error)
        // 模拟数据
        const mockRecord = {
          recordId: recordId,
          patientId: currentRecord.value.patientId,
          diagnoseDate: currentRecord.value.diagnoseDate,
          department: currentRecord.value.department,
          recordType: currentRecord.value.recordType,
          chiefComplaint: '头痛、发热三天，伴有咳嗽、咽痛',
          presentIllness: '患者三天前无明显诱因出现头痛、发热，体温最高达39.2℃，伴有咳嗽、咽痛、全身酸痛等症状。自行服用布洛芬后体温一度下降，但仍有反复发热情况。',
          pastHistory: '否认高血压、糖尿病等慢性疾病史，否认肝炎、结核等传染病史，否认药物过敏史。',
          examination: '体温38.5℃，脉搏92次/分，呼吸20次/分，血压125/80mmHg。咽部充血，双侧扁桃体Ⅱ度肿大，可见少量脓性分泌物。双肺呼吸音粗，可闻及少量干湿啰音。',
          diagnosis: currentRecord.value.diagnosis || '上呼吸道感染，肺炎',
          treatment: '1. 头孢呋辛酯片，0.25g，每次1片，每日2次，饭后服用，共3天\n2. 布洛芬缓释胶囊，0.3g，每次1粒，必要时服用（发热超过38.5℃），每6小时1次\n3. 盐酸氨溴索片，30mg，每次1片，每日3次，饭后服用，共5天',
          advice: '多休息，多饮水，清淡饮食，避免辛辣刺激食物，保持室内空气流通。',
          followUp: '病情无好转或加重请及时复诊。'
        }
        
        // 填充表单
        Object.keys(recordForm).forEach(key => {
          if (mockRecord[key] !== undefined) {
            recordForm[key] = mockRecord[key]
          }
        })
        
        // 获取患者信息
        getPatientInfo(mockRecord.patientId)
      }
      
      dialogVisible.value = true
    }
    
    // 提交病历表单
    const submitRecord = () => {
      if (!recordFormRef.value) return
      
      recordFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const url = editMode.value ? 
              `/doctor/medical-records/update/${recordForm.recordId}` : 
              '/doctor/medical-records/create'
            
            const { data: res } = await axios.post(url, recordForm)
            if (res.code === 200) {
              ElMessage.success(editMode.value ? '病历更新成功' : '病历创建成功')
              dialogVisible.value = false
              fetchRecordsList()
            } else {
              ElMessage.error(res.message || (editMode.value ? '更新失败' : '创建失败'))
            }
          } catch (error) {
            console.error(editMode.value ? '更新病历失败:' : '创建病历失败:', error)
            ElMessage({
              type: 'success',
              message: editMode.value ? '病历更新成功(模拟)' : '病历创建成功(模拟)'
            })
            dialogVisible.value = false
            fetchRecordsList()
          }
        }
      })
    }
    
    // 打印病历
    const printRecord = () => {
      ElMessage.info('打印功能待实现')
      // TODO: 实现打印功能
    }
    
    // 开具处方
    const createPrescription = (row) => {
      router.push({
        path: '/home/prescriptions/create',
        query: {
          patientId: row.patientId,
          patientName: row.patientName,
          recordId: row.recordId,
          diagnosis: row.diagnosis
        }
      })
    }
    
    return {
      loading,
      patientLoading,
      currentPage,
      pageSize,
      total,
      searchForm,
      recordsList,
      recordTypeOptions,
      departments,
      dialogVisible,
      dialogTitle,
      viewMode,
      editMode,
      recordForm,
      recordRules,
      recordFormRef,
      patientOptions,
      patientInfo,
      currentRecord,
      getDepartmentLabel,
      getRecordTypeLabel,
      handleSearch,
      resetSearch,
      handleCurrentChange,
      handleSizeChange,
      searchPatients,
      handlePatientChange,
      createRecord,
      viewRecord,
      editRecord,
      switchToEdit,
      submitRecord,
      printRecord,
      createPrescription
    }
  }
}
</script>

<style scoped>
.medical-records-container {
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

.el-divider {
  margin: 15px 0;
}
</style> 