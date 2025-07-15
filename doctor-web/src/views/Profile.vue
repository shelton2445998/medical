<template>
  <div class="profile-container page-container">
    <div class="page-header">
      <h2 class="page-title">个人设置</h2>
      <p class="page-subtitle">管理您的账户和个人信息</p>
    </div>
    
    <el-row :gutter="24">
      <el-col :xs="24" :sm="8" :md="8" :lg="8">
        <el-card class="profile-card custom-card" shadow="hover">
          <div class="profile-info">
            <h2 class="doctor-name">{{ doctorInfo.name || '未知' }}</h2>
            <p class="doctor-title">{{ doctorInfo.title || '医生' }}</p>
            <div class="doctor-workplace">
              <el-icon><OfficeBuilding /></el-icon>
              <span>{{ doctorInfo.hospitalName || '医院' }}</span>
            </div>
            <div class="doctor-department">
              <el-icon><House /></el-icon>
              <span>{{ doctorInfo.departmentName || '科室' }}</span>
            </div>
          </div>
          
          <el-divider />
          
          <!-- <div class="stats-container">
            <div class="stat-item">
              <div class="stat-value">{{ todayAppointments }}</div>
              <div class="stat-label">今日预约</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ pendingReports }}</div>
              <div class="stat-label">待处理报告</div>
            </div>
          </div> -->
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="16" :md="16" :lg="16">
        <el-tabs v-model="activeTab" class="custom-tabs">
          <el-tab-pane label="个人资料" name="basic">
            <el-card class="custom-card" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>基本信息</span>
                </div>
              </template>
              
              <el-form
                ref="profileFormRef"
                :model="profileForm"
                :rules="profileRules"
                label-width="100px"
                class="profile-form"
              >
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="profileForm.name" placeholder="请输入姓名" />
                </el-form-item>
                
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="profileForm.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="0">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                
                <el-form-item label="手机号" prop="mobile">
                  <el-input v-model="profileForm.mobile" placeholder="请输入手机号" />
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                
                <el-form-item label="所属医院">
                  <el-input v-model="doctorInfo.hospitalName" disabled placeholder="未分配医院" />
                </el-form-item>
                
                <el-form-item label="所属科室">
                  <el-input v-model="doctorInfo.departmentName" disabled placeholder="未分配科室" />
                </el-form-item>
                
                <el-form-item label="职称" prop="title">
                  <el-input v-model="profileForm.title" placeholder="请输入职称" />
                </el-form-item>
                
                <el-form-item label="简介" prop="introduction">
                  <el-input
                    v-model="profileForm.introduction"
                    type="textarea"
                    rows="4"
                    placeholder="请输入个人简介"
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="updateProfile">
                    <el-icon><Check /></el-icon>保存
                  </el-button>
                  <el-button @click="refreshDoctorInfo">
                    <el-icon><Refresh /></el-icon>刷新
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="修改密码" name="password">
            <el-card class="custom-card" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>密码安全</span>
                </div>
              </template>
              
              <el-form
                ref="passwordFormRef"
                :model="passwordForm"
                :rules="passwordRules"
                label-width="100px"
                class="password-form"
              >
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    show-password
                    placeholder="请输入原密码"
                  />
                </el-form-item>
                
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    show-password
                    placeholder="请输入新密码"
                  />
                </el-form-item>
                
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    show-password
                    placeholder="请确认新密码"
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="handleUpdatePassword">
                    <el-icon><Key /></el-icon>修改密码
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDoctorInfo, updateDoctorProfile, updatePassword } from '@/api/doctor'
import { Check, Refresh, Key, OfficeBuilding, House } from '@element-plus/icons-vue'

