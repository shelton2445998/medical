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

