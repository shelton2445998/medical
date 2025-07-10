<template>
  <el-card shadow="never" class="card-box">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" inline>
      <el-form-item label="套餐名称">
        <el-input v-model="queryParams.name" placeholder="请输入套餐名称" clearable />
      </el-form-item>
      <el-form-item label="套餐类型">
        <el-select v-model="queryParams.type" placeholder="请选择套餐类型" clearable>
          <el-option label="常规体检" value="1" />
          <el-option label="入职体检" value="2" />
          <el-option label="婚前体检" value="3" />
          <el-option label="孕前体检" value="4" />
        </el-select>
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
    <el-table v-loading="loading" :data="packageList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="套餐名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="code" label="套餐编码" width="120" />
      <el-table-column prop="type" label="套餐类型" width="100">
        <template #default="scope">
          <span v-if="scope.row.type === '1'">常规体检</span>
          <span v-else-if="scope.row.type === '2'">入职体检</span>
          <span v-else-if="scope.row.type === '3'">婚前体检</span>
          <span v-else-if="scope.row.type === '4'">孕前体检</span>
          <span v-else>其他</span>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="套餐价格" width="100">
        <template #default="scope">
          <span>¥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="checkItemCount" label="检查项目数" width="100" align="center" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="handleEdit(scope.row)">
            <el-icon><Edit /></el-icon>编辑
          </el-button>
          <el-button type="primary" link @click="handleViewDetail(scope.row)">
            <el-icon><View /></el-icon>查看详情
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
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="700px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="套餐名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入套餐名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="套餐编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入套餐编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="套餐类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择套餐类型" style="width: 100%">
                <el-option label="常规体检" value="1" />
                <el-option label="入职体检" value="2" />
                <el-option label="婚前体检" value="3" />
                <el-option label="孕前体检" value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="套餐价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="套餐说明" prop="description">
          <el-input v-model="form.description" type="textarea" rows="3" placeholder="请输入套餐说明" />
        </el-form-item>
        <el-form-item label="套餐图片" prop="image">
          <Upload v-model:value="form.image" type="image" />
        </el-form-item>
        <el-form-item label="适用人群" prop="targetUser">
          <el-input v-model="form.targetUser" type="textarea" rows="2" placeholder="请输入适用人群" />
        </el-form-item>
        <el-form-item label="注意事项" prop="notice">
          <el-input v-model="form.notice" type="textarea" rows="2" placeholder="请输入注意事项" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检查项目" prop="checkItems">
          <el-transfer
            v-model="form.checkItems"
            :data="checkItemsOptions"
            :props="{
              key: 'id',
              label: 'name'
            }"
            :titles="['可选项目', '已选项目']"
            filterable
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialog.visible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="套餐详情" v-model="detailDialog.visible" width="600px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="套餐名称">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="套餐编码">{{ detail.code }}</el-descriptions-item>
        <el-descriptions-item label="套餐类型">
          <span v-if="detail.type === '1'">常规体检</span>
          <span v-else-if="detail.type === '2'">入职体检</span>
          <span v-else-if="detail.type === '3'">婚前体检</span>
          <span v-else-if="detail.type === '4'">孕前体检</span>
          <span v-else>其他</span>
        </el-descriptions-item>
        <el-descriptions-item label="套餐价格">¥{{ detail.price }}</el-descriptions-item>
        <el-descriptions-item label="套餐说明">{{ detail.description }}</el-descriptions-item>
        <el-descriptions-item label="适用人群">{{ detail.targetUser }}</el-descriptions-item>
        <el-descriptions-item label="注意事项">{{ detail.notice }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detail.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <div class="mt20">
        <h3>包含检查项目</h3>
        <el-table :data="detail.checkItemList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="name" label="项目名称" min-width="120" show-overflow-tooltip />
          <el-table-column prop="code" label="项目编码" width="120" />
          <el-table-column prop="price" label="项目价格" width="100">
            <template #default="scope">
              <span>¥{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        </el-table>
      </div>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Refresh, Plus, Edit, Delete, View, CloseBold, CheckCircle } from '@element-plus/icons-vue';
import { http } from '@/utils/http';
import Upload from '@/components/Upload/index.vue';

// 定义套餐数据接口
interface Package {
  id: string | number;
  name: string;
  code: string;
  type: string;
  price: number;
  description: string;
  image: string;
  targetUser: string;
  notice: string;
  status: number;
  checkItemCount: number;
  checkItems?: (string | number)[];
  checkItemList?: CheckItem[];
}

interface CheckItem {
  id: string | number;
  name: string;
  code: string;
  price: number;
  remark: string;
}

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  type: '',
  status: ''
});

