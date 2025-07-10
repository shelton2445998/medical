create table overall_result
(
    id          bigint                             not null comment '主键'
        primary key,
    order_id    bigint                             not null comment '订单ID',
    content     text                               null comment '结论内容',
    doctor_id   bigint                             not null comment '医生ID',
    create_id   bigint                             null comment '创建人ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                             null comment '修改人ID',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '总检结论';

create index result_doctor_index
    on overall_result (doctor_id);

create index result_order_index
    on overall_result (order_id);

