<template>
  <div class="patient-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>患者管理</span>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="患者姓名" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="queryParams.idCard" placeholder="请输入身份证号" clearable />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="queryParams.phone" placeholder="请输入手机号码" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="success" @click="handleAdd">添加患者</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="patientList" v-loading="loading" style="width: 100%">
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column label="姓名" prop="name" />
        <el-table-column label="性别" prop="gender" />
        <el-table-column label="年龄" prop="age" />
        <el-table-column label="身份证号" prop="idCard" />
        <el-table-column label="手机号码" prop="phone" />
        <el-table-column label="既往病史" prop="medicalHistory" :show-overflow-tooltip="true" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" link @click="handleViewDetail(scope.row)">查看</el-button>
            <el-button type="primary" link @click="handleViewHistory(scope.row)">体检记录</el-button>
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 患者详情对话框 -->
    <el-dialog v-model="detailDialog" title="患者详情" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ patientDetail.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ patientDetail.gender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ patientDetail.age }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ patientDetail.idCard }}</el-descriptions-item>
        <el-descriptions-item label="手机号码">{{ patientDetail.phone }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ patientDetail.birthday }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ patientDetail.address }}</el-descriptions-item>
        <el-descriptions-item label="既往病史" :span="2">
          {{ patientDetail.medicalHistory }}
        </el-descriptions-item>
        <el-descriptions-item label="过敏史" :span="2">
          {{ patientDetail.allergyHistory }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 添加/编辑患者对话框 -->
    <el-dialog v-model="patientDialog" :title="patientDialogTitle" width="600px" append-to-body>
      <el-form :model="patientForm" :rules="patientRules" ref="patientFormRef" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="patientForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="patientForm.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="patientForm.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="patientForm.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker v-model="patientForm.birthday" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="patientForm.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="既往病史" prop="medicalHistory">
          <el-input v-model="patientForm.medicalHistory" type="textarea" placeholder="请输入既往病史" />
        </el-form-item>
        <el-form-item label="过敏史" prop="allergyHistory">
          <el-input v-model="patientForm.allergyHistory" type="textarea" placeholder="请输入过敏史" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="patientDialog = false">取消</el-button>
        <el-button type="primary" @click="submitPatientForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 患者历史体检记录对话框 -->
    <el-dialog v-model="historyDialog" title="历史体检记录" width="800px" append-to-body>
      <el-table :data="historyList" v-loading="historyLoading" style="width: 100%">
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column label="体检日期" prop="examDate" />
        <el-table-column label="体检套餐" prop="packageName" />
        <el-table-column label="体检医院" prop="hospitalName" />
        <el-table-column label="体检状态" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" link @click="handleViewReport(scope.row)">查看报告</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { getPatientListApi, getPatientApi, addPatientApi, updatePatientApi } from '@/api/doctor';
import { ElMessage } from 'element-plus';

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  idCard: '',
  phone: ''
});

// 患者列表数据
const patientList = ref([]);
const total = ref(0);
const loading = ref(false);

// 患者详情相关
const detailDialog = ref(false);
const patientDetail = ref({});

// 患者表单相关
const patientDialog = ref(false);
const patientDialogTitle = ref('');
const patientForm = ref({
  id: '',
  name: '',
  gender: '',
  idCard: '',
  phone: '',
  birthday: '',
  address: '',
  medicalHistory: '',
  allergyHistory: ''
});
const patientRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }]
};
const patientFormRef = ref(null);

// 历史体检记录相关
const historyDialog = ref(false);
const historyList = ref([]);
const historyLoading = ref(false);

// 初始化加载
onMounted(() => {
  getPatientList();
});

// 获取患者列表
const getPatientList = async () => {
  try {
    loading.value = true;
    const res = await getPatientListApi(queryParams);
    if (res && res.data) {
      patientList.value = res.data.list || [];
      total.value = res.data.total || 0;
    }
  } catch (error) {
    console.error('获取患者列表失败:', error);
    ElMessage.error('获取患者列表失败');
  } finally {
    loading.value = false;
  }
};

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1;
  getPatientList();
};

// 重置操作
const resetQuery = () => {
  queryParams.name = '';
  queryParams.idCard = '';
  queryParams.phone = '';
  handleQuery();
};

// 查看患者详情
const handleViewDetail = async (row) => {
  try {
    const res = await getPatientApi(row.id);
    if (res && res.data) {
      patientDetail.value = res.data;
      detailDialog.value = true;
    }
  } catch (error) {
    console.error('获取患者详情失败:', error);
    ElMessage.error('获取患者详情失败');
  }
};

// 添加患者
const handleAdd = () => {
  patientForm.value = {
    id: '',
    name: '',
    gender: '',
    idCard: '',
    phone: '',
    birthday: '',
    address: '',
    medicalHistory: '',
    allergyHistory: ''
  };
  patientDialogTitle.value = '添加患者';
  patientDialog.value = true;
};

// 编辑患者
const handleEdit = async (row) => {
  try {
    const res = await getPatientApi(row.id);
    if (res && res.data) {
      patientForm.value = res.data;
      patientDialogTitle.value = '编辑患者';
      patientDialog.value = true;
    }
  } catch (error) {
    console.error('获取患者详情失败:', error);
    ElMessage.error('获取患者详情失败');
  }
};

// 提交患者表单
const submitPatientForm = async () => {
  if (!patientFormRef.value) return;
  
  await patientFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (patientForm.value.id) {
          // 更新患者
          await updatePatientApi(patientForm.value);
          ElMessage.success('更新患者信息成功');
        } else {
          // 添加患者
          await addPatientApi(patientForm.value);
          ElMessage.success('添加患者成功');
        }
        patientDialog.value = false;
        getPatientList();
      } catch (error) {
        console.error('提交患者信息失败:', error);
        ElMessage.error('提交患者信息失败');
      }
    }
  });
};

// 查看患者历史体检记录
const handleViewHistory = (row) => {
  historyLoading.value = true;
  historyList.value = []; // 先清空历史记录
  
  // 模拟获取历史体检记录数据
  setTimeout(() => {
    historyList.value = [
      {
        id: '1',
        examDate: '2023-06-15',
        packageName: '全面体检套餐A',
        hospitalName: '协和医院',
        status: '已完成'
      },
      {
        id: '2',
        examDate: '2022-06-20',
        packageName: '年度常规体检',
        hospitalName: '协和医院',
        status: '已完成'
      }
    ];
    historyLoading.value = false;
    historyDialog.value = true;
  }, 500);
};

// 查看体检报告
const handleViewReport = (row) => {
  // 跳转到体检报告详情页面
  window.open(`/#/doctor/report/detail/${row.id}`, '_blank');
};

// 分页相关方法
const handleSizeChange = (size) => {
  queryParams.pageSize = size;
  getPatientList();
};

const handleCurrentChange = (current) => {
  queryParams.pageNum = current;
  getPatientList();
};
</script>

<style scoped>
.patient-container {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 