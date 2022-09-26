-- 用户表
create table user
(
    id            bigint auto_increment comment '用户唯一标识'
        primary key,
    username      varchar(256)                       null comment '用户昵称',
    user_account  varchar(256)                       not null comment '用户登录账号',
    avatar_url    varchar(1024)                      null comment '用户头像',
    gender        tinyint                            null comment '用户性别，"0" 男性，"1" 女性',
    user_password varchar(512)                       not null comment '用户密码',
    phone         varchar(128)                       null comment '用户电话号码',
    email         varchar(512)                       null comment '用户邮箱',
    user_status   tinyint  default 0                 null comment '用户状态，"0" 正常',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    is_delete     tinyint  default 0                 null comment '是否删除，"0" 未删除',
    user_role     tinyint  default 0                 not null comment '用户角色 0 —— 默认用户 1 ——管理员',
    planet_code   int                                not null comment '星球编号',
    tags          varchar(1024)                      null comment '标签JSON列表',
    constraint user_id_uindex
        unique (id),
    constraint user_planet_code_uindex
        unique (planet_code),
    constraint user_user_account_uindex
        unique (user_account)
)
    comment '用户表';

-- 标签表
create table tag
(
    id          bigint auto_increment comment '标签ID'
        primary key,
    tag_name    varchar(256)                       null comment '标签名',
    user_id     bigint                             null comment '用户ID',
    parent_id   bigint                             null comment '父标签ID',
    is_parent   tinyint                            null comment '是否父标签，"0" 不是，"1" 是',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除，"0" 未删除，"1" 已删除'
)
    comment '标签表';

create index idx_userId
    on tag (user_id);
create unique index unidx_tagName
    on tag (tag_name);

-- 队伍表
create table team
(
    id           bigint auto_increment comment 'id' primary key,
    name   varchar(256)                   not null comment '队伍名称',
    description varchar(1024)                      null comment '描述',
    max_num    int      default 1                 not null comment '最大人数',
    expire_time    datetime  null comment '过期时间',
    user_id            bigint comment '创建人用户id',
    status    int      default 0                 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password varchar(512)                       null comment '密码',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete     tinyint  default 0                 not null comment '是否删除'
)
    comment '队伍';

-- 用户队伍关系表
create table user_team
(
    id           bigint auto_increment comment 'id' primary key,
    user_id            bigint comment '用户id',
    team_id            bigint comment '队伍id',
    join_time datetime  null comment '加入时间',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete     tinyint  default 0                 not null comment '是否删除'
)
    comment '用户队伍关系';
