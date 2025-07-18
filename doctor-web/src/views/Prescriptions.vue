<!--
  处方管理页面组件
  
  提供处方信息的查询、新增、编辑、删除功能
  支持按患者姓名、ID搜索处方记录
  可以查看处方详情、管理药品清单
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="prescriptions-container">
    <!-- 页面头部 -->
    <div class="prescriptions-header">
      <!-- 页面标题 -->
      <div class="title">处方管理</div>
      <!-- 搜索和操作区域 -->
      <div class="search-bar">
        <!-- 搜索输入框 -->
        <el-input
          v-model="searchInput"
          placeholder="输入患者姓名、ID搜索"
          prefix-icon="el-icon-search"
          clearable
          @keyup.enter="handleSearch"
        />
        <!-- 搜索按钮 -->
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <!-- 新增处方按钮 -->
        <el-button type="success" @click="showAddPrescriptionDialog">新增处方</el-button>
      </div>
    </div>

    <!-- 处方表格区域 -->
    <div class="prescriptions-table">
      <!-- 处方信息表格 -->
      <el-table
        :data="prescriptionList"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <!-- 处方编号列 -->
        <el-table-column prop="prescriptionId" label="处方编号" width="120" />
        <!-- 患者姓名列 -->
        <el-table-column prop="patientName" label="患者姓名" width="120" />
        <!-- 患者ID列 -->
        <el-table-column prop="patientId" label="患者ID" width="120" />
        <!-- 诊断列 -->
        <el-table-column prop="diagnosis" label="诊断" width="180" />
        <!-- 药品清单列 -->
        <el-table-column prop="medicationList" label="药品清单">
          <template #default="scope">
            <div v-for="(med, index) in scope.row.medicationList" :key="index">
              {{ med.name }} {{ med.dosage }} {{ med.frequency }}
            </div>
          </template>
        </el-table-column>
        <!-- 创建时间列 -->
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <!-- 状态列 -->
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <div class="btn-group-right">
              <!-- 查看按钮 -->
              <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
              <!-- 编辑按钮 -->
              <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <!-- 删除按钮 -->
              <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next, jumper, total"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 处方详情对话框 -->
    <el-dialog 
      title="处方详情" 
      v-model="detailsVisible" 
      width="60%"
      :before-close="handleCloseDialog"
    >
      <!-- 处方详情内容 -->
      <div class="prescription-detail">
        <!-- 处方编号 -->
        <div class="detail-item">
          <span class="detail-label">处方编号:</span>
          <span>{{ currentPrescription.prescriptionId }}</span>
        </div>
        <!-- 患者姓名 -->
        <div class="detail-item">
          <span class="detail-label">患者姓名:</span>
          <span>{{ currentPrescription.patientName }}</span>
        </div>
        <!-- 诊断信息 -->
        <div class="detail-item">
          <span class="detail-label">诊断:</span>
          <span>{{ currentPrescription.diagnosis }}</span>
        </div>
        <!-- 药品清单 -->
        <div class="detail-item">
          <span class="detail-label">药品清单:</span>
          <div class="medication-list">
            <div v-for="(med, index) in currentPrescription.medicationList" :key="index" class="medication-item">
              <div>药品名称: {{ med.name }}</div>
              <div>用量: {{ med.dosage }}</div>
              <div>用法: {{ med.frequency }}</div>
            </div>
          </div>
        </div>
        <!-- 医嘱信息 -->
        <div class="detail-item">
          <span class="detail-label">医嘱:</span>
          <span>{{ currentPrescription.instructions }}</span>
        </div>
      </div>
    </el-dialog>

    <!-- 新增/编辑处方对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="formVisible" 
      width="60%" 
      :before-close="handleCloseDialog"
    >
      <!-- 处方表单 -->
      <el-form :model="prescriptionForm" ref="prescriptionFormRef" :rules="formRules" label-width="100px">
        <!-- 患者选择 -->
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="prescriptionForm.patientId" filterable placeholder="请选择患者">
            <el-option
              v-for="item in patientOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 诊断输入 -->
        <el-form-item label="诊断" prop="diagnosis">
          <el-input v-model="prescriptionForm.diagnosis" type="textarea" :rows="2" />
        </el-form-item>
        <!-- 药品清单 -->
        <el-form-item label="药品清单">
          <div class="medication-grid">
            <!-- 药品行 -->
            <div 
              v-for="(med, index) in prescriptionForm.medicationList" 
              :key="index" 
              class="medication-row"
            >
              <!-- 药品名称 -->
              <div class="medication-col">
                <el-input 
                  v-model="med.name" 
                  placeholder="药品名称" 
                  class="medication-input"
                />
              </div>
              <!-- 用量 -->
              <div class="medication-col">
                <el-input 
                  v-model="med.dosage" 
                  placeholder="用量" 
                  class="medication-input"
                />
              </div>
              <!-- 用法 -->
              <div class="medication-col">
                <el-input 
                  v-model="med.frequency" 
                  placeholder="用法" 
                  class="medication-input"
                />
              </div>
              <!-- 删除按钮 -->
              <div class="medication-col" >
                <el-button 
                  type="danger"
                  @click="removeMedication(index)"
                >删除</el-button>
              </div>
            </div>
            <!-- 添加药品按钮行 -->
            <div class="medication-row">
              <div class="medication-col" :colspan="3"></div>
              <div class="medication-col">
                <el-button 
                  type="primary" 
                  @click="addMedication"
                >添加药品</el-button>
              </div>
            </div>
          </div>
        </el-form-item>
        <!-- 医嘱输入 -->
        <el-form-item label="医嘱" prop="instructions">
          <el-input v-model="prescriptionForm.instructions" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <!-- 对话框底部按钮 -->
      <template #footer>
        <el-button @click="handleCloseDialog">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
