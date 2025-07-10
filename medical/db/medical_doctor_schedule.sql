create table doctor_schedule
(
    id              bigint                               not null comment '主键'
        primary key,
    doctor_id       bigint                               not null comment '医生ID',
    work_date       date                                 not null comment '工作日期',
    time_slot       varchar(20)                          not null comment '时间段',
    max_number      int        default 20                not null comment '最大可预约人数',
    reserved_number int        default 0                 not null comment '已预约人数',
    status          tinyint(1) default 1                 not null comment '状态 0:停诊，1:正常',
    create_id       bigint                               null comment '创建人ID',
    create_time     datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id       bigint                               null comment '修改人ID',
    update_time     datetime   default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '医生排班';

create index schedule_date_index
    on doctor_schedule (work_date);

create index schedule_doctor_index
    on doctor_schedule (doctor_id);

INSERT INTO medical.doctor_schedule (id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time, update_id, update_time) VALUES (9001, 3001, '2025-07-10', '上午(08:00-12:00)', 10, 0, 1, 1, '2025-07-09 09:47:40', null, '2025-07-09 09:47:40');
INSERT INTO medical.doctor_schedule (id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time, update_id, update_time) VALUES (9002, 3001, '2025-07-10', '下午(14:00-18:00)', 8, 0, 1, 1, '2025-07-09 09:47:40', null, '2025-07-09 09:47:40');
INSERT INTO medical.doctor_schedule (id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time, update_id, update_time) VALUES (9003, 3001, '2025-07-11', '上午(08:00-12:00)', 10, 0, 1, 1, '2025-07-09 09:47:40', null, '2025-07-09 09:47:40');
INSERT INTO medical.doctor_schedule (id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time, update_id, update_time) VALUES (9004, 3002, '2025-07-10', '上午(08:00-12:00)', 12, 0, 1, 1, '2025-07-09 09:47:40', null, '2025-07-09 09:47:40');
INSERT INTO medical.doctor_schedule (id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time, update_id, update_time) VALUES (9005, 3002, '2025-07-11', '下午(14:00-18:00)', 8, 0, 1, 1, '2025-07-09 09:47:40', null, '2025-07-09 09:47:40');
INSERT INTO medical.doctor_schedule (id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time, update_id, update_time) VALUES (9006, 3003, '2025-07-11', '上午(08:00-12:00)', 15, 0, 1, 1, '2025-07-09 09:47:40', null, '2025-07-09 09:47:40');
INSERT INTO medical.doctor_schedule (id, doctor_id, work_date, time_slot, max_number, reserved_number, status, create_id, create_time, update_id, update_time) VALUES (9007, 3003, '2025-07-12', '下午(14:00-18:00)', 10, 0, 1, 1, '2025-07-09 09:47:40', null, '2025-07-09 09:47:40');