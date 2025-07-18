<!--
  体检报告管理页面组件
  
  提供体检报告的查询、录入、查看、编辑功能
  支持分页签管理待录入和已完成的报告
  可以查看报告详情、录入检查结果并生成报告
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="examination-reports-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <!-- 页面标题区域 -->
      <div class="page-title-section">
        <h2 class="page-title">体检报告管理</h2>
        <p class="page-subtitle">管理患者的体检报告，录入检查结果并生成报告</p>
      </div>
      <!-- 页面操作区域 -->
      <div class="page-actions">
        <!-- 搜索输入框 -->
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
        <!-- 刷新按钮 -->
        <el-button type="primary" class="refresh-btn" @click="refreshData">
          <el-icon><Refresh /></el-icon>刷新
        </el-button>
      </div>
    </div>

    <!-- 报告列表 -->
    <el-card class="report-list-card custom-card" shadow="hover">
      <!-- 页签切换 -->
      <el-tabs v-model="activeTab" class="custom-tabs" @tab-change="handleTabChange">
        <!-- 待录入报告页签 -->
        <el-tab-pane label="待录入报告" name="pending">
          <!-- 表格工具栏 -->
          <div class="table-toolbar">
            <div class="table-stats">
              <el-tag effect="plain" type="warning">待处理: {{ pendingReports.length }}</el-tag>
            </div>
          </div>
          
          <!-- 待录入报告表格 -->
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
            <!-- 报告ID列 -->
            <el-table-column prop="id" label="报告ID" width="100" />
            <!-- 患者姓名列 -->
            <el-table-column label="患者姓名" width="120">
              <template #default="scope">
                <span class="patient-name">{{ scope.row.patientName || '未知' }}</span>
              </template>
            </el-table-column>
            <!-- 患者性别列 -->
            <el-table-column label="性别" width="80">
              <template #default="scope">
                {{ scope.row.patientGenderText || '未知' }}
              </template>
            </el-table-column>
            <!-- 患者年龄列 -->
            <el-table-column label="年龄" width="80">
              <template #default="scope">
                {{ scope.row.patientAge || '未知' }}
              </template>
            </el-table-column>
            <!-- 体检日期列 -->
            <el-table-column label="体检日期" width="150">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <!-- 检查项目列 -->
            <el-table-column prop="itemName" label="检查项目" show-overflow-tooltip />
            <!-- 状态列 -->
            <el-table-column label="状态" width="120">
              <template #default="scope">
                <el-tag type="warning" effect="dark" v-if="!scope.row.conclusion">待录入</el-tag>
                <el-tag type="success" effect="dark" v-else>已完成</el-tag>
              </template>
            </el-table-column>
            <!-- 操作列 -->
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

        <!-- 已完成报告页签 -->
        <el-tab-pane label="已完成报告" name="completed">
          <!-- 表格工具栏 -->
          <div class="table-toolbar">
            <div class="table-stats">
              <el-tag effect="plain" type="success">已完成: {{ completedReports.length }}</el-tag>
            </div>
          </div>
          
          <!-- 已完成报告表格 -->
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
            <!-- 报告ID列 -->
            <el-table-column prop="id" label="报告ID" width="100" />
            <!-- 患者姓名列 -->
            <el-table-column label="患者姓名" width="120">
              <template #default="scope">
                <span class="patient-name">{{ scope.row.patientName || '未知' }}</span>
              </template>
            </el-table-column>
            <!-- 患者性别列 -->
            <el-table-column label="性别" width="80">
              <template #default="scope">
                {{ scope.row.patientGenderText || '未知' }}
              </template>
            </el-table-column>
            <!-- 患者年龄列 -->
            <el-table-column label="年龄" width="80">
              <template #default="scope">
                {{ scope.row.patientAge || '未知' }}
              </template>
            </el-table-column>
            <!-- 体检日期列 -->
            <el-table-column label="体检日期" width="150">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <!-- 报告生成日期列 -->
            <el-table-column label="报告生成日期" width="150">
              <template #default="scope">
                {{ formatDate(scope.row.updateTime) }}
              </template>
            </el-table-column>
            <!-- 检查项目列 -->
            <el-table-column prop="itemName" label="检查项目" show-overflow-tooltip />
            <!-- 操作列 -->
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="scope">
                <!-- 查看报告按钮 -->
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="handleViewReport(scope.row)"
                  class="action-button"
                >
                  <el-icon><View /></el-icon>查看
                </el-button>
                <!-- 修改报告按钮 -->
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

      <!-- 分页区域 -->
      <div class="pagination-container">
        <!-- 分页信息 -->
        <div class="pagination-info">
          共 {{ activeTab === 'pending' ? pendingReports.length : completedReports.length }} 条记录
        </div>
        <!-- 分页组件 -->
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
        <!-- 患者信息面板 -->
        <div class="panel patient-info-panel">
          <div class="panel-header">
            <h3><el-icon><User /></el-icon>患者信息</h3>
          </div>
          <div class="panel-body">
            <!-- 患者信息描述列表 -->
            <el-descriptions :column="3" border>
              <el-descriptions-item label="姓名">{{ currentReport.patientName || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ currentReport.patientGenderText || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="年龄">{{ currentReport.patientAge || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="手机号">{{ currentReport.patientPhone || currentReport.userPhone || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="体检日期">{{ formatDate(currentReport.createTime) }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </div>

        <!-- 体检结果部分已移除 -->

        <!-- 体检结论面板 -->
        <div class="panel conclusion-panel">
          <div class="panel-header">
            <h3><el-icon><Document /></el-icon>体检结论</h3>
          </div>
          <div class="panel-body">
            <!-- 结论输入区域 -->
            <el-form-item>
              <el-input
                v-model="conclusion"
                type="textarea"
                :rows="4"
                placeholder="请输入体检结论..."
                class="conclusion-input"
              />
            </el-form-item>
          </div>
        </div>
      </div>
      <!-- 录入结果对话框底部 -->
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
        <!-- 报告头部信息 -->
        <div class="report-header">
          <h2>体检报告单</h2>
          <div class="report-info">
            <div><strong>报告编号：</strong>{{ currentReport.id }}</div>
            <div><strong>生成日期：</strong>{{ currentReport.reportDate }}</div>
          </div>
        </div>
        
        <!-- 患者信息面板 -->
        <div class="panel patient-info-panel">
          <div class="panel-header">
            <h3><el-icon><User /></el-icon>个人信息</h3>
          </div>
          <div class="panel-body">
            <!-- 患者信息描述列表 -->
            <el-descriptions :column="3" border>
              <el-descriptions-item label="姓名">{{ currentReport.patientName || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ currentReport.patientGenderText || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="年龄">{{ currentReport.patientAge || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="手机号">{{ currentReport.patientPhone || currentReport.userPhone || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="体检日期">{{ formatDate(currentReport.createTime) }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </div>

        <!-- 体检结果部分已移除 -->

        <!-- 体检结论面板 -->
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

        <!-- 医生签名区域 -->
        <div class="doctor-signature">
          <p>检查医生：<span class="doctor-name">{{ currentReport.doctorName }}</span></p>
          <p>报告日期：<span>{{ formatDate(currentReport.updateTime || currentReport.createTime) }}</span></p>
        </div>
      </div>
      <!-- 报告详情对话框底部 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reportDetailDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="handlePrintReport">
            <el-icon><Printer /></el-icon>打印报告
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
/**
 * 体检报告管理页面逻辑
 * 
 * 处理体检报告的查询、录入、查看、编辑等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, reactive, onMounted, computed, watch } from 'vue'
// 导入 Element Plus 图标组件
import { Search, Refresh, Edit, View, EditPen, Check, Promotion, User, Document, Files, CheckboxFilled, Printer } from '@element-plus/icons-vue'
// 导入 Element Plus 组件
import { ElMessage } from 'element-plus'
// 导入API接口函数
import { getReportList, getReportDetail, submitExaminationResults, generateReport, getPendingReports, getCompletedReports } from '@/api/doctor'

/**
 * 体检报告管理组件导出
 * 
 * 定义组件的基本信息和逻辑
 */
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
    Printer
  },
  setup() {
    // 基础状态
    const loading = ref(false)            // 加载状态
    const activeTab = ref('pending')      // 当前活动页签
    const searchKeyword = ref('')         // 搜索关键词
    
    // 分页相关
    const currentPage = ref(1)            // 当前页码
    const pageSize = ref(10)              // 每页条数
    const totalRecords = ref(0)           // 总记录数
    
    // 报告数据
    const pendingReports = ref([])        // 待录入报告列表
    const completedReports = ref([])      // 已完成报告列表
    
    // 对话框相关
    const resultDialogVisible = ref(false)         // 录入结果对话框可见性
    const reportDetailDialogVisible = ref(false)   // 报告详情对话框可见性
    const currentReport = ref(null)                // 当前操作的报告
    const checkItems = ref([])                     // 检查项目列表
    const reportItems = ref([])                    // 报告项目列表
    const conclusion = ref('')                     // 体检结论
    const isAutoCheckAbnormal = ref(true)          // 自动检查异常
    
    /**
     * 格式化日期函数
     * 
     * 格式化日期字符串显示
     * 
     * @param {string} dateStr - 日期字符串
     * @returns {string} 格式化后的日期
     */
    const formatDate = (dateStr) => {
      if (!dateStr) return '未知';
      // 可以按需调整日期格式
      return dateStr;
    }
    
    /**
     * 获取异常提示文本
     * 
     * 根据检查项目的值和正常值范围生成异常提示
     * 
     * @param {Object} item - 检查项目对象
     * @returns {string} 异常提示文本
     */
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
    
    /**
     * 获取报告列表
     * 
     * 根据当前页签获取对应的报告列表数据
     */
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
    
    /**
     * 搜索处理函数
     * 
     * 根据搜索关键词重新获取报告列表
     */
    const handleSearch = () => {
      currentPage.value = 1
      fetchReportList()
    }
    
    /**
     * 每页条数变化处理
     * 
     * 分页器每页条数改变时的处理函数
     * 
     * @param {number} val - 新的每页条数
     */
    const handleSizeChange = (val) => {
      pageSize.value = val
      fetchReportList()
    }
    
    /**
     * 页码变化处理
     * 
     * 分页器页码改变时的处理函数
     * 
     * @param {number} val - 新的页码
     */
    const handleCurrentChange = (val) => {
      currentPage.value = val
      fetchReportList()
    }
    
    /**
     * 录入检查结果
     * 
     * 打开录入结果对话框并获取报告详情
     * 
     * @param {Object} row - 报告记录对象
     */
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
    
    /**
     * 查看报告详情
     * 
     * 打开报告详情对话框并获取完整信息
     * 
     * @param {Object} row - 报告记录对象
     */
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
    
    /**
     * 编辑报告
     * 
     * 编辑报告时复用录入结果功能
     * 
     * @param {Object} row - 报告记录对象
     */
    const handleEditReport = (row) => {
      handleEnterResults(row)
    }
    
    /**
     * 保存检查结果
     * 
     * 验证并保存检查结果到服务器
     */
    const handleSaveResults = async () => {
      try {
        // 打印调试信息
        console.log('checkItems:', checkItems.value);
        
        // 检查是否至少有一个检查结果或者结论
        const hasCheckItemValues = checkItems.value.some(item => 
          item.details && item.details.some(detail => detail.value && detail.value.trim() !== '')
        );
        
        const hasConclusion = conclusion.value && conclusion.value.trim() !== '';
        
        console.log('hasCheckItemValues:', hasCheckItemValues, 'hasConclusion:', hasConclusion);
        
        // 如果既没有检查结果也没有结论，则提示用户
        if (!hasCheckItemValues && !hasConclusion) {
          ElMessage.warning('请填写检查结果或体检结论')
          return
        }
        
        // 修正数据结构，确保与后端API匹配
        const data = {
          reportId: currentReport.value.id,
          conclusion: conclusion.value,
          checkItems: checkItems.value.map(item => ({
            id: item.id,
            details: item.details.map(detail => ({
              id: detail.id,
              value: detail.value || '', // 确保undefined/null值被转换为空字符串
              isAbnormal: detail.isAbnormal ? 1 : 0 // 确保使用整数而不是布尔值
            }))
          }))
        }
        
        // 添加控制台日志，帮助调试
        console.log('提交的数据:', JSON.stringify(data))
        
        const res = await submitExaminationResults(data)
        if (res && res.code === 200) {
          ElMessage.success('保存体检结果成功')
          resultDialogVisible.value = false
          fetchReportList()
        } else {
          ElMessage.error((res && res.msg) || '保存体检结果失败')
        }
      } catch (error) {
        console.error('保存体检结果失败', error)
        ElMessage.error(error.message || '保存体检结果失败')
      }
    }
    
    /**
     * 生成体检报告
     * 
     * 验证数据并生成正式的体检报告
     */
    const handleGenerateReport = async () => {
      try {
        if (!conclusion.value || conclusion.value.trim() === '') {
          ElMessage.warning('请填写体检结论')
          return
        }
        
        // 检查是否至少有一个检查结果
        const hasCheckItemValues = checkItems.value.some(item => 
          item.details && item.details.some(detail => detail.value && detail.value.trim() !== '')
        );
        
        // 生成报告需要至少一个检查结果
        if (!hasCheckItemValues) {
          ElMessage.warning('请填写至少一项检查结果')
          return
        }
        
        // 修正数据结构，确保与后端API匹配
        const data = {
          reportId: currentReport.value.id,
          conclusion: conclusion.value,
          checkItems: checkItems.value.map(item => ({
            id: item.id,
            details: item.details.map(detail => ({
              id: detail.id,
              value: detail.value || '', // 确保undefined/null值被转换为空字符串
              isAbnormal: detail.isAbnormal ? 1 : 0 // 确保使用整数而不是布尔值
            }))
          }))
        }
        
        // 添加控制台日志，帮助调试
        console.log('生成报告的数据:', JSON.stringify(data))
        
        const res = await generateReport(data)
        if (res && res.code === 200) {
          ElMessage.success('生成体检报告成功')
          resultDialogVisible.value = false
          activeTab.value = 'completed'
          fetchReportList()
        } else {
          ElMessage.error((res && res.msg) || '生成体检报告失败')
        }
      } catch (error) {
        console.error('生成体检报告失败', error)
        ElMessage.error(error.message || '生成体检报告失败')
      }
    }
    
    /**
     * 打印报告
     * 
     * 调用浏览器打印功能打印报告
     */
    const handlePrintReport = () => {
      window.print()
    }
    
    /**
     * 监听自动标记异常的变化
     * 
     * 当开启自动标记异常时，自动判断检查结果是否异常
     */
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
    
    /**
     * 监听标签页变化
     * 
     * 当切换标签页时重新获取对应的数据
     */
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
    
    /**
     * 刷新数据
     * 
     * 手动刷新当前页面数据
     */
    const refreshData = () => {
      fetchReportList()
      ElMessage.success('数据已刷新');
    };
    
    /**
     * 处理选项卡变化
     * 
     * 当选项卡改变时重新获取数据
     * 
     * @param {string} tab - 选项卡名称
     */
    const handleTabChange = (tab) => {
      fetchReportList();
    };
    
    // 此部分删除下载PDF相关函数
    // 下载PDF报告
    // const handleDownloadReport = () => {
    //   ElMessage.success('报告下载中...');
    //   // 实际实现根据后端API
    // };
    
    /**
     * 组件挂载时的初始化
     * 
     * 页面加载时自动获取报告列表
     */
    onMounted(() => {
      fetchReportList()
    })
    
    /**
     * 返回模板需要的数据和方法
     * 
     * 将所有响应式数据和方法暴露给模板使用
     */
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
      handleTabChange
      // 移除 handleDownloadReport
    }
  }
}
</script>

<!--
  体检报告管理页面样式
  
  定义体检报告管理页面的视觉设计和布局
  使用 SCSS 语法编写样式，scoped 限制作用域
-->
<style scoped lang="scss">
/**
 * 页面主容器样式
 * 
 * 设置页面的基础间距和背景
 */
.examination-reports-container {
  padding: 24px;
  min-height: calc(100vh - 84px);
  background-color: var(--background-color);
}

/**
 * 页面头部样式
 * 
 * 设置头部区域的布局和间距
 */
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

/**
 * 报告列表卡片样式
 * 
 * 设置报告列表卡片的外观和内部布局
 */
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

/**
 * 空数据样式
 * 
 * 设置空数据状态的样式
 */
.empty-data {
  padding: 40px 0;
  background-color: var(--background-light);
  border-radius: var(--border-radius-md);
}

/**
 * 分页容器样式
 * 
 * 设置分页组件的布局和间距
 */
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

/**
 * 自定义对话框样式
 * 
 * 设置对话框的头部、主体和底部样式
 */
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

/**
 * 对话框内容样式
 * 
 * 设置对话框内容的滚动和间距
 */
.dialog-content {
  max-height: 60vh;
  overflow-y: auto;
  padding-right: 6px;
}

/**
 * 面板样式
 * 
 * 设置面板组件的通用样式
 */
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

/**
 * 检查项目容器样式
 * 
 * 设置检查项目容器的间距
 */
.exam-items-container {
  margin-bottom: 24px;
}

/**
 * 结论输入框样式
 * 
 * 设置结论输入框的样式
 */
.conclusion-input {
  :deep(.el-textarea__inner) {
    font-size: 14px;
    min-height: 120px;
  }
}

/**
 * 自动检查选项样式
 * 
 * 设置自动检查选项的间距
 */
.auto-check-option {
  margin-bottom: 16px;
}

/**
 * 报告详情样式
 * 
 * 设置报告详情页面的布局和样式
 */
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

/**
 * 响应式样式
 * 
 * 针对移动设备的样式适配
 */
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