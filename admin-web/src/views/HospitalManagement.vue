<template>
  <div class="hospital-management-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <h2>医院管理</h2>
          <el-button type="primary" @click="handleAddHospital">添加医院</el-button>
        </div>
      </template>
      
      <!-- 搜索过滤区域 -->
      <div class="filter-container">
        <el-form :inline="true" :model="filterForm" class="demo-form-inline">
          <el-form-item label="医院名称">
            <el-input v-model="filterForm.name" placeholder="请输入医院名称"></el-input>
          </el-form-item>
          <el-form-item label="所在城市">
            <el-select v-model="filterForm.city" placeholder="请选择城市" clearable>
              <el-option v-for="item in cities" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="医院等级">
            <el-select v-model="filterForm.level" placeholder="请选择等级" clearable>
              <el-option label="三级甲等" value="三级甲等"></el-option>
              <el-option label="三级乙等" value="三级乙等"></el-option>
              <el-option label="三级丙等" value="三级丙等"></el-option>
              <el-option label="二级甲等" value="二级甲等"></el-option>
              <el-option label="二级乙等" value="二级乙等"></el-option>
              <el-option label="一级医院" value="一级医院"></el-option>
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
        <el-table-column prop="name" label="医院名称" width="200"></el-table-column>
        <el-table-column prop="level" label="等级" width="120">
          <template #default="scope">
            <el-tag :type="getLevelType(scope.row.level)">{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="city" label="城市" width="120"></el-table-column>
        <el-table-column prop="address" label="详细地址"></el-table-column>
        <el-table-column prop="contactPhone" label="联系电话" width="140"></el-table-column>
        <el-table-column prop="appointmentCount" label="预约量" width="100" sortable>
          <template #default="scope">
            {{ scope.row.appointmentCount }}
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
            <el-button size="small" type="primary" @click="handleViewSchedule(scope.row)">排班</el-button>
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
      width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="医院名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入医院名称"></el-input>
        </el-form-item>
        <el-form-item label="医院等级" prop="level">
          <el-select v-model="form.level" placeholder="请选择等级" style="width: 100%">
            <el-option label="三级甲等" value="三级甲等"></el-option>
            <el-option label="三级乙等" value="三级乙等"></el-option>
            <el-option label="三级丙等" value="三级丙等"></el-option>
            <el-option label="二级甲等" value="二级甲等"></el-option>
            <el-option label="二级乙等" value="二级乙等"></el-option>
            <el-option label="一级医院" value="一级医院"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所在城市" prop="city">
          <el-select v-model="form.city" placeholder="请选择城市" style="width: 100%">
            <el-option v-for="item in cities" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="医院简介" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入医院简介"></el-input>
        </el-form-item>
        <el-form-item label="营业时间" prop="businessHours">
          <el-input v-model="form.businessHours" placeholder="例如：周一至周日 08:00-17:00"></el-input>
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
    
    <!-- 排班对话框 -->
    <el-dialog
      title="医院排班管理"
      v-model="scheduleDialogVisible"
      width="800px">
      <div v-if="currentHospital" class="schedule-container">
        <h3>{{ currentHospital.name }} - 排班表</h3>
        
        <div class="schedule-filter">
          <el-form :inline="true">
            <el-form-item label="选择月份">
              <el-date-picker
                v-model="scheduleMonth"
                type="month"
                placeholder="选择月份"
                format="YYYY年MM月"
                value-format="YYYY-MM">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadScheduleData">查看排班</el-button>
            </el-form-item>
          </el-form>
        </div>
        
        <div class="schedule-table">
          <el-table :data="scheduleData" border style="width: 100%" v-loading="scheduleLoading">
            <el-table-column prop="date" label="日期" width="120"></el-table-column>
            <el-table-column prop="dayOfWeek" label="星期" width="100"></el-table-column>
            <el-table-column prop="morningQuota" label="上午名额" width="100">
              <template #default="scope">
                <el-input-number v-model="scope.row.morningQuota" :min="0" :max="100" size="small"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column prop="afternoonQuota" label="下午名额" width="100">
              <template #default="scope">
                <el-input-number v-model="scope.row.afternoonQuota" :min="0" :max="100" size="small"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column prop="morningReserved" label="上午已约" width="100"></el-table-column>
            <el-table-column prop="afternoonReserved" label="下午已约" width="100"></el-table-column>
            <el-table-column prop="status" label="是否开放" width="100">
              <template #default="scope">
                <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0"></el-switch>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <div class="schedule-actions">
          <el-button type="primary" @click="saveSchedule">保存排班</el-button>
          <el-button @click="batchSetQuota">批量设置</el-button>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="scheduleDialogVisible = false">关闭</el-button>
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
  city: '',
  level: ''
});

