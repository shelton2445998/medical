<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="用户ID" prop="userId">
            <el-input type="number" v-model="form.userId" placeholder="请输入用户ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="套餐ID" prop="setmealId">
            <el-input type="number" v-model="form.setmealId" placeholder="请输入套餐ID"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="医院ID" prop="hospitalId">
            <el-input type="number" v-model="form.hospitalId" placeholder="请输入医院ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="医生ID" prop="doctorId">
            <el-input type="number" v-model="form.doctorId" placeholder="请输入医生ID"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="预约日期">
            <el-date-picker v-model="form.appointmentDate" type="date" placeholder="请选择预约日期"/>
          </el-form-item>
        </el-col>
         <el-col :span="12">
           <el-form-item label="时间段" prop="timeSlot">
             <el-input v-model="form.timeSlot" maxlength="20" placeholder="请输入时间段"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" clearable placeholder="请选择状态">
              <el-option value="0" label="已取消"/>
              <el-option value="1" label="待支付"/>
              <el-option value="2" label="已支付"/>
              <el-option value="3" label="已完成"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="订单金额" prop="amount">
            <el-input type="number" v-model="form.amount" placeholder="请输入订单金额"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="支付时间">
             <el-date-picker v-model="form.payTime" type="datetime" placeholder="请选择支付时间"/>
           </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="支付方式" prop="payType">
            <el-select v-model="form.payType" clearable placeholder="请选择支付方式">
              <el-option value="1" label="支付宝"/>
              <el-option value="2" label="微信"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="支付交易号" prop="transactionId">
             <el-input v-model="form.transactionId" maxlength="100" placeholder="请输入支付交易号"/>
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
import {addOrders, getOrders, updateOrders} from "@/api/medical/orders";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    userId: null,
    setmealId: null,
    hospitalId: null,
    doctorId: null,
    appointmentDate: null,
    timeSlot: null,
    status: null,
    amount: null,
    payTime: null,
    payType: null,
    transactionId: null,
});

// 效验规则
const rules = reactive<FormRules>({
  userId: [
    {required: true, message: '请输入用户ID', trigger: 'blur'},
  ],
  setmealId: [
    {required: true, message: '请输入套餐ID', trigger: 'blur'},
  ],
  hospitalId: [
    {required: true, message: '请输入医院ID', trigger: 'blur'},
  ],
  doctorId: [
    {required: true, message: '请输入医生ID', trigger: 'blur'},
  ],
  appointmentDate: [
    {required: true, message: '请选择预约日期', trigger: 'blur'},
  ],
  timeSlot: [
    {required: true, message: '请输入时间段', trigger: 'blur'},
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getOrders(id).then(res => {
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
  dialogData.title = '新增体检预约订单';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑体检预约订单';
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
       updateOrders({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addOrders({...data}).then(() => {
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
