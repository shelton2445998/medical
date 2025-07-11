-- 创建数据库
CREATE DATABASE IF NOT EXISTS medical_reserve DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE medical_reserve;

-- 科室表
CREATE TABLE IF NOT EXISTS department (
    departmentId INT AUTO_INCREMENT COMMENT '科室ID',
    departmentName VARCHAR(50) NOT NULL COMMENT '科室名称',
    description VARCHAR(200) COMMENT '科室描述',
    hospitalId INT COMMENT '所属医院ID',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (departmentId),
    KEY idx_hospitalId (hospitalId)
) COMMENT='科室表';

-- 医院表
CREATE TABLE IF NOT EXISTS hospital (
    hospitalId INT AUTO_INCREMENT COMMENT '医院ID',
    hospitalName VARCHAR(100) NOT NULL COMMENT '医院名称',
    hospitalPhone VARCHAR(20) COMMENT '医院电话',
    address VARCHAR(200) COMMENT '医院地址',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (hospitalId)
) COMMENT='医院表';

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    userId INT AUTO_INCREMENT COMMENT '用户ID',
    userName VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    realName VARCHAR(50) COMMENT '真实姓名',
    gender TINYINT COMMENT '性别：0-女，1-男',
    idCard VARCHAR(18) COMMENT '身份证号',
    mobile VARCHAR(11) NOT NULL COMMENT '手机号',
    email VARCHAR(50) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '地址',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (userId),
    UNIQUE KEY uk_userName (userName),
    UNIQUE KEY uk_mobile (mobile)
) COMMENT='用户表';

-- 医生表
CREATE TABLE IF NOT EXISTS doctor (
    doctorId INT AUTO_INCREMENT COMMENT '医生ID',
    doctorName VARCHAR(50) NOT NULL COMMENT '医生姓名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    gender TINYINT COMMENT '性别：0-女，1-男',
    mobile VARCHAR(11) COMMENT '手机号',
    email VARCHAR(50) COMMENT '邮箱',
    hospitalId INT COMMENT '所属医院ID',
    departmentId INT COMMENT '科室ID',
    title VARCHAR(50) COMMENT '职称',
    introduction TEXT COMMENT '简介',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (doctorId),
    KEY idx_hospitalId (hospitalId),
    KEY idx_departmentId (departmentId)
) COMMENT='医生表';

-- 体检套餐表
CREATE TABLE IF NOT EXISTS setmeal (
    packageId INT AUTO_INCREMENT COMMENT '套餐ID',
    packageName VARCHAR(100) NOT NULL COMMENT '套餐名称',
    packageType TINYINT COMMENT '套餐类型：1-基础，2-高级，3-专项',
    price DECIMAL(10, 2) COMMENT '价格',
    discountPrice DECIMAL(10, 2) COMMENT '折扣价',
    description TEXT COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (packageId)
) COMMENT='体检套餐表';

-- 检查项表
CREATE TABLE IF NOT EXISTS checkitem (
    itemId INT AUTO_INCREMENT COMMENT '检查项ID',
    itemName VARCHAR(100) NOT NULL COMMENT '检查项名称',
    departmentId INT COMMENT '科室ID',
    price DECIMAL(10, 2) COMMENT '价格',
    description TEXT COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (itemId),
    KEY idx_departmentId (departmentId)
) COMMENT='检查项表';

-- 检查项明细表
CREATE TABLE IF NOT EXISTS checkitemdetailed (
    detailId INT AUTO_INCREMENT COMMENT '明细ID',
    itemId INT COMMENT '检查项ID',
    detailName VARCHAR(100) COMMENT '明细名称',
    normalValue VARCHAR(100) COMMENT '正常值范围',
    unit VARCHAR(20) COMMENT '单位',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (detailId),
    KEY idx_itemId (itemId)
) COMMENT='检查项明细表';

-- 体检套餐明细表
CREATE TABLE IF NOT EXISTS setmealdetailed (
    detailId INT AUTO_INCREMENT COMMENT '明细ID',
    packageId INT COMMENT '套餐ID',
    itemId INT COMMENT '检查项ID',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (detailId),
    KEY idx_packageId (packageId),
    KEY idx_itemId (itemId)
) COMMENT='体检套餐明细表';

-- 体检预约订单表
CREATE TABLE IF NOT EXISTS orders (
    orderId INT AUTO_INCREMENT COMMENT '订单ID',
    userId INT COMMENT '用户ID',
    packageId INT COMMENT '套餐ID',
    hospitalId INT COMMENT '医院ID',
    appointmentDate DATE COMMENT '预约日期',
    timeSlot VARCHAR(50) COMMENT '时间段',
    status TINYINT DEFAULT 1 COMMENT '状态：0-已取消，1-待支付，2-已支付，3-已完成',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (orderId),
    KEY idx_userId (userId),
    KEY idx_packageId (packageId),
    KEY idx_hospitalId (hospitalId),
    KEY idx_appointmentDate (appointmentDate)
) COMMENT='体检预约订单表';

-- 总检结论表
CREATE TABLE IF NOT EXISTS overallresult (
    resultId INT AUTO_INCREMENT COMMENT '结论ID',
    orderId INT COMMENT '订单ID',
    content TEXT COMMENT '结论内容',
    doctorId INT COMMENT '医生ID',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (resultId),
    KEY idx_orderId (orderId),
    KEY idx_doctorId (doctorId)
) COMMENT='总检结论表';

-- 体检报告检查项信息表
CREATE TABLE IF NOT EXISTS cireport (
    reportId INT AUTO_INCREMENT COMMENT '报告ID',
    orderId INT COMMENT '订单ID',
    itemId INT COMMENT '检查项ID',
    doctorId INT COMMENT '医生ID',
    reportStatus TINYINT DEFAULT 0 COMMENT '报告状态：0-未生成，1-已生成',
    conclusion VARCHAR(500) COMMENT '检查结论',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (reportId),
    KEY idx_orderId (orderId),
    KEY idx_itemId (itemId),
    KEY idx_doctorId (doctorId)
) COMMENT='体检报告检查项信息表';

-- 体检报告检查项明细表
CREATE TABLE IF NOT EXISTS cidetailedreport (
    detailId INT AUTO_INCREMENT COMMENT '明细ID',
    reportId INT COMMENT '报告ID',
    detailItemId INT COMMENT '检查项明细ID',
    value VARCHAR(100) COMMENT '检查值',
    isAbnormal TINYINT DEFAULT 0 COMMENT '是否异常：0-正常，1-异常',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (detailId),
    KEY idx_reportId (reportId),
    KEY idx_detailItemId (detailItemId)
) COMMENT='体检报告检查项明细表';

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    adminId INT AUTO_INCREMENT COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    realName VARCHAR(50) COMMENT '真实姓名',
    mobile VARCHAR(11) COMMENT '手机号',
    email VARCHAR(50) COMMENT '邮箱',
    role VARCHAR(20) DEFAULT 'ADMIN' COMMENT '角色：SUPER_ADMIN-超级管理员，ADMIN-普通管理员',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (adminId),
    UNIQUE KEY uk_username (username)
) COMMENT='管理员表';
