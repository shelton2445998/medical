create table setmeal_detail
(
    id                 bigint                                   not null comment '主键'
        primary key,
    setmeal_id         bigint                                   not null comment '套餐ID',
    name               varchar(100)   default '默认套餐名称'          not null comment '套餐名称',
    price              decimal(10, 2) default 0.00              null comment '套餐原价',
    discount_price     decimal(10, 2) default 0.00              null comment '优惠价格',
    description        text                                     null comment '套餐详细介绍',
    check_items        text                                     null comment '检查项目列表',
    suitable_crowd     text                                     null comment '适用人群描述',
    appointment_notice text                                     null comment '预约须知',
    user_reviews       text                                     null comment '用户评价',
    hospital_id        bigint         default 1                 not null comment '所属医院ID',
    department_id      bigint                                   null comment '所属科室ID',
    package_type       tinyint        default 1                 not null comment '套餐类型：1-基础，2-高级，3-专项',
    status             tinyint        default 1                 not null comment '状态：0-下架，1-上架',
    sort_order         int            default 0                 null comment '排序权重',
    item_id            bigint                                   not null comment '检查项ID',
    create_id          bigint                                   null comment '创建人ID',
    create_time        datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    update_id          bigint                                   null comment '修改人ID',
    update_time        datetime       default CURRENT_TIMESTAMP null comment '修改时间'
)
    comment '体检套餐明细';

create index idx_department_id
    on setmeal_detail (department_id);

create index idx_hospital_id
    on setmeal_detail (hospital_id);

create index idx_item_id
    on setmeal_detail (item_id);

create index idx_package_type
    on setmeal_detail (package_type);

create index idx_setmeal_id
    on setmeal_detail (setmeal_id);

create index idx_status
    on setmeal_detail (status);

create index setmeal_detail_item_index
    on setmeal_detail (item_id);

create index setmeal_detail_setmeal_index
    on setmeal_detail (setmeal_id);

