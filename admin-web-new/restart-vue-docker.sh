#!/bin/bash
# Docker 重启部署脚本
# 用于在服务器上重新部署 Vue 项目的 Docker 容器

echo "开始重启部署 Vue Docker 容器..."

# 1. 停止正在运行的容器
echo "停止容器..."
docker stop spring-boot-plus-vue3-container

# 2. 删除旧的容器
echo "删除旧容器..."
docker rm spring-boot-plus-vue3-container

# 3. 删除旧的镜像
echo "删除旧镜像..."
docker rmi spring-boot-plus-vue3

# 4. 从 tar 文件加载新的镜像
echo "加载新镜像..."
docker load -i spring-boot-plus-vue3.tar

# 5. 运行新容器
# 端口映射：容器内部80端口映射到宿主机8889端口
# 后台运行模式（-d）
# 容器名称：spring-boot-plus-vue3-container
echo "启动新容器..."
docker run -p 8889:80 -d  --name spring-boot-plus-vue3-container spring-boot-plus-vue3

# 6. 查看容器运行状态
echo "查看容器运行状态..."
docker ps -a -f name=spring-boot-plus-vue3-container

# 7. 查看镜像信息
echo "查看镜像信息..."
docker images spring-boot-plus-vue3

# 8. 访问说明
echo "部署完成！"
echo "访问前端页面："
echo "本地访问：http://localhost:8889"
echo "服务器访问：http://[服务器IP]:8889"



