<!--
  医生个人设置页面组件
  
  提供医生个人信息管理和设置功能
  包含个人资料编辑、密码修改、头像上传等功能
  
  @author 医生端项目组
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="profile-container page-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2 class="page-title">个人设置</h2>
      <p class="page-subtitle">管理您的账户和个人信息</p>
    </div>
    
    <!-- 主要内容区域 -->
    <el-row :gutter="24">
      <!-- 左侧个人信息卡片 -->
      <el-col :xs="24" :sm="8" :md="8" :lg="8">
        <el-card class="profile-card custom-card" shadow="hover">
          <!-- 医生基本信息 -->
          <div class="profile-info">
            <h2 class="doctor-name">{{ doctorInfo.name || '未知' }}</h2>
            <p class="doctor-title">{{ doctorInfo.title || '医生' }}</p>
            
            <!-- 工作地点信息 -->
            <div class="doctor-workplace">
              <el-icon><OfficeBuilding /></el-icon>
              <span>{{ doctorInfo.hospitalName || '医院' }}</span>
            </div>
            
            <!-- 科室信息 -->
            <div class="doctor-department">
              <el-icon><House /></el-icon>
              <span>{{ doctorInfo.departmentName || '科室' }}</span>
            </div>
          </div>
          
          <el-divider />
          
          <!-- 统计信息（已注释） -->
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
      
      <!-- 右侧设置选项卡 -->
      <el-col :xs="24" :sm="16" :md="16" :lg="16">
        <el-tabs v-model="activeTab" class="custom-tabs">
          <!-- 个人资料标签页 -->
          <el-tab-pane label="个人资料" name="basic">
            <el-card class="custom-card" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>基本信息</span>
                </div>
              </template>
              
              <!-- 个人资料表单 -->
              <el-form
                ref="profileFormRef"
                :model="profileForm"
                :rules="profileRules"
                label-width="100px"
                class="profile-form"
              >
                <!-- 姓名输入 -->
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="profileForm.name" placeholder="请输入姓名" />
                </el-form-item>
                
                <!-- 性别选择 -->
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="profileForm.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="0">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                
                <!-- 手机号输入 -->
                <el-form-item label="手机号" prop="mobile">
                  <el-input v-model="profileForm.mobile" placeholder="请输入手机号" />
                </el-form-item>
                
                <!-- 邮箱输入 -->
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                
                <!-- 所属医院（只读） -->
                <el-form-item label="所属医院">
                  <el-input v-model="doctorInfo.hospitalName" disabled placeholder="未分配医院" />
                </el-form-item>
                
                <!-- 所属科室（只读） -->
                <el-form-item label="所属科室">
                  <el-input v-model="doctorInfo.departmentName" disabled placeholder="未分配科室" />
                </el-form-item>
                
                <!-- 职称输入 -->
                <el-form-item label="职称" prop="title">
                  <el-input v-model="profileForm.title" placeholder="请输入职称" />
                </el-form-item>
                
                <!-- 个人简介输入 -->
                <el-form-item label="简介" prop="introduction">
                  <el-input
                    v-model="profileForm.introduction"
                    type="textarea"
                    rows="4"
                    placeholder="请输入个人简介"
                  />
                </el-form-item>
                
                <!-- 表单操作按钮 -->
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
          
          <!-- 修改密码标签页 -->
          <el-tab-pane label="修改密码" name="password">
            <el-card class="custom-card" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>密码安全</span>
                </div>
              </template>
              
              <!-- 密码修改表单 -->
              <el-form
                ref="passwordFormRef"
                :model="passwordForm"
                :rules="passwordRules"
                label-width="100px"
                class="password-form"
              >
                <!-- 原密码输入 -->
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    show-password
                    placeholder="请输入原密码"
                  />
                </el-form-item>
                
                <!-- 新密码输入 -->
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    show-password
                    placeholder="请输入新密码"
                  />
                </el-form-item>
                
                <!-- 确认新密码输入 -->
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    show-password
                    placeholder="请确认新密码"
                  />
                </el-form-item>
                
                <!-- 密码修改按钮 -->
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
/**
 * 个人设置页面逻辑
 * 
 * 处理医生个人信息管理、密码修改等功能
 */

