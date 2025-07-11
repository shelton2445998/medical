<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="检查项ID" prop="itemId">
            <el-input type="number" v-model="form.itemId" placeholder="请输入检查项ID"/>
          </el-form-item>
        </el-col>
         <el-col :span="12">
           <el-form-item label="明细名称" prop="name">
             <el-input v-model="form.name" maxlength="100" placeholder="请输入明细名称"/>
           </el-form-item>
         </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="正常值范围" prop="normalValue">
             <el-input v-model="form.normalValue" maxlength="100" placeholder="请输入正常值范围"/>
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="单位" prop="unit">
             <el-input v-model="form.unit" maxlength="20" placeholder="请输入单位"/>
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
import {addCheckitemDetail, getCheckitemDetail, updateCheckitemDetail} from "@/api/medical/checkitemDetail";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    itemId: null,
    name: null,
    normalValue: null,
    unit: null,
});

// 效验规则
const rules = reactive<FormRules>({
  itemId: [
    {required: true, message: '请输入检查项ID', trigger: 'blur'},
  ],
  name: [
    {required: true, message: '请输入明细名称', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getCheckitemDetail(id).then(res => {
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
  dialogData.title = '新增检查项明细';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑检查项明细';
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
       updateCheckitemDetail({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addCheckitemDetail({...data}).then(() => {
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
