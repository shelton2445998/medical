create table sys_menu
(
    id             bigint                               not null comment '主键'
        primary key,
    name           varchar(32)                          null comment '菜单名称',
    parent_id      bigint                               not null comment '父id',
    type           tinyint                              not null comment '菜单类型，1：目录，2：菜单，3：权限',
    code           varchar(100)                         null comment '菜单编码',
    route_url      varchar(200)                         null comment '前端路由地址',
    component_path varchar(100)                         null comment '组件路径',
    route_redirect varchar(100)                         null comment '重定向',
    icon           varchar(100)                         null comment '菜单图标',
    sort           int        default 0                 not null comment '排序',
    is_show        tinyint(1) default 0                 not null comment '是否显示,0：不显示，1：显示',
    is_cache       tinyint(1) default 0                 not null comment '是否缓存，0：否 1：是',
    status         tinyint(1) default 1                 not null comment '状态，0：禁用，1：启用',
    create_id      bigint                               null comment '创建人ID',
    create_time    datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id      bigint                               null comment '修改人ID',
    update_time    datetime   default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '系统菜单';

create index sys_menu_name_index
    on sys_menu (name);

create index sys_menu_status_index
    on sys_menu (status);

create index sys_menu_type_index
    on sys_menu (type);

INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (100, '系统管理', 0, 1, '', '/system', '', '', 'ele-setting', 1, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (101, '用户管理', 100, 2, 'sys:user:list', '/system/user', 'system/user/index', null, 'ele-user', 1, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (102, '角色管理', 100, 2, 'sys:role:list', '/system/role', 'system/role/index', null, 'local-role', 2, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (103, '菜单管理', 100, 2, 'sys:menu:list', '/system/menu', 'system/menu/index', null, 'local-menu', 3, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (105, '系统设置', 100, 2, 'sys:settings', '/system/settings', 'system/settings/index', null, 'ele-cpu', 4, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (200, '医院管理', 0, 1, '', '/hospital', '', '', 'ele-office-building', 2, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (201, '医院列表', 200, 2, 'hospital:list', '/hospital/list', 'hospital/list/index', null, 'ele-house', 1, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (202, '医生管理', 200, 2, 'doctor:list', '/hospital/doctor', 'hospital/doctor/index', null, 'ele-user', 2, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (300, '体检管理', 0, 1, '', '/examination', '', '', 'ele-first-aid-kit', 3, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (301, '套餐管理', 300, 2, 'package:list', '/examination/package', 'examination/package/index', null, 'ele-goods', 1, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (302, '体检项目管理', 300, 2, 'check-item:list', '/examination/check-item', 'examination/check-item/index', null, 'ele-list', 2, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (400, '预约管理', 0, 1, '', '/appointment', '', '', 'ele-calendar', 4, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (401, '预约列表', 400, 2, 'appointment:list', '/appointment/list', 'appointment/list/index', null, 'ele-list', 1, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (402, '用户管理', 400, 2, 'user:list', '/appointment/user', 'appointment/user/index', null, 'ele-user', 2, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (500, '报告管理', 0, 1, '', '/report', '', '', 'ele-document', 5, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (501, '体检报告', 500, 2, 'report:list', '/report/list', 'report/list/index', null, 'ele-document', 1, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (600, '财务管理', 0, 1, '', '/finance', '', '', 'ele-money', 6, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (601, '财务统计', 600, 2, 'finance:statistics', '/finance/statistics', 'finance/statistics/index', null, 'ele-data-analysis', 1, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) VALUES (602, '财务记录', 600, 2, 'finance:list', '/finance/list', 'finance/list/index', null, 'ele-tickets', 2, 1, 0, 1, 1, '2025-07-09 12:48:13', null, '2025-07-09 12:48:13');