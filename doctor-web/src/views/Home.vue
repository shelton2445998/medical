<!--
  医生端主页面布局组件
  
  提供完整的医生端应用布局结构，包含侧边栏导航、头部工具栏、主内容区域
  负责整体页面布局和路由导航管理
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="home-container">
    <!-- 主容器布局 -->
    <el-container class="main-container">
      <!-- 侧边栏 - 可折叠的导航菜单 -->
      <el-aside :width="sidebarCollapsed ? '64px' : '220px'" class="aside">
        <!-- Logo区域 -->
        <div class="logo">
          <img src="../assets/images/new_logo.png" alt="Logo" />
          <span v-if="!sidebarCollapsed">医生工作站</span>
        </div>
        
        <!-- 侧边栏菜单 -->
        <el-menu 
          :default-active="activeMenu" 
          router
          :collapse="sidebarCollapsed" 
          background-color="#304156" 
          text-color="#bfcbd9"
          active-text-color="#3E7BFA"
          class="sidebar-menu">
          <!-- 工作台菜单项 -->
          <el-menu-item index="/home/dashboard">
            <el-icon><icon-menu /></el-icon>
            <template #title>工作台</template>
          </el-menu-item>
          
          <!-- 患者管理菜单项（已注释） -->
          <!-- <el-menu-item index="/home/patients">
            <el-icon><user /></el-icon>
            <template #title>患者管理</template>
          </el-menu-item> -->
          
          <!-- 预约管理菜单项（已注释） -->
          <!-- <el-menu-item index="/home/appointments">
            <el-icon><calendar /></el-icon>
            <template #title>预约管理</template>
          </el-menu-item> -->
          
          <!-- 体检报告菜单项 -->
          <el-menu-item index="/home/examination-reports">
            <el-icon><document-checked /></el-icon>
            <template #title>体检报告</template>
          </el-menu-item>
          
          <!-- 病历管理和处方管理菜单项（已注释） -->
          <!-- <el-menu-item index="/home/medical-records">
            <el-icon><document /></el-icon>
            <template #title>病历管理</template>
          </el-menu-item>
          <el-menu-item index="/home/prescriptions">
            <el-icon><tickets /></el-icon>
            <template #title>处方管理</template>
          </el-menu-item> -->
          
          <!-- 排班管理菜单项 -->
          <el-menu-item index="/home/schedule">
            <el-icon><timer /></el-icon>
            <template #title>排班管理</template>
          </el-menu-item>
          
          <!-- 个人设置菜单项 -->
          <el-menu-item index="/home/profile">
            <el-icon><setting /></el-icon>
            <template #title>个人设置</template>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 右侧主内容容器 -->
      <el-container>
        <!-- 头部工具栏 -->
        <el-header class="header">
          <!-- 头部左侧 - 侧边栏折叠按钮 -->
          <div class="header-left">
            <el-icon class="menu-toggle" @click="toggleSidebar">
              <component :is="sidebarCollapsed ? 'Expand' : 'Fold'" />
            </el-icon>
          </div>
          
          <!-- 头部右侧 - 操作按钮和用户信息 -->
          <div class="header-right">
            <!-- 操作按钮组 -->
            <div class="header-actions">
              <!-- 消息通知徽章 -->
              <el-badge :value="3" :max="99" class="notification-badge" type="primary">
                <el-icon class="header-icon"><bell /></el-icon>
              </el-badge>
              <!-- 消息框图标 -->
              <el-icon class="header-icon ml-10"><message-box /></el-icon>
            </div>
            
            <!-- 分割线 -->
            <el-divider direction="vertical" class="header-divider" />
            
            <!-- 用户信息下拉菜单 -->
            <el-dropdown trigger="click" class="user-dropdown">
              <div class="user-info">
                <div class="user-avatar">{{ doctorInfo.name ? doctorInfo.name.charAt(0) : 'D' }}</div>
                <span class="user-name">{{ doctorInfo.name }}</span>
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="openPersonalInfo">
                    <el-icon><user /></el-icon>个人信息
                  </el-dropdown-item>
                  <el-dropdown-item @click="openChangePassword">
                    <el-icon><key /></el-icon>修改密码
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">
                    <el-icon><switch-button /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 主内容区域 - 路由视图 -->
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
    
    <!-- 个人信息对话框 -->
    <el-dialog v-model="personalInfoDialogVisible" title="个人信息" width="500px">
      <div class="profile-dialog-content">
        <!-- 个人信息头部 -->
        <div class="profile-header">
          <div class="profile-avatar">{{ doctorInfo.name ? doctorInfo.name.charAt(0) : 'D' }}</div>
          <h3 class="profile-name">{{ doctorInfo.name }}</h3>
          <p class="profile-role">{{ doctorInfo.title || '医生' }}</p>
        </div>
        <el-divider />
        
        <!-- 个人信息详情表单 -->
        <el-form label-width="100px">
          <el-form-item label="工号">{{ doctorInfo.id || '-' }}</el-form-item>
          <el-form-item label="医院">{{ doctorInfo.hospitalName || '-' }}</el-form-item>
          <el-form-item label="科室">{{ doctorInfo.departmentName || '-' }}</el-form-item>
          <el-form-item label="联系电话">{{ doctorInfo.mobile || '-' }}</el-form-item>
          <el-form-item label="邮箱">{{ doctorInfo.email || '-' }}</el-form-item>
        </el-form>
      </div>
    </el-dialog>
    
    <!-- 修改密码对话框 -->
    <el-dialog v-model="changePasswordDialogVisible" title="修改密码" width="500px">
      <!-- 密码修改表单 -->
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
        <!-- 原密码输入 -->
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
        </el-form-item>
        
        <!-- 新密码输入 -->
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
        </el-form-item>
        
        <!-- 确认密码输入 -->
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
        </el-form-item>
      </el-form>
      
      <!-- 对话框底部按钮 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="changePasswordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitChangePassword">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
