CREATE TABLE `notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author` varchar(45) NOT NULL,
  `title` varchar(500) NOT NULL,
  `content` text NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
