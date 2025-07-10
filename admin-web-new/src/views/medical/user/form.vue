<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="账号" prop="username">
             <el-input v-model="form.username" maxlength="20" placeholder="请输入账号"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="昵称" prop="nickname">
             <el-input v-model="form.nickname" maxlength="20" placeholder="请输入昵称"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="密码" prop="password">
             <el-input v-model="form.password" maxlength="64" placeholder="请输入密码"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="盐值" prop="salt">
             <el-input v-model="form.salt" maxlength="32" placeholder="请输入盐值"/>
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
           <el-form-item label="微信openid" prop="openid">
             <el-input v-model="form.openid" maxlength="200" placeholder="请输入微信openid"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="手机号码" prop="phone">
             <el-input v-model="form.phone" maxlength="11" placeholder="请输入手机号码"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="头像" prop="head">
             <el-input v-model="form.head" maxlength="200" placeholder="请输入头像"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="个人简介" prop="introduction">
             <el-input v-model="form.introduction" maxlength="200" placeholder="请输入个人简介"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="用户角色ID" prop="userRoleId">
            <el-input type="number" v-model="form.userRoleId" placeholder="请输入用户角色ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio :label="true" border>正常</el-radio>
              <el-radio :label="false" border>禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="注册时间">
             <el-date-picker v-model="form.registerTime" type="datetime" placeholder="请选择注册时间"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="注册IP" prop="registerIp">
             <el-input v-model="form.registerIp" maxlength="20" placeholder="请输入注册IP"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="注册IP区域" prop="registerIpArea">
             <el-input v-model="form.registerIpArea" maxlength="100" placeholder="请输入注册IP区域"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="最后登录时间">
             <el-date-picker v-model="form.lastLoginTime" type="datetime" placeholder="请选择最后登录时间"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="最后登录IP" prop="lastLoginIp">
             <el-input v-model="form.lastLoginIp" maxlength="20" placeholder="请输入最后登录IP"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="最后一次登录IP区域" prop="lastLoginIpArea">
             <el-input v-model="form.lastLoginIpArea" maxlength="100" placeholder="请输入最后一次登录IP区域"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="备注" prop="remark">
             <el-input v-model="form.remark" maxlength="200" placeholder="请输入备注"/>
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
import {addUser, getUser, updateUser} from "@/api/medical/user";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    username: null,
    nickname: null,
    password: null,
    salt: null,
    gender:true,
    idCard: null,
    openid: null,
    phone: null,
    head: null,
    introduction: null,
    userRoleId: null,
    status:true,
    registerTime: null,
    registerIp: null,
    registerIpArea: null,
    lastLoginTime: null,
    lastLoginIp: null,
    lastLoginIpArea: null,
    remark: null,
});

// 效验规则
const rules = reactive<FormRules>({
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getUser(id).then(res => {
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
  dialogData.title = '新增体检用户信息';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑体检用户信息';
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
       updateUser({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addUser({...data}).then(() => {
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
