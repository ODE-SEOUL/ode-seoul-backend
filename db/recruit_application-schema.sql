CREATE TABLE `recruit_application` (
  `id` int NOT NULL AUTO_INCREMENT,
  `recruit_id` int NOT NULL,
  `member_user_id` int NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `saved` tinyint GENERATED ALWAYS AS (if((`deleted_at` is null),1,NULL)) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `recruit_application_unique` (`recruit_id`,`member_user_id`,`saved`),
  KEY `recruit_application_member_user_idx` (`member_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
