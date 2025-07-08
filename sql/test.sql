-- 使用数据库
USE medical_reserve;

-- 插入科室数据
INSERT INTO department (departmentName, description) VALUES
('内科', '处理内部器官疾病'),
('外科', '主要进行手术治疗'),
('妇产科', '专门处理女性生殖系统疾病和妊娠分娩'),
('儿科', '专注于婴幼儿和儿童的医疗保健'),
('眼科', '处理与眼睛相关的疾病'),
('耳鼻喉科', '处理耳、鼻和喉部相关疾病'),
('口腔科', '处理牙齿和口腔疾病'),
('皮肤科', '处理皮肤相关疾病'),
('中医科', '运用中医理论和方法治疗疾病'),
('体检中心', '提供全面的健康体检服务');

-- 插入医院数据
INSERT INTO hospital (hospitalName, hospitalPhone, address) VALUES
('第一人民医院', '0123-4567890', '市中心路123号'),
('第二人民医院', '0123-4567891', '健康大道456号'),
('中心医院', '0123-4567892', '阳光路789号'),
('妇幼保健院', '0123-4567893', '平安街101号'),
('中医院', '0123-4567894', '传统路202号');

-- 插入管理员数据（密码为123456的MD5加密）
INSERT INTO admin (username, password, realName, role) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'SUPER_ADMIN');

-- 插入体检套餐数据
INSERT INTO setmeal (packageName, packageType, price, discountPrice, description) VALUES
('基础体检套餐', 1, 300.00, 250.00, '包含基本体格检查、血常规、尿常规等基本项目'),
('高级体检套餐', 2, 800.00, 680.00, '包含基础套餐全部项目，另加心电图、彩超、血脂检测等'),
('男性健康套餐', 3, 1200.00, 980.00, '针对男性健康的专项检查，包含前列腺检查等'),
('女性健康套餐', 3, 1300.00, 1080.00, '针对女性健康的专项检查，包含乳腺、妇科检查等'),
('老年体检套餐', 3, 1500.00, 1280.00, '针对老年人的专项体检，重点检查心脑血管等');

-- 插入检查项数据
INSERT INTO checkitem (itemName, departmentId, price, description) VALUES
('血常规', 1, 50.00, '检查血液中的各项指标'),
('尿常规', 1, 30.00, '检查尿液中的各项指标'),
('肝功能', 1, 80.00, '检查肝脏功能相关指标'),
('心电图', 1, 100.00, '检查心脏电活动'),
('B超', 2, 150.00, '使用超声波检查内部器官'),
('X光', 2, 120.00, '使用X射线检查身体结构'),
('血压测量', 1, 10.00, '测量血压值'),
('血糖测量', 1, 20.00, '测量血糖浓度'),
('视力检查', 5, 40.00, '检查视力状况'),
('听力检查', 6, 50.00, '检查听力状况');

-- 插入检查项明细数据
INSERT INTO checkitemdetailed (itemId, detailName, normalValue, unit) VALUES
(1, '白细胞计数', '4.0-10.0', '10^9/L'),
(1, '红细胞计数', '3.5-5.5', '10^12/L'),
(1, '血红蛋白', '110-160', 'g/L'),
(1, '血小板计数', '100-300', '10^9/L'),
(2, 'pH值', '4.5-8.0', ''),
(2, '尿蛋白', '阴性', ''),
(2, '尿糖', '阴性', ''),
(2, '尿胆原', '阴性', ''),
(3, '总蛋白', '60-85', 'g/L'),
(3, '白蛋白', '35-55', 'g/L');

-- 插入体检套餐明细数据
INSERT INTO setmealdetailed (packageId, itemId) VALUES
(1, 1), (1, 2), (1, 7), (1, 8), -- 基础体检套餐包含的检查项
(2, 1), (2, 2), (2, 3), (2, 4), (2, 7), (2, 8), -- 高级体检套餐包含的检查项
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 7), (3, 8), -- 男性健康套餐包含的检查项
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 7), (4, 8), -- 女性健康套餐包含的检查项
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6), (5, 7), (5, 8), (5, 9), (5, 10); -- 老年体检套餐包含的检查项

