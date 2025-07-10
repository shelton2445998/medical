-- 目录菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981933068357, '体检报告检查项信息管理', 0, 1, null, '/medicalReportItem', null, null, null, 0, 1, 0, 1, now());

-- 菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981933068358, '体检报告检查项信息列表', 695981933068357, 2, 'report:item:page', '/medical/reportItem', 'medical/reportItem/index', null, null, 0, 1, 0, 1, now());

-- 权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981933068359, '添加体检报告检查项信息', 695981933068358, 3, 'report:item:add', null, null, null, null, 1, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981933068360, '修改体检报告检查项信息', 695981933068358, 3, 'report:item:update', null, null, null, null, 2, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981933068361, '删除体检报告检查项信息', 695981933068358, 3, 'report:item:delete', null, null, null, null, 3, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981933068362, '获取体检报告检查项信息详情', 695981933068358, 3, 'report:item:info', null, null, null, null, 4, 0, 0, 1, now());
