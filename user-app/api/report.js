import { request } from '@/utils/request';
const isDev = process.env.NODE_ENV === 'development';
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取当前用户的所有体检报告列表
 * @returns {Promise} 返回体检报告列表数据
 */
export function getCurrentUserReports() {
  return request({
    url: `${API_BASE_URL}/app/reportItem/getCurrentUserReports`,
    method: 'get'
  })
}

/**
 * 获取用户体检报告分页列表
 * @param {Object} query 查询条件
 * @returns {Promise} 返回体检报告分页数据
 */
export function getCurrentUserReportItemPage(query) {
  return request({
    url: `${API_BASE_URL}/app/reportItem/getCurrentUserReportItemPage`,
    method: 'post',
    data: query
  })
}

/**
 * 获取体检报告详情
 * @param {String} id 报告ID
 * @returns {Promise} 返回报告详情数据
 */
export function getReportDetail(id) {
  return request({
    url: `${API_BASE_URL}/app/reportItem/getReportDetail`,
    method: 'get',
    params: { id }
  })
}

/**
 * 获取检查项详情
 * @param {String} id 检查项ID
 * @returns {Promise} 返回检查项详情
 */
export function getCheckitemById(id) {
  return request({
    url: `${API_BASE_URL}/app/checkitem/getAppCheckitem/${id}`,
    method: 'post'
  })
}

/**
 * 获取检查项分页列表
 * @param {Object} query 查询条件
 * @returns {Promise} 返回检查项分页数据
 */
export function getCheckitemPage(query) {
  return request({
    url: `${API_BASE_URL}/app/checkitem/getAppCheckitemPage`,
    method: 'post',
    data: query
  })
}

/**
 * 获取检查项结论详情
 * @param {String} reportItemId 报告检查项ID
 * @returns {Promise} 返回检查项结论
 */
export function getReportItemConclusion(reportItemId) {
  return request({
    url: `${API_BASE_URL}/app/reportItem/getConclusion/${reportItemId}`,
    method: 'get'
  })
}

/**
 * 更新检查项结论
 * @param {String} reportItemId 报告检查项ID
 * @param {String} conclusionJson JSON格式的结论
 * @returns {Promise} 返回更新结果
 */
export function updateReportItemConclusion(reportItemId, conclusionJson) {
  return request({
    url: `${API_BASE_URL}/app/reportItem/updateConclusion`,
    method: 'post',
    data: {
      reportItemId,
      conclusionJson
    }
  })
}

