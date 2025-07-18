<!--
  医生端应用程序根组件
  作为整个应用的入口点，负责渲染路由视图
  @author 医生端项目组
  @date 2024
-->
<template>
  <!-- 路由视图容器，用于渲染当前路由对应的组件 -->
  <router-view />
</template>

<script>
/**
 * 应用程序根组件
 * 提供应用的基础结构和全局样式
 */
export default {
  // 组件名称，用于调试和开发工具识别
  name: 'App'
}
</script>

<style>
/* 全局基础样式配置 */
html, body, #app {
  height: 100%;      /* 设置全屏高度 */
  margin: 0;         /* 清除默认边距 */
  padding: 0;        /* 清除默认内边距 */
  
  /* 设置全局字体族，优先使用系统字体 */
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
}
</style>
