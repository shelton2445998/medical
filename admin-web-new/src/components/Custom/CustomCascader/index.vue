<!--
/**
 * @fileoverview 自定义级联选择器组件
 * @description 基于Element Plus Cascader封装的级联选择器组件，支持多级数据选择
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 多级数据选择
 * - 懒加载支持
 * - 搜索功能
 * - 自定义显示格式
 * - 可清空选择
 * - 禁用状态
 */
-->
<template>
  <el-cascader v-model="data.value" clearable :props="defaultConfig" :show-all-levels="false"
               :options="options||data.options"
               :placeholder="placeholder" style="width: 100%"/>
</template>

<script lang="ts" setup>
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
  checkStrictly: true
}

const emits = defineEmits<{
  (event: 'update:value', name: any): void
}>()

const data: any = reactive({
  value: computed({
    get: () => {
      return getCascadeParent(data.options.length > 0 ? data.options : props.options, props.value)
    },
    set: val => {
      emits('update:value', val?.length > 0 ? val[val.length - 1] : val || 0);
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
