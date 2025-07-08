/**
 * 模拟API数据服务
 * 当后端API不可用时，提供静态演示数据
 */

// 用户数据
export const userData = {
  list: [
    {
      userId: 1,
      userName: 'zhangsan',
      realName: '张三',
      gender: 1,
      idCard: '110101199001011234',
      mobile: '13800138001',
      email: 'zhangsan@example.com',
      address: '北京市朝阳区',
      status: 1,
      createTime: '2023-07-01 09:30:00',
      updateTime: '2023-07-05 15:20:00'
    },
    {
      userId: 2,
      userName: 'lisi',
      realName: '李四',
      gender: 1,
      idCard: '110101199002022345',
      mobile: '13800138002',
      email: 'lisi@example.com',
      address: '北京市海淀区',
      status: 1,
      createTime: '2023-07-02 10:15:00',
      updateTime: '2023-07-02 10:15:00'
    },
    {
      userId: 3,
      userName: 'wangwu',
      realName: '王五',
      gender: 1,
      idCard: '110101199003033456',
      mobile: '13800138003',
      email: 'wangwu@example.com',
      address: '北京市西城区',
      status: 0,
      createTime: '2023-07-03 14:20:00',
      updateTime: '2023-07-06 09:10:00'
    },
    {
      userId: 4,
      userName: 'zhaoliu',
      realName: '赵六',
      gender: 0,
      idCard: '110101199004044567',
      mobile: '13800138004',
      email: 'zhaoliu@example.com',
      address: '北京市东城区',
      status: 1,
      createTime: '2023-07-04 11:40:00',
      updateTime: '2023-07-04 11:40:00'
    },
    {
      userId: 5,
      userName: 'qianqi',
      realName: '钱七',
      gender: 0,
      idCard: '110101199005055678',
      mobile: '13800138005',
      email: 'qianqi@example.com',
      address: '北京市丰台区',
      status: 1,
      createTime: '2023-07-05 16:50:00',
      updateTime: '2023-07-05 16:50:00'
    }
  ]
};

// 医生数据
export const doctorData = {
  list: [
    {
      doctorId: 1,
      doctorName: '张医生',
      gender: 1,
      hospitalId: 1,
      hospitalName: '协和医院',
      departmentId: 1,
      departmentName: '内科',
      title: '主任医师',
      mobile: '13900138001',
      email: 'zhangys@example.com',
      status: 1,
      introduction: '从事内科临床工作20年，擅长心血管疾病的诊断与治疗',
      createTime: '2023-01-15 09:30:00',
      updateTime: '2023-01-15 09:30:00'
    },
    {
      doctorId: 2,
      doctorName: '李医生',
      gender: 0,
      hospitalId: 1,
      hospitalName: '协和医院',
      departmentId: 3,
      departmentName: '妇产科',
      title: '副主任医师',
      mobile: '13900138002',
      email: 'liys@example.com',
      status: 1,
      introduction: '从事妇产科临床工作15年，擅长妇科肿瘤的诊断与治疗',
      createTime: '2023-01-18 10:15:00',
      updateTime: '2023-01-18 10:15:00'
    },
    {
      doctorId: 3,
      doctorName: '王医生',
      gender: 1,
      hospitalId: 2,
      hospitalName: '同仁医院',
      departmentId: 5,
      departmentName: '眼科',
      title: '主任医师',
      mobile: '13900138003',
      email: 'wangys@example.com',
      status: 1,
      introduction: '从事眼科临床工作25年，擅长白内障、青光眼等眼部疾病的治疗',
      createTime: '2023-01-20 11:20:00',
      updateTime: '2023-01-20 11:20:00'
    },
    {
      doctorId: 4,
      doctorName: '赵医生',
      gender: 1,
      hospitalId: 3,
      hospitalName: '人民医院',
      departmentId: 2,
      departmentName: '外科',
      title: '主治医师',
      mobile: '13900138004',
      email: 'zhaoyx@example.com',
      status: 0,
      introduction: '从事外科临床工作10年，擅长普外科手术',
      createTime: '2023-01-22 14:40:00',
      updateTime: '2023-02-15 16:30:00'
    },
    {
      doctorId: 5,
      doctorName: '陈医生',
      gender: 0,
      hospitalId: 4,
      hospitalName: '北医三院',
      departmentId: 4,
      departmentName: '儿科',
      title: '副主任医师',
      mobile: '13900138005',
      email: 'chenys@example.com',
      status: 1,
      introduction: '从事儿科临床工作18年，擅长儿童常见病、多发病的诊断与治疗',
      createTime: '2023-02-01 09:50:00',
      updateTime: '2023-02-01 09:50:00'
    }
  ]
};

