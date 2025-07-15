<template>
  <div class="home-container">
    <el-container class="main-container">
      <el-aside :width="sidebarCollapsed ? '64px' : '220px'" class="aside">
        <div class="logo">
          <img src="../assets/images/new_logo.png" alt="Logo" />
          <span v-if="!sidebarCollapsed">医生工作站</span>
        </div>
        <el-menu 
          :default-active="activeMenu" 
          router
          :collapse="sidebarCollapsed" 
          background-color="#304156" 
          text-color="#bfcbd9"
          active-text-color="#3E7BFA"
          class="sidebar-menu">
          <el-menu-item index="/home/dashboard">
            <el-icon><icon-menu /></el-icon>
            <template #title>工作台</template>
          </el-menu-item>
          <!-- <el-menu-item index="/home/patients">
            <el-icon><user /></el-icon>
            <template #title>患者管理</template>
          </el-menu-item> -->
          <!-- <el-menu-item index="/home/appointments">
            <el-icon><calendar /></el-icon>
            <template #title>预约管理</template>
          </el-menu-item> -->
          <el-menu-item index="/home/examination-reports">
            <el-icon><document-checked /></el-icon>
            <template #title>体检报告</template>
          </el-menu-item>
          <!-- <el-menu-item index="/home/medical-records">
            <el-icon><document /></el-icon>
            <template #title>病历管理</template>
          </el-menu-item>
          <el-menu-item index="/home/prescriptions">
            <el-icon><tickets /></el-icon>
            <template #title>处方管理</template>
          </el-menu-item> -->
          <el-menu-item index="/home/schedule">
            <el-icon><timer /></el-icon>
            <template #title>排班管理</template>
          </el-menu-item>
          <el-menu-item index="/home/profile">
            <el-icon><setting /></el-icon>
            <template #title>个人设置</template>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header class="header">
          <div class="header-left">
            <el-icon class="menu-toggle" @click="toggleSidebar">
              <component :is="sidebarCollapsed ? 'Expand' : 'Fold'" />
            </el-icon>
          </div>
          <div class="header-right">
            <div class="header-actions">
              <el-badge :value="3" :max="99" class="notification-badge" type="primary">
                <el-icon class="header-icon"><bell /></el-icon>
              </el-badge>
              <el-icon class="header-icon ml-10"><message-box /></el-icon>
            </div>
            <el-divider direction="vertical" class="header-divider" />
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
        
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
    
    <!-- 个人信息对话框 -->
    <el-dialog v-model="personalInfoDialogVisible" title="个人信息" width="500px">
      <div class="profile-dialog-content">
        <div class="profile-header">
          <div class="profile-avatar">{{ doctorInfo.name ? doctorInfo.name.charAt(0) : 'D' }}</div>
          <h3 class="profile-name">{{ doctorInfo.name }}</h3>
          <p class="profile-role">{{ doctorInfo.title || '医生' }}</p>
        </div>
        <el-divider />
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
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
        </el-form-item>
      </el-form>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Menu as IconMenu, User, Calendar, Document, Tickets, Fold, ArrowDown, DocumentChecked, Timer, Setting, Bell, MessageBox, Key, SwitchButton, Expand } from '@element-plus/icons-vue'
import store from '@/store'
import { getDoctorInfo, doctorLogout, updatePassword } from '@/api/doctor'

export default {
  name: 'HomeView',
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
    const route = useRoute()
    const router = useRouter()
    const passwordFormRef = ref(null)
    const personalInfoDialogVisible = ref(false)
    const changePasswordDialogVisible = ref(false)
    const sidebarCollapsed = ref(false)

    // 当前活跃菜单
    const activeMenu = computed(() => route.path)

    // 医生信息
    const doctorInfo = reactive({
      name: '',
      id: '',
      hospitalName: '',
      departmentName: '',
      title: '',
      mobile: '',
      email: ''
    })

    // 修改密码表单
    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    // 密码表单校验规则
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

    // 初始化加载医生信息
    onMounted(async () => {
      await fetchDoctorInfo()
    })

    // 获取医生信息
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

    // 切换侧边栏
    const toggleSidebar = () => {
      sidebarCollapsed.value = !sidebarCollapsed.value
    }

    // 打开个人信息对话框
    const openPersonalInfo = () => {
      personalInfoDialogVisible.value = true
    }

    // 打开修改密码对话框
    const openChangePassword = () => {
      changePasswordDialogVisible.value = true
    }

    // 提交修改密码
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
            // 重置表单
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

    // 退出登录
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

    // 跳转到个人设置页面
    const goToProfilePage = () => {
      router.push('/home/profile')
    }
    
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

<style scoped>
.home-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.main-container {
  height: 100%;
}

.aside {
  background-color: #304156;
  transition: width var(--transition-normal);
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 10;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #263445;
  padding: 0 16px;
  overflow: hidden;
  transition: all var(--transition-normal);
}

.logo img {
  height: 32px;
  margin-right: 8px;
  transition: margin var(--transition-normal);
}

.sidebar-menu {
  border-right: none;
}

.header {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  z-index: 5;
  height: 60px !important;
}

.header-left {
  display: flex;
  align-items: center;
}

.menu-toggle {
  font-size: 20px;
  cursor: pointer;
  color: #595959;
  transition: color var(--transition-normal);
  padding: 8px;
  border-radius: 4px;
}

.menu-toggle:hover {
  color: var(--primary-color);
  background-color: rgba(0, 0, 0, 0.04);
}

.header-right {
  display: flex;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.header-icon {
  font-size: 18px;
  cursor: pointer;
  color: #595959;
  padding: 8px;
  border-radius: 4px;
  transition: all var(--transition-normal);
}

.header-icon:hover {
  color: var(--primary-color);
  background-color: rgba(0, 0, 0, 0.04);
}

.header-divider {
  height: 24px;
  margin: 0 16px;
}

.user-dropdown {
  cursor: pointer;
  margin-left: 8px;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color var(--transition-normal);
}

.user-info:hover {
  background-color: rgba(0, 0, 0, 0.04);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: var(--primary-color);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 500;
  margin-right: 8px;
}

.user-name {
  font-size: 14px;
  color: var(--text-regular);
  margin-right: 4px;
}

.main {
  background-color: #f0f2f5;
  padding: 24px;
  overflow-y: auto;
}

.notification-badge :deep(.el-badge__content) {
  top: 6px;
  right: 6px;
}

.profile-dialog-content {
  padding: 16px 0;
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.profile-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: var(--primary-color);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: 500;
  margin-bottom: 16px;
}

.profile-name {
  margin: 0 0 8px;
  font-size: 20px;
  font-weight: 600;
}

.profile-role {
  margin: 0;
  color: var(--text-secondary);
  font-size: 14px;
}
</style>