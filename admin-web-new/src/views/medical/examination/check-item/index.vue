<template>
  <el-card shadow="never" class="card-box">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" inline>
      <el-form-item label="项目名称">
        <el-input v-model="queryParams.name" placeholder="请输入项目名称" clearable />
      </el-form-item>
      <el-form-item label="项目编码">
        <el-input v-model="queryParams.code" placeholder="请输入项目编码" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">
          <el-icon><Search /></el-icon>查询
        </el-button>
        <el-button @click="resetQuery">
          <el-icon><Refresh /></el-icon>重置
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <div class="table-btn-box mb10">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增
      </el-button>
    </div>

    <!-- 表格区域 -->
    <el-table v-loading="loading" :data="checkItemList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="项目名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="code" label="项目编码" width="120" />
      <el-table-column prop="price" label="项目价格" width="100">
        <template #default="scope">
          <span>¥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="项目类型" width="100">
        <template #default="scope">
          <span v-if="scope.row.type === '1'">检验</span>
          <span v-else-if="scope.row.type === '2'">检查</span>
          <span v-else>其他</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="handleEdit(scope.row)">
            <el-icon><Edit /></el-icon>编辑
          </el-button>
          <el-button type="primary" link @click="handleStatusChange(scope.row)">
            <el-icon v-if="scope.row.status === 1"><CloseBold /></el-icon>
            <el-icon v-else><CheckCircle /></el-icon>
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button type="primary" link @click="handleDelete(scope.row)">
            <el-icon><Delete /></el-icon>删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:currentPage="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入项目编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择项目类型" style="width: 100%">
                <el-option label="检验" value="1" />
                <el-option label="检查" value="2" />
                <el-option label="其他" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="项目说明" prop="description">
          <el-input v-model="form.description" type="textarea" rows="3" placeholder="请输入项目说明" />
        </el-form-item>
        <el-form-item label="注意事项" prop="notice">
          <el-input v-model="form.notice" type="textarea" rows="2" placeholder="请输入注意事项" />
        </el-form-item>
        <el-form-item label="参考值范围" prop="referenceValue">
          <el-input v-model="form.referenceValue" placeholder="请输入参考值范围" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialog.visible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Refresh, Plus, Edit, Delete, CloseBold, CheckCircle } from '@element-plus/icons-vue';
import { http } from '@/utils/http';

// 定义检查项目数据接口
interface CheckItem {
  id: string | number;
  name: string;
  code: string;
  price: number;
  type: string;
  description: string;
  notice: string;
  referenceValue: string;
  status: number;
}

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  code: '',
  status: ''
});

const loading = ref(false);
const total = ref(0);
const checkItemList = ref<CheckItem[]>([]);
const dialog = reactive({
  visible: false,
  title: ''
});
const form = reactive<Partial<CheckItem>>({
  name: '',
  code: '',
  price: 0,
  type: '',
  description: '',
  notice: '',
  referenceValue: '',
  status: 1
});
const formRef = ref();

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入项目编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择项目类型', trigger: 'change' }],
  price: [{ required: true, message: '请输入项目价格', trigger: 'blur' }]
};

// 查询检查项目列表
const getCheckItemList = async () => {
  loading.value = true;
  try {
    const res = await http.get('/admin/checkitem/list', queryParams);
    checkItemList.value = res.records || [];
    total.value = res.total || 0;
  } catch (error) {
    console.error('获取检查项目列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 查询
const handleQuery = () => {
  queryParams.pageNum = 1;
  getCheckItemList();
};

// 重置
const resetQuery = () => {
  queryParams.name = '';
  queryParams.code = '';
  queryParams.status = '';
  handleQuery();
};

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val;
  getCheckItemList();
};

// 页码改变
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val;
  getCheckItemList();
};

// 新增
const handleAdd = () => {
  dialog.title = '新增检查项目';
  dialog.visible = true;
  Object.assign(form, {
    name: '',
    code: '',
    price: 0,
    type: '',
    description: '',
    notice: '',
    referenceValue: '',
    status: 1
  });
};

// 编辑
const handleEdit = async (row: CheckItem) => {
  dialog.title = '编辑检查项目';
  dialog.visible = true;
  try {
    const res = await http.get(`/admin/checkitem/detail/${row.id}`);
    Object.assign(form, res);
  } catch (error) {
    console.error('获取检查项目详情失败:', error);
  }
};

// 状态变更
const handleStatusChange = (row: CheckItem) => {
  const status = row.status === 1 ? 0 : 1;
  const statusText = status === 1 ? '启用' : '禁用';
  
  ElMessageBox.confirm(`确认${statusText}该检查项目吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.post('/admin/checkitem/update-status', { id: row.id, status });
      ElMessage.success(`${statusText}成功`);
      getCheckItemList();
    } catch (error) {
      console.error(`${statusText}失败:`, error);
    }
  }).catch(() => {});
};

// 删除
const handleDelete = (row: CheckItem) => {
  ElMessageBox.confirm('确认删除该检查项目吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.delete(`/admin/checkitem/delete/${row.id}`);
      ElMessage.success('删除成功');
      getCheckItemList();
    } catch (error) {
      console.error('删除失败:', error);
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate();
    const url = form.id ? '/admin/checkitem/update' : '/admin/checkitem/add';
    await http.post(url, form);
    ElMessage.success(form.id ? '修改成功' : '新增成功');
    dialog.visible = false;
    getCheckItemList();
  } catch (error) {
    console.error('提交失败:', error);
  }
};

onMounted(() => {
  getCheckItemList();
});
</script>

<style scoped>
.card-box {
  margin: 20px;
}
.table-btn-box {
  margin-bottom: 15px;
}
.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style> 