// 医院数据
export const hospitalData = {
  list: [
    {
      id: 1,
      name: '北京协和医院',
      level: '三级甲等',
      city: '北京',
      address: '北京市东城区帅府园1号',
      contactPhone: '010-69156114',
      description: '北京协和医院是中国著名的大型综合性医院，集医疗、教学、科研于一体。',
      businessHours: '周一至周日 08:00-17:00',
      status: 1,
      appointmentCount: 954
    },
    {
      id: 2,
      name: '上海交通大学医学院附属仁济医院',
      level: '三级甲等',
      city: '上海',
      address: '上海市浦东新区灵山路845号',
      contactPhone: '021-58752345',
      description: '上海交通大学医学院附属仁济医院始建于1844年，是一所大型综合性三级甲等医院。',
      businessHours: '周一至周日 08:00-17:00',
      status: 1,
      appointmentCount: 782
    },
    {
      id: 3,
      name: '广州中医药大学第一附属医院',
      level: '三级甲等',
      city: '广州',
      address: '广州市白云区机场路16号',
      contactPhone: '020-36591912',
      description: '广州中医药大学第一附属医院是国内著名的中医临床、教学、科研基地。',
      businessHours: '周一至周日 08:00-17:00',
      status: 1,
      appointmentCount: 645
    },
    {
      id: 4,
      name: '武汉同济医院',
      level: '三级甲等',
      city: '武汉',
      address: '湖北省武汉市硚口区解放大道1095号',
      contactPhone: '027-83662688',
      description: '华中科技大学同济医学院附属同济医院是一所集医疗、教学、科研、预防为一体的大型现代化综合性医院。',
      businessHours: '周一至周日 08:00-17:00',
      status: 1,
      appointmentCount: 532
    },
    {
      id: 5,
      name: '四川大学华西医院',
      level: '三级甲等',
      city: '成都',
      address: '四川省成都市武侯区国学巷37号',
      contactPhone: '028-85422114',
      description: '四川大学华西医院是集医疗、教学、科研为一体的大型现代化综合性医院。',
      businessHours: '周一至周日 08:00-17:00',
      status: 1,
      appointmentCount: 478
    }
  ]
};

