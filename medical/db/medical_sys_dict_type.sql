create table sys_dict_type
(
    id          bigint                               not null comment '主键'
        primary key,
    code        varchar(100)                         not null comment '字典类型编码',
    name        varchar(100)                         not null comment '字典类型名称',
    is_system   tinyint(1) default 0                 not null comment '是否是系统字典类型',
    remark      varchar(200)                         null comment '备注',
    create_id   bigint                               null comment '创建人ID',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                               null comment '修改人ID',
    update_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    constraint sys_dict_type_code_uindex
        unique (code)
)
    comment '字典类型';

INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1001, 'gender', '性别', 1, '性别：0-女，1-男', 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1002, 'setmeal_type', '体检套餐类型', 1, '体检套餐类型：1-基础，2-高级，3-专项', 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1003, 'order_status', '订单状态', 1, '订单状态：0-已取消，1-待支付，2-已支付，3-已完成', 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1004, 'pay_type', '支付方式', 1, '支付方式：1-支付宝，2-微信', 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1005, 'report_status', '报告状态', 1, '报告状态：0-未生成，1-已生成', 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1006, 'notification_type', '通知类型', 1, '通知类型：1-预约提醒，2-报告通知，3-系统公告', 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1007, 'invoice_type', '发票类型', 1, '发票类型：1-个人，2-企业', 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict_type (id, code, name, is_system, remark, create_id, create_time, update_id, update_time) VALUES (1008, 'time_slot', '预约时段', 1, '预约时间段设置', 1, '2025-07-09 09:47:44', null, '2025-07-09 09:47:44');