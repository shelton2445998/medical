-- 医疗模块菜单权限SQL

-- 套餐管理权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7011, '查看套餐', 701, 3, 'setmeal:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7012, '添加套餐', 701, 3, 'setmeal:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7013, '修改套餐', 701, 3, 'setmeal:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7014, '删除套餐', 701, 3, 'setmeal:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7015, '套餐详情', 701, 3, 'setmeal:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 套餐详情权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7021, '查看套餐详情', 702, 3, 'setmeal:detail:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7022, '添加套餐详情', 702, 3, 'setmeal:detail:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7023, '修改套餐详情', 702, 3, 'setmeal:detail:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7024, '删除套餐详情', 702, 3, 'setmeal:detail:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7025, '套餐详情信息', 702, 3, 'setmeal:detail:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 项目详情权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7031, '查看项目详情', 703, 3, 'checkitem:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7032, '添加项目详情', 703, 3, 'checkitem:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7033, '修改项目详情', 703, 3, 'checkitem:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7034, '删除项目详情', 703, 3, 'checkitem:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7035, '项目详情信息', 703, 3, 'checkitem:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 医生管理权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7041, '查看医生', 704, 3, 'doctor:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7042, '添加医生', 704, 3, 'doctor:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7043, '修改医生', 704, 3, 'doctor:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7044, '删除医生', 704, 3, 'doctor:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7045, '医生详情', 704, 3, 'doctor:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 医生排班权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7051, '查看医生排班', 705, 3, 'doctor-schedule:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7052, '添加医生排班', 705, 3, 'doctor-schedule:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7053, '修改医生排班', 705, 3, 'doctor-schedule:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7054, '删除医生排班', 705, 3, 'doctor-schedule:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7055, '医生排班详情', 705, 3, 'doctor-schedule:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW()); 