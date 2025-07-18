/**
 * Vue.js 项目配置文件
 * 用于配置 Vue CLI 项目的构建选项、开发服务器、代理等
 * @author 医生端项目组
 * @date 2024
 */

// 引入 Vue CLI 服务的配置定义函数
const { defineConfig } = require('@vue/cli-service')

// 导出 Vue 项目配置
module.exports = defineConfig({
  // 关闭 ESLint 保存时检查，避免开发时的代码检查干扰
  lintOnSave: false,
  
  // 开启依赖转译，确保第三方包的兼容性
  transpileDependencies: true,
 
  // 开发服务器配置
  devServer: {
    // 客户端配置
    client: {
      // 关闭错误覆盖层，避免开发时错误信息遮挡页面
      overlay: false
    },
    
    // 代理配置，用于解决开发环境跨域问题
    proxy: {
      // 将所有以 '/api' 开头的请求代理到后端服务器
      '/api': {
        // 目标服务器地址 - 后端 API 服务器
        target: 'http://localhost:8888',
        // 开启跨域代理，改变请求头中的 host 字段
        changeOrigin: true
      }
    }
  }
})
