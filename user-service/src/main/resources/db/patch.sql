-- 创建region表
CREATE TABLE `region` (
    `id` bigint NOT NULL COMMENT '区域ID',
    `name` varchar(50) NOT NULL COMMENT '区域名称',
    `code` varchar(100) NULL COMMENT '第三方区域ID',
    `sort` bigint NOT NULL COMMENT '排序',
    PRIMARY KEY (`id`),
    KEY `level_0` ((`id` >> 60)),
    KEY `level_1` ((`id` >> 48)),
    KEY `level_2` ((`id` >> 36)),
    KEY `level_3` ((`id` >> 24)),
    KEY `level_4` ((`id` >> 12)),
    KEY `level_5` ((`id` << 16)),
    KEY `level_6` ((`id` << 28)),
    KEY `level_7` ((`id` << 40)),
    KEY `level_8` ((`id` << 52))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
