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
          <el-form-item label="结论内容" prop="content">
            <el-input type="textarea" v-model="form.content" maxlength="" placeholder="请输入结论内容"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="医生ID" prop="doctorId">
            <el-input type="number" v-model="form.doctorId" placeholder="请输入医生ID"/>
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
import {addOverallResult, getOverallResult, updateOverallResult} from "@/api/medical/overallResult";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    orderId: null,
    content: null,
    doctorId: null,
});

// 效验规则
const rules = reactive<FormRules>({
  orderId: [
    {required: true, message: '请输入订单ID', trigger: 'blur'},
  ],
  doctorId: [
    {required: true, message: '请输入医生ID', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getOverallResult(id).then(res => {
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
  dialogData.title = '新增总检结论';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑总检结论';
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
       updateOverallResult({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addOverallResult({...data}).then(() => {
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
