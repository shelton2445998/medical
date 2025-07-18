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
