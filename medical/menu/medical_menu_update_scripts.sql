-- 根据生成的前端页面结构更新医疗模块菜单

-- 更新主菜单路径和组件
UPDATE medical.sys_menu SET route_url = '/medical', component_path = '', icon = 'ele-first-aid-kit', name = '医疗管理' 
WHERE id = 700;

-- 更新套餐管理菜单
UPDATE medical.sys_menu SET route_url = '/medical/setmeal', component_path = 'medical/setmeal/index', icon = 'ele-goods', code = 'setmeal:list', name = '套餐管理'
WHERE id = 701;

-- 更新套餐详情菜单 - 路径修改为setmealDetail目录结构
UPDATE medical.sys_menu SET route_url = '/medical/setmeal-detail', component_path = 'medical/setmealDetail/index', icon = 'ele-list', code = 'setmealDetail:list', name = '套餐明细'
WHERE id = 702;

-- 更新检查项目菜单
UPDATE medical.sys_menu SET route_url = '/medical/checkitem', component_path = 'medical/checkitem/index', icon = 'ele-list', code = 'checkitem:list', name = '检查项目'
WHERE id = 703;

-- 更新项目详情菜单
UPDATE medical.sys_menu SET route_url = '/medical/checkitem-detail', component_path = 'medical/checkitemDetail/index', icon = 'ele-document', code = 'checkitemDetail:list', name = '项目明细'
WHERE id = 704;

-- 更新医生管理菜单
UPDATE medical.sys_menu SET route_url = '/medical/doctor', component_path = 'medical/doctor/index', icon = 'ele-user', code = 'doctor:list', name = '医生管理'
WHERE id = 705;

-- 更新医生排班菜单
UPDATE medical.sys_menu SET route_url = '/medical/doctor-schedule', component_path = 'medical/doctorSchedule/index', icon = 'ele-calendar', code = 'doctorSchedule:list', name = '医生排班'
WHERE id = 706;

-- 更新科室管理菜单
UPDATE medical.sys_menu SET route_url = '/medical/department', component_path = 'medical/department/index', icon = 'ele-office-building', code = 'department:list', name = '科室管理'
WHERE id = 707;

-- 更新订单管理菜单
UPDATE medical.sys_menu SET route_url = '/medical/orders', component_path = 'medical/orders/index', icon = 'ele-shopping-cart', code = 'orders:list', name = '预约订单'
WHERE id = 708;

-- 更新家庭成员菜单
UPDATE medical.sys_menu SET route_url = '/medical/family-member', component_path = 'medical/familyMember/index', icon = 'ele-user-filled', code = 'familyMember:list', name = '家庭成员'
WHERE id = 709;

-- 更新医院管理菜单
UPDATE medical.sys_menu SET route_url = '/medical/hospital', component_path = 'medical/hospital/index', icon = 'ele-school', code = 'hospital:list', name = '医院管理'
WHERE id = 710;

-- 更新发票管理菜单
UPDATE medical.sys_menu SET route_url = '/medical/invoice', component_path = 'medical/invoice/index', icon = 'ele-tickets', code = 'invoice:list', name = '发票管理'
WHERE id = 711;

-- 更新通知管理菜单
UPDATE medical.sys_menu SET route_url = '/medical/notification', component_path = 'medical/notification/index', icon = 'ele-bell', code = 'notification:list', name = '通知管理'
WHERE id = 712;

-- 更新体检报告菜单
UPDATE medical.sys_menu SET route_url = '/medical/overall-result', component_path = 'medical/overallResult/index', icon = 'ele-document', code = 'overallResult:list', name = '体检报告'
WHERE id = 713;

-- 更新报告项目菜单
UPDATE medical.sys_menu SET route_url = '/medical/report-item', component_path = 'medical/reportItem/index', icon = 'ele-notebook', code = 'reportItem:list', name = '报告项目'
WHERE id = 714;

-- 更新报告详情菜单
UPDATE medical.sys_menu SET route_url = '/medical/report-item-detail', component_path = 'medical/reportItemDetail/index', icon = 'ele-document-copy', code = 'reportItemDetail:list', name = '报告详情'
WHERE id = 715;

-- 更新权限按钮编码，使其与后端接口匹配

-- 更新套餐管理权限编码
UPDATE medical.sys_menu SET code = 'setmeal:page' WHERE id = 7011;
UPDATE medical.sys_menu SET code = 'setmeal:add' WHERE id = 7012;
UPDATE medical.sys_menu SET code = 'setmeal:update' WHERE id = 7013;
UPDATE medical.sys_menu SET code = 'setmeal:delete' WHERE id = 7014;
UPDATE medical.sys_menu SET code = 'setmeal:info' WHERE id = 7015;

-- 更新套餐详情权限编码
UPDATE medical.sys_menu SET code = 'setmealDetail:page' WHERE id = 7021;
UPDATE medical.sys_menu SET code = 'setmealDetail:add' WHERE id = 7022;
UPDATE medical.sys_menu SET code = 'setmealDetail:update' WHERE id = 7023;
UPDATE medical.sys_menu SET code = 'setmealDetail:delete' WHERE id = 7024;
UPDATE medical.sys_menu SET code = 'setmealDetail:info' WHERE id = 7025;

-- 更新检查项目权限编码
UPDATE medical.sys_menu SET code = 'checkitem:page' WHERE id = 7031;
UPDATE medical.sys_menu SET code = 'checkitem:add' WHERE id = 7032;
UPDATE medical.sys_menu SET code = 'checkitem:update' WHERE id = 7033;
UPDATE medical.sys_menu SET code = 'checkitem:delete' WHERE id = 7034;
UPDATE medical.sys_menu SET code = 'checkitem:info' WHERE id = 7035;

-- 更新项目详情权限编码
UPDATE medical.sys_menu SET code = 'checkitemDetail:page' WHERE id = 7041;
UPDATE medical.sys_menu SET code = 'checkitemDetail:add' WHERE id = 7042;
UPDATE medical.sys_menu SET code = 'checkitemDetail:update' WHERE id = 7043;
UPDATE medical.sys_menu SET code = 'checkitemDetail:delete' WHERE id = 7044;
UPDATE medical.sys_menu SET code = 'checkitemDetail:info' WHERE id = 7045;

-- 更新医生管理权限编码
UPDATE medical.sys_menu SET code = 'doctor:page' WHERE id = 7051;
UPDATE medical.sys_menu SET code = 'doctor:add' WHERE id = 7052;
UPDATE medical.sys_menu SET code = 'doctor:update' WHERE id = 7053;
UPDATE medical.sys_menu SET code = 'doctor:delete' WHERE id = 7054;
UPDATE medical.sys_menu SET code = 'doctor:info' WHERE id = 7055;

-- 更新医生排班权限编码
UPDATE medical.sys_menu SET code = 'doctorSchedule:page' WHERE id = 7061;
UPDATE medical.sys_menu SET code = 'doctorSchedule:add' WHERE id = 7062;
UPDATE medical.sys_menu SET code = 'doctorSchedule:update' WHERE id = 7063;
UPDATE medical.sys_menu SET code = 'doctorSchedule:delete' WHERE id = 7064;
UPDATE medical.sys_menu SET code = 'doctorSchedule:info' WHERE id = 7065; 