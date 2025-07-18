/**
 * 枚举定义模块
 * 定义项目中使用的各种枚举类型
 */

/**
 * 时间计算类型枚举
 * 用于时间加减计算的单位类型
 */
export enum computeDateType{
    'year',     // 年
    'month',    // 月
    'day',      // 日
    'hour',     // 小时
    'minute',   // 分钟
    'second'    // 秒
}

/**
 * 图片文件类型枚举
 * 定义允许的图片文件格式
 */
export enum imageType {
    'jpeg',     // JPEG格式
    'png',      // PNG格式
    'jpg'       // JPG格式
}

/**
 * 头像文件类型枚举
 * 定义允许的头像文件格式
 */
export enum headType {
    'jpeg',     // JPEG格式
    'png',      // PNG格式
    'jpg'       // JPG格式
}

/**
 * Excel文件类型枚举
 * 定义允许的Excel文件格式
 */
export enum excelType {
    'xlsx',     // Excel 2007及以上格式
    'xls'       // Excel 97-2003格式
}

/**
 * Word文件类型枚举
 * 定义允许的Word文件格式
 */
export enum wordType {
    'doc',      // Word 97-2003格式
    'docx'      // Word 2007及以上格式
}

/**
 * PDF文件类型枚举
 * 定义PDF文件格式
 */
export enum pdfType {
    Pdf = 'pdf' // PDF格式
}
