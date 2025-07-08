-- 删除医生表的外键约束
ALTER TABLE doctor DROP FOREIGN KEY IF EXISTS fk_doctor_hospital;
ALTER TABLE doctor DROP FOREIGN KEY IF EXISTS fk_doctor_department;

-- 删除检查项表的外键约束
ALTER TABLE checkitem DROP FOREIGN KEY IF EXISTS fk_checkitem_department;

-- 删除检查项明细表的外键约束
ALTER TABLE checkitemdetailed DROP FOREIGN KEY IF EXISTS fk_checkitemdetailed_checkitem;

-- 删除体检套餐明细表的外键约束
ALTER TABLE setmealdetailed DROP FOREIGN KEY IF EXISTS fk_setmealdetailed_setmeal;
ALTER TABLE setmealdetailed DROP FOREIGN KEY IF EXISTS fk_setmealdetailed_checkitem;

-- 删除体检预约订单表的外键约束
ALTER TABLE orders DROP FOREIGN KEY IF EXISTS fk_orders_users;
ALTER TABLE orders DROP FOREIGN KEY IF EXISTS fk_orders_setmeal;
ALTER TABLE orders DROP FOREIGN KEY IF EXISTS fk_orders_hospital;

-- 删除总检结论表的外键约束
ALTER TABLE overallresult DROP FOREIGN KEY IF EXISTS fk_overallresult_orders;
ALTER TABLE overallresult DROP FOREIGN KEY IF EXISTS fk_overallresult_doctor;

-- 删除体检报告检查项信息表的外键约束
ALTER TABLE cireport DROP FOREIGN KEY IF EXISTS fk_cireport_orders;
ALTER TABLE cireport DROP FOREIGN KEY IF EXISTS fk_cireport_checkitem;
ALTER TABLE cireport DROP FOREIGN KEY IF EXISTS fk_cireport_doctor;

-- 删除体检报告检查项明细表的外键约束
ALTER TABLE cidetailedreport DROP FOREIGN KEY IF EXISTS fk_cidetailedreport_cireport;
ALTER TABLE cidetailedreport DROP FOREIGN KEY IF EXISTS fk_cidetailedreport_checkitemdetailed; 