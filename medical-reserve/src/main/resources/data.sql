-- 插入医院数据
INSERT INTO `hospital` (`hospitalName`, `hospitalPhone`, `address`, `status`)
VALUES
('北京协和医院', '010-12345678', '北京市东城区帅府园1号', 1),
('上海瑞金医院', '021-12345678', '上海市黄浦区瑞金二路197号', 1),
('广州中医药大学附属医院', '020-12345678', '广州市白云区白云大道北1号', 1);

-- 插入科室数据
INSERT INTO `department` (`departmentName`, `description`, `hospitalId`, `status`)
VALUES
('内科', '诊治内脏疾病的科室', 1, 1),
('外科', '进行外科手术和治疗的科室', 1, 1),
('妇产科', '诊治女性疾病和孕产的科室', 1, 1),
('儿科', '诊治儿童疾病的科室', 1, 1),
('内科', '诊治内脏疾病的科室', 2, 1),
('外科', '进行外科手术和治疗的科室', 2, 1),
('内科', '诊治内脏疾病的科室', 3, 1),
('中医科', '使用中医方法诊治疾病的科室', 3, 1);

-- 插入医生数据 (密码都是 "password" 的BCrypt加密形式)
INSERT INTO `doctor` (`doctorName`, `password`, `gender`, `mobile`, `email`, `hospitalId`, `departmentId`, `title`, `introduction`, `status`)
VALUES
('张医生', '$2a$10$rO.OdxdNdfdfdUvdSTEH2uLcBRy/aHBjj7J.k/NjH4yzrGZITtBpK', 1, '13800000001', 'zhang@example.com', 1, 1, '主任医师', '从事内科临床工作20年，擅长治疗心血管疾病', 1),
('李医生', '$2a$10$rO.OdxdNdfdfdUvdSTEH2uLcBRy/aHBjj7J.k/NjH4yzrGZITtBpK', 0, '13800000002', 'li@example.com', 1, 2, '副主任医师', '从事外科临床工作15年，擅长普外科手术', 1),
('王医生', '$2a$10$rO.OdxdNdfdfdUvdSTEH2uLcBRy/aHBjj7J.k/NjH4yzrGZITtBpK', 1, '13800000003', 'wang@example.com', 2, 5, '主任医师', '从事内科临床工作25年，擅长治疗消化系统疾病', 1),
('陈医生', '$2a$10$rO.OdxdNdfdfdUvdSTEH2uLcBRy/aHBjj7J.k/NjH4yzrGZITtBpK', 0, '13800000004', 'chen@example.com', 3, 8, '主任中医师', '从事中医临床工作30年，擅长中医内科诊治', 1);

-- 插入检查项数据
INSERT INTO `checkitem` (`itemName`, `departmentId`, `price`, `description`, `status`)
VALUES
('血常规', 1, 35.00, '检查血液中的血细胞计数、血红蛋白含量等指标', 1),
('尿常规', 1, 25.00, '检查尿液中的常规指标', 1),
('肝功能', 1, 50.00, '检查肝脏功能的各项指标', 1),
('肾功能', 1, 50.00, '检查肾脏功能的各项指标', 1),
('心电图', 1, 60.00, '记录心脏电活动的图形', 1),
('B超检查', 2, 120.00, '使用超声波检查内脏器官', 1),
('X光检查', 2, 100.00, '使用X射线检查身体组织', 1),
('中医体质辨识', 8, 80.00, '根据中医理论判断体质类型', 1);

-- 插入检查项明细数据
INSERT INTO `checkitemdetailed` (`itemId`, `detailName`, `normalValue`, `unit`)
VALUES
(1, '白细胞计数', '4.0-10.0', '10^9/L'),
(1, '红细胞计数', '4.0-5.5(男)/3.5-5.0(女)', '10^12/L'),
(1, '血红蛋白', '120-160(男)/110-150(女)', 'g/L'),
(1, '血小板计数', '100-300', '10^9/L'),
(2, '尿比重', '1.010-1.025', ''),
(2, '尿糖', '阴性', ''),
(2, '尿蛋白', '阴性', ''),
(3, '谷丙转氨酶', '7-40(男)/7-35(女)', 'U/L'),
(3, '谷草转氨酶', '13-35', 'U/L'),
(3, '总胆红素', '3.4-20.0', 'umol/L'),
(4, '尿素氮', '2.9-8.2', 'mmol/L'),
(4, '肌酐', '53-106(男)/44-97(女)', 'umol/L'),
(4, '尿酸', '149-416(男)/89-357(女)', 'umol/L');

-- 插入体检套餐数据
INSERT INTO `setmeal` (`packageName`, `packageType`, `price`, `discountPrice`, `description`, `status`)
VALUES
('基础体检套餐', 1, 300.00, 268.00, '包含常规体检项目，适合一般人群', 1),
('高级体检套餐', 2, 800.00, 698.00, '包含更全面的检查项目，适合中高龄人群', 1),
('男性体检专项套餐', 3, 500.00, 458.00, '针对男性特点设计的体检套餐', 1),
('女性体检专项套餐', 3, 600.00, 538.00, '针对女性特点设计的体检套餐', 1),
('老年人体检套餐', 3, 700.00, 628.00, '针对老年人特点设计的体检套餐', 1);

-- 插入体检套餐明细数据
INSERT INTO `setmealdetailed` (`packageId`, `itemId`)
VALUES
(1, 1), (1, 2), (1, 5), -- 基础套餐包含血常规、尿常规、心电图
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), -- 高级套餐包含基础套餐项目+肝功能、肾功能、B超
(3, 1), (3, 2), (3, 3), (3, 6), -- 男性套餐
(4, 1), (4, 2), (4, 3), (4, 6), -- 女性套餐
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 7); -- 老年人套餐

-- 插入用户数据 (密码都是 "password" 的BCrypt加密形式)
INSERT INTO `users` (`userName`, `password`, `realName`, `gender`, `idCard`, `mobile`, `email`, `address`, `status`)
VALUES
('user1', '$2a$10$rO.OdxdNdfdfdUvdSTEH2uLcBRy/aHBjj7J.k/NjH4yzrGZITtBpK', '张三', 1, '110101199001011234', '13900000001', 'user1@example.com', '北京市朝阳区', 1),
('user2', '$2a$10$rO.OdxdNdfdfdUvdSTEH2uLcBRy/aHBjj7J.k/NjH4yzrGZITtBpK', '李四', 1, '310101199202022345', '13900000002', 'user2@example.com', '上海市黄浦区', 1),
('user3', '$2a$10$rO.OdxdNdfdfdUvdSTEH2uLcBRy/aHBjj7J.k/NjH4yzrGZITtBpK', '王五', 0, '440101199303033456', '13900000003', 'user3@example.com', '广州市天河区', 1); 