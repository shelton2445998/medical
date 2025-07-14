create table report_item_detail
(
    id          bigint                             not null comment '主键'
        primary key,
    report_id   bigint                             not null comment '报告ID',
    detail_id   bigint                             not null comment '检查项明细ID',
    value       varchar(100)                       null comment '检查值',
    is_abnormal tinyint  default 0                 not null comment '是否异常 0:正常，1:异常',
    create_id   bigint                             null comment '创建人ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                             null comment '修改人ID',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '体检报告检查项明细';

create index detail_item_index
    on report_item_detail (detail_id);

create index detail_report_index
    on report_item_detail (report_id);

INSERT INTO medical.report_item_detail (id, report_id, detail_id, value, is_abnormal, create_id, create_time, update_id, update_time) VALUES (14001, 13001, 7001, '4.8', 0, 3001, '2025-07-20 15:00:00', null, '2025-07-20 15:00:00');
INSERT INTO medical.report_item_detail (id, report_id, detail_id, value, is_abnormal, create_id, create_time, update_id, update_time) VALUES (14002, 13001, 7002, '145', 0, 3001, '2025-07-20 15:00:00', null, '2025-07-20 15:00:00');
INSERT INTO medical.report_item_detail (id, report_id, detail_id, value, is_abnormal, create_id, create_time, update_id, update_time) VALUES (14003, 13001, 7003, '6.5', 0, 3001, '2025-07-20 15:00:00', null, '2025-07-20 15:00:00');
INSERT INTO medical.report_item_detail (id, report_id, detail_id, value, is_abnormal, create_id, create_time, update_id, update_time) VALUES (14004, 13001, 7004, '230', 0, 3001, '2025-07-20 15:00:00', null, '2025-07-20 15:00:00');
INSERT INTO medical.report_item_detail (id, report_id, detail_id, value, is_abnormal, create_id, create_time, update_id, update_time) VALUES (14005, 13002, 7005, '阴性', 0, 3001, '2025-07-20 15:10:00', null, '2025-07-20 15:10:00');
INSERT INTO medical.report_item_detail (id, report_id, detail_id, value, is_abnormal, create_id, create_time, update_id, update_time) VALUES (14006, 13002, 7006, '阴性', 0, 3001, '2025-07-20 15:10:00', null, '2025-07-20 15:10:00');