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

