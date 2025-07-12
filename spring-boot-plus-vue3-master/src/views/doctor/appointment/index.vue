<template>
  <div class="appointment-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>预约管理</span>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="预约日期" prop="appointmentDate">
          <el-date-picker
            v-model="queryParams.appointmentDate"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="科室" prop="departmentId">
          <el-select v-model="queryParams.departmentId" placeholder="请选择科室" clearable>
            <el-option
              v-for="item in departmentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="患者姓名" prop="patientName">
          <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="未开始" value="未开始" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="success" @click="handleSetAvailable">设置可预约时段</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="appointmentList" v-loading="loading" style="width: 100%">
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column label="预约编号" prop="appointmentNo" width="120" />
        <el-table-column label="患者姓名" prop="patientName" />
        <el-table-column label="预约日期" prop="appointmentDate" />
        <el-table-column label="预约时段" prop="appointmentTime" />
        <el-table-column label="科室" prop="departmentName" />
        <el-table-column label="体检套餐" prop="packageName" :show-overflow-tooltip="true" />
        <el-table-column label="状态" prop="status">
          <template #default="scope">
            <el-tag
              :type="
                scope.row.status === '已完成'
                  ? 'success'
                  : scope.row.status === '已取消'
                  ? 'danger'
                  : scope.row.status === '进行中'
                  ? 'warning'
                  : 'info'
              "
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" link @click="handleViewDetail(scope.row)">查看</el-button>
            <el-button type="primary" link @click="handleUpdateStatus(scope.row)" v-if="scope.row.status === '未开始'">
              开始体检
            </el-button>
            <el-button type="success" link @click="handleInputResult(scope.row)" v-if="scope.row.status === '进行中'">
              录入结果
            </el-button>
            <el-button
              type="warning"
              link
              @click="handleSpecialCase(scope.row)"
              v-if="scope.row.status !== '已完成' && scope.row.status !== '已取消'"
            >
              特殊处理
            </el-button>
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

    <!-- 预约详情对话框 -->
    <el-dialog v-model="detailDialog" title="预约详情" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="预约编号">{{ appointmentDetail.appointmentNo }}</el-descriptions-item>
        <el-descriptions-item label="预约状态">
          <el-tag
            :type="
              appointmentDetail.status === '已完成'
                ? 'success'
                : appointmentDetail.status === '已取消'
                ? 'danger'
                : appointmentDetail.status === '进行中'
                ? 'warning'
                : 'info'
            "
          >
            {{ appointmentDetail.status }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="患者姓名">{{ appointmentDetail.patientName }}</el-descriptions-item>
        <el-descriptions-item label="患者性别">{{ appointmentDetail.patientGender }}</el-descriptions-item>
        <el-descriptions-item label="患者年龄">{{ appointmentDetail.patientAge }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ appointmentDetail.patientPhone }}</el-descriptions-item>
        <el-descriptions-item label="预约日期">{{ appointmentDetail.appointmentDate }}</el-descriptions-item>
        <el-descriptions-item label="预约时段">{{ appointmentDetail.appointmentTime }}</el-descriptions-item>
        <el-descriptions-item label="科室">{{ appointmentDetail.departmentName }}</el-descriptions-item>
        <el-descriptions-item label="医院">{{ appointmentDetail.hospitalName }}</el-descriptions-item>
        <el-descriptions-item label="体检套餐" :span="2">{{ appointmentDetail.packageName }}</el-descriptions-item>
        <el-descriptions-item label="体检项目" :span="2">
          <el-tag v-for="(item, index) in appointmentDetail.examItems" :key="index" class="exam-item-tag">
            {{ item.name }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ appointmentDetail.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 特殊处理对话框 -->
    <el-dialog v-model="specialCaseDialog" title="特殊处理" width="500px" append-to-body>
      <el-form :model="specialCaseForm" ref="specialCaseFormRef" label-width="100px">
        <el-form-item label="处理类型" prop="type">
          <el-select v-model="specialCaseForm.type" placeholder="请选择处理类型" style="width: 100%">
            <el-option label="延期" value="delay" />
            <el-option label="取消" value="cancel" />
            <el-option label="更换科室" value="changeDepartment" />
          </el-select>
        </el-form-item>
        <el-form-item label="新预约日期" prop="newDate" v-if="specialCaseForm.type === 'delay'">
          <el-date-picker v-model="specialCaseForm.newDate" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="新预约时段" prop="newTime" v-if="specialCaseForm.type === 'delay'">
          <el-select v-model="specialCaseForm.newTime" placeholder="请选择时段" style="width: 100%">
            <el-option label="上午 8:00-10:00" value="上午 8:00-10:00" />
            <el-option label="上午 10:00-12:00" value="上午 10:00-12:00" />
            <el-option label="下午 14:00-16:00" value="下午 14:00-16:00" />
            <el-option label="下午 16:00-18:00" value="下午 16:00-18:00" />
          </el-select>
        </el-form-item>
        <el-form-item label="新科室" prop="newDepartmentId" v-if="specialCaseForm.type === 'changeDepartment'">
          <el-select v-model="specialCaseForm.newDepartmentId" placeholder="请选择科室" style="width: 100%">
            <el-option
              v-for="item in departmentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="原因说明" prop="reason">
          <el-input v-model="specialCaseForm.reason" type="textarea" placeholder="请输入原因说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="specialCaseDialog = false">取消</el-button>
        <el-button type="primary" @click="submitSpecialCase">确定</el-button>
      </template>
    </el-dialog>

    <!-- 设置可预约时段对话框 -->
    <el-dialog v-model="availableDialog" title="设置可预约时段" width="700px" append-to-body>
      <el-form :model="availableForm" ref="availableFormRef" label-width="100px">
        <el-form-item label="科室" prop="departmentId">
          <el-select v-model="availableForm.departmentId" placeholder="请选择科室" style="width: 100%">
            <el-option
              v-for="item in departmentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围" prop="dateRange">
          <el-date-picker
            v-model="availableForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="时段设置">
          <div class="time-slots-container">
            <div v-for="(slot, index) in availableTimeSlots" :key="index" class="time-slot-item">
              <span>{{ slot.label }}</span>
              <el-input-number v-model="slot.limit" :min="0" :max="20" />
            </div>
          </div>
        </el-form-item>
        <el-form-item label="不可用日期" prop="disabledDates">
          <el-select v-model="availableForm.disabledDates" multiple placeholder="选择不可用日期" style="width: 100%">
            <el-option
              v-for="date in getDisabledDateOptions()"
              :key="date"
              :label="date"
              :value="date"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="availableDialog = false">取消</el-button>
        <el-button type="primary" @click="submitAvailableSettings">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { getAppointmentListApi, getAppointmentApi, updateAppointmentApi } from '@/api/doctor';
import { getDepartmentListApi } from '@/api/doctor';
import { ElMessage } from 'element-plus';

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  appointmentDate: [],
  departmentId: '',
  patientName: '',
  status: ''
});