// 城市列表
const cities = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '南京', '重庆', '西安'];

// 弹窗相关
const dialogVisible = ref(false);
const dialogTitle = ref('添加医院');
const formRef = ref(null);

// 表单数据
const form = reactive({
  id: null,
  name: '',
  level: '',
  city: '',
  address: '',
  contactPhone: '',
  description: '',
  businessHours: '',
  status: 1,
  appointmentCount: 0
});

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入医院名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  level: [
    { required: true, message: '请选择医院等级', trigger: 'change' }
  ],
  city: [
    { required: true, message: '请选择所在城市', trigger: 'change' }
  ],
  address: [
    { required: true, message: '请输入详细地址', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$|^0\d{2,3}-\d{7,8}$/, message: '请输入正确的电话格式', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入医院简介', trigger: 'blur' }
  ],
  businessHours: [
    { required: true, message: '请输入营业时间', trigger: 'blur' }
  ]
};

// 排班相关
const scheduleDialogVisible = ref(false);
const currentHospital = ref(null);
const scheduleMonth = ref('');
const scheduleData = ref([]);
const scheduleLoading = ref(false);

// 获取数据
const fetchData = () => {
  loading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    const mockData = [];
    for (let i = 1; i <= 35; i++) {
      const cityIndex = Math.floor(Math.random() * cities.length);
      const levelIndex = Math.floor(Math.random() * 6);
      const levels = ['三级甲等', '三级乙等', '三级丙等', '二级甲等', '二级乙等', '一级医院'];
      
      mockData.push({
        id: i,
        name: `${cities[cityIndex]}${['人民医院', '协和医院', '中医院', '第一医院', '第二医院', '妇幼保健院'][i % 6]}${i}`,
        level: levels[levelIndex],
        city: cities[cityIndex],
        address: `${cities[cityIndex]}市${['医院路', '健康街', '康复大道', '平安路'][i % 4]}${Math.floor(Math.random() * 100) + 1}号`,
        contactPhone: `0${Math.floor(Math.random() * 900) + 100}-${Math.floor(Math.random() * 90000000) + 10000000}`,
        description: `这是${cities[cityIndex]}的一家${levels[levelIndex]}医院，提供全面的医疗服务。`,
        businessHours: '周一至周日 08:00-17:00',
        status: Math.random() > 0.2 ? 1 : 0,
        appointmentCount: Math.floor(Math.random() * 1000)
      });
    }
    
    // 过滤数据
    let filteredData = [...mockData];
    if (filterForm.name) {
      filteredData = filteredData.filter(item => item.name.includes(filterForm.name));
    }
    if (filterForm.city) {
      filteredData = filteredData.filter(item => item.city === filterForm.city);
    }
    if (filterForm.level) {
      filteredData = filteredData.filter(item => item.level === filterForm.level);
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

// 加载排班数据
const loadScheduleData = () => {
  if (!scheduleMonth.value) {
    ElMessage.warning('请选择月份');
    return;
  }
  
  scheduleLoading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    const [year, month] = scheduleMonth.value.split('-');
    const daysInMonth = new Date(year, month, 0).getDate();
    const result = [];
    
    const dayNames = ['日', '一', '二', '三', '四', '五', '六'];
    
    for (let i = 1; i <= daysInMonth; i++) {
      const date = new Date(year, month - 1, i);
      const dayOfWeek = date.getDay();
      const isWeekend = dayOfWeek === 0 || dayOfWeek === 6;
      
      result.push({
        date: `${year}-${month}-${String(i).padStart(2, '0')}`,
        dayOfWeek: `周${dayNames[dayOfWeek]}`,
        morningQuota: isWeekend ? 50 : 30,
        afternoonQuota: isWeekend ? 50 : 30,
        morningReserved: Math.floor(Math.random() * (isWeekend ? 30 : 20)),
        afternoonReserved: Math.floor(Math.random() * (isWeekend ? 30 : 20)),
        status: 1
      });
    }
    
    scheduleData.value = result;
    scheduleLoading.value = false;
  }, 500);
};

