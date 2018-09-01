-- phpMyAdmin SQL Dump
-- version phpStudy 2014
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2018 年 08 月 15 日 02:29
-- 服务器版本: 5.5.47
-- PHP 版本: 5.3.29

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `mydb`
--

-- --------------------------------------------------------

--
-- 表的结构 `categorylist`
--

CREATE TABLE IF NOT EXISTS `categorylist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `img1` varchar(45) NOT NULL,
  `img2` varchar(45) DEFAULT NULL,
  `img3` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- 转存表中的数据 `categorylist`
--

INSERT INTO `categorylist` (`id`, `name`, `description`, `img1`, `img2`, `img3`) VALUES
(1, 'catogoria della lista 1', 'desc1', '1.jpg', NULL, NULL),
(2, 'catogoria della lista 2', 'desc2', '2.jpg', NULL, NULL),
(3, 'catogoria della lista 3', 'desc3', '3.jpg', NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `categoryproduct`
--

CREATE TABLE IF NOT EXISTS `categoryproduct` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `img` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- 转存表中的数据 `categoryproduct`
--

INSERT INTO `categoryproduct` (`id`, `name`, `description`, `img`) VALUES
(1, 'catProd1', 'descCatProd1', 'cat1.jpg'),
(2, 'catProd2', 'descCatProd2', 'cat2.jpg'),
(3, 'catProd3', 'descCatProd3', 'cat3.jpg'),
(4, 'catProd4', 'descCatProd4', 'cat4.jpg'),
(5, 'catProd5', 'descCatProd5', 'cat1.jpg');

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `userId` int(11) NOT NULL,
  `listId` int(11) NOT NULL,
  `text` text NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_Comment_List1_idx` (`listId`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=29 ;

--
-- 转存表中的数据 `comment`
--

INSERT INTO `comment` (`userId`, `listId`, `text`, `id`) VALUES
(1, 1, 'cmtText1', 1),
(2, 1, 'cmtText2', 2),
(3, 1, 'cmtText3', 3),
(4, 2, 'cmtText4', 4),
(5, 2, 'cmtText5', 5),
(2, 2, 'cmtText6', 6),
(3, 2, 'cmtText7', 7),
(4, 4, 'cmtText8', 8),
(4, 5, 'cmtText9', 9),
(6, 1, 'cmtText10', 10),
(6, 3, '111111111', 11),
(6, 3, 'io sono un commento\r\n', 14),
(6, 1, 'aabb ss', 17),
(6, 1, 'asdas dfvbdfg df ghjjhk h', 19),
(6, 1, 'èèè', 21),
(6, 1, 'io sono AAS', 28);

-- --------------------------------------------------------

--
-- 表的结构 `list`
--

CREATE TABLE IF NOT EXISTS `list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` text,
  `img` varchar(45) DEFAULT NULL,
  `ownerId` int(11) NOT NULL,
  `categoryList` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_List_CategoryList1_idx` (`categoryList`),
  KEY `fk_List_User1_idx` (`ownerId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- 转存表中的数据 `list`
--

INSERT INTO `list` (`id`, `name`, `description`, `img`, `ownerId`, `categoryList`) VALUES
(1, 'list1', 'brbrbrbr sono la desrcizione della lista 1', '1.jpg', 6, 3),
(2, 'list2', 'brbrbrbr sono la desrcizione della lista 2', '1.jpg', 6, 1),
(3, 'list3', 'brbrbrbr sono la desrcizione della lista 3', '1.jpg', 1, 1),
(4, 'list4', 'brbrbrbr sono la desrcizione della lista 4', '1.jpg', 1, 1),
(5, 'list5', 'brbrbrbr sono la desrcizione della lista 5', '1.jpg', 1, 1),
(6, 'list6', 'brbrbrbr sono la desrcizione della lista 6', '1.jpg', 1, 1),
(7, 'list7', 'brbrbrbr sono la desrcizione della lista 7', '1.jpg', 1, 1),
(8, 'list8', 'brbrbrbr sono la desrcizione della lista 8', '1.jpg', 1, 1);

-- --------------------------------------------------------

--
-- 表的结构 `log`
--

CREATE TABLE IF NOT EXISTS `log` (
  `productId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `last1` timestamp NULL DEFAULT NULL,
  `last2` timestamp NULL DEFAULT NULL,
  `last3` timestamp NULL DEFAULT NULL,
  `last4` timestamp NULL DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_Log_Product1_idx` (`productId`),
  KEY `fk_Log_User1_idx` (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

--
-- 转存表中的数据 `log`
--

INSERT INTO `log` (`productId`, `userId`, `last1`, `last2`, `last3`, `last4`, `id`) VALUES
(1, 1, '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 1),
(2, 1, '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 2),
(3, 2, '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 3),
(4, 2, '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 4),
(5, 3, '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 5),
(6, 3, '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 6),
(1, 1, NULL, NULL, NULL, NULL, 7),
(1, 1, '2018-07-20 13:32:55', NULL, NULL, NULL, 8),
(3, 6, '2018-08-03 10:59:22', '2018-07-22 12:39:31', NULL, NULL, 9),
(157, 6, '2018-07-28 17:34:41', '2018-07-25 15:31:17', '2018-07-25 15:30:53', '2018-07-25 15:30:25', 10),
(155, 6, '2018-07-28 17:30:32', '2018-07-26 14:52:33', '2018-07-26 14:51:21', '2018-07-25 08:52:03', 11),
(150, 6, '2018-07-22 13:15:54', NULL, NULL, NULL, 12),
(154, 6, '2018-07-30 10:30:56', '2018-07-22 13:15:57', NULL, NULL, 13),
(149, 6, '2018-08-04 20:30:12', '2018-07-22 16:36:46', NULL, NULL, 14),
(8, 6, '2018-07-22 16:36:51', NULL, NULL, NULL, 15),
(156, 6, '2018-07-26 14:52:06', '2018-07-26 14:51:45', '2018-07-26 14:43:44', NULL, 16),
(152, 6, '2018-08-03 09:20:41', '2018-07-31 13:27:51', NULL, NULL, 17),
(153, 6, '2018-08-01 19:34:30', NULL, NULL, NULL, 18),
(151, 6, '2018-08-01 19:34:48', NULL, NULL, NULL, 19);

-- --------------------------------------------------------

--
-- 表的结构 `notification`
--

CREATE TABLE IF NOT EXISTS `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `text` text NOT NULL,
  `status` tinyint(4) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Notifica_User1_idx` (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- 转存表中的数据 `notification`
--

INSERT INTO `notification` (`id`, `date`, `text`, `status`, `userId`) VALUES
(1, '2018-07-03 13:44:49', 'notText1', 0, 1),
(2, '2018-07-03 13:44:49', 'notText2', 0, 1),
(3, '2018-07-03 13:44:49', 'notText3', 1, 2),
(4, '2018-07-03 13:44:49', 'notText4', 1, 2),
(5, '2018-07-03 13:44:49', 'notText5', 0, 3),
(6, '2018-07-03 13:44:49', 'notText6', 1, 3),
(7, '2018-07-03 13:44:49', 'notText7', 1, 4),
(8, '2018-07-03 13:44:49', 'notText8', 0, 4),
(9, '2018-07-03 13:44:49', 'notText9', 1, 5),
(10, '2018-07-03 13:44:49', 'notText10', 1, 5);

-- --------------------------------------------------------

--
-- 表的结构 `permission`
--

CREATE TABLE IF NOT EXISTS `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `addObject` tinyint(4) NOT NULL,
  `deleteObject` tinyint(4) NOT NULL,
  `modifyList` tinyint(4) NOT NULL,
  `deleteList` tinyint(4) NOT NULL,
  `listId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Permissi_User1_idx` (`userId`),
  KEY `listId` (`listId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

--
-- 转存表中的数据 `permission`
--

INSERT INTO `permission` (`id`, `addObject`, `deleteObject`, `modifyList`, `deleteList`, `listId`, `userId`) VALUES
(1, 1, 1, 1, 1, 1, 6),
(2, 0, 1, 1, 0, 2, 6),
(3, 1, 1, 0, 0, 3, 6),
(4, 0, 1, 0, 0, 1, 4),
(6, 1, 0, 1, 0, 1, 1),
(9, 0, 0, 1, 1, 1, 2),
(13, 0, 0, 0, 0, 2, 2);

-- --------------------------------------------------------

--
-- 表的结构 `product`
--

CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `img` varchar(45) NOT NULL,
  `logo` varchar(45) NOT NULL,
  `categoryProductId` int(11) NOT NULL,
  `privateListId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Product_CategoryProduct1_idx` (`categoryProductId`),
  KEY `fk_Product_List1_idx` (`privateListId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=158 ;

--
-- 转存表中的数据 `product`
--

INSERT INTO `product` (`id`, `name`, `description`, `img`, `logo`, `categoryProductId`, `privateListId`) VALUES
(1, 'prodotto1', 'desc1', '1.jpg', '1.jpg', 1, NULL),
(2, 'prodotto2', 'desc2', '2.jpg', '2.jpg', 1, NULL),
(3, 'prodotto3', 'desc3', '3.jpg', '3.jpg', 2, NULL),
(4, 'prodotto4', 'desc4', '4.jpg', '4.jpg', 2, NULL),
(5, 'prodotto5', 'desc5', '5.jpg', '5.jpg', 3, NULL),
(6, 'prodotto6', 'desc6', '6.jpg', '6.jpg', 3, NULL),
(7, 'prodotto7', 'desc7', '7.jpg', '7.jpg', 4, NULL),
(8, 'prodotto8', 'desc8', '8.jpg', 'default.jpg', 4, NULL),
(9, 'prodotto9', 'desc9', '9.jpg', 'default.jpg', 5, NULL),
(10, 'prodotto10', 'desc10', '10.jpg', 'default.jpg', 5, NULL),
(128, 'prodotto11', 'descrizione 11', '11.jpg', 'default.jpg', 1, NULL),
(129, 'prodotto12', 'descrizione 12', '12.jpg', 'default.jpg', 3, NULL),
(130, 'prodotto13', 'descrizione 13', '13.jpg', 'default.jpg', 4, NULL),
(131, 'prodotto14', 'descrizione 14', '14.jpg', 'default.jpg', 5, NULL),
(132, 'prodotto15', 'descrizione 15', '15.jpg', 'default.jpg', 1, NULL),
(133, 'prodotto16', 'descrizione 16', '16.jpg', 'default.jpg', 2, NULL),
(134, 'prodotto17', 'descrizione 17', '17.jpg', 'default.jpg', 3, NULL),
(135, 'prodotto18', 'descrizione 18', '18.jpg', 'default.jpg', 4, NULL),
(136, 'prodotto19', 'descrizione 19', '19.jpg', 'default.jpg', 5, NULL),
(137, 'prodotto20', 'descrizione 20', '20.jpg', 'default.jpg', 1, NULL),
(138, 'prodotto21', 'descrizione 21', '21.jpg', 'default.jpg', 2, NULL),
(139, 'prodotto22', 'descrizione 22', '22.jpg', 'default.jpg', 3, NULL),
(140, 'prodotto23', 'descrizione 23', '23.jpg', 'default.jpg', 4, NULL),
(141, 'prodotto24', 'descrizione 24', '24.jpg', 'default.jpg', 5, NULL),
(142, 'prodotto25', 'descrizione 25', '25.jpg', 'default.jpg', 1, NULL),
(143, 'prodotto26', 'descrizione 26', '26.jpg', 'default.jpg', 2, NULL),
(144, 'prodotto27', 'descrizione 27', '27.jpg', 'default.jpg', 3, NULL),
(145, 'prodotto28', 'descrizione 28', '28.jpg', 'default.jpg', 4, NULL),
(146, 'prodotto29', 'descrizione 29', '29.jpg', 'default.jpg', 5, NULL),
(147, 'prodotto30', 'descrizione 30', '30.jpg', 'default.jpg', 1, NULL),
(148, 'prodotto31', 'descrizione 31', '31.jpg', 'default.jpg', 2, NULL),
(149, 'prodotto32', 'descrizione 32', '32.jpg', 'default.jpg', 3, NULL),
(150, 'prodotto33', 'descrizione 33', '33.jpg', 'default.jpg', 4, NULL),
(151, 'prodotto34', 'descrizione 34', '34.jpg', 'default.jpg', 5, NULL),
(152, 'prodotto35', 'descrizione 35', '35.jpg', '6.jpg', 1, NULL),
(153, 'prodotto36', 'descrizione 36', '36.jpg', '5.jpg', 2, NULL),
(154, 'prodotto37', 'descrizione 37', '37.jpg', '4.jpg', 3, NULL),
(155, 'prodotto38', 'descrizione 38', '38.jpg', '3.jpg', 4, NULL),
(156, 'prodotto39', 'descrizione 39', '39.jpg', '2.jpg', 5, NULL),
(157, 'prodotto40', 'descrizione 40', '40.jpg', '1.jpg', 1, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `productinlist`
--

CREATE TABLE IF NOT EXISTS `productinlist` (
  `productId` int(11) NOT NULL,
  `listId` int(11) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_ProductInList_List1_idx` (`listId`),
  KEY `productId` (`productId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=58 ;

--
-- 转存表中的数据 `productinlist`
--

INSERT INTO `productinlist` (`productId`, `listId`, `status`, `id`) VALUES
(3, 3, 1, 7),
(4, 4, 0, 9),
(4, 5, 0, 10),
(4, 6, 0, 11),
(4, 7, 0, 12),
(6, 8, 0, 13),
(7, 8, 0, 14),
(156, 3, 1, 27),
(155, 1, 1, 36),
(157, 1, 1, 37),
(154, 1, 1, 39),
(153, 1, 1, 40),
(152, 1, 1, 41),
(150, 1, 0, 43),
(149, 1, 1, 44),
(148, 1, 0, 45),
(147, 1, 0, 46),
(146, 1, 0, 47),
(137, 1, 0, 49),
(132, 1, 0, 50),
(156, 1, 0, 51),
(144, 1, 0, 52),
(143, 1, 0, 53),
(142, 1, 0, 54),
(141, 1, 0, 55),
(140, 1, 0, 56),
(138, 1, 0, 57);

-- --------------------------------------------------------

--
-- 表的结构 `shop`
--

CREATE TABLE IF NOT EXISTS `shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `category` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Shop_CategoryList1_idx` (`category`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- 转存表中的数据 `shop`
--

INSERT INTO `shop` (`id`, `lat`, `lng`, `category`) VALUES
(1, 123, 123, 1),
(2, 132, 132, 1),
(3, 213, 213, 2),
(4, 231, 231, 2),
(5, 312, 312, 3),
(6, 321, 321, 3);

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `img` varchar(45) NOT NULL,
  `isAdmin` tinyint(1) NOT NULL,
  `verifyEmailLink` char(36) DEFAULT NULL,
  `resetPwdEmailLink` char(36) DEFAULT NULL,
  `acceptedPrivacy` tinyint(1) NOT NULL,
  `lastLoginTimeMillis` bigint(20) DEFAULT NULL,
  `keyForFastLogin` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=17 ;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `name`, `surname`, `email`, `password`, `img`, `isAdmin`, `verifyEmailLink`, `resetPwdEmailLink`, `acceptedPrivacy`, `lastLoginTimeMillis`, `keyForFastLogin`) VALUES
(1, 'name1', 'surname1', '1@qq.com', '99024280cab824efca53a5d1341b9210', 'default.jpg', 1, 'verem1@test.com', 'resem1@test.com', 0, 2018, ''),
(2, 'name2', 'surname2', '2@qq.com', 'pwd2', '1.jpg', 0, 'verem2@test.com', 'resem2@test.com', 0, 2018, ''),
(3, 'name3', 'surname3', '3@qq.com', 'pwd3', '2.jpg', 0, 'verem3@test.com', 'resem3@test.com', 0, 0, ''),
(4, 'name4', 'surname4', '41@qq.com', 'pwd4', '4.jpg', 0, 'verem4@test.com', 'resem4@test.com', 0, 0, ''),
(5, 'name5', 'surname5', '2', 'c81e728d9d4c2f636f067f89cc14862c', '5.jpg', 0, 'ssad', 'resem5@test.com', 0, 0, ''),
(6, '1', '1', 'mikuclub@qq.com', 'c4ca4238a0b923820dcc509a6f75849b', '3.jpg', 0, NULL, 'c2817536-d6af-4e30-a491-227c8752097f', 0, 1534189131803, '5FCAECA58DC1BA49EF82A673C7E161CD'),
(8, 'WENJIE', 'LIU', 'hexie21092@gmail.com', 'a4e2a9651e0d0796db0ebcf58771f413', 'user-ninja.svg', 0, '4dd25827-4c8b-4234-9eb1-5beadcbe27c3', NULL, 0, NULL, NULL),
(16, 'wenjie', 'liu', '1256883490@qq.com', '861B8E66B14CB42B94D684BC206E9DAA', '20180813145542855.jpg', 0, NULL, NULL, 0, 0, NULL);

--
-- 限制导出的表
--

--
-- 限制表 `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`listId`) REFERENCES `list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `list`
--
ALTER TABLE `list`
  ADD CONSTRAINT `fk_List_CategoryList1` FOREIGN KEY (`categoryList`) REFERENCES `categorylist` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `list_ibfk_1` FOREIGN KEY (`ownerId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `log_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `log_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `permission`
--
ALTER TABLE `permission`
  ADD CONSTRAINT `permission_ibfk_1` FOREIGN KEY (`listId`) REFERENCES `list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `permission_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `fk_Product_CategoryProduct1` FOREIGN KEY (`categoryProductId`) REFERENCES `categoryproduct` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`privateListId`) REFERENCES `list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `productinlist`
--
ALTER TABLE `productinlist`
  ADD CONSTRAINT `productinlist_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `productinlist_ibfk_2` FOREIGN KEY (`listId`) REFERENCES `list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `shop`
--
ALTER TABLE `shop`
  ADD CONSTRAINT `fk_Shop_CategoryList1` FOREIGN KEY (`category`) REFERENCES `categorylist` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
