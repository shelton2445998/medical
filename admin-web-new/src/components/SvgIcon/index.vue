<!--
/**
 * @fileoverview SVG图标组件
 * @description 提供SVG图标的渲染功能，支持自定义颜色、大小等属性
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - SVG图标渲染
 * - 自定义颜色
 * - 自定义大小
 * - 图标名称映射
 * - 动态加载
 * - 样式定制
 */
-->
<template>
  <!-- SVG图标容器 -->
  <svg aria-hidden="true" class="svg-icon" :width="props.size" :height="props.size">
    <!-- 使用symbol引用图标 -->
    <use :xlink:href="symbolId" />
  </svg>
</template>

<script setup lang="ts">
/**
 * SVG图标组件逻辑
 * 提供图标名称和大小的配置
 */

/**
 * 组件属性定义
 */
const props = defineProps({
  // 图标名称（必填）
  name: {
    type: String,
    required: true
  },
  // 图标大小（可选，默认1em）
  size: {
    type: String,
    default: '1em'
  }
})

/**
 * 计算图标的Symbol ID
 * 用于在SVG中引用对应的图标
 */
const symbolId = computed(() => `#${props.name}`)
</script>

<style lang="scss" scoped>
/**
 * SVG图标样式
 * 设置图标的基本样式属性
 */
.svg-icon{
  fill:currentColor;        /* 使用当前文本颜色填充图标 */
  font-size: inherit;       /* 继承父元素的字体大小 */
}
</style>
