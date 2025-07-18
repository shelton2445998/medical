/**
 * 表单验证工具模块
 * 提供常用的表单验证函数，用于Element Plus的表单验证
 */

/**
 * 邮箱格式验证
 * 验证邮箱地址的格式是否正确
 * @param rule 验证规则对象
 * @param value 需要验证的值
 * @param callback 验证回调函数
 */
export function validateEmail(rule: any, value: any, callback: any): void {
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
    if (value&&!emailRegex.test(value)) {
        callback(new Error('请输入正确的邮箱格式'))
    } else {
        callback()
    }
}

/**
 * 手机号格式验证
 * 验证中国大陆手机号码的格式是否正确
 * @param rule 验证规则对象
 * @param value 需要验证的值
 * @param callback 验证回调函数
 */
export function validatePhoneNumber(rule: any, value: any, callback: any): void {
    const phoneRegex = /^1[3456789]\d{9}$/;
    if (value&&!phoneRegex.test(value)) {
        callback(new Error('请输入正确的手机号格式'))
    } else {
        callback()
    }
}
