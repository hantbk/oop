DROP DATABASE IF EXISTS `quiz`;
CREATE DATABASE `quiz`;
USE `quiz`;

CREATE TABLE `category` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `parent_id` INT,
    `course_count` INT NOT NULL,
    `id_number` INT NOT NULL,
    `category_info` VARCHAR(255),
     CONSTRAINT `fk` FOREIGN KEY (`parent_id`)
        REFERENCES `category` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    PRIMARY KEY (`id`),
    UNIQUE (`id_number`),
    UNIQUE (`name`)
);

INSERT INTO `category` (`id`, `name`, `parent_id`, `course_count`, `id_number`) VALUES
(1, 'Course IT', NULL, 0, 1),
(2, 'Top for IT', 1, 0, 2),
(3, 'C câu hỏi dễ', 2, 256, 3),
(4, 'C from linh tinh 1', 2, 5, 4),
(5, 'C khó', 2, 56, 5),
(6, 'Default for IT', 2, 0, 6),
(7, 'Dễ', 6, 0, 7),
(8, '20221', 7, 0, 8),
(9, 'Trước đến 20211', 7, 0, 9),
(10, 'Khó', 6, 0, 10),
(11, 'Khó 20221', 10, 0, 11),
(12, 'Khó cho đến 20211', 10, 0, 12),
(13, 'Tự luận 20221', 6, 0, 13),
(14, 'Tự luận KTLT', 6, 0, 14),
(15, 'From me - de thi GK2 L7 Sinh', 2, 21, 15),
(16, 'Sinh học kỳ 2 L7', 2, 67, 16),
(17, 'Sử Địa GK2 L7', 2, 130, 17),
(18, 'Tin học GK2 L7', 2, 94, 18),
(19, 'Vật lý GK2 L7', 2, 121, 19);


-- CREATE TABLE `questions` (
--   `id` INT NOT NULL AUTO_INCREMENT,
--   `question` VARCHAR(255) NOT NULL,
--   `A` VARCHAR(255) NOT NULL,
--   `B` VARCHAR(255) NOT NULL,
--   `C` VARCHAR(255) NOT NULL,
--   `D` VARCHAR(255) NOT NULL,
--   `answer` VARCHAR(255) NOT NULL,
--   `created_at` DATETIME NOT NULL,
--   `updated_at` DATETIME NOT NULL,
--   PRIMARY KEY (`id`)
-- );