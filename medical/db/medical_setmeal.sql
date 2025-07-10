create table setmeal
(
    id             bigint                               not null comment '主键'
        primary key,
    name           varchar(100)                         not null comment '套餐名称',
    type           tinyint                              not null comment '套餐类型 1:基础，2:高级，3:专项',
    price          decimal(10, 2)                       null comment '价格',
    discount_price decimal(10, 2)                       null comment '折扣价',
    description    varchar(500)                         null comment '描述',
    status         tinyint(1) default 1                 not null comment '状态 0:下架，1:上架',
    create_id      bigint                               null comment '创建人ID',
    create_time    datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id      bigint                               null comment '修改人ID',
    update_time    datetime   default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '体检套餐';

INSERT INTO medical.setmeal (id, name, type, price, discount_price, description, status, create_id, create_time, update_id, update_time) VALUES (4001, '基础体检套餐', 1, 299.00, 269.00, '包含常规体检项目，适合一般健康检查', 1, 1, '2025-07-09 09:47:37', null, '2025-07-09 09:47:37');
INSERT INTO medical.setmeal (id, name, type, price, discount_price, description, status, create_id, create_time, update_id, update_time) VALUES (4002, '高级体检套餐', 2, 699.00, 599.00, '包含基础套餐及更多专项检查，适合中老年人', 1, 1, '2025-07-09 09:47:37', null, '2025-07-09 09:47:37');
INSERT INTO medical.setmeal (id, name, type, price, discount_price, description, status, create_id, create_time, update_id, update_time) VALUES (4003, '心脑血管专项套餐', 3, 499.00, 459.00, '针对心脑血管疾病的专项检查，适合高风险人群', 1, 1, '2025-07-09 09:47:37', null, '2025-07-09 09:47:37');