/**
 * 医生端主页面布局逻辑
 * 
 * 处理页面布局状态管理、用户信息管理、导航控制等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, reactive, computed, onMounted } from 'vue'
// 导入路由相关函数
import { useRoute, useRouter } from 'vue-router'
// 导入 Element Plus 组件
import { ElMessage, ElMessageBox } from 'element-plus'
// 导入 Element Plus 图标组件
import { Menu as IconMenu, User, Calendar, Document, Tickets, Fold, ArrowDown, DocumentChecked, Timer, Setting, Bell, MessageBox, Key, SwitchButton, Expand } from '@element-plus/icons-vue'
// 导入状态管理
import store from '@/store'
// 导入API接口函数
import { getDoctorInfo, doctorLogout, updatePassword } from '@/api/doctor'

/**
 * 主页面组件导出
 * 
 * 定义组件的基本信息、注册的图标组件和主要逻辑
 */
export default {
  name: 'HomeView',
  // 注册使用的图标组件
  components: {
    IconMenu,
    User,
    Calendar,
    Document,
    Tickets,
    Fold,
    ArrowDown,
    DocumentChecked,
    Timer,
    Setting,
    Bell,
    MessageBox,
    Key,
    SwitchButton,
    Expand
  },
  setup() {
    // 路由实例
    const route = useRoute()
    const router = useRouter()
    
    // 表单引用
    const passwordFormRef = ref(null)
    
    // 对话框显示状态
    const personalInfoDialogVisible = ref(false)    // 个人信息对话框
    const changePasswordDialogVisible = ref(false) // 修改密码对话框
    const sidebarCollapsed = ref(false)             // 侧边栏折叠状态

    /**
     * 当前活跃菜单项
     * 
     * 根据当前路由路径计算活跃的菜单项
     */
    const activeMenu = computed(() => route.path)

    /**
     * 医生信息响应式对象
     * 
     * 存储当前登录医生的详细信息
     */
    const doctorInfo = reactive({
      name: '',           // 医生姓名
      id: '',             // 医生工号
      hospitalName: '',   // 医院名称
      departmentName: '', // 科室名称
      title: '',          // 职位头衔
      mobile: '',         // 手机号
      email: ''           // 邮箱
    })

    /**
     * 修改密码表单数据
     * 
     * 包含原密码、新密码和确认密码字段
     */
    const passwordForm = reactive({
      oldPassword: '',    // 原密码
      newPassword: '',    // 新密码
      confirmPassword: '' // 确认密码
    })

    /**
     * 密码表单校验规则
     * 
     * 定义密码修改表单的验证规则
     */
    const passwordRules = {
      oldPassword: [
        { required: true, message: '请输入原密码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码长度不少于6个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        {
          // 自定义验证器：确保两次输入的密码一致
          validator: (rule, value, callback) => {
            if (value !== passwordForm.newPassword) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }

    /**
     * 组件挂载时的初始化逻辑
     * 
     * 在组件挂载后自动加载医生信息
     */
    onMounted(async () => {
      await fetchDoctorInfo()
    })

    /**
     * 获取医生信息
     * 
     * 从服务器获取当前登录医生的详细信息并存储到本地
     */
    const fetchDoctorInfo = async () => {
      try {
        const res = await getDoctorInfo()
        console.log('Home - 获取医生信息响应:', res)
        
        if (res.code === 200) {
          console.log('Home - 医生原始数据:', res.data)
          
          // 将原始数据映射到doctorInfo对象
          Object.assign(doctorInfo, {
            name: res.data.nickname,
            id: res.data.userId,
            hospitalName: res.data.hospitalName || '',
            departmentName: res.data.departmentName || '',
            title: res.data.roleName,
            mobile: res.data.phone,
            email: res.data.email
          })
          
          // 将医生信息存储到localStorage中，供其他页面使用
          const doctorInfoToStore = {
            id: res.data.userId,
            username: res.data.nickname,
            hospitalName: res.data.hospitalName,
            departmentName: res.data.departmentName,
            title: res.data.roleName,
            mobile: res.data.phone,
            email: res.data.email
          };
          
          console.log('Home - 即将存储的医生信息:', doctorInfoToStore)
          localStorage.setItem('doctorInfo', JSON.stringify(doctorInfoToStore))
        }
      } catch (error) {
        console.error('获取医生信息失败：', error)
      }
    }

    /**
     * 切换侧边栏折叠状态
     * 
     * 控制侧边栏的展开和收起
     */
    const toggleSidebar = () => {
      sidebarCollapsed.value = !sidebarCollapsed.value
    }

    /**
     * 打开个人信息对话框
     * 
     * 显示医生个人信息的查看对话框
     */
    const openPersonalInfo = () => {
      personalInfoDialogVisible.value = true
    }

    /**
     * 打开修改密码对话框
     * 
     * 显示密码修改的表单对话框
     */
    const openChangePassword = () => {
      changePasswordDialogVisible.value = true
    }

    /**
     * 提交修改密码
     * 
     * 验证表单并提交密码修改请求
     */
    const submitChangePassword = () => {
      if (!passwordFormRef.value) return
      
      passwordFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            await updatePassword({
              oldPassword: passwordForm.oldPassword,
              newPassword: passwordForm.newPassword
            })
            ElMessage.success('密码修改成功')
            changePasswordDialogVisible.value = false
            
            // 重置表单数据
            passwordForm.oldPassword = ''
            passwordForm.newPassword = ''
            passwordForm.confirmPassword = ''
          } catch (error) {
            console.error('修改密码失败：', error)
            ElMessage.error(error.message || '修改密码失败，请稍后重试')
          }
        }
      })
    }

    /**
     * 处理退出登录
     * 
     * 显示确认对话框，确认后清除本地存储并跳转到登录页
     */
    const handleLogout = () => {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await doctorLogout()
          console.log('退出登录成功')
        } catch (error) {
          console.error('退出登录失败：', error)
        } finally {
          // 无论成功失败，都清除本地存储并跳转到登录页
          localStorage.removeItem('doctorToken')
          localStorage.removeItem('doctorInfo')
          router.push('/')
          ElMessage.success('已安全退出系统')
        }
      }).catch(() => {})
    }

    /**
     * 跳转到个人设置页面
     * 
     * 路由导航到个人设置页面
     */
    const goToProfilePage = () => {
      router.push('/home/profile')
    }
    
    // 返回模板需要的数据和方法
    return {
      doctorInfo,
      activeMenu,
      personalInfoDialogVisible,
      changePasswordDialogVisible,
      passwordForm,
      passwordRules,
      passwordFormRef,
      sidebarCollapsed,
      toggleSidebar,
      openPersonalInfo,
      openChangePassword,
      submitChangePassword,
      handleLogout,
      goToProfilePage
    }
  }
}
</script>

