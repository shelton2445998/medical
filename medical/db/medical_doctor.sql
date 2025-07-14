create table doctor
(
    id            bigint                               not null comment '主键'
        primary key,
    name          varchar(32)                          not null comment '医生姓名',
    password      varchar(64)                          not null comment '密码',
    salt          varchar(32)                          not null comment '盐值',
    gender        tinyint(1)                           null comment '性别 0:女，1:男',
    mobile        varchar(20)                          null comment '手机号',
    email         varchar(100)                         null comment '邮箱',
    hospital_id   bigint                               null comment '所属医院ID',
    department_id bigint                               null comment '科室ID',
    title         varchar(50)                          null comment '职称',
    introduction  varchar(500)                         null comment '简介',
    avatar_url    varchar(255)                         null comment '头像URL',
    status        tinyint(1) default 1                 not null comment '状态 0:禁用，1:启用',
    create_id     bigint                               null comment '创建人ID',
    create_time   datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_id     bigint                               null comment '修改人ID',
    update_time   datetime   default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '医生';

create index doctor_department_index
    on doctor (department_id);

create index doctor_hospital_index
    on doctor (hospital_id);

create index doctor_mobile_index
    on doctor (mobile);

INSERT INTO medical.doctor (id, name, password, salt, gender, mobile, email, hospital_id, department_id, title, introduction, avatar_url, status, create_id, create_time, update_id, update_time) VALUES (3001, '张医生', '123456', '970eb1c2f043455e9f70c35578f7e87c', 1, '13900001111', 'zhang@medical.com', 1001, 2001, '主任医师', '从事内科临床工作20年，经验丰富', null, 1, 1, '2025-07-09 09:47:36', null, '2025-07-09 09:47:36');
INSERT INTO medical.doctor (id, name, password, salt, gender, mobile, email, hospital_id, department_id, title, introduction, avatar_url, status, create_id, create_time, update_id, update_time) VALUES (3002, '李医生', 'fa1a2b8314d2a19cb1881eeebc4747adcc31d0b3aa7fba8f45643f1ec8570883', '970eb1c2f043455e9f70c35578f7e87c', 0, '13900002222', 'li@medical.com', 1001, 2002, '副主任医师', '外科专家，擅长微创手术', null, 1, 1, '2025-07-09 09:47:36', null, '2025-07-09 09:47:36');
INSERT INTO medical.doctor (id, name, password, salt, gender, mobile, email, hospital_id, department_id, title, introduction, avatar_url, status, create_id, create_time, update_id, update_time) VALUES (3003, '王医生', 'fa1a2b8314d2a19cb1881eeebc4747adcc31d0b3aa7fba8f45643f1ec8570883', '970eb1c2f043455e9f70c35578f7e87c', 1, '13900003333', 'wang@medical.com', 1002, 2005, '主任医师', '心脑血管疾病诊疗专家', null, 1, 1, '2025-07-09 09:47:36', null, '2025-07-09 09:47:36');
INSERT INTO medical.doctor (id, name, password, salt, gender, mobile, email, hospital_id, department_id, title, introduction, avatar_url, status, create_id, create_time, update_id, update_time) VALUES (696281184763909, 'test1', 'test1', '7CQVJuUE', 1, '1800', null, 696263284899845, 2007, 'test1', 'test1', null, 1, 1, '2025-07-10 13:55:26', 1, '2025-07-10 13:55:25');
INSERT INTO medical.doctor (id, name, password, salt, gender, mobile, email, hospital_id, department_id, title, introduction, avatar_url, status, create_id, create_time, update_id, update_time) VALUES (696613004308485, 'test', '123456', '123', 1, '18004828812', null, 1, null, null, null, null, 1, 1, '2025-07-11 12:25:36', null, '2025-07-11 12:25:38');
INSERT INTO medical.doctor (id, name, password, salt, gender, mobile, email, hospital_id, department_id, title, introduction, avatar_url, status, create_id, create_time, update_id, update_time) VALUES (696616830558213, 'wby', '0677712ff5a007fa3f65e640b2a9c32ce670e90b4c1d63d3ac4d94bb5f2878bb', '71fd4770', 1, '13900002223', null, null, null, null, null, null, 1, 1, '2025-07-11 12:41:10', 1, '2025-07-11 12:41:12');