export default {
  name: 'Profile',
  components: {
    Check,
    Refresh,
    Key,
    OfficeBuilding,
    House
  },
  setup() {
    const activeTab = ref('basic')
    const doctorInfo = ref({
      id: '',
      name: '',
      gender: 1,
      mobile: '',
      email: '',
      hospitalName: '',
      departmentName: '', 
      title: '',
      introduction: ''
    })
    const profileFormRef = ref(null)
    const passwordFormRef = ref(null)
    const todayAppointments = ref(0)
    const pendingReports = ref(0)
    
    const profileForm = reactive({
      name: '',
      gender: 1,
      mobile: '',
      email: '',
      title: '',
      introduction: ''
    })
    
    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
    
    const profileRules = {
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
      ],
      mobile: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      title: [
        { required: true, message: '请输入职称', trigger: 'blur' }
      ],
      introduction: [
        { max: 500, message: '简介不能超过500个字符', trigger: 'blur' }
      ]
    }
    
    const passwordRules = {
      oldPassword: [
        { required: true, message: '请输入原密码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== passwordForm.newPassword) {
              callback(new Error('两次输入密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }
    
    // 获取医生信息
    const fetchDoctorInfo = async () => {
      try {
        const res = await getDoctorInfo()
        
        if (res && res.data) {
          // 定义从医生角色编码获取医院和科室的函数
          const getDoctorHospitalAndDept = (roleCode) => {
            if (roleCode === 'doctor') {
              return {
                hospitalName: '第一人民医院',
                departmentName: '外科'
              }
            }
            return {
              hospitalName: '',
              departmentName: ''
            }
          }
          
          // 获取医院和科室信息
          const { hospitalName, departmentName } = getDoctorHospitalAndDept(res.data.roleCode)
          
          // 映射API返回的字段到doctorInfo对象
          doctorInfo.value = {
            id: res.data.userId || '',
            name: res.data.nickname || res.data.username || '',
            gender: res.data.gender !== undefined ? res.data.gender : 1,
            mobile: res.data.phone || '',
            email: res.data.email || '',
            hospitalName: hospitalName,
            departmentName: departmentName,
            title: res.data.title || '医师',
            introduction: res.data.introduction || '暂无简介',
            todayAppointments: res.data.todayAppointments || 0,
            pendingReports: res.data.pendingReports || 0
          }
          
          // 填充表单
          profileForm.name = doctorInfo.value.name
          profileForm.gender = doctorInfo.value.gender
          profileForm.mobile = doctorInfo.value.mobile
          profileForm.email = doctorInfo.value.email
          profileForm.title = doctorInfo.value.title || ''
          profileForm.introduction = doctorInfo.value.introduction || ''
          
          // 同步更新数量指标
          todayAppointments.value = doctorInfo.value.todayAppointments
          pendingReports.value = doctorInfo.value.pendingReports
        } else {
          ElMessage.warning('获取数据结构异常')
        }
      } catch (error) {
        console.error('获取医生信息失败', error)
        ElMessage.error('获取医生信息失败')
      }
    }
    
    // 更新个人资料
    const updateProfile = async () => {
      if (!profileFormRef.value) return
      
      await profileFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            await updateDoctorProfile(profileForm)
            ElMessage.success('个人资料更新成功')
            fetchDoctorInfo()
          } catch (error) {
            console.error('更新个人资料失败', error)
            ElMessage.error('更新个人资料失败')
          }
        } else {
          return false
        }
      })
    }
    
    // 修改密码
    const handleUpdatePassword = async () => {
      if (!passwordFormRef.value) return
      
      await passwordFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            await updatePassword({
              oldPassword: passwordForm.oldPassword,
              newPassword: passwordForm.newPassword
            })
            ElMessage.success('密码修改成功')
            passwordForm.oldPassword = ''
            passwordForm.newPassword = ''
            passwordForm.confirmPassword = ''
            activeTab.value = 'basic'
          } catch (error) {
            console.error('修改密码失败', error)
            ElMessage.error(error.message || '修改密码失败')
          }
        } else {
          return false
        }
      })
    }
    
    // 刷新医生信息
    const refreshDoctorInfo = () => {
      ElMessage.info('正在刷新信息...')
      fetchDoctorInfo().then(() => {
        ElMessage.success('信息刷新成功')
      }).catch(error => {
        console.error('刷新信息失败', error)
        ElMessage.error('刷新信息失败')
      })
    }
    
    onMounted(() => {
      fetchDoctorInfo().catch(error => {
        console.error('获取医生信息失败', error)
        ElMessage.error('获取医生信息失败')
      })
    })
    
    return {
      activeTab,
      doctorInfo,
      profileForm,
      passwordForm,
      profileFormRef,
      passwordFormRef,
      profileRules,
      passwordRules,
      todayAppointments,
      pendingReports,
      updateProfile,
      handleUpdatePassword,
      refreshDoctorInfo
    }
  }
}
</script>

<style scoped lang="scss">
.profile-container {
  min-height: calc(100vh - 84px);
}

.profile-card {
  height: 100%;

  .profile-info {
    text-align: center;
    padding: 20px 0;

    .doctor-name {
      margin: 0 0 10px;
      font-size: 24px;
      font-weight: 600;
      color: var(--text-primary);
    }

    .doctor-title {
      font-size: 16px;
      color: var(--text-secondary);
      margin: 0 0 16px;
    }

    .doctor-workplace,
    .doctor-department {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 8px;
      color: var(--text-regular);
      
      .el-icon {
        margin-right: 8px;
        color: var(--primary-color);
      }
    }
  }
}

.stats-container {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;

  .stat-item {
    text-align: center;
  }

  .stat-value {
    font-size: 24px;
    font-weight: 600;
    color: var(--primary-color);
  }

  .stat-label {
    font-size: 14px;
    color: var(--text-secondary);
    margin-top: 4px;
  }
}

.custom-card {
  margin-bottom: 24px;
  
  .card-header {
    font-size: 16px;
    font-weight: 600;
  }
}

.profile-form,
.password-form {
  padding: 20px 0;
  max-width: 500px;
}

@media (max-width: 768px) {
  .profile-card {
    margin-bottom: 24px;
  }
}
</style> 