// 保存排班
const saveSchedule = () => {
  ElMessage.success('排班保存成功');
};

// 批量设置
const batchSetQuota = () => {
  ElMessageBox.prompt('请输入每天的预约名额（上午/下午）', '批量设置', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d+\/\d+$/,
    inputErrorMessage: '格式不正确，请按照"上午名额/下午名额"的格式输入，如"30/30"'
  }).then(({ value }) => {
    const [morning, afternoon] = value.split('/').map(Number);
    scheduleData.value.forEach(day => {
      day.morningQuota = morning;
      day.afternoonQuota = afternoon;
    });
    ElMessage.success('批量设置成功');
  }).catch(() => {
    // 用户取消操作
  });
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchData();
};

// 重置表单
const resetForm = () => {
  filterForm.name = '';
  filterForm.city = '';
  filterForm.level = '';
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

// 获取医院等级标签类型
const getLevelType = (level) => {
  const types = {
    '三级甲等': 'success',
    '三级乙等': 'success',
    '三级丙等': 'warning',
    '二级甲等': 'warning',
    '二级乙等': 'info',
    '一级医院': 'info'
  };
  return types[level] || '';
};

// 处理状态变更
const handleStatusChange = (row) => {
  ElMessage.success(`${row.name} 状态已${row.status === 1 ? '启用' : '禁用'}`);
};

// 添加医院
const handleAddHospital = () => {
  dialogTitle.value = '添加医院';
  // 重置表单
  Object.keys(form).forEach(key => {
    if (key === 'status') {
      form[key] = 1;
    } else if (key === 'appointmentCount') {
      form[key] = 0;
    } else {
      form[key] = '';
    }
  });
  dialogVisible.value = true;
};

// 编辑医院
const handleEdit = (row) => {
  dialogTitle.value = '编辑医院';
  // 填充表单
  Object.keys(form).forEach(key => {
    form[key] = row[key];
  });
  dialogVisible.value = true;
};

// 查看排班
const handleViewSchedule = (row) => {
  currentHospital.value = row;
  scheduleMonth.value = new Date().toISOString().substring(0, 7); // 当前年月
  scheduleDialogVisible.value = true;
  loadScheduleData();
};

// 删除医院
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除医院 "${row.name}" 吗？`, '提示', {
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
        // 更新现有医院
        const index = tableData.value.findIndex(item => item.id === form.id);
        if (index !== -1) {
          tableData.value[index] = { ...form };
          ElMessage.success('更新成功');
        }
      } else {
        // 添加新医院
        const newHospital = {
          ...form,
          id: tableData.value.length > 0 ? Math.max(...tableData.value.map(item => item.id)) + 1 : 1,
          appointmentCount: 0
        };
        tableData.value.unshift(newHospital);
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
.hospital-management-container {
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

.schedule-container {
  padding: 10px;
}

.schedule-filter {
  margin-bottom: 20px;
}

.schedule-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 