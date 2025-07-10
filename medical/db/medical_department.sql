create table department
(
    id          bigint                               not null comment '主键'
        primary key,
    name        varchar(50)                          not null comment '科室名称',
    hospital_id bigint                               null comment '所属医院ID',
    description varchar(200)                         null comment '描述',
    status      tinyint(1) default 1                 not null comment '状态 0:禁用，1:启用',
    create_id   bigint                               null comment '创建人ID',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                               null comment '修改人ID',
    update_time datetime   default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '科室';

create index department_hospital_index
    on department (hospital_id);

INSERT INTO medical.department (id, name, hospital_id, description, status, create_id, create_time, update_id, update_time) VALUES (2001, '内科', 1001, '内科诊疗各种常见病、多发病', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.department (id, name, hospital_id, description, status, create_id, create_time, update_id, update_time) VALUES (2002, '外科', 1001, '外科治疗各种外伤及需要手术的疾病', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.department (id, name, hospital_id, description, status, create_id, create_time, update_id, update_time) VALUES (2003, '妇科', 1001, '专注女性健康问题', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.department (id, name, hospital_id, description, status, create_id, create_time, update_id, update_time) VALUES (2004, '儿科', 1001, '专注儿童健康问题', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.department (id, name, hospital_id, description, status, create_id, create_time, update_id, update_time) VALUES (2005, '心脑血管科', 1002, '专注心脑血管疾病', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.department (id, name, hospital_id, description, status, create_id, create_time, update_id, update_time) VALUES (2006, '影像科', 1002, '提供各类医学影像检查', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.department (id, name, hospital_id, description, status, create_id, create_time, update_id, update_time) VALUES (2007, '检验科', 1002, '提供各类医学检验服务', 1, 1, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');