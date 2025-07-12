import { reactive } from 'vue'
import { getDoctorInfo } from '@/api/doctor'

// 创建一个响应式的状态对象
const state = reactive({
  doctor: null,
  loading: false,
  initialized: false
})

// 创建状态管理
const store = {
  state,
  
  // 获取医生信息
  async getDoctorInfo() {
    if (state.doctor && state.initialized) {
      return state.doctor
    }
    
    const token = localStorage.getItem('doctorToken')
    if (!token) {
      return null
    }
    
    state.loading = true
    try {
      const res = await getDoctorInfo()
      if (res.code === 200) {
        state.doctor = res.data
        state.initialized = true
      } else {
        state.doctor = null
      }
    } catch (error) {
      console.error('获取医生信息失败:', error)
      state.doctor = null
    } finally {
      state.loading = false
    }
    
    return state.doctor
  },
  
  // 清除医生信息（登出时使用）
  clearDoctor() {
    state.doctor = null
    state.initialized = false
    localStorage.removeItem('doctorToken')
    localStorage.removeItem('doctorInfo')
  },
  
  // 检查是否登录
  isLoggedIn() {
    return !!localStorage.getItem('doctorToken')
  },
  
  // 获取token
  getToken() {
    return localStorage.getItem('doctorToken') || ''
  }
}

export default store 