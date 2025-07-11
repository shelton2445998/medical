-- 医疗模块菜单SQL

-- 医疗管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (700, '医疗管理', 0, 1, '', '/medical', '', '', 'ele-first-aid-kit', 7, 1, 0, 1, 1, NOW(), null, NOW());

-- 套餐管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (701, '套餐管理', 700, 2, 'setmeal:list', '/medical/setmeal', 'medical/setmeal/index', null, 'ele-goods', 1, 1, 0, 1, 1, NOW(), null, NOW());

-- 套餐详情菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (702, '套餐详情', 700, 2, 'setmealDetail:list', '/medical/setmeal-detail', 'medical/setmealDetail/index', null, 'ele-list', 2, 1, 0, 1, 1, NOW(), null, NOW());

-- 检查项目菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (703, '检查项目', 700, 2, 'checkitem:list', '/medical/checkitem', 'medical/checkitem/index', null, 'ele-list', 3, 1, 0, 1, 1, NOW(), null, NOW());

-- 检查项目详情菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (704, '项目详情', 700, 2, 'checkitemDetail:list', '/medical/checkitem-detail', 'medical/checkitemDetail/index', null, 'ele-document', 4, 1, 0, 1, 1, NOW(), null, NOW());

-- 医生管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (705, '医生管理', 700, 2, 'doctor:list', '/medical/doctor', 'medical/doctor/index', null, 'ele-user', 5, 1, 0, 1, 1, NOW(), null, NOW());

-- 医生排班菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (706, '医生排班', 700, 2, 'doctorSchedule:list', '/medical/doctor-schedule', 'medical/doctorSchedule/index', null, 'ele-calendar', 6, 1, 0, 1, 1, NOW(), null, NOW());

-- 科室管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (707, '科室管理', 700, 2, 'department:list', '/medical/department', 'medical/department/index', null, 'ele-office-building', 7, 1, 0, 1, 1, NOW(), null, NOW());

-- 订单管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (708, '订单管理', 700, 2, 'orders:list', '/medical/orders', 'medical/orders/index', null, 'ele-shopping-cart', 8, 1, 0, 1, 1, NOW(), null, NOW());

-- 家庭成员菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (709, '家庭成员', 700, 2, 'familyMember:list', '/medical/family-member', 'medical/familyMember/index', null, 'ele-user-filled', 9, 1, 0, 1, 1, NOW(), null, NOW());

-- 医院管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (710, '医院管理', 700, 2, 'hospital:list', '/medical/hospital', 'medical/hospital/index', null, 'ele-school', 10, 1, 0, 1, 1, NOW(), null, NOW());

-- 发票管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (711, '发票管理', 700, 2, 'invoice:list', '/medical/invoice', 'medical/invoice/index', null, 'ele-tickets', 11, 1, 0, 1, 1, NOW(), null, NOW());

-- 通知管理菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (712, '通知管理', 700, 2, 'notification:list', '/medical/notification', 'medical/notification/index', null, 'ele-bell', 12, 1, 0, 1, 1, NOW(), null, NOW());

-- 体检报告菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (713, '体检报告', 700, 2, 'overallResult:list', '/medical/overall-result', 'medical/overallResult/index', null, 'ele-document', 13, 1, 0, 1, 1, NOW(), null, NOW());

-- 报告项目菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (714, '报告项目', 700, 2, 'reportItem:list', '/medical/report-item', 'medical/reportItem/index', null, 'ele-notebook', 14, 1, 0, 1, 1, NOW(), null, NOW());

-- 报告详情菜单
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (715, '报告详情', 700, 2, 'reportItemDetail:list', '/medical/report-item-detail', 'medical/reportItemDetail/index', null, 'ele-document-copy', 15, 1, 0, 1, 1, NOW(), null, NOW());

-- 以下是权限按钮SQL

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
VALUES (7021, '查看套餐详情', 702, 3, 'setmealDetail:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7022, '添加套餐详情', 702, 3, 'setmealDetail:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7023, '修改套餐详情', 702, 3, 'setmealDetail:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7024, '删除套餐详情', 702, 3, 'setmealDetail:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7025, '套餐详情信息', 702, 3, 'setmealDetail:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 检查项目权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7031, '查看检查项目', 703, 3, 'checkitem:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7032, '添加检查项目', 703, 3, 'checkitem:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7033, '修改检查项目', 703, 3, 'checkitem:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7034, '删除检查项目', 703, 3, 'checkitem:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7035, '检查项目详情', 703, 3, 'checkitem:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 项目详情权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7041, '查看项目详情', 704, 3, 'checkitemDetail:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7042, '添加项目详情', 704, 3, 'checkitemDetail:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7043, '修改项目详情', 704, 3, 'checkitemDetail:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7044, '删除项目详情', 704, 3, 'checkitemDetail:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7045, '项目详情信息', 704, 3, 'checkitemDetail:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 医生管理权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7051, '查看医生', 705, 3, 'doctor:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7052, '添加医生', 705, 3, 'doctor:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7053, '修改医生', 705, 3, 'doctor:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7054, '删除医生', 705, 3, 'doctor:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7055, '医生详情', 705, 3, 'doctor:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW());

-- 医生排班权限
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7061, '查看医生排班', 706, 3, 'doctorSchedule:page', null, null, null, null, 1, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7062, '添加医生排班', 706, 3, 'doctorSchedule:add', null, null, null, null, 2, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7063, '修改医生排班', 706, 3, 'doctorSchedule:update', null, null, null, null, 3, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7064, '删除医生排班', 706, 3, 'doctorSchedule:delete', null, null, null, null, 4, 0, 0, 1, 1, NOW(), null, NOW());
INSERT INTO medical.sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (7065, '医生排班详情', 706, 3, 'doctorSchedule:info', null, null, null, null, 5, 0, 0, 1, 1, NOW(), null, NOW()); 