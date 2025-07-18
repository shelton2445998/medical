/**
 * 代码生成API接口模块
 * 提供代码生成相关的功能，包括数据库表管理、代码生成和下载
 */
import {http} from '@/utils/http'

/**
 * 代码生成API接口枚举
 * 定义所有代码生成相关的API端点
 */
enum Api {
    getDbTablePage = '/admin/generator/getDbTablePage',         // 获取数据库表分页列表
    generatorCode = '/admin/generator/generatorCode',           // 生成代码
    downloadCode = '/admin/generator/downloadCode',             // 下载代码
    updateGeneratorTable = '/admin/generator/updateGeneratorTable', // 更新生成代码表配置
    getGeneratorTable = '/admin/generator/getGeneratorTable/',  // 获取生成代码表详情
    previewCode = '/admin/generator/previewCode/',              // 预览代码
}

/**
 * 代码生成相关接口
 */

/**
 * 获取数据库表分页列表
 * @param data 查询参数，包含分页信息和筛选条件
 * @returns 返回数据库表列表和分页信息
 */
export function getDbTablePageApi(data:any) {
    return http.post<any>(Api.getDbTablePage, data)
}

/**
 * 生成代码
 * @param data 生成代码参数，包含表名等信息
 * @returns 返回代码生成操作结果
 */
export function generatorCodeApi(data:any) {
    return http.post<any>(Api.generatorCode, data)
}

/**
 * 下载生成的代码
 * @param data 下载参数，包含表名等信息
 * @returns 返回代码文件的二进制流
 */
export function downloadCodeAPi(data:any) {
    return http.post<any>(Api.downloadCode,data,{
        // 设置响应类型为blob，用于处理文件下载
        responseType:'blob'
    })
}

/**
 * 修改生成代码表配置
 * @param data 表配置数据
 * @returns 返回修改操作结果
 */
export function updateGeneratorTableApi(data:any) {
    return http.post<any>(Api.updateGeneratorTable, data)
}

/**
 * 获取生成代码表详情
 * @param tableName 表名
 * @returns 返回指定表的详细配置信息
 */
export function getGeneratorTableApi(tableName:string) {
    return http.post<any>(Api.getGeneratorTable+tableName)
}

/**
 * 预览生成的代码
 * @param tableName 表名
 * @returns 返回预览的代码内容
 */
export function previewCodeApi(tableName:any) {
    return http.post<any>(Api.previewCode+tableName)
}















