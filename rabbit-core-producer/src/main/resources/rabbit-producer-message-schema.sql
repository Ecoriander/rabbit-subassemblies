-- 表 broker_message.broker_message 结构
CREATE TABLE if not exists `broker_message`
(
    `message_id`  varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键',
    `message`     varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '消息实际内容',
    `try_count`   int                                                            DEFAULT '0' COMMENT '最大尝试次数',
    `status`      varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci   DEFAULT '' COMMENT '消息状态:0待确认；1投递成功；2投递失败',
    `next_retry`  datetime                                                      NOT NULL COMMENT '下次一次尝试时间',
    `create_time` datetime                                                      NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    PRIMARY KEY (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='投递消息记录';