/**
 * 处方管理页面逻辑
 * 
 * 处理处方信息的查询、新增、编辑、删除等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, reactive, onMounted } from 'vue'
// 导入 Element Plus 组件
import { ElMessage, ElMessageBox } from 'element-plus'
// 导入 HTTP 请求库
import axios from 'axios'

/**
 * 处方管理组件导出
 * 
 * 定义组件的基本信息和逻辑
 */
export default {
  name: 'PrescriptionsView',
  setup() {
    // 搜索相关状态
    const searchInput = ref('')         // 搜索输入
    const loading = ref(false)          // 加载状态
    
    // 分页相关状态
    const currentPage = ref(1)          // 当前页码
    const pageSize = ref(10)            // 每页条数
    const total = ref(0)                // 总记录数
    
    // 列表数据
    const prescriptionList = ref([])    // 处方列表
    
    // 对话框相关状态
    const detailsVisible = ref(false)   // 详情对话框是否可见
    const formVisible = ref(false)      // 表单对话框是否可见
    const dialogTitle = ref('新增处方') // 对话框标题
    const currentPrescription = ref({}) // 当前处方数据
    const isEdit = ref(false)           // 是否为编辑模式
    
    // 选项数据
    const patientOptions = ref([])      // 患者选项列表

    /**
     * 处方表单响应式对象
     * 
     * 存储处方信息的表单数据
     */
    const prescriptionForm = reactive({
      id: '',                           // 处方ID
      patientId: '',                    // 患者ID
      diagnosis: '',                    // 诊断
      medicationList: [{ name: '', dosage: '', frequency: '' }], // 药品清单
      instructions: ''                  // 医嘱
    })

    /**
     * 表单验证规则
     * 
     * 定义处方表单的验证规则
     */
    const formRules = {
      patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
      diagnosis: [{ required: true, message: '请输入诊断', trigger: 'blur' }]
    }

    // 表单引用
    const prescriptionFormRef = ref(null)

    /**
     * 组件挂载时的初始化
     * 
     * 页面加载时获取处方列表和患者列表
     */
    onMounted(() => {
      fetchPrescriptions()
      fetchPatients()
    })

    /**
     * 获取处方列表
     * 
     * 从服务器获取处方信息列表，支持分页和搜索
     */
    const fetchPrescriptions = async () => {
      loading.value = true
      try {
        const params = {
          pageNum: currentPage.value,  // 文档规范的分页参数
          pageSize: pageSize.value,
          keyword: searchInput.value  // 搜索关键词
        }
        // 假设处方接口遵循医生端接口规范，添加api前缀
        const { data: res } = await axios.get('/api/doctor/prescription/list', { params })
        if (res.code === 200) {
          prescriptionList.value = res.data.list
          total.value = res.data.total
        } else {
          ElMessage.error(res.message || '获取处方列表失败')
        }
        loading.value = false
      } catch (error) {
        console.error('获取处方列表失败:', error)
        // 模拟数据
        setTimeout(() => {
          prescriptionList.value = [
            {
              id: '1',
              prescriptionId: 'RX20230001',
              patientId: '1001',
              patientName: '张三',
              diagnosis: '普通感冒',
              medicationList: [
                { name: '感冒灵颗粒', dosage: '1袋', frequency: '每日3次' },
                { name: '维生素C片', dosage: '1片', frequency: '每日1次' }
              ],
              createTime: '2023-06-01 08:30:00',
              status: '已完成',
              instructions: '多喝水，注意休息'
            }
          ]
          total.value = 1
          loading.value = false
        }, 500)
      }
    }

    /**
     * 获取患者列表
     * 
     * 从服务器获取患者信息用于下拉选择
     */
    const fetchPatients = async () => {
      try {
        const { data: res } = await axios.get('/api/doctor/patients/search', { params: { query: '' } })
        if (res.code === 200) {
          patientOptions.value = res.data.map(p => ({
            value: p.patientId,
            label: `${p.name} (${p.patientId})`
          }))
        }
      } catch (error) {
        console.error('获取患者列表失败:', error)
        // 模拟数据
        patientOptions.value = [
          { value: '1001', label: '张三 (1001)' },
          { value: '1002', label: '李四 (1002)' },
          { value: '1003', label: '王五 (1003)' }
        ]
      }
    }

    /**
     * 搜索处理函数
     * 
     * 根据搜索条件重新获取处方列表
     */
    const handleSearch = () => {
      currentPage.value = 1
      fetchPrescriptions()
    }

    /**
     * 页码变化处理
     * 
     * 分页器页码改变时的处理函数
     * 
     * @param {number} page - 新的页码
     */
    const handlePageChange = (page) => {
      currentPage.value = page
      fetchPrescriptions()
    }

    /**
     * 显示新增处方对话框
     * 
     * 重置表单数据并显示新增对话框
     */
    const showAddPrescriptionDialog = () => {
      dialogTitle.value = '新增处方'
      isEdit.value = false
      prescriptionForm.id = ''
      prescriptionForm.patientId = ''
      prescriptionForm.diagnosis = ''
      prescriptionForm.medicationList = [{ name: '', dosage: '', frequency: '' }]
      prescriptionForm.instructions = ''
      formVisible.value = true
    }

    /**
     * 查看处方详情
     * 
     * 显示处方的详细信息对话框
     * 
     * @param {Object} row - 处方数据对象
     */
    const handleView = (row) => {
      currentPrescription.value = row
      detailsVisible.value = true
    }

    /**
     * 编辑处方信息
     * 
     * 打开编辑对话框并填充处方数据
     * 
     * @param {Object} row - 处方数据对象
     */
    const handleEdit = (row) => {
      dialogTitle.value = '编辑处方'
      isEdit.value = true
      prescriptionForm.id = row.id
      prescriptionForm.patientId = row.patientId
      prescriptionForm.diagnosis = row.diagnosis
      prescriptionForm.medicationList = [...row.medicationList]
      prescriptionForm.instructions = row.instructions || ''
      formVisible.value = true
    }

    /**
     * 删除处方
     * 
     * 确认删除处方记录
     * 
     * @param {Object} row - 处方数据对象
     */
    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除此处方吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          try {
            // 调用删除接口
            const { data: res } = await axios.delete(`/api/doctor/prescription/delete/${row.id}`)
            if (res.code === 200) {
              const index = prescriptionList.value.findIndex(item => item.id === row.id)
              if (index !== -1) {
                prescriptionList.value.splice(index, 1)
                total.value -= 1
              }
              ElMessage.success('删除成功')
            } else {
              ElMessage.error(res.message || '删除失败')
            }
          } catch (error) {
            console.error('删除处方失败:', error)
            // 演示模式下直接移除
            const index = prescriptionList.value.findIndex(item => item.id === row.id)
            if (index !== -1) {
              prescriptionList.value.splice(index, 1)
              total.value -= 1
            }
            ElMessage.success('删除成功(演示模式)!')
          }
        })
        .catch(() => {})
    }

    /**
     * 关闭对话框
     * 
     * 隐藏所有对话框
     */
    const handleCloseDialog = () => {
      detailsVisible.value = false
      formVisible.value = false
    }

    /**
     * 添加药品
     * 
     * 在药品清单中添加新的药品项
     */
    const addMedication = () => {
      prescriptionForm.medicationList.push({ name: '', dosage: '', frequency: '' })
    }

    /**
     * 删除药品
     * 
     * 从药品清单中删除指定的药品项
     * 
     * @param {number} index - 要删除的药品索引
     */
    const removeMedication = (index) => {
      if (prescriptionForm.medicationList.length > 1) {
        prescriptionForm.medicationList.splice(index, 1)
      } else {
        ElMessage.warning('至少需要一项药品')
      }
    }

    /**
     * 提交表单
     * 
     * 验证并提交处方表单数据
     */
    const submitForm = () => {
      prescriptionFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            let res
            if (!isEdit.value) {
              // 新增处方：POST请求
              res = await axios.post('/api/doctor/prescription/add', prescriptionForm)
            } else {
              // 编辑处方：PUT请求
              res = await axios.put('/api/doctor/prescription/update', prescriptionForm)
            }
            if (res.data.code === 200) {
              ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
              formVisible.value = false
              fetchPrescriptions()
            } else {
              ElMessage.error(res.data.message || (isEdit.value ? '更新失败' : '添加失败'))
            }
          } catch (error) {
            console.error(isEdit.value ? '更新处方失败:' : '添加处方失败:', error)
            // 演示模式处理
            if (!isEdit.value) {
              const now = new Date()
              const newPrescription = {
                id: (prescriptionList.value.length + 1).toString(),
                prescriptionId: 'RX' + now.getFullYear() + (now.getMonth() + 1).toString().padStart(2, '0') + now.getDate().toString().padStart(2, '0') + (prescriptionList.value.length + 1).toString().padStart(3, '0'),
                patientId: prescriptionForm.patientId,
                patientName: patientOptions.value.find(p => p.value === prescriptionForm.patientId)?.label.split(' ')[0] || '患者',
                diagnosis: prescriptionForm.diagnosis,
                medicationList: [...prescriptionForm.medicationList],
                createTime: now.toLocaleString(),
                status: '处理中',
                instructions: prescriptionForm.instructions
              }
              prescriptionList.value.unshift(newPrescription)
              total.value += 1
            } else {
              const index = prescriptionList.value.findIndex(item => item.id === prescriptionForm.id)
              if (index !== -1) {
                prescriptionList.value[index].diagnosis = prescriptionForm.diagnosis
                prescriptionList.value[index].medicationList = [...prescriptionForm.medicationList]
                prescriptionList.value[index].instructions = prescriptionForm.instructions
              }
            }
            ElMessage.success(isEdit.value ? '更新成功(演示模式)!' : '添加成功(演示模式)!')
            formVisible.value = false
          }
        }
      })
    }

    /**
     * 返回模板需要的数据和方法
     * 
     * 将所有响应式数据和方法暴露给模板使用
     */
    return {
      searchInput,
      loading,
      currentPage,
      pageSize,
      total,
      prescriptionList,
      detailsVisible,
      formVisible,
      dialogTitle,
      currentPrescription,
      prescriptionForm,
      formRules,
      prescriptionFormRef,
      patientOptions,
      handleSearch,
      handlePageChange,
      showAddPrescriptionDialog,
      handleView,
      handleEdit,
      handleDelete,
      handleCloseDialog,
      addMedication,
      removeMedication,
      submitForm
    }
  }
}
</script>