// 预约列表数据
const appointmentList = ref([]);
const total = ref(0);
const loading = ref(false);

// 科室选项
const departmentOptions = ref([]);

// 预约详情相关
const detailDialog = ref(false);
const appointmentDetail = ref({
  appointmentNo: '',
  patientName: '',
  patientGender: '',
  patientAge: '',
  patientPhone: '',
  appointmentDate: '',
  appointmentTime: '',
  departmentName: '',
  hospitalName: '',
  packageName: '',
  status: '',
  remark: '',
  examItems: []
});

// 特殊处理相关
const specialCaseDialog = ref(false);
const specialCaseForm = reactive({
  type: '',
  newDate: '',
  newTime: '',
  newDepartmentId: '',
  reason: '',
  appointmentId: ''
});
const specialCaseFormRef = ref(null);

// 可预约时段设置相关
const availableDialog = ref(false);
const availableForm = reactive({
  departmentId: '',
  dateRange: [],
  disabledDates: []
});
const availableTimeSlots = reactive([
  { label: '上午 8:00-10:00', key: 'morning1', limit: 10 },
  { label: '上午 10:00-12:00', key: 'morning2', limit: 10 },
  { label: '下午 14:00-16:00', key: 'afternoon1', limit: 10 },
  { label: '下午 16:00-18:00', key: 'afternoon2', limit: 10 }
]);
const availableFormRef = ref(null);

// 初始化加载
onMounted(() => {
  getAppointmentList();
  getDepartmentList();
});

// 获取预约列表
const getAppointmentList = async () => {
  try {
    loading.value = true;
    const params = { ...queryParams };
    if (params.appointmentDate && params.appointmentDate.length === 2) {
      params.startDate = params.appointmentDate[0];
      params.endDate = params.appointmentDate[1];
      delete params.appointmentDate;
    }
    
    const res = await getAppointmentListApi(params);
    if (res && res.data) {
      appointmentList.value = res.data.list || [];
      total.value = res.data.total || 0;
    }
  } catch (error) {
    console.error('获取预约列表失败:', error);
    ElMessage.error('获取预约列表失败');
  } finally {
    loading.value = false;
  }
};

