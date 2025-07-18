/**
 * 系统日志API接口模块
 * 提供系统操作日志的查询和管理功能
 */
import {http} from '@/utils/http'

/**
 * 系统日志API接口枚举
 * 定义所有日志相关的API端点
 */
enum Api {
    getSysLogList = '/admin/sysLog/getSysLogPage',  // 获取系统日志分页列表
    getSysLog = '/admin/sysLog/getSysLog/',         // 获取系统日志详情
}

/**
 * 系统日志管理相关接口
 */

/**
 * 获取操作日志分页列表
 * @param data 查询参数，包含分页信息和筛选条件
 * @returns 返回操作日志列表和分页信息
 */
export function getSysLogList(data:any) {
    return http.post<any>(Api.getSysLogList, data)
}

/**
 * 获取操作日志详情
 * @param id 日志ID
 * @returns 返回指定日志的详细信息
 */
export function getSysLog(id:string|number) {
    return http.post<any>(Api.getSysLog+id)
}
