<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.title" @close="closeDialog" draggable width="800px">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="套餐ID" prop="setmealId">
                <el-select v-model="form.setmealId" placeholder="请选择套餐" filterable>
                  <el-option
                    v-for="item in setmealOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检查项ID" prop="itemId">
                <el-select v-model="form.itemId" placeholder="请选择检查项" filterable>
                  <el-option
                    v-for="item in checkItemOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="套餐名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入套餐名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属医院" prop="hospitalId">
                <el-select v-model="form.hospitalId" placeholder="请选择医院">
                  <el-option
                    v-for="item in hospitalOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="套餐原价" prop="price">
                <el-input-number v-model="form.price" :precision="2" :step="0.01" :min="0" style="width: 100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="优惠价格" prop="discountPrice">
                <el-input-number v-model="form.discountPrice" :precision="2" :step="0.01" :min="0" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="所属科室" prop="departmentId">
                <el-select v-model="form.departmentId" placeholder="请选择科室">
                  <el-option
                    v-for="item in departmentOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="套餐类型" prop="packageType">
                <el-select v-model="form.packageType" placeholder="请选择套餐类型">
                  <el-option label="基础" :value="1"/>
                  <el-option label="高级" :value="2"/>
                  <el-option label="专项" :value="3"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-select v-model="form.status" placeholder="请选择状态">
                  <el-option label="上架" :value="1"/>
                  <el-option label="下架" :value="0"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序权重" prop="sortOrder">
                <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="详细描述" name="description">
          <el-form-item label="套餐详细介绍" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入套餐详细介绍"/>
          </el-form-item>
          <el-form-item label="检查项目列表" prop="checkItems">
            <el-input v-model="form.checkItems" type="textarea" :rows="4" placeholder="请输入检查项目列表"/>
          </el-form-item>
          <el-form-item label="适用人群描述" prop="suitableCrowd">
            <el-input v-model="form.suitableCrowd" type="textarea" :rows="4" placeholder="请输入适用人群描述"/>
          </el-form-item>
          <el-form-item label="预约须知" prop="appointmentNotice">
            <el-input v-model="form.appointmentNotice" type="textarea" :rows="4" placeholder="请输入预约须知"/>
          </el-form-item>
          <el-form-item label="用户评价" prop="userReviews">
            <el-input v-model="form.userReviews" type="textarea" :rows="4" placeholder="请输入用户评价"/>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">确定</el-button>
        <el-button @click="closeDialog">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 详情查看对话框 -->
  <el-dialog v-model="detailDialogVisible" title="套餐详情" width="800px">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="套餐名称">{{ detailData.name }}</el-descriptions-item>
      <el-descriptions-item label="套餐ID">{{ detailData.setmealId }}</el-descriptions-item>
      <el-descriptions-item label="套餐原价">¥{{ detailData.price || 0 }}</el-descriptions-item>
      <el-descriptions-item label="优惠价格">¥{{ detailData.discountPrice || 0 }}</el-descriptions-item>
      <el-descriptions-item label="所属医院">{{ detailData.hospitalId }}</el-descriptions-item>
      <el-descriptions-item label="所属科室">{{ detailData.departmentId }}</el-descriptions-item>
      <el-descriptions-item label="套餐类型">
        <el-tag v-if="detailData.packageType === 1" type="success">基础</el-tag>
        <el-tag v-else-if="detailData.packageType === 2" type="warning">高级</el-tag>
        <el-tag v-else-if="detailData.packageType === 3" type="danger">专项</el-tag>
        <span v-else>-</span>
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag v-if="detailData.status === 1" type="success">上架</el-tag>
        <el-tag v-else-if="detailData.status === 0" type="info">下架</el-tag>
        <span v-else>-</span>
      </el-descriptions-item>
      <el-descriptions-item label="排序权重">{{ detailData.sortOrder }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
    </el-descriptions>
    
    <el-divider />
    
    <el-tabs>
      <el-tab-pane label="套餐详细介绍">
        <div class="detail-content">{{ detailData.description || '暂无数据' }}</div>
      </el-tab-pane>
      <el-tab-pane label="检查项目列表">
        <div class="detail-content">{{ detailData.checkItems || '暂无数据' }}</div>
      </el-tab-pane>
      <el-tab-pane label="适用人群">
        <div class="detail-content">{{ detailData.suitableCrowd || '暂无数据' }}</div>
      </el-tab-pane>
      <el-tab-pane label="预约须知">
        <div class="detail-content">{{ detailData.appointmentNotice || '暂无数据' }}</div>
      </el-tab-pane>
      <el-tab-pane label="用户评价">
        <div class="detail-content">{{ detailData.userReviews || '暂无数据' }}</div>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup lang="ts">
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {addSetmealDetail, getSetmealDetail, updateSetmealDetail, SetmealDetail} from "@/api/medical/setmealDetail";
import {onMounted, ref, reactive} from "vue";
import {getSetmealPage} from "@/api/medical/setmeal";
import {getCheckitemPage} from "@/api/medical/checkitem";
import {getHospitalPage} from "@/api/medical/hospital";
import {getDepartmentPage} from "@/api/medical/department";

const formRef = ref<FormInstance>()
const activeTab = ref('basic')

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
const form = ref<SetmealDetail>({
  id: null,
  setmealId: 0,
  name: '',
  price: 0,
  discountPrice: 0,
  description: '',
  checkItems: '',
  suitableCrowd: '',
  appointmentNotice: '',
  userReviews: '',
  hospitalId: 1,
  departmentId: undefined,
  packageType: 1,
  status: 1,
  sortOrder: 0,
  itemId: 0,
});

// 详情对话框
const detailDialogVisible = ref(false)
const detailData = ref<any>({})

// 效验规则
const rules = reactive<FormRules>({
  setmealId: [
    {required: true, message: '请选择套餐', trigger: 'change'},
  ],
  itemId: [
    {required: true, message: '请选择检查项', trigger: 'change'},
  ],
  name: [
    {required: true, message: '请输入套餐名称', trigger: 'blur'},
    {max: 100, message: '套餐名称不能超过100个字符', trigger: 'blur'},
  ],
  hospitalId: [
    {required: true, message: '请选择所属医院', trigger: 'change'},
  ],
})

// 下拉选项
const setmealOptions = ref([])
const checkItemOptions = ref([])
const hospitalOptions = ref([])
const departmentOptions = ref([])

// 加载下拉选项数据
const loadOptions = async () => {
  try {
    // 加载套餐列表
    const setmealRes = await getSetmealPage({})
    console.log('套餐列表数据:', setmealRes)
    if (setmealRes.data && Array.isArray(setmealRes.data)) {
      setmealOptions.value = setmealRes.data
    } else if (setmealRes.data && setmealRes.data.list && Array.isArray(setmealRes.data.list)) {
      setmealOptions.value = setmealRes.data.list
    } else if (setmealRes.data && setmealRes.data.records && Array.isArray(setmealRes.data.records)) {
      setmealOptions.value = setmealRes.data.records
    } else if (setmealRes.list && Array.isArray(setmealRes.list)) {
      setmealOptions.value = setmealRes.list
    } else {
      setmealOptions.value = []
    }
    
    // 加载检查项列表
    const checkItemRes = await getCheckitemPage({})
    console.log('检查项列表数据:', checkItemRes)
    if (checkItemRes.data && Array.isArray(checkItemRes.data)) {
      checkItemOptions.value = checkItemRes.data
    } else if (checkItemRes.data && checkItemRes.data.list && Array.isArray(checkItemRes.data.list)) {
      checkItemOptions.value = checkItemRes.data.list
    } else if (checkItemRes.data && checkItemRes.data.records && Array.isArray(checkItemRes.data.records)) {
      checkItemOptions.value = checkItemRes.data.records
    } else if (checkItemRes.list && Array.isArray(checkItemRes.list)) {
      checkItemOptions.value = checkItemRes.list
    } else {
      checkItemOptions.value = []
    }
    
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

onMounted(() => {
  loadOptions()
})

// 获取详情
const getDetails = (id: string) => {
  getSetmealDetail(id).then(res => {
    form.value = Object.assign({}, form.value, res.data || res);
  })
}

// 弹框数据
const dialogData = reactive({
  isShow: false,
  title: '',
  id: null
})

// 打开弹框
const openDialog = async (id: string) => {
  dialogData.isShow = true;
  dialogData.title = id ? '编辑体检套餐明细' : '新增体检套餐明细';
  if (id) {
    dialogData.id = id;
    getDetails(id);
  }
}

// 打开详情对话框
const openDetailDialog = async (data: any) => {
  detailData.value = data
  detailDialogVisible.value = true
}

// 关闭弹框
const closeDialog = () => {
  dialogData.isShow = false;
  form.value = {
    id: null,
    setmealId: 0,
    name: '',
    price: 0,
    discountPrice: 0,
    description: '',
    checkItems: '',
    suitableCrowd: '',
    appointmentNotice: '',
    userReviews: '',
    hospitalId: 1,
    departmentId: undefined,
    packageType: 1,
    status: 1,
    sortOrder: 0,
    itemId: 0,
  };
  activeTab.value = 'basic';
}

// 提交
const submit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogData.id) {
          await updateSetmealDetail(form.value);
          ElMessage.success('修改成功');
        } else {
          await addSetmealDetail(form.value);
          ElMessage.success('添加成功');
        }
        closeDialog();
        emits('refresh');
      } catch (error) {
        console.error(error);
      }
    }
  });
}

defineExpose({
  openDialog,
  openDetailDialog
})
</script>
<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.detail-content {
  padding: 10px;
  min-height: 100px;
  background-color: #f8f8f8;
  border-radius: 4px;
  white-space: pre-wrap;
}
</style>
