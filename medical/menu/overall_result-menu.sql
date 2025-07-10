-- 目录菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981936799813, '总检结论管理', 0, 1, null, '/medicalOverallResult', null, null, null, 0, 1, 0, 1, now());

-- 菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981936799814, '总检结论列表', 695981936799813, 2, 'overall:result:page', '/medical/overallResult', 'medical/overallResult/index', null, null, 0, 1, 0, 1, now());

-- 权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981936799815, '添加总检结论', 695981936799814, 3, 'overall:result:add', null, null, null, null, 1, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981936799816, '修改总检结论', 695981936799814, 3, 'overall:result:update', null, null, null, null, 2, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981936799817, '删除总检结论', 695981936799814, 3, 'overall:result:delete', null, null, null, null, 3, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981936799818, '获取总检结论详情', 695981936799814, 3, 'overall:result:info', null, null, null, null, 4, 0, 0, 1, now());
