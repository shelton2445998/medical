<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="医生姓名" prop="name">
             <el-input v-model="form.name" maxlength="32" placeholder="请输入医生姓名"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="密码" prop="password">
             <el-input v-model="form.password" type="password" maxlength="64" placeholder="请输入密码" :show-password="true"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="性别">
            <el-radio-group v-model="form.gender">
              <el-radio :label="false" border>女</el-radio>
              <el-radio :label="true" border>男</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="mobile">
            <el-input v-model="form.mobile" maxlength="20" placeholder="请输入手机号"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="邮箱" prop="email">
             <el-input v-model="form.email" maxlength="100" placeholder="请输入邮箱"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
          <el-form-item label="所属医院ID" prop="hospitalId">
            <el-input type="number" v-model="form.hospitalId" placeholder="请输入所属医院ID"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="科室ID" prop="departmentId">
            <el-input type="number" v-model="form.departmentId" placeholder="请输入科室ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职称" prop="title">
             <el-input v-model="form.title" maxlength="50" placeholder="请输入职称"/>
           </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="简介" prop="introduction">
            <el-input type="textarea" v-model="form.introduction" maxlength="500" placeholder="请输入简介"/>
          </el-form-item>
        </el-col>
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
import {addDoctor, getDoctor, updateDoctor} from "@/api/medical/doctor";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    name: null,
    password: null,
    gender:true,
    mobile: null,
    email: null,
    hospitalId: null,
    departmentId: null,
    title: null,
    introduction: null,
    status:true,
});

// 效验规则
const rules = reactive<FormRules>({
  name: [
    {required: true, message: '请输入医生姓名', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getDoctor(id).then(res => {
    form.value = Object.assign({}, form.value, res);
    // 查看时不回显密码
    form.value.password = '';
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
  dialogData.title = '新增医生';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑医生';
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
       updateDoctor({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addDoctor({...data}).then(() => {
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
