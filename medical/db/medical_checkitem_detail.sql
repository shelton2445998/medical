create table checkitem_detail
(
    id           bigint                             not null comment '主键'
        primary key,
    item_id      bigint                             not null comment '检查项ID',
    name         varchar(100)                       not null comment '明细名称',
    normal_value varchar(100)                       null comment '正常值范围',
    unit         varchar(20)                        null comment '单位',
    create_id    bigint                             null comment '创建人ID',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id    bigint                             null comment '修改人ID',
    update_time  datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '检查项明细';

create index checkitem_detail_item_index
    on checkitem_detail (item_id);

INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7001, 6001, '红细胞计数', '4.0-5.5', '10^12/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7002, 6001, '血红蛋白', '120-160', 'g/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7003, 6001, '白细胞计数', '4.0-10.0', '10^9/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7004, 6001, '血小板计数', '100-300', '10^9/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7005, 6002, '尿蛋白', '阴性', '', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7006, 6002, '尿糖', '阴性', '', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7007, 6003, '谷丙转氨酶(ALT)', '0-40', 'U/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7008, 6003, '谷草转氨酶(AST)', '0-40', 'U/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7009, 6004, '肌酐', '44-106', 'umol/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7010, 6004, '尿素氮', '3.2-7.1', 'mmol/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7011, 6008, '总胆固醇', '3.1-5.7', 'mmol/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');
INSERT INTO medical.checkitem_detail (id, item_id, name, normal_value, unit, create_id, create_time, update_id, update_time) VALUES (7012, 6008, '甘油三酯', '0.4-1.7', 'mmol/L', 1, '2025-07-09 09:47:39', null, '2025-07-09 09:47:39');