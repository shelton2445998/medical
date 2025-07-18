<!--
  医疗记录管理页面组件
  
  提供医疗记录的查询、新建、编辑、查看功能
  支持按患者信息、诊断时间、诊断类型搜索记录
  可以查看病历详情、管理诊断信息和治疗方案
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="medical-records-container">
    <!-- 页面标题 -->
    <h2 class="page-title">病历管理</h2>
    
    <!-- 搜索区域 -->
    <el-card class="mb-20">
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <!-- 患者信息搜索 -->
        <el-form-item label="患者信息">
          <el-input 
            v-model="searchForm.patientKeyword" 
            placeholder="请输入患者姓名/ID/手机号" 
            clearable 
            style="width: 220px"
          />
        </el-form-item>
        <!-- 诊断时间范围选择 -->
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
        <!-- 诊断类型筛选 -->
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
        <!-- 搜索按钮 -->
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 病历列表 -->
    <el-card>
      <!-- 卡片头部 -->
      <template #header>
        <div class="card-header">
          <span>病历列表</span>
          <el-button type="primary" size="small" @click="createRecord">新建病历</el-button>
        </div>
      </template>
      
      <!-- 病历信息表格 -->
      <el-table :data="recordsList" style="width: 100%" v-loading="loading">
        <!-- 病历编号列 -->
        <el-table-column prop="recordId" label="病历编号" width="120" />
        <!-- 患者姓名列 -->
        <el-table-column prop="patientName" label="患者姓名" width="100" />
        <!-- 患者ID列 -->
        <el-table-column prop="patientId" label="患者ID" width="120" />
        <!-- 性别列 -->
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 'male' ? '男' : scope.row.gender === 'female' ? '女' : '未知' }}
          </template>
        </el-table-column>
        <!-- 年龄列 -->
        <el-table-column prop="age" label="年龄" width="80" />
        <!-- 诊断日期列 -->
        <el-table-column prop="diagnoseDate" label="诊断日期" width="120" />
        <!-- 科室列 -->
        <el-table-column prop="department" label="科室" width="100">
          <template #default="scope">
            {{ getDepartmentLabel(scope.row.department) }}
          </template>
        </el-table-column>
        <!-- 医生姓名列 -->
        <el-table-column prop="doctorName" label="医生" width="100" />
        <!-- 诊断结果列 -->
        <el-table-column prop="diagnosis" label="诊断结果" show-overflow-tooltip />
        <!-- 病历类型列 -->
        <el-table-column prop="recordType" label="类型" width="100">
          <template #default="scope">
            {{ getRecordTypeLabel(scope.row.recordType) }}
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <!-- 查看按钮 -->
            <el-button type="primary" size="small" @click="viewRecord(scope.row)">
              查看
            </el-button>
            <!-- 编辑按钮 -->
            <el-button type="success" size="small" @click="editRecord(scope.row)">
              编辑
            </el-button>
            <!-- 更多操作下拉菜单 -->
            <el-dropdown size="small">
              <el-button type="info" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <!-- 下拉菜单项 -->
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
    
    <!-- 病历详情/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="80%"
    >
      <!-- 病历表单 -->
      <el-form 
        :model="recordForm" 
        :rules="recordRules" 
        ref="recordFormRef"
        label-width="100px"
        :disabled="viewMode"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <!-- 患者信息选择（新建模式） -->
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
            <!-- 患者姓名显示（编辑模式） -->
            <el-form-item label="患者姓名" v-if="editMode">
              <el-input v-model="patientInfo.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <!-- 患者ID显示 -->
            <el-form-item label="患者ID">
              <el-input v-model="patientInfo.patientId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <!-- 性别显示 -->
            <el-form-item label="性别">
              <el-input v-model="patientInfo.genderLabel" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <!-- 年龄显示 -->
            <el-form-item label="年龄">
              <el-input v-model="patientInfo.age" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <!-- 手机号码显示 -->
            <el-form-item label="手机号码">
              <el-input v-model="patientInfo.phone" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <!-- 过敏史显示 -->
            <el-form-item label="过敏史">
              <el-input v-model="patientInfo.allergies" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 分隔线 -->
        <el-divider />
        
        <el-row :gutter="20">
          <el-col :span="8">
            <!-- 诊断日期选择 -->
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
            <!-- 科室选择 -->
            <el-form-item label="科室" prop="department">
              <el-select v-model="recordForm.department" placeholder="请选择科室" style="width: 100%">
                <el-option v-for="dept in departments" :key="dept.value" :label="dept.label" :value="dept.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <!-- 病历类型选择 -->
            <el-form-item label="病历类型" prop="recordType">
              <el-select v-model="recordForm.recordType" placeholder="请选择类型" style="width: 100%">
                <el-option v-for="type in recordTypeOptions" :key="type.value" :label="type.label" :value="type.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 主诉输入 -->
        <el-form-item label="主诉" prop="chiefComplaint">
          <el-input
            v-model="recordForm.chiefComplaint"
            type="textarea"
            placeholder="请输入患者主诉"
            rows="3"
          />
        </el-form-item>
        
        <!-- 现病史输入 -->
        <el-form-item label="现病史" prop="presentIllness">
          <el-input
            v-model="recordForm.presentIllness"
            type="textarea"
            placeholder="请输入现病史"
            rows="3"
          />
        </el-form-item>
        
        <!-- 既往史输入 -->
        <el-form-item label="既往史">
          <el-input
            v-model="recordForm.pastHistory"
            type="textarea"
            placeholder="请输入既往史"
            rows="2"
          />
        </el-form-item>
        
        <!-- 检查结果输入 -->
        <el-form-item label="检查结果">
          <el-input
            v-model="recordForm.examination"
            type="textarea"
            placeholder="请输入检查结果"
            rows="3"
          />
        </el-form-item>
        
        <!-- 诊断结果输入 -->
        <el-form-item label="诊断结果" prop="diagnosis">
          <el-input
            v-model="recordForm.diagnosis"
            type="textarea"
            placeholder="请输入诊断结果"
            rows="3"
          />
        </el-form-item>
        
        <!-- 治疗方案输入 -->
        <el-form-item label="治疗方案" prop="treatment">
          <el-input
            v-model="recordForm.treatment"
            type="textarea"
            placeholder="请输入治疗方案"
            rows="3"
          />
        </el-form-item>
        
        <!-- 医嘱输入 -->
        <el-form-item label="医嘱">
          <el-input
            v-model="recordForm.advice"
            type="textarea"
            placeholder="请输入医嘱"
            rows="2"
          />
        </el-form-item>
        
        <!-- 随访计划输入 -->
        <el-form-item label="随访计划">
          <el-input
            v-model="recordForm.followUp"
            type="textarea"
            placeholder="请输入随访计划"
            rows="2"
          />
        </el-form-item>
      </el-form>
      
      <!-- 对话框底部按钮 -->
      <template #footer>
        <!-- 编辑模式按钮 -->
        <span class="dialog-footer" v-if="!viewMode">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitRecord">保 存</el-button>
        </span>
        <!-- 查看模式按钮 -->
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
/**
 * 医疗记录管理页面逻辑
 * 
 * 处理医疗记录的查询、新建、编辑、查看等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, reactive, onMounted } from 'vue'
// 导入路由相关函数
import { useRoute, useRouter } from 'vue-router'
// 导入 Element Plus 组件
import { ElMessage } from 'element-plus'
// 导入 HTTP 请求库
import axios from 'axios'

/**
 * 医疗记录管理组件导出
 * 
 * 定义组件的基本信息和逻辑
 */
