-- 创建医院表
CREATE TABLE IF NOT EXISTS `hospital` (
    `hospitalId` int NOT NULL AUTO_INCREMENT COMMENT '医院ID',
    `hospitalName` varchar(100) NOT NULL COMMENT '医院名称',
    `hospitalPhone` varchar(20) COMMENT '医院电话',
    `address` varchar(200) COMMENT '医院地址',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`hospitalId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医院信息表';

-- 创建用户表
CREATE TABLE IF NOT EXISTS `users` (
    `userId` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `userName` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
    `realName` varchar(50) COMMENT '真实姓名',
    `gender` int COMMENT '性别：0-女，1-男',
    `idCard` varchar(20) COMMENT '身份证号',
    `mobile` varchar(20) NOT NULL COMMENT '手机号',
    `email` varchar(100) COMMENT '邮箱',
    `address` varchar(200) COMMENT '地址',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`userId`),
    UNIQUE KEY `idx_userName` (`userName`),
    UNIQUE KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 创建科室表
CREATE TABLE IF NOT EXISTS `department` (
    `departmentId` int NOT NULL AUTO_INCREMENT COMMENT '科室ID',
    `departmentName` varchar(50) NOT NULL COMMENT '科室名称',
    `description` varchar(200) COMMENT '科室描述',
    `hospitalId` int COMMENT '所属医院ID',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`departmentId`),
    KEY `idx_hospitalId` (`hospitalId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室信息表';

-- 创建医生表
CREATE TABLE IF NOT EXISTS `doctor` (
    `doctorId` int NOT NULL AUTO_INCREMENT COMMENT '医生ID',
    `doctorName` varchar(50) NOT NULL COMMENT '医生姓名',
    `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
    `gender` int COMMENT '性别：0-女，1-男',
    `mobile` varchar(20) COMMENT '手机号',
    `email` varchar(100) COMMENT '邮箱',
    `hospitalId` int COMMENT '所属医院ID',
    `departmentId` int COMMENT '科室ID',
    `title` varchar(50) COMMENT '职称',
    `introduction` text COMMENT '简介',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`doctorId`),
    KEY `idx_hospitalId` (`hospitalId`),
    KEY `idx_departmentId` (`departmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';

-- 创建体检套餐表
CREATE TABLE IF NOT EXISTS `setmeal` (
    `packageId` int NOT NULL AUTO_INCREMENT COMMENT '套餐ID',
    `packageName` varchar(100) NOT NULL COMMENT '套餐名称',
    `packageType` int COMMENT '套餐类型：1-基础，2-高级，3-专项',
    `price` decimal(10,2) COMMENT '价格',
    `discountPrice` decimal(10,2) COMMENT '折扣价',
    `description` text COMMENT '描述',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`packageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体检套餐表';

-- 创建检查项表
CREATE TABLE IF NOT EXISTS `checkitem` (
    `itemId` int NOT NULL AUTO_INCREMENT COMMENT '检查项ID',
    `itemName` varchar(100) NOT NULL COMMENT '检查项名称',
    `departmentId` int COMMENT '科室ID',
    `price` decimal(10,2) COMMENT '价格',
    `description` varchar(500) COMMENT '描述',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`itemId`),
    KEY `idx_departmentId` (`departmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检查项表';

-- 创建体检套餐明细表
CREATE TABLE IF NOT EXISTS `setmealdetailed` (
    `detailId` int NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    `packageId` int NOT NULL COMMENT '套餐ID',
    `itemId` int NOT NULL COMMENT '检查项ID',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`detailId`),
    KEY `idx_packageId` (`packageId`),
    KEY `idx_itemId` (`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体检套餐明细表';

-- 创建检查项明细表
CREATE TABLE IF NOT EXISTS `checkitemdetailed` (
    `detailId` int NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    `itemId` int NOT NULL COMMENT '检查项ID',
    `detailName` varchar(100) COMMENT '明细名称',
    `normalValue` varchar(100) COMMENT '正常值范围',
    `unit` varchar(20) COMMENT '单位',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`detailId`),
    KEY `idx_itemId` (`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检查项明细表';

-- 创建预约订单表
CREATE TABLE IF NOT EXISTS `orders` (
    `orderId` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `userId` int NOT NULL COMMENT '用户ID',
    `packageId` int NOT NULL COMMENT '套餐ID',
    `hospitalId` int NOT NULL COMMENT '医院ID',
    `appointmentDate` date NOT NULL COMMENT '预约日期',
    `timeSlot` varchar(20) NOT NULL COMMENT '时间段',
    `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-已取消，1-待支付，2-已支付，3-已完成',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`orderId`),
    KEY `idx_userId` (`userId`),
    KEY `idx_packageId` (`packageId`),
    KEY `idx_hospitalId` (`hospitalId`),
    KEY `idx_appointmentDate` (`appointmentDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约订单表';

-- 创建总检结论表
CREATE TABLE IF NOT EXISTS `overallresult` (
    `resultId` int NOT NULL AUTO_INCREMENT COMMENT '结论ID',
    `orderId` int NOT NULL COMMENT '订单ID',
    `content` text COMMENT '结论内容',
    `doctorId` int NOT NULL COMMENT '医生ID',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`resultId`),
    UNIQUE KEY `idx_orderId` (`orderId`),
    KEY `idx_doctorId` (`doctorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='总检结论表';

-- 创建体检报告检查项信息表
CREATE TABLE IF NOT EXISTS `cireport` (
    `reportId` int NOT NULL AUTO_INCREMENT COMMENT '报告ID',
    `orderId` int NOT NULL COMMENT '订单ID',
    `itemId` int NOT NULL COMMENT '检查项ID',
    `doctorId` int NOT NULL COMMENT '医生ID',
    `reportStatus` int DEFAULT 0 COMMENT '报告状态：0-未生成，1-已生成',
    `conclusion` varchar(500) COMMENT '检查结论',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`reportId`),
    KEY `idx_orderId` (`orderId`),
    KEY `idx_itemId` (`itemId`),
    KEY `idx_doctorId` (`doctorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体检报告检查项信息表';

-- 创建体检报告检查项明细表
CREATE TABLE IF NOT EXISTS `cidetailedreport` (
    `detailId` int NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    `reportId` int NOT NULL COMMENT '报告ID',
    `detailItemId` int NOT NULL COMMENT '检查项明细ID',
    `value` varchar(100) COMMENT '检查值',
    `isAbnormal` int DEFAULT 0 COMMENT '是否异常：0-正常，1-异常',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`detailId`),
    KEY `idx_reportId` (`reportId`),
    KEY `idx_detailItemId` (`detailItemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体检报告检查项明细表'; 