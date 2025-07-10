create table user
(
    id                 bigint                               not null comment 'ID'
        primary key,
    username           varchar(20)                          null comment '账号',
    nickname           varchar(20)                          null comment '昵称',
    password           varchar(64)                          null comment '密码',
    salt               varchar(32)                          null comment '盐值',
    gender             tinyint(1)                           null comment '性别 0:女，1:男',
    idCard             varchar(18)                          null comment '身份证号',
    openid             varchar(200)                         null comment '微信openid',
    phone              varchar(11)                          null comment '手机号码',
    head               varchar(200)                         null comment '头像',
    introduction       varchar(200)                         null comment '个人简介',
    user_role_id       bigint                               null comment '用户角色ID',
    status             tinyint(1) default 1                 not null comment '状态 1：正常，0：禁用',
    register_time      datetime                             null comment '注册时间',
    register_ip        varchar(20)                          null comment '注册IP',
    register_ip_area   varchar(100)                         null comment '注册IP区域',
    last_login_time    datetime                             null comment '最后登录时间',
    last_login_ip      varchar(20)                          null comment '最后登录IP',
    last_login_ip_area varchar(100)                         null comment '最后一次登录IP区域',
    remark             varchar(200)                         null comment '备注',
    create_id          bigint                               null comment '创建人ID',
    create_time        datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id          bigint                               null comment '修改人ID',
    update_time        datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    constraint user_openid_uindex
        unique (openid)
)
    comment '体检用户信息';

create index user_idCard_index
    on user (idCard);

create index user_phone_index
    on user (phone);

create index user_username_index
    on user (username);

INSERT INTO medical.user (id, username, nickname, password, salt, gender, idCard, openid, phone, head, introduction, user_role_id, status, register_time, register_ip, register_ip_area, last_login_time, last_login_ip, last_login_ip_area, remark, create_id, create_time, update_id, update_time) VALUES (5001, 'user1', '用户一', 'fa1a2b8314d2a19cb1881eeebc4747adcc31d0b3aa7fba8f45643f1ec8570883', '970eb1c2f043455e9f70c35578f7e87c', 1, '110101199001011234', null, '13800001111', null, null, 3, 1, '2025-07-09 09:47:37', null, null, null, null, null, null, null, '2025-07-09 09:47:37', null, '2025-07-09 09:47:37');
INSERT INTO medical.user (id, username, nickname, password, salt, gender, idCard, openid, phone, head, introduction, user_role_id, status, register_time, register_ip, register_ip_area, last_login_time, last_login_ip, last_login_ip_area, remark, create_id, create_time, update_id, update_time) VALUES (5002, 'user2', '用户二', 'fa1a2b8314d2a19cb1881eeebc4747adcc31d0b3aa7fba8f45643f1ec8570883', '970eb1c2f043455e9f70c35578f7e87c', 0, '110101199002022345', null, '13800002222', null, null, 3, 1, '2025-07-09 09:47:37', null, null, null, null, null, null, null, '2025-07-09 09:47:37', null, '2025-07-09 09:47:37');