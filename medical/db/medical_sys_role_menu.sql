create table sys_role_menu
(
    id          bigint                             not null comment '主键'
        primary key,
    role_id     bigint                             null comment '角色id',
    menu_id     bigint                             null comment '菜单id',
    is_choice   tinyint(1)                         null comment '是否用户选中 0：否，1：是',
    create_id   bigint                             null comment '创建人ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                             null comment '修改人ID',
    update_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '角色菜单关系表';

create index sys_role_menu_menu_id_index
    on sys_role_menu (menu_id);

create index sys_role_menu_role_id_index
    on sys_role_menu (role_id);

INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10001, 1, 100, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10002, 1, 101, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10003, 1, 102, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10004, 1, 103, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10005, 1, 105, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10006, 1, 200, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10007, 1, 201, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10008, 1, 202, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10009, 1, 300, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10010, 1, 301, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10011, 1, 302, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10012, 1, 400, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10013, 1, 401, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10014, 1, 402, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10015, 1, 500, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10016, 1, 501, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10017, 1, 600, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10018, 1, 601, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (10019, 1, 602, 1, 1, '2025-07-09 12:48:14', null, '2025-07-09 12:48:14');