export default {
  name: 'MedicalRecordsView',
  setup() {
    // 路由实例
    const route = useRoute()
    const router = useRouter()
    // 表单引用
    const recordFormRef = ref(null)
    
    // 加载状态
    const loading = ref(false)              // 列表加载状态
    const patientLoading = ref(false)       // 患者搜索加载状态
    
    /**
     * 分页参数
     * 
     * 控制病历列表的分页显示
     */
    const currentPage = ref(1)              // 当前页码
    const pageSize = ref(10)                // 每页显示数量
    const total = ref(0)                    // 总记录数
    
    /**
     * 搜索表单响应式对象
     * 
     * 存储搜索条件的表单数据
     */
    const searchForm = reactive({
      patientKeyword: '',                   // 患者关键词
      dateRange: [],                        // 日期范围
      recordType: ''                        // 病历类型
    })
    
    /**
     * 病历列表数据
     * 
     * 存储从服务器获取的病历记录列表
     */
    const recordsList = ref([])
    
    /**
     * 科室选项列表
     * 
     * 用于科室选择的选项数据
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
     * 病历类型选项列表
     * 
     * 用于病历类型选择的选项数据
     */
    const recordTypeOptions = [
      { value: 'firstVisit', label: '初诊' },
      { value: 'followUp', label: '复诊' },
      { value: 'emergency', label: '急诊' },
      { value: 'consultation', label: '会诊' }
    ]
    
    /**
     * 患者选项列表
     * 
     * 存储搜索到的患者信息
     */
    const patientOptions = ref([])
    
    /**
     * 对话框相关状态
     * 
     * 控制对话框的显示和模式
     */
    const dialogVisible = ref(false)        // 对话框是否可见
    const dialogTitle = ref('新建病历')     // 对话框标题
    const viewMode = ref(false)             // 是否为查看模式
    const editMode = ref(false)             // 是否为编辑模式
    
    /**
     * 当前病历记录
     * 
     * 存储当前操作的病历记录数据
     */
    const currentRecord = ref(null)
    
    /**
     * 病历表单响应式对象
     * 
     * 存储病历信息的表单数据
     */
    const recordForm = reactive({
      recordId: '',                         // 病历ID
      patientId: '',                        // 患者ID
      diagnoseDate: new Date(),             // 诊断日期
      department: '',                       // 科室
      recordType: 'firstVisit',             // 病历类型
      chiefComplaint: '',                   // 主诉
      presentIllness: '',                   // 现病史
      pastHistory: '',                      // 既往史
      examination: '',                      // 检查结果
      diagnosis: '',                        // 诊断结果
      treatment: '',                        // 治疗方案
      advice: '',                           // 医嘱
      followUp: ''                          // 随访计划
    })
    
    /**
     * 表单验证规则
     * 
     * 定义病历表单的验证规则
     */
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
    
    /**
     * 患者基本信息响应式对象
     * 
     * 存储当前选择患者的基本信息
     */
    const patientInfo = reactive({
      patientId: '',        // 患者ID
      name: '',             // 姓名
      gender: '',           // 性别
      genderLabel: '',      // 性别标签
      age: '',              // 年龄
      phone: '',            // 电话
      allergies: ''         // 过敏史
    })
    
    /**
     * 组件挂载时的初始化
     * 
     * 页面加载时自动获取病历列表和处理路由参数
     */
    onMounted(() => {
      fetchRecordsList()
      
      // 从路由参数中获取患者信息（从预约或患者管理页面跳转而来）
      if (route.query.patientId) {
        getPatientInfo(route.query.patientId)
      }
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
     * 获取病历类型名称
     * 
     * 根据病历类型值获取对应的显示名称
     * 
     * @param {string} value - 病历类型值
     * @returns {string} 病历类型名称
     */
    const getRecordTypeLabel = (value) => {
      const type = recordTypeOptions.find(t => t.value === value)
      return type ? type.label : value
    }
    
    /**
     * 获取病历列表
     * 
     * 从服务器获取病历记录列表，支持分页和搜索
     */
    const fetchRecordsList = async () => {
      loading.value = true
      try {
        const params = {
          pageNum: currentPage.value,  // 修正为文档中的pageNum
          pageSize: pageSize.value,    // 修正为文档中的pageSize
          patientKeyword: searchForm.patientKeyword,
          startDate: searchForm.dateRange ? searchForm.dateRange[0] : '',
          endDate: searchForm.dateRange ? searchForm.dateRange[1] : '',
          recordType: searchForm.recordType
        }
        // 修正接口URL，添加api前缀
        const { data: res } = await axios.get('/api/doctor/report/list', { params })
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
    
    /**
     * 模拟病历数据
     * 
     * 在网络请求失败时提供模拟数据
     */
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
    
    /**
     * 搜索患者
     * 
     * 根据关键词搜索患者信息
     * 
     * @param {string} query - 搜索关键词
     */
    const searchPatients = async (query) => {
      if (query.length < 2) return
      patientLoading.value = true
      try {
        // 修正接口URL，添加api前缀
        const { data: res } = await axios.get('/api/doctor/patients/search', { params: { query } })
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
    
    /**
     * 获取患者信息
     * 
     * 根据患者ID获取患者的详细信息
     * 
     * @param {string} patientId - 患者ID
     */
    const getPatientInfo = async (patientId) => {
      try {
        // 修正接口URL，添加api前缀
        const { data: res } = await axios.get(`/api/doctor/patients/info/${patientId}`)
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
    
    /**
     * 处理患者变更
     * 
     * 当选择的患者改变时，获取新患者的信息
     * 
     * @param {string} patientId - 患者ID
     */
    const handlePatientChange = (patientId) => {
      if (patientId) {
        getPatientInfo(patientId)
      }
    }
    
    /**
     * 搜索处理函数
     * 
     * 根据搜索条件重新获取病历列表
     */
    const handleSearch = () => {
      currentPage.value = 1
      fetchRecordsList()
    }
    
    /**
     * 重置搜索条件
     * 
     * 清空所有搜索条件并重新加载数据
     */
    const resetSearch = () => {
      searchForm.patientKeyword = ''
      searchForm.dateRange = []
      searchForm.recordType = ''
      currentPage.value = 1
      fetchRecordsList()
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
      fetchRecordsList()
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
      fetchRecordsList()
    }
    
    /**
     * 新建病历
     * 
     * 打开新建病历对话框并重置表单
     */
    const createRecord = () => {
      dialogTitle.value = '新建病历'
      viewMode.value = false
      editMode.value = false
      
      // 重置表单
      Object.keys(recordForm).forEach(key => {
        if (key === 'diagnoseDate') {
          recordForm.diagnoseDate = new Date().toISOString().split('T')[0] // 格式化为 'YYYY-MM-DD'
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
    
    /**
     * 查看病历
     * 
     * 打开病历查看对话框并获取详细信息
     * 
     * @param {Object} row - 病历记录对象
     */
    const viewRecord = (row) => {
      currentRecord.value = row
      dialogTitle.value = '查看病历'
      viewMode.value = true
      editMode.value = false
      
      // 获取病历详细信息
      getRecordDetail(row.recordId)
    }
    
    /**
     * 编辑病历
     * 
     * 打开病历编辑对话框并获取详细信息
     * 
     * @param {Object} row - 病历记录对象
     */
    const editRecord = (row) => {
      currentRecord.value = row
      dialogTitle.value = '编辑病历'
      viewMode.value = false
      editMode.value = true
      
      // 获取病历详细信息
      getRecordDetail(row.recordId)
    }
    
    /**
     * 从查看模式切换到编辑模式
     * 
     * 切换对话框状态为编辑模式
     */
    const switchToEdit = () => {
      viewMode.value = false
      editMode.value = true
      dialogTitle.value = '编辑病历'
    }
    
    /**
     * 获取病历详情
     * 
     * 根据病历ID获取病历的详细信息
     * 
     * @param {string} recordId - 病历ID
     */
    const getRecordDetail = async (recordId) => {
      try {
        // 修正接口URL，添加api前缀
        const { data: res } = await axios.get(`/api/doctor/report/detail/${recordId}`)
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
    
    /**
     * 提交病历表单
     * 
     * 验证表单数据并提交到服务器
     */
    const submitRecord = () => {
      if (!recordFormRef.value) return
      
      recordFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            let res
            if (editMode.value) {
              // 编辑病历：使用PUT方法，修正接口URL
              res = await axios.put(`/api/doctor/report/update`, recordForm)
            } else {
              // 新建病历：使用POST方法，修正接口URL
              res = await axios.post('/api/doctor/report/create', recordForm)
            }
            
            if (res.data.code === 200) {
              ElMessage.success(editMode.value ? '病历更新成功' : '病历创建成功')
              dialogVisible.value = false
              fetchRecordsList()
            } else {
              ElMessage.error(res.data.message || (editMode.value ? '更新失败' : '创建失败'))
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
    
    /**
     * 打印病历
     * 
     * 打印当前病历记录
     */
    const printRecord = () => {
      ElMessage.info('打印功能待实现')
      // TODO: 实现打印功能
    }
    
    /**
     * 开具处方
     * 
     * 跳转到处方页面为患者开具处方
     * 
     * @param {Object} row - 病历记录对象
     */
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
    
    /**
     * 返回模板需要的数据和方法
     * 
     * 将所有响应式数据和方法暴露给模板使用
     */
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

<!--
  医疗记录管理页面样式
  
  定义医疗记录管理页面的视觉设计和布局
  使用 scoped 限制样式作用域
-->
<style scoped>
/**
 * 按钮组右对齐样式
 * 
 * 设置操作按钮组的右对齐布局
 */
.btn-group-right {
  display: flex; 
  justify-content: flex-end; 
}

/**
 * 按钮间距样式
 * 
 * 设置按钮之间的左边距
 */
.el-button {
  margin-left: 8px; 
}

/**
 * 页面主容器样式
 * 
 * 设置页面的基础间距
 */
.medical-records-container {
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
 * 通用边距样式
 * 
 * 设置组件的底部边距
 */
.mb-20 {
  margin-bottom: 20px;
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