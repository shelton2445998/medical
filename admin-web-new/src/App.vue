<!--
  应用程序根组件
  提供全局配置和路由视图容器
-->
<template>
  <!-- Element Plus 配置提供者，用于全局配置 -->
  <el-config-provider :locale="currentLocale">
    <!-- 路由视图容器，用于渲染当前路由对应的组件 -->
    <router-view />
  </el-config-provider>
</template>

<script lang="ts" setup>
/**
 * 应用程序根组件逻辑
 * 负责全局配置和国际化设置
 */
import {useGlobeConfigure} from "@/layout/hook/useGlobe";

// 获取全局配置相关方法
const {setGlobeConfigure,getTranslation} =useGlobeConfigure();

// 计算当前语言环境配置
const currentLocale=computed(getTranslation);

// 设置全局配置
setGlobeConfigure();
</script>
