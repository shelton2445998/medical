-- 修改orders表，添加患者相关信息字段
ALTER TABLE `orders` 
ADD COLUMN `patient_name` VARCHAR(50) NULL COMMENT '患者姓名' AFTER `checkitem_ids`,
ADD COLUMN `patient_age` INT NULL COMMENT '患者年龄' AFTER `patient_name`,
ADD COLUMN `patient_gender` TINYINT NULL COMMENT '患者性别 0:女，1:男' AFTER `patient_age`,
ADD COLUMN `patient_phone` VARCHAR(20) NULL COMMENT '患者联系方式' AFTER `patient_gender`;

-- 更新索引
ALTER TABLE `orders` ADD INDEX `idx_patient_name` (`patient_name`);
ALTER TABLE `orders` ADD INDEX `idx_patient_phone` (`patient_phone`);

-- 添加注释说明
ALTER TABLE `orders` COMMENT = '体检预约订单，增加了患者基本信息'; 