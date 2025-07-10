-- 目录菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981959880773, '医生排班管理', 0, 1, null, '/medicalDoctorSchedule', null, null, null, 0, 1, 0, 1, now());

-- 菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981959880774, '医生排班列表', 695981959880773, 2, 'doctor:schedule:page', '/medical/doctorSchedule', 'medical/doctorSchedule/index', null, null, 0, 1, 0, 1, now());

-- 权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981959880775, '添加医生排班', 695981959880774, 3, 'doctor:schedule:add', null, null, null, null, 1, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981959880776, '修改医生排班', 695981959880774, 3, 'doctor:schedule:update', null, null, null, null, 2, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981959880777, '删除医生排班', 695981959880774, 3, 'doctor:schedule:delete', null, null, null, null, 3, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981959880778, '获取医生排班详情', 695981959880774, 3, 'doctor:schedule:info', null, null, null, null, 4, 0, 0, 1, now());
