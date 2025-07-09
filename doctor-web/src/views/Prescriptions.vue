<template>
  <div class="prescriptions-container">
    <div class="prescriptions-header">
      <div class="title">处方管理</div>
      <div class="search-bar">
        <el-input
          v-model="searchInput"
          placeholder="输入患者姓名、ID搜索"
          prefix-icon="el-icon-search"
          clearable
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button type="success" @click="showAddPrescriptionDialog">新增处方</el-button>
      </div>
    </div>

    <div class="prescriptions-table">
      <el-table
        :data="prescriptionList"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="prescriptionId" label="处方编号" width="120" />
        <el-table-column prop="patientName" label="患者姓名" width="120" />
        <el-table-column prop="patientId" label="患者ID" width="120" />
        <el-table-column prop="diagnosis" label="诊断" width="180" />
        <el-table-column prop="medicationList" label="药品清单">
          <template #default="scope">
            <div v-for="(med, index) in scope.row.medicationList" :key="index">
              {{ med.name }} {{ med.dosage }} {{ med.frequency }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
			  <!-- 外层加 div 控制布局 -->
			  <div class="btn-group-right">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
              </div>
		  </template>
        </el-table-column>
      </el-table>

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
      <div class="prescription-detail">
        <div class="detail-item">
          <span class="detail-label">处方编号:</span>
          <span>{{ currentPrescription.prescriptionId }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">患者姓名:</span>
          <span>{{ currentPrescription.patientName }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">诊断:</span>
          <span>{{ currentPrescription.diagnosis }}</span>
        </div>
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
      <el-form :model="prescriptionForm" ref="prescriptionFormRef" :rules="formRules" label-width="100px">
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
        <el-form-item label="诊断" prop="diagnosis">
          <el-input v-model="prescriptionForm.diagnosis" type="textarea" :rows="2" />
        </el-form-item>
     <!-- 在药品清单的 el-form-item 里，用 Grid 包裹药品行 + 添加按钮 -->
     <el-form-item label="药品清单">
       <div class="medication-grid">
         <!-- 药品行循环 -->
         <div 
           v-for="(med, index) in prescriptionForm.medicationList" 
           :key="index" 
           class="medication-row"
         >
           <div class="medication-col">
             <el-input 
               v-model="med.name" 
               placeholder="药品名称" 
               class="medication-input"
             />
           </div>
           <div class="medication-col">
             <el-input 
               v-model="med.dosage" 
               placeholder="用量" 
               class="medication-input"
             />
           </div>
           <div class="medication-col">
             <el-input 
               v-model="med.frequency" 
               placeholder="用法" 
               class="medication-input"
             />
           </div>
           <div class="medication-col" >
             <el-button 
               type="danger" 
               icon="el-icon-delete" 
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
        <el-form-item label="医嘱" prop="instructions">
          <el-input v-model="prescriptionForm.instructions" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleCloseDialog">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'PrescriptionsView',
  setup() {
    const searchInput = ref('')
    const loading = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    const prescriptionList = ref([])
    const detailsVisible = ref(false)
    const formVisible = ref(false)
    const dialogTitle = ref('新增处方')
    const currentPrescription = ref({})
    const isEdit = ref(false)
    const patientOptions = ref([])

    const prescriptionForm = reactive({
      id: '',
      patientId: '',
      diagnosis: '',
      medicationList: [{ name: '', dosage: '', frequency: '' }],
      instructions: ''
    })

    const formRules = {
      patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
      diagnosis: [{ required: true, message: '请输入诊断', trigger: 'blur' }]
    }

    const prescriptionFormRef = ref(null)

    onMounted(() => {
      fetchPrescriptions()
      fetchPatients()
    })

    const fetchPrescriptions = async () => {
      loading.value = true
      try {
        // 这里应该是从API获取处方数据
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
            },
            {
              id: '2',
              prescriptionId: 'RX20230002',
              patientId: '1002',
              patientName: '李四',
              diagnosis: '咽喉炎',
              medicationList: [
                { name: '金嗓子喉片', dosage: '2片', frequency: '每日3次' },
                { name: '阿莫西林胶囊', dosage: '1粒', frequency: '每日3次' }
              ],
              createTime: '2023-06-02 10:15:00',
              status: '处理中',
              instructions: '多喝温水，避免辛辣食物'
            }
          ]
          total.value = 2
          loading.value = false
        }, 500)
      } catch (error) {
        console.error('获取处方列表失败:', error)
        loading.value = false
      }
    }

    const fetchPatients = () => {
      // 模拟从API获取患者列表
      patientOptions.value = [
        { value: '1001', label: '张三 (1001)' },
        { value: '1002', label: '李四 (1002)' },
        { value: '1003', label: '王五 (1003)' }
      ]
    }

    const handleSearch = () => {
      currentPage.value = 1
      fetchPrescriptions()
    }

    const handlePageChange = (page) => {
      currentPage.value = page
      fetchPrescriptions()
    }

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

    const handleView = (row) => {
      currentPrescription.value = row
      detailsVisible.value = true
    }

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

    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除此处方吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // 这里应该调用API删除处方
          // 在演示模式下直接从列表中移除
          const index = prescriptionList.value.findIndex(item => item.id === row.id)
          if (index !== -1) {
            prescriptionList.value.splice(index, 1)
            total.value -= 1
          }
          
          ElMessage({
            type: 'success',
            message: '删除成功(演示模式)!'
          })
        })
        .catch(() => {
          // 取消删除
        })
    }

    const handleCloseDialog = () => {
      detailsVisible.value = false
      formVisible.value = false
    }

    const addMedication = () => {
      prescriptionForm.medicationList.push({ name: '', dosage: '', frequency: '' })
    }

    const removeMedication = (index) => {
      if (prescriptionForm.medicationList.length > 1) {
        prescriptionForm.medicationList.splice(index, 1)
      } else {
        ElMessage.warning('至少需要一项药品')
      }
    }

    const submitForm = () => {
      prescriptionFormRef.value.validate((valid) => {
        if (valid) {
          // 这里应该调用API保存处方
          
          // 在演示模式下添加处方到列表
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
            // 更新已有处方
            const index = prescriptionList.value.findIndex(item => item.id === prescriptionForm.id)
            if (index !== -1) {
              prescriptionList.value[index].diagnosis = prescriptionForm.diagnosis
              prescriptionList.value[index].medicationList = [...prescriptionForm.medicationList]
              prescriptionList.value[index].instructions = prescriptionForm.instructions
            }
          }
          
          ElMessage({
            type: 'success',
            message: isEdit.value ? '更新成功(演示模式)!' : '添加成功(演示模式)!'
          })
          formVisible.value = false
        }
      })
    }

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

