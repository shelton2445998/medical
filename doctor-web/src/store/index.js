/**
 * 状态管理文件
 * 
 * 基于 Vue 3 的响应式系统实现的轻量级状态管理
 * 管理医生登录状态、用户信息等全局状态
 * 
 * @author 医生端项目组
 * @date 2024
 * @version 1.0.0
 */

import { reactive } from 'vue'
import { getDoctorInfo } from '@/api/doctor'

/**
 * 应用全局状态对象
 * 
 * 使用 Vue 3 的 reactive 创建响应式状态
 */
const state = reactive({
  doctor: null,        // 医生信息对象
  loading: false,      // 加载状态标识
  initialized: false   // 初始化状态标识
})

/**
 * 状态管理对象
 * 
 * 提供状态操作方法和数据访问接口
 */
const store = {
  // 暴露状态对象
  state,
  
  /**
   * 获取医生信息方法
   * 
   * 从服务器获取当前登录医生的详细信息
   * 包含缓存机制，避免重复请求
   * 
   * @returns {Promise<Object|null>} 返回医生信息对象，失败时返回 null
   */
  async getDoctorInfo() {
    // 如果已有医生信息且已初始化，直接返回缓存的数据
    if (state.doctor && state.initialized) {
      return state.doctor
    }
    
    // 检查本地存储中是否有有效的 token
    const token = localStorage.getItem('doctorToken')
    if (!token) {
      return null
    }
    
    // 设置加载状态
    state.loading = true
    
    try {
      // 调用 API 获取医生信息
      const res = await getDoctorInfo()
      
      if (res.code === 200) {
        // 请求成功，更新状态
        state.doctor = res.data
        state.initialized = true
      } else {
        // 请求失败，清空医生信息
        state.doctor = null
      }
    } catch (error) {
      // 捕获异常，记录错误日志
      console.error('获取医生信息失败:', error)
      state.doctor = null
    } finally {
      // 重置加载状态
      state.loading = false
    }
    
    return state.doctor
  },
  
  /**
   * 清除医生信息方法
   * 
   * 用于用户退出登录时清理所有相关状态和本地存储
   */
  clearDoctor() {
    // 清空状态
    state.doctor = null
    state.initialized = false
    
    // 清除本地存储
    localStorage.removeItem('doctorToken')
    localStorage.removeItem('doctorInfo')
  },
  
  /**
   * 检查登录状态方法
   * 
   * 通过检查本地存储中的 token 来判断用户是否已登录
   * 
   * @returns {boolean} 返回是否已登录的布尔值
   */
  isLoggedIn() {
    return !!localStorage.getItem('doctorToken')
  },
  
  /**
   * 获取认证 token 方法
   * 
   * 从本地存储获取医生登录 token
   * 
   * @returns {string} 返回 token 字符串，未找到时返回空字符串
   */
  getToken() {
    return localStorage.getItem('doctorToken') || ''
  }
}

// 导出状态管理实例
export default store 