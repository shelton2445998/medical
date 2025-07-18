/**
 * 通用工具函数模块
 * 提供日期格式化、时间计算等常用功能
 */
import {computeDateType} from "@/enum";

/**
 * 时间格式化函数
 * 将时间戳、字符串或Date对象格式化为指定格式的字符串
 * @param timestamp 时间戳、时间字符串或Date对象
 * @param format 格式化模板，默认为'YYYY-MM-DD hh:mm:ss'
 * @returns 格式化后的时间字符串
 */
export function dateFormat (timestamp: number|string|Date, format = 'YYYY-MM-DD hh:mm:ss'): string {
    // 转换为Date对象
    const date = new Date(timestamp)
    
    /**
     * 补零函数
     * 确保数字始终显示为两位数
     * @param value 需要补零的数值
     * @returns 补零后的字符串
     */
    function fixedTwo (value: number): string {
        return value < 10 ? '0' + value : String(value)
    }
    
    // 复制格式化模板
    let showTime = format
    
    // 处理毫秒（SSS）
    if (showTime.includes('SSS')) {
        const S = date.getMilliseconds()
        showTime = showTime.replace('SSS', '0'.repeat(3 - String(S).length) + S)
    }
    
    // 处理年份（YYYY或YY）
    if (showTime.includes('YY')) {
        const Y = date.getFullYear()
        showTime = showTime.includes('YYYY') ? showTime.replace('YYYY', String(Y)) : showTime.replace('YY', String(Y).slice(2, 4))
    }
    
    // 处理月份（MM或M）
    if (showTime.includes('M')) {
        const M = date.getMonth() + 1
        showTime = showTime.includes('MM') ? showTime.replace('MM', fixedTwo(M)) : showTime.replace('M', String(M))
    }
    
    // 处理日期（DD或D）
    if (showTime.includes('D')) {
        const D = date.getDate()
        showTime = showTime.includes('DD') ? showTime.replace('DD', fixedTwo(D)) : showTime.replace('D', String(D))
    }
    
    // 处理小时（hh或h）
    if (showTime.includes('h')) {
        const h = date.getHours()
        showTime = showTime.includes('hh') ? showTime.replace('hh', fixedTwo(h)) : showTime.replace('h', String(h))
    }
    
    // 处理分钟（mm或m）
    if (showTime.includes('m')) {
        const m = date.getMinutes()
        showTime = showTime.includes('mm') ? showTime.replace('mm', fixedTwo(m)) : showTime.replace('m', String(m))
    }
    
    // 处理秒数（ss或s）
    if (showTime.includes('s')) {
        const s = date.getSeconds()
        showTime = showTime.includes('ss') ? showTime.replace('ss', fixedTwo(s)) : showTime.replace('s', String(s))
    }
    
    return showTime
}

/**
 * 时间加减计算函数
 * 对指定时间进行加减操作
 * @param num 加减的数量（正数为加，负数为减）
 * @param type 时间单位类型（年、月、日等）
 * @param timestamp 基准时间，默认为当前时间
 * @returns 计算后的时间（Date对象或格式化字符串）
 */
export function computeDate(num:number, type:computeDateType, timestamp:number|string|Date=new Date()):Date|string{
    const date = new Date(timestamp)
    switch (type) {
        // 按年计算
        case computeDateType.year:
            return dateFormat(date.setFullYear(date.getFullYear()+num));
        // 按月计算
        case computeDateType.month:
            return dateFormat(date.setMonth(date.getMonth()+num));
        // 按日计算
        case computeDateType.day:
            return dateFormat(date.setDate(date.getDate()+num));
        // 按小时计算
        case computeDateType.hour:
            return dateFormat(date.setHours(date.getHours()+num));
        // 按分钟计算
        case computeDateType.minute:
            return dateFormat(date.setMinutes(date.getMinutes()+num));
        // 按秒计算
        case computeDateType.second:
            return dateFormat(date.setSeconds(date.getSeconds()+num));
    }
}

/**
 * 十六进制颜色转RGB颜色
 * 将#开头的十六进制颜色值转换为RGB数组
 * @param str 十六进制颜色值（如#FF0000）
 * @returns RGB颜色数组 [r, g, b]
 */
export function hexToRgb(str:any) {
    // 去掉#号并按两位一组分割
    let hxs = str.replace("#", "").match(/../g)
    // 转换为十进制数值
    for (let i = 0; i < 3; i++) hxs[i] = parseInt(hxs[i], 16)
    return hxs
}

