DROP DATABASE IF EXISTS `quiz`;
CREATE DATABASE `quiz`;
USE `quiz`;

CREATE TABLE `quiz`.`category` (
  `category_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(45) NOT NULL,
  `parent_id` INT UNSIGNED,
  `course_count` INT NULL,
  `id_number` INT UNSIGNED NOT NULL,
  `category_info` VARCHAR(255) NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE `quiz`.`question` (
  `question_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `question_content` VARCHAR(255) ,
  `question_name` VARCHAR(255) ,
  `category_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`question_id`),
  INDEX `category_id_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `quiz`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE `quiz`.`choice` (
  `choice_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `choice_content` VARCHAR(255) NOT NULL,
  `choice_is_correct` TINYINT NOT NULL,
  `choice_grade` INT NULL,
  `question_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`choice_id`),
  INDEX `question_id_idx` (`question_id` ASC) VISIBLE,
  CONSTRAINT `question_id`
    FOREIGN KEY (`question_id`)
    REFERENCES `quiz`.`question` (`question_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

INSERT INTO `category` (`category_id`, `category_name`, `parent_id`, `course_count`, `id_number`) VALUES
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

INSERT INTO `question` (`question_id`, `question_content`, `category_id`) VALUES
(1, 'Ay yo', 4),
(2, 'do dat', 4);