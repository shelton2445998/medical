-- 医院管理菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time)
VALUES ('10001', '医院管理', '0', 1, '', '/hospital', 'layout/routerView/parent', '', 'Hospital', 10, 1, 0, 1, 1, NOW());

-- 医院列表菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time)
VALUES ('10002', '医院列表', '10001', 2, 'hospital:list', '/hospital/list', 'hospital/list/index', '', 'List', 1, 1, 0, 1, 1, NOW());

-- 医生管理菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time)
VALUES ('10003', '医生管理', '10001', 2, 'doctor:list', '/hospital/doctor', 'hospital/doctor/index', '', 'User', 2, 1, 0, 1, 1, NOW());
