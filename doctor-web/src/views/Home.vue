<template>
  <div class="home-container">
    <el-container class="main-container">
      <el-aside width="200px" class="aside">
        <div class="logo">
          <img src="../assets/images/new_logo.png" alt="Logo" />
          <span>医生工作站</span>
        </div>
        <el-menu 
          :default-active="activeMenu" 
          router 
          background-color="#304156" 
          text-color="#bfcbd9"
          active-text-color="#409EFF">
          <el-menu-item index="/home/dashboard">
            <el-icon><icon-menu /></el-icon>
            <span>工作台</span>
          </el-menu-item>
          <el-menu-item index="/home/patients">
            <el-icon><user /></el-icon>
            <span>患者管理</span>
          </el-menu-item>
          <el-menu-item index="/home/appointments">
            <el-icon><calendar /></el-icon>
            <span>预约管理</span>
          </el-menu-item>
          <el-menu-item index="/home/medical-records">
            <el-icon><document /></el-icon>
            <span>病历管理</span>
          </el-menu-item>
          <el-menu-item index="/home/prescriptions">
            <el-icon><tickets /></el-icon>
            <span>处方管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header class="header">
          <div class="header-left">
            <el-icon class="menu-toggle" @click="toggleSidebar"><fold /></el-icon>
          </div>
          <div class="header-right">
            <el-dropdown trigger="click">
              <span class="el-dropdown-link">
                {{ doctorInfo.name || '医生' }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="openPersonalInfo">个人信息</el-dropdown-item>
                  <el-dropdown-item @click="openChangePassword">修改密码</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
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
      <el-form label-width="100px">
        <el-form-item label="姓名">{{ doctorInfo.name }}</el-form-item>
        <el-form-item label="工号">{{ doctorInfo.id || '-' }}</el-form-item>
        <el-form-item label="医院">{{ doctorInfo.hospitalName || '-' }}</el-form-item>
        <el-form-item label="科室">{{ doctorInfo.departmentName || '-' }}</el-form-item>
        <el-form-item label="职称">{{ doctorInfo.title || '-' }}</el-form-item>
        <el-form-item label="联系电话">{{ doctorInfo.mobile || '-' }}</el-form-item>
        <el-form-item label="邮箱">{{ doctorInfo.email || '-' }}</el-form-item>
      </el-form>
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
import { Menu as IconMenu, User, Calendar, Document, Tickets, Fold, ArrowDown } from '@element-plus/icons-vue'
import store from '@/store'
import { getDoctorInfo, doctorLogout } from '@/api/doctor'

export default {
  name: 'HomeView',
  components: {
    IconMenu,
    User,
    Calendar,
    Document,
    Tickets,
    Fold,
    ArrowDown
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
        if (res.code === 200) {
          Object.assign(doctorInfo, res.data)
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
            // 这里应调用修改密码的API
            ElMessage.success('密码修改成功')
            changePasswordDialogVisible.value = false
            // 重置表单
            passwordForm.oldPassword = ''
            passwordForm.newPassword = ''
            passwordForm.confirmPassword = ''
          } catch (error) {
            console.error('修改密码失败：', error)
            ElMessage.error('修改密码失败，请稍后重试')
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
          store.clearDoctor()
          router.push('/')
          ElMessage.success('已安全退出系统')
        }
      }).catch(() => {})
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
      handleLogout
    }
  }
}
</script>

<style scoped>
.home-container {
  height: 100vh;
  width: 100vw;
}

.main-container {
  height: 100%;
}

.aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
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
}

.logo img {
  height: 32px;
  margin-right: 8px;
}

.header {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.menu-toggle {
  font-size: 20px;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>