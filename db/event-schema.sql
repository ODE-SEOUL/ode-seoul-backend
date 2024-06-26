CREATE TABLE `event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uuid` varchar(45) NOT NULL,
  `codename` varchar(45) NOT NULL COMMENT '분류',
  `guname` varchar(45) NOT NULL COMMENT '자치구',
  `title` varchar(500) NOT NULL COMMENT '공연/행사명',
  `place` varchar(500) NOT NULL COMMENT '장소',
  `use_target` varchar(500) NOT NULL COMMENT '이용대상',
  `use_fee` varchar(500) NOT NULL COMMENT '이용요금',
  `org_link` varchar(3000) NOT NULL COMMENT '홈페이지주소',
  `main_image` varchar(3000) NOT NULL COMMENT '대표이미지',
  `start_date` date NOT NULL COMMENT '시작일',
  `end_date` date NOT NULL COMMENT '종료일',
  `register_date` date NOT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_codename_idx` (`codename`),
  KEY `event_guname_idx` (`guname`),
  KEY `event_register_date_idx` (`register_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