// 体检套餐数据
export const packageData = {
  list: [
    {
      id: 1,
      name: '基础体检套餐',
      price: 298,
      originalPrice: 399,
      description: '包含基本体格检查、血常规、尿常规、肝功能、肾功能等基础项目',
      suitableFor: '适合一般健康体检需求的成年人',
      items: [
        { id: 1, name: '身高体重测量', type: '体格检查' },
        { id: 2, name: '血压测量', type: '体格检查' },
        { id: 3, name: '血常规', type: '实验室检查' },
        { id: 4, name: '尿常规', type: '实验室检查' },
        { id: 5, name: '肝功能', type: '实验室检查' },
        { id: 6, name: '肾功能', type: '实验室检查' },
        { id: 7, name: '血糖', type: '实验室检查' },
        { id: 8, name: '血脂', type: '实验室检查' }
      ],
      status: 1,
      salesCount: 1245,
      createTime: '2023-03-10 10:00:00',
      updateTime: '2023-05-15 16:30:00'
    },
    {
      id: 2,
      name: '全面体检套餐',
      price: 698,
      originalPrice: 899,
      description: '在基础套餐的基础上增加了心电图、彩超、胸部CT等检查项目',
      suitableFor: '适合年龄在40岁以上需要全面体检的人群',
      items: [
        { id: 1, name: '身高体重测量', type: '体格检查' },
        { id: 2, name: '血压测量', type: '体格检查' },
        { id: 3, name: '血常规', type: '实验室检查' },
        { id: 4, name: '尿常规', type: '实验室检查' },
        { id: 5, name: '肝功能', type: '实验室检查' },
        { id: 6, name: '肾功能', type: '实验室检查' },
        { id: 7, name: '血糖', type: '实验室检查' },
        { id: 8, name: '血脂', type: '实验室检查' },
        { id: 9, name: '心电图', type: '影像学检查' },
        { id: 10, name: '腹部彩超', type: '影像学检查' },
        { id: 11, name: '胸部CT', type: '影像学检查' }
      ],
      status: 1,
      salesCount: 876,
      createTime: '2023-03-12 09:30:00',
      updateTime: '2023-05-20 15:20:00'
    },
    {
      id: 3,
      name: '妇科体检套餐',
      price: 598,
      originalPrice: 799,
      description: '在基础套餐的基础上增加了妇科检查、乳腺彩超、宫颈筛查等项目',
      suitableFor: '适合女性定期进行妇科健康检查',
      items: [
        { id: 1, name: '身高体重测量', type: '体格检查' },
        { id: 2, name: '血压测量', type: '体格检查' },
        { id: 3, name: '血常规', type: '实验室检查' },
        { id: 4, name: '尿常规', type: '实验室检查' },
        { id: 5, name: '肝功能', type: '实验室检查' },
        { id: 6, name: '肾功能', type: '实验室检查' },
        { id: 12, name: '妇科检查', type: '专科检查' },
        { id: 13, name: '乳腺彩超', type: '影像学检查' },
        { id: 14, name: '宫颈筛查', type: '实验室检查' },
        { id: 15, name: '盆腔彩超', type: '影像学检查' }
      ],
      status: 1,
      salesCount: 654,
      createTime: '2023-03-15 11:20:00',
      updateTime: '2023-06-01 10:10:00'
    },
    {
      id: 4,
      name: '老年人体检套餐',
      price: 798,
      originalPrice: 999,
      description: '专为老年人设计的体检套餐，增加了骨密度、动脉硬化等相关检查',
      suitableFor: '适合60岁以上老年人',
      items: [
        { id: 1, name: '身高体重测量', type: '体格检查' },
        { id: 2, name: '血压测量', type: '体格检查' },
        { id: 3, name: '血常规', type: '实验室检查' },
        { id: 4, name: '尿常规', type: '实验室检查' },
        { id: 5, name: '肝功能', type: '实验室检查' },
        { id: 6, name: '肾功能', type: '实验室检查' },
        { id: 7, name: '血糖', type: '实验室检查' },
        { id: 8, name: '血脂', type: '实验室检查' },
        { id: 9, name: '心电图', type: '影像学检查' },
        { id: 16, name: '骨密度检查', type: '影像学检查' },
        { id: 17, name: '动脉硬化检查', type: '专科检查' },
        { id: 18, name: '眼底检查', type: '专科检查' }
      ],
      status: 1,
      salesCount: 432,
      createTime: '2023-03-18 14:00:00',
      updateTime: '2023-06-05 11:30:00'
    },
    {
      id: 5,
      name: '男性体检套餐',
      price: 598,
      originalPrice: 799,
      description: '针对男性设计的体检套餐，增加了前列腺彩超等检查项目',
      suitableFor: '适合中年男性体检',
      items: [
        { id: 1, name: '身高体重测量', type: '体格检查' },
        { id: 2, name: '血压测量', type: '体格检查' },
        { id: 3, name: '血常规', type: '实验室检查' },
        { id: 4, name: '尿常规', type: '实验室检查' },
        { id: 5, name: '肝功能', type: '实验室检查' },
        { id: 6, name: '肾功能', type: '实验室检查' },
        { id: 7, name: '血糖', type: '实验室检查' },
        { id: 8, name: '血脂', type: '实验室检查' },
        { id: 19, name: '前列腺彩超', type: '影像学检查' },
        { id: 20, name: 'PSA检查', type: '实验室检查' }
      ],
      status: 1,
      salesCount: 587,
      createTime: '2023-03-20 16:40:00',
      updateTime: '2023-06-10 09:50:00'
    }
  ]
};

