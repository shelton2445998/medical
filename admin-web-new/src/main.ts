import {createApp} from 'vue'
import App from './App.vue'
import {setupStore} from "@/store";
import {router} from "@/router";
import {setupDirective} from "@/directives";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue"
import 'virtual:svg-icons-register'


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
app.use(router)

app
    .use(ElementPlus)
    .mount('#app')
