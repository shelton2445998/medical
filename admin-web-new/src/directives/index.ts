/**
 * 自定义指令入口文件
 * 负责注册和管理所有自定义指令
 */
import auth from './auth'

/**
 * 设置自定义指令
 * 将所有自定义指令注册到Vue应用实例中
 * @param app Vue应用实例
 */
export function setupDirective(app: any) {
    // 指令映射对象
    const directives: Record<string, any> = {
        auth  // 权限控制指令
    }
    
    // 遍历并注册所有指令
    Object.keys(directives).forEach(name => {
        app.directive(name, directives[name])
    })
}
