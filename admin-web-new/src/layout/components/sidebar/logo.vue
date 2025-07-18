<!--
/**
 * @fileoverview 侧边栏Logo组件
 * @description 侧边栏顶部Logo展示组件，包含系统标识和名称
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - Logo图片展示
 * - 系统名称显示
 * - 折叠状态适配
 * - 点击跳转首页
 * - 响应式设计
 */
-->
<template>
  <div class="logo-box" :class="{'width':configure.isCollapse}">
    <div v-if="configure.isCollapse" class="logo-content">
      <img class="logo-img logo-txt" src="@/assets/logo.png" alt="">
    </div>
    <div v-else class="logo-content">
      <img class="logo-img" src="@/assets/logo.png" alt="">
      <span class="logo-txt ml5">医疗预约系统</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {storeToRefs} from 'pinia'
import {useConfigStoreHook} from "@/store/modules/config";

const {configure} = storeToRefs(useConfigStoreHook())
</script>

<style lang="scss" scoped>
.logo-img{
  width: 24px;
}
.logo-box{
  width: 210px;
  &.width{
    width: 64px;
  }
  .logo-content{
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
  }
  .logo-txt{
    font-size: 21px;
    white-space: nowrap;
    animation: logoAnimation 0.8s ease;
  }
}
@keyframes logoAnimation {
  0%{
    transform: scale(0);
  }
  80%{
    transform: scale(1.2);
  }
  100%{
    transform: scale(1);
  }
}
</style>
