/**
 * Babel 配置文件
 * 用于配置 JavaScript 代码的编译转换选项
 * Babel 是一个 JavaScript 编译器，可以将 ES6+ 代码转换为向后兼容的 JavaScript 代码
 * @author 医生端项目组
 * @date 2024
 */

// 导出 Babel 配置对象
module.exports = {
  // 预设配置数组，指定要使用的 Babel 预设
  presets: [
    // 使用 Vue CLI 的 Babel 预设
    // 这个预设包含了 Vue 项目所需的所有 Babel 插件和转换规则
    // 支持 ES6+、TypeScript、JSX 等语法的转换
    '@vue/cli-plugin-babel/preset'
  ]
}
