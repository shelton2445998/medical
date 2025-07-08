<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
// 使用Vue 3的组合式API
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f0f2f5;
}

#app {
  height: 100%;
}

/* 重置一些全局样式 */
.el-card {
  border-radius: 4px;
}

.el-card__header {
  padding: 15px 20px;
  font-weight: 600;
}

/* 移动端适配 */
@media screen and (max-width: 768px) {
  .el-dialog {
    width: 95% !important;
  }
  
  .el-form-item {
    margin-bottom: 18px;
  }
}
</style>
