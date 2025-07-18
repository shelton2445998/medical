#!/bin/bash
# Docker 构建脚本
# 用于将 Vue 项目构建成 Docker 镜像

# 1. 构建项目
# 使用 npm 构建生产环境的静态文件
echo "开始构建项目..."
npm run build

# 2. 构建 Docker 镜像
# 根据 Dockerfile 构建名为 spring-boot-plus-vue3 的镜像
echo "构建 Docker 镜像..."
docker build -t spring-boot-plus-vue3 .

# 3. 导出镜像为 tar 文件
# 将构建好的镜像导出为 tar 文件，便于部署到其他服务器
echo "导出镜像为 tar 文件..."
docker save spring-boot-plus-vue3 -o spring-boot-plus-vue3.tar

echo "Docker 镜像构建完成！"


