-- 目录菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981917556805, '体检套餐明细管理', 0, 1, null, '/medicalSetmealDetail', null, null, null, 0, 1, 0, 1, now());

-- 菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981917556806, '体检套餐明细列表', 695981917556805, 2, 'setmeal:detail:page', '/medical/setmealDetail', 'medical/setmealDetail/index', null, null, 0, 1, 0, 1, now());

-- 权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981917556807, '添加体检套餐明细', 695981917556806, 3, 'setmeal:detail:add', null, null, null, null, 1, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981917556808, '修改体检套餐明细', 695981917556806, 3, 'setmeal:detail:update', null, null, null, null, 2, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981917556809, '删除体检套餐明细', 695981917556806, 3, 'setmeal:detail:delete', null, null, null, null, 3, 0, 0, 1, now());
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_time) VALUES (695981917556810, '获取体检套餐明细详情', 695981917556806, 3, 'setmeal:detail:info', null, null, null, null, 4, 0, 0, 1, now());
