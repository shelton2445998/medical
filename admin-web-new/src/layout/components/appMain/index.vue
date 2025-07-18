<!--
/**
 * @fileoverview 主内容区域组件
 * @description 系统主要内容展示区域，包含路由视图和页面切换功能
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 路由视图展示
 * - 页面切换动画
 * - 缓存管理
 * - 响应式布局
 * - 内容区域适配
 */
-->
<template>
  <section class="app-main">
    <router-view>
      <template #default="{ Component, route }">
        <transition name="el-fade-in-linear" mode="out-in">
          <keep-alive v-if="route.meta.isCache">
            <component
                :is="Component"
                :key="route.fullPath"
                class="main-content"
            />
          </keep-alive>
          <component
              v-else
              :is="Component"
              :key="route.fullPath"
              class="main-content"
          />
        </transition>
      </template>
    </router-view>
  </section>
</template>

<script lang="ts" setup>
// 页面加载时
import {NextLoading} from "@/utils/loading";

onMounted(() => {
  NextLoading.done(600);
});
</script>

<style lang="scss" scoped>
.app-main {
  width: 100%;
  height: calc(100vh - 90px);
  position: relative;
  overflow-x: hidden;
  background-color: #f8f8f8;
  padding: 2px;
}
</style>
