-- 修改department表，添加hospitalId字段
ALTER TABLE department ADD COLUMN hospitalId INT COMMENT '所属医院ID' AFTER description;
ALTER TABLE department ADD INDEX idx_hospitalId (hospitalId);

-- 更新现有数据，根据科室名称关联医院ID
UPDATE department d
SET d.hospitalId = 1
WHERE d.departmentId IN (1, 2, 3, 4);

UPDATE department d
SET d.hospitalId = 2
WHERE d.departmentId IN (5, 6);

UPDATE department d
SET d.hospitalId = 3
WHERE d.departmentId IN (7, 8); 