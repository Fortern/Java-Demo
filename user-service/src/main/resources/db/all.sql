-- 创建region表
CREATE TABLE `region`
(
    `id`     bigint      NOT NULL COMMENT '区域ID',
    `level`  tinyint     NOT NULL COMMENT '区域等级，越小表示范围越大',
    `name`   varchar(50) NOT NULL COMMENT '区域名称',
    `sort`   int         NOT NULL COMMENT '排序',
    `code`   varchar(128) DEFAULT NULL COMMENT '第三方区域代码',
    `parent` bigint      NOT NULL COMMENT '父区域',
    PRIMARY KEY (`id`),
    KEY `region_parent_sort_index` (`parent`, `sort`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='区域'
