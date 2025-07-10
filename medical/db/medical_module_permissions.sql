-- 医疗模块权限和菜单SQL脚本
-- 生成日期：2025-07-10

-- 清除可能存在的旧数据（谨慎使用，如果不想清除请注释掉）
-- DELETE FROM sys_role_menu WHERE menu_id >= 700 AND menu_id < 800;
-- DELETE FROM sys_menu WHERE id >= 700 AND id < 800;

-- 创建医疗管理主菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (700, '医疗管理', 0, 1, '', '/medical', '', '', 'ele-first-aid-kit', 7, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 医院管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (701, '医院管理', 700, 2, 'hospital:list', '/medical/hospital', 'medical/hospital/index', NULL, 'ele-office-building', 1, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 医院管理权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70101, '添加医院', 701, 3, 'hospital:add', NULL, NULL, NULL, NULL, 1, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70102, '修改医院', 701, 3, 'hospital:update', NULL, NULL, NULL, NULL, 2, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70103, '删除医院', 701, 3, 'hospital:delete', NULL, NULL, NULL, NULL, 3, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70104, '查看医院', 701, 3, 'hospital:info', NULL, NULL, NULL, NULL, 4, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70105, '医院列表', 701, 3, 'hospital:page', NULL, NULL, NULL, NULL, 5, 0, 0, 1, 1, NOW(), NULL, NOW());

-- 套餐管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (702, '套餐管理', 700, 2, 'setmeal:list', '/medical/setmeal', 'medical/setmeal/index', NULL, 'ele-goods', 2, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 套餐管理权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70201, '添加套餐', 702, 3, 'setmeal:add', NULL, NULL, NULL, NULL, 1, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70202, '修改套餐', 702, 3, 'setmeal:update', NULL, NULL, NULL, NULL, 2, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70203, '删除套餐', 702, 3, 'setmeal:delete', NULL, NULL, NULL, NULL, 3, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70204, '查看套餐', 702, 3, 'setmeal:info', NULL, NULL, NULL, NULL, 4, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70205, '套餐列表', 702, 3, 'setmeal:page', NULL, NULL, NULL, NULL, 5, 0, 0, 1, 1, NOW(), NULL, NOW());

-- 套餐详情管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (703, '套餐详情', 700, 2, 'setmeal-detail:list', '/medical/setmeal-detail', 'medical/setmeal-detail/index', NULL, 'ele-document', 3, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 套餐详情权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70301, '添加套餐详情', 703, 3, 'setmeal-detail:add', NULL, NULL, NULL, NULL, 1, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70302, '修改套餐详情', 703, 3, 'setmeal-detail:update', NULL, NULL, NULL, NULL, 2, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70303, '删除套餐详情', 703, 3, 'setmeal-detail:delete', NULL, NULL, NULL, NULL, 3, 0, 0, 1, 1, NOW(), NULL, NOW());

-- 体检项目管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (704, '体检项目', 700, 2, 'checkitem:list', '/medical/checkitem', 'medical/checkitem/index', NULL, 'ele-list', 4, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 体检项目权限
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70401, '添加体检项目', 704, 3, 'checkitem:add', NULL, NULL, NULL, NULL, 1, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70402, '修改体检项目', 704, 3, 'checkitem:update', NULL, NULL, NULL, NULL, 2, 0, 0, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (70403, '删除体检项目', 704, 3, 'checkitem:delete', NULL, NULL, NULL, NULL, 3, 0, 0, 1, 1, NOW(), NULL, NOW());

-- 体检项目详情管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (705, '项目详情', 700, 2, 'checkitem-detail:list', '/medical/checkitem-detail', 'medical/checkitem-detail/index', NULL, 'ele-document', 5, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 医生管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (706, '医生管理', 700, 2, 'doctor:list', '/medical/doctor', 'medical/doctor/index', NULL, 'ele-user', 6, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 医生排班管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (707, '医生排班', 700, 2, 'doctor-schedule:list', '/medical/doctor-schedule', 'medical/doctor-schedule/index', NULL, 'ele-calendar', 7, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 科室管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (708, '科室管理', 700, 2, 'department:list', '/medical/department', 'medical/department/index', NULL, 'ele-office-building', 8, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 订单管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (709, '订单管理', 700, 2, 'orders:list', '/medical/orders', 'medical/orders/index', NULL, 'ele-shopping-cart', 9, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 家庭成员管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (710, '家庭成员', 700, 2, 'family-member:list', '/medical/family-member', 'medical/family-member/index', NULL, 'ele-user', 10, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 发票管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (711, '发票管理', 700, 2, 'invoice:list', '/medical/invoice', 'medical/invoice/index', NULL, 'ele-tickets', 11, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 通知管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (712, '通知管理', 700, 2, 'notification:list', '/medical/notification', 'medical/notification/index', NULL, 'ele-bell', 12, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 体检报告管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (713, '体检报告', 700, 2, 'overall-result:list', '/medical/overall-result', 'medical/overall-result/index', NULL, 'ele-document', 13, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 报告项目管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (714, '报告项目', 700, 2, 'report-item:list', '/medical/report-item', 'medical/report-item/index', NULL, 'ele-list', 14, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 报告项目详情管理子菜单
INSERT INTO sys_menu (id, name, parent_id, type, code, route_url, component_path, route_redirect, icon, sort, is_show, is_cache, status, create_id, create_time, update_id, update_time) 
VALUES (715, '报告详情', 700, 2, 'report-item-detail:list', '/medical/report-item-detail', 'medical/report-item-detail/index', NULL, 'ele-document', 15, 1, 0, 1, 1, NOW(), NULL, NOW());

-- 将所有菜单权限分配给角色ID=1
-- 首先为主菜单分配权限
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20001, 1, 700, 1, 1, NOW(), NULL, NOW());

-- 为子菜单分配权限
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20002, 1, 701, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20003, 1, 70101, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20004, 1, 70102, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20005, 1, 70103, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20006, 1, 70104, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20007, 1, 70105, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20008, 1, 702, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20009, 1, 70201, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20010, 1, 70202, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20011, 1, 70203, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20012, 1, 70204, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20013, 1, 70205, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20014, 1, 703, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20015, 1, 70301, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20016, 1, 70302, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20017, 1, 70303, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20018, 1, 704, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20019, 1, 70401, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20020, 1, 70402, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20021, 1, 70403, 1, 1, NOW(), NULL, NOW());

INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20022, 1, 705, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20023, 1, 706, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20024, 1, 707, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20025, 1, 708, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20026, 1, 709, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20027, 1, 710, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20028, 1, 711, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20029, 1, 712, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20030, 1, 713, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20031, 1, 714, 1, 1, NOW(), NULL, NOW());
INSERT INTO sys_role_menu (id, role_id, menu_id, is_choice, create_id, create_time, update_id, update_time)
VALUES (20032, 1, 715, 1, 1, NOW(), NULL, NOW());

-- 完成脚本 