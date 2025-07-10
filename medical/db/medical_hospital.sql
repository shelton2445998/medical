create table hospital
(
    id          bigint                               not null comment '主键'
        primary key,
    name        varchar(100)                         not null comment '医院名称',
    phone       varchar(20)                          null comment '医院电话',
    address     varchar(200)                         null comment '医院地址',
    description varchar(500)                         null comment '医院简介',
    status      tinyint(1) default 1                 not null comment '状态 0:禁用，1:启用',
    create_id   bigint                               null comment '创建人ID',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                               null comment '修改人ID',
    update_time datetime   default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '医院';

INSERT INTO medical.hospital (id, name, phone, address, description, status, create_id, create_time, update_id, update_time) VALUES (1001, '第一人民医院', '0123-12345678', '某某市中心路123号', '三甲综合医院，提供全面的医疗服务', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.hospital (id, name, phone, address, description, status, create_id, create_time, update_id, update_time) VALUES (1002, '第二人民医院', '0123-87654321', '某某市健康路456号', '专业体检中心，设备先进，服务贴心', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');