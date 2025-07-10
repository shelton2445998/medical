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