<!--
  主页面布局样式
  
  定义医生端主页面的整体布局和视觉效果
  包含侧边栏、头部、主内容区域等组件样式
-->
<style scoped>
/* ==================== 基础容器样式 ==================== */

/**
 * 主容器样式
 * 
 * 提供全屏布局容器，隐藏滚动条避免布局混乱
 */
.home-container {
  height: 100vh;          /* 视口高度 */
  width: 100vw;           /* 视口宽度 */
  overflow: hidden;       /* 隐藏溢出内容 */
}

/**
 * 主布局容器
 * 
 * Element Plus 容器组件的基础样式
 */
.main-container {
  height: 100%;
}

/* ==================== 侧边栏样式 ==================== */

/**
 * 侧边栏容器样式
 * 
 * 深色背景的侧边栏，支持过渡动画和阴影效果
 */
.aside {
  background-color: #304156;                    /* 深蓝色背景 */
  transition: width var(--transition-normal);  /* 宽度变化过渡 */
  overflow: hidden;                             /* 隐藏溢出内容 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);  /* 阴影效果 */
  position: relative;                           /* 相对定位 */
  z-index: 10;                                 /* 层级较高 */
}

/**
 * Logo区域样式
 * 
 * 侧边栏顶部的品牌标识区域
 */
.logo {
  height: 60px;                                /* 固定高度 */
  display: flex;                               /* 弹性布局 */
  align-items: center;                         /* 垂直居中 */
  justify-content: center;                     /* 水平居中 */
  color: #fff;                                 /* 白色文字 */
  font-size: 18px;                            /* 字体大小 */
  font-weight: bold;                           /* 粗体 */
  background-color: #263445;                   /* 更深的背景色 */
  padding: 0 16px;                            /* 内边距 */
  overflow: hidden;                            /* 隐藏溢出 */
  transition: all var(--transition-normal);   /* 全属性过渡 */
}

