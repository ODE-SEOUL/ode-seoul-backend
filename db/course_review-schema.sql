CREATE TABLE `course_review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int NOT NULL,
  `user_id` int NOT NULL,
  `score` int NOT NULL,
  `content` text NOT NULL,
  `image` varchar(500) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `saved` tinyint GENERATED ALWAYS AS (if((`deleted_at` is null),1,NULL)) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `course_review_unique` (`course_id`,`user_id`,`saved`),
  KEY `course_review_user_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
