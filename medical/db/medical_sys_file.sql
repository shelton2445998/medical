create table sys_file
(
    id                 bigint                             not null comment '主键'
        primary key,
    trace_id           varchar(32)                        null comment '日志链路ID',
    server_type        tinyint                            not null comment '服务类型 1：本地服务，2：阿里云OSS',
    upload_type        varchar(32)                        null comment '上传类型',
    dir_name           varchar(100)                       null comment '目录名称',
    original_file_name varchar(200)                       null comment '源文件名称',
    file_name          varchar(200)                       null comment '生成的文件名称',
    content_type       varchar(200)                       null comment '文件内容类型',
    extension          varchar(32)                        null comment '文件后缀',
    size               bigint                             null comment '文件大小',
    size_mb            decimal(10, 2)                     null comment '文件大小MB',
    url                varchar(500)                       null comment '访问的URL',
    system_type        tinyint                            null comment '系统类型 1：管理端，2：移动端',
    user_id            bigint                             null comment '用户ID',
    file_path          varchar(500)                       null comment '本地文件服务时的物流文件位置',
    file_type          int                                null comment '文件类型 1：图片，2：音视频，3：文档，4：文件',
    ip                 varchar(32)                        null comment 'IP地址',
    ip_area            varchar(100)                       null comment 'IP区域',
    create_id          bigint                             null comment '创建人ID',
    create_time        datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id          bigint                             null comment '修改人ID',
    update_time        datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '系统文件';

