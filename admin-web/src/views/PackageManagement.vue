<template>
  <div class="package-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>体检套餐管理</span>
          <el-button type="primary" @click="handleAddPackage">新增套餐</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="套餐名称">
          <el-input v-model="searchForm.packageName" placeholder="请输入套餐名称" clearable />
        </el-form-item>
        <el-form-item label="套餐类型">
          <el-select v-model="searchForm.packageType" placeholder="请选择套餐类型" clearable>
            <el-option label="基础套餐" value="1" />
            <el-option label="高级套餐" value="2" />
            <el-option label="专项套餐" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="上架" value="1" />
            <el-option label="下架" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="tableData" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="packageName" label="套餐名称" />
        <el-table-column prop="packageType" label="套餐类型">
          <template #default="scope">
            <el-tag v-if="scope.row.packageType === '1'" type="success">基础套餐</el-tag>
            <el-tag v-else-if="scope.row.packageType === '2'" type="warning">高级套餐</el-tag>
            <el-tag v-else-if="scope.row.packageType === '3'" type="danger">专项套餐</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="原价(元)" />
        <el-table-column prop="discountPrice" label="优惠价(元)" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleViewDetail(scope.row)">查看</el-button>
            <el-button type="success" size="small" @click="handleEditPackage(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDeletePackage(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="searchForm.pageSize"
          :current-page="searchForm.pageNum"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 套餐表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="650px"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-form ref="packageForm" :model="packageForm" :rules="packageRules" label-width="100px">
        <el-form-item label="套餐名称" prop="packageName">
          <el-input v-model="packageForm.packageName" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-form-item label="套餐类型" prop="packageType">
          <el-select v-model="packageForm.packageType" placeholder="请选择套餐类型" style="width: 100%">
            <el-option label="基础套餐" value="1" />
            <el-option label="高级套餐" value="2" />
            <el-option label="专项套餐" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="套餐价格" prop="price">
          <el-input-number v-model="packageForm.price" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="优惠价格" prop="discountPrice">
          <el-input-number v-model="packageForm.discountPrice" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="packageForm.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="套餐描述" prop="description">
          <el-input
            v-model="packageForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入套餐描述"
          />
        </el-form-item>

        <el-form-item label="检查项目">
          <el-transfer
            v-model="packageForm.selectedItems"
            :data="checkItemsData"
            :titles="['可选项目', '已选项目']"
            :button-texts="['移除', '添加']"
            :props="{
              key: 'itemId',
              label: 'itemName'
            }"
            style="text-align: left; width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="套餐详情" v-model="detailDialogVisible" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="套餐ID">{{ detailData.packageId }}</el-descriptions-item>
        <el-descriptions-item label="套餐名称">{{ detailData.packageName }}</el-descriptions-item>
        <el-descriptions-item label="套餐类型">
          <el-tag v-if="detailData.packageType === '1'" type="success">基础套餐</el-tag>
          <el-tag v-else-if="detailData.packageType === '2'" type="warning">高级套餐</el-tag>
          <el-tag v-else-if="detailData.packageType === '3'" type="danger">专项套餐</el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="原价(元)">{{ detailData.price }}</el-descriptions-item>
        <el-descriptions-item label="优惠价(元)">{{ detailData.discountPrice }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 1 ? 'success' : 'info'">
            {{ detailData.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="套餐描述">
          {{ detailData.description || '暂无描述' }}
        </el-descriptions-item>
      </el-descriptions>

      <el-divider>包含检查项目</el-divider>
      <el-table :data="detailData.items || []" style="width: 100%" max-height="300">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="itemName" label="项目名称" />
        <el-table-column prop="price" label="价格(元)" width="120" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 加载状态
const loading = ref(false)
// 套餐表单对话框显示状态
const dialogVisible = ref(false)
// 套餐详情对话框显示状态
const detailDialogVisible = ref(false)
// 对话框标题
const dialogTitle = ref('新增套餐')
// 操作类型：add-新增，edit-编辑
const operationType = ref('add')
// 分页总数
const total = ref(0)

// 搜索表单数据
const searchForm = reactive({
  packageName: '',
  packageType: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

// 表格数据
const tableData = ref([
  {
    packageId: 1,
    packageName: '全面体检套餐',
    packageType: '1',
    price: 1200.00,
    discountPrice: 999.00,
    status: 1,
    createTime: '2023-07-10 14:30:00',
    updateTime: '2023-07-10 14:30:00',
    description: '全面体检套餐，包含血常规、尿常规、心电图等基础检查项目'
  },
  {
    packageId: 2,
    packageName: '入职体检套餐',
    packageType: '1',
    price: 800.00,
    discountPrice: 700.00,
    status: 1,
    createTime: '2023-07-09 10:15:00',
    updateTime: '2023-07-09 10:15:00',
    description: '入职体检套餐，满足企业入职体检需求'
  },
  {
    packageId: 3,
    packageName: '高级体检套餐',
    packageType: '2',
    price: 2800.00,
    discountPrice: 2500.00,
    status: 1,
    createTime: '2023-07-08 09:20:00',
    updateTime: '2023-07-08 09:20:00',
    description: '高级体检套餐，包含全面体检套餐的全部项目，并增加CT、核磁共振等高级检查项目'
  },
  {
    packageId: 4,
    packageName: '女性体检套餐',
    packageType: '3',
    price: 1500.00,
    discountPrice: 1300.00,
    status: 0,
    createTime: '2023-07-07 14:00:00',
    updateTime: '2023-07-11 10:30:00',
    description: '女性体检套餐，针对女性健康问题定制'
  },
  {
    packageId: 5,
    packageName: '心脑血管检查',
    packageType: '3',
    price: 3200.00,
    discountPrice: 2800.00,
    status: 1,
    createTime: '2023-07-06 11:25:00',
    updateTime: '2023-07-06 11:25:00',
    description: '心脑血管专项检查，针对心脑血管疾病高危人群'
  }
])

// 套餐表单数据
const packageForm = reactive({
  packageId: null,
  packageName: '',
  packageType: '1',
  price: 0,
  discountPrice: 0,
  status: 1,
  description: '',
  selectedItems: []
})

// 套餐表单验证规则
const packageRules = {
  packageName: [
    { required: true, message: '请输入套餐名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  packageType: [{ required: true, message: '请选择套餐类型', trigger: 'change' }],
  price: [{ required: true, message: '请输入套餐价格', trigger: 'blur' }],
  discountPrice: [{ required: true, message: '请输入优惠价格', trigger: 'blur' }]
}

// 检查项目数据
const checkItemsData = ref([
  { itemId: 1, itemName: '血常规', price: 50 },
  { itemId: 2, itemName: '尿常规', price: 50 },
  { itemId: 3, itemName: '肝功能', price: 100 },
  { itemId: 4, itemName: '肾功能', price: 100 },
  { itemId: 5, itemName: '心电图', price: 150 },
  { itemId: 6, itemName: '胸部X光', price: 200 },
  { itemId: 7, itemName: 'B超', price: 300 },
  { itemId: 8, itemName: 'CT', price: 800 },
  { itemId: 9, itemName: '核磁共振', price: 1500 },
  { itemId: 10, itemName: '妇科检查', price: 200 },
  { itemId: 11, itemName: '前列腺检查', price: 200 },
  { itemId: 12, itemName: '骨密度检查', price: 250 }
])

// 详情数据
const detailData = reactive({})

// 初始化数据
onMounted(() => {
  fetchData()
})

// 获取表格数据
const fetchData = () => {
  loading.value = true
  // 模拟请求
  setTimeout(() => {
    total.value = tableData.value.length
    loading.value = false
  }, 500)
}

// 处理查询
const handleSearch = () => {
  searchForm.pageNum = 1
  fetchData()
}

// 重置查询
const resetSearch = () => {
  searchForm.packageName = ''
  searchForm.packageType = ''
  searchForm.status = ''
  searchForm.pageNum = 1
  fetchData()
}

// 处理分页大小变化
const handleSizeChange = (val) => {
  searchForm.pageSize = val
  fetchData()
}

// 处理页码变化
const handleCurrentChange = (val) => {
  searchForm.pageNum = val
  fetchData()
}

// 处理状态变更
const handleStatusChange = (row) => {
  const statusText = row.status === 1 ? '上架' : '下架'
  ElMessage.success(`套餐"${row.packageName}"已${statusText}`)
}

// 新增套餐
const handleAddPackage = () => {
  dialogTitle.value = '新增套餐'
  operationType.value = 'add'
  resetPackageForm()
  dialogVisible.value = true
}

// 编辑套餐
const handleEditPackage = (row) => {
  dialogTitle.value = '编辑套餐'
  operationType.value = 'edit'
  resetPackageForm()
  
  // 填充表单数据
  Object.keys(packageForm).forEach(key => {
    if (key in row) {
      packageForm[key] = row[key]
    }
  })
  
  // 假设已选中检查项目ID为1、2、5
  packageForm.selectedItems = [1, 2, 5]
  dialogVisible.value = true
}

// 查看套餐详情
const handleViewDetail = (row) => {
  // 填充详情数据
  Object.assign(detailData, row)
  
  // 假设套餐包含的检查项目
  detailData.items = [
    { itemId: 1, itemName: '血常规', price: 50 },
    { itemId: 2, itemName: '尿常规', price: 50 },
    { itemId: 5, itemName: '心电图', price: 150 }
  ]
  
  detailDialogVisible.value = true
}

// 删除套餐
const handleDeletePackage = (row) => {
  ElMessageBox.confirm(
    `确定要删除套餐"${row.packageName}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(() => {
      // 这里应该调用后端API进行删除
      ElMessage.success(`套餐"${row.packageName}"已删除`)
      fetchData()
    })
    .catch(() => {
      // 取消删除
    })
}

// 重置表单
const resetPackageForm = () => {
  packageForm.packageId = null
  packageForm.packageName = ''
  packageForm.packageType = '1'
  packageForm.price = 0
  packageForm.discountPrice = 0
  packageForm.status = 1
  packageForm.description = ''
  packageForm.selectedItems = []
}

// 提交表单
const submitForm = () => {
  // 这里应该调用后端API保存数据
  const successMsg = operationType.value === 'add' ? '新增成功' : '编辑成功'
  ElMessage.success(successMsg)
  dialogVisible.value = false
  fetchData()
}
</script>

<style scoped>
.package-management-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 