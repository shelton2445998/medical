# Docker 镜像构建文件
# 用于构建 Vue 项目的 Nginx 镜像

# 使用官方 nginx 镜像作为基础镜像
FROM nginx

# 将构建后的静态文件复制到 nginx 默认目录
ADD dist/ /usr/share/nginx/html/

# 将自定义的 nginx 配置文件复制到配置目录
ADD nginx.cnf /etc/nginx/conf.d/nginx.cnf
