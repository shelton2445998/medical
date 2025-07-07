<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <h1>医疗预约系统 - 管理员端</h1>
      </el-header>
      <el-main>
        <el-card class="welcome-card">
          <template #header>
            <div class="card-header">
              <span>欢迎使用管理员后台</span>
            </div>
          </template>
          <div class="card-content">
            <p>这是基于Vue 3和Element Plus开发的管理员后台</p>
            <el-button type="success">进入系统</el-button>
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
  background-color: #67C23A;
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
</style>
