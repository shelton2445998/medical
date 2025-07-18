# 医疗预约系统管理平台

基于 Vue 3 + TypeScript + Vite 开发的现代化医疗预约管理系统后台管理平台。

## ? 技术栈

- **前端框架**: Vue 3 (Composition API)
- **语言**: TypeScript
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP客户端**: Axios
- **图标**: Element Plus Icons
- **样式预处理**: Sass/SCSS

## ? 项目结构

```
admin-web-new/
├── src/
│   ├── api/              # API接口层
│   ├── assets/           # 静态资源
│   ├── components/       # 通用组件
│   ├── directives/       # 自定义指令
│   ├── enum/             # 枚举定义
│   ├── hooks/            # 组合式API
│   ├── layout/           # 布局组件
│   ├── router/           # 路由配置
│   ├── store/            # 状态管理
│   ├── style/            # 样式文件
│   ├── types/            # 类型定义
│   ├── utils/            # 工具函数
│   ├── views/            # 页面视图
│   ├── App.vue           # 根组件
│   └── main.ts           # 入口文件
├── public/               # 公共资源
├── Dockerfile           # Docker配置
├── nginx.cnf            # Nginx配置
└── vite.config.ts       # Vite配置
```

## ? 功能特性

- **用户管理**: 用户增删改查、角色权限分配
- **医疗管理**: 套餐管理、项目详情、医生管理
- **预约管理**: 预约列表、用户管理
- **系统管理**: 菜单管理、角色管理、配置管理
- **权限控制**: 基于角色的访问控制(RBAC)
- **响应式设计**: 适配多种设备尺寸
- **国际化**: 多语言支持
- **主题切换**: 支持明暗主题切换

## ?? 开发环境

### 推荐IDE配置

- [VS Code](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (禁用 Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin)

### Vue文件的TypeScript支持

TypeScript 默认无法处理 `.vue` 文件的类型信息，因此我们使用 `vue-tsc` 而不是 `tsc` 进行类型检查。在编辑器中，需要 [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin) 来让 TypeScript 语言服务识别 `.vue` 类型。

### 性能优化

如果独立的 TypeScript 插件运行较慢，Volar 还提供了更高性能的 [Take Over Mode](https://github.com/johnsoncodehk/volar/discussions/471#discussioncomment-1361669)。启用步骤：

1. 禁用内置的 TypeScript 扩展
   1. 在 VSCode 命令面板中运行 `Extensions: Show Built-in Extensions`
   2. 找到 `TypeScript and JavaScript Language Features`，右键选择 `Disable (Workspace)`
2. 运行 `Developer: Reload Window` 重新加载 VSCode 窗口

## ? 快速开始

### 安装依赖

```bash
npm install
# 或
pnpm install
# 或
yarn install
```

### 开发环境运行

```bash
npm run dev
# 或
pnpm dev
# 或
yarn dev
```

### 构建生产版本

```bash
npm run build
# 或
pnpm build
# 或
yarn build
```

### 类型检查

```bash
npm run type-check
# 或
pnpm type-check
# 或
yarn type-check
```

## ? Docker部署

### 构建Docker镜像

```bash
# 执行构建脚本
./build-docker.sh
```

### 运行Docker容器

```bash
# 执行启动脚本
./restart-vue-docker.sh
```

访问应用：`http://localhost:8889`

## ? 开发规范

- 使用 TypeScript 进行类型安全开发
- 遵循 Vue 3 Composition API 最佳实践
- 组件命名采用 PascalCase
- 文件命名采用 kebab-case
- 使用 ESLint 和 Prettier 进行代码格式化
- 提交代码前进行类型检查

## ? 贡献指南

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## ? 许可证

本项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情。
