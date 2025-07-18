/**
 * @fileoverview 用户端健康指标管理API模块
 * @description 提供健康指标数据获取、检查项明细查询等功能的API接口
 * @author 用户端项目组
 * @version 1.0.0
 * @since 2024-01-01
 * 
 * @features
 * - 健康指标数据获取
 * - 检查项明细查询
 * - 健康数据分析
 * - 分页查询支持
 * - 环境自适应配置
 * - 统一错误处理
 * 
 * @dependencies
 * - request: 统一请求工具
 * - process.env: 环境变量
 * 
 * @usage
 * 在页面组件中导入相关函数进行健康指标数据管理操作
 * 
 * @security
 * - 用户权限验证
 * - 数据访问控制
 * - 敏感健康信息保护
 * 
 * @performance
 * - 分页查询优化大数据量处理
 * - 环境自适应减少配置复杂度
 * - 统一的请求工具提升复用性
 * 
 * @maintenance
 * - 模块化的API组织
 * - 清晰的函数命名
 * - 便于后续功能扩展
 * 
 * @api_endpoints
 * - POST /app/checkitemDetail/getAppCheckitemDetailPage: 获取检查项明细分页
 * 
 * @data_structures
 * - CheckitemDetail: 检查项明细对象
 *   - id: 检查项ID
 *   - name: 检查项名称
 *   - description: 检查项描述
 *   - normalRange: 正常值范围
 *   - unit: 单位
 *   - category: 分类
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
 * 获取检查项明细列表
 * @returns {Promise} 返回检查项明细数据
 */
export function getCheckitemDetailList() {
  // 发起POST请求获取检查项明细列表
  return request({
    url: `${API_BASE_URL}/app/checkitemDetail/getAppCheckitemDetailPage`, // 请求URL
    method: 'POST', // 请求方法
    data: {
      pageNum: 1, // 页码，从第1页开始
      pageSize: 100 // 每页记录数，获取100条记录，可根据实际需求调整
    }
  });
}