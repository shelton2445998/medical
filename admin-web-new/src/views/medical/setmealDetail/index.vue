<template>
  <div class="app-container">
    <el-card shadow="never">
      <template #header>
        <el-form :inline="true" :model="queryParams" class="app-form-search">
          <el-form-item>
            <el-input v-model="queryParams.name" clearable placeholder="套餐名称" @keyup.enter="getList"/>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryParams.packageType" clearable placeholder="套餐类型">
              <el-option label="基础" :value="1"/>
              <el-option label="高级" :value="2"/>
              <el-option label="专项" :value="3"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryParams.status" clearable placeholder="状态">
              <el-option label="上架" :value="1"/>
              <el-option label="下架" :value="0"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getList">
              <el-icon>
                <Search/>
              </el-icon>
              搜索
            </el-button>
            <el-button @click="resetQuery">
              <el-icon>
                <Refresh/>
              </el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </template>

      <!-- 操作按钮 -->
      <el-button v-auth="'setmeal:detail:add'" type="primary" @click="handleAdd">
        <el-icon>
          <Plus/>
        </el-icon>
        新增
      </el-button>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column type="index" label="#" width="50" align="center"/>
        <el-table-column prop="name" label="套餐名称" min-width="150" show-overflow-tooltip/>
        <el-table-column prop="price" label="原价" width="100">
          <template #default="scope">
            {{ scope.row.price ? '¥' + scope.row.price : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="discountPrice" label="优惠价" width="100">
          <template #default="scope">
            {{ scope.row.discountPrice ? '¥' + scope.row.discountPrice : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="hospitalId" label="所属医院" width="120">
          <template #default="scope">
            {{ getHospitalName(scope.row.hospitalId) || scope.row.hospitalId || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="departmentId" label="所属科室" width="120">
          <template #default="scope">
            {{ getDepartmentName(scope.row.departmentId) || scope.row.departmentId || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="packageType" label="套餐类型" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.packageType === 1" type="success">基础</el-tag>
            <el-tag v-else-if="scope.row.packageType === 2" type="warning">高级</el-tag>
            <el-tag v-else-if="scope.row.packageType === 3" type="danger">专项</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">上架</el-tag>
            <el-tag v-else-if="scope.row.status === 0" type="info">下架</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" align="center"/>
        <el-table-column prop="createTime" label="创建时间" width="180"/>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button 
              type="primary" 
              link 
              @click="handleEdit(scope.row)" 
              v-auth="'setmeal:detail:update'"
            >
              编辑
            </el-button>
            <el-button 
              type="success" 
              link 
              @click="handleViewDetail(scope.row)" 
            >
              详情
            </el-button>
            <el-popconfirm 
              title="确认删除该记录吗？" 
              @confirm="handleDelete(scope.row)" 
              v-auth="'setmeal:detail:delete'"
            >
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="app-pagination">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 表单弹框 -->
    <Form ref="formRef" @refresh="getList"/>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from "element-plus";
import { ref, reactive, onMounted } from "vue";
import Form from "./form.vue";
import { deleteSetmealDetail, getSetmealDetailPage } from "@/api/medical/setmealDetail";
import { Search, Plus, Refresh } from "@element-plus/icons-vue";
import { getHospitalPage } from "@/api/medical/hospital";
import { getDepartmentPage } from "@/api/medical/department";

const formRef = ref();
const loading = ref(false);
const tableData = ref([]);
const total = ref(0);

// 医院和科室选项
const hospitalOptions = ref([]);
const departmentOptions = ref([]);

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  packageType: '',
  status: '',
});

// 获取医院名称
const getHospitalName = (id) => {
  if (!id) return '-'
  const hospital = hospitalOptions.value.find((item) => item.id == id)
  return hospital ? hospital.name : id
}

// 获取科室名称
const getDepartmentName = (id) => {
  if (!id) return '-'
  const department = departmentOptions.value.find((item) => item.id == id)
  return department ? department.name : id
}

// 查看详情
const handleViewDetail = (row) => {
  formRef.value.openDetailDialog(row)
}

// 加载医院和科室数据
const loadOptions = async () => {
  try {
    // 加载医院列表
    const hospitalRes = await getHospitalPage({})
    console.log('医院列表数据:', hospitalRes)
    if (hospitalRes.data && Array.isArray(hospitalRes.data)) {
      hospitalOptions.value = hospitalRes.data
    } else if (hospitalRes.data && hospitalRes.data.list && Array.isArray(hospitalRes.data.list)) {
      hospitalOptions.value = hospitalRes.data.list
    } else if (hospitalRes.data && hospitalRes.data.records && Array.isArray(hospitalRes.data.records)) {
      hospitalOptions.value = hospitalRes.data.records
    } else if (hospitalRes.list && Array.isArray(hospitalRes.list)) {
      hospitalOptions.value = hospitalRes.list
    } else {
      hospitalOptions.value = []
    }
    
    // 加载科室列表
    const departmentRes = await getDepartmentPage({})
    console.log('科室列表数据:', departmentRes)
    if (departmentRes.data && Array.isArray(departmentRes.data)) {
      departmentOptions.value = departmentRes.data
    } else if (departmentRes.data && departmentRes.data.list && Array.isArray(departmentRes.data.list)) {
      departmentOptions.value = departmentRes.data.list
    } else if (departmentRes.data && departmentRes.data.records && Array.isArray(departmentRes.data.records)) {
      departmentOptions.value = departmentRes.data.records
    } else if (departmentRes.list && Array.isArray(departmentRes.list)) {
      departmentOptions.value = departmentRes.list
    } else {
      departmentOptions.value = []
    }
  } catch (error) {
    console.error('加载选项数据失败', error)
  }
}

// 获取列表
const getList = () => {
  loading.value = true;
  getSetmealDetailPage(queryParams).then(res => {
    console.log('套餐详情列表数据:', res)
    if (res.data && Array.isArray(res.data)) {
      tableData.value = res.data
      total.value = res.data.length
    } else if (res.data && res.data.list && Array.isArray(res.data.list)) {
      tableData.value = res.data.list
      total.value = res.data.total || res.data.list.length
    } else if (res.data && res.data.records && Array.isArray(res.data.records)) {
      tableData.value = res.data.records
      total.value = res.data.total || res.data.records.length
    } else if (res.list && Array.isArray(res.list)) {
      tableData.value = res.list
      total.value = res.total || res.list.length
    } else {
      tableData.value = []
      total.value = 0
    }
  }).finally(() => {
    loading.value = false;
  })
}

// 重置查询
const resetQuery = () => {
  queryParams.name = '';
  queryParams.packageType = '';
  queryParams.status = '';
  getList();
}

// 分页
const handleSizeChange = (val) => {
  queryParams.pageSize = val;
  getList();
}

const handleCurrentChange = (val) => {
  queryParams.pageNum = val;
  getList();
}

// 新增
const handleAdd = () => {
  formRef.value.openDialog();
}

// 编辑
const handleEdit = (row) => {
  formRef.value.openDialog(row.id);
}

// 删除
const handleDelete = (row) => {
  deleteSetmealDetail(row.id).then(() => {
    ElMessage.success('删除成功');
    getList();
  })
}

onMounted(() => {
  loadOptions()
  getList()
})
</script> 