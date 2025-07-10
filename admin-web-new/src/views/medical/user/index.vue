<template>
  <div class="container">
    <el-card shadow="never" class="card-box">
      <!-- 搜索表单 -->
      <el-form :model="searchForm" label-width="80px" class="search-form">
        <el-row :gutter="20">
          <el-col :sm="12" :md="8" :lg="6">
            <el-form-item label="用户名">
              <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
            </el-form-item>
          </el-col>
          <el-col :sm="12" :md="8" :lg="6">
            <el-form-item label="手机号">
              <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
            </el-form-item>
          </el-col>
          <el-col :sm="12" :md="8" :lg="6">
            <el-form-item label="状态">
              <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :sm="24" :md="8" :lg="6">
            <el-form-item>
              <el-button type="primary" @click="searchUsers">
                <el-icon><Search /></el-icon>查询
              </el-button>
              <el-button @click="resetSearch">
                <el-icon><Refresh /></el-icon>重置
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 操作按钮 -->
      <div class="table-btn-box">
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon>新增用户
        </el-button>
      </div>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
        row-key="id"
      >
        <el-table-column prop="id" label="用户ID" width="80" align="center" />
        <el-table-column prop="username" label="用户名" width="120" align="center" />
        <el-table-column prop="nickname" label="昵称" width="120" align="center" />
        <el-table-column prop="phone" label="手机号" width="120" align="center" />
        <el-table-column prop="userRoleName" label="角色" width="120" align="center" />
        <el-table-column label="性别" width="80" align="center">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : scope.row.gender === 0 ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status ? 'success' : 'danger'">
              {{ scope.row.status ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" align="center" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.registerTime) }}
          </template>
        </el-table-column>
        <el-table-column label="最后登录时间" align="center" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.lastLoginTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" align="center" width="220">
          <template #default="scope">
            <el-button link type="primary" @click="editUser(scope.row)">
              <el-icon><Edit /></el-icon>编辑
            </el-button>
            <el-button link type="primary" @click="resetPassword(scope.row)">
              <el-icon><Key /></el-icon>重置密码
            </el-button>
            <el-button 
              link 
              :type="scope.row.status ? 'danger' : 'success'" 
              @click="toggleStatus(scope.row)"
            >
              <el-icon><Setting /></el-icon>{{ scope.row.status ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="danger" @click="deleteUser(scope.row)">
              <el-icon><Delete /></el-icon>删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageInfo.current"
          v-model:page-size="pageInfo.size"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageInfo.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 用户表单弹窗 -->
      <el-dialog
        v-model="formVisible"
        :title="formType === 'add' ? '新增用户' : '编辑用户'"
        width="600px"
        destroy-on-close
      >
        <el-form
          ref="userFormRef"
          :model="userForm"
          :rules="formRules"
          label-width="100px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="formType === 'edit'" />
          </el-form-item>
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="userForm.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="角色" prop="userRoleId">
            <el-select v-model="userForm.userRoleId" placeholder="请选择角色">
              <el-option v-for="role in roleOptions" :key="role.value" :label="role.label" :value="role.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="userForm.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-switch
              v-model="userForm.status"
              :active-value="true"
              :inactive-value="false"
              active-text="启用"
              inactive-text="禁用"
            />
          </el-form-item>
          <el-form-item v-if="formType === 'add'" label="密码" prop="password">
            <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="userForm.remark" type="textarea" placeholder="请输入备注信息" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="formVisible = false">取消</el-button>
            <el-button type="primary" @click="submitForm">确定</el-button>
          </div>
        </template>
      </el-dialog>

      <!-- 重置密码弹窗 -->
      <el-dialog
        v-model="passwordDialogVisible"
        title="重置密码"
        width="400px"
        destroy-on-close
      >
        <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          label-width="100px"
        >
          <el-form-item label="新密码" prop="password">
            <el-input v-model="passwordForm.password" type="password" placeholder="请输入新密码" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="passwordDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitPasswordReset">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Refresh, Plus, Edit, Delete, Key, Setting } from '@element-plus/icons-vue';
import { http } from '@/utils/http';
import { formatDateTime as formatDateTimeUtil } from '@/utils/date';

// 加载状态
const loading = ref(false);

// 表格数据
const tableData = ref([]);

// 分页信息
const pageInfo = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 搜索表单
const searchForm = reactive({
  username: '',
  phone: '',
  status: ''
});

// 表单相关
const formVisible = ref(false);
const formType = ref('add');
const userFormRef = ref();
const userForm = reactive({
  id: '',
  username: '',
  nickname: '',
  phone: '',
  userRoleId: 3, // 默认为普通用户角色
  gender: 1,
  status: true,
  password: '',
  remark: ''
});

// 角色选项
const roleOptions = ref([
  { value: 3, label: '普通用户' },
  { value: 2, label: '医生' }
]);

// 表单校验规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  userRoleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ]
};

// 重置密码相关
const passwordDialogVisible = ref(false);
const passwordFormRef = ref();
const passwordForm = reactive({
  userId: '',
  password: '',
  confirmPassword: ''
});

const passwordRules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== passwordForm.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      }, 
      trigger: 'blur' 
    }
  ]
};