/**
 * RGB颜色转十六进制颜色
 * 将RGB数值转换为十六进制颜色值
 * @param a 红色分量（0-255）
 * @param b 绿色分量（0-255）
 * @param c 蓝色分量（0-255）
 * @returns 十六进制颜色值（如#FF0000）
 */
export function rgbToHex(a:number, b:number, c:number) {
    // 将数值转换为十六进制字符串
    let hexes = [a.toString(16), b.toString(16), c.toString(16)]
    // 确保每个分量都是两位数
    for (let i = 0; i < 3; i++) {
        if (hexes[i].length == 1) hexes[i] = `0${hexes[i]}`
    }
    return `#${hexes.join("")}`
}

/**
 * 加深颜色值
 * 将指定颜色按比例加深
 * @param color 原始颜色（十六进制格式）
 * @param level 加深程度（0-1之间，越大越深）
 * @returns 加深后的颜色值
 */
export function darken(color:string, level:number) {
    let rgba = hexToRgb(color)
    // 按比例降低每个颜色分量
    for (let i = 0; i < 3; i++) rgba[i] = Math.floor(rgba[i] * (1 - level))
    return rgbToHex(rgba[0], rgba[1], rgba[2])
}

/**
 * 变浅颜色值
 * 将指定颜色按比例变浅
 * @param color 原始颜色（十六进制格式）
 * @param level 变浅程度（0-1之间，越大越浅）
 * @returns 变浅后的颜色值
 */
export function lighten(color:string, level:number) {
    let rgba = hexToRgb(color)
    // 按比例增加每个颜色分量
    for (let i = 0; i < 3; i++)
        rgba[i] = Math.floor((255 - rgba[i]) * level + rgba[i])
    return rgbToHex(rgba[0], rgba[1], rgba[2])
}

/**
 * CSS类名切换工具
 * 根据条件添加或移除CSS类名
 * @param flag 是否添加类名
 * @param clsName 要操作的类名
 * @param target 目标元素，默认为document.body
 */
export function toggleClass(flag: boolean, clsName: string, target?: HTMLElement) {
    const targetEl = target || document.body;
    let { className } = targetEl;
    // 移除现有的类名
    className = className.replace(clsName, "").trim();
    // 根据flag决定是否添加类名
    targetEl.className = flag ? `${className} ${clsName} ` : className;
}

/**
 * 获取级联父级数组
 * 在树形结构中查找指定节点的所有父级节点ID
 * @param list 树形数据数组
 * @param parentId 要查找的父级ID
 * @param parentName 父级ID字段名，默认为'parentId'
 * @param idName ID字段名，默认为'id'
 * @returns 父级ID数组，从根节点到直接父级
 */
export function getCascadeParent(list:Array<any>=[],parentId:string,parentName:string="parentId",idName:string='id') {
    for (const i in list) {
        // 如果找到匹配的节点，返回其ID
        if(list[i][idName]==parentId){
            return [list[i][idName]]
        }
        // 如果节点有子节点，递归查找
        if (list[i].children) {
            let node:any = getCascadeParent(list[i].children, parentId,parentName,idName)
            if (node !== undefined) {
                // 将当前节点ID添加到路径前面
                node.unshift(list[i][idName])
                return node
            }
        }
    }
}

/**
 * 打开外部链接
 * 在新窗口中打开指定链接
 * @param src 链接地址
 */
export function openLink(src:string) {
    if(!src){return;}
    window.open(src);
}

/**
 * 时间问候语生成器
 * 根据当前时间生成相应的问候语
 * @param param 当前时间，new Date() 格式
 * @description param 调用 `formatGreet(new Date())` 输出 `上午好`
 * @returns 返回对应时间段的问候语
 */
export function formatGreet(param: Date): string {
    let hour: number = new Date(param).getHours();
    if (hour < 6) return '凌晨好';
    else if (hour < 9) return '早上好';
    else if (hour < 12) return '上午好';
    else if (hour < 14) return '中午好';
    else if (hour < 17) return '下午好';
    else if (hour < 19) return '傍晚好';
    else if (hour < 22) return '晚上好';
    else return '夜里好';
}

/**
 * 驼峰命名转下划线命名
 * 将驼峰格式的字符串转换为下划线格式
 * @param str 转化目标字符串（驼峰格式）
 * @returns 转换后的下划线格式字符串
 */
export function toSnakeCase(str:string):string {
    return str.replace(/([a-z])([A-Z])/g, '$1_$2').toLowerCase();
}















