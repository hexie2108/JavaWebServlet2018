-- MySQL dump 10.13  Distrib 5.7.22, for Linux (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CategoryList`
--

USE `mydb`;

DROP TABLE IF EXISTS `CategoryList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CategoryList` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `img1` varchar(45) NOT NULL,
  `img2` varchar(45) DEFAULT NULL,
  `img3` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CategoryList`
--

LOCK TABLES `CategoryList` WRITE;
/*!40000 ALTER TABLE `CategoryList` DISABLE KEYS */;
INSERT INTO `CategoryList` (`id`, `name`, `description`, `img1`, `img2`, `img3`) VALUES (1,'categ1','desc1','img1',NULL,NULL),(2,'categ2','desc2','img2',NULL,NULL),(3,'categ3','desc3','img3',NULL,NULL);
/*!40000 ALTER TABLE `CategoryList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CategoryProduct`
--

DROP TABLE IF EXISTS `CategoryProduct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CategoryProduct` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `img` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CategoryProduct`
--

LOCK TABLES `CategoryProduct` WRITE;
/*!40000 ALTER TABLE `CategoryProduct` DISABLE KEYS */;
INSERT INTO `CategoryProduct` (`id`, `name`, `description`, `img`) VALUES (1,'catProd1','descCatProd1','imgCatProd1'),(2,'catProd2','descCatProd2','imgCatProd2'),(3,'catProd3','descCatProd3','imgCatProd3'),(4,'catProd4','descCatProd4','imgCatProd4'),(5,'catProd5','descCatProd5','imgCatProd5');
/*!40000 ALTER TABLE `CategoryProduct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comment`
--

DROP TABLE IF EXISTS `Comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(256) NOT NULL,
  `userId` int(11) NOT NULL,
  `listId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Comment_List1_idx` (`listId`),
  KEY `fk_Comment_User1` (`userId`),
  CONSTRAINT `fk_Comment_List1` FOREIGN KEY (`listId`) REFERENCES `List` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comment_User1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment`
--

LOCK TABLES `Comment` WRITE;
/*!40000 ALTER TABLE `Comment` DISABLE KEYS */;
INSERT INTO `Comment` (`id`, `text`, `userId`, `listId`) VALUES (1,'cmtText1',1,1),(2,'cmtText2',2,1),(3,'cmtText3',3,1),(4,'cmtText4',4,2),(5,'cmtText5',5,2),(6,'cmtText6',2,3),(7,'cmtText7',3,3),(8,'cmtText8',4,4),(9,'cmtText9',4,5),(10,'cmtText10',5,6);
/*!40000 ALTER TABLE `Comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `List`
--

DROP TABLE IF EXISTS `List`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `List` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `img` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `ownerId` int(11) NOT NULL,
  `categoryList` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_List_CategoryList1_idx` (`categoryList`),
  KEY `fk_List_User1_idx` (`ownerId`),
  CONSTRAINT `fk_List_CategoryList1` FOREIGN KEY (`categoryList`) REFERENCES `CategoryList` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_List_User1` FOREIGN KEY (`ownerId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `List`
--

LOCK TABLES `List` WRITE;
/*!40000 ALTER TABLE `List` DISABLE KEYS */;
INSERT INTO `List` (`id`, `name`, `img`, `description`, `ownerId`, `categoryList`) VALUES (1,'list1',NULL,NULL,1,1),(2,'list2',NULL,NULL,2,1),(3,'list3',NULL,NULL,3,2),(4,'list4',NULL,NULL,4,2),(5,'list5',NULL,NULL,5,3),(6,'list6',NULL,NULL,1,3),(7,'list7',NULL,NULL,2,1),(8,'list8',NULL,NULL,3,1);
/*!40000 ALTER TABLE `List` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Log`
--

DROP TABLE IF EXISTS `Log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `last1` datetime NOT NULL,
  `last2` datetime DEFAULT NULL,
  `last3` datetime DEFAULT NULL,
  `last4` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Log_Product1_idx` (`productId`),
  KEY `fk_Log_User1_idx` (`userId`),
  CONSTRAINT `fk_Log_Product1` FOREIGN KEY (`productId`) REFERENCES `Product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Log_User1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Log`
--

LOCK TABLES `Log` WRITE;
/*!40000 ALTER TABLE `Log` DISABLE KEYS */;
INSERT INTO `Log` (`id`, `productId`, `userId`, `last1`, `last2`, `last3`, `last4`) VALUES (1,1,2018,'2018-07-03 15:57:48','2018-07-03 15:57:48','2018-07-03 15:57:48','0000-00-00 00:00:00'),(2,1,2018,'2018-07-03 15:57:48','2018-07-03 15:57:48','2018-07-03 15:57:48','0000-00-00 00:00:00'),(3,2,2018,'2018-07-03 15:57:48','2018-07-03 15:57:48','2018-07-03 15:57:48','0000-00-00 00:00:00'),(4,2,2018,'2018-07-03 15:57:48','2018-07-03 15:57:48','2018-07-03 15:57:48','0000-00-00 00:00:00'),(5,3,2018,'2018-07-03 15:57:48','2018-07-03 15:57:48','2018-07-03 15:57:48','0000-00-00 00:00:00'),(6,3,2018,'2018-07-03 15:57:49','2018-07-03 15:57:49','2018-07-03 15:57:49','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `Log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Notification`
--

DROP TABLE IF EXISTS `Notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `text` varchar(45) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Notifica_User1_idx` (`userId`),
  CONSTRAINT `fk_Notifica_User1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notification`
--

LOCK TABLES `Notification` WRITE;
/*!40000 ALTER TABLE `Notification` DISABLE KEYS */;
INSERT INTO `Notification` (`id`, `date`, `text`, `status`, `userId`) VALUES (1,'2018-07-03 13:44:49','notText1',0,1),(2,'2018-07-03 13:44:49','notText2',0,1),(3,'2018-07-03 13:44:49','notText3',1,2),(4,'2018-07-03 13:44:49','notText4',1,2),(5,'2018-07-03 13:44:49','notText5',0,3),(6,'2018-07-03 13:44:49','notText6',1,3),(7,'2018-07-03 13:44:49','notText7',1,4),(8,'2018-07-03 13:44:49','notText8',0,4),(9,'2018-07-03 13:44:49','notText9',1,5),(10,'2018-07-03 13:44:49','notText10',1,5);
/*!40000 ALTER TABLE `Notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Permission`
--

DROP TABLE IF EXISTS `Permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `addObject` tinyint(4) NOT NULL,
  `deleteObject` tinyint(4) NOT NULL,
  `modifyList` tinyint(4) NOT NULL,
  `deleteList` tinyint(4) NOT NULL,
  `listId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Permissi_User1_idx` (`userId`),
  KEY `fk_Permissi_List1` (`listId`),
  CONSTRAINT `fk_Permissi_List1` FOREIGN KEY (`listId`) REFERENCES `List` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Permissi_User1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Permission`
--

LOCK TABLES `Permission` WRITE;
/*!40000 ALTER TABLE `Permission` DISABLE KEYS */;
INSERT INTO `Permission` (`id`, `addObject`, `deleteObject`, `modifyList`, `deleteList`, `listId`, `userId`) VALUES (1,1,1,1,1,1,1),(2,1,1,0,0,1,2),(3,1,1,0,0,1,3),(4,1,1,1,0,2,4),(5,1,1,1,1,2,2),(6,0,0,0,1,7,1),(7,1,1,1,1,5,5);
/*!40000 ALTER TABLE `Permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Product`
--

DROP TABLE IF EXISTS `Product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `img` varchar(45) NOT NULL,
  `logo` varchar(45) NOT NULL,
  `categoryProductId` int(11) NOT NULL,
  `privateListId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Product_CategoryProduct1_idx` (`categoryProductId`),
  KEY `_idx` (`privateListId`),
  CONSTRAINT `fk_Product_CategoryProduct1` FOREIGN KEY (`categoryProductId`) REFERENCES `CategoryProduct` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Product_List1` FOREIGN KEY (`privateListId`) REFERENCES `List` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Product`
--

LOCK TABLES `Product` WRITE;
/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
INSERT INTO `Product` (`id`, `name`, `description`, `img`, `logo`, `categoryProductId`, `privateListId`) VALUES (1,'prod1','desc1','img1','logo1',1,NULL),(2,'prod2','desc2','img2','logo2',1,NULL),(3,'prod3','desc3','img3','logo3',2,NULL),(4,'prod4','desc4','img4','logo4',2,NULL),(5,'prod5','desc5','img5','logo5',3,NULL),(6,'prod6','desc6','img6','logo6',3,NULL),(7,'prod7','desc7','img7','logo7',4,NULL),(8,'prod8','desc8','img8','logo8',4,NULL),(9,'prod9','desc9','img9','logo9',5,NULL),(10,'prod10','desc10','img10','logo10',5,NULL);
/*!40000 ALTER TABLE `Product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProductInList`
--

DROP TABLE IF EXISTS `ProductInList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProductInList` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` tinyint(4) NOT NULL,
  `productId` int(11) NOT NULL,
  `listId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ProductInList_List1_idx` (`listId`),
  KEY `fk_ProductInList_Product` (`productId`),
  CONSTRAINT `fk_ProductInList_List1` FOREIGN KEY (`listId`) REFERENCES `List` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProductInList_Product` FOREIGN KEY (`productId`) REFERENCES `Product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProductInList`
--

LOCK TABLES `ProductInList` WRITE;
/*!40000 ALTER TABLE `ProductInList` DISABLE KEYS */;
INSERT INTO `ProductInList` (`id`, `status`, `productId`, `listId`) VALUES (1,0,1,1),(2,0,2,1),(3,0,3,1),(4,0,4,1),(5,0,1,2),(6,0,8,2),(7,0,3,3),(8,0,5,3),(9,0,4,4),(10,0,4,5),(11,0,4,6),(12,0,4,7),(13,0,6,8),(14,0,7,8);
/*!40000 ALTER TABLE `ProductInList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Shop`
--

DROP TABLE IF EXISTS `Shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `category` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Shop_CategoryList1_idx` (`category`),
  CONSTRAINT `fk_Shop_CategoryList1` FOREIGN KEY (`category`) REFERENCES `CategoryList` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Shop`
--

LOCK TABLES `Shop` WRITE;
/*!40000 ALTER TABLE `Shop` DISABLE KEYS */;
INSERT INTO `Shop` (`id`, `lat`, `lng`, `category`) VALUES (1,123,123,1),(2,132,132,1),(3,213,213,2),(4,231,231,2),(5,312,312,3),(6,321,321,3);
/*!40000 ALTER TABLE `Shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `img` varchar(45) NOT NULL,
  `isAdmin` tinyint(4) NOT NULL,
  `verifyEmailLink` char(36) DEFAULT NULL,
  `resendPwdEmailLink` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `verify_mail_link_UNIQUE` (`verifyEmailLink`),
  UNIQUE KEY `resend_mail_link_UNIQUE` (`resendPwdEmailLink`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` (`id`, `name`, `surname`, `email`, `password`, `img`, `isAdmin`, `verifyEmailLink`, `resendPwdEmailLink`) VALUES (1,'name1','surname1','usr1@test.com','pwd1','img1',1,'verem1@test.com','resem1@test.com'),(2,'name2','surname2','usr2@test.com','pwd2','img2',0,'verem2@test.com','resem2@test.com'),(3,'name3','surname3','usr3@test.com','pwd3','img3',0,'verem3@test.com','resem3@test.com'),(4,'name4','surname4','usr4@test.com','pwd4','img4',0,'verem4@test.com','resem4@test.com'),(5,'name5','surname5','usr5@test.com','pwd5','img5',0,'verem5@test.com','resem5@test.com');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-07-25 18:14:29
