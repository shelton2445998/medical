<template>
  <el-form ref="formRef" :rules="rules" :model="userForm" label-width="90px">
    <el-row class="user-box">
      <el-col>
        <el-form-item prop="head">
          <Upload v-model:value="userForm.head" type="head"
                  :customStyle="{'border-radius':'50%','width':'120px','height':'120px'}"/>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item label="用户名">
          <el-input v-model="userForm.username" disabled placeholder="请输入昵称"/>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname" placeholder="请输入昵称"/>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="userForm.phone" maxlength="11" placeholder="请输入手机号码"/>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item label="电子邮件" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入电子邮件"/>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item label="角色">
          <el-input v-model="userForm.roleName" disabled/>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item label="创建时间">
          <el-input v-model="userForm.createTime" disabled/>
        </el-form-item>
      </el-col>
      <el-col :sm="24" :md="12" :lg="8" :xl="6">
        <el-form-item>
          <el-button type="primary" @click="submitUserInfo">更新个人信息</el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup lang="ts">
import {useUserStoreHook} from "@/store/modules/user";
import type {FormInstance, FormRules} from "element-plus";
import {ElMessage} from "element-plus";
import {validateEmail, validatePhoneNumber} from "@/utils/validate";
import {system_status} from "@/utils/status";
import {getProfile, updateProfile} from "@/api/user";
import Upload from "@/components/Upload/index.vue";

const {userinfo} = storeToRefs(useUserStoreHook());
const formRef = ref<FormInstance>()


interface UserInfoType {
  head: NullType<string>
  nickname: string
  username: string
  phone: string
  email: string
  gender: NullType<number>
  roleName: string
  createTime: string
}

// 个人信息
const userForm = ref<UserInfoType>({
  head: '',
  nickname: '',
  phone: '',
  username: '',
  email: '',
  gender: null,
  roleName: '',
  createTime: '',
})

// 效验规则
const rules = reactive<FormRules>({
  nickname: [
    {required: true, message: '请输入昵称', trigger: 'blur'},
  ],
  phone: [
    {validator: validatePhoneNumber, trigger: 'blur'},
  ],
  email: [
    {validator: validateEmail, trigger: 'blur'},
  ],
})

// 获取用户信息
const getUserInfo = () => {
  getProfile().then(res => {
    userForm.value = {...userForm.value, ...res}
  })
}

// 更新个人信息
const submitUserInfo = async () => {
  if (!formRef.value) return;
  await formRef.value.validate((valid) => {
    if (valid) {
      updateProfile({
        id: userinfo.value.id,
        nickname: userForm.value.nickname,
        phone: userForm.value.phone,
        email: userForm.value.email,
        gender: userForm.value.gender,
        head: userForm.value.head,
      }).then(() => {
        ElMessage.success('修改成功');
        useUserStoreHook().getLoginUserInfo();
      })
    }
  })
}
// 重置表单
const resetForm = () => {
  if (formRef.value) formRef.value.resetFields();
}
defineExpose({
  resetForm
})

onBeforeMount(() => {
  getUserInfo();
})
</script>

<style scoped>

</style>
