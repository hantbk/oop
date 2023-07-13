DROP DATABASE IF EXISTS `quiz`;
CREATE DATABASE `quiz`;
USE `quiz`;

--
-- Table structure for table `category`
--
CREATE TABLE `category` (
  `category_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(100) NOT NULL,
  `parent_id` INT UNSIGNED,
  `question_count` INT DEFAULT 0 NOT NULL,
  `category_info` VARCHAR(255) NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

--
-- Table structure for table `question`
--
CREATE TABLE `question` (
  `question_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `question_name` VARCHAR(100),
  `question_text` VARCHAR(1000) NOT NULL,
  `question_image` VARCHAR(255) DEFAULT NULL,
  `mark` INT UNSIGNED NOT NULL DEFAULT 1,
  `category_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`question_id`),
  INDEX `category_id_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`category_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

--
-- Triggers for question_count
--
DELIMITER ;;
CREATE TRIGGER trg_question_count_insert
AFTER INSERT ON question
FOR EACH ROW
BEGIN
    UPDATE category
    SET question_count = (SELECT COUNT(*) FROM question WHERE category_id = NEW.category_id)
    WHERE category_id = NEW.category_id;
END;;

CREATE TRIGGER trg_question_count_delete
AFTER DELETE ON question
FOR EACH ROW
BEGIN
    UPDATE category
    SET question_count = (SELECT COUNT(*) FROM question WHERE category_id = OLD.category_id)
    WHERE category_id = OLD.category_id;
END;;
DELIMITER ;

--
-- Table structure for table `choice`
--
CREATE TABLE `choice` (
  `choice_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `choice_content` VARCHAR(1000) NOT NULL,
  `choice_grade` DOUBLE,
  `question_id` INT UNSIGNED NOT NULL,
  `image_data` LONGBLOB,

  PRIMARY KEY (`choice_id`),
  INDEX `question_id_idx` (`question_id` ASC) VISIBLE,
  CONSTRAINT `question_id`
    FOREIGN KEY (`question_id`)
    REFERENCES `question` (`question_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

--
-- Table structure for table `quiz`
--
CREATE TABLE `quiz` (
  `quiz_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `quiz_name` VARCHAR(255) NOT NULL,
  `quiz_description` VARCHAR(255) NULL,
  `quiz_open_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `quiz_close_date` DATETIME,
  `quiz_time_limit` INT UNSIGNED,
  `quiz_time_format` varchar(10) NOT NULL,
  PRIMARY KEY (`quiz_id`),
  UNIQUE INDEX `quiz_id_UNIQUE` (`quiz_id` ASC) VISIBLE,
  UNIQUE INDEX `quiz_name_UNIQUE` (`quiz_name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `quiz_question`
--
CREATE TABLE `quiz_question` (
   `quiz_id` INT UNSIGNED NOT NULL,
   `question_id` INT UNSIGNED NOT NULL,
   `question_order` INT UNSIGNED NOT NULL,
   PRIMARY KEY (`quiz_id`, `question_id`),
   INDEX `question_id_idx` (`question_id` ASC) VISIBLE,
   CONSTRAINT `quiz_id`
     FOREIGN KEY (`quiz_id`)
     REFERENCES `quiz` (`quiz_id`)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
   CONSTRAINT `fk_question_id`
     FOREIGN KEY (`question_id`)
     REFERENCES `question` (`question_id`)
     ON DELETE CASCADE
     ON UPDATE CASCADE)
 ENGINE = InnoDB
 DEFAULT CHARACTER SET = utf8mb4;

--
-- Insert data
--
INSERT INTO `category` (`category_id`, `category_name`, `parent_id`) VALUES
(1, 'Course IT', NULL),
(2, 'Top for IT', 1),
(3, 'C câu hỏi dễ', 2),
(4, 'C from linh tinh 1', 2),
(5, 'C khó', 2),
(6, 'Default for IT', 2),
(7, 'Dễ', 6),
(8, '20221', 7),
(9, 'Trước đến 20211', 7),
(10, 'Khó', 6),
(11, 'Khó 20221', 10),
(12, 'Khó cho đến 20211', 10),
(13, 'Tự luận 20221', 6),
(14, 'Tự luận KTLT', 6),
(15, 'From me - de thi GK2 L7 Sinh', 2),
(16, 'Sinh học kỳ 2 L7', 2),
(17, 'Sử Địa GK2 L7', 2),
(18, 'Tin học GK2 L7', 2),
(19, 'Vật lý GK2 L7', 2);

INSERT INTO `question` (`question_id`, `question_name`,`question_text`, `category_id`) VALUES
(1, 'Ay yo', 'abcd', 4),
(2, 'do dat', 'efgh', 4);

INSERT INTO `choice` (`choice_id`, `choice_content`, `choice_grade`, `question_id`, `image_data`) VALUES
(1, 'a', 1, 1, NULL),
(2, 'b', 0, 1, NULL),
(3, 'c', 0, 1, NULL),
(4, 'd', 0, 1, NULL),
(5, 'e', 1, 2, NULL),
(6, 'f', 0, 2, NULL),
(7, 'g', 0, 2, NULL),
(8, 'h', 0, 2, NULL);

INSERT INTO `quiz`.`quiz` (`quiz_id`, `quiz_name`, `quiz_description`) VALUES ('1', 'OOP', 'Kiểm tra giữa kì');
INSERT INTO `quiz`.`quiz` (`quiz_id`, `quiz_name`, `quiz_description`) VALUES ('2', 'KTLT', 'Thi cuối kì ');
INSERT INTO `quiz`.`quiz` (`quiz_id`, `quiz_name`, `quiz_description`) VALUES ('3', 'Bảo Hiểm', 'Test BTL');


-- INSERT INTO `quiz`.`quiz_question` (`quiz_id`, `question_id`, `question_order`) VALUES
--  ('3', '3', '1'),
--  ('3', '4', '2'),
--  ('3', '5', '3'),
--  ('3', '6', '4'),
--  ('3', '7', '5'),
--  ('3', '8', '6'),
--  ('3', '9', '7'),
--  ('3', '10', '8'),
--  ('3', '11', '9'),
--  ('3', '12', '10'),
--  ('3', '13', '11'),
--  ('3', '14', '12'),
--  ('3', '15', '13'),
--  ('3', '16', '14'),
--  ('3', '17', '15'),
--  ('3', '18', '16'),
--  ('3', '19', '17'),
--  ('3', '20', '18'),
--  ('3', '21', '19'),
--  ('3', '22', '20'),
--  ('3', '23', '21'),
--  ('3', '24', '22'),
--  ('3', '25', '23'),
--  ('3', '26', '24'),
--  ('3', '27', '25'),
--  ('3', '28', '26'),
--  ('3', '29', '27'),
--  ('3', '30', '28'),
--  ('3', '31', '29'),
--  ('3', '32', '30'),
-- ('3', '33', '31'),
-- ('3', '34', '32'),
-- ('3', '35', '33');
