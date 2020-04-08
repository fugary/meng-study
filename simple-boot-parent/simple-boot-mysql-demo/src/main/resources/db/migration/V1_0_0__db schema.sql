DROP TABLE IF EXISTS t_user;
CREATE TABLE `t_user` (
  `id` bigint not null,
  `user_name` varchar(100) NOT NULL,
  `nick_name` varchar(100) DEFAULT NULL,
  `birth` timestamp DEFAULT NULL,
  `user_password` varchar(1024) DEFAULT NULL,
  `user_status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;