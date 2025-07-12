<template>
  <div class="report-input-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>体检结果录入</span>
          <div>
            <el-button type="primary" @click="handleSave">保存</el-button>
            <el-button type="success" @click="handleGenerate">生成报告</el-button>
            <el-button @click="handleReturn">返回</el-button>
          </div>
        </div>
      </template>

      <!-- 基本信息 -->
      <el-descriptions title="预约信息" :column="3" border>
        <el-descriptions-item label="预约编号">{{ appointmentInfo.appointmentNo }}</el-descriptions-item>
        <el-descriptions-item label="患者姓名">{{ appointmentInfo.patientName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ appointmentInfo.patientGender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ appointmentInfo.patientAge }}</el-descriptions-item>
        <el-descriptions-item label="体检日期">{{ appointmentInfo.appointmentDate }}</el-descriptions-item>
        <el-descriptions-item label="体检套餐">{{ appointmentInfo.packageName }}</el-descriptions-item>
      </el-descriptions>

      <!-- 体检项目结果录入 -->
      <div class="exam-items-container">
        <el-divider content-position="left">体检项目结果</el-divider>
        
        <!-- 体检结果表单 -->
        <div v-loading="loading">
          <div v-for="(category, categoryIndex) in examCategories" :key="categoryIndex" class="exam-category">
            <h3>{{ category.name }}</h3>
            
            <el-table :data="category.items" border>
              <el-table-column label="序号" type="index" width="50" />
              <el-table-column label="检查项目" prop="name" width="180" />
              <el-table-column label="检查结果" width="250">
                <template #default="scope">
                  <el-input v-model="scope.row.value" placeholder="请输入检查结果" />
                </template>
              </el-table-column>
              <el-table-column label="正常参考值" prop="normalRange" />
              <el-table-column label="是否异常" width="120">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.isAbnormal"
                    inline-prompt
                    active-text="是"
                    inactive-text="否"
                    :active-value="true"
                    :inactive-value="false"
                  />
                </template>
              </el-table-column>
              <el-table-column label="备注" width="250">
                <template #default="scope">
                  <el-input v-model="scope.row.remark" placeholder="备注" />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        
        <!-- 医生建议 -->
        <div class="doctor-advice">
          <el-divider content-position="left">医生建议</el-divider>
          <el-form :model="adviceForm" label-position="top">
            <el-form-item label="体检总结">
              <el-input
                v-model="adviceForm.summary"
                type="textarea"
                :rows="3"
                placeholder="请输入体检总结..."
              />
            </el-form-item>
            <el-form-item label="健康建议">
              <el-input
                v-model="adviceForm.advice"
                type="textarea"
                :rows="3"
                placeholder="请输入健康建议..."
              />
            </el-form-item>
            <el-form-item label="注意事项">
              <el-input
                v-model="adviceForm.notice"
                type="textarea"
                :rows="3"
                placeholder="请输入注意事项..."
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getAppointmentApi } from '@/api/doctor';

const route = useRoute();
const router = useRouter();
const appointmentId = ref('');
const loading = ref(false);

// 预约信息
const appointmentInfo = ref({
  appointmentNo: '',
  patientName: '',
  patientGender: '',
  patientAge: '',
  appointmentDate: '',
  packageName: ''
});

// 体检分类和项目
const examCategories = ref([
  {
    name: '一般检查',
    items: [
      { name: '身高', value: '', normalRange: '', isAbnormal: false, remark: '' },
      { name: '体重', value: '', normalRange: '', isAbnormal: false, remark: '' },
      { name: '体重指数(BMI)', value: '', normalRange: '18.5-24', isAbnormal: false, remark: '' },
      { name: '血压(收缩压)', value: '', normalRange: '90-140mmHg', isAbnormal: false, remark: '' },
      { name: '血压(舒张压)', value: '', normalRange: '60-90mmHg', isAbnormal: false, remark: '' },
      { name: '脉搏', value: '', normalRange: '60-100次/分', isAbnormal: false, remark: '' }
    ]
  },
  {
    name: '血常规',
    items: [
      { name: '白细胞计数(WBC)', value: '', normalRange: '4-10×10^9/L', isAbnormal: false, remark: '' },
      { name: '红细胞计数(RBC)', value: '', normalRange: '4-5.5×10^12/L', isAbnormal: false, remark: '' },
      { name: '血红蛋白(HGB)', value: '', normalRange: '120-160g/L', isAbnormal: false, remark: '' },
      { name: '血小板计数(PLT)', value: '', normalRange: '100-300×10^9/L', isAbnormal: false, remark: '' }
    ]
  },
  {
    name: '肝功能',
    items: [
      { name: '总胆红素(TBIL)', value: '', normalRange: '5.1-19μmol/L', isAbnormal: false, remark: '' },
      { name: '丙氨酸氨基转移酶(ALT)', value: '', normalRange: '0-40U/L', isAbnormal: false, remark: '' },
      { name: '天门冬氨酸氨基转移酶(AST)', value: '', normalRange: '0-40U/L', isAbnormal: false, remark: '' },
      { name: '总蛋白(TP)', value: '', normalRange: '65-85g/L', isAbnormal: false, remark: '' }
    ]
  }
]);

