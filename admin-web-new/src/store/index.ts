/**
 * Pinia 状态管理配置文件
 * 负责创建和配置全局状态管理器
 */
import type {App} from "vue";
import {createPinia} from "pinia";
import piniaPluginPersist from 'pinia-plugin-persist'

// 创建 Pinia 实例
const store = createPinia();

// 使用持久化插件，支持状态数据的本地存储
store.use(piniaPluginPersist)

/**
 * 设置状态管理器
 * @param app Vue应用实例
 */
export function setupStore(app: App<Element>) {
    app.use(store);
}

// 导出store实例，供其他模块使用
export {store};
