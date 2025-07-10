create table sys_dict
(
    id          bigint                               not null comment '主键'
        primary key,
    value       varchar(200)                         not null comment '字典值',
    label       varchar(100)                         not null comment '字典名称',
    dict_code   varchar(100)                         not null comment '字典类型code',
    status      tinyint(1) default 1                 not null comment '状态 1：启用，0：禁用',
    sort        int        default 0                 not null comment '排序值',
    remark      varchar(200)                         null comment '备注',
    create_id   bigint                               null comment '创建人ID',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                               null comment '修改人ID',
    update_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    constraint sys_dict_dict_code_value_uindex
        unique (dict_code, value)
)
    comment '字典数据';

create index sys_dict_dict_type_index
    on sys_dict (dict_code);

create index sys_dict_label_index
    on sys_dict (label);

create index sys_dict_value_index
    on sys_dict (value);

INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2001, '0', '女', 'gender', 1, 1, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2002, '1', '男', 'gender', 1, 2, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2003, '1', '基础', 'setmeal_type', 1, 1, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2004, '2', '高级', 'setmeal_type', 1, 2, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2005, '3', '专项', 'setmeal_type', 1, 3, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2006, '0', '已取消', 'order_status', 1, 1, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2007, '1', '待支付', 'order_status', 1, 2, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2008, '2', '已支付', 'order_status', 1, 3, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2009, '3', '已完成', 'order_status', 1, 4, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2010, '1', '支付宝', 'pay_type', 1, 1, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2011, '2', '微信', 'pay_type', 1, 2, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2012, '0', '未生成', 'report_status', 1, 1, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2013, '1', '已生成', 'report_status', 1, 2, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2014, '1', '预约提醒', 'notification_type', 1, 1, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2015, '2', '报告通知', 'notification_type', 1, 2, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2016, '3', '系统公告', 'notification_type', 1, 3, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2017, '1', '个人', 'invoice_type', 1, 1, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2018, '2', '企业', 'invoice_type', 1, 2, null, 1, '2025-07-09 09:47:32', null, '2025-07-09 09:47:32');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2019, '上午(08:00-12:00)', '上午(08:00-12:00)', 'time_slot', 1, 1, null, 1, '2025-07-09 09:47:45', null, '2025-07-09 09:47:45');
INSERT INTO medical.sys_dict (id, value, label, dict_code, status, sort, remark, create_id, create_time, update_id, update_time) VALUES (2020, '下午(14:00-18:00)', '下午(14:00-18:00)', 'time_slot', 1, 2, null, 1, '2025-07-09 09:47:45', null, '2025-07-09 09:47:45');