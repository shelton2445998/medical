<template>
  <div class="examination-reports-container">
    <div class="page-header">
      <h2>体检报告管理</h2>
      <el-input
        v-model="searchKeyword"
        placeholder="搜索患者姓名/手机号"
        style="width: 250px"
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <!-- 报告列表 -->
    <el-card class="report-list-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="待录入报告" name="pending">
          <el-table :data="pendingReports" style="width: 100%" v-loading="loading">
            <el-table-column prop="id" label="报告ID" width="100" />
            <el-table-column prop="patientName" label="患者姓名" width="120" />
            <el-table-column prop="patientGender" label="性别" width="80">
              <template #default="scope">
                {{ scope.row.patientGender === 1 ? '男' : '女' }}
              </template>
            </el-table-column>
            <el-table-column prop="patientAge" label="年龄" width="80" />
            <el-table-column prop="examDate" label="体检日期" width="150" />
            <el-table-column prop="checkItems" label="检查项目" />
            <el-table-column label="操作" width="180">
              <template #default="scope">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="handleEnterResults(scope.row)"
                >录入结果</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="已完成报告" name="completed">
          <el-table :data="completedReports" style="width: 100%" v-loading="loading">
            <el-table-column prop="id" label="报告ID" width="100" />
            <el-table-column prop="patientName" label="患者姓名" width="120" />
            <el-table-column prop="patientGender" label="性别" width="80">
              <template #default="scope">
                {{ scope.row.patientGender === 1 ? '男' : '女' }}
              </template>
            </el-table-column>
            <el-table-column prop="patientAge" label="年龄" width="80" />
            <el-table-column prop="examDate" label="体检日期" width="150" />
            <el-table-column prop="reportDate" label="报告生成日期" width="150" />
            <el-table-column prop="abnormalItems" label="异常项">
              <template #default="scope">
                <el-tag v-if="scope.row.abnormalItems > 0" type="danger">{{ scope.row.abnormalItems }}项异常</el-tag>
                <span v-else>无异常</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="220">
              <template #default="scope">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="handleViewReport(scope.row)"
                >查看详情</el-button>
                <el-button 
                  type="warning" 
                  size="small" 
                  @click="handleEditReport(scope.row)"
                >修改报告</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalRecords"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 录入体检结果弹窗 -->
    <el-dialog
      v-model="resultDialogVisible"
      title="录入体检结果"
      width="70%"
    >
      <div v-if="currentReport">
        <div class="patient-info">
          <h3>患者信息</h3>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="姓名">{{ currentReport.patientName }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentReport.patientGender === 1 ? '男' : '女' }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ currentReport.patientAge }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ currentReport.patientMobile }}</el-descriptions-item>
            <el-descriptions-item label="体检日期">{{ currentReport.examDate }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="exam-items" v-for="item in checkItems" :key="item.id">
          <h4>{{ item.name }}</h4>
          <el-table :data="item.details" style="width: 100%">
            <el-table-column prop="name" label="检查项目" width="180" />
            <el-table-column prop="normalValue" label="正常值" width="180" />
            <el-table-column prop="unit" label="单位" width="100" />
            <el-table-column label="检查结果">
              <template #default="scope">
                <el-input v-model="scope.row.value" placeholder="请输入检查结果" />
              </template>
            </el-table-column>
            <el-table-column label="是否异常" width="150">
              <template #default="scope">
                <el-switch
                  v-model="scope.row.isAbnormal"
                  active-text="异常"
                  inactive-text="正常"
                  :disabled="isAutoCheckAbnormal"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="conclusion-section">
          <h4>体检结论</h4>
          <el-input
            v-model="conclusion"
            type="textarea"
            rows="4"
            placeholder="请输入体检结论和医疗建议"
          />
        </div>

        <div class="auto-check-option">
          <el-checkbox v-model="isAutoCheckAbnormal">自动标记异常项（根据正常值范围对比）</el-checkbox>
        </div>
      </div>
      <template #footer>
        <span>
          <el-button @click="resultDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveResults">保存结果</el-button>
          <el-button type="success" @click="handleGenerateReport">生成报告</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看报告详情弹窗 -->
    <el-dialog
      v-model="reportDetailDialogVisible"
      title="体检报告详情"
      width="70%"
    >
      <div v-if="currentReport" class="report-detail">
        <div class="report-header">
          <h2>体检报告单</h2>
          <div class="report-info">
            <div>报告编号：{{ currentReport.id }}</div>
            <div>生成日期：{{ currentReport.reportDate }}</div>
          </div>
        </div>
        
        <div class="patient-info">
          <h3>个人信息</h3>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="姓名">{{ currentReport.patientName }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentReport.patientGender === 1 ? '男' : '女' }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ currentReport.patientAge }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ currentReport.patientMobile }}</el-descriptions-item>
            <el-descriptions-item label="体检日期">{{ currentReport.examDate }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="exam-results">
          <h3>体检结果</h3>
          <div v-for="item in reportItems" :key="item.id" class="exam-item">
            <h4>{{ item.name }}</h4>
            <el-table :data="item.details" style="width: 100%">
              <el-table-column prop="name" label="检查项目" width="180" />
              <el-table-column prop="value" label="检查结果" width="120" />
              <el-table-column prop="unit" label="单位" width="80" />
              <el-table-column prop="normalValue" label="参考范围" width="150" />
              <el-table-column label="结果" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.isAbnormal ? 'danger' : 'success'">
                    {{ scope.row.isAbnormal ? '异常' : '正常' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <div class="report-conclusion">
          <h3>体检结论</h3>
          <el-card class="conclusion-card">
            {{ currentReport.conclusion }}
          </el-card>
        </div>

        <div class="doctor-signature">
          <p>检查医生：{{ currentReport.doctorName }}</p>
        </div>
      </div>
      <template #footer>
        <span>
          <el-button @click="reportDetailDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="handlePrintReport">打印报告</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getReportList, getReportDetail, submitExaminationResults, generateReport } from '@/api/doctor'

export default {
  name: 'ExaminationReports',
  components: {
    Search
  },
  setup() {
    const loading = ref(false)
    const activeTab = ref('pending')
    const searchKeyword = ref('')
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalRecords = ref(0)
    const pendingReports = ref([])
    const completedReports = ref([])
    
    const resultDialogVisible = ref(false)
    const reportDetailDialogVisible = ref(false)
    const currentReport = ref(null)
    const checkItems = ref([])
    const reportItems = ref([])
    const conclusion = ref('')
    const isAutoCheckAbnormal = ref(true)
    
    const fetchReportList = async () => {
      loading.value = true
      try {
        const params = {
          page: currentPage.value,
          pageSize: pageSize.value,
          keyword: searchKeyword.value,
          status: activeTab.value === 'pending' ? 0 : 1
        }
        
        const res = await getReportList(params)
        if (activeTab.value === 'pending') {
          pendingReports.value = res.data.list
        } else {
          completedReports.value = res.data.list
        }
        totalRecords.value = res.data.total
      } catch (error) {
        console.error('获取报告列表失败', error)
        ElMessage.error('获取报告列表失败')
      } finally {
        loading.value = false
      }
    }
    
    const handleSearch = () => {
      currentPage.value = 1
      fetchReportList()
    }
    
    const handleSizeChange = (val) => {
      pageSize.value = val
      fetchReportList()
    }
    
    const handleCurrentChange = (val) => {
      currentPage.value = val
      fetchReportList()
    }
    
    const handleEnterResults = async (row) => {
      currentReport.value = row
      try {
        const res = await getReportDetail(row.id)
        checkItems.value = res.data.checkItems || []
        conclusion.value = res.data.conclusion || ''
        resultDialogVisible.value = true
      } catch (error) {
        console.error('获取报告详情失败', error)
        ElMessage.error('获取报告详情失败')
      }
    }
    
    const handleViewReport = async (row) => {
      currentReport.value = row
      try {
        const res = await getReportDetail(row.id)
        reportItems.value = res.data.checkItems || []
        reportDetailDialogVisible.value = true
      } catch (error) {
        console.error('获取报告详情失败', error)
        ElMessage.error('获取报告详情失败')
      }
    }
    
    const handleEditReport = (row) => {
      handleEnterResults(row)
    }
    
    const handleSaveResults = async () => {
      try {
        const data = {
          reportId: currentReport.value.id,
          checkItems: checkItems.value.map(item => ({
            id: item.id,
            details: item.details.map(detail => ({
              id: detail.id,
              value: detail.value,
              isAbnormal: detail.isAbnormal
            }))
          })),
          conclusion: conclusion.value
        }
        
        await submitExaminationResults(data)
        ElMessage.success('保存体检结果成功')
        resultDialogVisible.value = false
        fetchReportList()
      } catch (error) {
        console.error('保存体检结果失败', error)
        ElMessage.error('保存体检结果失败')
      }
    }
    
    const handleGenerateReport = async () => {
      try {
        const data = {
          reportId: currentReport.value.id,
          checkItems: checkItems.value.map(item => ({
            id: item.id,
            details: item.details.map(detail => ({
              id: detail.id,
              value: detail.value,
              isAbnormal: detail.isAbnormal
            }))
          })),
          conclusion: conclusion.value
        }
        
        await generateReport(data)
        ElMessage.success('生成体检报告成功')
        resultDialogVisible.value = false
        activeTab.value = 'completed'
        fetchReportList()
      } catch (error) {
        console.error('生成体检报告失败', error)
        ElMessage.error('生成体检报告失败')
      }
    }
    
    const handlePrintReport = () => {
      window.print()
    }
    
    // 监听自动标记异常的变化
    watch(isAutoCheckAbnormal, (newVal) => {
      if (newVal && checkItems.value.length > 0) {
        checkItems.value.forEach(item => {
          if (item.details && item.details.length > 0) {
            item.details.forEach(detail => {
              // 自动判断结果是否异常
              if (detail.normalValue && detail.value) {
                const normalValue = detail.normalValue
                const value = detail.value
                
                // 根据正常值范围判断
                if (normalValue.includes('-')) {
                  // 范围判断
                  const [min, max] = normalValue.split('-').map(v => parseFloat(v))
                  const actualValue = parseFloat(value)
                  if (!isNaN(actualValue) && !isNaN(min) && !isNaN(max)) {
                    detail.isAbnormal = actualValue < min || actualValue > max
                  }
                } else if (normalValue === '阴性' || normalValue === '阳性') {
                  // 阴性/阳性判断
                  detail.isAbnormal = normalValue !== value
                }
              }
            })
          }
        })
      }
    })
    
    // 监听标签页变化
    watch(activeTab, () => {
      currentPage.value = 1
      fetchReportList()
    })
    
    onMounted(() => {
      fetchReportList()
    })
    
    return {
      loading,
      activeTab,
      searchKeyword,
      currentPage,
      pageSize,
      totalRecords,
      pendingReports,
      completedReports,
      resultDialogVisible,
      reportDetailDialogVisible,
      currentReport,
      checkItems,
      reportItems,
      conclusion,
      isAutoCheckAbnormal,
      handleSearch,
      handleSizeChange,
      handleCurrentChange,
      handleEnterResults,
      handleViewReport,
      handleEditReport,
      handleSaveResults,
      handleGenerateReport,
      handlePrintReport
    }
  }
}
</script>

<style scoped>
.examination-reports-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.report-list-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.patient-info {
  margin-bottom: 20px;
}

.exam-items {
  margin-bottom: 20px;
}

.conclusion-section {
  margin: 20px 0;
}

.auto-check-option {
  margin: 10px 0;
}

.report-detail {
  padding: 10px;
}

.report-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.report-info {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 10px;
}

.exam-item {
  margin-bottom: 20px;
}

.conclusion-card {
  padding: 10px;
}

.doctor-signature {
  margin-top: 30px;
  text-align: right;
}

@media print {
  .el-dialog__header,
  .el-dialog__footer {
    display: none;
  }
}
</style> 