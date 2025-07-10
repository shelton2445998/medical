<template>
  <el-card shadow="never" class="card-box">
    <template #header>
      <div class="card-header">
        <span>体检套餐列表</span>
        <el-button type="primary" @click="openAddForm">新增套餐</el-button>
      </div>
    </template>

    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="套餐名称">
        <el-input v-model="searchForm.name" placeholder="请输入套餐名称" clearable />
      </el-form-item>
      <el-form-item label="套餐状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchPackages">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格区域 -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="套餐编码" width="120" />
      <el-table-column prop="name" label="套餐名称" min-width="120" />
      <el-table-column label="套餐类型" width="100">
        <template #default="scope">
          <span>{{ formatType(scope.row.type) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="套餐价格" width="100">
        <template #default="scope">
          <span>{{ formatPrice(scope.row.price) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="discountPrice" label="折扣价" width="100">
        <template #default="scope">
          <span>{{ formatPrice(scope.row.discountPrice) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="套餐描述" min-width="180" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status ? 'success' : 'danger'">
            {{ scope.row.status ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" link @click="editPackage(scope.row)">编辑</el-button>
          <el-button type="primary" link @click="viewItems(scope.row)">查看项目</el-button>
          <el-button
            type="primary"
            link
            @click="changeStatus(scope.row)"
          >{{ scope.row.status ? '禁用' : '启用' }}</el-button>
          <el-button type="danger" link @click="deletePackage(scope.row)">删除</el-button>
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
      :title="formType === 'add' ? '新增体检套餐' : '编辑体检套餐'"
      width="600px"
      append-to-body
    >
      <el-form ref="packageFormRef" :model="packageForm" :rules="formRules" label-width="100px">
        <el-form-item label="套餐名称" prop="name">
          <el-input v-model="packageForm.name" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-form-item label="套餐类型" prop="type">
          <el-select v-model="packageForm.type" placeholder="请选择套餐类型">
            <el-option :label="'基础'" :value="1" />
            <el-option :label="'高级'" :value="2" />
            <el-option :label="'专项'" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="套餐价格" prop="price">
          <el-input-number v-model="packageForm.price" :min="0" :precision="2" :step="10" />
        </el-form-item>
        <el-form-item label="折扣价" prop="discountPrice">
          <el-input-number v-model="packageForm.discountPrice" :min="0" :precision="2" :step="10" />
        </el-form-item>
        <el-form-item label="套餐描述" prop="description">
          <el-input v-model="packageForm.description" type="textarea" rows="3" placeholder="请输入套餐描述" />
        </el-form-item>
        <el-form-item label="套餐状态" prop="status">
          <el-radio-group v-model="packageForm.status">
            <el-radio :label="true">启用</el-radio>
            <el-radio :label="false">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="套餐项目" prop="checkItems">
          <el-transfer
            v-model="packageForm.checkItems"
            :titles="['可选项目', '已选项目']"
            :data="checkItemsList"
            :props="{ key: 'id', label: 'name' }"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="formVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看项目弹窗 -->
    <el-dialog v-model="itemsVisible" title="套餐包含项目" width="700px" append-to-body>
      <el-table :data="packageItems" border style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="code" label="项目编码" width="120" />
        <el-table-column prop="name" label="项目名称" min-width="150" />
        <el-table-column prop="price" label="项目价格" width="100">
          <template #default="scope">
            <span>{{ formatPrice(scope.row.price) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="所属科室" width="120" />
        <el-table-column prop="type" label="项目类型" width="100" />
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { http } from '@/utils/http';

// 搜索表单
const searchForm = reactive({
  name: '',
  status: ''
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

// 套餐表单
const packageForm = reactive({
  id: '',
  name: '',
  type: 1,
  price: 0,
  discountPrice: 0,
  description: '',
  status: true,
  checkItems: []
});

const formRules = {
  name: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择套餐类型', trigger: 'change' }],
  price: [{ required: true, message: '请输入套餐价格', trigger: 'change' }],
  description: [{ required: true, message: '请输入套餐描述', trigger: 'blur' }],
  checkItems: [{ type: 'array', required: true, message: '请至少选择一个体检项目', trigger: 'change' }]
};

// 弹窗控制
const formType = ref('add'); // add或edit
const formVisible = ref(false);
const itemsVisible = ref(false);
const packageFormRef = ref();
const packageItems = ref([]);
const checkItemsList = ref([]);

// 格式化价格
const formatPrice = (price: number) => {
  return `¥${price.toFixed(2)}`;
};

// 格式化类型
const formatType = (type: number) => {
  switch(type) {
    case 1: return '基础';
    case 2: return '高级';
    case 3: return '专项';
    default: return '未知';
  }
};

// 获取套餐列表
const getPackageList = async () => {
  loading.value = true;
  try {
    const params = {
      ...searchForm,
      pageNum: pageInfo.current,
      pageSize: pageInfo.size
    };
    const res: any = await http.get('/admin/package/list', { params });
    tableData.value = res.list || [];
    pageInfo.total = res.total || 0;
  } catch (error) {
    console.error('获取套餐列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 获取体检项目列表
const getCheckItemsList = async () => {
  try {
    const res: any = await http.get('/admin/check-item/list', { 
      params: { 
        pageSize: 1000  // 获取所有体检项目 
      } 
    });
    checkItemsList.value = res.list || [];
  } catch (error) {
    console.error('获取体检项目列表失败:', error);
  }
};

// 搜索套餐
const searchPackages = () => {
  pageInfo.current = 1;
  getPackageList();
};

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = '';
  });
  pageInfo.current = 1;
  getPackageList();
};

// 分页事件
const handleSizeChange = (val: number) => {
  pageInfo.size = val;
  getPackageList();
};

const handleCurrentChange = (val: number) => {
  pageInfo.current = val;
  getPackageList();
};

// 打开添加表单
const openAddForm = () => {
  formType.value = 'add';
  Object.keys(packageForm).forEach(key => {
    packageForm[key] = key === 'status' ? true : key === 'price' ? 0 : key === 'checkItems' ? [] : '';
  });
  formVisible.value = true;
  getCheckItemsList();
};

// 编辑套餐
const editPackage = async (row: any) => {
  formType.value = 'edit';
  try {
    const res: any = await http.get(`/admin/package/detail/${row.id}`);
    Object.keys(packageForm).forEach(key => {
      packageForm[key] = res[key] || packageForm[key];
    });
    formVisible.value = true;
    getCheckItemsList();
  } catch (error) {
    console.error('获取套餐详情失败:', error);
  }
};

// 查看套餐包含的项目
const viewItems = async (row: any) => {
  try {
    const res: any = await http.get(`/admin/package/detail/${row.id}`);
    packageItems.value = res.checkItems || [];
    itemsVisible.value = true;
  } catch (error) {
    console.error('获取套餐项目失败:', error);
  }
};

// 修改套餐状态
const changeStatus = async (row: any) => {
  try {
    // 使用PUT请求
    await http.put('/admin/package/status', { 
      id: row.id, 
      status: !row.status 
    });
    ElMessage.success(`${row.status ? '禁用' : '启用'}成功`);
    getPackageList();
  } catch (error) {
    console.error('修改套餐状态失败:', error);
  }
};

// 删除套餐
const deletePackage = (row: any) => {
  ElMessageBox.confirm('确定要删除该套餐吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.delete(`/admin/package/delete/${row.id}`);
      ElMessage.success('删除成功');
      getPackageList();
    } catch (error) {
      console.error('删除套餐失败:', error);
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  try {
    await packageFormRef.value.validate();
    const formData = { ...packageForm };
    
    // 确保type是数字类型
    if (formData.type) {
      formData.type = Number(formData.type);
    }
    
    if (formType.value === 'add') {
      // 添加使用POST
      await http.post('/admin/package/add', formData);
      ElMessage.success('添加成功');
    } else {
      // 更新使用PUT请求
      await http.put('/admin/package/update', formData);
      ElMessage.success('更新成功');
    }
    
    formVisible.value = false;
    getPackageList();
  } catch (error) {
    console.error(`${formType.value === 'add' ? '添加' : '更新'}套餐失败:`, error);
  }
};

onMounted(() => {
  getPackageList();
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