/**
 * Logo图片样式
 * 
 * 品牌标识图片的样式设置
 */
.logo img {
  height: 32px;                               /* 图片高度 */
  margin-right: 8px;                          /* 右边距 */
  transition: margin var(--transition-normal); /* 边距过渡 */
}

/**
 * 侧边栏菜单样式
 * 
 * 移除默认的右边框
 */
.sidebar-menu {
  border-right: none;
}

/* ==================== 头部样式 ==================== */

/**
 * 头部工具栏样式
 * 
 * 白色背景的头部区域，包含工具按钮和用户信息
 */
.header {
  background-color: #fff;                     /* 白色背景 */
  display: flex;                              /* 弹性布局 */
  align-items: center;                        /* 垂直居中 */
  justify-content: space-between;             /* 两端对齐 */
  padding: 0 20px;                           /* 水平内边距 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06); /* 轻微阴影 */
  z-index: 5;                                /* 层级设置 */
  height: 60px !important;                   /* 固定高度 */
}

/**
 * 头部左侧区域样式
 * 
 * 包含侧边栏折叠按钮等左侧工具
 */
.header-left {
  display: flex;
  align-items: center;
}

/**
 * 菜单折叠按钮样式
 * 
 * 控制侧边栏展开/收起的按钮
 */
.menu-toggle {
  font-size: 20px;                           /* 图标大小 */
  cursor: pointer;                            /* 鼠标指针 */
  color: #595959;                            /* 图标颜色 */
  transition: color var(--transition-normal); /* 颜色过渡 */
  padding: 8px;                              /* 内边距 */
  border-radius: 4px;                        /* 圆角 */
}

/**
 * 菜单折叠按钮悬停效果
 * 
 * 鼠标悬停时的视觉反馈
 */
.menu-toggle:hover {
  color: var(--primary-color);               /* 主色调 */
  background-color: rgba(0, 0, 0, 0.04);    /* 浅灰背景 */
}

/**
 * 头部右侧区域样式
 * 
 * 包含通知、用户信息等右侧内容
 */
.header-right {
  display: flex;
  align-items: center;
}

/**
 * 头部操作按钮区域样式
 * 
 * 包含通知、消息等操作按钮
 */
.header-actions {
  display: flex;
  align-items: center;
}

/**
 * 头部图标按钮样式
 * 
 * 通知、消息等功能图标的统一样式
 */
.header-icon {
  font-size: 18px;                           /* 图标大小 */
  cursor: pointer;                            /* 鼠标指针 */
  color: #595959;                            /* 图标颜色 */
  padding: 8px;                              /* 内边距 */
  border-radius: 4px;                        /* 圆角 */
  transition: all var(--transition-normal);  /* 全属性过渡 */
}

/**
 * 头部图标按钮悬停效果
 * 
 * 鼠标悬停时的视觉反馈
 */
.header-icon:hover {
  color: var(--primary-color);               /* 主色调 */
  background-color: rgba(0, 0, 0, 0.04);    /* 浅灰背景 */
}

/**
 * 头部分割线样式
 * 
 * 分隔操作按钮和用户信息区域
 */
