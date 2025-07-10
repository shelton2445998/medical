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
           <el-form-item label="发票抬头" prop="title">
             <el-input v-model="form.title" maxlength="100" placeholder="请输入发票抬头"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="税号" prop="taxNumber">
             <el-input v-model="form.taxNumber" maxlength="50" placeholder="请输入税号"/>
           </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="发票金额" prop="amount">
            <el-input type="number" v-model="form.amount" placeholder="请输入发票金额"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="发票类型" prop="type">
            <el-select v-model="form.type" clearable placeholder="请选择发票类型">
              <el-option value="1" label="个人"/>
              <el-option value="2" label="企业"/>
            </el-select>
          </el-form-item>
        </el-col>
         <el-col :span="12">
           <el-form-item label="发票内容" prop="content">
             <el-input v-model="form.content" maxlength="100" placeholder="请输入发票内容"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" clearable placeholder="请选择状态">
              <el-option value="0" label="未开"/>
              <el-option value="1" label="已开"/>
            </el-select>
          </el-form-item>
        </el-col>
         <el-col :span="12">
           <el-form-item label="PDF文件地址" prop="pdfUrl">
             <el-input v-model="form.pdfUrl" maxlength="255" placeholder="请输入PDF文件地址"/>
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
import {addInvoice, getInvoice, updateInvoice} from "@/api/medical/invoice";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    orderId: null,
    title: null,
    taxNumber: null,
    amount: null,
    type: null,
    content: null,
    status: null,
    pdfUrl: null,
});

// 效验规则
const rules = reactive<FormRules>({
  orderId: [
    {required: true, message: '请输入订单ID', trigger: 'blur'},
  ],
  title: [
    {required: true, message: '请输入发票抬头', trigger: 'blur'},
  ],
  amount: [
    {required: true, message: '请输入发票金额', trigger: 'blur'},
  ],
  type: [
    {required: true, message: '请选择发票类型', trigger: 'blur'},
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getInvoice(id).then(res => {
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
  dialogData.title = '新增电子发票';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑电子发票';
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
       updateInvoice({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addInvoice({...data}).then(() => {
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
