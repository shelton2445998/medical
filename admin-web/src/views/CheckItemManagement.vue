<template>
  <div class="check-item-management-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <h2>体检项目管理</h2>
          <el-button type="primary" @click="handleAddItem">添加项目</el-button>
        </div>
      </template>
      
      <!-- 搜索过滤区域 -->
      <div class="filter-container">
        <el-form :inline="true" :model="filterForm" class="demo-form-inline">
          <el-form-item label="项目名称">
            <el-input v-model="filterForm.name" placeholder="请输入项目名称"></el-input>
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="filterForm.category" placeholder="请选择分类" clearable>
              <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="项目名称" width="150"></el-table-column>
        <el-table-column prop="category" label="分类" width="120">
          <template #default="scope">
            <el-tag :type="getCategoryType(scope.row.category)">{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="项目描述"></el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="检查时长" width="100">
          <template #default="scope">
            {{ scope.row.duration }}分钟
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </el-card>
    
    <!-- 添加/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="10" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="检查时长" prop="duration">
          <el-input-number v-model="form.duration" :min="1" :max="480" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入项目描述"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

// 表格数据
const tableData = ref([]);
const loading = ref(false);
const total = ref(0);
const pageSize = ref(10);
const currentPage = ref(1);

// 过滤表单
const filterForm = reactive({
  name: '',
  category: ''
});

// 分类选项
const categories = [
  { label: '常规检查', value: '常规检查' },
  { label: '影像学检查', value: '影像学检查' },
  { label: '实验室检查', value: '实验室检查' },
  { label: '功能检查', value: '功能检查' },
  { label: '专科检查', value: '专科检查' }
];

// 弹窗相关
const dialogVisible = ref(false);
const dialogTitle = ref('添加体检项目');
const formRef = ref(null);

// 表单数据
const form = reactive({
  id: null,
  name: '',
  category: '',
  price: 0,
  duration: 30,
  description: '',
  status: 1
});

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择项目分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ],
  duration: [
    { required: true, message: '请输入检查时长', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入项目描述', trigger: 'blur' }
  ]
};

// 获取数据
const fetchData = () => {
  loading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    const mockData = [];
    for (let i = 1; i <= 35; i++) {
      const categoryIndex = (i % 5);
      mockData.push({
        id: i,
        name: `体检项目${i}`,
        category: categories[categoryIndex].value,
        description: `这是体检项目${i}的详细描述信息，包含了检查内容、适用人群等信息。`,
        price: Math.floor(Math.random() * 1000) + 50,
        duration: (Math.floor(Math.random() * 6) + 1) * 10,
        status: Math.random() > 0.2 ? 1 : 0
      });
    }
    
    // 过滤数据
    let filteredData = [...mockData];
    if (filterForm.name) {
      filteredData = filteredData.filter(item => item.name.includes(filterForm.name));
    }
    if (filterForm.category) {
      filteredData = filteredData.filter(item => item.category === filterForm.category);
    }
    
    // 分页
    total.value = filteredData.length;
    tableData.value = filteredData.slice(
      (currentPage.value - 1) * pageSize.value,
      currentPage.value * pageSize.value
    );
    
    loading.value = false;
  }, 500);
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchData();
};

// 重置表单
const resetForm = () => {
  filterForm.name = '';
  filterForm.category = '';
  handleSearch();
};

// 处理页面大小变化
const handleSizeChange = (size) => {
  pageSize.value = size;
  fetchData();
};

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page;
  fetchData();
};

// 获取分类标签类型
const getCategoryType = (category) => {
  const types = {
    '常规检查': '',
    '影像学检查': 'success',
    '实验室检查': 'warning',
    '功能检查': 'danger',
    '专科检查': 'info'
  };
  return types[category] || '';
};

// 处理状态变更
const handleStatusChange = (row) => {
  ElMessage.success(`${row.name} 状态已${row.status === 1 ? '启用' : '禁用'}`);
};

// 添加项目
const handleAddItem = () => {
  dialogTitle.value = '添加体检项目';
  // 重置表单
  Object.keys(form).forEach(key => {
    if (key === 'status') {
      form[key] = 1;
    } else if (key === 'price') {
      form[key] = 0;
    } else if (key === 'duration') {
      form[key] = 30;
    } else {
      form[key] = '';
    }
  });
  dialogVisible.value = true;
};

// 编辑项目
const handleEdit = (row) => {
  dialogTitle.value = '编辑体检项目';
  // 填充表单
  Object.keys(form).forEach(key => {
    form[key] = row[key];
  });
  dialogVisible.value = true;
};

// 删除项目
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除项目 "${row.name}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 模拟删除操作
    tableData.value = tableData.value.filter(item => item.id !== row.id);
    ElMessage.success('删除成功');
  }).catch(() => {
    // 取消删除
  });
};

// 提交表单
const submitForm = () => {
  if (!formRef.value) return;
  
  formRef.value.validate((valid) => {
    if (valid) {
      if (form.id) {
        // 更新现有项目
        const index = tableData.value.findIndex(item => item.id === form.id);
        if (index !== -1) {
          tableData.value[index] = { ...form };
          ElMessage.success('更新成功');
        }
      } else {
        // 添加新项目
        const newItem = {
          ...form,
          id: tableData.value.length > 0 ? Math.max(...tableData.value.map(item => item.id)) + 1 : 1
        };
        tableData.value.unshift(newItem);
        ElMessage.success('添加成功');
      }
      dialogVisible.value = false;
    } else {
      return false;
    }
  });
};

// 页面加载时获取数据
onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.check-item-management-container {
  padding: 20px;
}

.main-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-container {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 