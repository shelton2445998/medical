create table orders
(
    id               bigint                             not null comment '主键'
        primary key,
    user_id          bigint                             not null comment '用户ID',
    setmeal_id       bigint                             not null comment '套餐ID',
    hospital_id      bigint                             not null comment '医院ID',
    doctor_id        bigint                             not null comment '医生ID',
    appointment_date date                               not null comment '预约日期',
    time_slot        varchar(20)                        not null comment '时间段',
    status           tinyint  default 1                 not null comment '状态 0:已取消，1:待支付，2:已支付，3:已完成',
    amount           decimal(10, 2)                     null comment '订单金额',
    pay_time         datetime                           null comment '支付时间',
    pay_type         tinyint                            null comment '支付方式 1:支付宝，2:微信',
    transaction_id   varchar(100)                       null comment '支付交易号',
    create_id        bigint                             null comment '创建人ID',
    create_time      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id        bigint                             null comment '修改人ID',
    update_time      datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '体检预约订单';

create index order_appointment_date_index
    on orders (appointment_date);

create index order_doctor_index
    on orders (doctor_id);

create index order_hospital_index
    on orders (hospital_id);

create index order_setmeal_index
    on orders (setmeal_id);

create index order_user_index
    on orders (user_id);

INSERT INTO medical.orders (id, order_number, user_id, setmeal_id, hospital_id, doctor_id, appointment_date, time_slot, status, amount, price, pay_time, cancel_time, pay_type, transaction_id, create_id, create_time, update_id, update_time) VALUES (10001, '', 5001, 4001, 1001, 3001, '2025-07-12', '7:00-8:00', 1, null, null, null, null, null, null, null, '2025-07-12 15:31:29', null, '2025-07-12 15:31:29');
INSERT INTO medical.orders (id, order_number, user_id, setmeal_id, hospital_id, doctor_id, appointment_date, time_slot, status, amount, price, pay_time, cancel_time, pay_type, transaction_id, create_id, create_time, update_id, update_time) VALUES (697022964727813, 'ORD1752308024227', 5001, 4003, 1002, 3001, '2025-07-12', '上午(08:00-12:00)', 0, null, 499.00, null, null, null, null, 5001, '2025-07-12 16:13:44', null, '2025-07-12 16:13:48');
INSERT INTO medical.orders (id, order_number, user_id, setmeal_id, hospital_id, doctor_id, appointment_date, time_slot, status, amount, price, pay_time, cancel_time, pay_type, transaction_id, create_id, create_time, update_id, update_time) VALUES (697025642848261, 'ORD1752308678085', 5001, 4003, 1002, 3001, '2025-07-12', '晚上(19:00-21:00)', 0, null, 499.00, null, null, null, null, 5001, '2025-07-12 16:24:38', null, '2025-07-12 16:24:42');
INSERT INTO medical.orders (id, order_number, user_id, setmeal_id, hospital_id, doctor_id, appointment_date, time_slot, status, amount, price, pay_time, cancel_time, pay_type, transaction_id, create_id, create_time, update_id, update_time) VALUES (697030803365893, 'ORD1752309937978', 5001, 4001, 1001, 3001, '2025-07-12', '上午(08:00-12:00)', 0, null, 299.00, null, null, null, null, 5001, '2025-07-12 16:45:38', null, '2025-07-12 16:45:42');
INSERT INTO medical.orders (id, order_number, user_id, setmeal_id, hospital_id, doctor_id, appointment_date, time_slot, status, amount, price, pay_time, cancel_time, pay_type, transaction_id, create_id, create_time, update_id, update_time) VALUES (697623286071301, 'ORD1752454587073', 5001, 4002, 696263284899845, 3003, '2025-07-14', '上午(08:00-12:00)', 1, null, 699.00, null, null, null, null, 5001, '2025-07-14 08:56:27', null, '2025-07-14 08:56:29');
INSERT INTO medical.orders (id, order_number, user_id, setmeal_id, hospital_id, doctor_id, appointment_date, time_slot, status, amount, price, pay_time, cancel_time, pay_type, transaction_id, create_id, create_time, update_id, update_time) VALUES (697624637636613, 'ORD1752454917048', 5001, 4003, 1002, 3001, '2025-07-14', '上午(08:00-12:00)', 1, null, 499.00, null, null, null, null, 5001, '2025-07-14 09:01:57', null, '2025-07-14 09:01:59');