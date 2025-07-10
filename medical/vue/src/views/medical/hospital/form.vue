<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="医院名称" prop="name">
             <el-input v-model="form.name" maxlength="100" placeholder="请输入医院名称"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="医院电话" prop="phone">
             <el-input v-model="form.phone" maxlength="20" placeholder="请输入医院电话"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="医院地址" prop="address">
             <el-input v-model="form.address" maxlength="200" placeholder="请输入医院地址"/>
           </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="医院简介" prop="description">
            <el-input type="textarea" v-model="form.description" maxlength="500" placeholder="请输入医院简介"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio :label="false" border>禁用</el-radio>
              <el-radio :label="true" border>启用</el-radio>
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
import {addHospital, getHospital, updateHospital} from "@/api/medical/hospital";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    name: null,
    phone: null,
    address: null,
    description: null,
    status:true,
});

// 效验规则
const rules = reactive<FormRules>({
  name: [
    {required: true, message: '请输入医院名称', trigger: 'blur'},
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getHospital(id).then(res => {
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
  dialogData.title = '新增医院';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑医院';
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
       updateHospital({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addHospital({...data}).then(() => {
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