// 导入 Vue 3 组合式API相关函数
import { ref, reactive, onMounted } from 'vue'
// 导入 Element Plus 组件
import { ElMessage } from 'element-plus'
// 导入API接口函数
import { getDoctorInfo, updateDoctorProfile, updatePassword } from '@/api/doctor'
// 导入 Element Plus 图标组件
import { Check, Refresh, Key, OfficeBuilding, House } from '@element-plus/icons-vue'

/**
 * 个人设置组件导出
 * 
 * 定义组件的基本信息和逻辑
 */
export default {
  name: 'Profile',
  // 注册使用的图标组件
  components: {
    Check,
    Refresh,
    Key,
    OfficeBuilding,
    House
  },
  setup() {
    // 当前激活的选项卡
    const activeTab = ref('basic')
    
    /**
     * 医生信息对象
     * 
     * 存储医生的详细信息
     */
    const doctorInfo = ref({
      id: '',              // 医生ID
      name: '',            // 医生姓名
      gender: 1,           // 性别（1-男，0-女）
      mobile: '',          // 手机号
      email: '',           // 邮箱
      hospitalName: '',    // 医院名称
      departmentName: '',  // 科室名称
      title: '',           // 职称
      introduction: ''     // 个人简介
    })
    
    // 表单引用
    const profileFormRef = ref(null)     // 个人资料表单引用
    const passwordFormRef = ref(null)    // 密码修改表单引用
    
    // 统计数据
    const todayAppointments = ref(0)     // 今日预约数
    const pendingReports = ref(0)        // 待处理报告数
    
    /**
     * 个人资料表单数据
     * 
     * 可编辑的个人信息字段
     */
    const profileForm = reactive({
      name: '',            // 姓名
      gender: 1,           // 性别
      mobile: '',          // 手机号
      email: '',           // 邮箱
      title: '',           // 职称
      introduction: ''     // 个人简介
    })
    
    /**
     * 密码修改表单数据
     * 
     * 包含原密码和新密码字段
     */
    const passwordForm = reactive({
      oldPassword: '',     // 原密码
      newPassword: '',     // 新密码
      confirmPassword: ''  // 确认密码
    })
    
    /**
     * 个人资料表单验证规则
     * 
     * 定义各字段的验证规则
     */
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
    
    /**
     * 密码修改表单验证规则
     * 
     * 定义密码相关字段的验证规则
     */
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
          // 自定义验证器：确保两次输入的密码一致
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
    
    /**
     * 获取医生信息
     * 
     * 从服务器获取医生详细信息并填充到表单
     */
    const fetchDoctorInfo = async () => {
      try {
        const res = await getDoctorInfo()
        
        if (res && res.data) {
          /**
           * 根据医生角色编码获取医院和科室信息
           * 
           * @param {string} roleCode - 医生角色编码
           * @returns {Object} 包含医院和科室名称的对象
           */
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
          
          // 填充表单数据
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
    
    /**
     * 更新个人资料
     * 
     * 验证表单并提交个人资料更新请求
     */
    const updateProfile = async () => {
      if (!profileFormRef.value) return
      
      await profileFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            await updateDoctorProfile(profileForm)
            ElMessage.success('个人资料更新成功')
            // 更新成功后重新获取医生信息
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
    
    /**
     * 处理密码修改
     * 
     * 验证表单并提交密码修改请求
     */
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
            
            // 清空密码表单
            passwordForm.oldPassword = ''
            passwordForm.newPassword = ''
            passwordForm.confirmPassword = ''
            
            // 切换到基本信息选项卡
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
    
    /**
     * 刷新医生信息
     * 
     * 手动刷新医生信息数据
     */
    const refreshDoctorInfo = () => {
      ElMessage.info('正在刷新信息...')
      fetchDoctorInfo().then(() => {
        ElMessage.success('信息刷新成功')
      }).catch(error => {
        console.error('刷新信息失败', error)
        ElMessage.error('刷新信息失败')
      })
    }
    
    /**
     * 组件挂载时的初始化逻辑
     * 
     * 在组件挂载后自动获取医生信息
     */
    onMounted(() => {
      fetchDoctorInfo().catch(error => {
        console.error('获取医生信息失败', error)
        ElMessage.error('获取医生信息失败')
      })
    })
    
    // 返回模板需要的数据和方法
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

<!--
  个人设置页面样式
  
  定义个人设置页面的视觉设计和布局
  使用 SCSS 语法编写样式
-->
<style scoped lang="scss">
/* ==================== 页面容器样式 ==================== */

/**
 * 个人设置页面主容器
 * 
 * 设置页面的最小高度，扣除头部和导航高度
 */
.profile-container {
  min-height: calc(100vh - 84px);
}

/* ==================== 个人信息卡片样式 ==================== */

/**
 * 个人信息卡片样式
 * 
 * 左侧个人信息展示卡片
 */
.profile-card {
  height: 100%;

  /**
   * 个人信息内容区域
   * 
   * 医生基本信息的展示区域
   */
  .profile-info {
    text-align: center;                       /* 居中对齐 */
    padding: 20px 0;                         /* 垂直内边距 */

    /**
     * 医生姓名样式
     * 
     * 个人信息卡片中的医生姓名
     */
    .doctor-name {
      margin: 0 0 10px;                       /* 底部边距 */
      font-size: 24px;                       /* 字体大小 */
      font-weight: 600;                       /* 字体粗细 */
      color: var(--text-primary);            /* 主要文本颜色 */
    }

    /**
     * 医生职位样式
     * 
     * 个人信息卡片中的医生职位
     */
    .doctor-title {
      font-size: 16px;                       /* 字体大小 */
      color: var(--text-secondary);          /* 次要文本颜色 */
      margin: 0 0 16px;                      /* 底部边距 */
    }

    /**
     * 医生工作地点和科室样式
     * 
     * 显示医生的工作地点和科室信息
     */
    .doctor-workplace,
    .doctor-department {
      display: flex;                          /* 弹性布局 */
      align-items: center;                    /* 垂直居中 */
      justify-content: center;                /* 水平居中 */
      margin-bottom: 8px;                     /* 底部边距 */
      color: var(--text-regular);            /* 常规文本颜色 */
      
      /**
       * 图标样式
       * 
       * 工作地点和科室前的图标
       */
      .el-icon {
        margin-right: 8px;                    /* 右边距 */
        color: var(--primary-color);          /* 主色调 */
      }
    }
  }
}

/* ==================== 统计信息样式 ==================== */

/**
 * 统计信息容器样式
 * 
 * 显示医生的统计数据（已注释）
 */
.stats-container {
  display: flex;                              /* 弹性布局 */
  justify-content: space-around;              /* 均匀分布 */
  margin: 20px 0;                            /* 垂直边距 */

  /**
   * 统计项样式
   * 
   * 单个统计指标的样式
   */
  .stat-item {
    text-align: center;                       /* 居中对齐 */
  }

  /**
   * 统计数值样式
   * 
   * 统计指标的数值部分
   */
  .stat-value {
    font-size: 24px;                         /* 字体大小 */
    font-weight: 600;                         /* 字体粗细 */
    color: var(--primary-color);             /* 主色调 */
  }

  /**
   * 统计标签样式
   * 
   * 统计指标的标签部分
   */
  .stat-label {
    font-size: 14px;                         /* 字体大小 */
    color: var(--text-secondary);            /* 次要文本颜色 */
    margin-top: 4px;                          /* 顶部边距 */
  }
}

/* ==================== 自定义卡片样式 ==================== */

/**
 * 自定义卡片样式
 * 
 * 统一的卡片样式设置
 */
.custom-card {
  margin-bottom: 24px;                        /* 底部边距 */
  
  /**
   * 卡片头部样式
   * 
   * 卡片标题的样式设置
   */
  .card-header {
    font-size: 16px;                          /* 字体大小 */
    font-weight: 600;                          /* 字体粗细 */
  }
}

/* ==================== 表单样式 ==================== */

/**
 * 表单样式
 * 
 * 个人资料表单和密码表单的统一样式
 */
.profile-form,
.password-form {
  padding: 20px 0;                            /* 垂直内边距 */
  max-width: 500px;                           /* 最大宽度 */
}

/* ==================== 响应式设计 ==================== */

/**
 * 移动端适配
 * 
 * 在小屏幕设备上的布局调整
 */
@media (max-width: 768px) {
  .profile-card {
    margin-bottom: 24px;                      /* 底部边距 */
  }
}
</style> 