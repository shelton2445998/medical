-- 目录菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981929078853, '体检报告检查项明细管理', 0, 1, null, '/medicalReportItemDetail', null, null, null, 0, 1, 0, 1, now());

-- 菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981929078854, '体检报告检查项明细列表', 695981929078853, 2, 'report:item:detail:page', '/medical/reportItemDetail', 'medical/reportItemDetail/index', null, null, 0, 1, 0, 1, now());

-- 权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981929078855, '添加体检报告检查项明细', 695981929078854, 3, 'report:item:detail:add', null, null, null, null, 1, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981929078856, '修改体检报告检查项明细', 695981929078854, 3, 'report:item:detail:update', null, null, null, null, 2, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981929078857, '删除体检报告检查项明细', 695981929078854, 3, 'report:item:detail:delete', null, null, null, null, 3, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981929078858, '获取体检报告检查项明细详情', 695981929078854, 3, 'report:item:detail:info', null, null, null, null, 4, 0, 0, 1, now());
