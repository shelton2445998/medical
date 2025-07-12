<template>
  <div class="doctor-home-container">
    <!-- 简单欢迎区域 -->
    <el-card class="welcome-card">
      <h1>欢迎使用医生工作站</h1>
      <p>今天是 {{ new Date().toLocaleDateString() }}</p>
    </el-card>

    <!-- 简单统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <h2>12</h2>
          <p>今日患者</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <h2>5</h2>
          <p>待诊患者</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <h2>7</h2>
          <p>今日已诊</p>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <h2>9</h2>
          <p>今日处方</p>
        </el-card>
      </el-col>
    </el-row>

    <!-- 简单功能区 -->
    <el-card class="function-card">
      <h2>快捷功能</h2>
      <el-row :gutter="20">
        <el-col :span="4" v-for="(item, index) in quickFunctions" :key="index">
          <el-button type="primary" plain>{{ item }}</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      quickFunctions: [
        '患者管理',
        '预约管理',
        '诊断记录',
        '处方管理',
        '数据统计',
        '个人设置'
      ]
    };
  }
};
</script>

<style scoped>
.doctor-home-container {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.welcome-card h1 {
  font-size: 24px;
  margin: 0 0 10px 0;
}

.welcome-card p {
  margin: 0;
  color: #606266;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-card h2 {
  font-size: 24px;
  margin: 0 0 10px 0;
  color: #409eff;
}

.stat-card p {
  margin: 0;
  color: #606266;
}

.function-card {
  margin-bottom: 20px;
}

.function-card h2 {
  margin-top: 0;
  margin-bottom: 16px;
}

.el-button {
  margin-bottom: 10px;
  width: 100%;
}
</style>
