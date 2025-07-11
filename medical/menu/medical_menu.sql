-- 医疗模块菜单SQL

-- 医疗管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (700, '医疗管理', 0, 1, '', '/medical', '', '', 'ele-first-aid-kit', 7, 1, 0, 1, 1, NOW(), null, NOW());

-- 套餐管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (701, '套餐管理', 700, 2, 'setmeal:list', '/medical/setmeal', 'medical/setmeal/index', null, 'ele-goods', 1, 1, 0, 1, 1, NOW(), null, NOW());

-- 套餐详情菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (702, '套餐详情', 700, 2, 'setmeal:detail:list', '/medical/setmeal-detail', 'medical/setmeal-detail/index', null, 'ele-list', 2, 1, 0, 1, 1, NOW(), null, NOW());

-- 项目详情菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (703, '项目详情', 700, 2, 'checkitem:list', '/medical/checkitem', 'medical/checkitem/index', null, 'ele-list', 3, 1, 0, 1, 1, NOW(), null, NOW());

-- 医生管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (704, '医生管理', 700, 2, 'doctor:list', '/medical/doctor', 'medical/doctor/index', null, 'ele-user', 4, 1, 0, 1, 1, NOW(), null, NOW());

-- 医生排班菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (705, '医生排班', 700, 2, 'doctor-schedule:list', '/medical/doctor-schedule', 'medical/doctor-schedule/index', null, 'ele-calendar', 5, 1, 0, 1, 1, NOW(), null, NOW());

-- 科室管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (706, '科室管理', 700, 2, 'department:list', '/medical/department', 'medical/department/index', null, 'ele-office-building', 6, 1, 0, 1, 1, NOW(), null, NOW());

-- 订单管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (707, '订单管理', 700, 2, 'orders:list', '/medical/orders', 'medical/orders/index', null, 'ele-shopping-cart', 7, 1, 0, 1, 1, NOW(), null, NOW());

-- 家庭成员菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (708, '家庭成员', 700, 2, 'family-member:list', '/medical/family-member', 'medical/family-member/index', null, 'ele-user-filled', 8, 1, 0, 1, 1, NOW(), null, NOW());

-- 发票管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (709, '发票管理', 700, 2, 'invoice:list', '/medical/invoice', 'medical/invoice/index', null, 'ele-tickets', 9, 1, 0, 1, 1, NOW(), null, NOW());

-- 通知管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (710, '通知管理', 700, 2, 'notification:list', '/medical/notification', 'medical/notification/index', null, 'ele-bell', 10, 1, 0, 1, 1, NOW(), null, NOW());

-- 体检报告菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (711, '体检报告', 700, 2, 'overall-result:list', '/medical/overall-result', 'medical/overall-result/index', null, 'ele-document', 11, 1, 0, 1, 1, NOW(), null, NOW());

-- 报告项目菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (712, '报告项目', 700, 2, 'report-item:list', '/medical/report-item', 'medical/report-item/index', null, 'ele-notebook', 12, 1, 0, 1, 1, NOW(), null, NOW());

-- 报告详情菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (713, '报告详情', 700, 2, 'report-item-detail:list', '/medical/report-item-detail', 'medical/report-item-detail/index', null, 'ele-document-copy', 13, 1, 0, 1, 1, NOW(), null, NOW()); 