const loading = ref(false);
const total = ref(0);
const packageList = ref<Package[]>([]);
const checkItemsOptions = ref<CheckItem[]>([]);
const dialog = reactive({
  visible: false,
  title: ''
});
const detailDialog = reactive({
  visible: false
});
const detail = ref<Package>({} as Package);
const form = reactive<Partial<Package>>({
  name: '',
  code: '',
  type: '',
  price: 0,
  description: '',
  image: '',
  targetUser: '',
  notice: '',
  status: 1,
  checkItems: []
});
const formRef = ref();

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入套餐编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择套餐类型', trigger: 'change' }],
  price: [{ required: true, message: '请输入套餐价格', trigger: 'blur' }],
  checkItems: [{ required: true, message: '请选择检查项目', trigger: 'change' }]
};

// 获取检查项目列表
const getCheckItemList = async () => {
  try {
    const res = await http.get('/admin/checkitem/all');
    checkItemsOptions.value = res || [];
  } catch (error) {
    console.error('获取检查项目列表失败:', error);
  }
};

// 查询套餐列表
const getPackageList = async () => {
  loading.value = true;
  try {
    const res = await http.get('/admin/setmeal/list', queryParams);
    packageList.value = res.records || [];
    total.value = res.total || 0;
  } catch (error) {
    console.error('获取套餐列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 查询
const handleQuery = () => {
  queryParams.pageNum = 1;
  getPackageList();
};

// 重置
const resetQuery = () => {
  queryParams.name = '';
  queryParams.type = '';
  queryParams.status = '';
  handleQuery();
};

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val;
  getPackageList();
};

// 页码改变
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val;
  getPackageList();
};

// 新增
const handleAdd = () => {
  dialog.title = '新增套餐';
  dialog.visible = true;
  Object.assign(form, {
    name: '',
    code: '',
    type: '',
    price: 0,
    description: '',
    image: '',
    targetUser: '',
    notice: '',
    status: 1,
    checkItems: []
  });
};

// 编辑
const handleEdit = async (row: Package) => {
  dialog.title = '编辑套餐';
  dialog.visible = true;
  try {
    const res = await http.get(`/admin/setmeal/detail/${row.id}`);
    Object.assign(form, res);
    form.checkItems = res.checkItemList?.map((item: CheckItem) => item.id) || [];
  } catch (error) {
    console.error('获取套餐详情失败:', error);
  }
};

// 查看详情
const handleViewDetail = async (row: Package) => {
  try {
    const res = await http.get(`/admin/setmeal/detail/${row.id}`);
    detail.value = res;
    detailDialog.visible = true;
  } catch (error) {
    console.error('获取套餐详情失败:', error);
  }
};

// 状态变更
const handleStatusChange = (row: Package) => {
  const status = row.status === 1 ? 0 : 1;
  const statusText = status === 1 ? '启用' : '禁用';
  
  ElMessageBox.confirm(`确认${statusText}该套餐吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.post('/admin/setmeal/update-status', { id: row.id, status });
      ElMessage.success(`${statusText}成功`);
      getPackageList();
    } catch (error) {
      console.error(`${statusText}失败:`, error);
    }
  }).catch(() => {});
};

// 删除
const handleDelete = (row: Package) => {
  ElMessageBox.confirm('确认删除该套餐吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.delete(`/admin/setmeal/delete/${row.id}`);
      ElMessage.success('删除成功');
      getPackageList();
    } catch (error) {
      console.error('删除失败:', error);
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate();
    const url = form.id ? '/admin/setmeal/update' : '/admin/setmeal/add';
    await http.post(url, form);
    ElMessage.success(form.id ? '修改成功' : '新增成功');
    dialog.visible = false;
    getPackageList();
  } catch (error) {
    console.error('提交失败:', error);
  }
};

onMounted(() => {
  getCheckItemList();
  getPackageList();
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
.mt20 {
  margin-top: 20px;
}
:deep(.el-transfer) {
  display: flex;
  justify-content: space-between;
}
:deep(.el-transfer__buttons) {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style> 