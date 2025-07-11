<template>
  <el-card shadow="never" class="card-box">
    <el-form :model="queryForm" label-width="90px">
      <el-row :gutter="20">
        <el-col :sm="24" :md="12" :lg="8" :xl="6">
          <el-form-item label="类型">
            <el-select v-model="queryForm.type" clearable placeholder="请选择菜单类型">
              <el-option label="目录" :value="1"/>
              <el-option label="菜单" :value="2"/>
              <el-option label="权限" :value="3"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="8" :xl="6">
          <el-form-item label="状态">
            <el-select v-model="queryForm.status" clearable placeholder="请选择状态">
              <el-option label="启用" :value="1"/>
              <el-option label="禁用" :value="0"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="8" :xl="6">
          <el-form-item label="是否显示">
            <el-select v-model="queryForm.isShow" clearable placeholder="请选择是否显示">
              <el-option label="是" :value="1"/>
              <el-option label="否" :value="0"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="8" :xl="6">
          <el-form-item label="搜索">
            <el-input v-model="queryForm.keyword" @keyup.enter="onSearch" clearable placeholder="请输入菜单名称/路由地址/权限编码等"/>
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
    <!--    新 增-->
    <div class="table-btn-box mb10">
      <el-button type="primary" @click="openDialog">
        <el-icon class="mr5">
          <ele-circle-plus/>
        </el-icon>
        新 增
      </el-button>
      <el-button @click="isExpandTable">
        <el-icon class="mr5">
          <ele-expand/>
        </el-icon>
        展开/折叠
      </el-button>
    </div>
    <!--    表格-->
    <el-table v-loading="tableLoading.status" :data="tableData.data" border style="width: 100%" row-key="id" :expand-row-keys="expandRowKeys.value">
      <el-table-column prop="name" label="菜单名称" min-width="220">
        <template #default="scope">
          <div class="flex-inline flex item-center">
            <template v-if="scope.row.icon?.indexOf('local')>=0">
              <SvgIcon :name="scope.row.icon"/>
            </template>
            <el-icon v-else>
              <component :is="scope.row.icon"></component>
            </el-icon>
            <span class="ml3">{{ scope.row.name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" align="center" min-width="66"/>
      <el-table-column prop="routeUrl" label="路由地址" align="left" min-width="190"/>
      <el-table-column prop="componentPath" label="组件路径" align="left" min-width="190"/>
      <el-table-column prop="code" label="权限标识" align="left" min-width="190"/>
      <el-table-column prop="type" label="类型" align="center" min-width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.type===1" disable-transitions>目录</el-tag>
          <el-tag v-else-if="scope.row.type===2" type="success" disable-transitions>菜单</el-tag>
          <el-tag v-else type="warning" disable-transitions>权限</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="status" label="状态" align="center" min-width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.status===true" type="success" disable-transitions>启用</el-tag>
          <el-tag v-else type="danger" disable-transitions>禁用</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" align="center" min-width="168">
        <template #default="{row}">
          <el-button link type="primary" @click="openAddDialog(row)">
            <el-icon>
              <ele-circle-plus/>
            </el-icon>
            新增
          </el-button>
          <el-button link type="primary" @click="openDialog(row)">
            <el-icon>
              <ele-edit/>
            </el-icon>
            修改
          </el-button>
          <el-button link type="primary" @click="delTable(row)">
            <el-icon>
              <ele-delete/>
            </el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--    添加，编辑弹框-->
    <TableForm ref="tableDialogRef" @refresh="getTableList"/>
  </el-card>
</template>
<script lang="ts" setup>
import {ElMessage, ElMessageBox} from "element-plus";
import {deleteSysMenu, getAllSysMenuTreeList} from "@/api/menu";
import SvgIcon from '@/components/SvgIcon/index.vue'
import TableForm from './table-form.vue'


/** 查询*/
let queryForm = ref({})

const tableLoading = ref({
  status: false
})

// 查询
const onSearch = () => {
  getTableList();
}
// 查询
const onReset = () => {
  queryForm.value = {}
  getTableList();
}

/** 表格*/
// 表格数据
const tableData = reactive({
  data: [],
})
let expandRowKeys = ref([])
const menuIds = ref([])
// 默认展开第一级
const expandRowKeyData = computed(() => {
  filterMenus(tableData.data, menuIds);
  return menuIds.value;
})

function filterMenus(menus: any[], menuIds: { value: any[]; }) {
  menus.map((p: any) => {
    menuIds.value.push(p.id)
    if (p.children?.length > 0) {
      filterMenus(p.children, menuIds);
    }
  })
}

// 是否展开折叠
let expandTable = false;
const isExpandTable = () => {
  expandTable = !expandTable;
  if (expandTable) {
    expandRowKeys.value = expandRowKeyData;
  } else {
    expandRowKeys.value = ref([]);
  }
}

isExpandTable();

// 获取表格列表
const getTableList = () => {
  tableLoading.value.status = true;
  getAllSysMenuTreeList({...queryForm.value}).then(res => {
    tableData.data = res || [];
    tableLoading.value.status = false;
  })
}
/** 删除*/
const delTable = (row: any) => {
  ElMessageBox.confirm(
      '是否确认删除本条数据？',
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      })
      .then(() => {
        deleteSysMenu(row.id).then(() => {
          ElMessage.success('删除成功');
          getTableList();
        })
      }).catch(() => {
  })
}


/** 添加，编辑*/
const tableDialogRef = ref()
// 打开弹框
const openDialog = async (row: any = {}) => {
  await tableDialogRef.value.openDialog(row);
}
const openAddDialog = async (row: any = {}) => {
  let nextType;
  let nextParentId;
  if (row.type === 1){
    nextType = 2;
    nextParentId = row.id;
  }else if (row.type === 2){
    nextType = 3;
    nextParentId = row.id;
  }else{
    nextType = 3;
    nextParentId = row.parentId;
  }
  let addRow = {
    type: nextType,
    parentId: nextParentId
  }
  await tableDialogRef.value.openDialog(addRow);
}

getTableList();
</script>
<style></style>
