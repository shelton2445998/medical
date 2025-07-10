<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="报告ID" prop="reportId">
            <el-input type="number" v-model="form.reportId" placeholder="请输入报告ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查项明细ID" prop="detailId">
            <el-input type="number" v-model="form.detailId" placeholder="请输入检查项明细ID"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="检查值" prop="value">
             <el-input v-model="form.value" maxlength="100" placeholder="请输入检查值"/>
           </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="是否异常" prop="isAbnormal">
            <el-select v-model="form.isAbnormal" clearable placeholder="请选择是否异常">
              <el-option value="0" label="正常"/>
              <el-option value="1" label="异常"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">确定</el-button>
        <el-button @click="closeDialog">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {addReportItemDetail, getReportItemDetail, updateReportItemDetail} from "@/api/medical/reportItemDetail";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    reportId: null,
    detailId: null,
    value: null,
    isAbnormal: null,
});

// 效验规则
const rules = reactive<FormRules>({
  reportId: [
    {required: true, message: '请输入报告ID', trigger: 'blur'},
  ],
  detailId: [
    {required: true, message: '请输入检查项明细ID', trigger: 'blur'},
  ],
  isAbnormal: [
    {required: true, message: '请选择是否异常', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getReportItemDetail(id).then(res => {
    form.value = Object.assign({}, form.value, res);
  })
}

// 弹框数据
const dialogData = reactive({
  isShow: false,
  id: null
})

// 打开弹框
const openDialog = async (id: string) => {
  dialogData.isShow = true;
  dialogData.title = '新增体检报告检查项明细';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑体检报告检查项明细';
    getDetails(id);
  }
}

// 关闭弹框
const closeDialog = () => {
  dialogData.isShow = false;
  form.value = {};
}
// 提交

const submit = async () => {
   if (!formRef.value) return;
   await formRef.value.validate((valid: any) => {
   if (valid) {
       let data = form.value;
     if (data.id) {
       updateReportItemDetail({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addReportItemDetail({...data}).then(() => {
       ElMessage.success('操作成功');
       closeDialog();
       emits('refresh');
       })
     }
   }
  })
}

defineExpose({
  openDialog,
});
</script>

<style scoped>

</style>
