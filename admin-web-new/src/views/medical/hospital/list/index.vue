<template>
  <el-card shadow="never" class="card-box">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" inline>
      <el-form-item label="医院名称">
        <el-input v-model="queryParams.name" placeholder="请输入医院名称" clearable />
      </el-form-item>
      <el-form-item label="医院类型">
        <el-select v-model="queryParams.type" placeholder="请选择医院类型" clearable>
          <el-option label="公立医院" value="1" />
          <el-option label="私立医院" value="2" />
          <el-option label="专科医院" value="3" />
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
    <el-table v-loading="loading" :data="hospitalList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="医院名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="type" label="医院类型" width="120">
        <template #default="scope">
          <el-tag v-if="scope.row.type === '1'">公立医院</el-tag>
          <el-tag v-else-if="scope.row.type === '2'" type="success">私立医院</el-tag>
          <el-tag v-else-if="scope.row.type === '3'" type="warning">专科医院</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="address" label="医院地址" min-width="180" show-overflow-tooltip />
      <el-table-column prop="phone" label="联系电话" width="120" />
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="handleEdit(scope.row)">
            <el-icon><Edit /></el-icon>编辑
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
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="医院名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入医院名称" />
        </el-form-item>
        <el-form-item label="医院类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择医院类型">
            <el-option label="公立医院" value="1" />
            <el-option label="私立医院" value="2" />
            <el-option label="专科医院" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="医院地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入医院地址" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入医院描述" />
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
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue';
import { http } from '@/utils/http';

// 定义医院数据接口
interface Hospital {
  id: string | number;
  name: string;
  type: string;
  address: string;
  phone: string;
  description: string;
}

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  type: ''
});

const loading = ref(false);
const total = ref(0);
const hospitalList = ref<Hospital[]>([]);
const dialog = reactive({
  visible: false,
  title: ''
});
const form = reactive<Partial<Hospital>>({
  name: '',
  type: '',
  address: '',
  phone: '',
  description: ''
});
const formRef = ref();

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入医院名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择医院类型', trigger: 'change' }],
  address: [{ required: true, message: '请输入医院地址', trigger: 'blur' }]
};

// 查询医院列表
const getHospitalList = async () => {
  loading.value = true;
  try {
    const res = await http.get('/admin/hospital/list', queryParams);
    hospitalList.value = res.records || [];
    total.value = res.total || 0;
  } catch (error) {
    console.error('获取医院列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 查询
const handleQuery = () => {
  queryParams.pageNum = 1;
  getHospitalList();
};

// 重置
const resetQuery = () => {
  queryParams.name = '';
  queryParams.type = '';
  handleQuery();
};

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val;
  getHospitalList();
};

// 页码改变
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val;
  getHospitalList();
};

// 新增
const handleAdd = () => {
  dialog.title = '新增医院';
  dialog.visible = true;
  Object.keys(form).forEach(key => {
    form[key as keyof typeof form] = '';
  });
};

// 编辑
const handleEdit = (row: Hospital) => {
  dialog.title = '编辑医院';
  dialog.visible = true;
  Object.assign(form, row);
};

// 删除
const handleDelete = (row: Hospital) => {
  ElMessageBox.confirm('确认删除该医院吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.delete(`/admin/hospital/delete/${row.id}`);
      ElMessage.success('删除成功');
      getHospitalList();
    } catch (error) {
      console.error('删除失败:', error);
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate();
    const url = form.id ? '/admin/hospital/update' : '/admin/hospital/add';
    await http.post(url, form);
    ElMessage.success(form.id ? '修改成功' : '新增成功');
    dialog.visible = false;
    getHospitalList();
  } catch (error) {
    console.error('提交失败:', error);
  }
};

onMounted(() => {
  getHospitalList();
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