<template>
  <el-dialog :model-value="dialogData.isShow" :title="dialogData.id?'编辑':'新增'"
             @close="closeDialog" draggable>
    <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
      <el-row :gutter="10">
         <el-col :span="12">
           <el-form-item label="套餐名称" prop="name">
             <el-input v-model="form.name" maxlength="100" placeholder="请输入套餐名称"/>
           </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="套餐类型" prop="type">
            <el-select v-model="form.type" clearable placeholder="请选择套餐类型">
              <el-option value="1" label="基础"/>
              <el-option value="2" label="高级"/>
              <el-option value="3" label="专项"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="价格" prop="price">
            <el-input type="number" v-model="form.price" placeholder="请输入价格"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="折扣价" prop="discountPrice">
            <el-input type="number" v-model="form.discountPrice" placeholder="请输入折扣价"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="12">
          <el-form-item label="描述" prop="description">
            <el-input type="textarea" v-model="form.description" maxlength="500" placeholder="请输入描述"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio :label="false" border>下架</el-radio>
              <el-radio :label="true" border>上架</el-radio>
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
import {addSetmeal, getSetmeal, updateSetmeal} from "@/api/medical/setmeal";

const formRef = ref<FormInstance>()

const emits = defineEmits<{
  (event: 'refresh'): void
}>()

// 表单
let form: any = ref({
    id: null,
    name: null,
    type: null,
    price: null,
    discountPrice: null,
    description: null,
    status:true,
});

// 效验规则
const rules = reactive<FormRules>({
  name: [
    {required: true, message: '请输入套餐名称', trigger: 'blur'},
  ],
  type: [
    {required: true, message: '请选择套餐类型', trigger: 'blur'},
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'blur'},
  ],
})

// 获取详情
const getDetails = (id: string) => {
  getSetmeal(id).then(res => {
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
  dialogData.title = '新增体检套餐';
  if (id) {
    dialogData.id = id;
    dialogData.title = '编辑体检套餐';
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
       updateSetmeal({...data}).then(() => {
         ElMessage.success('操作成功');
         closeDialog();
         emits('refresh');
       })
     } else {
       addSetmeal({...data}).then(() => {
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
