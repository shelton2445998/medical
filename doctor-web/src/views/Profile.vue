<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="profile-card">
          <div class="profile-header">
            <div class="avatar-container">
              <el-avatar :size="100" :src="doctorInfo.avatar">
                {{ doctorInfo.name ? doctorInfo.name.charAt(0) : 'D' }}
              </el-avatar>
            </div>
            <div class="upload-avatar">
              <el-upload
                class="avatar-uploader"
                action="/api/doctor/upload-avatar"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <el-button size="small">更换头像</el-button>
              </el-upload>
            </div>
          </div>
          
          <div class="profile-info">
            <h2>{{ doctorInfo.name || '未知' }}</h2>
            <p>{{ doctorInfo.title || '医生' }}</p>
            <p>{{ doctorInfo.hospitalName || '医院' }} · {{ doctorInfo.departmentName || '科室' }}</p>
          </div>
          
          <div class="quick-info">
            <div class="info-item">
              <span class="label">今日预约</span>
              <span class="value">{{ todayAppointments }}</span>
            </div>
            <el-divider direction="vertical" />
            <div class="info-item">
              <span class="label">待完成报告</span>
              <span class="value">{{ pendingReports }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="个人资料" name="basic">
            <el-card>
              <el-form
                ref="profileFormRef"
                :model="profileForm"
                :rules="profileRules"
                label-width="100px"
              >
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="profileForm.name" />
                </el-form-item>
                
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="profileForm.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="0">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                
                <el-form-item label="手机号" prop="mobile">
                  <el-input v-model="profileForm.mobile" />
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="profileForm.email" />
                </el-form-item>
                
                <el-form-item label="所属医院">
                  <el-input v-model="doctorInfo.hospitalName" disabled placeholder="未分配医院" />
                </el-form-item>
                
                <el-form-item label="所属科室">
                  <el-input v-model="doctorInfo.departmentName" disabled placeholder="未分配科室" />
                </el-form-item>
                
                <el-form-item label="职称" prop="title">
                  <el-input v-model="profileForm.title" />
                </el-form-item>
                
                <el-form-item label="简介" prop="introduction">
                  <el-input
                    v-model="profileForm.introduction"
                    type="textarea"
                    rows="4"
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="updateProfile">保存</el-button>
                  <el-button type="info" @click="setMockData">加载测试数据</el-button>
                  <el-button type="warning" @click="refreshDoctorInfo">刷新数据</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="修改密码" name="password">
            <el-card>
              <el-form
                ref="passwordFormRef"
                :model="passwordForm"
                :rules="passwordRules"
                label-width="100px"
              >
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    show-password
                  />
                </el-form-item>
                
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    show-password
                  />
                </el-form-item>
                
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    show-password
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="handleUpdatePassword">修改密码</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>
        </el-tabs>
        
        <!-- 调试区域 -->
        <el-button type="info" size="small" @click="showDebug = !showDebug" style="margin-top: 20px;">
          {{ showDebug ? '隐藏调试信息' : '显示调试信息' }}
        </el-button>
        <el-card v-if="showDebug" style="margin-top: 10px;">
          <h3>调试信息</h3>
          <pre>{{ JSON.stringify(doctorInfo, null, 2) }}</pre>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getDoctorInfo, updateDoctorProfile, updatePassword } from '@/api/doctor'

