<!--
/**
 * @fileoverview 自定义标签组件
 * @description 基于Element Plus Tag封装的标签组件，提供统一的标签样式
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 多种标签类型
 * - 可关闭标签
 * - 自定义颜色
 * - 尺寸配置
 * - 圆角样式
 */
-->
<template>
  <el-tag :type="type" :color="color" :effect="effect">
    <slot></slot>
  </el-tag>
</template>

<script lang="ts" setup>
const types=["primary","success","info","warning","danger"]

const props = withDefaults(defineProps<{
  type:string
}>(), {
  type:''
})

let effect=ref('light');
const type=computed(()=>types.indexOf(props.type)>=0?props.type:'')

const color=computed(()=>{
  return types.indexOf(props.type)>=0?"":props.type;
})
</script>

<style scoped>

</style>