-- 插入测试用户数据
INSERT INTO users (userName, password, realName, gender, idCard, mobile, email, address) VALUES
('testuser1', 'e10adc3949ba59abbe56e057f20f883e', '张三', 1, '110101199001011234', '13800138001', 'zhangsan@example.com', '北京市朝阳区'),
('testuser2', 'e10adc3949ba59abbe56e057f20f883e', '李四', 1, '110101199002022345', '13800138002', 'lisi@example.com', '北京市海淀区'),
('testuser3', 'e10adc3949ba59abbe56e057f20f883e', '王五', 1, '110101199003033456', '13800138003', 'wangwu@example.com', '北京市西城区'),
('testuser4', 'e10adc3949ba59abbe56e057f20f883e', '赵六', 1, '110101199004044567', '13800138004', 'zhaoliu@example.com', '北京市东城区'),
('testuser5', 'e10adc3949ba59abbe56e057f20f883e', '钱七', 0, '110101199005055678', '13800138005', 'qianqi@example.com', '北京市丰台区');

-- 插入测试医生数据
INSERT INTO doctor (doctorName, password, gender, mobile, email, hospitalId, departmentId, title, introduction) VALUES
('王医生', 'e10adc3949ba59abbe56e057f20f883e', 1, '13900139001', 'doctor1@example.com', 1, 1, '主任医师', '内科专家，从事临床工作20年'),
('李医生', 'e10adc3949ba59abbe56e057f20f883e', 0, '13900139002', 'doctor2@example.com', 1, 2, '副主任医师', '外科专家，擅长微创手术'),
('张医生', 'e10adc3949ba59abbe56e057f20f883e', 1, '13900139003', 'doctor3@example.com', 2, 3, '主治医师', '妇产科医生，专注妇科疾病治疗'),
('刘医生', 'e10adc3949ba59abbe56e057f20f883e', 0, '13900139004', 'doctor4@example.com', 3, 4, '主任医师', '儿科专家，擅长儿童疾病诊断'),
('陈医生', 'e10adc3949ba59abbe56e057f20f883e', 1, '13900139005', 'doctor5@example.com', 4, 5, '副主任医师', '眼科医生，擅长白内障手术');

-- 插入测试预约订单数据
INSERT INTO orders (userId, packageId, hospitalId, appointmentDate, timeSlot, status) VALUES
(1, 1, 1, DATE_ADD(CURDATE(), INTERVAL 7 DAY), '上午 9:00-10:00', 2),
(2, 2, 1, DATE_ADD(CURDATE(), INTERVAL 8 DAY), '上午 10:00-11:00', 2),
(3, 3, 2, DATE_ADD(CURDATE(), INTERVAL 9 DAY), '下午 14:00-15:00', 1),
(4, 4, 3, DATE_ADD(CURDATE(), INTERVAL 10 DAY), '下午 15:00-16:00', 1),
(5, 5, 4, DATE_ADD(CURDATE(), INTERVAL 11 DAY), '上午 11:00-12:00', 2);

-- 插入测试体检报告数据（只为已支付的订单生成）
INSERT INTO cireport (orderId, itemId, doctorId, reportStatus, conclusion) VALUES
(1, 1, 1, 1, '血常规检查正常'),
(1, 2, 1, 1, '尿常规检查正常'),
(1, 7, 1, 1, '血压正常'),
(1, 8, 1, 1, '血糖正常'),
(2, 1, 2, 1, '血常规检查正常'),
(2, 2, 2, 1, '尿常规检查正常'),
(2, 3, 2, 1, '肝功能检查正常'),
(2, 4, 2, 1, '心电图检查轻微异常，建议定期复查'),
(5, 1, 5, 1, '血常规检查正常'),
(5, 2, 5, 1, '尿常规检查正常');

-- 插入测试体检报告明细数据
INSERT INTO cidetailedreport (reportId, detailItemId, value, isAbnormal) VALUES
(1, 1, '7.5', 0),
(1, 2, '4.5', 0),
(1, 3, '135', 0),
(1, 4, '250', 0),
(2, 5, '6.0', 0),
(2, 6, '阴性', 0),
(2, 7, '阴性', 0),
(3, 7, '120/80', 0),
(4, 7, '5.5', 0),
(5, 1, '8.0', 0);

-- 插入测试总检结论
INSERT INTO overallresult (orderId, content, doctorId) VALUES
(1, '体检结果正常，建议定期体检，保持良好的生活习惯。', 1),
(2, '体检结果大部分正常，心电图有轻微异常，建议定期复查，注意休息，避免过度劳累。', 2),
(5, '体检结果正常，建议加强锻炼，保持健康饮食。', 5); 