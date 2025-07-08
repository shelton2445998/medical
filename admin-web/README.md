# 预约体检系统 - 管理员端

这是预约体检系统的管理员Web端，基于Vue 3和Element Plus开发。

## 功能特性

- 用户管理：管理普通用户账号，包括添加、编辑、启用/禁用等操作
- 医生管理：管理医生账号和信息，包括添加、编辑、启用/禁用等操作
- 体检套餐管理：管理体检套餐，包括添加、编辑、上架/下架等操作
- 体检项目管理：管理体检项目，包括添加、编辑、启用/禁用等操作
- 医院管理：管理体检医院信息，包括添加、编辑、启用/禁用等操作
- 预约管理：管理用户体检预约，查看统计数据等
- 财务管理：管理体检收费、退款等财务相关操作
- 系统设置：配置系统参数、预约规则等

## 技术栈

- Vue 3：前端框架
- Vue Router：路由管理
- Element Plus：UI组件库
- Axios：HTTP客户端

## 开发环境设置

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run serve
```

### 构建生产版本

```bash
npm run build
```

### 代码检查和修复

```bash
npm run lint
```

## 项目结构

```
admin-web/
├── public/              # 静态资源
├── src/                 # 源代码
│   ├── api/             # API请求
│   ├── assets/          # 图片等资源文件
│   ├── components/      # 公共组件
│   ├── router/          # 路由配置
│   ├── views/           # 页面组件
│   ├── App.vue          # 根组件
│   └── main.js          # 入口文件
├── .gitignore           # Git忽略文件
├── babel.config.js      # Babel配置
├── jsconfig.json        # JavaScript配置
├── package.json         # 项目依赖
├── README.md            # 项目说明
└── vue.config.js        # Vue配置
```

## 浏览器支持

- Chrome
- Firefox
- Safari
- Edge

## 账号信息

### 管理员账号
- 用户名：admin
- 密码：admin123


