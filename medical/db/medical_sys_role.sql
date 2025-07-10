create table sys_role
(
    id          bigint                               not null comment '主键'
        primary key,
    code        varchar(100)                         null comment '角色唯一编码',
    name        varchar(32)                          not null comment '角色名称',
    is_system   tinyint(1) default 0                 not null comment '是否系统内置角色 1：是，0：否',
    remark      varchar(200)                         null comment '备注',
    create_id   bigint                               null comment '创建人ID',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                               null comment '修改人ID',
    update_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    constraint sys_role_code_uindex
        unique (code),
    constraint sys_role_name_uindex
        unique (name)
)
    comment '系统角色';

INSERT INTO medical.sys_role (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1, 'admin', '管理员', 1, '系统管理员', 1, '2023-12-12 22:33:06', null, '2023-12-25 12:04:31');
INSERT INTO medical.sys_role (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (2, 'doctor', '医生', 1, '医生角色', 1, '2025-07-09 09:47:31', null, '2025-07-09 09:47:31');
INSERT INTO medical.sys_role (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (3, 'user', '普通用户', 1, '普通用户角色', 1, '2025-07-09 09:47:31', null, '2025-07-09 09:47:31');