<!--
  处方管理页面样式
  
  定义处方管理页面的视觉设计和布局
  使用 scoped 限制样式作用域
-->
<style scoped>
/**
 * 按钮组右对齐样式
 * 
 * 设置操作按钮组的右对齐布局
 */
.btn-group-right {
  display: flex; 
  justify-content: flex-end; 
}

/**
 * 按钮间距样式
 * 
 * 设置按钮之间的左边距
 */
.el-button {
  margin-left: 8px; 
}

/**
 * 页面主容器样式
 * 
 * 设置页面的基础间距和背景
 */
.prescriptions-container {
  padding: 20px;
  background-color: #f5f7fa;
  height: 100%;
}

/**
 * 页面头部样式
 * 
 * 设置头部区域的布局和间距
 */
.prescriptions-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/**
 * 页面标题样式
 * 
 * 设置主标题的字体和颜色
 */
.prescriptions-header .title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

/**
 * 搜索栏样式
 * 
 * 设置搜索区域的布局和间距
 */
.search-bar {
  display: flex;
  gap: 10px;
}

/**
 * 搜索输入框样式
 * 
 * 设置搜索框的宽度
 */
.search-bar .el-input {
  width: 300px;
}

/**
 * 处方表格容器样式
 * 
 * 设置表格区域的背景和阴影
 */