<style scoped>
	.btn-group-right {
	  /* Flex 布局，按钮靠右 */
	  display: flex; 
	  justify-content: flex-end; 
	}
	
	/* 按钮间距（可选，避免挤在一起） */
	.el-button {
	  margin-left: 8px; 
	}
.prescriptions-container {
  padding: 20px;
  background-color: #f5f7fa;
  height: 100%;
}

.prescriptions-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.prescriptions-header .title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.search-bar {
  display: flex;
  gap: 10px;
}

.search-bar .el-input {
  width: 300px;
}

.prescriptions-table {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.prescription-detail .detail-item {
  margin-bottom: 15px;
}

.detail-label {
  font-weight: bold;
  margin-right: 10px;
}

.medication-list {
  margin-top: 10px;
}

.medication-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

/* .delete-btn-col {
  display: flex;
  justify-content: flex-end;
  align-items: center;
} */
/* 添加药品按钮 */
/* .add-medication-btn {
  margin-top: 10px;
  text-align: right;
} */
.medication-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4列：名称、用量、用法、操作 */
  gap: 10px; /* 列间距 */
  margin-top: 10px;
}
.medication-row {
  display: contents; /* 让行容器不影响 Grid 布局 */
}
.medication-col {
  display: flex;
  align-items: center;
  
}
/* 让“添加药品”按钮所在列与“删除”按钮列对齐 */
.medication-col[colspan] {
  grid-column: span 3; /* 占3列，把按钮挤到第4列 */
}
</style> 