-- 套餐详情表完善
ALTER TABLE setmeal_detail 
ADD COLUMN name VARCHAR(100) NOT NULL DEFAULT '默认套餐名称' COMMENT '套餐名称' AFTER setmeal_id,
ADD COLUMN price DECIMAL(10,2) NULL DEFAULT 0.00 COMMENT '套餐原价' AFTER name,
ADD COLUMN discount_price DECIMAL(10,2) NULL DEFAULT 0.00 COMMENT '优惠价格' AFTER price,
ADD COLUMN description TEXT NULL COMMENT '套餐详细介绍' AFTER discount_price,
ADD COLUMN check_items TEXT NULL COMMENT '检查项目列表' AFTER description,
ADD COLUMN suitable_crowd TEXT NULL COMMENT '适用人群描述' AFTER check_items,
ADD COLUMN appointment_notice TEXT NULL COMMENT '预约须知' AFTER suitable_crowd,
ADD COLUMN user_reviews TEXT NULL COMMENT '用户评价' AFTER appointment_notice,
ADD COLUMN hospital_id BIGINT NOT NULL DEFAULT 1 COMMENT '所属医院ID' AFTER user_reviews,
ADD COLUMN department_id BIGINT NULL COMMENT '所属科室ID' AFTER hospital_id,
ADD COLUMN package_type TINYINT NOT NULL DEFAULT 1 COMMENT '套餐类型：1-基础，2-高级，3-专项' AFTER department_id,
ADD COLUMN status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架' AFTER package_type,
ADD COLUMN sort_order INT NULL DEFAULT 0 COMMENT '排序权重' AFTER status;

-- 添加索引
ALTER TABLE setmeal_detail 
ADD INDEX idx_setmeal_id (setmeal_id),
ADD INDEX idx_hospital_id (hospital_id),
ADD INDEX idx_department_id (department_id),
ADD INDEX idx_package_type (package_type),
ADD INDEX idx_status (status),
ADD INDEX idx_item_id (item_id);

-- 为现有数据添加新字段的默认值
UPDATE setmeal_detail 
SET name = (SELECT name FROM setmeal WHERE setmeal.id = setmeal_detail.setmeal_id),
    price = (SELECT price FROM setmeal WHERE setmeal.id = setmeal_detail.setmeal_id),
    hospital_id = 1,
    package_type = 1,
    status = 1
WHERE 1=1; 