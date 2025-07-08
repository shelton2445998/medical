<template>
  <div class="user-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAddUser">新增用户</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.userName" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.mobile" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="1" />
            <el-option label="禁用" value="0" />
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
        <el-table-column prop="userName" label="用户名" />
        <el-table-column prop="realName" label="真实姓名" />
        <el-table-column prop="gender" label="性别">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
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
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column fixed="right" label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleViewDetail(scope.row)">查看</el-button>
            <el-button type="success" size="small" @click="handleEditUser(scope.row)">编辑</el-button>
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

    <!-- 用户表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-form ref="userForm" :model="userForm" :rules="userRules" label-width="100px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userForm.userName" placeholder="请输入用户名" :disabled="operationType === 'edit'" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="userForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="userForm.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="userForm.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="userForm.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="operationType === 'add'">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
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
    <el-dialog title="用户详情" v-model="detailDialogVisible" width="500px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detailData.userName }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ detailData.realName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailData.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailData.mobile }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailData.email || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.idCard || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detailData.address || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 1 ? 'success' : 'danger'">
            {{ detailData.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
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
// 用户表单对话框显示状态
const dialogVisible = ref(false)
// 用户详情对话框显示状态
const detailDialogVisible = ref(false)
// 重置密码对话框显示状态
const resetPasswordDialogVisible = ref(false)
// 对话框标题
const dialogTitle = ref('新增用户')
// 操作类型：add-新增，edit-编辑
const operationType = ref('add')
// 分页总数
const total = ref(0)
// 当前操作的用户
const currentUser = ref({})

// 搜索表单数据
const searchForm = reactive({
  userName: '',
  mobile: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

// 表格数据
const tableData = ref([
  {
    userId: 1,
    userName: 'zhangsan',
    realName: '张三',
    gender: 1,
    idCard: '110101199001011234',
    mobile: '13800138001',
    email: 'zhangsan@example.com',
    address: '北京市朝阳区',
    status: 1,
    createTime: '2023-07-01 09:30:00',
    updateTime: '2023-07-05 15:20:00'
  },
  {
    userId: 2,
    userName: 'lisi',
    realName: '李四',
    gender: 1,
    idCard: '110101199002022345',
    mobile: '13800138002',
    email: 'lisi@example.com',
    address: '北京市海淀区',
    status: 1,
    createTime: '2023-07-02 10:15:00',
    updateTime: '2023-07-02 10:15:00'
  },
  {
    userId: 3,
    userName: 'wangwu',
    realName: '王五',
    gender: 1,
    idCard: '110101199003033456',
    mobile: '13800138003',
    email: 'wangwu@example.com',
    address: '北京市西城区',
    status: 0,
    createTime: '2023-07-03 14:20:00',
    updateTime: '2023-07-06 09:10:00'
  },
  {
    userId: 4,
    userName: 'zhaoliu',
    realName: '赵六',
    gender: 0,
    idCard: '110101199004044567',
    mobile: '13800138004',
    email: 'zhaoliu@example.com',
    address: '北京市东城区',
    status: 1,
    createTime: '2023-07-04 11:40:00',
    updateTime: '2023-07-04 11:40:00'
  },
  {
    userId: 5,
    userName: 'qianqi',
    realName: '钱七',
    gender: 0,
    idCard: '110101199005055678',
    mobile: '13800138005',
    email: 'qianqi@example.com',
    address: '北京市丰台区',
    status: 1,
    createTime: '2023-07-05 16:50:00',
    updateTime: '2023-07-05 16:50:00'
  }
])

// 用户表单数据
const userForm = reactive({
  userId: null,
  userName: '',
  realName: '',
  gender: 1,
  idCard: '',
  mobile: '',
  email: '',
  address: '',
  status: 1,
  password: ''
})

// 用户表单验证规则
const userRules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  idCard: [
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 重置密码表单数据
const resetPasswordForm = reactive({
  userId: null,
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
  searchForm.userName = ''
  searchForm.mobile = ''
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
  ElMessage.success(`用户"${row.userName}"已${statusText}`)
}

// 新增用户
const handleAddUser = () => {
  dialogTitle.value = '新增用户'
  operationType.value = 'add'
  resetUserForm()
  dialogVisible.value = true
}

// 编辑用户
const handleEditUser = (row) => {
  dialogTitle.value = '编辑用户'
  operationType.value = 'edit'
  resetUserForm()
  
  // 填充表单数据
  Object.keys(userForm).forEach(key => {
    if (key in row) {
      userForm[key] = row[key]
    }
  })
  
  dialogVisible.value = true
}

// 查看用户详情
const handleViewDetail = (row) => {
  // 填充详情数据
  Object.assign(detailData, row)
  detailDialogVisible.value = true
}

// 重置用户密码
const handleResetPassword = (row) => {
  currentUser.value = row
  resetPasswordForm.userId = row.userId
  resetPasswordForm.newPassword = ''
  resetPasswordForm.confirmPassword = ''
  resetPasswordDialogVisible.value = true
}

// 重置表单
const resetUserForm = () => {
  userForm.userId = null
  userForm.userName = ''
  userForm.realName = ''
  userForm.gender = 1
  userForm.idCard = ''
  userForm.mobile = ''
  userForm.email = ''
  userForm.address = ''
  userForm.status = 1
  userForm.password = ''
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
  ElMessage.success(`用户"${currentUser.value.userName}"密码重置成功`)
  resetPasswordDialogVisible.value = false
}
</script>

<style scoped>
.user-management-container {
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