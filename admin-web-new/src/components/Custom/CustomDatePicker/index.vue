<!--
/**
 * @fileoverview 自定义日期选择器组件
 * @description 基于Element Plus DatePicker封装的日期选择器组件，提供统一的日期选择功能
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 日期选择
 * - 日期范围选择
 * - 时间选择
 * - 格式化显示
 * - 快捷选择
 * - 禁用日期设置
 */
-->
<template>
  <el-date-picker
      v-model="data.value"
      value-format="YYYY-MM-DD"
      start-placeholder="开始时间"
      end-placeholder="结束时间"
      :type="type"
  />
</template>

<script lang="ts" setup>
type pickerType =
    "year"
    | "month"
    | "date"
    | "dates"
    | "datetime"
    | "week"
    | "datetimerange"
    | "daterange"
    | "monthrange"

const props = withDefaults(defineProps<{
  value?: any,
  startDate?:any
  endDate?:any
  type?: pickerType
  placeholder?: string
}>(), {
  placeholder: '请选择',
  type: "date"
})

const emits = defineEmits<{
  (event: 'update:value', val: any): void
  (event: 'update:startDate', val: any): void
  (event: 'update:endDate', val: any): void
}>()

const data: any = reactive({
  value: computed({
    get: () => {
      if(props.type=="daterange"){
        return [props.startDate,props.endDate]
      }else{
        return props.value
      }
    },
    set: val => {
      if(props.type=="daterange"){
        Array.isArray(val)?emits('update:startDate', val[0]):emits('update:startDate', '')
        Array.isArray(val)?emits('update:endDate', val[1]):emits('update:endDate', '')
      }else{
        emits('update:value', val);
      }
    }
  }),
})
</script>

<style scoped>

</style>
