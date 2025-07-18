/**
 * 全局配置Hook
 * 提供全局配置初始化、国际化、主题设置等功能
 */
import {useConfigure} from "@/layout/hook/useConfigure";
import zhCn from "element-plus/dist/locale/zh-cn.mjs"
import en from "element-plus/dist/locale/en.mjs"
import {useConfigStoreHook} from "@/store/modules/config";

/**
 * 使用全局配置Hook
 * 返回全局配置相关的方法
 */
export function useGlobeConfigure() {

    /**
     * 设置主题色
     * 调用配置Hook来设置主题色
     * @param color 主题色值
     */
    function setThemeColor(color: string) {
        useConfigure().setThemeColor(color)
    }

    /**
     * 设置夜晚模式
     * 根据当前时间自动切换暗黑模式（19点后启用）
     */
    function setNightMode() {
        const hour: number = new Date().getHours();
        // 19点后启用暗黑模式
        useConfigStoreHook().storageConfigureChange('isDark', hour > 19);
        useConfigure().setDark(hour > 19);
    }

    /**
     * 获取翻译配置
     * 根据语言设置返回对应的Element Plus语言包
     * @returns Element Plus语言包对象
     */
    function getTranslation() {
        return useConfigStoreHook().configure.locale == 'zh' ? zhCn : en
    }

    /**
     * 设置全局配置
     * 从本地存储中读取配置并应用到系统中
     */
    function setGlobeConfigure() {
        const configure = JSON.parse(localStorage.getItem('configure') as string);
        
        if (configure) {
            // 应用存储的配置
            setThemeColor(configure.themeColor);        // 设置主题色
            useConfigure().setGrey(configure.isGrey);   // 设置灰色模式
            useConfigure().setWeakness(configure.isWeakness);  // 设置色弱模式
            useConfigure().setDark(configure.isDark);   // 设置暗黑模式
            useConfigStoreHook().setConfigure();       // 更新store配置
        } else {
            // 使用默认主题色
            setThemeColor(getComputedStyle(document.documentElement)
                .getPropertyValue('--el-color-primary'));
        }
        // 可选：启用夜晚模式自动切换
        // setNightMode();
    }

    /**
     * 返回全局配置相关的方法
     */
    return {
        setGlobeConfigure,      // 设置全局配置
        setThemeColor,          // 设置主题色
        getTranslation          // 获取翻译配置
    }
}
