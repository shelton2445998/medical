create table checkitem
(
    id            bigint                               not null comment '主键'
        primary key,
    name          varchar(100)                         not null comment '检查项名称',
    department_id bigint                               null comment '科室ID',
    price         decimal(10, 2)                       null comment '价格',
    description   varchar(500)                         null comment '描述',
    status        tinyint(1) default 1                 not null comment '状态 0:禁用，1:启用',
    create_id     bigint                               null comment '创建人ID',
    create_time   datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id     bigint                               null comment '修改人ID',
    update_time   datetime   default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '检查项';

create index checkitem_department_index
    on checkitem (department_id);

INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6001, '血常规', 2007, 30.00, '检测血液中各种细胞成分及数量', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');
INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6002, '尿常规', 2007, 20.00, '检测尿液中的各项生化指标', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');
INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6003, '肝功能', 2007, 50.00, '检测肝脏功能相关指标', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');
INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6004, '肾功能', 2007, 50.00, '检测肾脏功能相关指标', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');
INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6005, '心电图', 2005, 40.00, '检测心脏电活动情况', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');
INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6006, '胸部X光', 2006, 60.00, '检测胸部器官情况', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');
INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6007, '腹部B超', 2006, 80.00, '检测腹部器官情况', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');
INSERT INTO medical.checkitem (id, name, department_id, price, description, status, create_id, create_time, update_id, update_time) VALUES (6008, '血脂检查', 2007, 40.00, '检测血液中脂质含量', 1, 1, '2025-07-09 09:47:38', null, '2025-07-09 09:47:38');