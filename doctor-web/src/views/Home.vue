<template>
  <div class="home-container">
    <el-container class="main-container">
      <el-aside width="200px" class="aside">
        <div class="logo">
          <img src="../assets/images/logo.png" alt="Logo" />
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
        <el-form-item label="工号">{{ doctorInfo.doctorId }}</el-form-item>
        <el-form-item label="科室">{{ doctorInfo.department }}</el-form-item>
        <el-form-item label="职称">{{ doctorInfo.title }}</el-form-item>
        <el-form-item label="联系电话">{{ doctorInfo.phone }}</el-form-item>
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
import axios from 'axios'

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
      doctorId: '',
      department: '',
      title: '',
      phone: ''
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
    onMounted(() => {
      const storedInfo = localStorage.getItem('doctorInfo')
      if (storedInfo) {
        try {
          const info = JSON.parse(storedInfo)
          Object.assign(doctorInfo, info)
        } catch (error) {
          console.error('解析医生信息出错：', error)
        }
      } else {
        // 如果本地没有缓存，则请求获取
        getDoctorInfo()
      }
    })

    // 获取医生信息
    const getDoctorInfo = async () => {
      try {
        const { data: res } = await axios.get('/doctor/info')
        if (res.code === 200) {
          Object.assign(doctorInfo, res.data)
          localStorage.setItem('doctorInfo', JSON.stringify(res.data))
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
            const { data: res } = await axios.post('/doctor/change-password', {
              oldPassword: passwordForm.oldPassword,
              newPassword: passwordForm.newPassword
            })
            if (res.code === 200) {
              ElMessage.success('密码修改成功，请重新登录')
              changePasswordDialogVisible.value = false
              handleLogout()
            } else {
              ElMessage.error(res.message || '密码修改失败')
            }
          } catch (error) {
            console.error('修改密码出错：', error)
            ElMessage.error('密码修改失败，请稍后再试')
          }
        }
      })
    }

    // 退出登录
    const handleLogout = () => {
      ElMessageBox.confirm('确认退出登录吗？', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('doctorToken')
        localStorage.removeItem('doctorInfo')
        router.push('/')
        ElMessage.success('已退出登录')
      }).catch(() => {})
    }

    return {
      activeMenu,
      doctorInfo,
      passwordForm,
      passwordRules,
      passwordFormRef,
      personalInfoDialogVisible,
      changePasswordDialogVisible,
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
  overflow: hidden;
}

.main-container {
  height: 100%;
}

.aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  font-weight: bold;
  padding: 10px 0;
  margin-bottom: 10px;
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.menu-toggle {
  font-size: 20px;
  cursor: pointer;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.main {
  padding: 20px;
  background-color: #f0f2f5;
  overflow-y: auto;
}

:deep(.el-menu) {
  border-right: none;
}
</style> 