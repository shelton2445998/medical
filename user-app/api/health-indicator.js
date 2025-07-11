import { request } from '@/utils/request';
const isDev = process.env.NODE_ENV === 'development';
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取检查项明细列表
 * @returns {Promise} 返回检查项明细数据
 */
export function getCheckitemDetailList() {
  return request({
    url: `${API_BASE_URL}/app/checkitemDetail/getAppCheckitemDetailPage`,
    method: 'POST',
    data: {
      pageNum: 1,
      pageSize: 100 // 获取100条记录，可根据实际需求调整
    }
  });
}