export default {
  name: 'Profile',
  setup() {
    const activeTab = ref('basic')
    const showDebug = ref(false)
    const doctorInfo = ref({
      id: '',
      name: '',
      gender: 1,
      mobile: '',
      email: '',
      hospitalName: '',
      departmentName: '', 
      title: '',
      introduction: '',
      avatar: ''
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
        console.log('获取到的医生信息原始数据:', res)
        
        if (res && res.data) {
          // 定义从医生角色编码获取医院和科室的函数
          const getDoctorHospitalAndDept = (roleCode) => {
            if (roleCode === 'doctor') {
              return {
                hospitalName: '第一人民医院', // 临时硬编码，实际应从API获取
                departmentName: '外科'        // 临时硬编码，实际应从API获取
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
            title: res.data.title || '医师', // 默认职称
            introduction: res.data.introduction || '暂无简介',
            avatar: res.data.head || '',
            todayAppointments: res.data.todayAppointments || 0,
            pendingReports: res.data.pendingReports || 0
          }
          
          console.log('映射后的医生信息:', doctorInfo.value)
          
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
          console.error('API返回数据结构异常:', res)
          ElMessage.warning('获取数据结构异常，将使用模拟数据')
          setMockData()
        }
      } catch (error) {
        console.error('获取医生信息失败', error)
        ElMessage.error('获取医生信息失败')
        setMockData()
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
    
    // 上传头像前的验证
    const beforeAvatarUpload = (file) => {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!isJPG && !isPNG) {
        ElMessage.error('头像只能是 JPG 或 PNG 格式!')
      }
      if (!isLt2M) {
        ElMessage.error('头像大小不能超过 2MB!')
      }
      return (isJPG || isPNG) && isLt2M
    }
    
    // 头像上传成功回调
    const handleAvatarSuccess = (res) => {
      if (res.code === 200) {
        doctorInfo.value.avatar = res.data
        ElMessage.success('头像上传成功')
        fetchDoctorInfo()
      } else {
        ElMessage.error(res.message || '头像上传失败')
      }
    }
    
    // 在模板中添加调试展示区域
    onMounted(() => {
      // 显示已加载的提示信息
      console.log('Profile页面已加载，初始doctorInfo:', doctorInfo.value)
      ElMessage.info('个人设置页面已加载，正在获取数据...')
      
      fetchDoctorInfo().catch(error => {
        console.error('获取医生信息失败，使用模拟数据', error)
        // 如果获取数据失败，使用模拟数据
        setMockData()
      })
    })
    
    // 调试函数：如果API返回数据不正确，使用模拟数据进行测试
    const setMockData = () => {
      // 设置全面的模拟数据
      doctorInfo.value = {
        id: "3002",
        name: "李医生",
        gender: 0,
        mobile: "13900002222",
        email: "li@medical.com",
        hospitalName: "第一人民医院",
        departmentName: "外科",
        title: "副主任医师",
        introduction: "外科专家，擅长微创手术",
        avatar: "",
        todayAppointments: 5,
        pendingReports: 3
      }
      
      console.log('使用模拟数据:', doctorInfo.value)
      
      // 填充表单
      profileForm.name = doctorInfo.value.name
      profileForm.gender = doctorInfo.value.gender
      profileForm.mobile = doctorInfo.value.mobile
      profileForm.email = doctorInfo.value.email
      profileForm.title = doctorInfo.value.title
      profileForm.introduction = doctorInfo.value.introduction
      
      // 设置统计指标
      todayAppointments.value = doctorInfo.value.todayAppointments
      pendingReports.value = doctorInfo.value.pendingReports
      
      // 强制更新视图
      setTimeout(() => {
        ElMessage.info('已加载模拟数据用于展示')
      }, 100)
    }

    // 刷新医生信息
    const refreshDoctorInfo = () => {
      ElMessage.info('正在刷新医生信息...')
      fetchDoctorInfo().then(() => {
        ElMessage.success('医生信息刷新成功')
      }).catch(error => {
        console.error('刷新医生信息失败', error)
        ElMessage.error('刷新医生信息失败')
      })
    }
    
    return {
      activeTab,
      showDebug,
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
      beforeAvatarUpload,
      handleAvatarSuccess,
      setMockData,
      refreshDoctorInfo
    }
  }
}
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

.profile-card {
  height: 100%;
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.avatar-container {
  margin-bottom: 10px;
}

.profile-info {
  text-align: center;
  margin: 20px 0;
}

.profile-info h2 {
  margin-bottom: 5px;
  font-size: 20px;
}

.profile-info p {
  color: #606266;
  margin: 5px 0;
}

.quick-info {
  display: flex;
  justify-content: center;
  margin: 20px 0;
  padding: 10px 0;
  border-top: 1px solid #ebeef5;
}

.info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 20px;
}

.info-item .label {
  font-size: 14px;
  color: #909399;
}

.info-item .value {
  font-size: 20px;
  font-weight: bold;
  margin-top: 5px;
}
</style> 