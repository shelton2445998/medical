<template>
  <el-card shadow="never" class="card-box">
    <el-form :model="queryForm" label-width="80px">
      <el-row :gutter="20">
      </el-row>
      <el-row :gutter="20">
        <el-col :sm="24" :md="12" :lg="8" :xl="6">
          <el-form-item label="搜索">
            <el-input v-model="queryForm.keyword" @keyup.enter="onSearch" clearable placeholder="请输入名称"/>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="8" :xl="6">
          <el-form-item label-width="0">
            <el-button type="primary" @click="onSearch">
              <el-icon>
                <ele-search/>
              </el-icon>
              <span class="search-btn__left">查询</span>
            </el-button>
            <el-button @click="onReset">
              <el-icon>
                <ele-refresh/>
              </el-icon>
              <span class="search-btn__left">重置</span>
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-auth="'user:add'" class="table-btn-box mb10">
      <el-button type="primary" @click="openDialog()">
        <el-icon class="mr5">
          <ele-circle-plus/>
        </el-icon>
        新 增
      </el-button>
    </div>
    <el-table v-loading="tableLoading.status" :data="tableData.data" border row-key="id" @sort-change="sortChange">
      <el-table-column prop="_tableIndex" label="序号" align="center" width="50px"/>
      <el-table-column prop="username" label="账号" align="center" show-overflow-tooltip/>
      <el-table-column prop="nickname" label="昵称" align="center" show-overflow-tooltip/>
      <el-table-column prop="password" label="密码" align="center" show-overflow-tooltip/>
      <el-table-column prop="salt" label="盐值" align="center" show-overflow-tooltip/>
      <el-table-column prop="gender" label="性别" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.gender == false" type="danger" disable-transitions>女</el-tag>
          <el-tag v-if="scope.row.gender == true" type="success" disable-transitions>男</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="idCard" label="身份证号" align="center" show-overflow-tooltip/>
      <el-table-column prop="openid" label="微信openid" align="center" show-overflow-tooltip/>
      <el-table-column prop="phone" label="手机号码" align="center" show-overflow-tooltip/>
      <el-table-column prop="head" label="头像" align="center" show-overflow-tooltip/>
      <el-table-column prop="introduction" label="个人简介" align="center" show-overflow-tooltip/>
      <el-table-column prop="userRoleId" label="用户角色ID" align="center" show-overflow-tooltip/>
      <el-table-column prop="status" label="状态" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.status == true" type="success" disable-transitions>正常</el-tag>
          <el-tag v-if="scope.row.status == false" type="danger" disable-transitions>禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="registerIp" label="注册IP" align="center" show-overflow-tooltip/>
      <el-table-column prop="registerIpArea" label="注册IP区域" align="center" show-overflow-tooltip/>
      <el-table-column prop="lastLoginIp" label="最后登录IP" align="center" show-overflow-tooltip/>
      <el-table-column prop="lastLoginIpArea" label="最后一次登录IP区域" align="center" show-overflow-tooltip/>
      <el-table-column prop="remark" label="备注" align="center" show-overflow-tooltip/>
      <el-table-column prop="createTime" label="创建时间" align="center" min-width="180" sortable="custom"/>
      <el-table-column label="操作" fixed="right" align="center" min-width="116">
        <template #default="scope">
          <el-button v-auth="'user:update'" link type="primary" @click="openDialog(scope.row.id)">
            <el-icon>
              <ele-edit/>
            </el-icon>
            修改
          </el-button>
          <el-button v-auth="'user:delete'" link type="primary" @click="delTable(scope.row.id)">
            <el-icon>
              <ele-delete/>
            </el-icon>
            删除
          </el-button>

        </template>
      </el-table-column>
    </el-table>
    <CustomPagination v-model:currentPage="pagination.pageIndex" v-model:pageSize="pagination.pageSize"
                      :total="pagination.total" @changePage="changePage"/>

    <TableForm ref="tableDialogRef" @refresh="getTableList"/>

  </el-card>
</template>
<script lang="ts" setup>
import {deleteUser, getUserPage} from "@/api/medical/user";
import {ElMessage, ElMessageBox} from 'element-plus'
import TableForm from './form.vue'
import {calcTableIndex} from "@/utils/util";

/** 查询参数 **/
let queryForm: any = ref({
    keyword: null,
});

const tableLoading = ref({
    status: false
})

// 查询
const onSearch = () => {
    pagination.pageIndex = 1;
    getTableList();
}
// 重置
const onReset = () => {
    queryForm.value = {}
    pagination.pageIndex = 1;
    getTableList();
}

/** 分页*/
// 分页数据
const pagination = reactive({
    pageIndex: 1,
    pageSize: 10,
    total: 0
})
// 翻页
const changePage = (page: number) => {
    pagination.pageIndex = page;
    getTableList();
}

/** 排序*/
const orderBy = ref({})
// 排序
const sortChange = ({column, prop, order}) => {
    if (order) {
        orderBy.value.orderByColumn = prop;
        orderBy.value.orderByAsc = order === "ascending";
    } else {
        orderBy.value = {}
    }
    pagination.pageIndex = 1;
    getTableList();
}

/** 表格*/
// 表格数据
const tableData = reactive({
    data: [],
})
// 获取表格列表
const getTableList = () => {
    tableLoading.value.status = true;
    getUserPage({...pagination, ...queryForm.value, ...orderBy.value}).then(res => {
        tableData.data = calcTableIndex(res, pagination);;
        pagination.total = res.total;
        tableLoading.value.status = false;
    })
}
// 删除
const delTable = (id: string) => {
    ElMessageBox.confirm(
            '是否确认删除本条数据？',
            '提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            })
            .then(() => {
                deleteUser(id).then(() => {
                    ElMessage.success('删除成功');
                    getTableList();
                })
            }).catch(() => {
    })
}

/** 添加，编辑*/
const tableDialogRef = ref()
// 打开弹框
const openDialog = async (id: string) => {
    await tableDialogRef.value.openDialog(id);
}

getTableList();
</script>
<style scoped>

</style>
