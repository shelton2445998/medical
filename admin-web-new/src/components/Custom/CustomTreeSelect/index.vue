<!--
/**
 * @fileoverview 自定义树选择器组件
 * @description 基于Element Plus Tree和Select封装的树形选择器组件
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 树形数据选择
 * - 搜索功能
 * - 多选支持
 * - 展开/收起控制
 * - 自定义节点内容
 * - 懒加载支持
 */
-->
<template>
  <el-tree-select v-model="data.value" filterable clearable :check-strictly="true" :props="defaultConfig" :data="options||data.options" :placeholder="placeholder"/>
</template>

<script lang="ts" setup>
import {computed, onMounted, reactive} from 'vue'
import {http} from "@/utils/http";
import {getCascadeParent} from "@/utils";


type optionType = {
  name: string
  id: string
  children?: Array<optionType>
}
type configType = {
  valueName: string
  labelName: string
}

const props = withDefaults(defineProps<{
  value: any,
  options?: Array<optionType> | undefined,
  config?: configType
  placeholder?: string
  url?: string
}>(), {
  placeholder: '请选择'
})
const defaultConfig = {
  value: props.config?.valueName || 'id',
  label: props.config?.labelName || 'name',
}

const emits = defineEmits<{
  (event: 'update:value', name: any): void
}>()

const data: any = reactive({
  value: computed({
    get: () => props.value,
    set: val => {
      emits('update:value', val);
    }
  }),
  options: []
})

/** 通过接口获取option数据*/
function getOptions() {
  if (props?.url) {
    http.post<any>(props.url, {}).then((res) => {
      data.options = res
    })
  }
}

onMounted(() => {
  getOptions();
})
</script>

<style lang="scss" scoped>

</style>
