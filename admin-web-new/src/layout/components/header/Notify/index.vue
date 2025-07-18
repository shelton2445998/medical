<!--
/**
 * @fileoverview 通知组件
 * @description 系统通知中心组件，展示系统消息和用户通知
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 通知列表展示
 * - 未读消息提醒
 * - 消息分类
 * - 标记已读
 * - 通知清空
 * - 实时更新
 */
-->
<template>
    <el-popover
        :width="300"
        trigger="click"
        popper-style="padding: 20px;padding:0"
    >
      <template #reference>
        <div class="header-bar__icon">
          <i class="iconfont icon-xiaoxi" title="通知"></i>
        </div>
      </template>
      <template #default>
        <el-tabs
            v-model="activeName"
        >
          <el-tab-pane label="通知(0)" name="first">
            <div class="news-body">
              <el-empty description="暂无通知" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="消息(0)" name="second">
            <div class="news-body">
              <el-empty description="暂无消息" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="待办(0)" name="third">
            <div class="news-body">
              <el-empty description="暂无待办" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </template>
    </el-popover>
</template>

<script lang="ts" setup>

const activeName = 'first'
const newsList = []
</script>

<style lang="scss" scoped>
:deep(*){
  .el-tabs__nav-scroll{
    display: flex !important;
    justify-content: center;
  }
  .el-tabs__nav-wrap::after{
    height: 1px !important;
  }
  .el-empty{
    padding: 0 0 10px;
  }
}
</style>
