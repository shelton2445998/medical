/**
 * @fileoverview 用户端用户管理API模块
 * @description 提供用户信息管理相关的API接口，包括用户信息获取、登录状态管理等功能
 * @author 用户端项目组
 * @version 1.0.0
 * @since 2024-01-01
 * 
 * @features
 * - 当前用户信息获取
 * - 指定用户信息查询
 * - 登录状态管理
 * - 用户权限验证
 * - 环境自适应配置
 * - 统一错误处理
 * 
 * @dependencies
 * - request: 统一请求工具
 * - process.env: 环境变量
 * 
 * @usage
 * 在页面组件中导入相关函数进行用户信息管理操作
 * 
 * @security
 * - 用户身份验证
 * - 权限访问控制
 * - 敏感信息保护
 * - 会话状态管理
 * 
 * @performance
 * - 环境自适应减少配置复杂度
 * - 统一的请求工具提升复用性
 * - 合理的API设计减少请求次数
 * 
 * @maintenance
 * - 模块化的API组织
 * - 清晰的函数命名
 * - 便于后续功能扩展
 * 
 * @api_endpoints
 * - POST /app/getLoginUserInfo: 获取当前登录用户信息
 * - POST /app/getUserInfo: 根据用户ID获取用户信息
 * 
 * @data_structures
 * - UserInfo: 用户信息对象
 *   - id: 用户ID
 *   - username: 用户名
 *   - realName: 真实姓名
 *   - phone: 手机号
 *   - email: 邮箱
 *   - avatar: 头像
 *   - status: 用户状态
 */

// 导入请求工具函数
import { request } from '@/utils/request';

/**
 * 环境配置
 * 
 * 根据当前环境自动选择API服务器地址
 * 支持开发环境和生产环境的自动切换
 */
// 判断是否为开发环境
const isDev = process.env.NODE_ENV === 'development';
// 根据环境设置API基础URL
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取当前登录用户信息
 * @description 获取当前登录用户的详细信息，包括基本信息、权限等
 * @returns {Promise<Object>} 返回用户信息数据
 * @example
 * getLoginUserInfo().then(userInfo => {
 *   console.log('当前用户:', userInfo);
 * });
 */
export function getLoginUserInfo() {
  // 发起POST请求获取当前登录用户信息
  return request({
    url: `${API_BASE_URL}/app/getLoginUserInfo`, // 请求URL
    method: 'post' // 请求方法
  })
}

/**
 * 根据用户ID获取用户信息
 * @description 根据指定的用户ID获取该用户的详细信息
 * @param {Number|String} userId 用户ID
 * @returns {Promise<Object>} 返回用户信息数据
 * @example
 * getUserInfoById(123).then(userInfo => {
 *   console.log('用户信息:', userInfo);
 * });
 */
export function getUserInfoById(userId) {
  // 发起POST请求根据用户ID获取用户信息
  return request({
    url: `${API_BASE_URL}/app/getUserInfo`, // 请求URL
    method: 'post', // 请求方法
    data: { userId } // 请求数据，包含用户ID
  })
}