SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `categories` ;

CREATE  TABLE IF NOT EXISTS `categories` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL ,
  `parent_id` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `FK26DEEF95A525FC` (`parent_id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `posts` ;

CREATE  TABLE IF NOT EXISTS `posts` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `description` LONGTEXT NULL DEFAULT NULL ,
  `publishedAt` DATE NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `baseevent_article`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `post_categories` ;

CREATE  TABLE IF NOT EXISTS `post_categories` (
  `post_id` BIGINT(20) NOT NULL ,
  `category_id` BIGINT(20) NOT NULL ,
  INDEX `FK3C4108C0EE621A59` (`post_id` ASC) ,
  INDEX `FK3C4108C0F9AAC991` (`category_id` ASC) ,
  CONSTRAINT `FK3C4108C0EE621A59`
    FOREIGN KEY (`post_id` )
    REFERENCES `posts` (`id` ),
  CONSTRAINT `FK3C4108C0F9AAC991`
    FOREIGN KEY (`category_id` )
    REFERENCES `categories` (`id` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
