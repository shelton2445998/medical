-- 目录菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981970493509, '检查项明细管理', 0, 1, null, '/medicalCheckitemDetail', null, null, null, 0, 1, 0, 1, now());

-- 菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981970493510, '检查项明细列表', 695981970493509, 2, 'checkitem:detail:page', '/medical/checkitemDetail', 'medical/checkitemDetail/index', null, null, 0, 1, 0, 1, now());

-- 权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981970493511, '添加检查项明细', 695981970493510, 3, 'checkitem:detail:add', null, null, null, null, 1, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981970493512, '修改检查项明细', 695981970493510, 3, 'checkitem:detail:update', null, null, null, null, 2, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981970493513, '删除检查项明细', 695981970493510, 3, 'checkitem:detail:delete', null, null, null, null, 3, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981970493514, '获取检查项明细详情', 695981970493510, 3, 'checkitem:detail:info', null, null, null, null, 4, 0, 0, 1, now());
