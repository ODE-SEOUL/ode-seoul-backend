CREATE TABLE `location` (
  `code` varchar(45) NOT NULL COMMENT '행정동코드',
  `address1` varchar(45) NOT NULL COMMENT '시도명',
  `address2` varchar(45) NOT NULL COMMENT '시군구명',
  `address3` varchar(45) NOT NULL COMMENT '읍면동명',
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `seoul_gugun` tinyint(1) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `location_seoul_gugun_idx` (`seoul_gugun`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
