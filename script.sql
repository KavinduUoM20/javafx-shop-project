-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`product` (
  `code` INT NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`code`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`customer` (
  `email` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `contact` VARCHAR(45) NULL,
  `salary` DOUBLE NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(750) NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`order` (
  `code` INT NOT NULL,
  `issued_date` DATE NULL,
  `total_cost` DOUBLE NULL,
  `discount_amount` DOUBLE NULL,
  `customer_email` VARCHAR(100) NOT NULL,
  `user_email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`code`, `customer_email`, `user_email`),
  INDEX `fk_order_customer1_idx` (`customer_email` ASC) VISIBLE,
  INDEX `fk_order_user1_idx` (`user_email` ASC) VISIBLE,
  CONSTRAINT `fk_order_customer1`
    FOREIGN KEY (`customer_email`)
    REFERENCES `mydb`.`customer` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_user1`
    FOREIGN KEY (`user_email`)
    REFERENCES `mydb`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`orderline`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`orderline` (
  `order_orderid` INT NOT NULL,
  `product_productid` INT NOT NULL,
  `quantity` INT NULL,
  `amount` DOUBLE NULL,
  `discount` DOUBLE NULL,
  PRIMARY KEY (`order_orderid`, `product_productid`),
  INDEX `fk_order_has_product_product1_idx` (`product_productid` ASC) VISIBLE,
  INDEX `fk_order_has_product_order_idx` (`order_orderid` ASC) VISIBLE,
  CONSTRAINT `fk_order_has_product_order`
    FOREIGN KEY (`order_orderid`)
    REFERENCES `mydb`.`order` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_has_product_product1`
    FOREIGN KEY (`product_productid`)
    REFERENCES `mydb`.`product` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`loyalty_card`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`loyalty_card` (
  `code` INT NOT NULL,
  `type` ENUM('GOLD', 'PLATINUM', 'SILVER') NULL,
  `barcode` LONGBLOB NULL,
  `customer_customeremail` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`code`, `customer_customeremail`),
  INDEX `fk_loyalty_card_customer1_idx` (`customer_customeremail` ASC) VISIBLE,
  CONSTRAINT `fk_loyalty_card_customer1`
    FOREIGN KEY (`customer_customeremail`)
    REFERENCES `mydb`.`customer` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`product_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`product_details` (
  `code` MEDIUMTEXT NOT NULL,
  `barcode` LONGBLOB NULL,
  `qty_onhand` INT NULL,
  `selling_price` DOUBLE NULL,
  `buying_price` DOUBLE NULL,
  `discount_availability` TINYINT NULL,
  `show_price` DOUBLE NULL,
  `product_code` INT NOT NULL,
  `product_category_categoryid` INT NOT NULL,
  PRIMARY KEY (`code`, `product_code`, `product_category_categoryid`),
  INDEX `fk_product_details_product1_idx` (`product_code` ASC, `product_category_categoryid` ASC) VISIBLE,
  CONSTRAINT `fk_product_details_product1`
    FOREIGN KEY (`product_code`)
    REFERENCES `mydb`.`product` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
