/**
 * 通用API接口模块
 * 提供文件上传等通用接口功能
 */
import {http} from '@/utils/http'
import {UploadParamsTypes} from "@/api/types/uploadTypes";

/**
 * API接口枚举
 * 定义各种API接口的路径
 */
enum Api {
    upload = '/common/upload/',
}

/**
 * 单个文件上传接口
 * @param data 上传参数，包含文件信息
 * @returns 返回上传结果
 */
export function uploadApi(data:UploadParamsTypes) {
    return http.post<any>(Api.upload, data,{
        headers:{
            // 设置内容类型为multipart/form-data，用于文件上传
            'Content-Type':'multipart/form-data'
        }
    })
}

