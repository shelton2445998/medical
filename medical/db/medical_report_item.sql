create table report_item
(
    id            bigint                             not null comment '主键'
        primary key,
    order_id      bigint                             not null comment '订单ID',
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

