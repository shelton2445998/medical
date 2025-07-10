<template>
  <el-card shadow="never" class="card-box">
    <template #header>
      <div class="card-header">
        <span>体检项目列表</span>
        <el-button type="primary" @click="openAddDialog">新增项目</el-button>
      </div>
    </template>

    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="项目名称">
        <el-input v-model="searchForm.name" placeholder="请输入项目名称" clearable />
      </el-form-item>
      <el-form-item label="所属科室">
        <el-select v-model="searchForm.departmentId" placeholder="请选择科室" clearable>
          <el-option 
            v-for="item in departmentOptions" 
            :key="item.value" 
            :label="item.label" 
            :value="item.value" 
          />
        </el-select>
      </el-form-item>
      <el-form-item label="项目类型">
        <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
          <el-option label="检验" value="检验" />
          <el-option label="检查" value="检查" />
          <el-option label="其他" value="其他" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchItems">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格区域 -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="项目编码" width="120" />
      <el-table-column prop="name" label="项目名称" min-width="120" />
      <el-table-column prop="price" label="项目价格" width="100">
        <template #default="scope">
          <span>{{ formatPrice(scope.row.price) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属科室" width="120">
        <template #default="scope">
          <span>{{ getDepartmentName(scope.row.departmentId) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status ? 'success' : 'danger'">
            {{ scope.row.status ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button type="primary" link @click="editItem(scope.row)">编辑</el-button>
          <el-button
            type="primary"
            link
            @click="changeStatus(scope.row)"
          >{{ scope.row.status ? '禁用' : '启用' }}</el-button>
          <el-button type="danger" link @click="deleteItem(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
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

    <!-- 表单弹窗 -->
    <el-dialog
      v-model="formVisible"
      :title="formType === 'add' ? '新增体检项目' : '编辑体检项目'"
      width="500px"
      append-to-body
    >
      <el-form ref="itemFormRef" :model="itemForm" :rules="formRules" label-width="100px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="itemForm.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目价格" prop="price">
          <el-input-number v-model="itemForm.price" :min="0" :precision="2" :step="10" />
        </el-form-item>
        <el-form-item label="所属科室" prop="departmentId">
          <el-select v-model="itemForm.departmentId" placeholder="请选择科室">
            <el-option 
              v-for="item in departmentOptions" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="itemForm.description" type="textarea" rows="3" placeholder="请输入项目描述" />
        </el-form-item>
        <el-form-item label="项目状态" prop="status">
          <el-radio-group v-model="itemForm.status">
            <el-radio :label="true">启用</el-radio>
            <el-radio :label="false">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="formVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { http } from '@/utils/http';

// 科室选项，改为从数据库中获取
const departmentOptions = reactive([
  { label: '检验科', value: 2007 },
  { label: '影像科', value: 2006 },
  { label: '心脑血管科', value: 2005 },
  { label: '内科', value: 2001 },
  { label: '外科', value: 2002 },
  { label: '妇科', value: 2003 },
  { label: '儿科', value: 2004 }
]);

// 搜索表单
const searchForm = reactive({
  name: '',
  departmentId: null,
  status: null
});

// 表格数据
const tableData = ref([]);
const loading = ref(false);

// 分页信息
const pageInfo = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 项目表单
const itemFormRef = ref();
const itemForm = reactive({
  id: '',
  name: '',
  departmentId: null, // 数据库中是Long类型
  price: 0,
  description: '',
  status: true
});

const formRules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请选择所属科室', trigger: 'change' }],
  price: [{ required: true, message: '请输入项目价格', trigger: 'change' }],
  description: [{ required: true, message: '请输入项目描述', trigger: 'blur' }]
};

// 弹窗控制
const formType = ref('add'); // add或edit
const formVisible = ref(false);

// 格式化价格
const formatPrice = (price: number) => {
  return `¥${price.toFixed(2)}`;
};

// 获取科室名称
const getDepartmentName = (departmentId: number) => {
  const dept = departmentOptions.find(item => item.value === departmentId);
  return dept ? dept.label : departmentId;
};

// 获取项目列表
const getItemsList = async () => {
  loading.value = true;
  try {
    const params = {
      ...searchForm,
      pageNum: pageInfo.current,
      pageSize: pageInfo.size
    };
    const res: any = await http.get('/admin/check-item/list', { params });
    tableData.value = res.list || [];
    pageInfo.total = res.total || 0;
  } catch (error) {
    console.error('获取项目列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索项目
const searchItems = () => {
  pageInfo.current = 1;
  getItemsList();
};

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = '';
  });
  pageInfo.current = 1;
  getItemsList();
};

// 分页事件
const handleSizeChange = (val: number) => {
  pageInfo.size = val;
  getItemsList();
};

const handleCurrentChange = (val: number) => {
  pageInfo.current = val;
  getItemsList();
};

// 打开添加弹窗
const openAddDialog = () => {
  formType.value = 'add';
  resetForm();
  formVisible.value = true;
};

// 编辑项目
const editItem = async (row: any) => {
  formType.value = 'edit';
  resetForm();
  try {
    const res: any = await http.get(`/admin/check-item/detail/${row.id}`);
    if (res) {
      Object.keys(itemForm).forEach(key => {
        itemForm[key] = res[key] !== undefined ? res[key] : itemForm[key];
      });
    }
    formVisible.value = true;
  } catch (error) {
    console.error('获取项目详情失败:', error);
  }
};

// 重置表单
const resetForm = () => {
  Object.keys(itemForm).forEach(key => {
    itemForm[key] = key === 'status' ? true : key === 'price' ? 0 : key === 'departmentId' ? null : '';
  });
  itemFormRef.value?.clearValidate();
};

// 修改项目状态
const changeStatus = async (row: any) => {
  try {
    // 使用POST请求
    await http.post('/admin/check-item/status', {
      id: row.id,
      status: !row.status
    });
    ElMessage.success(`${row.status ? '禁用' : '启用'}成功`);
    getItemsList();
  } catch (error) {
    console.error('修改状态失败:', error);
  }
};

// 删除项目
const deleteItem = (row: any) => {
  ElMessageBox.confirm('确定要删除该项目吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.post(`/admin/check-item/delete/${row.id}`);
      ElMessage.success('删除成功');
      getItemsList();
    } catch (error) {
      console.error('删除失败:', error);
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  if (!itemFormRef.value) return;
  
  try {
    await itemFormRef.value.validate();
    
    // 确保提交的数据符合后端要求
    const formData = { ...itemForm };
    // 确保departmentId是数字类型
    if (formData.departmentId) {
      formData.departmentId = Number(formData.departmentId);
    }
    
    if (formType.value === 'add') {
      // 添加使用POST
      await http.post('/admin/check-item/add', formData);
      ElMessage.success('添加成功');
    } else {
      // 更新使用POST请求
      await http.post('/admin/check-item/update', formData);
      ElMessage.success('编辑成功');
    }
    
    formVisible.value = false;
    getItemsList();
  } catch (error) {
    console.error('提交表单失败:', error);
  }
};

onMounted(() => {
  getItemsList();
});
</script>

<style scoped>
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