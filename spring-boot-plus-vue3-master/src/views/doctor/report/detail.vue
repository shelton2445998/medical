<template>
  <div class="report-detail-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>体检报告详情</span>
          <div>
            <el-button type="primary" @click="handleEdit">编辑报告</el-button>
            <el-button type="success" @click="handleExportPdf">导出PDF</el-button>
            <el-button @click="handleReturn">返回</el-button>
          </div>
        </div>
      </template>

      <div v-loading="loading">
        <!-- 报告头部 -->
        <div class="report-header">
          <h1 class="report-title">体检报告</h1>
          <p class="report-no">报告编号：{{ reportDetail.reportNo }}</p>
        </div>

        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="姓名">{{ reportDetail.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ reportDetail.patientGender }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ reportDetail.patientAge }}</el-descriptions-item>
          <el-descriptions-item label="体检日期">{{ reportDetail.examDate }}</el-descriptions-item>
          <el-descriptions-item label="体检套餐">{{ reportDetail.packageName }}</el-descriptions-item>
          <el-descriptions-item label="体检医院">{{ reportDetail.hospitalName }}</el-descriptions-item>
        </el-descriptions>

        <!-- 体检结果 -->
        <el-divider content-position="left">体检结果</el-divider>
        <div v-for="(category, categoryIndex) in reportDetail.examCategories" :key="categoryIndex" class="exam-category">
          <h3>{{ category.name }}</h3>
          
          <el-table :data="category.items" border>
            <el-table-column label="序号" type="index" width="50" />
            <el-table-column label="检查项目" prop="name" width="180" />
            <el-table-column label="检查结果" prop="value" width="100" />
            <el-table-column label="参考值" prop="normalRange" width="180" />
            <el-table-column label="结果" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.isAbnormal ? 'danger' : 'success'">
                  {{ scope.row.isAbnormal ? '异常' : '正常' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="备注" prop="remark" />
          </el-table>
        </div>

        <!-- 医生建议 -->
        <el-divider content-position="left">医生建议</el-divider>
        <el-card class="advice-card" shadow="never">
          <template #header>
            <div class="advice-header">体检总结</div>
          </template>
          <div class="advice-content">{{ reportDetail.doctorAdvice.summary || '无' }}</div>
        </el-card>
        
        <el-card class="advice-card" shadow="never">
          <template #header>
            <div class="advice-header">健康建议</div>
          </template>
          <div class="advice-content">{{ reportDetail.doctorAdvice.advice || '无' }}</div>
        </el-card>
        
        <el-card class="advice-card" shadow="never">
          <template #header>
            <div class="advice-header">注意事项</div>
          </template>
          <div class="advice-content">{{ reportDetail.doctorAdvice.notice || '无' }}</div>
        </el-card>

        <!-- 报告尾部 -->
        <div class="report-footer">
          <div class="report-doctor">
            <span>检查医生：{{ reportDetail.doctorName }}</span>
          </div>
          <div class="report-date">
            <span>报告日期：{{ reportDetail.createTime }}</span>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();
const reportId = ref('');
const loading = ref(false);

// 报告详情
const reportDetail = reactive({
  reportNo: '',
  patientName: '',
  patientGender: '',
  patientAge: '',
  examDate: '',
  packageName: '',
  hospitalName: '',
  doctorName: '',
  createTime: '',
  examCategories: [] as any[],
  doctorAdvice: {
    summary: '',
    advice: '',
    notice: ''
  }
});

// 初始化加载
onMounted(async () => {
  reportId.value = route.params.id as string;
  if (reportId.value) {
    await getReportDetail();
  } else {
    ElMessage.error('缺少报告ID参数');
    router.push('/doctor/report');
  }
});

// 获取报告详情
const getReportDetail = async () => {
  try {
    loading.value = true;
    
    // 实际项目中应调用API获取数据
    // 此处模拟数据
    setTimeout(() => {
      reportDetail.reportNo = 'R20230701001';
      reportDetail.patientName = '张三';
      reportDetail.patientGender = '男';
      reportDetail.patientAge = '35';
      reportDetail.examDate = '2023-07-01';
      reportDetail.packageName = '全面体检套餐A';
      reportDetail.hospitalName = '协和医院';
      reportDetail.doctorName = '李医生';
      reportDetail.createTime = '2023-07-01 16:30:00';
      
      reportDetail.examCategories = [
        {
          name: '一般检查',
          items: [
            { name: '身高', value: '175 cm', normalRange: '', isAbnormal: false, remark: '' },
            { name: '体重', value: '70 kg', normalRange: '', isAbnormal: false, remark: '' },
            { name: '体重指数(BMI)', value: '22.9', normalRange: '18.5-24', isAbnormal: false, remark: '' },
            { name: '血压(收缩压)', value: '135 mmHg', normalRange: '90-140mmHg', isAbnormal: false, remark: '' },
            { name: '血压(舒张压)', value: '85 mmHg', normalRange: '60-90mmHg', isAbnormal: false, remark: '' },
            { name: '脉搏', value: '75 次/分', normalRange: '60-100次/分', isAbnormal: false, remark: '' }
          ]
        },
        {
          name: '血常规',
          items: [
            { name: '白细胞计数(WBC)', value: '5.8×10^9/L', normalRange: '4-10×10^9/L', isAbnormal: false, remark: '' },
            { name: '红细胞计数(RBC)', value: '5.2×10^12/L', normalRange: '4-5.5×10^12/L', isAbnormal: false, remark: '' },
            { name: '血红蛋白(HGB)', value: '150g/L', normalRange: '120-160g/L', isAbnormal: false, remark: '' },
            { name: '血小板计数(PLT)', value: '280×10^9/L', normalRange: '100-300×10^9/L', isAbnormal: false, remark: '' }
          ]
        },
        {
          name: '肝功能',
          items: [
            { name: '总胆红素(TBIL)', value: '15μmol/L', normalRange: '5.1-19μmol/L', isAbnormal: false, remark: '' },
            { name: '丙氨酸氨基转移酶(ALT)', value: '45U/L', normalRange: '0-40U/L', isAbnormal: true, remark: '轻度升高，建议复查' },
            { name: '天门冬氨酸氨基转移酶(AST)', value: '38U/L', normalRange: '0-40U/L', isAbnormal: false, remark: '' },
            { name: '总蛋白(TP)', value: '72g/L', normalRange: '65-85g/L', isAbnormal: false, remark: '' }
          ]
        }
      ];
      
      reportDetail.doctorAdvice = {
        summary: '体检结果大部分正常，肝功能ALT轻度升高，建议关注并复查。',
        advice: '1. 保持健康的生活习惯，避免过度劳累。\n2. 注意饮食健康，减少高脂肪、高糖分食物摄入。\n3. 定期锻炼，保持适当体重。\n4. 建议3个月后复查肝功能。',
        notice: '如有不适症状，请及时就医。'
      };
      
      loading.value = false;
    }, 500);
  } catch (error) {
    console.error('获取报告详情失败:', error);
    ElMessage.error('获取报告详情失败');
    loading.value = false;
  }
};

// 编辑报告
const handleEdit = () => {
  router.push(`/doctor/report/edit/${reportId.value}`);
};

// 导出PDF
const handleExportPdf = () => {
  ElMessage.success(`正在导出报告 ${reportDetail.reportNo} 的PDF文件`);
  // 实际项目中，这里应该调用后端API进行PDF导出
};

// 返回列表
const handleReturn = () => {
  router.push('/doctor/report');
};
</script>

<style scoped>
.report-detail-container {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.report-header {
  text-align: center;
  margin-bottom: 20px;
}

.report-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.report-no {
  color: #666;
}

.exam-category {
  margin-bottom: 20px;
}

.exam-category h3 {
  margin: 10px 0;
  color: #409EFF;
  font-weight: bold;
}

.advice-card {
  margin-bottom: 15px;
}

.advice-header {
  font-weight: bold;
}

.advice-content {
  white-space: pre-line;
  padding: 10px 0;
}

.report-footer {
  margin-top: 40px;
  display: flex;
  justify-content: space-between;
  padding-top: 20px;
  border-top: 1px dashed #ddd;
}
</style> 