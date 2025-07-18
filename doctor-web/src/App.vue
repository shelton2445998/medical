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
 * @fileoverview 医生端应用程序根组件
 * @description 作为Vue3应用的根组件，负责路由视图渲染和全局样式配置
 * @author 医生端项目组
 * @version 2.0.0
 * @since 2024-01-01
 * 
 * @features
 * - 路由视图容器
 * - 全局样式配置
 * - 响应式布局支持
 * - 系统字体优化
 * 
 * @dependencies
 * - Vue Router: 路由管理
 * - Element Plus: UI组件库
 * 
 * @usage
 * 作为医生端Vue3项目的根组件，提供应用的基础结构
 * 
 * @performance
 * - 简洁的组件结构，减少渲染开销
 * - 系统字体优先，提升加载速度
 * - 全屏高度设置，优化视觉效果
 * 
 * @maintenance
 * - 清晰的组件命名
 * - 模块化的样式组织
 * - 便于后续功能扩展
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
