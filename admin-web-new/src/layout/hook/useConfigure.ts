/**
 * 布局配置Hook
 * 提供主题色、暗黑模式、灰色模式等视觉配置功能
 */
import {darken, lighten, toggleClass} from "@/utils";
import { useDark, useToggle } from '@vueuse/core'

/**
 * 使用配置Hook
 * 返回各种视觉配置方法
 */
export function useConfigure() {

    /**
     * 设置主题色
     * 动态修改CSS变量来改变整个应用的主题色
     * @param color 主题色值（十六进制格式）
     */
    function setThemeColor(color: string) {
        // 设置主要颜色
        document.documentElement.style.setProperty("--el-color-primary", color)
        document.documentElement.style.setProperty("--el-color-primary", color);
        
        // 生成浅色变体（1-9级）
        for (let i = 1; i <= 9; i++) {
            document.documentElement.style.setProperty(
                `--el-color-primary-light-${i}`,
                lighten(color, i / 10)
            );
        }
        
        // 生成深色变体（1-2级）
        for (let i = 1; i <= 2; i++) {
            document.documentElement.style.setProperty(
                `--el-color-primary-dark-${i}`,
                darken(color, i / 10)
            );
        }
    }

    /**
     * 设置暗黑模式
     * 切换整个应用的暗黑主题
     * @param isDark 是否启用暗黑模式
     */
    function setDark(isDark: boolean) {
        useToggle(useDark())(isDark);
    }

    /**
     * 设置灰色模式
     * 将整个应用切换为灰色滤镜效果
     * @param isGrey 是否启用灰色模式
     */
    function setGrey(isGrey: boolean) {
        toggleClass(isGrey, "html-grey", document.getElementsByName('html')[0]);
    }

    /**
     * 设置色弱模式
     * 调整颜色以适应色弱用户
     * @param isWeakness 是否启用色弱模式
     */
    function setWeakness(isWeakness: boolean) {
        toggleClass(isWeakness, "html-weakness",document.getElementsByName('html')[0]);
    }

    /**
     * 返回所有配置方法
     */
    return {
        setThemeColor,      // 设置主题色
        setWeakness,        // 设置色弱模式
        setDark,            // 设置暗黑模式
        setGrey             // 设置灰色模式
    }
}
