create table notification
(
    id          bigint                               not null comment '主键'
        primary key,
    user_id     bigint                               null comment '接收用户ID',
    doctor_id   bigint                               null comment '接收医生ID',
    title       varchar(100)                         not null comment '通知标题',
    content     text                                 null comment '通知内容',
    type        tinyint                              not null comment '通知类型 1:预约提醒，2:报告通知，3:系统公告',
    is_read     tinyint(1) default 0                 not null comment '是否已读 0:未读，1:已读',
    create_id   bigint                               null comment '创建人ID',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                               null comment '修改人ID',
    update_time datetime   default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '系统通知';

create index notification_doctor_index
    on notification (doctor_id);

create index notification_user_index
    on notification (user_id);

INSERT INTO medical.notification (id, user_id, doctor_id, title, content, type, is_read, create_id, create_time, update_id, update_time) VALUES (10001, null, null, '系统正式上线公告', '尊敬的用户，预约体检系统已正式上线，欢迎使用！', 3, 0, 1, '2025-07-09 09:47:42', null, '2025-07-09 09:47:42');
INSERT INTO medical.notification (id, user_id, doctor_id, title, content, type, is_read, create_id, create_time, update_id, update_time) VALUES (10002, 5001, null, '预约成功通知', '您已成功预约2025-07-12的体检服务，请按时到院。', 1, 0, 1, '2025-07-12 15:31:29', null, '2025-07-12 15:31:29');
INSERT INTO medical.notification (id, user_id, doctor_id, title, content, type, is_read, create_id, create_time, update_id, update_time) VALUES (10003, 5002, null, '支付成功通知', '您的体检预约订单已支付成功，期待您的光临。', 1, 1, 1, '2025-07-12 16:30:00', null, '2025-07-12 16:30:00');
INSERT INTO medical.notification (id, user_id, doctor_id, title, content, type, is_read, create_id, create_time, update_id, update_time) VALUES (10004, 5001, null, '预约取消通知', '您的2025-07-15体检预约已取消。', 1, 0, 1, '2025-07-11 14:22:35', null, '2025-07-11 14:22:35');
INSERT INTO medical.notification (id, user_id, doctor_id, title, content, type, is_read, create_id, create_time, update_id, update_time) VALUES (10005, 5002, null, '体检报告通知', '您的体检报告已生成，请登录系统查看或到院领取。', 2, 0, 1, '2025-07-20 16:40:00', null, '2025-07-20 16:40:00');
INSERT INTO medical.notification (id, user_id, doctor_id, title, content, type, is_read, create_id, create_time, update_id, update_time) VALUES (10006, null, 3001, '新预约通知', '您有一个新的预约安排在2025-07-12，请注意查看。', 1, 0, 1, '2025-07-12 15:31:29', null, '2025-07-12 15:31:29');