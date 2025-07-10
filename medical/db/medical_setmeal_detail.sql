create table setmeal_detail
(
    id          bigint                             not null comment '主键'
        primary key,
    setmeal_id  bigint                             not null comment '套餐ID',
    item_id     bigint                             not null comment '检查项ID',
    create_id   bigint                             null comment '创建人ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                             null comment '修改人ID',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '体检套餐明细';

create index setmeal_detail_item_index
    on setmeal_detail (item_id);

create index setmeal_detail_setmeal_index
    on setmeal_detail (setmeal_id);

INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8001, 4001, 6001, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8002, 4001, 6002, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8003, 4001, 6003, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8004, 4001, 6005, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8005, 4002, 6001, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8006, 4002, 6002, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8007, 4002, 6003, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8008, 4002, 6004, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8009, 4002, 6005, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8010, 4002, 6006, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8011, 4002, 6007, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8012, 4002, 6008, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8013, 4003, 6001, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8014, 4003, 6005, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.setmeal_detail (id, setmeal_id, item_id, create_id, create_time, update_id, update_time) VALUES (8015, 4003, 6008, 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');