// 体检项目数据
export const checkItemData = {
  list: [
    {
      id: 1,
      name: '身高体重测量',
      category: '常规检查',
      description: '测量身高、体重，计算BMI指数',
      price: 10,
      duration: 5,
      status: 1
    },
    {
      id: 2,
      name: '血压测量',
      category: '常规检查',
      description: '测量收缩压和舒张压',
      price: 10,
      duration: 5,
      status: 1
    },
    {
      id: 3,
      name: '血常规',
      category: '实验室检查',
      description: '检测血红蛋白、白细胞、血小板等多项指标',
      price: 50,
      duration: 10,
      status: 1
    },
    {
      id: 4,
      name: '尿常规',
      category: '实验室检查',
      description: '检查尿液中的常规指标，如尿蛋白、尿糖等',
      price: 40,
      duration: 10,
      status: 1
    },
    {
      id: 5,
      name: '肝功能',
      category: '实验室检查',
      description: '检测肝酶、胆红素等指标，评估肝脏功能',
      price: 80,
      duration: 15,
      status: 1
    },
    {
      id: 6,
      name: '肾功能',
      category: '实验室检查',
      description: '检测肌酐、尿素氮等指标，评估肾脏功能',
      price: 80,
      duration: 15,
      status: 1
    },
    {
      id: 7,
      name: '血糖',
      category: '实验室检查',
      description: '检测空腹血糖水平',
      price: 30,
      duration: 10,
      status: 1
    },
    {
      id: 8,
      name: '血脂',
      category: '实验室检查',
      description: '检测总胆固醇、甘油三酯等血脂指标',
      price: 60,
      duration: 10,
      status: 1
    },
    {
      id: 9,
      name: '心电图',
      category: '影像学检查',
      description: '记录心脏的电活动，用于心脏疾病的筛查和诊断',
      price: 100,
      duration: 20,
      status: 1
    },
    {
      id: 10,
      name: '腹部彩超',
      category: '影像学检查',
      description: '使用超声波检查腹部器官，如肝脏、胆囊、胰腺等',
      price: 180,
      duration: 30,
      status: 1
    }
  ]
};

// 预约数据
export const appointmentData = {
  list: [
    {
      id: 1,
      appointmentNo: 'APT000001',
      username: 'user1',
      phone: '13900138001',
      packageName: '基础体检套餐',
      price: 298,
      hospital: '北京协和医院',
      hospitalAddress: '北京市东城区帅府园1号',
      appointmentDate: '2023-11-05',
      appointmentTime: '09:00',
      status: 'COMPLETED',
      createTime: '2023-10-20 10:15:30',
      remark: ''
    },
    {
      id: 2,
      appointmentNo: 'APT000002',
      username: 'user2',
      phone: '13900138002',
      packageName: '全面体检套餐',
      price: 698,
      hospital: '上海交通大学医学院附属仁济医院',
      hospitalAddress: '上海市浦东新区灵山路845号',
      appointmentDate: '2023-11-08',
      appointmentTime: '10:30',
      status: 'CONFIRMED',
      createTime: '2023-10-22 14:25:10',
      remark: '请提前30分钟到达'
    },
    {
      id: 3,
      appointmentNo: 'APT000003',
      username: 'user3',
      phone: '13900138003',
      packageName: '妇科体检套餐',
      price: 598,
      hospital: '广州中医药大学第一附属医院',
      hospitalAddress: '广州市白云区机场路16号',
      appointmentDate: '2023-11-10',
      appointmentTime: '08:30',
      status: 'PENDING',
      createTime: '2023-10-25 09:40:20',
      remark: ''
    },
    {
      id: 4,
      appointmentNo: 'APT000004',
      username: 'user4',
      phone: '13900138004',
      packageName: '老年人体检套餐',
      price: 798,
      hospital: '武汉同济医院',
      hospitalAddress: '湖北省武汉市硚口区解放大道1095号',
      appointmentDate: '2023-11-12',
      appointmentTime: '14:00',
      status: 'CANCELLED',
      createTime: '2023-10-28 16:35:15',
      remark: '用户取消'
    },
    {
      id: 5,
      appointmentNo: 'APT000005',
      username: 'user5',
      phone: '13900138005',
      packageName: '男性体检套餐',
      price: 598,
      hospital: '四川大学华西医院',
      hospitalAddress: '四川省成都市武侯区国学巷37号',
      appointmentDate: '2023-11-15',
      appointmentTime: '09:30',
      status: 'CONFIRMED',
      createTime: '2023-10-30 11:20:45',
      remark: ''
    }
  ]
};

