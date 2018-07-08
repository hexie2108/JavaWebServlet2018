-- MySQL Script generated by MySQL Workbench
-- dom 08 lug 2018 15:40:06 CEST
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`CategoryProduct`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`CategoryProduct` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `img` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`CategoryList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`CategoryList` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `img` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`User` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `img` VARCHAR(45) NOT NULL,
  `isAdmin` TINYINT NOT NULL,
  `verifyEmailLink` CHAR(32) NULL,
  `resendEmailLink` CHAR(32) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `verify_mail_link_UNIQUE` (`verifyEmailLink` ASC),
  UNIQUE INDEX `resend_mail_link_UNIQUE` (`resendEmailLink` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`List`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`List` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `ownerId` INT NOT NULL,
  `categoryList` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_List_CategoryList1_idx` (`categoryList` ASC),
  INDEX `fk_List_User1_idx` (`ownerId` ASC),
  CONSTRAINT `fk_List_CategoryList1`
    FOREIGN KEY (`categoryList`)
    REFERENCES `mydb`.`CategoryList` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_List_User1`
    FOREIGN KEY (`ownerId`)
    REFERENCES `mydb`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Product` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `img` VARCHAR(45) NOT NULL,
  `logo` VARCHAR(45) NOT NULL,
  `categoryProductId` INT NOT NULL,
  `privateListId` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Product_CategoryProduct1_idx` (`categoryProductId` ASC),
  INDEX `fk_Product_List1_idx` (`privateListId` ASC),
  CONSTRAINT `fk_Product_CategoryProduct1`
    FOREIGN KEY (`categoryProductId`)
    REFERENCES `mydb`.`CategoryProduct` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Product_List1`
    FOREIGN KEY (`privateListId`)
    REFERENCES `mydb`.`List` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Shop`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Shop` (
  `id` INT NOT NULL,
  `lat` DOUBLE NOT NULL,
  `lng` DOUBLE NOT NULL,
  `category` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Shop_CategoryList1_idx` (`category` ASC),
  CONSTRAINT `fk_Shop_CategoryList1`
    FOREIGN KEY (`category`)
    REFERENCES `mydb`.`CategoryList` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ProductInList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ProductInList` (
  `productId` INT NOT NULL,
  `listId` INT NOT NULL,
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ProductInList_List1_idx` (`listId` ASC),
  CONSTRAINT `fk_ProductInList_Product`
    FOREIGN KEY (`productId`)
    REFERENCES `mydb`.`Product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProductInList_List1`
    FOREIGN KEY (`listId`)
    REFERENCES `mydb`.`List` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Comment` (
  `userId` INT NOT NULL,
  `listId` INT NOT NULL,
  `text` VARCHAR(256) NOT NULL,
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Comment_List1_idx` (`listId` ASC),
  CONSTRAINT `fk_Comment_User1`
    FOREIGN KEY (`userId`)
    REFERENCES `mydb`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comment_List1`
    FOREIGN KEY (`listId`)
    REFERENCES `mydb`.`List` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Permission` (
  `id` INT NOT NULL,
  `addObject` TINYINT NOT NULL,
  `deleteObject` TINYINT NOT NULL,
  `modifyList` TINYINT NOT NULL,
  `deleteList` TINYINT NOT NULL,
  `listId` INT NOT NULL,
  `userId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Permissi_User1_idx` (`userId` ASC),
  CONSTRAINT `fk_Permissi_List1`
    FOREIGN KEY (`listId`)
    REFERENCES `mydb`.`List` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Permissi_User1`
    FOREIGN KEY (`userId`)
    REFERENCES `mydb`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Notification` (
  `id` INT NOT NULL,
  `date` TIMESTAMP NOT NULL,
  `text` VARCHAR(45) NOT NULL,
  `status` TINYINT NOT NULL,
  `userId` INT NOT NULL,
  INDEX `fk_Notifica_User1_idx` (`userId` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Notifica_User1`
    FOREIGN KEY (`userId`)
    REFERENCES `mydb`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Log` (
  `productId` INT NOT NULL,
  `userId` INT NOT NULL,
  `last1` DATETIME NOT NULL,
  `last2` DATETIME NULL,
  `last3` DATETIME NULL,
  `last4` DATETIME NULL,
  `id` INT NOT NULL,
  INDEX `fk_Log_Product1_idx` (`productId` ASC),
  INDEX `fk_Log_User1_idx` (`userId` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Log_Product1`
    FOREIGN KEY (`productId`)
    REFERENCES `mydb`.`Product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Log_User1`
    FOREIGN KEY (`userId`)
    REFERENCES `mydb`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
