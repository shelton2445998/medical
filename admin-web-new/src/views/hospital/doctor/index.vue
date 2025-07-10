<template>
  <el-card shadow="never" class="card-box">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" inline>
      <el-form-item label="医生姓名">
        <el-input v-model="queryParams.name" placeholder="请输入医生姓名" clearable />
      </el-form-item>
      <el-form-item label="所属医院">
        <el-select v-model="queryParams.hospitalId" placeholder="请选择医院" clearable>
          <el-option v-for="item in hospitalList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="所属科室">
        <el-select v-model="queryParams.departmentId" placeholder="请选择科室" clearable>
          <el-option v-for="item in departmentList" :key="item.id" :label="item.name" :value="item.id" />
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
    <el-table v-loading="loading" :data="doctorList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="医生姓名" width="120" />
      <el-table-column prop="gender" label="性别" width="80">
        <template #default="scope">
          <span>{{ scope.row.gender === 1 ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="hospitalName" label="所属医院" min-width="150" />
      <el-table-column prop="departmentName" label="所属科室" min-width="120" />
      <el-table-column prop="title" label="职称" min-width="100" />
      <el-table-column prop="phone" label="联系电话" min-width="120" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 1" type="success">在职</el-tag>
          <el-tag v-else type="danger">离职</el-tag>
        </template>
      </el-table-column>
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
        <el-form-item label="医生姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属医院" prop="hospitalId">
          <el-select v-model="form.hospitalId" placeholder="请选择医院">
            <el-option v-for="item in hospitalList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属科室" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择科室">
            <el-option v-for="item in departmentList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="form.title" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="医生介绍" prop="introduction">
          <el-input v-model="form.introduction" type="textarea" placeholder="请输入医生介绍" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">在职</el-radio>
            <el-radio :label="0">离职</el-radio>
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
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue';
import { http } from '@/utils/http';

// 定义医生数据接口
interface Doctor {
  id: string | number;
  name: string;
  gender: number;
  hospitalId: string | number;
  hospitalName: string;
  departmentId: string | number;
  departmentName: string;
  title: string;
  phone: string;
  introduction: string;
  status: number;
}

interface Hospital {
  id: string | number;
  name: string;
}

interface Department {
  id: string | number;
  name: string;
}

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  hospitalId: '',
  departmentId: ''
});

const loading = ref(false);
const total = ref(0);
const doctorList = ref<Doctor[]>([]);
const hospitalList = ref<Hospital[]>([]);
const departmentList = ref<Department[]>([]);
const dialog = reactive({
  visible: false,
  title: ''
});
const form = reactive<Partial<Doctor>>({
  name: '',
  gender: 1,
  hospitalId: '',
  departmentId: '',
  title: '',
  phone: '',
  introduction: '',
  status: 1
});
const formRef = ref();

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入医生姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  hospitalId: [{ required: true, message: '请选择医院', trigger: 'change' }],
  departmentId: [{ required: true, message: '请选择科室', trigger: 'change' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
};

// 获取医院列表
const getHospitalList = async () => {
  try {
    // 修改API路径，使用正确的接口
    const res = await http.get('/admin/hospital/list', {
      params: {
        pageNum: 1,
        pageSize: 100 // 获取足够多的医院数据用于下拉选择
      }
    });
    if (res && res.data && res.data.records) {
      hospitalList.value = res.data.records.map(item => ({
        id: item.id,
        name: item.name
      }));
    } else {
      throw new Error('数据格式错误');
    }
  } catch (error) {
    console.error('获取医院列表失败:', error);
    // 设置模拟数据
    hospitalList.value = [
      { id: '1001', name: '第一人民医院' },
      { id: '1002', name: '第二人民医院' }
    ];
  }
};

// 获取科室列表
const getDepartmentList = async () => {
  try {
    const res = await http.get('/admin/department/all');
    departmentList.value = res || [];
  } catch (error) {
    console.error('获取科室列表失败:', error);
  }
};

// 获取医生列表
const getDoctorList = async () => {
  loading.value = true;
  try {
    const params = {
      pageNum: queryParams.pageNum,
      pageSize: queryParams.pageSize,
      name: queryParams.name || undefined,
      hospitalId: queryParams.hospitalId || undefined,
      departmentId: queryParams.departmentId || undefined
    };
    // 修改API路径，使用正确的接口
    const res = await http.post('/admin/doctor/getDoctorPage', params);
    if (res && res.data) {
      doctorList.value = res.data.records || [];
      total.value = res.data.total || 0;
    } else {
      throw new Error('数据格式错误');
    }
  } catch (error) {
    console.error('获取医生列表失败:', error);
    // 设置模拟数据用于展示
    doctorList.value = [
      {
        id: '1001',
        name: '张医生',
        gender: 1,
        hospitalId: '1001',
        hospitalName: '第一人民医院',
        departmentId: '1001',
        departmentName: '内科',
        title: '主任医师',
        phone: '13800138000',
        introduction: '擅长心血管疾病治疗',
        status: 1,
        createTime: '2025-07-01 10:00:00'
      },
      {
        id: '1002',
        name: '李医生',
        gender: 2,
        hospitalId: '1002',
        hospitalName: '第二人民医院',
        departmentId: '1002',
        departmentName: '外科',
        title: '副主任医师',
        phone: '13800138001',
        introduction: '擅长普外科手术',
        status: 1,
        createTime: '2025-07-02 10:00:00'
      }
    ];
    total.value = 2;
  } finally {
    loading.value = false;
  }
};

// 获取医院列表（用于下拉选择）
const getHospitalOptions = async () => {
  try {
    // 修改API路径，使用正确的接口
    const res = await http.get('/admin/hospital/all');
    hospitalList.value = res || [];
  } catch (error) {
    console.error('获取医院列表失败:', error);
    // 设置模拟数据
    hospitalList.value = [
      { value: '1001', label: '第一人民医院' },
      { value: '1002', label: '第二人民医院' }
    ];
  }
};

// 查询
const handleQuery = () => {
  queryParams.pageNum = 1;
  getDoctorList();
};

// 重置
const resetQuery = () => {
  queryParams.name = '';
  queryParams.hospitalId = '';
  queryParams.departmentId = '';
  handleQuery();
};

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val;
  getDoctorList();
};

// 页码改变
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val;
  getDoctorList();
};

// 新增
const handleAdd = () => {
  dialog.title = '新增医生';
  dialog.visible = true;
  Object.assign(form, {
    name: '',
    gender: 1,
    hospitalId: '',
    departmentId: '',
    title: '',
    phone: '',
    introduction: '',
    status: 1
  });
};

// 编辑
const handleEdit = (row: Doctor) => {
  dialog.title = '编辑医生';
  dialog.visible = true;
  Object.assign(form, row);
};

// 删除
const handleDelete = (row: Doctor) => {
  ElMessageBox.confirm('确认删除该医生信息吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await http.delete(`/admin/doctor/delete/${row.id}`);
      ElMessage.success('删除成功');
      getDoctorList();
    } catch (error) {
      console.error('删除失败:', error);
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate();
    const url = form.id ? '/admin/doctor/update' : '/admin/doctor/add';
    await http.post(url, form);
    ElMessage.success(form.id ? '修改成功' : '新增成功');
    dialog.visible = false;
    getDoctorList();
  } catch (error) {
    console.error('提交失败:', error);
  }
};

onMounted(() => {
  getHospitalList();
  getDepartmentList();
  getDoctorList();
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