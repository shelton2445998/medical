create table report_item
(
    id            bigint                             not null comment '主键'
        primary key,
    order_id      bigint                             not null comment '订单ID',
    user_id       bigint                             null comment '用户ID',
    item_id       bigint                             not null comment '检查项ID',
    doctor_id     bigint                             not null comment '医生ID',
    report_status tinyint  default 0                 not null comment '报告状态 0:未生成，1:已生成',
    conclusion    varchar(500)                       null comment '检查结论',
    create_id     bigint                             null comment '创建人ID',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id     bigint                             null comment '修改人ID',
    update_time   datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '体检报告检查项信息';

create index report_doctor_index
    on report_item (doctor_id);

create index report_item_index
    on report_item (item_id);

create index report_order_index
    on report_item (order_id);

create index report_user_index
    on report_item (user_id);

INSERT INTO medical.report_item (id, order_id, user_id, item_id, doctor_id, report_status, conclusion, create_id, create_time, update_id, update_time) VALUES (13001, 10004, 5001, 6001, 3001, 1, '血液指标正常', 3001, '2025-07-20 14:30:00', null, '2025-07-20 14:30:00');
INSERT INTO medical.report_item (id, order_id, user_id, item_id, doctor_id, report_status, conclusion, create_id, create_time, update_id, update_time) VALUES (13002, 10004, 5001, 6002, 3001, 1, '尿液指标正常', 3001, '2025-07-20 14:45:00', null, '2025-07-20 14:45:00');
INSERT INTO medical.report_item (id, order_id, user_id, item_id, doctor_id, report_status, conclusion, create_id, create_time, update_id, update_time) VALUES (20001, 10001, null, 6001, 3001, 0, null, null, '2025-07-12 15:42:56', null, '2025-07-12 15:42:56');