-- 为医生表添加头像URL字段
ALTER TABLE doctor
ADD COLUMN avatar_url VARCHAR(255) NULL COMMENT '头像URL' AFTER introduction; 