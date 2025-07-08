<script>
export default {
  name: "AdminLayout"
}
</script>

<template>
  <div class="layout-container">
    <el-container style="height: 100vh;">
      <!-- 侧边栏 -->
      <el-aside width="220px" class="sidebar">
        <div class="logo">
          <img src="../assets/logo.png" alt="Logo" height="40" />
          <h2>医疗预约系统</h2>
        </div>
        <!-- 侧边菜单 -->
        <el-menu
          router
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF">

          <el-menu-item index="/dashboard">
            <el-icon><el-icon-odometer /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>

          <el-menu-item index="/users">
            <el-icon><el-icon-user /></el-icon>
            <span>用户管理</span>
          </el-menu-item>

          <el-menu-item index="/doctors">
            <el-icon><el-icon-user-filled /></el-icon>
            <span>医生管理</span>
          </el-menu-item>

          <el-menu-item index="/packages">
            <el-icon><el-icon-suitcase /></el-icon>
            <span>体检套餐管理</span>
          </el-menu-item>

          <el-menu-item index="/checkItems">
            <el-icon><el-icon-document /></el-icon>
            <span>体检项目管理</span>
          </el-menu-item>

          <el-menu-item index="/hospitals">
            <el-icon><el-icon-house /></el-icon>
            <span>医院管理</span>
          </el-menu-item>

          <el-menu-item index="/appointments">
            <el-icon><el-icon-calendar /></el-icon>
            <span>预约管理</span>
          </el-menu-item>

          <el-menu-item index="/finance">
            <el-icon><el-icon-money /></el-icon>
            <span>财务管理</span>
          </el-menu-item>

          <el-menu-item index="/settings">
            <el-icon><el-icon-setting /></el-icon>
            <span>系统设置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <!-- 切换侧边栏按钮 -->
            <el-icon class="toggle-button" @click="toggleSidebar">
              <el-icon-menu />
            </el-icon>
            <!-- 面包屑 -->
            <el-breadcrumb>
              <el-breadcrumb-item v-for="(item, index) in breadcrumb" :key="index" :to="item.path">
                {{ item.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>

          <div class="header-right">
            <!-- 用户信息 -->
            <el-dropdown>
              <span class="user-info">
                <img src="../assets/avatar.svg" class="avatar" alt="avatar" />
                <span>管理员</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToSettings">个人设置</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主体内容 -->
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const sidebarCollapse = ref(false)

// 计算当前激活的菜单项
const activeMenu = computed(() => {
  return route.path
})

// 面包屑导航
const breadcrumb = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  const first = { path: '/', title: '首页' }
  const result = [first]
  
  matched.forEach(item => {
    result.push({
      path: item.path,
      title: item.meta.title
    })
  })
  
  return result
})

// 切换侧边栏
const toggleSidebar = () => {
  sidebarCollapse.value = !sidebarCollapse.value
}

// 前往设置页面
const goToSettings = () => {
  router.push('/settings')
}

// 退出登录
const logout = () => {
  localStorage.removeItem('admin-token')
  router.push('/login')
}

// 在组件挂载时检查登录状态
onMounted(() => {
  const token = localStorage.getItem('admin-token')
  if (!token) {
    router.push('/login')
  }
})

// 监听路由变化
watch(
  () => route.path,
  () => {
    // 可以添加路由变化时的逻辑
  }
)
</script>

<style scoped>
.layout-container {
  height: 100%;
}

.sidebar {
  background-color: #304156;
  overflow: hidden;
  transition: width 0.3s;
}

.sidebar .logo {
  height: 60px;
  line-height: 60px;
  display: flex;
  align-items: center;
  padding-left: 20px;
  background-color: #2b3649;
  color: #fff;
}

.sidebar .logo h2 {
  margin-left: 10px;
  font-size: 16px;
  white-space: nowrap;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.toggle-button {
  font-size: 20px;
  margin-right: 20px;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 8px;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .el-aside {
    width: 64px !important;
  }

  .el-menu--collapse .el-menu-item span {
    display: none;
  }

  .sidebar .logo h2 {
    display: none;
  }
}
</style> 