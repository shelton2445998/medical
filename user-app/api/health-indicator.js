// 导入请求工具函数
import { request } from '@/utils/request';
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