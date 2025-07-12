<template>
  <div class="report-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>体检报告管理</span>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="患者姓名" prop="patientName">
          <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="报告状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待完成" value="待完成" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="体检日期" prop="examDate">
          <el-date-picker
            v-model="queryParams.examDate"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="reportList" v-loading="loading" style="width: 100%">
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column label="报告编号" prop="reportNo" width="120" />
        <el-table-column label="患者姓名" prop="patientName" />
        <el-table-column label="体检日期" prop="examDate" />
        <el-table-column label="体检套餐" prop="packageName" :show-overflow-tooltip="true" />
        <el-table-column label="报告状态" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="异常项数" prop="abnormalCount">
          <template #default="scope">
            <el-tag type="danger" v-if="scope.row.abnormalCount > 0">{{ scope.row.abnormalCount }}</el-tag>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column label="生成时间" prop="createTime" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" link @click="handleViewDetail(scope.row)">查看</el-button>
            <el-button type="primary" link @click="handleEditReport(scope.row)">编辑</el-button>
            <el-button type="success" link @click="handleExportPdf(scope.row)">导出PDF</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

// 路由
const router = useRouter();

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  patientName: '',
  status: '',
  examDate: []
});

// 体检报告列表数据
const reportList = ref([]);
const total = ref(0);
const loading = ref(false);

// 初始化加载
onMounted(() => {
  getReportList();
});

// 获取体检报告列表
const getReportList = async () => {
  try {
    loading.value = true;
    
    // 模拟API调用
    setTimeout(() => {
      reportList.value = [
        {
          id: '1',
          reportNo: 'R20230615001',
          patientName: '张三',
          examDate: '2023-06-15',
          packageName: '全面体检套餐A',
          status: '已完成',
          abnormalCount: 2,
          createTime: '2023-06-15 14:30:00'
        },
        {
          id: '2',
          reportNo: 'R20230615002',
          patientName: '李四',
          examDate: '2023-06-15',
          packageName: '年度常规体检',
          status: '已完成',
          abnormalCount: 0,
          createTime: '2023-06-15 16:20:00'
        },
        {
          id: '3',
          reportNo: 'R20230616001',
          patientName: '王五',
          examDate: '2023-06-16',
          packageName: '入职体检套餐',
          status: '待完成',
          abnormalCount: 0,
          createTime: '2023-06-16 09:15:00'
        }
      ];
      
      total.value = reportList.value.length;
      loading.value = false;
    }, 500);
  } catch (error) {
    console.error('获取体检报告列表失败:', error);
    ElMessage.error('获取体检报告列表失败');
    loading.value = false;
  }
};

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1;
  getReportList();
};

// 重置操作
const resetQuery = () => {
  queryParams.patientName = '';
  queryParams.status = '';
  queryParams.examDate = [];
  handleQuery();
};

// 查看报告详情
const handleViewDetail = (row) => {
  router.push({
    path: `/doctor/report/detail/${row.id}`
  });
};

// 编辑体检报告
const handleEditReport = (row) => {
  router.push({
    path: `/doctor/report/edit/${row.id}`
  });
};

// 导出PDF
const handleExportPdf = (row) => {
  ElMessage.success(`正在导出报告 ${row.reportNo} 的PDF文件`);
  // 实际项目中，这里应该调用后端API进行PDF导出
};

// 分页相关方法
const handleSizeChange = (size) => {
  queryParams.pageSize = size;
  getReportList();
};

const handleCurrentChange = (current) => {
  queryParams.pageNum = current;
  getReportList();
};
</script>

<style scoped>
.report-container {
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