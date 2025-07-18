<!--
/**
 * @fileoverview 自定义分页组件
 * @description 基于Element Plus Pagination封装的分页组件，提供统一的分页功能
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 分页信息显示
 * - 页码切换
 * - 每页条数选择
 * - 跳转到指定页
 * - 响应式设计
 */
-->
<template>
  <div class="pagination-box">
    <el-pagination
        v-model:current-page="paginationData.currentPage"
        v-model:page-size="paginationData.pageSize"
        :page-sizes="[10, 20,50, 100]"
        background
        hide-on-single-page
        layout="total, sizes, prev, pager, next, jumper"
        :total="props.total"
        @size-change="changeSize"
        @current-change="changePage"
    />
  </div>
</template>

<script lang="ts" setup>
import {reactive} from "vue";

const props = withDefaults(defineProps<{
  currentPage: number,
  pageSize?: number,
  total: number
}>(), {
  pageSize: 10,
})
let paginationData = reactive({
  currentPage: computed({
    get: () => props.currentPage,
    set: (val: number) => {
      emits('update:currentPage', val)
    }
  }),
  pageSize: computed({
    get: () => props.pageSize,
    set: (val: number) => {
      emits('update:pageSize', val)
    }
  }),
})

// 每页条数
const changeSize = (size: number) => {
  paginationData.currentPage = 1;
  paginationData.pageSize = size;
  emits('changeSize', size)
}

// 翻页
const changePage = (page: number) => {
  paginationData.currentPage = page;
  emits('changePage', page)
}

const emits = defineEmits<{
  (event: 'changePage', page: number): void
  (event: 'changeSize', size: number): void
  (event: 'update:pageSize', size: number): void
  (event: 'update:currentPage', page: number): void
}>()
</script>

<style lang="scss" scoped>
.pagination-box {
  display: flex;
  justify-content: flex-end;
  padding: 20px 0;
}
</style>