// 财务数据
export const financeData = {
  list: [
    {
      id: 1,
      orderNo: 'ORD000001',
      username: 'user1',
      packageName: '基础体检套餐',
      amount: 298,
      status: 'PAID',
      paymentMethod: '微信支付',
      createTime: '2023-10-20 10:15:30',
      payTime: '2023-10-20 10:17:45'
    },
    {
      id: 2,
      orderNo: 'ORD000002',
      username: 'user2',
      packageName: '全面体检套餐',
      amount: 698,
      status: 'PAID',
      paymentMethod: '支付宝',
      createTime: '2023-10-22 14:25:10',
      payTime: '2023-10-22 14:28:35'
    },
    {
      id: 3,
      orderNo: 'ORD000003',
      username: 'user3',
      packageName: '妇科体检套餐',
      amount: 598,
      status: 'UNPAID',
      paymentMethod: '待支付',
      createTime: '2023-10-25 09:40:20',
      payTime: '--'
    },
    {
      id: 4,
      orderNo: 'ORD000004',
      username: 'user4',
      packageName: '老年人体检套餐',
      amount: 798,
      status: 'REFUNDED',
      paymentMethod: '微信支付',
      createTime: '2023-10-28 16:35:15',
      payTime: '2023-10-28 16:38:20'
    },
    {
      id: 5,
      orderNo: 'ORD000005',
      username: 'user5',
      packageName: '男性体检套餐',
      amount: 598,
      status: 'PAID',
      paymentMethod: '银行卡',
      createTime: '2023-10-30 11:20:45',
      payTime: '2023-10-30 11:25:10'
    }
  ],
  stats: {
    totalIncome: 1594,
    totalOrders: 5,
    refundAmount: 798
  }
};

// 系统设置数据
export const settingsData = {
  basicSettings: {
    systemName: '预约体检系统',
    logoUrl: '',
    description: '预约体检系统是一个一体化平台，旨在实现体检用户在线预约体检服务、医生管理体检项目和查看体检报告、管理员进行系统维护和数据管理的功能。',
    supportContact: '400-123-4567'
  },
  appointmentSettings: {
    advanceDays: 14,
    maxAppointmentsPerDay: 100,
    timeSlots: [
      { startTime: '08:00', endTime: '10:00', maxCount: 30 },
      { startTime: '10:00', endTime: '12:00', maxCount: 30 },
      { startTime: '14:00', endTime: '16:00', maxCount: 30 }
    ],
    disabledDates: []
  },
  paymentSettings: {
    enableAlipay: true,
    alipayAppId: '2021000000000000',
    alipayPrivateKey: '******',
    alipayPublicKey: '******',
    enableWechat: true,
    wechatMchId: '1900000000',
    wechatAppId: 'wx1234567890',
    wechatKey: '******'
  }
};

/**
 * 模拟API响应
 * @param {Object} data 响应数据
 * @param {Number} delay 延迟时间(ms)
 * @returns {Promise} Promise对象
 */
export function mockResponse(data, delay = 500) {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '操作成功',
        data
      });
    }, delay);
  });
}

/**
 * 检查是否为API模拟模式
 */
export const isMockMode = true;

// 模拟管理员信息
export const adminInfo = {
  id: 1,
  username: 'admin',
  name: '系统管理员',
  avatar: '',
  role: 'admin',
  permissions: ['*']
};

// 模拟仪表盘数据
export const dashboardData = {
  todayAppointments: 15,
  totalAppointments: 1245,
  todayIncome: 8976,
  totalIncome: 758930,
  userCount: 2560,
  packageCount: 12,
  hospitalCount: 8,
  doctorCount: 45,
  appointmentChart: {
    xData: ['10-01', '10-02', '10-03', '10-04', '10-05', '10-06', '10-07'],
    yData: [25, 32, 18, 30, 28, 35, 22]
  },
  incomeChart: {
    xData: ['10-01', '10-02', '10-03', '10-04', '10-05', '10-06', '10-07'],
    yData: [8900, 12500, 7600, 10200, 9800, 13500, 8200]
  },
  packageRank: [
    { name: '全面体检套餐', value: 452 },
    { name: '基础体检套餐', value: 320 },
    { name: '妇科体检套餐', value: 280 },
    { name: '男性体检套餐', value: 190 },
    { name: '老年人体检套餐', value: 160 }
  ],
  hospitalRank: [
    { name: '北京协和医院', value: 320 },
    { name: '上海交通大学医学院附属仁济医院', value: 280 },
    { name: '广州中医药大学第一附属医院', value: 250 },
    { name: '武汉同济医院', value: 220 },
    { name: '四川大学华西医院', value: 180 }
  ]
}; 