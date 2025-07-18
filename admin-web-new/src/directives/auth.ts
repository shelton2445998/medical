/**
 * 权限控制指令
 * 基于用户权限控制DOM元素的显示和隐藏
 */
import {useUserStore} from '@/store/modules/user';

/**
 * 权限验证指令
 * 根据用户权限控制元素是否显示
 * 
 * 使用方式：
 * - v-auth="'permission'" - 验证单个权限
 * - v-auth="['perm1', 'perm2']" - 验证多个权限（默认需要全部权限）
 * - v-auth:some="['perm1', 'perm2']" - 验证多个权限（只需要其中一个）
 * 
 * @param value 需要验证的权限值，可以是字符串或字符串数组
 * @param arg 验证模式，'some'表示只需要其中一个权限，默认需要全部权限
 */
export default {
    /**
     * 指令挂载时调用
     * @param el 绑定指令的DOM元素
     * @param binding 指令的绑定对象
     */
    mounted(el: any, binding: any) {
        // 获取指令的值和参数
        const {value, arg} = binding;
        // 获取用户信息
        const {userinfo} = useUserStore();
        
        // 如果不是管理员，需要进行权限验证
        if (!userinfo.admin) {
            // 权限验证结果
            let isPermission = false;
            
            // 如果指令的值是字符串
            if (typeof value === 'string') {
                // 判断用户权限列表中是否包含该权限
                isPermission = userinfo.permissions.some((item:string) => item === value);
            } 
            // 如果指令的值是数组
            else if (Array.isArray(value)) {
                // 如果参数是'some'，只需要其中一个权限
                if (arg == 'some') {
                    isPermission = userinfo.permissions.some((item:string) => value.includes(item));
                } 
                // 默认需要全部权限
                else {
                    isPermission = value.every((item:string) => userinfo.permissions.includes(item as never));
                }
            }
            // 如果指令的值不是字符串也不是数组
            else {
                // 抛出错误
                throw new Error("Only supports types of 'string' or 'string[]'")
            }
            
            // 如果不具有权限，隐藏元素
            if (!isPermission) {
                el.style.display = 'none';
            }
        }
    }
}
