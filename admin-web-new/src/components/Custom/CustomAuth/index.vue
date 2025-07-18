<!--
/**
 * @fileoverview 权限控制组件
 * @description 基于用户权限控制组件显示隐藏的通用组件
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 权限验证
 * - 条件渲染
 * - 支持多种权限类型
 * - 插槽内容支持
 */
-->
<template>
  <slot v-if="getUserAuthBtnList"/>
</template>

<script setup lang="ts">
import {useUserStore} from '@/store/modules/user';

// 定义父组件传过来的值
const props = defineProps({
  value: {
    type: String,
    default: () => '',
  },
});

const {userinfo} = useUserStore();

// 获取用户权限
const getUserAuthBtnList = computed(() => {
  if(userinfo.admin){
    return true;
  }
  return userinfo.permissions.some((v: string) => v === props.value);
});
</script>
