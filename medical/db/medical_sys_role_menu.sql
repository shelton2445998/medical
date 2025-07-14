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

INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20001, 1, 700, 1, 1, '2025-07-10 16:50:54', null, '2025-07-10 16:50:54');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20002, 1, 701, 1, 1, '2025-07-10 16:50:54', null, '2025-07-10 16:50:54');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20003, 1, 70101, 1, 1, '2025-07-10 16:50:55', null, '2025-07-10 16:50:55');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20004, 1, 70102, 1, 1, '2025-07-10 16:50:56', null, '2025-07-10 16:50:56');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20005, 1, 70103, 1, 1, '2025-07-10 16:50:57', null, '2025-07-10 16:50:57');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20006, 1, 70104, 1, 1, '2025-07-10 16:50:57', null, '2025-07-10 16:50:57');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20007, 1, 70105, 1, 1, '2025-07-10 16:50:58', null, '2025-07-10 16:50:58');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20008, 1, 702, 1, 1, '2025-07-10 16:50:59', null, '2025-07-10 16:50:59');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20009, 1, 70201, 1, 1, '2025-07-10 16:50:59', null, '2025-07-10 16:50:59');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20010, 1, 70202, 1, 1, '2025-07-10 16:51:00', null, '2025-07-10 16:51:00');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20011, 1, 70203, 1, 1, '2025-07-10 16:51:01', null, '2025-07-10 16:51:01');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20012, 1, 70204, 1, 1, '2025-07-10 16:51:02', null, '2025-07-10 16:51:02');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20013, 1, 70205, 1, 1, '2025-07-10 16:51:03', null, '2025-07-10 16:51:03');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20014, 1, 703, 1, 1, '2025-07-10 16:51:03', null, '2025-07-10 16:51:03');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20015, 1, 70301, 1, 1, '2025-07-10 16:51:04', null, '2025-07-10 16:51:04');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20016, 1, 70302, 1, 1, '2025-07-10 16:51:05', null, '2025-07-10 16:51:05');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20017, 1, 70303, 1, 1, '2025-07-10 16:51:05', null, '2025-07-10 16:51:05');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20018, 1, 704, 1, 1, '2025-07-10 16:51:06', null, '2025-07-10 16:51:06');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20019, 1, 70401, 1, 1, '2025-07-10 16:51:07', null, '2025-07-10 16:51:07');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20020, 1, 70402, 1, 1, '2025-07-10 16:51:07', null, '2025-07-10 16:51:07');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20021, 1, 70403, 1, 1, '2025-07-10 16:51:08', null, '2025-07-10 16:51:08');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20022, 1, 705, 1, 1, '2025-07-10 16:51:09', null, '2025-07-10 16:51:09');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20023, 1, 706, 1, 1, '2025-07-10 16:51:09', null, '2025-07-10 16:51:09');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20024, 1, 707, 1, 1, '2025-07-10 16:51:10', null, '2025-07-10 16:51:10');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20025, 1, 708, 1, 1, '2025-07-10 16:51:11', null, '2025-07-10 16:51:11');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20026, 1, 709, 1, 1, '2025-07-10 16:51:11', null, '2025-07-10 16:51:11');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20027, 1, 710, 1, 1, '2025-07-10 16:51:12', null, '2025-07-10 16:51:12');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20028, 1, 711, 1, 1, '2025-07-10 16:51:13', null, '2025-07-10 16:51:13');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20029, 1, 712, 1, 1, '2025-07-10 16:51:13', null, '2025-07-10 16:51:13');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20030, 1, 713, 1, 1, '2025-07-10 16:51:14', null, '2025-07-10 16:51:14');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20031, 1, 714, 1, 1, '2025-07-10 16:51:15', null, '2025-07-10 16:51:15');
INSERT INTO medical.sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time) VALUES (20032, 1, 715, 1, 1, '2025-07-10 16:51:15', null, '2025-07-10 16:51:15');