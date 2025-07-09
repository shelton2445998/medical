<template>
  <div class="dashboard-container">
    <h2 class="page-title">工作台</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb-20">
<el-col :span="6">
  <el-card shadow="hover" class="stat-card">
    <el-icon class="stat-full-icon waiting-icon"><clock /></el-icon>
    <div class="stat-content">
      <div class="stat-label">今日待诊人数</div>
	  <div class="stat-value">{{ statistics.waitingPatients }}</div>
    </div>
  </el-card>
</el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
		   <el-icon class="stat-full-icon completed-icon"><circle-check /></el-icon>
          <div class="stat-content">
            <div class="stat-label">今日已诊人数</div>
			<div class="stat-value">{{ statistics.completedPatients }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
		  <el-icon class="stat-full-icon appointment-icon"><calendar /></el-icon>
          <div class="stat-content">
            <div class="stat-label">今日总预约数</div>
			<div class="stat-value">{{ statistics.totalAppointments }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <el-icon class="stat-full-icon prescription-icon"><tickets /></el-icon>
          <div class="stat-content">
            <div class="stat-label">今日处方数</div>
			<div class="stat-value">{{ statistics.totalPrescriptions }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 待诊患者列表 -->
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>待诊患者</span>
          <el-button type="primary" size="small" @click="handleRefresh">刷新</el-button>
        </div>
      </template>
      <el-table :data="waitingPatients" style="width: 100%" v-loading="loading">
        <el-table-column prop="appointmentId" label="预约编号" width="100" />
        <el-table-column prop="patientName" label="患者姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 'male' ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="appointmentTime" label="预约时间" width="180" />
        <el-table-column prop="symptoms" label="症状描述" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="startDiagnosis(scope.row)">开始诊断</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="waitingPatients.length === 0 && !loading" class="empty-data">
        暂无待诊患者
      </div>
    </el-card>
    
    <!-- 通知公告 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>通知公告</span>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="(activity, index) in activities"
          :key="index"
          :timestamp="activity.timestamp"
          :type="activity.type"
        >
          {{ activity.content }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Clock, CircleCheck, Calendar, Tickets } from '@element-plus/icons-vue'
import axios from 'axios'

// 添加JWT认证请求拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 添加响应拦截器处理错误码
axios.interceptors.response.use(response => {
  return response
}, error => {
  const { response } = error
  if (response) {
    switch(response.status) {
      case 401:
        ElMessage.error('未授权，请登录')
        window.location.href = '/login'
        break
      case 403:
        ElMessage.error('权限不足，禁止访问')
        break
      case 404:
        ElMessage.error('请求的资源不存在')
        break
      case 500:
        ElMessage.error('服务器内部错误')
        break
    }
  } else {
    ElMessage.error('网络异常')
  }
  return Promise.reject(error)
})

export default {
  name: 'DashboardView',
  components: {
    Clock,
    CircleCheck,
    Calendar,
    Tickets
  },
  setup() {
    const router = useRouter()
    const loading = ref(false)
    
    // 统计数据
    const statistics = reactive({
      waitingPatients: 0,
      completedPatients: 0,
      totalAppointments: 0,
      totalPrescriptions: 0
    })
    
    // 待诊患者列表
    const waitingPatients = ref([])
    
    // 通知公告
    const activities = ref([
      {
        content: '今日医院全体会议将在下午15:00在三楼会议室召开，请准时参加。',
        timestamp: '2023-08-15 09:00',
        type: 'primary'
      },
      {
        content: '请各位医生及时完成本周病历记录，并上传至系统。',
        timestamp: '2023-08-14 10:30',
        type: 'success'
      },
      {
        content: '系统将于今晚22:00-23:00进行维护升级，请合理安排工作。',
        timestamp: '2023-08-13 16:20',
        type: 'warning'
      },
      {
        content: '新冠疫苗接种工作将在本周持续进行，请各位医生做好防护。',
        timestamp: '2023-08-12 14:45',
        type: 'info'
      }
    ])
    
    // 加载数据
    onMounted(() => {
      fetchDashboardData()
    })
    
    // 获取工作台数据（仅修改接口相关部分）
    const fetchDashboardData = async () => {
      loading.value = true
      try {
        // 1. 获取统计数据（使用医生端接口路径规范）
        const { data: statsRes } = await axios.get('/api/doctor/dashboard')
        if (statsRes.code === 200) {
          Object.assign(statistics, statsRes.data)
        }
        
        // 2. 获取待诊患者列表（复用医生端预约列表接口，筛选状态为待确认的预约）
        const { data: patientsRes } = await axios.get('/api/doctor/appointment/list', {
          params: {
            status: 'waiting',
            pageNum: 1,
            pageSize: 100
          }
        })
        if (patientsRes.code === 200) {
          waitingPatients.value = patientsRes.data.list
        }
      } catch (error) {
        console.error('获取工作台数据失败:', error)
        // 使用模拟数据
        mockData()
        ElMessage.warning('获取数据失败，显示模拟数据')
      } finally {
        loading.value = false
      }
    }
    
    // 模拟数据
    const mockData = () => {
      statistics.waitingPatients = 5
      statistics.completedPatients = 12
      statistics.totalAppointments = 17
      statistics.totalPrescriptions = 14
      
      waitingPatients.value = [
        {
          appointmentId: 'AP20230815001',
          patientName: '张三',
          gender: 'male',
          age: 45,
          appointmentTime: '2023-08-15 10:30',
          symptoms: '头痛，发热，全身乏力'
        },
        {
          appointmentId: 'AP20230815003',
          patientName: '李四',
          gender: 'male',
          age: 32,
          appointmentTime: '2023-08-15 11:00',
          symptoms: '咳嗽，咽喉疼痛'
        },
        {
          appointmentId: 'AP20230815005',
          patientName: '王五',
          gender: 'male',
          age: 28,
          appointmentTime: '2023-08-15 11:30',
          symptoms: '腹痛，腹泻'
        },
        {
          appointmentId: 'AP20230815007',
          patientName: '赵六',
          gender: 'female',
          age: 52,
          appointmentTime: '2023-08-15 14:00',
          symptoms: '关节疼痛，行走困难'
        },
        {
          appointmentId: 'AP20230815009',
          patientName: '钱七',
          gender: 'female',
          age: 35,
          appointmentTime: '2023-08-15 14:30',
          symptoms: '皮疹，瘙痒'
        }
      ]
    }
    
    // 刷新数据
    const handleRefresh = () => {
      fetchDashboardData()
    }
    
    // 开始诊断
    const startDiagnosis = (patient) => {
      ElMessage.success(`开始为患者 ${patient.patientName} 进行诊断`)
      router.push({
        path: '/home/medical-records',
        query: {
          appointmentId: patient.appointmentId,
          patientName: patient.patientName
        }
      })
    }
    
    return {
      loading,
      statistics,
      waitingPatients,
      activities,
      handleRefresh,
      startDiagnosis
    }
  }
}
</script>

<style scoped>
.stat-card {
  position: relative; 
  height: 100%;
  padding: 0; 
  overflow: hidden; 
  border-radius: 12px;
}

.stat-full-icon {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%; 
  height: 100%; 
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0.4 ; 
  font-size: 120px; 
  color: currentColor; 
}

.waiting-icon {
  
  color: black; 
}
.completed-icon{
	 color: black;
}
.appointment-icon{
	color: black;
}
.prescription-icon{
	color: black;
}

.stat-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative; 
  z-index: 1;      
  padding: 20px;
  
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #000000;

}

.waiting-icon {
  background-color: #409EFF;
}
.completed-icon {
  background-color: #67C23A;
}
.appointment-icon {
  background-color: #e6d6c2;
}
.prescription-icon {
  background-color: #ffd4fb;
}
</style>