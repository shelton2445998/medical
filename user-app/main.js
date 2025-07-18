/**
 * @fileoverview 用户端应用主入口文件
 * @description 负责初始化Vue应用、配置全局状态管理、挂载应用到DOM
 * @author 医疗系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 * 
 * @features
 * - Vue应用初始化配置
 * - Vuex状态管理集成
 * - 全局store挂载
 * - 生产环境优化配置
 * 
 * @dependencies
 * - Vue: 前端框架核心
 * - Vuex: 状态管理库
 * - App.vue: 根组件
 * 
 * @usage
 * 此文件作为uni-app项目的入口点，负责应用的启动和全局配置
 * 
 * @security
 * - 关闭生产环境提示信息，避免信息泄露
 * - 通过Vue原型链安全地挂载全局store
 * 
 * @performance
 * - 使用Vue.config.productionTip = false优化生产环境性能
 * - 通过原型链挂载避免重复注入
 * 
 * @maintenance
 * - 保持简洁的入口配置
 * - 便于后续添加全局插件和配置
 */

// 导入Vue框架核心库
import Vue from 'vue'
// 导入根组件App
import App from './App'

// 导入Vuex状态管理store
import store from './store'

// 关闭Vue生产环境提示信息
Vue.config.productionTip = false

// 将store挂载到Vue原型上，使所有组件都能通过this.$store访问
Vue.prototype.$store = store

// 设置App组件的类型为应用根组件
App.mpType = 'app'

// 创建Vue实例，传入store和App组件配置
const app = new Vue({
	store, // 注入store到Vue实例
	...App // 展开App组件的配置
})
// 挂载Vue应用到DOM
app.$mount()
