/**
 * 应用程序主入口文件
 * 负责创建Vue应用实例、配置全局组件、注册插件等
 */
import {createApp} from 'vue'
import App from './App.vue'
import {setupStore} from "@/store";
import {router} from "@/router";
import {setupDirective} from "@/directives";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue"
import 'virtual:svg-icons-register'

// 导入样式文件
// import "element-plus/dist/index.css";
import "@/style/element/variarbles.scss";
// 导入公共样式
import "@/style/index.scss";

// 导入路由权限配置
import "@/router/permission"

// 创建Vue应用实例
const app = createApp(App)

/**
 * 注册Element Plus图标组件
 * 将所有图标组件注册为全局组件，方便在模板中使用
 */
app.config.globalProperties.$icons=[]
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    // 将驼峰命名转换为kebab-case格式，并添加'ele-'前缀
    let name='ele-'+key.replace(/([a-z])([A-Z])/g,'$1-$2').toLocaleLowerCase()
    // 保存图标名称列表
    app.config.globalProperties.$icons.push(name)
    // 注册为全局组件
    app.component(name, component)
}

/**
 * 配置自定义指令
 * 设置全局指令，如权限控制指令等
 */
setupDirective(app)

/**
 * 配置状态管理器
 * 初始化Pinia状态管理
 */
setupStore(app)

/**
 * 配置路由
 * 注册Vue Router路由系统
 */
app.use(router)

/**
 * 挂载应用
 * 注册Element Plus组件库并挂载到DOM节点
 */
app
    .use(ElementPlus)
    .mount('#app')
