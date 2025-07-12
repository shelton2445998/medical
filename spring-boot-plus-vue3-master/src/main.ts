import {createApp} from 'vue'
import App from './App.vue'
import {setupStore} from "@/store";
import {setupRouter} from "@/router";
import {setupDirective} from "@/directives";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue"
import 'virtual:svg-icons-register'
import {initStaticRoutes} from "@/router/staticRoutes";

// import "element-plus/dist/index.css";
import "@/style/element/variarbles.scss";
// 导入公共样式
import "@/style/index.scss";

import "@/router/permission"


const app = createApp(App)

/** 注册element icon图标组件*/
app.config.globalProperties.$icons=[]
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    let name='ele-'+key.replace(/([a-z])([A-Z])/g,'$1-$2').toLocaleLowerCase()
    app.config.globalProperties.$icons.push(name)
    app.component(name, component)
}

/** 配置自定义指令*/
setupDirective(app)

/** 配置 store*/
setupStore(app)

/** 配置 路由*/
setupRouter(app)

// 判断是否已登录，如果已登录则立即初始化静态路由
import {getToken} from "@/utils/auth";
import {useUserStoreHook} from "@/store/modules/user";
if (getToken()) {
    initStaticRoutes();
}

app
    .use(ElementPlus)
    .mount('#app')
