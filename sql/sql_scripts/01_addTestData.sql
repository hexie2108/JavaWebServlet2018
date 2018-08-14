-- MySQL dump 10.16  Distrib 10.1.35-MariaDB, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `CategoryList`
--

USE `mydb`;

/*!40000 ALTER TABLE `CategoryList` DISABLE KEYS */;
INSERT INTO `CategoryList` (`id`, `name`, `description`, `img1`, `img2`, `img3`) VALUES (1,'catogoria della lista 1','desc1','1.jpg',NULL,NULL),(2,'catogoria della lista 2','desc2','2.jpg',NULL,NULL),(3,'catogoria della lista 3','desc3','3.jpg',NULL,NULL);
/*!40000 ALTER TABLE `CategoryList` ENABLE KEYS */;

--
-- Dumping data for table `CategoryProduct`
--

/*!40000 ALTER TABLE `CategoryProduct` DISABLE KEYS */;
INSERT INTO `CategoryProduct` (`id`, `name`, `description`, `img`) VALUES (1,'catProd1','descCatProd1','cat1.jpg'),(2,'catProd2','descCatProd2','cat2.jpg'),(3,'catProd3','descCatProd3','cat3.jpg'),(4,'catProd4','descCatProd4','cat4.jpg'),(5,'catProd5','descCatProd5','cat1.jpg');
/*!40000 ALTER TABLE `CategoryProduct` ENABLE KEYS */;

--
-- Dumping data for table `Comment`
--

/*!40000 ALTER TABLE `Comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comment` ENABLE KEYS */;

--
-- Dumping data for table `List`
--

/*!40000 ALTER TABLE `List` DISABLE KEYS */;
/*!40000 ALTER TABLE `List` ENABLE KEYS */;

--
-- Dumping data for table `Log`
--

/*!40000 ALTER TABLE `Log` DISABLE KEYS */;
/*!40000 ALTER TABLE `Log` ENABLE KEYS */;

--
-- Dumping data for table `Notification`
--

/*!40000 ALTER TABLE `Notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `Notification` ENABLE KEYS */;

--
-- Dumping data for table `Permission`
--

/*!40000 ALTER TABLE `Permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `Permission` ENABLE KEYS */;

--
-- Dumping data for table `Product`
--

/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
/*!40000 ALTER TABLE `Product` ENABLE KEYS */;

--
-- Dumping data for table `ProductInList`
--

/*!40000 ALTER TABLE `ProductInList` DISABLE KEYS */;
/*!40000 ALTER TABLE `ProductInList` ENABLE KEYS */;

--
-- Dumping data for table `Shop`
--

/*!40000 ALTER TABLE `Shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `Shop` ENABLE KEYS */;

--
-- Dumping data for table `User`
--

/*!40000 ALTER TABLE `User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-14 22:48:54
