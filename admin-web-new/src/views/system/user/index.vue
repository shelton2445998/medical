<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <p class="page-description">管理系统用户信息、权限和状态</p>
    </div>

    <el-card shadow="hover" class="search-form-card">
      <!-- 搜索表单 -->
      <el-form :model="queryForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :sm="24" :md="12" :lg="8" :xl="6">
            <el-form-item label="角色">
              <el-select v-model="queryForm.roleId" clearable placeholder="请选择角色" class="w-full">
                <el-option v-for="item in roleList" :label="item.name" :value="item.id" :key="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :sm="24" :md="12" :lg="8" :xl="6">
            <el-form-item label="状态">
              <el-select v-model="queryForm.status" clearable placeholder="请选择状态" class="w-full">
                <el-option label="启用" :value="1"/>
                <el-option label="禁用" :value="0"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :sm="24" :md="12" :lg="8" :xl="6">
            <el-form-item label="创建时间">
              <custom-date-picker type="daterange" v-model:startDate="queryForm.startCreateTime"
                                  v-model:endDate="queryForm.endCreateTime" clearable
                                  start-placeholder="开始时间"
                                  end-placeholder="结束时间"/>
            </el-form-item>
          </el-col>
          <el-col :sm="24" :md="12" :lg="8" :xl="6">
            <el-form-item label="搜索">
              <el-input v-model="queryForm.keyword" clearable placeholder="请输入用户名/昵称/手机号码/邮箱"/>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="form-actions">
          <el-button type="primary" @click="onSearch">
            <el-icon>
              <ele-search/>
            </el-icon>
            <span class="ml-1">查询</span>
          </el-button>
          <el-button @click="onReset">
            <el-icon>
              <ele-refresh/>
            </el-icon>
            <span class="ml-1">重置</span>
          </el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 表格工具栏 -->
    <div class="table-toolbar">
      <div class="toolbar-left">
        <el-button v-auth="'sys:user:add'" type="primary" @click="openDialog">
          <el-icon class="mr5">
            <ele-circle-plus/>
          </el-icon>
          新增用户
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-tooltip content="刷新" placement="top">
          <el-button type="primary" circle @click="getTableList">
            <el-icon>
              <ele-refresh/>
            </el-icon>
          </el-button>
        </el-tooltip>
      </div>
    </div>

    <!-- 表格 -->
    <el-card shadow="hover" class="table-card">
      <el-table 
        v-loading="tableLoading.status" 
        :data="tableData.data" 
        border 
        style="width: 100%" 
        row-key="id" 
        @sort-change="sortChange"
        :header-cell-style="{backgroundColor: '#f5f7fa', color: '#303133', fontWeight: '600'}"
      >
        <el-table-column prop="_tableIndex" label="序号" align="center" width="70"/>
        <el-table-column prop="username" label="用户名" align="center" min-width="120"/>
        <el-table-column prop="nickname" label="昵称" align="center" min-width="120"/>
        <el-table-column prop="roleName" label="角色" align="center" min-width="120">
          <template #default="{row}">
            <el-tag v-if="row.roleIsSystem" effect="light">{{row.roleName}}</el-tag>
            <el-tag v-else type="success" effect="light">{{row.roleName}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号码" align="center" min-width="120"/>
        <el-table-column prop="email" label="邮箱" align="center" min-width="180"/>
        <el-table-column prop="status" label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status" type="success" effect="light">启用</el-tag>
            <el-tag v-else type="danger" effect="light">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" min-width="160" sortable="custom"/>
        <el-table-column label="操作" fixed="right" align="center" width="200">
          <template #default="{row}">
            <div class="table-actions">
              <template v-if="!row.isAdmin">
                <el-button v-auth="'sys:user:update'" type="primary" link @click="openDialog(row)">
                  <el-icon>
                    <ele-edit/>
                  </el-icon>
                  编辑
                </el-button>
                <el-dropdown>
                  <el-button type="primary" link>
                    <span>更多</span>
                    <el-icon class="el-icon--right">
                      <ele-arrow-down/>
                    </el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="openPswDialog(row)">
                        <el-icon>
                          <ele-key/>
                        </el-icon>
                        <span class="ml-1">重置密码</span>
                      </el-dropdown-item>
                      <el-dropdown-item @click="delTable(row)" divided>
                        <el-icon>
                          <ele-delete/>
                        </el-icon>
                        <span class="ml-1">删除</span>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
              <template v-else>
                <el-tag type="info" effect="plain">系统管理员</el-tag>
              </template>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <CustomPagination v-model:currentPage="pageData.pageIndex" v-model:pageSize="pageData.pageSize"
                        :total="pageData.total" @changePage="changePage"/>
      </div>
    </el-card>

    <!-- 添加，编辑弹框 -->
    <TableForm ref="tableDialogRef" @refresh="getTableList" :roleList="roleList"/>

    <!-- 重置密码 -->
    <ResetPassword ref="passwordDialogRef"/>
  </div>
</template>
<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { deleteSysUser, getSysUserList } from "@/api/user";
import { ElMessage, ElMessageBox } from 'element-plus';
import TableForm from './table-form.vue';
import ResetPassword from './reset-password.vue';
import { getSysRoleAllList } from "@/api/role";
import { calcTableIndex } from "@/utils/util";

/** 查询 */
const queryForm = ref({})
const roleList = ref([]);

const tableLoading = ref({
  status: false
})

// 获取角色列表
const getRoleList = () => {
  getSysRoleAllList().then(res => {
    roleList.value = res || [];
  })
}

// 查询
const onSearch = () => {
  pageData.pageIndex = 1;
  getTableList();
}
// 重置
const onReset = () => {
  queryForm.value = {}
  pageData.pageIndex = 1;
  getTableList();
}

/** 分页 */
// 分页数据
const pageData = reactive({
  pageIndex: 1,
  pageSize: 10,
  total: 0
})
// 翻页
const changePage = (page: number) => {
  pageData.pageIndex = page;
  getTableList();
}

/** 排序 */
const orderBy = ref({})

/** 表格 */
// 表格数据
const tableData = reactive({
  data: [],
})
// 获取表格列表
const getTableList = () => {
  tableLoading.value.status = true;
  getSysUserList({...pageData, ...queryForm.value, ...orderBy.value}).then(res => {
    tableData.data = calcTableIndex(res, pageData);
    pageData.total = res.total;
    tableLoading.value.status = false;
  }).catch(() => {
    tableLoading.value.status = false;
  })
}

// 打开对话框
const tableDialogRef = ref();
const openDialog = (row?: any) => {
  tableDialogRef.value.openDialog(row);
}

// 打开重置密码对话框
const passwordDialogRef = ref();
const openPswDialog = (row: any) => {
  passwordDialogRef.value.openPswDialog(row);
}

// 删除
const delTable = (row: any) => {
  ElMessageBox.confirm(
      '是否确认删除该用户？删除后不可恢复。',
      '删除确认',
      {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button--danger',
      })
      .then(() => {
        deleteSysUser(row.id).then(() => {
          ElMessage.success('删除成功');
          getTableList();
        })
      }).catch(() => {
        // 取消删除，不做任何处理
      })
}

// 排序
const sortChange = (params: any) => {
  const { prop, order } = params;
  
  if (order) {
    orderBy.value.orderByColumn = prop;
    orderBy.value.orderByAsc = order === "ascending";
  } else {
    orderBy.value = {}
  }

  pageData.pageIndex = 1;
  getTableList();
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}`;
};

// 页面加载时初始化数据
getRoleList();
getTableList();
</script>
<style lang="scss" scoped>
@import '@/style/variables.scss';

.page-container {
  padding: $spacing-base;
}

.page-header {
  margin-bottom: $spacing-large;
  
  .page-title {
    font-size: 22px;
    font-weight: 600;
    color: $text-primary;
    margin: 0 0 $spacing-mini;
  }
  
  .page-description {
    font-size: 14px;
    color: $text-secondary;
    margin: 0;
  }
}

.search-form-card {
  margin-bottom: $spacing-large;
  border-radius: 8px;
  
  .form-actions {
    display: flex;
    justify-content: flex-end;
    margin-top: $spacing-base;
    
    .el-button {
      margin-left: $spacing-small;
    }
  }
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-base;
  
  .toolbar-left {
    display: flex;
    align-items: center;
  }
  
  .toolbar-right {
    display: flex;
    align-items: center;
  }
}

.table-card {
  border-radius: 8px;
  
  .table-actions {
    display: flex;
    justify-content: center;
    align-items: center;
    
    .el-button {
      padding: 6px 8px;
    }
  }
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: $spacing-large;
}

.w-full {
  width: 100%;
}

.ml-1 {
  margin-left: 4px;
}

.mr5 {
  margin-right: 5px;
}
</style>