INSERT INTO medical.setmeal_detail (id, setmeal_id, name, price, discount_price, description, check_items, suitable_crowd, appointment_notice, user_reviews, hospital_id, department_id, package_type, status, sort_order, item_id, create_id, create_time, update_id, update_time) VALUES (4001, 4001, '基础体检套餐', 299.00, 269.00, '本套餐是针对25-45岁人群设计的基础体检套餐，包含血常规、尿常规、肝功能、肾功能、血脂、血糖等基础检查项目，能够全面了解身体健康状况，及早发现潜在健康风险。', '[{"category":"一般检查","name":"身高体重","desc":"测量身高、体重，计算体重指数(BMI)"},{"category":"一般检查","name":"血压","desc":"测量收缩压和舒张压"},{"category":"一般检查","name":"视力","desc":"检查远视力和近视力"},{"category":"血液检查","name":"血常规","desc":"检查红细胞、白细胞、血小板等指标"},{"category":"血液检查","name":"肝功能","desc":"检查ALT、AST、总蛋白等肝功能指标"},{"category":"血液检查","name":"肾功能","desc":"检查尿素氮、肌酐等肾功能指标"},{"category":"血液检查","name":"血脂","desc":"检查总胆固醇、甘油三酯等血脂指标"},{"category":"血液检查","name":"血糖","desc":"检查空腹血糖"},{"category":"尿液检查","name":"尿常规","desc":"检查尿液颜色、比重、pH值等指标"},{"category":"影像检查","name":"胸部X光","desc":"检查肺部、心脏等胸腔器官"},{"category":"影像检查","name":"B超","desc":"肝、胆、脾、胰、肾B超检查"}]', '适合25-45岁人群，特别是长期伏案工作、生活压力大、作息不规律的上班族，建议每年体检一次。', '["体检前一天请清淡饮食，避免辛辣、油腻食物","体检当天请空腹，禁食8-12小时","体检前一天晚上请保证充足睡眠","体检当天请携带身份证等有效证件","体检报告一般在3-5个工作日出具，可在APP查看"]', '[{"id":1,"name":"张先生","avatar":"/static/images/avatar1.jpg","time":"2023-07-08","rating":5,"content":"套餐内容很全面，医态很态度很好，检查很仔细，报告出来也很快，值得推荐！"},{"id":2,"name":"李女士","avatar":"/static/images/avatar2.jpg","time":"2023-07-06","rating":4,"content":"体检很顺利，医护人员很专业，就是人比较多，需要排队等待。"}]', 1, 1, 1, 1, 0, 6001, 1, '2025-07-11 14:51:09', null, '2025-07-11 14:51:09');
INSERT INTO medical.setmeal_detail (id, setmeal_id, name, price, discount_price, description, check_items, suitable_crowd, appointment_notice, user_reviews, hospital_id, department_id, package_type, status, sort_order, item_id, create_id, create_time, update_id, update_time) VALUES (4002, 4002, '高级体检套餐', 699.00, 599.00, '适合45岁以上人群，包含心脑血管、肿瘤筛查等全面检查，帮助早期发现慢性病和肿瘤风险。', '[{"category":"一般检查","name":"身高体重","desc":"测量身高、体重，计算BMI"},{"category":"血液检查","name":"血常规","desc":"检查红细胞、白细胞、血小板等"},{"category":"血液检查","name":"肝功能","desc":"ALT、AST等肝功能指标"},{"category":"血液检查","name":"肾功能","desc":"尿素氮、肌酐等肾功能指标"},{"category":"血液检查","name":"血脂","desc":"总胆固醇、甘油三酯等"},{"category":"血液检查","name":"血糖","desc":"空腹血糖"},{"category":"心电检查","name":"心电图","desc":"心脏电活动检测"},{"category":"心脏检查","name":"心脏彩超","desc":"心脏结构及功能"},{"category":"心脑血管","name":"脑血流图","desc":"脑部血流情况检测"},{"category":"专项检查","name":"肿瘤标志物","desc":"多种肿瘤早筛"},{"category":"影像检查","name":"胸部X光","desc":"肺部、心脏等胸腔器官"},{"category":"影像检查","name":"腹部B超","desc":"腹部脏器筛查"}]', '适合45岁以上关注慢性病、肿瘤风险的人群，建议每年体检一次。', '["体检前请清淡饮食，避免油腻、辛辣食物","体检当天请空腹，禁食8-12小时","如有慢性病请携带相关病历资料","体检报告一般在3-5个工作日出具，可在APP查看"]', '[{"id":1,"name":"孙女士","avatar":"/static/images/avatar2.jpg","time":"2023-07-10","rating":5,"content":"检查项目很全面，医生很专业，推荐给中老年朋友。"},{"id":2,"name":"李先生","avatar":"/static/images/avatar1.jpg","time":"2023-07-09","rating":4,"content":"体检体验不错，报告解读很细致。"}]', 1, 1, 2, 1, 0, 6002, 1, '2025-07-11 14:51:09', null, '2025-07-11 14:51:09');
INSERT INTO medical.setmeal_detail (id, setmeal_id, name, price, discount_price, description, check_items, suitable_crowd, appointment_notice, user_reviews, hospital_id, department_id, package_type, status, sort_order, item_id, create_id, create_time, update_id, update_time) VALUES (4003, 4003, '心脑血管专项套餐', 499.00, 459.00, '针对心脑血管疾病的专项检查，适合高风险人群，包含心电图、心脏彩超、脑血流图等专项检查。', '[{"category":"一般检查","name":"身高体重","desc":"测量身高、体重，计算BMI"},{"category":"血液检查","name":"血常规","desc":"检查红细胞、白细胞、血小板等"},{"category":"血液检查","name":"血脂","desc":"总胆固醇、甘油三酯等"},{"category":"血液检查","name":"血糖","desc":"空腹血糖"},{"category":"心电检查","name":"心电图","desc":"心脏电活动检测"},{"category":"心脏检查","name":"心脏彩超","desc":"心脏结构及功能"},{"category":"心脑血管","name":"脑血流图","desc":"脑部血流情况检测"},{"category":"影像检查","name":"胸部X光","desc":"肺部、心脏等胸腔器官"}]', '适合有心脑血管疾病家族史、高血压、糖尿病等慢性病患者，以及45岁以上关注心脑血管健康的人群。', '["体检前避免剧烈运动","体检当天请空腹","如有慢性病请携带相关病历资料","如有心脏起搏器请提前告知"]', '[{"id":1,"name":"王大爷","avatar":"/static/images/avatar1.jpg","time":"2023-06-15","rating":5,"content":"心脑血管专项检查很专业，医生讲解很详细。"},{"id":2,"name":"刘奶奶","avatar":"/static/images/avatar2.jpg","time":"2023-06-10","rating":5,"content":"检查很全面，对心脑血管健康很有帮助。"}]', 1, 2, 3, 1, 0, 6003, 1, '2025-07-11 14:51:09', null, '2025-07-11 14:51:09');