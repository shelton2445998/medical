<template>
  <div class="doctor-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>医生管理</span>
          <el-button type="primary" @click="handleAddDoctor">新增医生</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="医生姓名">
          <el-input v-model="searchForm.doctorName" placeholder="请输入医生姓名" clearable />
        </el-form-item>
        <el-form-item label="所属医院">
          <el-select v-model="searchForm.hospitalId" placeholder="请选择医院" clearable>
            <el-option v-for="item in hospitalOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="科室">
          <el-select v-model="searchForm.departmentId" placeholder="请选择科室" clearable>
            <el-option v-for="item in departmentOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="在职" value="1" />
            <el-option label="离职" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="tableData" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="doctorName" label="医生姓名" />
        <el-table-column prop="gender" label="性别">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="hospitalName" label="所属医院" />
        <el-table-column prop="departmentName" label="所属科室" />
        <el-table-column prop="title" label="职称" />
        <el-table-column prop="mobile" label="联系电话" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleViewDetail(scope.row)">查看</el-button>
            <el-button type="success" size="small" @click="handleEditDoctor(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleResetPassword(scope.row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="searchForm.pageSize"
          :current-page="searchForm.pageNum"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 医生表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-form ref="doctorForm" :model="doctorForm" :rules="doctorRules" label-width="100px">
        <el-form-item label="医生姓名" prop="doctorName">
          <el-input v-model="doctorForm.doctorName" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="doctorForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属医院" prop="hospitalId">
          <el-select v-model="doctorForm.hospitalId" placeholder="请选择所属医院" style="width: 100%">
            <el-option v-for="item in hospitalOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属科室" prop="departmentId">
          <el-select v-model="doctorForm.departmentId" placeholder="请选择所属科室" style="width: 100%">
            <el-option v-for="item in departmentOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="doctorForm.title" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="mobile">
          <el-input v-model="doctorForm.mobile" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="doctorForm.email" placeholder="请输入电子邮箱" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="doctorForm.status">
            <el-radio :label="1">在职</el-radio>
            <el-radio :label="0">离职</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="简介" prop="introduction">
          <el-input
            v-model="doctorForm.introduction"
            type="textarea"
            :rows="3"
            placeholder="请输入医生简介"
          />
        </el-form-item>
        <el-form-item label="登录账号" prop="username" v-if="operationType === 'add'">
          <el-input v-model="doctorForm.username" placeholder="请设置登录账号" />
        </el-form-item>
        <el-form-item label="登录密码" prop="password" v-if="operationType === 'add'">
          <el-input v-model="doctorForm.password" type="password" placeholder="请设置登录密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="医生详情" v-model="detailDialogVisible" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="医生ID">{{ detailData.doctorId }}</el-descriptions-item>
        <el-descriptions-item label="医生姓名">{{ detailData.doctorName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailData.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="所属医院">{{ detailData.hospitalName }}</el-descriptions-item>
        <el-descriptions-item label="所属科室">{{ detailData.departmentName }}</el-descriptions-item>
        <el-descriptions-item label="职称">{{ detailData.title }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.mobile }}</el-descriptions-item>
        <el-descriptions-item label="电子邮箱">{{ detailData.email || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 1 ? 'success' : 'info'">
            {{ detailData.status === 1 ? '在职' : '离职' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="医生简介">
          {{ detailData.introduction || '暂无简介' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 重置密码对话框 -->
    <el-dialog title="重置密码" v-model="resetPasswordDialogVisible" width="400px">
      <el-form :model="resetPasswordForm" :rules="resetPasswordRules" ref="resetPasswordFormRef" label-width="100px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="resetPasswordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetPasswordForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resetPasswordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitResetPassword">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 加载状态
const loading = ref(false)
// 医生表单对话框显示状态
const dialogVisible = ref(false)
// 医生详情对话框显示状态
const detailDialogVisible = ref(false)
// 重置密码对话框显示状态
const resetPasswordDialogVisible = ref(false)
// 对话框标题
const dialogTitle = ref('新增医生')
// 操作类型：add-新增，edit-编辑
const operationType = ref('add')
// 分页总数
const total = ref(0)
// 当前操作的医生
const currentDoctor = ref({})

// 医院选项
const hospitalOptions = ref([
  { value: 1, label: '协和医院' },
  { value: 2, label: '同仁医院' },
  { value: 3, label: '人民医院' },
  { value: 4, label: '北医三院' },
  { value: 5, label: '海淀医院' }
])

// 科室选项
const departmentOptions = ref([
  { value: 1, label: '内科' },
  { value: 2, label: '外科' },
  { value: 3, label: '妇产科' },
  { value: 4, label: '儿科' },
  { value: 5, label: '眼科' },
  { value: 6, label: '耳鼻喉科' },
  { value: 7, label: '口腔科' },
  { value: 8, label: '皮肤科' },
  { value: 9, label: '神经科' },
  { value: 10, label: '放射科' },
  { value: 11, label: '检验科' }
])

// 搜索表单数据
const searchForm = reactive({
  doctorName: '',
  hospitalId: '',
  departmentId: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

// 表格数据
const tableData = ref([
  {
    doctorId: 1,
    doctorName: '张医生',
    gender: 1,
    hospitalId: 1,
    hospitalName: '协和医院',
    departmentId: 1,
    departmentName: '内科',
    title: '主任医师',
    mobile: '13900138001',
    email: 'zhangys@example.com',
    status: 1,
    introduction: '从事内科临床工作20年，擅长心血管疾病的诊断与治疗',
    createTime: '2023-01-15 09:30:00',
    updateTime: '2023-01-15 09:30:00'
  },
  {
    doctorId: 2,
    doctorName: '李医生',
    gender: 0,
    hospitalId: 1,
    hospitalName: '协和医院',
    departmentId: 3,
    departmentName: '妇产科',
    title: '副主任医师',
    mobile: '13900138002',
    email: 'liys@example.com',
    status: 1,
    introduction: '从事妇产科临床工作15年，擅长妇科肿瘤的诊断与治疗',
    createTime: '2023-01-18 10:15:00',
    updateTime: '2023-01-18 10:15:00'
  },
  {
    doctorId: 3,
    doctorName: '王医生',
    gender: 1,
    hospitalId: 2,
    hospitalName: '同仁医院',
    departmentId: 5,
    departmentName: '眼科',
    title: '主任医师',
    mobile: '13900138003',
    email: 'wangys@example.com',
    status: 1,
    introduction: '从事眼科临床工作25年，擅长白内障、青光眼等眼部疾病的治疗',
    createTime: '2023-01-20 11:20:00',
    updateTime: '2023-01-20 11:20:00'
  },
  {
    doctorId: 4,
    doctorName: '赵医生',
    gender: 1,
    hospitalId: 3,
    hospitalName: '人民医院',
    departmentId: 2,
    departmentName: '外科',
    title: '主治医师',
    mobile: '13900138004',
    email: 'zhaoyx@example.com',
    status: 0,
    introduction: '从事外科临床工作10年，擅长普外科手术',
    createTime: '2023-01-22 14:40:00',
    updateTime: '2023-02-15 16:30:00'
  },
  {
    doctorId: 5,
    doctorName: '陈医生',
    gender: 0,
    hospitalId: 4,
    hospitalName: '北医三院',
    departmentId: 4,
    departmentName: '儿科',
    title: '副主任医师',
    mobile: '13900138005',
    email: 'chenys@example.com',
    status: 1,
    introduction: '从事儿科临床工作18年，擅长儿童常见病、多发病的诊断与治疗',
    createTime: '2023-02-01 09:50:00',
    updateTime: '2023-02-01 09:50:00'
  }
])

// 医生表单数据
const doctorForm = reactive({
  doctorId: null,
  doctorName: '',
  gender: 1,
  hospitalId: '',
  departmentId: '',
  title: '',
  mobile: '',
  email: '',
  status: 1,
  introduction: '',
  username: '',
  password: ''
})

// 医生表单验证规则
const doctorRules = {
  doctorName: [{ required: true, message: '请输入医生姓名', trigger: 'blur' }],
  hospitalId: [{ required: true, message: '请选择所属医院', trigger: 'change' }],
  departmentId: [{ required: true, message: '请选择所属科室', trigger: 'change' }],
  title: [{ required: true, message: '请输入职称', trigger: 'blur' }],
  mobile: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请设置登录账号', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请设置登录密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 重置密码表单数据
const resetPasswordForm = reactive({
  doctorId: null,
  newPassword: '',
  confirmPassword: ''
})

// 重置密码表单验证规则
const resetPasswordRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== resetPasswordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 详情数据
const detailData = reactive({})

// 初始化数据
onMounted(() => {
  fetchData()
})

// 获取表格数据
const fetchData = () => {
  loading.value = true
  // 模拟请求
  setTimeout(() => {
    total.value = tableData.value.length
    loading.value = false
  }, 500)
}

// 处理查询
const handleSearch = () => {
  searchForm.pageNum = 1
  fetchData()
}

// 重置查询
const resetSearch = () => {
  searchForm.doctorName = ''
  searchForm.hospitalId = ''
  searchForm.departmentId = ''
  searchForm.status = ''
  searchForm.pageNum = 1
  fetchData()
}

// 处理分页大小变化
const handleSizeChange = (val) => {
  searchForm.pageSize = val
  fetchData()
}

// 处理页码变化
const handleCurrentChange = (val) => {
  searchForm.pageNum = val
  fetchData()
}

// 处理状态变更
const handleStatusChange = (row) => {
  const statusText = row.status === 1 ? '启用' : '禁用'
  ElMessage.success(`医生"${row.doctorName}"已${statusText}`)
}

// 新增医生
const handleAddDoctor = () => {
  dialogTitle.value = '新增医生'
  operationType.value = 'add'
  resetDoctorForm()
  dialogVisible.value = true
}

// 编辑医生
const handleEditDoctor = (row) => {
  dialogTitle.value = '编辑医生'
  operationType.value = 'edit'
  resetDoctorForm()
  
  // 填充表单数据
  Object.keys(doctorForm).forEach(key => {
    if (key in row) {
      doctorForm[key] = row[key]
    }
  })
  
  dialogVisible.value = true
}

// 查看医生详情
const handleViewDetail = (row) => {
  // 填充详情数据
  Object.assign(detailData, row)
  detailDialogVisible.value = true
}

// 重置医生密码
const handleResetPassword = (row) => {
  currentDoctor.value = row
  resetPasswordForm.doctorId = row.doctorId
  resetPasswordForm.newPassword = ''
  resetPasswordForm.confirmPassword = ''
  resetPasswordDialogVisible.value = true
}

// 重置表单
const resetDoctorForm = () => {
  doctorForm.doctorId = null
  doctorForm.doctorName = ''
  doctorForm.gender = 1
  doctorForm.hospitalId = ''
  doctorForm.departmentId = ''
  doctorForm.title = ''
  doctorForm.mobile = ''
  doctorForm.email = ''
  doctorForm.status = 1
  doctorForm.introduction = ''
  doctorForm.username = ''
  doctorForm.password = ''
}

// 提交表单
const submitForm = () => {
  // 这里应该调用后端API保存数据
  const successMsg = operationType.value === 'add' ? '新增成功' : '编辑成功'
  ElMessage.success(successMsg)
  dialogVisible.value = false
  fetchData()
}

// 提交重置密码
const submitResetPassword = () => {
  // 这里应该调用后端API重置密码
  ElMessage.success(`医生"${currentDoctor.value.doctorName}"密码重置成功`)
  resetPasswordDialogVisible.value = false
}
</script>

<style scoped>
.doctor-management-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 