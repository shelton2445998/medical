-- 为report_item表的doctor_id字段添加默认值0
ALTER TABLE report_item MODIFY doctor_id BIGINT NOT NULL DEFAULT 0 COMMENT '医生ID'; 