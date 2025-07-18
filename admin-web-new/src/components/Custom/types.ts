/**
 * @fileoverview Custom组件类型定义
 * @description 定义所有自定义组件的类型接口和枚举
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 表格组件类型
 * - 搜索组件类型
 * - 分页组件类型
 * - 表单组件类型
 * - 通用组件类型
 */

/**
 * 表格列配置接口
 * 定义表格列的显示配置
 */
export interface columnsType{
    label: string,                              // 列标题
    name: string                                // 列数据字段名
    tagConfig?: Record<any, tagConfig>          // 标签配置（可选）
}

/**
 * 标签配置接口
 * 定义标签的样式配置
 */
export interface tagConfig {
    color: string                               // 标签颜色
    text: string                                // 标签文本
}

/**
 * 选项配置接口
 * 定义下拉框等组件的选项结构
 */
export interface optionsType{
    id: string | number,                        // 选项ID
    name: string                                // 选项名称
}

/**
 * 自定义配置类型
 * 定义动态获取数据的配置
 */
export type customType = {
    /** 接口地址*/
    url: string
    /** option的label属性名称,默认：name*/
    labelName?: string
    /** option的value属性名称,默认：id*/
    valueName?: string
}

/**
 * 表单类型枚举
 * 定义表单项的不同类型
 */
export enum formType{
    input,          // 输入框
    password,       // 密码输入框
    select,         // 选择框
    radio,          // 单选框
    number,         // 数字输入框
    textarea,       // 文本域
    checkBox,       // 复选框
    cascade         // 级联选择器
}

/**
 * 表单配置接口
 * 定义表单项的配置结构
 */
export interface configureType{
    label: string                               // 表单项标签
    name: string                                // 表单项字段名
    placeholder?: string                        // 占位符文本
    type?: formType                             // 表单项类型
    custom?: customType                         // 自定义数据源配置
    options?: optionsType                       // 静态选项配置
}

/**
 * 操作配置类型
 * 定义CRUD操作的配置
 */
export type operationsType={
    configure: Array<configureType>,            // 表单配置数组
    
    /** 分页获取配置*/
    getOptions?: {
        url: string,                            // 分页接口地址
        type?: string                           // 请求类型
        pageSize?: number                       // 每页条数
    },
    
    /** 删除配置*/
    delOptions?: {
        url: string                             // 删除接口地址
    },
    
    /** 添加配置*/
    addOptions?: {
        url: string                             // 添加接口地址
    },
    
    /** 编辑配置*/
    editOptions?: {
        url: string                             // 编辑接口地址
    },
}
