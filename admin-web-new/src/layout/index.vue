<!--
  主布局组件
  定义整个管理系统的布局结构，包括头部、侧边栏和主内容区域
-->
<template>
  <!-- 顶部布局模式时的头部 -->
  <layout-header v-if="configure.layoutMode===1"/>
  
  <!-- 布局容器 -->
  <div class="wrapper-box">
    <!-- 左侧边栏区域 -->
    <div v-if="configure.layoutMode!==1" class="layout-left" :class="{'hideSidebar':configure.isCollapse}">
      <!-- Logo组件 -->
      <Logo/>
      <!-- 侧边栏菜单 -->
      <Sidebar/>
    </div>
    
    <!-- 右侧主内容区域 -->
    <div class="layout-right">
      <!-- 非顶部布局模式时的头部 -->
      <layout-header v-if="configure.layoutMode!==1"/>
      <!-- 主内容容器 -->
      <app-main/>
    </div>
  </div>
</template>

<script lang="ts" setup>
/**
 * 主布局组件逻辑
 * 引入各个子组件并管理布局状态
 */
import Logo from './components/sidebar/logo.vue'
import Sidebar from './components/sidebar/index.vue'
import LayoutHeader from './components/header/index.vue'
import AppMain from './components/appMain/index.vue'
import {storeToRefs} from 'pinia'
import {useConfigStoreHook} from "@/store/modules/config";

// 获取全局配置状态
const {configure} = storeToRefs(useConfigStoreHook())
</script>

<style lang="scss">
/**
 * 布局组件样式
 * 定义整体布局的结构和响应式行为
 */
.wrapper-box{
  width: 100%;
  height: inherit;
  display: flex;
  
  /* 左侧边栏样式 */
  .layout-left{
    flex:0 0 210px;                               // 固定宽度210px
    display: flex;
    flex-direction: column;
    border-right: 1px solid var(--el-menu-border-color);
    transition: width .3s ease;                   // 宽度变化过渡效果
    
    /* 隐藏侧边栏状态 */
    &.hideSidebar{
      flex:0 0 64px !important;                   // 折叠后宽度64px
    }
  }
  
  /* 右侧主内容区域样式 */
  .layout-right{
    flex:1;                                       // 自适应剩余空间
    min-width: 800px;                             // 最小宽度限制
  }
  
  /* 父级容器样式 */
  .layout-parent{
    overflow-x: hidden;                           // 隐藏横向滚动条
  }
}
</style>
