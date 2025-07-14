-- 目录菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (697639856996421, '体检报告总管理', 0, 1, null, '/medicalReport', null, null, null, 0, 1, 0, 1, now());

-- 菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (697639856996422, '体检报告总列表', 697639856996421, 2, 'report:page', '/medical/report', 'medical/report/index', null, null, 0, 1, 0, 1, now());

-- 权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (697639856996423, '添加体检报告总', 697639856996422, 3, 'report:add', null, null, null, null, 1, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (697639856996424, '修改体检报告总', 697639856996422, 3, 'report:update', null, null, null, null, 2, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (697639856996425, '删除体检报告总', 697639856996422, 3, 'report:delete', null, null, null, null, 3, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (697639856996426, '获取体检报告总详情', 697639856996422, 3, 'report:info', null, null, null, null, 4, 0, 0, 1, now());
