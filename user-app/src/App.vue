<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <h1>医疗预约系统 - 用户端</h1>
      </el-header>
      <el-main>
        <el-card class="welcome-card">
          <template #header>
            <div class="card-header">
              <span>欢迎使用医疗预约APP</span>
            </div>
          </template>
          <div class="card-content">
            <p>这是基于Vue 3和Element Plus开发的用户移动应用</p>
            <el-button type="primary" round>预约挂号</el-button>
            <el-button type="info" round>查询预约</el-button>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
// 使用Vue 3的组合式API
</script>

<style scoped>
.el-header {
  background-color: #E6A23C;
  color: white;
  text-align: center;
  line-height: 60px;
}

.welcome-card {
  max-width: 600px;
  margin: 0 auto;
  margin-top: 40px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
  padding: 20px;
}

.el-button {
  margin: 10px;
}
</style>
