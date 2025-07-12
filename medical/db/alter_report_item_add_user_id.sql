ALTER TABLE report_item
ADD COLUMN user_id bigint COMMENT '用户ID' AFTER order_id,
ADD INDEX report_user_index (user_id); 