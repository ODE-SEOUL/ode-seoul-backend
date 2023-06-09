CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nickname` varchar(45) NOT NULL,
  `profile_image` varchar(500) NOT NULL,
  `location_code` varchar(45) DEFAULT NULL,
  `login_id` varchar(45) NOT NULL,
  `login_pw` varchar(500) NOT NULL,
  `refresh_token` varchar(500) NOT NULL,
  `signup_status` enum('BEFORE_REGISTERED','REGISTERED') NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `saved` tinyint GENERATED ALWAYS AS (if((`deleted_at` is null),1,NULL)) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_login_id_unique` (`login_id`,`saved`),
  KEY `user_refresh_token_idx` (`refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