// 医生建议表单
const adviceForm = reactive({
  summary: '',
  advice: '',
  notice: ''
});

// 初始化加载
onMounted(async () => {
  appointmentId.value = route.params.id as string;
  if (appointmentId.value) {
    await getAppointmentInfo();
  } else {
    ElMessage.error('缺少预约ID参数');
    router.push('/doctor/appointment');
  }
});

// 获取预约信息
const getAppointmentInfo = async () => {
  try {
    loading.value = true;
    
    // 实际项目中应调用API获取数据
    // const res = await getAppointmentApi(appointmentId.value);
    // if (res && res.data) {
    //   appointmentInfo.value = res.data;
    // }
    
    // 模拟获取预约信息
    setTimeout(() => {
      appointmentInfo.value = {
        appointmentNo: 'A20230701001',
        patientName: '张三',
        patientGender: '男',
        patientAge: '35',
        appointmentDate: '2023-07-01',
        packageName: '全面体检套餐A'
      };
      loading.value = false;
    }, 500);
  } catch (error) {
    console.error('获取预约信息失败:', error);
    ElMessage.error('获取预约信息失败');
    loading.value = false;
  }
};

// 保存体检结果
const handleSave = async () => {
  try {
    // 构建保存的数据
    const examResults = examCategories.value.flatMap(category => 
      category.items.map(item => ({
        examItemName: item.name,
        value: item.value,
        normalRange: item.normalRange,
        isAbnormal: item.isAbnormal,
        remark: item.remark
      }))
    );
    
    const saveData = {
      appointmentId: appointmentId.value,
      examResults,
      doctorAdvice: {
        summary: adviceForm.summary,
        advice: adviceForm.advice,
        notice: adviceForm.notice
      }
    };
    
    console.log('保存的数据:', saveData);
    
    // 实际项目中应调用API保存数据
    ElMessage.success('体检结果保存成功');
  } catch (error) {
    console.error('保存体检结果失败:', error);
    ElMessage.error('保存体检结果失败');
  }
};

// 生成体检报告
const handleGenerate = async () => {
  try {
    // 检查是否有未填项目
    const hasEmptyItems = examCategories.value.some(category => 
      category.items.some(item => !item.value)
    );
    
    if (hasEmptyItems) {
      ElMessageBox.confirm('还有体检项目未填写结果，是否继续生成报告？', '提示', {
        confirmButtonText: '继续生成',
        cancelButtonText: '返回填写',
        type: 'warning'
      }).then(() => {
        // 用户确认继续生成
        saveAndGenerateReport();
      }).catch(() => {
        // 用户取消
      });
    } else {
      saveAndGenerateReport();
    }
  } catch (error) {
    console.error('生成体检报告失败:', error);
    ElMessage.error('生成体检报告失败');
  }
};

// 保存并生成报告
const saveAndGenerateReport = async () => {
  // 先保存数据
  await handleSave();
  
  // 实际项目中应调用API生成报告
  ElMessage.success('体检报告生成成功');
  
  // 跳转到报告详情页
  router.push(`/doctor/report/detail/${appointmentId.value}`);
};

// 返回预约列表
const handleReturn = () => {
  router.push('/doctor/appointment');
};
</script>

<style scoped>
.report-input-container {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.exam-items-container {
  margin-top: 20px;
}

.exam-category {
  margin-bottom: 30px;
}

.exam-category h3 {
  margin-bottom: 15px;
  color: #409EFF;
}

.doctor-advice {
  margin-top: 30px;
}
</style> 