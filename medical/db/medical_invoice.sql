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

INSERT INTO medical.invoice (id, order_id, title, tax_number, amount, type, content, status, pdf_url, create_id, create_time, update_id, update_time) VALUES (12001, 10002, '个人', null, 599.00, 1, '体检服务费', 1, '/invoices/INV10002.pdf', 5002, '2025-07-12 16:35:00', null, '2025-07-12 16:35:00');
INSERT INTO medical.invoice (id, order_id, title, tax_number, amount, type, content, status, pdf_url, create_id, create_time, update_id, update_time) VALUES (12002, 10004, '个人', null, 269.00, 1, '体检服务费', 1, '/invoices/INV10004.pdf', 5002, '2025-07-20 15:45:00', null, '2025-07-20 15:45:00');