.prescriptions-table {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/**
 * 分页组件样式
 * 
 * 设置分页组件的位置和间距
 */
.pagination {
  margin-top: 20px;
  text-align: right;
}

/**
 * 处方详情项样式
 * 
 * 设置详情项的间距
 */
.prescription-detail .detail-item {
  margin-bottom: 15px;
}

/**
 * 详情标签样式
 * 
 * 设置详情标签的字体和间距
 */
.detail-label {
  font-weight: bold;
  margin-right: 10px;
}

/**
 * 药品列表样式
 * 
 * 设置药品列表的上边距
 */
.medication-list {
  margin-top: 10px;
}

/**
 * 药品项样式
 * 
 * 设置药品项的间距和内边距
 */
.medication-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

/**
 * 药品表格样式
 * 
 * 设置药品表格的网格布局
 */
.medication-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  margin-top: 10px;
}

/**
 * 药品行样式
 * 
 * 设置药品行的显示方式
 */
.medication-row {
  display: contents;
}

/**
 * 药品列样式
 * 
 * 设置药品列的对齐方式
 */
.medication-col {
  display: flex;
  align-items: center;
}

/**
 * 药品列跨列样式
 * 
 * 设置药品列的跨列显示
 */
.medication-col[colspan] {
  grid-column: span 3;
}
</style>