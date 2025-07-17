-- 为医生创建一个月的排班记录 (2025/07/16到2025/08/16)
-- 使用最简单的方法，不使用临时表和复杂SQL

-- 设置日期范围（仅作为注释参考）
-- 2025-07-16 到 2025-08-16

-- 使用一个递增的ID变量
SET @schedule_id = (SELECT IFNULL(MAX(id), 700000000000000) FROM doctor_schedule);

-- 2025-07-16 排班
INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1), -- 递增ID
    doctor.id,
    '2025-07-16', -- 日期
    '上午(08:00-12:00)', -- 上午
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-16'
        AND time_slot = '上午(08:00-12:00)'
    );

INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1), -- 递增ID
    doctor.id,
    '2025-07-16', -- 日期
    '下午(14:00-18:00)', -- 下午
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-16'
        AND time_slot = '下午(14:00-18:00)'
    );

-- 2025-07-17 排班
INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1), -- 递增ID
    doctor.id,
    '2025-07-17', -- 日期
    '上午(08:00-12:00)', -- 上午
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-17'
        AND time_slot = '上午(08:00-12:00)'
    );

INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1), -- 递增ID
    doctor.id,
    '2025-07-17', -- 日期
    '下午(14:00-18:00)', -- 下午
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-17'
        AND time_slot = '下午(14:00-18:00)'
    );

-- 2025-07-18 排班
INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1),
    doctor.id,
    '2025-07-18', 
    '上午(08:00-12:00)', 
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-18'
        AND time_slot = '上午(08:00-12:00)'
    );

INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1),
    doctor.id,
    '2025-07-18', 
    '下午(14:00-18:00)', 
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-18'
        AND time_slot = '下午(14:00-18:00)'
    );
    
-- 2025-07-19 排班
INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1),
    doctor.id,
    '2025-07-19', 
    '上午(08:00-12:00)', 
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-19'
        AND time_slot = '上午(08:00-12:00)'
    );

INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1),
    doctor.id,
    '2025-07-19', 
    '下午(14:00-18:00)', 
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-19'
        AND time_slot = '下午(14:00-18:00)'
    );

-- 2025-07-20 排班
INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1),
    doctor.id,
    '2025-07-20', 
    '上午(08:00-12:00)', 
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-20'
        AND time_slot = '上午(08:00-12:00)'
    );

INSERT INTO doctor_schedule (
    id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time
)
SELECT 
    (@schedule_id := @schedule_id + 1),
    doctor.id,
    '2025-07-20', 
    '下午(14:00-18:00)', 
    20, 0, 1, 1, NOW()
FROM 
    doctor
WHERE 
    doctor.status = 1
    AND NOT EXISTS (
        SELECT 1 FROM doctor_schedule
        WHERE doctor_id = doctor.id 
        AND work_date = '2025-07-20'
        AND time_slot = '下午(14:00-18:00)'
    ); 