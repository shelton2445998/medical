# 医疗系统医生工作站 - 前端项目

## 项目介绍
医疗系统医生工作站是为医生提供的Web端应用，用于管理医生日常工作中的患者、预约、病历和处方等信息。该项目基于Vue 3、Element Plus构建，使用Vue Router进行路由管理，并通过Axios与后端API进行通信。

## 项目结构
```
doctor-web/
├── public/              # 静态资源
├── src/
│   ├── api/             # API服务层
│   ├── assets/          # 项目资源文件
│   ├── components/      # 公共组件
│   ├── router/          # 路由配置
│   ├── store/           # 状态管理
│   ├── views/           # 页面组件
│   ├── App.vue          # 根组件
│   └── main.js          # 入口文件
├── .gitignore           # Git忽略配置
├── babel.config.js      # Babel配置
├── package.json         # 项目依赖
└── vue.config.js        # Vue CLI配置
```

## 功能模块
- **登录认证**：医生使用手机号和密码进行登录
- **工作台**：显示今日工作概况和待诊患者
- **患者管理**：查看和管理患者信息
- **预约管理**：管理患者预约信息
- **病历管理**：创建和查看患者病历
- **处方管理**：开具和管理患者处方

## 开发环境准备
1. 确保已安装Node.js (推荐v16+)
2. 克隆项目代码

## 项目安装
```bash
# 安装依赖
npm install
```

## 运行项目
```bash
# 开发环境运行
npm run serve
```

## 打包部署
```bash
# 构建生产环境代码
npm run build
```

## 接口文档
项目使用的后端API接口，请参考[医疗系统后端接口文档](../medical/docs/医疗系统后端接口文档.md)。

## 开发规范
- 组件命名使用PascalCase
- API请求统一通过api目录下的服务进行
- 页面组件放置在views目录
- 公共组件放置在components目录

## 配置说明
后端API代理配置在vue.config.js中，默认代理到本地开发服务器：
```js
devServer: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      pathRewrite: {
        '^/api': ''
      }
    }
  }
}
```