.header-divider {
  height: 24px;                              /* 分割线高度 */
  margin: 0 16px;                            /* 水平间距 */
}

/**
 * 用户下拉菜单样式
 * 
 * 用户信息和操作菜单的容器
 */
.user-dropdown {
  cursor: pointer;                            /* 鼠标指针 */
  margin-left: 8px;                          /* 左边距 */
}

/**
 * 用户信息区域样式
 * 
 * 显示用户头像、姓名等信息
 */
.user-info {
  display: flex;                              /* 弹性布局 */
  align-items: center;                        /* 垂直居中 */
  padding: 4px 8px;                          /* 内边距 */
  border-radius: 4px;                        /* 圆角 */
  transition: background-color var(--transition-normal); /* 背景色过渡 */
}

/**
 * 用户信息悬停效果
 * 
 * 鼠标悬停时的背景色变化
 */
.user-info:hover {
  background-color: rgba(0, 0, 0, 0.04);    /* 浅灰背景 */
}

/**
 * 用户头像样式
 * 
 * 圆形头像，显示用户姓名首字母
 */
.user-avatar {
  width: 32px;                               /* 头像宽度 */
  height: 32px;                              /* 头像高度 */
  border-radius: 50%;                        /* 圆形头像 */
  background-color: var(--primary-color);   /* 主色调背景 */
  color: #fff;                               /* 白色文字 */
  display: flex;                             /* 弹性布局 */
  align-items: center;                       /* 垂直居中 */
  justify-content: center;                   /* 水平居中 */
  font-size: 16px;                          /* 字体大小 */
  font-weight: 500;                          /* 字体粗细 */
  margin-right: 8px;                         /* 右边距 */
}

/**
 * 用户姓名样式
 * 
 * 显示在头像旁边的用户姓名
 */
.user-name {
  font-size: 14px;                          /* 字体大小 */
  color: var(--text-regular);               /* 文字颜色 */
  margin-right: 4px;                        /* 右边距 */
}

/* ==================== 主内容区域样式 ==================== */

/**
 * 主内容区域样式
 * 
 * 右侧主要内容显示区域，包含路由视图
 */
.main {
  background-color: #f0f2f5;                /* 浅灰背景 */
  padding: 24px;                            /* 内边距 */
  overflow-y: auto;                          /* 垂直滚动 */
}

/* ==================== 通知徽章样式 ==================== */

/**
 * 通知徽章内容样式
 * 
 * 调整通知徽章的位置
 */
.notification-badge :deep(.el-badge__content) {
  top: 6px;                                 /* 顶部位置调整 */
  right: 6px;                               /* 右侧位置调整 */
}

/* ==================== 对话框样式 ==================== */

/**
 * 个人信息对话框内容样式
 * 
 * 个人信息对话框的内容区域
 */
.profile-dialog-content {
  padding: 16px 0;                          /* 垂直内边距 */
}

/**
 * 个人信息头部样式
 * 
 * 对话框顶部的头像和基本信息区域
 */
.profile-header {
  display: flex;                             /* 弹性布局 */
  flex-direction: column;                    /* 垂直排列 */
  align-items: center;                       /* 水平居中 */
  margin-bottom: 24px;                       /* 底部边距 */
}

/**
 * 对话框中的头像样式
 * 
 * 比头部头像更大的圆形头像
 */
.profile-avatar {
  width: 80px;                              /* 头像宽度 */
  height: 80px;                             /* 头像高度 */
  border-radius: 50%;                       /* 圆形头像 */
  background-color: var(--primary-color);  /* 主色调背景 */
  color: #fff;                              /* 白色文字 */
  display: flex;                            /* 弹性布局 */
  align-items: center;                       /* 垂直居中 */
  justify-content: center;                   /* 水平居中 */
  font-size: 32px;                          /* 字体大小 */
  font-weight: 500;                          /* 字体粗细 */
  margin-bottom: 16px;                       /* 底部边距 */
}

/**
 * 个人信息姓名样式
 * 
 * 对话框中显示的用户姓名
 */
.profile-name {
  margin: 0 0 8px;                           /* 底部边距 */
  font-size: 20px;                          /* 字体大小 */
  font-weight: 600;                          /* 字体粗细 */
}

/**
 * 个人信息角色样式
 * 
 * 对话框中显示的用户角色或职位
 */
.profile-role {
  margin: 0;                                 /* 无边距 */
  color: var(--text-secondary);             /* 次要文本颜色 */
  font-size: 14px;                          /* 字体大小 */
}
</style>