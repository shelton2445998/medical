/**
 * 状态管理工具模块
 * 定义系统中各种数据状态的显示配置，包括颜色类型、标签等
 */

/**
 * 状态颜色类型配置
 * 定义Element Plus中各种状态的颜色类型
 */
const color = {
    primary: '',        // 主要
    success: 'success', // 成功
    info: 'info',       // 信息
    warning: 'warning', // 警告
    danger: 'danger',   // 危险
}

/**
 * 状态接口
 * 定义状态项的结构
 */
export interface InterfaceStatus {
    label: string,      // 显示标签
    type?: string       // 状态类型（对应颜色）
}

/**
 * 状态类型定义
 * 记录类型，键为状态分类，值为状态项配置
 */
type StatusType = Record<string, Record<number | string, InterfaceStatus>>

/**
 * 系统模块状态配置
 * 定义各种业务状态的显示配置
 */
export const system_status: StatusType = {
    /**
     * 日志类型状态
     * 定义操作日志的类型及其显示标签
     */
    logType: {
        0: {label: '访问日志'},
        1: {label: '新增'},
        2: {label: '修改'},
        3: {label: '删除'},
        4: {label: '详情'},
        5: {label: '所有列表'},
        6: {label: '分页列表'},
        7: {label: '其它查询'},
        8: {label: '上传文件'},
        9: {label: '登录'},
        10: {label: '退出'},
    },
    
    /**
     * 响应状态
     * 定义API响应成功/失败的显示配置
     */
    responseSuccess: {
        true: {label: '成功', type: color.success},
        false: {label: '失败', type: color.danger},
    },
    
    /**
     * 设备信息状态
     * 定义设备类型的显示配置
     */
    isMobile: {
        true: {label: '移动端'},
        false: {label: 'PC端'},
    },
}
