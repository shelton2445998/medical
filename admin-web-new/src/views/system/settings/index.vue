<template>
  <el-card shadow="never" class="card-box">
    <el-form :model="formData" label-width="120px" ref="formRef">
      <el-form-item label="系统名称" prop="sysName">
        <el-input v-model="formData.sysName" placeholder="请输入系统名称" />
      </el-form-item>
      <el-form-item label="系统标题" prop="sysTitle">
        <el-input v-model="formData.sysTitle" placeholder="请输入系统标题" />
      </el-form-item>
      <el-form-item label="系统Logo" prop="sysLogo">
        <Upload v-model:value="formData.sysLogo" type="image" />
      </el-form-item>
      <el-form-item label="系统版权" prop="sysCopyright">
        <el-input v-model="formData.sysCopyright" placeholder="请输入系统版权" />
      </el-form-item>
      <el-form-item label="备案号" prop="sysBeian">
        <el-input v-model="formData.sysBeian" placeholder="请输入备案号" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveSettings">保存设置</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { http } from '@/utils/http';
import Upload from '@/components/Upload/index.vue';

const formRef = ref();
const formData = ref({
  sysName: '',
  sysTitle: '',
  sysLogo: '',
  sysCopyright: '',
  sysBeian: ''
});

// 保存设置
const saveSettings = async () => {
  try {
    const valid = await formRef.value.validate();
    if (valid) {
      // 创建一个配置数组，适应后端接口
      const configList = [
        { configKey: 'system.name', configValue: formData.value.sysName },
        { configKey: 'system.title', configValue: formData.value.sysTitle },
        { configKey: 'system.logo', configValue: formData.value.sysLogo },
        { configKey: 'system.copyright', configValue: formData.value.sysCopyright },
        { configKey: 'system.beian', configValue: formData.value.sysBeian }
      ];
      
      await http.post('/admin/config/update', configList);
      ElMessage.success('保存成功');
    }
  } catch (error) {
    console.error('保存失败:', error);
  }
};

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields();
  getSystemSettings();
};

// 获取系统设置
const getSystemSettings = async () => {
  try {
    // 使用可能存在的接口获取所有系统配置
    const result = await http.get('/admin/config/list');
    if (result && Array.isArray(result)) {
      // 从结果中提取需要的配置项
      const configMap = result.reduce((map, item) => {
        map[item.configKey] = item.configValue;
        return map;
      }, {});
      
      // 填充表单
      formData.value = {
        sysName: configMap['system.name'] || '医疗体检系统',
        sysTitle: configMap['system.title'] || '医疗体检管理平台',
        sysLogo: configMap['system.logo'] || '',
        sysCopyright: configMap['system.copyright'] || '© 2023 医疗体检管理平台',
        sysBeian: configMap['system.beian'] || ''
      };
    }
  } catch (error) {
    console.error('获取系统设置失败:', error);
    // 设置默认值
    formData.value = {
      sysName: '医疗体检系统',
      sysTitle: '医疗体检管理平台',
      sysLogo: '',
      sysCopyright: '© 2023 医疗体检管理平台',
      sysBeian: ''
    };
  }
};

onMounted(() => {
  getSystemSettings();
});
</script>

<style scoped>
.card-box {
  margin: 20px;
}
</style> 