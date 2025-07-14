<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="订单ID" prop="orderId">
            <el-input type="number" v-model="form.orderId" placeholder="请输入订单ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户ID" prop="userId">
            <el-input type="number" v-model="form.userId" placeholder="请输入用户ID"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="检查项ID列表" prop="checkitemIds">
            <el-input type="textarea" v-model="form.checkitemIds" maxlength="1000" placeholder="请输入检查项ID列表"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告项ID列表" prop="reportItemIds">
            <el-input type="textarea" v-model="form.reportItemIds" maxlength="1000" placeholder="请输入报告项ID列表"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" clearable placeholder="请选择状态">
              <el-option value="0" label="未完成"/>
              <el-option value="1" label="已完成"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总结论" prop="conclusion">
            <el-input type="textarea" v-model="form.conclusion" maxlength="500" placeholder="请输入总结论"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="医生ID" prop="doctorId">
            <el-input type="number" v-model="form.doctorId" placeholder="请输入医生ID"/>
          </el-form-item>
        </el-col>
         <el-col :span="12">
           <el-form-item label="报告日期">
             <el-date-picker v-model="form.reportDate" type="datetime" placeholder="请选择报告日期"/>
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
import {addReport, getReport, updateReport} from "@/api/medical/report";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    orderId: null,
    userId: null,
    checkitemIds: null,
    reportItemIds: null,
    status: null,
    conclusion: null,
    doctorId: null,
    reportDate: null,
});

// 效验规则
const rules = reactive<FormRules>({
  orderId: [
    {required: true, message: '请输入订单ID', trigger: 'blur'},
  ],
  userId: [
    {required: true, message: '请输入用户ID', trigger: 'blur'},
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getReport(id).then(res => {
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
  dialogData.title = '新增体检报告总';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑体检报告总';
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
       updateReport({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addReport({...data}).then(() => {
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
