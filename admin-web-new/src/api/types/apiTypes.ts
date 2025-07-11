/**
 * 接口返回结构
 */
export interface ApiResult<T = any> {
  code: number
  data: T
  msg: string
  success: boolean
}

/**
 * 分页结果
 */
export interface PageResult<T = any> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

/**
 * 内容类型枚举
 */
export enum ContentTypeEnum {
  // json
  JSON = 'application/json;charset=UTF-8',
  // form-data
  FORM_URLENCODED = 'application/x-www-form-urlencoded;charset=UTF-8',
  // form-data 上传
  FORM_DATA = 'multipart/form-data;charset=UTF-8'
} 