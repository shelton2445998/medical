<template>
  <div class="examination-reports-container">
    <div class="page-header">
      <div class="page-title-section">
        <h2 class="page-title">体检报告管理</h2>
        <p class="page-subtitle">管理患者的体检报告，录入检查结果并生成报告</p>
      </div>
      <div class="page-actions">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索患者姓名/手机号"
          class="search-input"
          clearable
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
        <el-button type="primary" class="refresh-btn" @click="refreshData">
          <el-icon><Refresh /></el-icon>刷新
        </el-button>
      </div>
    </div>

    <!-- 报告列表 -->
    <el-card class="report-list-card custom-card" shadow="hover">
      <el-tabs v-model="activeTab" class="custom-tabs" @tab-change="handleTabChange">
        <el-tab-pane label="待录入报告" name="pending">
          <div class="table-toolbar">
            <div class="table-stats">
              <el-tag effect="plain" type="warning">待处理: {{ pendingReports.length }}</el-tag>
            </div>
          </div>
          
          <el-table 
            :data="pendingReports" 
            style="width: 100%" 
            v-loading="loading" 
            border
            stripe
            highlight-current-row
            class="custom-table"
            :empty-text="loading ? '加载中...' : '暂无待处理的体检报告'"
          >
            <el-table-column prop="id" label="报告ID" width="100" />
            <el-table-column label="患者姓名" width="120">
              <template #default="scope">
                <span class="patient-name">{{ scope.row.patientName || '未知' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="性别" width="80">
              <template #default="scope">
                {{ scope.row.patientGenderText || '未知' }}
              </template>
            </el-table-column>
            <el-table-column label="年龄" width="80">
              <template #default="scope">
                {{ scope.row.patientAge || '未知' }}
              </template>
            </el-table-column>
            <el-table-column label="体检日期" width="150">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="itemName" label="检查项目" show-overflow-tooltip />
            <el-table-column label="状态" width="120">
              <template #default="scope">
                <el-tag type="warning" effect="dark" v-if="!scope.row.conclusion">待录入</el-tag>
                <el-tag type="success" effect="dark" v-else>已完成</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="handleEnterResults(scope.row)"
                  class="action-button"
                >
                  <el-icon><Edit /></el-icon>录入结果
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="已完成报告" name="completed">
          <div class="table-toolbar">
            <div class="table-stats">
              <el-tag effect="plain" type="success">已完成: {{ completedReports.length }}</el-tag>
            </div>
          </div>
          
          <el-table 
            :data="completedReports" 
            style="width: 100%" 
            v-loading="loading" 
            border
            stripe
            highlight-current-row
            class="custom-table"
            :empty-text="loading ? '加载中...' : '暂无已完成的体检报告'"
          >
            <el-table-column prop="id" label="报告ID" width="100" />
            <el-table-column label="患者姓名" width="120">
              <template #default="scope">
                <span class="patient-name">{{ scope.row.patientName || '未知' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="性别" width="80">
              <template #default="scope">
                {{ scope.row.patientGenderText || '未知' }}
              </template>
            </el-table-column>
            <el-table-column label="年龄" width="80">
              <template #default="scope">
                {{ scope.row.patientAge || '未知' }}
              </template>
            </el-table-column>
            <el-table-column label="体检日期" width="150">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="报告生成日期" width="150">
              <template #default="scope">
                {{ formatDate(scope.row.updateTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="itemName" label="检查项目" show-overflow-tooltip />
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="scope">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="handleViewReport(scope.row)"
                  class="action-button"
                >
                  <el-icon><View /></el-icon>查看
                </el-button>
                <el-button 
                  type="warning" 
                  size="small" 
                  @click="handleEditReport(scope.row)"
                  class="action-button"
                >
                  <el-icon><EditPen /></el-icon>修改
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <div class="pagination-container">
        <div class="pagination-info">
          共 {{ activeTab === 'pending' ? pendingReports.length : completedReports.length }} 条记录
        </div>
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          background
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
      width="80%"
      destroy-on-close
      class="custom-dialog"
    >
      <div v-if="currentReport" class="dialog-content">
        <div class="panel patient-info-panel">
          <div class="panel-header">
            <h3><el-icon><User /></el-icon>患者信息</h3>
          </div>
          <div class="panel-body">
          <el-descriptions :column="3" border>
            <el-descriptions-item label="姓名">{{ currentReport.patientName || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentReport.patientGenderText || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ currentReport.patientAge || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ currentReport.patientPhone || currentReport.userPhone || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="体检日期">{{ formatDate(currentReport.createTime) }}</el-descriptions-item>
          </el-descriptions>
          </div>
        </div>

        <div class="exam-items-container">
          <div class="panel exam-item-panel" v-for="item in checkItems" :key="item.id">
            <div class="panel-header">
              <h4><el-icon><CheckboxFilled /></el-icon>{{ item.name }}</h4>
            </div>
            <div class="panel-body">
              <el-table :data="item.details" border stripe class="custom-table">
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
          </div>
        </div>

        <div class="panel conclusion-panel">
          <div class="panel-header">
            <h4><el-icon><Document /></el-icon>体检结论</h4>
          </div>
          <div class="panel-body">
          <el-input
            v-model="conclusion"
            type="textarea"
            rows="4"
            placeholder="请输入体检结论和医疗建议"
              class="conclusion-input"
          />
          </div>
        </div>

        <div class="auto-check-option">
          <el-checkbox v-model="isAutoCheckAbnormal">自动标记异常项（根据正常值范围对比）</el-checkbox>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resultDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveResults">
            <el-icon><Check /></el-icon>保存结果
          </el-button>
          <el-button type="success" @click="handleGenerateReport">
            <el-icon><Promotion /></el-icon>生成报告
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看报告详情弹窗 -->
    <el-dialog
      v-model="reportDetailDialogVisible"
      title="体检报告详情"
      width="80%"
      class="custom-dialog report-detail-dialog"
    >
      <div v-if="currentReport" class="report-detail">
        <div class="report-header">
          <h2>体检报告单</h2>
          <div class="report-info">
            <div><strong>报告编号：</strong>{{ currentReport.id }}</div>
            <div><strong>生成日期：</strong>{{ currentReport.reportDate }}</div>
          </div>
        </div>
        
        <div class="panel patient-info-panel">
          <div class="panel-header">
            <h3><el-icon><User /></el-icon>个人信息</h3>
          </div>
          <div class="panel-body">
          <el-descriptions :column="3" border>
            <el-descriptions-item label="姓名">{{ currentReport.patientName || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentReport.patientGenderText || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ currentReport.patientAge || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ currentReport.patientPhone || currentReport.userPhone || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="体检日期">{{ formatDate(currentReport.createTime) }}</el-descriptions-item>
          </el-descriptions>
          </div>
        </div>

        <div class="panel exam-results-panel">
          <div class="panel-header">
            <h3><el-icon><Files /></el-icon>体检结果</h3>
          </div>
          <div class="panel-body">
          <div v-for="item in reportItems" :key="item.id" class="exam-item">
              <h4 class="exam-item-title">
                <el-icon><CheckboxFilled /></el-icon>{{ item.name }}
              </h4>
              <el-table :data="item.details" class="custom-table" border stripe>
              <el-table-column prop="name" label="检查项目" width="180" />
              <el-table-column prop="value" label="检查结果" width="120" />
              <el-table-column prop="unit" label="单位" width="80" />
              <el-table-column prop="normalValue" label="参考范围" width="150" />
              <el-table-column label="结果" width="100">
                <template #default="scope">
                    <el-tag :type="scope.row.isAbnormal ? 'danger' : 'success'" effect="dark">
                    {{ scope.row.isAbnormal ? '异常' : '正常' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="说明" min-width="180">
                <template #default="scope">
                  <span v-if="scope.row.isAbnormal" class="abnormal-hint">
                    {{ getAbnormalHint(scope.row) }}
                  </span>
                  <span v-else class="normal-hint">正常范围内</span>
                </template>
              </el-table-column>
            </el-table>
            </div>
          </div>
        </div>

        <div class="panel conclusion-panel">
          <div class="panel-header">
            <h3><el-icon><Document /></el-icon>体检结论</h3>
          </div>
          <div class="panel-body">
            <el-card class="conclusion-card" shadow="never">
              <div class="conclusion-content">{{ currentReport.conclusion }}</div>
          </el-card>
          </div>
        </div>

        <div class="doctor-signature">
          <p>检查医生：<span class="doctor-name">{{ currentReport.doctorName }}</span></p>
          <p>报告日期：<span>{{ formatDate(currentReport.updateTime || currentReport.createTime) }}</span></p>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reportDetailDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="handlePrintReport">
            <el-icon><Printer /></el-icon>打印报告
          </el-button>
          <el-button type="success" @click="handleDownloadReport">
            <el-icon><Download /></el-icon>下载PDF
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { Search, Refresh, Edit, View, EditPen, Check, Promotion, User, Document, Files, CheckboxFilled, Printer, Download } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getReportList, getReportDetail, submitExaminationResults, generateReport, getPendingReports, getCompletedReports } from '@/api/doctor'

export default {
  name: 'ExaminationReports',
  components: {
    Search,
    Refresh,
    Edit,
    View,
    EditPen,
    Check,
    Promotion,
    User,
    Document,
    Files,
    CheckboxFilled,
    Printer,
    Download
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
    
    // 格式化日期函数
    const formatDate = (dateStr) => {
      if (!dateStr) return '未知';
      // 可以按需调整日期格式
      return dateStr;
    }
    
    // 获取异常提示文本
    const getAbnormalHint = (item) => {
      if (!item.value || !item.normalValue) return '数据异常';
      
      if (item.normalValue.includes('-')) {
        try {
          const [min, max] = item.normalValue.split('-').map(v => parseFloat(v));
          const value = parseFloat(item.value);
          
          if (value < min) {
            return `低于正常值 (${item.normalValue})`;
          } else if (value > max) {
            return `高于正常值 (${item.normalValue})`;
          }
        } catch (e) {
          return '数据异常';
        }
      } else if (item.normalValue === '阴性' || item.normalValue === '阳性') {
        return `应为${item.normalValue}，实际为${item.value}`;
      }
      
      return '异常值';
    };
    
    const fetchReportList = async () => {
      loading.value = true
      try {
        const params = {
          page: currentPage.value,
          pageSize: pageSize.value,
          keyword: searchKeyword.value
        }
        
        // 根据activeTab决定请求哪个接口
        let res;
        if (activeTab.value === 'pending') {
          // 待处理报告使用专门的待处理报告接口
          res = await getPendingReports();
        } else {
          // 已完成报告使用专门的已完成报告接口
          res = await getCompletedReports();
        }
        
        if (res.code === 200) {
          if (activeTab.value === 'pending') {
            pendingReports.value = res.data.records || [];
          } else {
            completedReports.value = res.data.records || [];
          }
          totalRecords.value = res.data.total || 0;
        } else {
          ElMessage.error(res.msg || '获取报告列表失败');
        }
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
        // 确保显示结论
        currentReport.value.conclusion = res.data.conclusion || '无结论'
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
        if (!checkItems.value.some(item => item.details && item.details.some(detail => detail.value))) {
          ElMessage.warning('请先填写检查结果')
          return
        }
        
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
        if (!conclusion.value) {
          ElMessage.warning('请填写体检结论')
          return
        }
        
        if (!checkItems.value.some(item => item.details && item.details.some(detail => detail.value))) {
          ElMessage.warning('请填写检查结果')
          return
        }
        
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
      // 重置数据，避免ResizeObserver错误
      if (activeTab.value === 'pending') {
        completedReports.value = []
      } else {
        pendingReports.value = []
      }
      fetchReportList()
    })
    
    // 刷新数据
    const refreshData = () => {
      fetchReportList()
      ElMessage.success('数据已刷新');
    };
    
    // 处理选项卡变化
    const handleTabChange = (tab) => {
      fetchReportList();
    };
    
    // 下载PDF报告
    const handleDownloadReport = () => {
      ElMessage.success('报告下载中...');
      // 实际实现根据后端API
    };
    
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
      formatDate,
      getAbnormalHint,
      handleSearch,
      handleSizeChange,
      handleCurrentChange,
      handleEnterResults,
      handleViewReport,
      handleEditReport,
      handleSaveResults,
      handleGenerateReport,
      handlePrintReport,
      refreshData,
      handleTabChange,
      handleDownloadReport
    }
  }
}
</script>

<style scoped lang="scss">
.examination-reports-container {
  padding: 24px;
  min-height: calc(100vh - 84px);
  background-color: var(--background-color);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  
  .page-title-section {
    .page-title {
      margin: 0;
      font-size: 24px;
      font-weight: 600;
      color: var(--text-primary);
    }
    
    .page-subtitle {
      margin: 8px 0 0;
      font-size: 14px;
      color: var(--text-secondary);
    }
  }
  
  .page-actions {
    display: flex;
    align-items: center;
    
    .search-input {
      width: 250px;
      margin-right: 16px;
    }
    
    .refresh-btn {
      display: flex;
      align-items: center;
      
      .el-icon {
        margin-right: 4px;
      }
    }
  }
}

.report-list-card {
  margin-bottom: 24px;
  box-shadow: var(--box-shadow-light) !important;
  border-radius: var(--border-radius-md);
  border: none;
  
  .custom-tabs {
    margin-bottom: 20px;
  }
  
  .table-toolbar {
    margin-bottom: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .table-stats {
      .el-tag {
        padding: 6px 12px;
        font-size: 14px;
      }
    }
  }
  
  .custom-table {
    margin-bottom: 20px;
    
    .patient-name {
      font-weight: 500;
      color: var(--primary-color);
    }
    
    .action-button {
      padding: 6px 12px;
      margin: 0 4px;
      
      .el-icon {
        margin-right: 4px;
      }
    }
  }
}

.empty-data {
  padding: 40px 0;
  background-color: var(--background-light);
  border-radius: var(--border-radius-md);
}

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;

  .pagination-info {
    font-size: 14px;
    color: var(--text-secondary);
  }
}

// 弹窗样式
.custom-dialog {
  :deep(.el-dialog__header) {
    border-bottom: 1px solid var(--border-light);
    padding: 20px;
    margin: 0;
    background-color: var(--background-light);
  }
  
  :deep(.el-dialog__body) {
    padding: 24px;
  }
  
  :deep(.el-dialog__footer) {
    border-top: 1px solid var(--border-light);
    padding: 16px 20px;
    background-color: var(--background-light);
  }
}

.dialog-content {
  max-height: 60vh;
  overflow-y: auto;
  padding-right: 6px;
}

// 面板样式
.panel {
  background: #ffffff;
  border-radius: var(--border-radius-md);
  border: 1px solid var(--border-light);
  margin-bottom: 24px;
  overflow: hidden;
  box-shadow: var(--box-shadow-light);
  
  .panel-header {
    padding: 12px 16px;
    border-bottom: 1px solid var(--border-light);
    background-color: var(--background-light);
    
    h3, h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
      display: flex;
      align-items: center;
      
      .el-icon {
        margin-right: 8px;
        color: var(--primary-color);
      }
    }
  }
  
  .panel-body {
    padding: 16px;
  }
}

.exam-items-container {
  margin-bottom: 24px;
}

.conclusion-input {
  :deep(.el-textarea__inner) {
    font-size: 14px;
    min-height: 120px;
  }
}

.auto-check-option {
  margin-bottom: 16px;
}

// 报告详情样式
.report-detail {
  .report-header {
    text-align: center;
    margin-bottom: 24px;
    
    h2 {
      font-size: 24px;
      margin: 0 0 16px;
      color: var(--primary-color);
      font-weight: 600;
    }

    .report-info {
      display: flex;
      justify-content: center;
      gap: 24px;
      font-size: 14px;
      color: var(--text-secondary);
    }
  }

  .exam-item-title {
    margin: 16px 0 12px;
    padding-bottom: 8px;
    border-bottom: 1px dashed var(--border-light);
    font-size: 16px;
    font-weight: 500;
    color: var(--text-primary);
    display: flex;
    align-items: center;
    
    .el-icon {
      margin-right: 8px;
      color: var(--primary-color);
    }
  }

  .conclusion-card {
    background-color: #f9f9f9;
    
    .conclusion-content {
      font-size: 14px;
      line-height: 1.6;
      white-space: pre-line;
    }
  }

  .doctor-signature {
    text-align: right;
    margin-top: 32px;
    padding-top: 16px;
    border-top: 1px dashed var(--border-light);
    
    p {
      font-size: 14px;
      color: var(--text-secondary);
      margin: 8px 0;
      
      .doctor-name {
        font-weight: 500;
        color: var(--text-primary);
      }
    }
  }
  
  .abnormal-hint {
    color: var(--danger-color);
    font-size: 13px;
  }
  
  .normal-hint {
    color: var(--success-color);
    font-size: 13px;
  }
}

// 响应式样式
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    
    .page-title-section {
      margin-bottom: 16px;
    }
    
    .page-actions {
      width: 100%;
      
      .search-input {
        flex: 1;
      }
    }
  }
  
  .pagination-container {
    flex-direction: column-reverse;
    
    .pagination-info {
      margin-top: 16px;
    }
  }
}

// 打印样式
@media print {
  .report-detail-dialog {
    :deep(.el-dialog__header),
    :deep(.el-dialog__footer) {
      display: none;
    }
    
    :deep(.el-dialog__body) {
      padding: 0;
    }
  }
  
  .report-detail {
    padding: 20px;
  }
}
</style> 