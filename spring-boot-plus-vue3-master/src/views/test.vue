<template>
  <div class="test-page">
    <h1>测试页面正常显示</h1>
    <p>如果您能看到这个页面，说明路由系统正常工作</p>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';

onMounted(() => {
  console.log('测试页面已挂载');
});
</script>

<style scoped>
.test-page {
  padding: 20px;
  text-align: center;
}
</style> 