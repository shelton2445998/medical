create table family_member
(
    id          bigint                             not null comment '主键'
        primary key,
    user_id     bigint                             not null comment '关联用户ID',
    name        varchar(32)                        not null comment '成员姓名',
    gender      tinyint(1)                         null comment '性别 0:女，1:男',
    id_card     varchar(18)                        null comment '身份证号',
    mobile      varchar(20)                        null comment '手机号',
    relation    varchar(20)                        null comment '与用户关系',
    create_id   bigint                             null comment '创建人ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_id   bigint                             null comment '修改人ID',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '家庭成员';

create index family_user_index
    on family_member (user_id);

INSERT INTO medical.family_member (id, user_id, name, gender, id_card, mobile, relation, create_id, create_time, update_id, update_time) VALUES (11001, 5001, '张小明', 1, '110101201005121234', '13900001234', '子', 5001, '2025-07-10 10:20:00', null, '2025-07-10 10:20:00');
INSERT INTO medical.family_member (id, user_id, name, gender, id_card, mobile, relation, create_id, create_time, update_id, update_time) VALUES (11002, 5002, '李大妈', 0, '110101196508231245', '13900005678', '母', 5002, '2025-07-10 11:30:00', null, '2025-07-10 11:30:00');
INSERT INTO medical.family_member (id, user_id, name, gender, id_card, mobile, relation, create_id, create_time, update_id, update_time) VALUES (6001, 5001, '张三', 1, '110101199003033456', '13800003333', '父亲', 5001, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.family_member (id, user_id, name, gender, id_card, mobile, relation, create_id, create_time, update_id, update_time) VALUES (6002, 5001, '李四', 0, '110101199004044567', '13800004444', '母亲', 5001, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');
INSERT INTO medical.family_member (id, user_id, name, gender, id_card, mobile, relation, create_id, create_time, update_id, update_time) VALUES (6003, 5002, '王五', 1, '110101199005055678', '13800005555', '丈夫', 5002, '2025-07-09 09:47:35', null, '2025-07-09 09:47:35');