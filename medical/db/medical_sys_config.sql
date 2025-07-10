create table sys_config
(
    id           bigint                               not null comment '主键'
        primary key,
    config_name  varchar(100)                         not null comment '配置名称',
    config_key   varchar(100)                         not null comment '配置key',
    config_value varchar(200)                         not null comment '配置值',
    is_system    tinyint(1) default 0                 not null comment '是否是系统字典类型',
    status       tinyint(1) default 1                 not null comment '状态 1：正常，0：禁用',
    remark       varchar(200)                         null comment '备注',
    create_id    bigint                               null comment '创建人ID',
    update_id    bigint                               null comment '修改人ID',
    create_time  datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    constraint sys_config_config_key_uindex
        unique (config_key)
)
    comment '系统配置';

INSERT INTO medical.sys_config (id, config_name, config_key, config_value, is_system, status, remark, create_id, update_id, create_time, update_time) VALUES (1001, '系统名称', 'system.name', '预约体检管理系统', 1, 1, '系统名称配置', 1, null, '2025-07-09 09:47:34', '2025-07-09 09:47:34');
INSERT INTO medical.sys_config (id, config_name, config_key, config_value, is_system, status, remark, create_id, update_id, create_time, update_time) VALUES (1002, '系统版本', 'system.version', '1.0.0', 1, 1, '系统版本配置', 1, null, '2025-07-09 09:47:34', '2025-07-09 09:47:34');
INSERT INTO medical.sys_config (id, config_name, config_key, config_value, is_system, status, remark, create_id, update_id, create_time, update_time) VALUES (1003, '每日最大预约人数', 'appointment.max.daily', '100', 1, 1, '每日最大预约人数', 1, null, '2025-07-09 09:47:34', '2025-07-09 09:47:34');
INSERT INTO medical.sys_config (id, config_name, config_key, config_value, is_system, status, remark, create_id, update_id, create_time, update_time) VALUES (1004, '预约提前天数', 'appointment.advance.days', '7', 1, 1, '允许提前预约的天数', 1, null, '2025-07-09 09:47:34', '2025-07-09 09:47:34');
INSERT INTO medical.sys_config (id, config_name, config_key, config_value, is_system, status, remark, create_id, update_id, create_time, update_time) VALUES (1005, '取消预约退款期限(小时)', 'appointment.cancel.refund.hours', '24', 1, 1, '取消预约后可退款的时间限制(小时)', 1, null, '2025-07-09 09:47:34', '2025-07-09 09:47:34');
INSERT INTO medical.sys_config (id, config_name, config_key, config_value, is_system, status, remark, create_id, update_id, create_time, update_time) VALUES (1006, '短信验证码有效期(分钟)', 'sms.code.expire.minutes', '5', 1, 1, '短信验证码有效期(分钟)', 1, null, '2025-07-09 09:47:34', '2025-07-09 09:47:34');