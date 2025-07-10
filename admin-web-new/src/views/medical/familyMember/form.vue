<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="关联用户ID" prop="userId">
            <el-input type="number" v-model="form.userId" placeholder="请输入关联用户ID"/>
          </el-form-item>
        </el-col>
         <el-col :span="12">
           <el-form-item label="成员姓名" prop="name">
             <el-input v-model="form.name" maxlength="32" placeholder="请输入成员姓名"/>
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
           <el-form-item label="身份证号" prop="idCard">
             <el-input v-model="form.idCard" maxlength="18" placeholder="请输入身份证号"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="手机号" prop="mobile">
             <el-input v-model="form.mobile" maxlength="20" placeholder="请输入手机号"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="与用户关系" prop="relation">
             <el-input v-model="form.relation" maxlength="20" placeholder="请输入与用户关系"/>
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
import {addFamilyMember, getFamilyMember, updateFamilyMember} from "@/api/medical/familyMember";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    userId: null,
    name: null,
    gender:true,
    idCard: null,
    mobile: null,
    relation: null,
});

// 效验规则
const rules = reactive<FormRules>({
  userId: [
    {required: true, message: '请输入关联用户ID', trigger: 'blur'},
  ],
  name: [
    {required: true, message: '请输入成员姓名', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getFamilyMember(id).then(res => {
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
  dialogData.title = '新增家庭成员';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑家庭成员';
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
       updateFamilyMember({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addFamilyMember({...data}).then(() => {
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
