<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="接收用户ID" prop="userId">
            <el-input type="number" v-model="form.userId" placeholder="请输入接收用户ID"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接收医生ID" prop="doctorId">
            <el-input type="number" v-model="form.doctorId" placeholder="请输入接收医生ID"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="通知标题" prop="title">
             <el-input v-model="form.title" maxlength="100" placeholder="请输入通知标题"/>
           </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="通知内容" prop="content">
            <el-input type="textarea" v-model="form.content" maxlength="" placeholder="请输入通知内容"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="通知类型" prop="type">
            <el-select v-model="form.type" clearable placeholder="请选择通知类型">
              <el-option value="1" label="预约提醒"/>
              <el-option value="2" label="报告通知"/>
              <el-option value="3" label="系统公告"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否已读">
            <el-radio-group v-model="form.isRead">
              <el-radio :label="false" border>未读</el-radio>
              <el-radio :label="true" border>已读</el-radio>
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
import {addNotification, getNotification, updateNotification} from "@/api/medical/notification";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    userId: null,
    doctorId: null,
    title: null,
    content: null,
    type: null,
    isRead:true,
});

// 效验规则
const rules = reactive<FormRules>({
  title: [
    {required: true, message: '请输入通知标题', trigger: 'blur'},
  ],
  type: [
    {required: true, message: '请选择通知类型', trigger: 'blur'},
  ],
  isRead: [
    {required: true, message: '请选择是否已读', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getNotification(id).then(res => {
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
  dialogData.title = '新增系统通知';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑系统通知';
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
       updateNotification({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addNotification({...data}).then(() => {
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
