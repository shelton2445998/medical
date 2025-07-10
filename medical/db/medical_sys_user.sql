create table sys_user
(
    id          bigint                               not null comment '主键'
        primary key,
    username    varchar(20)                          not null comment '用户名',
    nickname    varchar(20)                          not null comment '昵称',
    password    varchar(64)                          not null comment '密码',
    salt        varchar(32)                          not null comment '盐值',
    phone       varchar(20)                          null comment '手机号码',
    email       varchar(255)                         null comment '电子邮件',
    head        varchar(100)                         null comment '头像',
    status      tinyint(1) default 1                 not null comment '状态，0：禁用，1：启用',
    role_id     bigint                               not null comment '角色ID',
    is_admin    tinyint(1) default 0                 not null comment '是否是超管 0：否，1：是',
    create_id   bigint                               null comment '创建人ID',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                               null comment '修改人ID',
    update_time datetime                             null comment '修改时间',
    constraint sys_us
        unique (username)
)
    comment '系统用户(管理员)';

create index sys_user_status_index
    on sys_user (status);

INSERT INTO medical.sys_user (id, username, nickname, password, salt, phone, email, head, status, role_id, is_admin, create_id, create_time, update_id, update_time) VALUES (1, 'admin', '管理员', 'fa1a2b8314d2a19cb1881eeebc4747adcc31d0b3aa7fba8f45643f1ec8570883', '970eb1c2f043455e9f70c35578f7e87c', '15888888888', 'admin@medical.com', 'https://geekidea.oss-cn-chengdu.aliyuncs.com/boot/202312/20231212223125492564821676037.png', 1, 1, 1, 1, '2023-12-12 22:33:06', null, '2023-12-25 12:04:31');