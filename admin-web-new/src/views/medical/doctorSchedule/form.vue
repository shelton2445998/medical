<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="医生ID" prop="doctorId">
            <el-input type="number" v-model="form.doctorId" placeholder="请输入医生ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作日期">
            <el-date-picker v-model="form.workDate" type="date" placeholder="请选择工作日期"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="时间段" prop="timeSlot">
             <el-input v-model="form.timeSlot" maxlength="20" placeholder="请输入时间段"/>
           </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="最大可预约人数" prop="maxNumber">
            <el-input type="number" v-model="form.maxNumber" placeholder="请输入最大可预约人数"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="已预约人数" prop="reservedNumber">
            <el-input type="number" v-model="form.reservedNumber" placeholder="请输入已预约人数"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio :label="false" border>停诊</el-radio>
              <el-radio :label="true" border>正常</el-radio>
            </el-radio-group>
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
import {addDoctorSchedule, getDoctorSchedule, updateDoctorSchedule} from "@/api/medical/doctorSchedule";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    doctorId: null,
    workDate: null,
    timeSlot: null,
    maxNumber: null,
    reservedNumber: null,
    status:true,
});

// 效验规则
const rules = reactive<FormRules>({
  doctorId: [
    {required: true, message: '请输入医生ID', trigger: 'blur'},
  ],
  workDate: [
    {required: true, message: '请选择工作日期', trigger: 'blur'},
  ],
  timeSlot: [
    {required: true, message: '请输入时间段', trigger: 'blur'},
  ],
  maxNumber: [
    {required: true, message: '请输入最大可预约人数', trigger: 'blur'},
  ],
  reservedNumber: [
    {required: true, message: '请输入已预约人数', trigger: 'blur'},
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getDoctorSchedule(id).then(res => {
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
  dialogData.title = '新增医生排班';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑医生排班';
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
       updateDoctorSchedule({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addDoctorSchedule({...data}).then(() => {
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
