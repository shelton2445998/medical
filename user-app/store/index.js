// 导入Vue框架核心库
import Vue from 'vue'
// 导入Vuex状态管理库
import Vuex from 'vuex'

// 在Vue中使用Vuex插件
Vue.use(Vuex)

// 创建Vuex store实例
const store = new Vuex.Store({
	// 定义应用的状态
	state: {
		/**
		 * 是否需要强制登录
		 */
		forcedLogin: false, // 强制登录标志，默认为false
		hasLogin: false, // 用户是否已登录，默认为false
		userName: "" // 当前登录用户的用户名，默认为空字符串
	},
	// 定义修改状态的方法（mutations）
	mutations: {
		// 登录方法，用于设置用户登录状态
		login(state, userName) {
			state.userName = userName || '新用户'; // 设置用户名，如果未提供则默认为'新用户'
			state.hasLogin = true; // 设置登录状态为true
		},
		// 登出方法，用于清除用户登录状态
		logout(state) {
			state.userName = ""; // 清空用户名
			state.hasLogin = false; // 设置登录状态为false
		}
	}
})

// 导出store实例供其他模块使用
export default store
