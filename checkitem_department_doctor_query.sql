-- 多表查询：检查项对应的科室、医院以及医生信息
-- 如果对应科室没有医生，医生信息将置空显示

SELECT 
    c.id AS checkitem_id,          -- 检查项ID
    c.name AS checkitem_name,      -- 检查项名称
    c.price AS checkitem_price,    -- 检查项价格
    c.description AS checkitem_desc, -- 检查项描述
    d.id AS department_id,         -- 科室ID
    d.name AS department_name,     -- 科室名称
    h.id AS hospital_id,           -- 医院ID
    h.name AS hospital_name,       -- 医院名称
    dr.id AS doctor_id,            -- 医生ID（可能为NULL）
    dr.name AS doctor_name,        -- 医生姓名（可能为NULL）
    dr.title AS doctor_title,      -- 医生职称（可能为NULL）
    dr.introduction AS doctor_specialty -- 医生简介（可能为NULL）
FROM 
    checkitem c
JOIN 
    department d ON c.department_id = d.id
JOIN 
    hospital h ON d.hospital_id = h.id
LEFT JOIN 
    doctor dr ON dr.department_id = d.id AND dr.status = 1 -- 只显示状态为启用的医生
ORDER BY 
    c.id, d.id, dr.id;

-- 如果只需要检查项和对应科室的简化版本，可以使用以下查询：
/*
SELECT 
    c.id AS checkitem_id, 
    c.name AS checkitem_name,
    d.id AS department_id,
    d.name AS department_name,
    dr.id AS doctor_id,
    dr.name AS doctor_name
FROM 
    checkitem c
JOIN 
    department d ON c.department_id = d.id
LEFT JOIN 
    doctor dr ON dr.department_id = d.id AND dr.status = 1
ORDER BY 
    c.id, dr.id;
*/ 