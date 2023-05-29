CREATE TABLE `user_picked_course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `course_id` int NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `saved` tinyint GENERATED ALWAYS AS (if((`deleted_at` is null),1,NULL)) VIRTUAL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_picked_course_unique` (`user_id`,`course_id`,`saved`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
