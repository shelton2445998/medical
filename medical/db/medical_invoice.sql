create table invoice
(
    id          bigint                             not null comment '主键'
        primary key,
    order_id    bigint                             not null comment '订单ID',
    title       varchar(100)                       not null comment '发票抬头',
    tax_number  varchar(50)                        null comment '税号',
    amount      decimal(10, 2)                     not null comment '发票金额',
    type        tinyint                            not null comment '发票类型 1:个人，2:企业',
    content     varchar(100)                       null comment '发票内容',
    status      tinyint  default 0                 not null comment '状态 0:未开，1:已开',
    pdf_url     varchar(255)                       null comment 'PDF文件地址',
    create_id   bigint                             null comment '创建人ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                             null comment '修改人ID',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '电子发票';

create index invoice_order_index
    on invoice (order_id);