// 获取用户列表
const getUserList = async () => {
  loading.value = true;
  try {
    const params = {
      ...searchForm,
      pageNum: pageInfo.current,
      pageSize: pageInfo.size
    };
    const res = await http.post('/admin/user/getUserPage', params);
    tableData.value = res.list || [];
    pageInfo.total = res.total || 0;
  } catch (error) {
    console.error('获取用户列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索用户
const searchUsers = () => {
  pageInfo.current = 1;
  getUserList();
};

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = '';
  });
  pageInfo.current = 1;
  getUserList();
};

// 分页事件
const handleSizeChange = (val: number) => {
  pageInfo.size = val;
  getUserList();
};

const handleCurrentChange = (val: number) => {
  pageInfo.current = val;
  getUserList();
};

// 打开新增用户弹窗
const openAddDialog = () => {
  formType.value = 'add';
  resetForm();
  formVisible.value = true;
};

// 打开编辑用户弹窗
const editUser = async (row: any) => {
  formType.value = 'edit';
  resetForm();
  try {
    const res = await http.post(`/admin/user/getUser/${row.id}`);
    if (res) {
      Object.keys(userForm).forEach(key => {
        if (key in res && key !== 'password') {
          userForm[key] = res[key];
        }
      });
    }
    formVisible.value = true;
  } catch (error) {
    console.error('获取用户详情失败:', error);
  }
};

// 重置表单
const resetForm = () => {
  Object.keys(userForm).forEach(key => {
    if (key === 'status') {
      userForm[key] = true;
    } else if (key === 'gender') {
      userForm[key] = 1;
    } else if (key === 'userRoleId') {
      userForm[key] = 3;
    } else {
      userForm[key] = '';
    }
  });
  if (userFormRef.value) {
    userFormRef.value.clearValidate();
  }
};

// 提交用户表单
const submitForm = async () => {
  if (!userFormRef.value) return;
  
  try {
    await userFormRef.value.validate();
    
    if (formType.value === 'add') {
      await http.post('/admin/user/addUser', userForm);
      ElMessage.success('添加用户成功');
    } else {
      await http.post('/admin/user/updateUser', userForm);
      ElMessage.success('编辑用户成功');
    }
    
    formVisible.value = false;
    getUserList();
  } catch (error) {
    console.error('提交表单失败:', error);
  }
};

// 切换用户状态
const toggleStatus = async (row: any) => {
  try {
    const statusText = row.status ? '禁用' : '启用';
    await ElMessageBox.confirm(
      `确定要${statusText}该用户吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    const params = {
      id: row.id,
      status: !row.status
    };
    
    await http.post('/admin/user/updateUser', params);
    ElMessage.success(`${statusText}用户成功`);
    getUserList();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新状态失败:', error);
    }
  }
};

// 删除用户
const deleteUser = (row: any) => {
  ElMessageBox.confirm(
    '确定要删除该用户吗？此操作不可恢复！',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await http.post(`/admin/user/deleteUser/${row.id}`);
      ElMessage.success('删除用户成功');
      getUserList();
    } catch (error) {
      console.error('删除用户失败:', error);
    }
  }).catch(() => {});
};

// 打开重置密码弹窗
const resetPassword = (row: any) => {
  passwordForm.userId = row.id;
  passwordForm.password = '';
  passwordForm.confirmPassword = '';
  passwordDialogVisible.value = true;
};

// 提交重置密码
const submitPasswordReset = async () => {
  if (!passwordFormRef.value) return;
  
  try {
    await passwordFormRef.value.validate();
    
    const params = {
      id: passwordForm.userId,
      password: passwordForm.password
    };
    
    await http.post('/admin/user/updateUser', params);
    ElMessage.success('重置密码成功');
    passwordDialogVisible.value = false;
  } catch (error) {
    console.error('重置密码失败:', error);
  }
};

// 格式化日期时间
const formatDateTime = (date: string | number | Date) => {
  if (!date) return '';
  return formatDateTimeUtil(date);
};

onMounted(() => {
  getUserList();
});
</script>

<style scoped>
.container {
  padding: 20px;
}
.card-box {
  width: 100%;
}
.search-form {
  margin-bottom: 20px;
}
.table-btn-box {
  margin-bottom: 20px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 