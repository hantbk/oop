DROP DATABASE IF EXISTS `quiz`;
CREATE DATABASE `quiz`;
USE `quiz`;

--
-- Table structure for table `category`
--
CREATE TABLE `category` (
  `category_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(45) NOT NULL,
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
  `question_name` VARCHAR(1000),
  `question_text` VARCHAR(1000) NOT NULL,
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
  `choice_is_correct` TINYINT NOT NULL,
  `choice_grade` INT NULL,
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