// 获取科室列表
const getDepartmentList = async () => {
  try {
    const res = await getDepartmentListApi();
    if (res && res.data) {
      departmentOptions.value = res.data;
    }
  } catch (error) {
    console.error('获取科室列表失败:', error);
    ElMessage.error('获取科室列表失败');
  }
};

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1;
  getAppointmentList();
};

// 重置操作
const resetQuery = () => {
  queryParams.appointmentDate = [];
  queryParams.departmentId = '';
  queryParams.patientName = '';
  queryParams.status = '';
  handleQuery();
};

// 查看预约详情
const handleViewDetail = async (row) => {
  try {
    const res = await getAppointmentApi(row.id);
    if (res && res.data) {
      appointmentDetail.value = res.data;
      detailDialog.value = true;
    }
  } catch (error) {
    console.error('获取预约详情失败:', error);
    ElMessage.error('获取预约详情失败');
  }
};

// 更新预约状态
const handleUpdateStatus = async (row) => {
  try {
    const res = await updateAppointmentApi({
      id: row.id,
      status: '进行中'
    });
    if (res && res.success) {
      ElMessage.success('状态更新成功');
      getAppointmentList();
    }
  } catch (error) {
    console.error('更新预约状态失败:', error);
    ElMessage.error('更新预约状态失败');
  }
};

// 录入体检结果
const handleInputResult = (row) => {
  // 跳转到体检结果录入页面
  window.open(`/#/doctor/report/input/${row.id}`, '_blank');
};

// 特殊处理
const handleSpecialCase = (row) => {
  specialCaseForm.type = '';
  specialCaseForm.newDate = '';
  specialCaseForm.newTime = '';
  specialCaseForm.newDepartmentId = '';
  specialCaseForm.reason = '';
  specialCaseForm.appointmentId = row.id;
  specialCaseDialog.value = true;
};

// 提交特殊处理
const submitSpecialCase = async () => {
  if (!specialCaseFormRef.value) return;
  
  await specialCaseFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 根据处理类型组装请求参数
        const params: any = {
          id: specialCaseForm.appointmentId,
          reason: specialCaseForm.reason
        };
        
        if (specialCaseForm.type === 'cancel') {
          params.status = '已取消';
        } else if (specialCaseForm.type === 'delay') {
          params.appointmentDate = specialCaseForm.newDate;
          params.appointmentTime = specialCaseForm.newTime;
        } else if (specialCaseForm.type === 'changeDepartment') {
          params.departmentId = specialCaseForm.newDepartmentId;
        }
        
        const res = await updateAppointmentApi(params);
        if (res && res.success) {
          ElMessage.success('处理成功');
          specialCaseDialog.value = false;
          getAppointmentList();
        }
      } catch (error) {
        console.error('特殊处理失败:', error);
        ElMessage.error('特殊处理失败');
      }
    }
  });
};

// 设置可预约时段
const handleSetAvailable = () => {
  availableForm.departmentId = '';
  availableForm.dateRange = [];
  availableForm.disabledDates = [];
  availableTimeSlots.forEach(slot => {
    slot.limit = 10;
  });
  availableDialog.value = true;
};

// 获取不可用日期选项（假日等）
const getDisabledDateOptions = () => {
  // 这里可以返回一些假日或周末的日期
  return [
    '2023-07-01', // 例如：建党节
    '2023-10-01', // 例如：国庆节
    '2024-01-01', // 例如：元旦
  ];
};

// 提交可预约时段设置
const submitAvailableSettings = async () => {
  if (!availableFormRef.value) return;
  
  await availableFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 这里应该调用后端API保存设置，示例中模拟成功
        ElMessage.success('设置成功');
        availableDialog.value = false;
      } catch (error) {
        console.error('设置可预约时段失败:', error);
        ElMessage.error('设置可预约时段失败');
      }
    }
  });
};

// 分页相关方法
const handleSizeChange = (size) => {
  queryParams.pageSize = size;
  getAppointmentList();
};

const handleCurrentChange = (current) => {
  queryParams.pageNum = current;
  getAppointmentList();
};
</script>

<style scoped>
.appointment-container {
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

.exam-item-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}

.time-slots-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.time-slot-item {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 250px;
}
</style> 