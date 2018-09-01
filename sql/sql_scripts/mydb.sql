-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 2018-08-31 22:00:38
-- 服务器版本： 5.7.23-0ubuntu0.16.04.1
-- PHP Version: 7.1.20-1+ubuntu16.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mydb`
--

-- --------------------------------------------------------

--
-- 表的结构 `categorylist`
--

CREATE TABLE `CategoryList` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `img1` varchar(45) NOT NULL,
  `img2` varchar(45) DEFAULT NULL,
  `img3` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `categorylist`
--

INSERT INTO `CategoryList` (`id`, `name`, `description`, `img1`, `img2`, `img3`) VALUES
(1, 'supermercato', ' tutti i prodotti di consumo quotidiamo', '1.jpg', 'be1c6aee-eb38-40b4-ad58-54c45fc710ec.jpg', '8d965043-e797-4393-b560-fc60b11e6a2f.jpg'),
(2, 'ferramenta', 'comprende i prodotti metalici e accessori', '2.jpg', 'ce2fd289-1322-4755-aba5-6e12686b3a19.jpg', '97045c11-4528-48bf-b1d5-48d9067f6091.jpg'),
(3, 'elettronica', 'comprende PC, Notebook, Telefonia, ecc..', 'c3e6d805-df05-4f03-99ac-95ff86802c59.jpg', 'f3165e19-71ed-45a5-abc6-2f0e7bca39d8.jpg', 'cbf70c7e-6a81-4ab4-9d22-db25246ad84d.jpg'),
(4, 'pasticceria', 'comprende gli alimenti dolci', '8f2a0f73-5e96-478d-b04c-6471ce523f29.jpg', 'ef44c73e-1ec0-4046-83bd-b763734e620b.jpg', 'ef2ae0e1-ac84-4061-9da9-afe2ee353ba7.jpg'),
(5, 'abbigliamento', 'comprende tutti gli vestiti, pantaloni..', '0809e8bd-292b-43b5-a2fa-c63b26cac3f0.jpg', '95db48f8-a084-45cf-86a9-ca6470c3e2a2.jpg', '6f07ea56-70a9-4984-90df-548d4ea1968d.jpg'),
(6, 'Ferramenta', 'Tutto quel che puo\' servire per il fai da te', 'c78f0c6f-de62-4778-bd4f-65e4aab2fa9a.jpg', '9ace9785-b10d-4dea-8467-33ffda0510a8.jpg', '1c7eb4ed-5a1c-43a2-a733-3a180a029ce8.jpg');

-- --------------------------------------------------------

--
-- 表的结构 `categoryproduct`
--

CREATE TABLE `CategoryProduct` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `img` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `categoryproduct`
--

INSERT INTO `CategoryProduct` (`id`, `name`, `description`, `img`) VALUES
(9, 'Alimentari', 'Cibo vario di origine animale e vegetale', '85abdaf4-9025-4588-a829-1f83db678374.jpg'),
(10, 'Bevande', 'Qualsiasi cosa si beva', '4f58bec9-9a9d-4a75-b33e-6a6b0fc16961.jpg'),
(11, 'Cosmetica', 'Prodotti di cura corporea', 'ff351a11-2a08-4075-9e06-74ec7059200c.jpg'),
(12, 'Surgelati', 'Cibo surgelato', '28a83c58-703b-495e-bc4d-2c1e1461d0b9.jpg'),
(13, 'Carboidrati', 'Cibo carboidrati', '0af8ff85-26d6-451e-872c-c7eddf4f0a26.jpg'),
(14, 'Telefonia e Tablet', 'Contiene prodotti telefoni e tablet', '7b231b79-0642-4e67-aa0a-297930db5b3b.jpg'),
(15, 'Consoles di gioco', 'Contiene le consoles di gioco', 'c1161402-99e3-4934-b6ca-b997df92426b.jpg'),
(16, 'Elettrodomestici', 'Contiene i piu\' comuni elettrodomestici', '921d235b-f242-4da5-8ca2-eac3e5de9882.jpg'),
(17, 'Parti superiori vestiario', 'Abbigliamento parti superiori', '0d7ed4db-4677-4cfa-84c6-4ad98f305b8f.jpg'),
(18, 'Accessori abbigliamento', 'Contiene gli accessori indossabili', '285de308-3e25-4ea4-91e6-11b760be6bd6.jpg'),
(19, 'Parti inferiori vestiario', 'Contiene le parti inferiori di abbigliamento', '53901a07-a543-4367-acfe-05b745c5d440.jpg'),
(20, 'Abbigliamento intimo', 'Contiene parti del vestiario intimo', '4a19b324-602a-45ac-8553-2f3e29aceef2.jpg'),
(21, 'Scarpe', 'Vari generi di scarpe', '56df5f23-30b4-423c-85c0-996d0d0b0ce7.jpg'),
(22, 'Attrezzi', 'Attrezzi', '2cf76790-425c-4c89-b81d-b3d1d9ca7f04.jpg'),
(23, 'Viteria', 'Viti, chiodi. Fissaggio in generale', 'b3fb6cb9-7d4c-41dc-8df9-0faa38b1d18a.jpg'),
(24, 'Legname', 'Legname vario per fai da te', 'b966b4c3-eb94-46a6-9a76-d77ee76e53ca.jpg');

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

CREATE TABLE `Comment` (
  `userId` int(11) NOT NULL,
  `listId` int(11) NOT NULL,
  `text` text NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `comment`
--

INSERT INTO `Comment` (`userId`, `listId`, `text`, `id`) VALUES
(20, 14, 'I need to buy bananas', 29);

-- --------------------------------------------------------

--
-- 表的结构 `list`
--

CREATE TABLE `List` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` text,
  `img` varchar(45) DEFAULT NULL,
  `ownerId` int(11) NOT NULL,
  `categoryList` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `list`
--

INSERT INTO `List` (`id`, `name`, `description`, `img`, `ownerId`, `categoryList`) VALUES
(1, 'mio lista di natale', 'tutti i prodotti per natale', '1.jpg', 6, 2),
(14, 'Bananas', 'List of bananas', '1da9b8ed-d9f1-49a3-b75f-39b806632f78.jpg', 20, 1);

-- --------------------------------------------------------

--
-- 表的结构 `log`
--

CREATE TABLE `Log` (
  `productId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `last1` timestamp NULL DEFAULT NULL,
  `last2` timestamp NULL DEFAULT NULL,
  `last3` timestamp NULL DEFAULT NULL,
  `last4` timestamp NULL DEFAULT NULL,
  `id` int(11) NOT NULL,
  `emailStatus` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `log`
--

INSERT INTO `Log` (`productId`, `userId`, `last1`, `last2`, `last3`, `last4`, `id`, `emailStatus`) VALUES
(244, 6, '2018-08-31 17:19:33', NULL, NULL, NULL, 23, 0);

-- --------------------------------------------------------

--
-- 表的结构 `permission`
--

CREATE TABLE `Permission` (
  `id` int(11) NOT NULL,
  `addObject` tinyint(4) NOT NULL,
  `deleteObject` tinyint(4) NOT NULL,
  `modifyList` tinyint(4) NOT NULL,
  `deleteList` tinyint(4) NOT NULL,
  `listId` int(11) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `permission`
--

INSERT INTO `Permission` (`id`, `addObject`, `deleteObject`, `modifyList`, `deleteList`, `listId`, `userId`) VALUES
(1, 1, 1, 1, 1, 1, 6),
(9, 0, 0, 1, 1, 1, 2),
(18, 0, 0, 0, 0, 1, 1),
(39, 1, 1, 1, 1, 14, 20),
(40, 0, 0, 0, 0, 14, 6);

-- --------------------------------------------------------

--
-- 表的结构 `product`
--

CREATE TABLE `Product` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `img` varchar(45) NOT NULL,
  `logo` varchar(45) NOT NULL,
  `categoryProductId` int(11) NOT NULL,
  `privateListId` int(11) DEFAULT NULL
) ;

--
-- 转存表中的数据 `product`
--

INSERT INTO `Product` (`id`, `name`, `description`, `img`, `logo`, `categoryProductId`, `privateListId`) VALUES
(164, 'Cetrioli', 'Cetrioli freschi', 'e7069c97-012c-49c6-ade7-a932e879267d.jpg', '83ed40f8-0829-4e73-bd50-8f604253eeaf.jpg', 9, NULL),
(165, 'Melanzane', 'Melanzane fresche', '11061a22-68fc-48f6-8cbc-256c94cf3e2f.jpg', '243e9eba-e23f-48ca-a8ce-36208edf8470.jpg', 9, NULL),
(166, 'Zucchine', 'Zucchine fresche', 'f8263189-121f-4559-ba44-51a73aa289b5.jpg', 'e3cbd936-528b-469c-9f13-f9e335075cee.jpg', 9, NULL),
(167, 'Mele', 'Mele fresche', '6d40825d-45ec-440f-addb-4938996acb55.jpg', '1f745110-99e8-46d9-99be-564e34b37d38.jpg', 9, NULL),
(168, 'Patate', 'Patate intere', '567bf028-beb6-4a7c-bf1c-92114198e43c.jpg', '42d1f5ea-dde4-4c35-a1ab-cff95a7579c4.jpg', 9, NULL),
(169, 'Pesche', 'Pesche fresche', 'aa43675b-661d-4682-a17c-8440314162e0.jpg', '56dd56d2-3ea2-4924-847d-232fa5bc8eae.jpg', 9, NULL),
(170, 'Pomodori', 'Pomodori freschi', 'c1e4ccb3-b816-40bb-85ca-9a86310a6581.jpg', '147e2b81-4cc6-4518-98c1-545f3e7bc1d7.jpg', 9, NULL),
(171, 'Salmone', 'Salmone fresco', 'ead1a78e-32ab-43a1-b2c9-5d68f6f5c03d.jpg', 'f84268b6-ec69-4269-a0f9-306b27cf14ad.jpg', 9, NULL),
(172, 'Uova', 'Uova fresche', 'de478bbb-50db-4115-8ce2-75c5baab917b.jpg', 'b18ce40c-996a-4935-9eab-e9ae15c666a4.jpg', 9, NULL),
(173, 'Zucca', 'Jack O\'Lantern', 'c1930dd0-8d26-4208-8727-43680090ffca.jpg', '09140c99-5876-41a7-89ce-62ee27f75abc.jpg', 9, NULL),
(174, 'Beer', 'Crack a cold one open', '578807e8-55cd-4f9b-b6ec-8e846d112f87.jpg', '2426763c-b23e-40c0-8284-5db45a4416f5.jpg', 10, NULL),
(175, 'Coca cola', 'Sugar juice', '20d59de7-97b8-4b24-9cf5-1fbb586ac05f.jpg', '643ed611-b9b4-4e5c-a0f9-2018634d82f5.jpg', 10, NULL),
(176, 'Bevanda energetica', 'Sugar juice 2', '27601fcb-a376-4c2a-b458-915634d542d4.jpg', '39159b12-4f02-48da-9597-2ca4d5412372.jpg', 10, NULL),
(177, 'Acqua naturale', 'Life juice', '3221ed5d-a486-477c-8da7-70ad47c78771.jpg', '91a5ad17-e8fd-4be2-86eb-c647aef18f8d.jpg', 10, NULL),
(178, 'Latte', 'Succo di mucca', '83459065-ece3-4b5d-b37e-aa060489e4ed.jpg', '4a12e000-d7be-46a3-83c3-48ab74fa5909.jpg', 10, NULL),
(179, 'Latte di cocco', 'Descrizione latte di cocco', 'b9692355-7ec7-462e-96b9-9d861033e03f.jpg', '5e933fb1-a91d-4d6b-a8c8-cd5b24aaa530.jpg', 10, NULL),
(180, 'Acqua gassata', 'Sparkly drinky boi', 'af0fd1a2-6e3b-4a1a-81e0-224721ba0b08.jpg', '454e131a-c545-4df4-8cd8-6a85ece9abaa.jpg', 10, NULL),
(181, 'Succo di frutta', 'Descrizione succo di frutta', '012646b7-6109-47c6-873d-46650d0badf8.jpg', 'abd9040e-af04-4ccb-9e59-e63515648c46.jpg', 10, NULL),
(182, 'Whisky', 'River take my mind', 'fdb4a050-93b8-4bfc-92d1-991488894558.jpg', 'b9057329-e620-4d2c-be49-747b6ad70493.jpg', 10, NULL),
(183, 'Vino', 'E\' finito il vino', 'b21212e2-b447-4dcd-a02c-fabcf76d3ff0.jpg', '835e23c6-e008-4ba3-83e1-41d4a20c0517.jpg', 10, NULL),
(184, 'Borotalco', 'Altola\' il sudore', 'f9af095e-cf7d-41fb-bad1-7ae4e02fb3fe.jpg', '1620e556-2566-4823-ae50-e64f15db5fd4.jpg', 11, NULL),
(185, 'Cipria', 'Descrizione cipria', 'c679fa1e-0327-4cc1-a913-15a6d4738814.jpg', 'eb4b1598-2c9b-499f-aec1-1fc315556a45.jpg', 11, NULL),
(186, 'Crema per mani', 'Descrizione crema per mani', 'c86b7db6-e54a-44ab-87d5-bbaa114f85dc.jpg', '62827570-f410-4d66-b2c7-c7debcb1d161.jpg', 11, NULL),
(187, 'Crema solare', 'Descrizione crema solare', '04f2aad0-3183-4f5d-8791-d972295a3ed7.jpg', '2dcb13c6-6517-4220-8b95-67659faafe3b.jpg', 11, NULL),
(188, 'Eyeliner', 'Descrizione eyeliner', 'd6b31758-84c0-458b-b5cd-ff55af95e3ac.jpg', '5cfa067d-d0e4-4a92-9576-d7dcff2c2d24.jpg', 11, NULL),
(189, 'Fondotinta', 'Descrizione fondotinta', 'd662f0bd-b31b-4428-b96e-a06852000524.jpg', 'dc3626c7-716c-4ae8-a222-bdd137b61f08.jpg', 11, NULL),
(190, 'Saponetta', 'Sapone solido', '0695b8aa-bb0b-4fc9-a601-ed81ddd5881d.jpg', '9eb4458a-568c-4e0c-bdb3-ff7236c257ca.jpg', 11, NULL),
(191, 'Rasoio', 'Per radersi', 'f6370d6a-0d4f-4abc-b6a6-1b3ffd3bffe3.jpg', '2054145a-28cb-4855-bc17-83385bc2a8d6.jpg', 11, NULL),
(192, 'Schiuma da barba', 'Per radersi', '9cebaf01-7b58-44dd-813d-9c80116042c3.jpg', '12b78232-4c78-4190-9cc0-89dc7e4b3b46.jpg', 11, NULL),
(193, 'Sapone', 'Sapone liquido', 'e59a2925-d8bb-4d51-953b-92209b8db56d.jpg', 'a09a824b-2a37-4355-973a-ab44fdf77d93.jpg', 11, NULL),
(194, 'Cozze', 'Cozze surgelate', 'e987f622-8439-4b47-9763-8ed5d7397596.jpg', '033604b2-0512-43de-a53c-2eccd15efad9.jpg', 12, NULL),
(195, 'Dolci', 'Dolci surgelati', 'ecbaef6f-2cd5-4c2a-aaa4-43031fee81af.jpg', '371ab4d4-5e42-4dea-93ff-4f99bc85d4a5.jpg', 12, NULL),
(196, 'Funghi', 'Funghi surgelati', 'b350191a-548b-4817-bcfb-88218be136f5.jpg', '8d8d7ac4-e7b9-40a2-ac1e-5197085a7fc0.jpg', 12, NULL),
(197, 'Gelato', 'Gelato surgelato', '436a0d35-b2ad-46b7-8682-396684bccda7.jpg', 'aaa69716-ad3c-4966-8596-c8b19021c672.jpg', 12, NULL),
(198, 'Hamburger', 'Hamburger surgelato', '8e2920a6-9024-444a-a017-b42f21870f16.jpg', 'eb2ad55a-6623-49f4-9dc3-ba159bbeea09.jpg', 12, NULL),
(199, 'Lasagne', 'Saddest dish', '72a552db-9887-4679-8785-5c4f79aba31c.jpg', 'bde4cfa3-8977-490e-aa0d-f03c57ab0949.jpg', 12, NULL),
(200, 'Pesce', 'Pesce surgelato', '3ccb1261-5308-45ce-9c0b-7606de2c38c9.jpg', 'e8220180-903b-4c3b-a22e-9f2f8db1756e.jpg', 12, NULL),
(201, 'Piselli', 'Piselli surgelati', '5e1fdaa6-3761-4972-aed4-e4439ffe0c4c.jpg', '3bc7b307-88c7-450b-b240-b9ae86120f26.jpg', 12, NULL),
(202, 'Verdure', 'Mix verdure surgelate', '1ddd31ca-7dcb-4c3a-9e3a-18ad876fec9b.jpg', '2c9814aa-dd29-418f-b299-e4a0aede603b.jpg', 12, NULL),
(203, 'Vongole', 'Vongole surgelate', '8b11d196-5006-4139-b63e-ee5140c1d656.jpg', 'ec0fd287-f209-4cd7-a2f1-bfdb3ac02485.jpg', 12, NULL),
(204, 'Bretzel', 'Austrian bread', '1e0c9cc8-46b9-4d45-a132-d783ae2005a4.jpg', 'deb50f63-394d-479d-890a-b2a555df281b.jpg', 13, NULL),
(205, 'Conchiglie', 'Pasta conchiglie', '06295f63-7076-41ce-8ca0-00318caff5b2.jpg', 'b76f0c13-d09c-418e-a23f-af33c88abff5.jpg', 13, NULL),
(206, 'Crostata', 'Torta', '9818e376-97d3-4248-9e4b-3bc89d7519af.jpg', '60a7ff9f-5249-44fd-ae1b-014ef112843e.jpg', 13, NULL),
(207, 'Focaccia', 'Descrizione focaccia', '688ba794-68fa-491a-bf2f-100005965993.jpg', 'ed9edace-744e-4c81-98c3-f837d2b6a8e9.jpg', 13, NULL),
(208, 'Pane', 'Descrizione pane', '60ab4142-2cc8-48a0-a313-e1557093026a.jpg', 'b745729b-53f6-40f4-a6b2-483bde9dcada.jpg', 13, NULL),
(209, 'Penne', 'Pasta penne', '9410943d-056b-47fb-8b41-31ffd06d1ee3.jpg', '8259c162-3ba2-4116-b03e-5f7b0dd8b115.jpg', 13, NULL),
(210, 'Pizza', 'Descrizione pizza', '49a50ec1-6aa9-4b5a-8ff0-5cf6de6cea61.jpg', 'aa5f5502-5610-44bf-8cd8-753c41897ba9.jpg', 13, NULL),
(211, 'Quiche', 'Torta salata', 'a5a72323-20a3-4380-8871-93076cedb8ae.jpg', 'c3e929aa-63d8-455c-9213-d868dc13c271.jpg', 13, NULL),
(212, 'Pasta sfoglia', 'Descrizione pasta sfoglia', 'f21fee4b-339d-4b09-9df3-cecf7a436703.jpg', '4a2b332f-b463-47e4-b1a1-d166f32b89fa.jpg', 13, NULL),
(213, 'Spaghetti', 'Pasta spaghetti', '43e8c02f-680a-4cba-834b-149543aba313.jpg', '0fd16a24-bc1c-4bf1-a658-f6f68f8f77bf.jpg', 13, NULL),
(214, 'Smart cordless', 'Cool looking cordless', '2d880405-796f-41ec-a405-c187cbf1a656.jpg', '36c0b9a6-6d2e-4cf6-80ff-ad89606ec8c0.jpg', 14, NULL),
(215, 'Brondi', 'Telefono brondi', '945323eb-d12b-4bdf-a0a4-66bd5c9ec07c.jpg', '6bfaaef2-a353-4087-927a-99791aed0693.jpg', 14, NULL),
(216, 'Cordless', 'Standard cordless', '707bc118-7232-47f7-bc58-76064cea34c9.jpg', '64b3b196-5dd8-4a2f-b5d7-5922820e0251.jpg', 14, NULL),
(217, 'iPad', 'Apple\'s iPad', '54d3ebf7-56c4-41e3-9b3d-4a35050bd062.jpg', '9b506d50-d24c-408e-99e5-b72849a52530.jpg', 14, NULL),
(218, 'iPhone', 'Apple\'s iPhone', 'b72b38c9-8cf3-4950-925a-32e494b54639.jpg', 'a8172371-e387-49f7-bbdf-4763f2ef52dd.jpg', 14, NULL),
(219, 'Palmare', 'Old device', 'c24b911b-0eb7-460e-aa1c-3997b4189d3e.jpg', '7352b127-1732-4b2b-8912-d610f7f64ff3.jpg', 14, NULL),
(220, 'Smartphone', 'Generic smartphone', '2f197195-03ec-4796-bac7-640adbf68f12.jpg', 'bdc05c6d-85b2-4042-9490-aae86b357ac8.jpg', 14, NULL),
(221, 'Tablet', 'Generic tablet', 'f6bc6f3a-ccae-4bf4-8ef4-1cb70aec40ff.jpg', '2ac52c82-1f01-4108-90b0-2f291906d6e5.jpg', 14, NULL),
(222, 'Tomtom', 'No one uses them anymore', '4eb2cc3b-953d-4c0b-969b-1e59cf269e95.jpg', '1009c911-20bd-4287-88d5-43c904fd0f39.jpg', 14, NULL),
(223, 'Vecchio telefono fisso', 'Un vecchio telefono fisso', 'e6b76f73-a6d9-4e65-82f4-4ef6efa6bbc5.jpg', '756d2769-7d5f-4b68-a8ae-150b9527638b.jpg', 14, NULL),
(224, 'Gameboy', 'Old Gameboy', '5c8dd438-e2ee-46ee-a292-dfbed19068d3.jpg', '0491dd7a-3e08-468e-abae-297638f835f1.jpg', 15, NULL),
(225, 'Gaming PC', 'An expensive gaming PC', 'b3ff2d4b-c1a1-417b-90f7-bcad6d6c1419.jpg', '683053ef-b73a-49ee-ab38-cc820ea1431e.jpg', 15, NULL),
(226, 'Nintendo64', 'Descrizione Nintendo64', 'd0aa3977-fffe-48a3-a135-a62627a3ab9b.jpg', 'a6cfafc5-48a8-499a-add0-13641402ddf6.jpg', 15, NULL),
(227, 'Nintendo DS', 'Descrizione Nintendo DS', 'a508619f-ee63-44dd-80b3-cdeb3ec7568e.jpg', '115791bb-8ad8-467d-9bcf-9a7ae7b2ada8.jpg', 15, NULL),
(228, 'Playstation 3', 'Descrizione Playstation 3', 'ac6a2b13-76d2-4130-a738-92e7c4b0d921.jpg', '045f92b4-9649-4df5-b17d-9d26523961ad.jpg', 15, NULL),
(229, 'Playstation 4', 'Descrizione Playstation 4', 'bd9c015c-26fa-405b-8e64-84296b777a2f.jpg', 'a2dd9fca-4a38-4c81-bb17-c4f3b205d901.jpg', 15, NULL),
(230, 'Xbox One', 'Come fermacarte funziona bene', '25fb83f0-9b8d-4ba0-ab89-e799038b62e0.jpg', 'd6638cc3-14c8-4f49-938e-6d07a90b23d7.jpg', 15, NULL),
(231, 'Nintendo Switch', 'Descrizione Nintendo Switch', '59e06e5f-38ed-4d6e-b402-8fecaf7ae2e8.jpg', '0f38b9a8-9c56-46ac-96a7-221d3c098545.jpg', 15, NULL),
(232, 'Wii', 'Descrizione Wii', '6942bf20-a93a-4de2-a270-9cdf80591209.jpg', '480fd1dd-50ed-48e3-bec6-d95636a73a1e.jpg', 15, NULL),
(233, 'Xbox 360', 'Descrizione Xbox 360', '7e5f1cea-9b19-4e61-a5ad-79afddcee814.jpg', '09231f83-aeb4-4929-abe9-9330546ade1f.jpg', 15, NULL),
(234, 'Abat jour', 'Dim light', 'ea2ffe8f-6326-4efc-9b9a-0b610a09601c.jpg', 'a71409b3-f631-44af-aad7-40a92bea1e04.jpg', 16, NULL),
(235, 'Ferro da stiro', 'Un ferro da stiro', 'a29c93b9-daca-4098-af3a-c04796b80cc7.jpg', 'b9b7d63a-85f7-41f4-8968-7b5ebaf3fde6.jpg', 16, NULL),
(236, 'Forno', 'Un forno', 'cc879e15-9a7f-4bfe-bfd2-a7a9c467242b.jpg', '5dc43082-c561-4b7e-be17-3cab23874782.jpg', 16, NULL),
(237, 'Gelatiera', 'Una gelatiera', 'bd37bd38-768e-4202-8f3c-b5a6b1824d94.jpg', '71ed1afd-d51b-4b82-887d-f78cefe6da72.jpg', 16, NULL),
(238, 'Kettle', 'Un bollitore', '7c9eccd8-ea2c-445f-b7c8-a33d1e61ed35.jpg', 'ed99a000-76e0-428c-adc5-9917b6f03328.jpg', 16, NULL),
(239, 'Lavastoviglie', 'Una lavastoviglie', '3a1efb63-9181-45a4-a6bf-412a433cc225.jpg', '733676d4-1fe6-473a-ae87-18a415deeb69.jpg', 16, NULL),
(240, 'Lavatrice', 'Una lavatrice', 'f7e5eea2-ff2a-478b-b67a-b10f03892408.jpg', '66aa370f-b04a-4c57-9b13-dca2be709c53.jpg', 16, NULL),
(241, 'Mixer', 'Per mixare', 'd9dd6c11-0423-4d1c-bf25-9b28c71a4fab.jpg', 'cb3fe2a8-5102-4124-859c-27acb4f2e791.jpg', 16, NULL),
(242, 'Pinguino', 'Animale antartico', 'd5b512f2-eae0-4611-a936-fb0e5270599c.jpg', 'a3973268-98fd-4190-8720-0cea22de7895.jpg', 16, NULL),
(243, 'Ventilatore', 'Per ventilare', '8d7bf26b-9e1d-42a8-a63d-558c19268aa3.jpg', 'ddd76335-ecf1-45a8-8aac-c3328075077c.jpg', 16, NULL),
(244, 'Cardigan', 'Un cardigan', 'f93ebf3a-550c-48d5-982b-04e7e3c88d51.jpg', '003a6b01-e8b7-4c50-952a-c1c0a66e3af2.jpg', 17, NULL),
(245, 'Felpa', 'Una felpa', 'fa074aac-f38f-4173-b9b4-c5f676567846.jpg', 'd18459a8-0425-433a-beba-6de93b85d68e.jpg', 17, NULL),
(247, 'Giacca', 'Giacca generica', '6a31af32-9381-418c-a0e3-2ffadbae2dc0.jpg', 'feb02031-2251-416e-b156-fbb5d80028d8.jpg', 17, NULL),
(248, 'Giacca a vento', 'Una giacca a vento', '466eae18-e98d-42ee-b331-721fa67086f6.jpg', 'a28cd140-acd1-4d4c-a34f-d9eb369b8d76.jpg', 17, NULL),
(249, 'Golf', 'Un maglioncino golf', '808e6223-507e-4a91-abf6-386bde13a96e.jpg', '92c6dfda-e543-4648-aab3-051b3061a54e.jpg', 17, NULL),
(250, 'Mantello', 'Un mantello', 'a7b02503-3301-4f05-97e4-7af0a6e4aadc.jpg', '6f569299-756b-464a-9f89-e08ad4885e17.jpg', 17, NULL),
(251, 'Piumino', 'Un piumino', '3ae6922e-c558-40f4-8ad6-ee9f956eb813.jpg', '14b55d4a-e320-4ae5-9c8d-9f6122a5af67.jpg', 17, NULL),
(252, 'Trench coat', 'Cool looking coat', 'dd2d8422-d590-47f5-94bb-e1ec5594f030.jpg', '653fb541-06c1-4eb3-8213-4f36cb7dd645.jpg', 17, NULL),
(253, 'T shirt', 'Semplice t shirt', 'a3d81c45-be50-4a42-a0a5-a81a1ea2ab0f.jpg', 'cbd24100-6e9a-41d2-8bea-bd8d03326cdb.jpg', 17, NULL),
(254, 'Berretto', 'Un berretto', '99260adf-4d7d-47ad-a571-8aa9a5d5b57f.jpg', 'ee327fb7-76a0-46a0-a717-3fe3f828c22e.jpg', 18, NULL),
(255, 'Cappello', 'Un cappello', 'ea8dfd1d-225c-4c0e-b443-6fd72c3ec48f.jpg', '852b8177-f637-4333-8eb2-cfb4652e30d4.jpg', 18, NULL),
(256, 'Ghette', 'Delle ghette', '421f6d3b-2dba-44b4-bb93-6d519655c0dd.jpg', '173c787e-ded4-4d21-8e6d-5976dc21e3af.jpg', 18, NULL),
(257, 'Gioielli', 'Generici gioielli, braccialetti, orecchini..', '96297661-b868-49a4-be77-9a958322d639.jpg', '458ab516-3cd5-413e-b7ce-1c5707a0113f.jpg', 18, NULL),
(258, 'Guanti', 'Dei guanti', 'c4c24302-4ee3-4bfb-baa9-f1e31b9f8f1c.jpg', 'd0009cf0-040f-489d-af3c-25df5d222faf.jpg', 18, NULL),
(259, 'Orologio', 'Un orologio da polso', '6ee3fb55-87e3-4f5c-b3a2-68c9954eaeef.jpg', 'dcef5de1-20b4-4210-b32c-3da3d998f64a.jpg', 18, NULL),
(260, 'Sciarpa', 'Una sciarpa', '51210666-4b51-483b-b3e6-fbea12b8c351.jpg', '9b6b797c-fb82-458c-a662-b98ea8e560d8.jpg', 18, NULL),
(261, 'Occhiali da sole', 'Degli occhiali da sole', '0e66201a-6879-4636-8748-f3e202adc24e.jpg', 'aa1522a6-6cb8-48b6-8bd5-118fe6de8445.jpg', 18, NULL),
(262, 'Gonna lunga', 'Una gonna lunga', '1c0804da-898c-44dc-a054-10890c781fe2.jpg', 'db462b44-0eac-4358-9268-896f0396ee22.jpg', 19, NULL),
(263, 'Kilt', 'Un kilt tradizionale', '9716081e-2287-46b9-a4af-72638f1e4d49.jpg', '1eb82c27-d2da-4695-99a3-2a42fd9057f9.jpg', 19, NULL),
(264, 'Boxer', 'Mutande boxer', '771678ed-092f-4139-9da5-b8a4dbd22fe3.jpg', 'c155c692-46d0-4fb9-b49b-d5eead71dedf.jpg', 20, NULL),
(265, 'Reggiseno', 'Un reggiseno', '1e200127-b1b6-4d62-86bc-201c53fa74eb.jpg', '19dc5464-6ced-43d3-aaf5-3e655649ab50.jpg', 20, NULL),
(266, 'Cannottiera', 'Una cannottiera', '60dddd1e-e86b-4c3b-acf5-0ca4e9c71889.jpg', 'b69693cc-5963-47d4-b7ad-8252b98e526f.jpg', 20, NULL),
(267, 'Calze lunghe', 'Delle calze lunghe', 'ff4011c7-278e-4771-8842-f93f91dc4fc4.jpg', 'df739c54-f3e0-49fb-85d3-6a257de12895.jpg', 20, NULL),
(268, 'Mutande', 'Mutande femminili', 'b8dd000e-151d-4765-bfe7-4ce9fb45b271.jpg', '90d76069-b26c-44f7-837e-39cda3ed5039.jpg', 20, NULL),
(269, 'Calze corte', 'Delle calze corte', '88f1fdd7-9d4a-4028-85a5-ca5252f11b6f.jpg', '69a06183-5bf7-4b40-8d1e-7bd52e20f24b.jpg', 20, NULL),
(270, 'Moccassini', 'Dei moccassini', '09d94226-9e45-4c2d-9da9-4ed61f362045.jpg', 'c0fcbbe7-31d0-432b-9a1a-b980d5101c79.jpg', 21, NULL),
(271, 'Sandali', 'Dei sandali', 'fce28a77-f8de-4067-a890-4631b752a834.jpg', '18548e3d-3e9a-4ccc-8c81-7e4c7783d573.jpg', 21, NULL),
(272, 'Scarpe di pelle', 'Eleganti scarpe di pelle', 'd794d5cc-6f9d-4fee-afb0-b94019600975.jpg', '478d4fbb-282c-442d-b9c0-433e838fa2be.jpg', 21, NULL),
(273, 'Stivali', 'Stivali generici', 'fcc8798f-442e-4a94-9a49-7ca10dccb0b0.jpg', '01cec19a-defc-41ac-9f50-50c75dd31f0a.jpg', 21, NULL),
(274, 'Stivali da pesca', 'Degli stivali da pesca', '020dedf9-f61c-47db-b97b-d57c47957618.jpg', 'e003a873-ee01-4025-8f2b-f37801d12ed6.jpg', 21, NULL),
(275, 'Scarpe con i tacchi', 'Femminili scarpe con tacchi', '32144adb-627b-42ad-aa11-ac16c3715218.jpg', 'f5cdd4db-f1e5-47dd-b2d3-7690dfdbce82.jpg', 21, NULL),
(276, 'Martello', 'Un martello. Per martellare', '1c91ee90-8e60-48aa-a3cc-ea58df0d842c.jpg', '4820a5a0-d8c6-4b9a-be6a-c94b4961a883.jpg', 22, NULL),
(277, 'Pennello', 'Un pennello. Per pennellare', '139126e5-01e1-4f71-9698-d3caed3af3e3.jpg', 'fdf37a69-a8b4-4e41-931a-4629101d0795.jpg', 22, NULL),
(278, ' Mizuno Wave Ultima 9 WOS', 'Scarpe da donna', 'bd321a28-01f9-4c6a-8ae6-eed15a23912a.jpg', '9fec1e6a-9661-4ce3-a081-2744a44b4cab.jpg', 21, NULL),
(279, 'Chiave Inglese', 'Una chiave inglese da 10', 'f24666a5-69f0-4853-b5a3-565b65676c76.jpg', '1d922aa3-d09e-4b87-afdd-613cb9238c9e.jpg', 22, NULL),
(280, ' adidas Duramo 8 W', 'Scarpe da donna adidas', '9b183413-ea52-4203-974d-ca224fb11719.jpg', 'aee427f1-7998-4041-bcac-2eeeedbbc21a.jpg', 21, NULL),
(281, 'Gummistiefel Punkte Aus Naturkautschuk', 'Stivali Da Infilare da donna', '452f5130-fce4-4699-8829-c53494e78430.jpg', '289c0714-c697-4e7e-84b7-1bfeb3e25b1e.jpg', 21, NULL),
(282, 'Trapano', 'Un trapano. Per fare i buchi nel muro', '965b6f9d-53ee-4dd3-a9ec-3efb4334f38d.jpg', '38d35a42-696f-4e79-bcb0-554524496246.jpg', 22, NULL),
(283, 'Cacciavite a stella', 'Un cacciavite. Per avvitare', '5f863cd1-39aa-4b2c-94ad-c4ac84e80b33.jpg', '893c2935-9481-4de6-8f32-4f7aeb231eca.jpg', 22, NULL),
(285, 'Forbici da Elettricista', 'Forbici isolate da elettricista', '7290c615-7f6d-4a0f-9c78-bb63e8386d22.jpg', 'd6ff84a7-38ea-418a-8173-bcfd76961e35.jpg', 22, NULL),
(286, 'Sega a mano', 'Una sega a mano con denti da legno', '99043632-345d-4f7a-a277-5af093739205.jpg', 'ec15fb6f-f5d9-4c34-b764-62975436d6ac.jpg', 22, NULL),
(287, 'Seghetto', 'Un seghetto da ferro', '8f76347c-1462-4dc4-958f-8837aa17ce09.jpg', '6b13a754-d187-4e23-a3b1-52e08d7cd2e3.jpg', 22, NULL),
(288, 'Paltons Talaso 0824 145 mm, Occhiali', 'Occhiali da sole', '6af523b3-0b47-499a-a627-cf16c9022796.jpg', '8b8aca1e-12ac-481a-9779-fad2a4ef3df6.jpg', 18, NULL),
(289, 'Pinze', 'Pinze a testa piatta lunghe', 'cae6d7a2-a956-46d0-93c2-f1a66c8ca754.jpg', '24bd85ff-5b1c-4240-9414-bbdfef1c5936.jpg', 22, NULL),
(290, 'Set elegante accessori smoking', 'set di papillon e bretelle, con fazzoletto', '15a044d2-b2a9-4339-b02f-61c7eebd45ee.jpg', 'df90f928-a63f-4038-bf8b-7e3ab2e80798.jpg', 18, NULL),
(291, 'Viti', 'Sacchetto di viti a testa a stella', '97ba39f1-bde5-488e-8c7f-d86f2f0349e0.jpg', '900ca02c-271e-4650-9431-fc1b4ab8a465.jpg', 23, NULL),
(292, 'Viti', 'Sacchetto di viti a testa piatta', 'b09404f3-694c-4a2f-9c49-7047f398ae56.jpg', '034eb5c1-6e81-497f-ad9a-95944093b9e9.jpg', 23, NULL),
(293, 'Chiodi', 'Sacchetto di chiodi', '744925ba-55a1-4121-84c9-7858e113f987.jpg', '888165e0-4f12-45b3-ac4c-f0cf6f72a0cc.jpg', 23, NULL),
(294, 'Dadi', 'Sacchetto di dadi M5', '9296818b-2b9a-4396-ae05-69d2a548ff9e.jpg', '9a58acba-2627-45dd-8cbb-9cf5110b725a.jpg', 23, NULL),
(295, 'Bulloni', 'Sacchetto di bulloni M5', 'cb9e8fe6-7b89-406f-923c-36f521e328a1.jpg', '4a34cd9a-b36c-4aa0-b559-6c42424bcce8.jpg', 23, NULL),
(296, ' Pantaloni da Lavoro Uomo', 'Cotone Multistagione con Tasche Laterali', 'ec58575c-0e3b-4bff-a923-f997e988c157.jpg', '506e8117-badf-4d53-b367-7383ce6fcc6c.jpg', 19, NULL),
(297, 'Rivetti', 'Sacchetto di rivetti in alluminio', '3685567f-9609-4703-87b0-8ac9a3d43ce9.jpg', 'd2be9e8c-9387-4e10-8b6d-4fc0976d8589.jpg', 23, NULL),
(298, 'Rondelle', 'Sacchetto di rondelle piene', '625de100-6213-4edf-b82b-677cf8c8f926.jpg', 'd6794afc-2ab6-4134-b5ce-0de94fc4290b.jpg', 23, NULL),
(299, 'Rondelle', 'Sacchetto di rondelle spezzate', '357dec95-c90f-49a4-be43-2cc636c32027.jpg', '47ac46e4-446c-4738-a968-054910ff8cba.jpg', 23, NULL),
(300, 'pantaloni sportivi da donna', 'pantaloni da yoga da donna di SEASUM', '22308aef-5446-4f20-a94d-ce25acef648b.jpg', 'adf6e139-c3d0-404c-9368-07f7a5ebdf03.jpg', 19, NULL),
(301, 'Rondelle', 'Sacchetto di rondelle dentellate', '0badd0a3-9756-4ee1-ab2d-aba29a82644f.jpg', 'b9826456-372d-4cbf-9e52-fe69661d4b01.jpg', 23, NULL),
(302, 'Viti Fisher', 'Sacchetto di viti Fisher per montaggio', 'dc1ec366-998a-4b87-b959-70fb4069249e.jpg', '61ee8859-bf1f-4f65-a69d-6e268276b169.jpg', 23, NULL),
(303, ' Camicia Casual - Uomo ', 'camicia da uomo Armani con maniche lunghe', '88097210-ff20-424a-9355-1beade322d27.jpg', 'f5fd5125-b8e7-4f24-b6d9-2db3e9af3fd5.jpg', 17, NULL),
(304, 'Assi di Legno', 'Assi di legno di abete ', 'd8ab6849-212e-48fd-9949-48b3e18131b2.jpg', '95e5ae1d-7202-4069-9de7-9d16399a2cf2.jpg', 24, NULL),
(305, 'Assortimento di attrezzi', 'Cassetta degli attrezzi universale', '9ea469c6-7834-4b23-b017-4e206888daa8.jpg', 'd4d671ba-572d-4a06-87f6-45428b2cf457.jpg', 22, NULL),
(306, 'Assi di Legno', 'Assi di legno in quercia', '6a012728-5e8e-4aae-b65e-c3169982e9af.jpg', 'ddbd057a-133f-4615-841b-b98bf96cc1a0.jpg', 24, NULL),
(307, 'Assi di Legno', 'Assi di legno in betulla', '7e8c3d7e-ee12-4201-aeaa-ab13c40f7b3d.jpg', '61632f40-f506-49a0-86a1-ff7089fa4540.jpg', 24, NULL),
(308, 'Assi di Legno', 'Assi di legno in compensato', 'cfb924f6-0d5a-49a8-9375-60a83e477be1.jpg', '08a7aa2b-98ad-441c-bb2f-53517d7fd4b7.jpg', 24, NULL),
(309, 'Legno grezzo', 'Legno grezzo da fuoco o taglio', '1288ee6d-f5ca-48c2-b9ed-9c938a95db21.jpg', 'd5d29cf5-0973-4eed-9fc1-84d17426e5fc.jpg', 24, NULL),
(310, 'MDF', 'Pannelli in MDF', 'e99d02a3-7bb7-48fe-8246-5cd8b09091eb.jpg', '6e151f02-8a99-436b-9e58-d40019564768.jpg', 24, NULL),
(311, ' NAPAPIJRI, Giacca Uomo ', 'Ottoman di nylon. Tessuto traspirante', '070fe6dd-d4dc-402b-88ad-d6e7d170ca4e.jpg', 'c815b31f-b25d-431c-92ba-369855b2a828.jpg', 17, NULL),
(312, 'Assi di Legno', 'Assi di legno in bambu\'', 'f8220491-99df-42a7-8a85-5dea83ebfdf0.jpg', 'd93e2124-21c7-4858-b756-fc0b8b9e66a7.jpg', 24, NULL),
(313, 'Donna Floreale Pizzo Reggiseno', 'Imbottito Senza Ferretto Bra Senza giunte ', '351e2b09-c5ba-42a1-924a-fbb3bc109fa6.jpg', 'f6b882fa-55a0-4dbc-b3e3-04a7f3e1e4e2.jpg', 20, NULL),
(314, 'Assi di Legno', 'Assi di legno laminate', '2de8b48a-666f-42e2-9430-b4b15d5d6464.jpg', '81627d77-99e1-463e-b62e-ea955b82798f.jpg', 24, NULL),
(315, 'Bodywear Boxer da uomo, 2 pezzi', 'Boxer Uomo  95% Cotone, 5% Elastan  ', '8648c191-37c3-4344-af6a-654d038afa5c.jpg', 'ccf5ea34-2f33-44a6-b176-92b4e07df074.jpg', 20, NULL),
(316, 'Intimo da uomo, slip', 'Calvin Klein Confezione da 3 slip', 'c0956f01-a433-48af-8d52-23622d11832e.jpg', '4b24f762-ead7-4996-ba05-7b5a101b4b9c.jpg', 20, NULL),
(317, 'Assi di Legno', 'Assi di legno in pino', '1f3a4216-a02d-4648-b666-7ed2865a2ec3.jpg', '8ee3cd52-bb15-45a2-a5ca-87de082a10b7.jpg', 24, NULL),
(318, 'Reggiseno Push Up', 'Reggiseno senza spalline e senza schienale', 'f17be76e-c11a-4bc9-90e9-83c363e0ecf6.jpg', '3a5bde6a-6d63-4d6c-880c-dce0c936f8c2.jpg', 20, NULL),
(319, 'Legno grezzo', 'Assi di legno grezzo', '51d0ad97-9d86-48b0-9f38-f0c44af87409.jpg', '9c60b1bc-000b-4293-bf52-2ff795d9be35.jpg', 24, NULL),
(320, 'Mini Gonna Svasata', 'Minigonna', '9c0ffe44-08df-4358-b089-917a8ab37e0b.jpg', 'c10fec3d-0035-41f8-b9ad-853d396a0bb2.jpg', 19, NULL),
(321, 'LEE Women\'s Relaxed Fit', '98% Cotton, 2% Spandex, Imported', 'd358d9e0-9130-4d35-acf6-36e9bb871cb0.jpg', '6bd445f3-cbdc-4503-b858-0f69c12150e4.jpg', 19, NULL),
(322, 'Men\'s 24-7 Polyester Cotton Rip', '2-Cargo pockets with hook and loop closure', '0d4da320-d53f-4261-a3ee-747d87b8ce2e.jpg', '495a1452-403c-43de-b117-e90eb95a8b57.jpg', 19, NULL),
(323, 'Pantaloni mimetici Woodland', 'Pantaloni mimetici resistenti.', '4d536ab5-c600-48d5-b3ee-e4b8648e9f1c.jpg', 'd345a20e-404f-4e86-9cbd-942a2fad72a5.jpg', 19, NULL),
(324, 'Casual Pants Trousers Solid Color', 'cotton, comfortable and strench', 'a5143afa-8f60-4bae-9216-ccede2f7fe17.jpg', '35efd187-4438-4ef1-806c-1c0262943647.jpg', 19, NULL),
(325, 'Pantaloni da lavoro', 'Robusti pantaloni da lavoro', 'a26c3c50-9873-4c6f-9850-92564767f76f.jpg', '0a9dd7e7-6ffe-4b4e-b031-81c200235b7b.jpg', 19, NULL),
(326, 'Casual Military Army Cargo', 'A comfortable pant for outdoor activities', 'e918e902-5388-4132-8313-b18577b1de1d.jpg', '2e474731-04af-4e65-9fcd-72ebc786cc22.jpg', 19, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `productinlist`
--

CREATE TABLE `ProductInList` (
  `productId` int(11) NOT NULL,
  `listId` int(11) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `productinlist`
--

INSERT INTO `ProductInList` (`productId`, `listId`, `status`, `id`) VALUES
(244, 1, 1, 77),
(245, 1, 0, 78);

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `User` (
  `id` int(11) NOT NULL,
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
  `keyForFastLogin` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user`
--

INSERT INTO `User` (`id`, `name`, `surname`, `email`, `password`, `img`, `isAdmin`, `verifyEmailLink`, `resetPwdEmailLink`, `acceptedPrivacy`, `lastLoginTimeMillis`, `keyForFastLogin`) VALUES
(1, 'Albano', 'Agostini', '1@qq.com', '99024280cab824efca53a5d1341b9210', 'user.svg', 1, NULL, NULL, 0, 2018, ''),
(2, 'Federica', 'Albanese', '2@qq.com', 'pwd2', 'user.svg', 0, NULL, NULL, 0, 2018, ''),
(3, 'Lauriano', 'Andruccioli', '3@qq.com', 'pwd3', 'user.svg', 0, NULL, NULL, 0, 0, ''),
(4, 'Armaroli', 'Legarre', '41@qq.com', 'pwd4', 'user.svg', 0, NULL, NULL, 0, 0, ''),
(6, 'wenjieÃ ', 'liu', 'mikuclub@qq.com', 'c4ca4238a0b923820dcc509a6f75849b', '4d95e932-67a2-4805-a358-cd58ecb94ba3.jpg', 1, NULL, NULL, 1, 1535744316063, '3EFB3AE79A32567085692C336F704145'),
(19, 'luigi', 'luca', 'hexie2109@gmail.com', '475CC0CFF8A97714BE3B65DE8715FECE', 'ddfbbf05-224d-4639-afbf-5cd493e87d0b.jpg', 0, NULL, NULL, 0, 1535653914985, 'A80C464CAC401A72E3E43AE1A5DBAA1E'),
(20, 'Aldo', 'Moro', 'snaffza@gmail.com', '495B5C53D4174F1C0D9DBB5F246D9C7D', 'f8f366b7-2e63-4e5b-adec-c5daf15aebf4.jpg', 1, NULL, NULL, 1, 1535721976663, 'E669CA67A74991EF6BA897DD5ED987B0'),
(22, 'Matteo', 'Longato', 'astrangecanal97@gmail.com', 'D62B7FD1D150B9F19A1C260E52E11BB2', 'user.svg', 1, NULL, NULL, 1, NULL, NULL),
(23, 'Enrico', 'Soprana', 'esoprana@gmail.com', '4D72F8721DC6E2BE0E40EFD8FAF258A7', 'user-astronaut.svg', 0, 'd00f5377-a409-484e-be03-10a9d95e600f', NULL, 1, NULL, NULL),
(24, 'Lauro', 'barruga', '1256883490@qq.com', '475CC0CFF8A97714BE3B65DE8715FECE', 'user-secret.svg', 0, 'd098bcc3-585d-4bbe-ae42-1ce813232698', NULL, 1, NULL, NULL),
(25, 'Enrico', 'Soprana2', 'enrico.soprana@studenti.unitn.it', '37BAAA7A9466B47B25FFAB01C9297790', 'user.svg', 0, 'f9f39361-e719-41b5-9250-77d87a776561', NULL, 1, NULL, NULL),
(26, 'A', 'B', 'registrazioneprogettowebprog@gmail.com', '27CB89A007AE0A1536F4CA4785E15E6F', 'user-ninja.svg', 0, '7425b25c-32e8-4629-abcd-1f62313b1453', NULL, 1, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categorylist`
--
ALTER TABLE `CategoryList`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categoryproduct`
--
ALTER TABLE `CategoryProduct`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `comment`
--
ALTER TABLE `Comment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_Comment_List1_idx` (`listId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `list`
--
ALTER TABLE `List`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_List_CategoryList1_idx` (`categoryList`),
  ADD KEY `fk_List_User1_idx` (`ownerId`);

--
-- Indexes for table `log`
--
ALTER TABLE `Log`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_Log_Product1_idx` (`productId`),
  ADD KEY `fk_Log_User1_idx` (`userId`);

--
-- Indexes for table `permission`
--
ALTER TABLE `Permission`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_Permissi_User1_idx` (`userId`),
  ADD KEY `listId` (`listId`);

--
-- Indexes for table `product`
--
ALTER TABLE `Product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_Product_CategoryProduct1_idx` (`categoryProductId`),
  ADD KEY `fk_Product_List1_idx` (`privateListId`);
ALTER TABLE `Product` ADD FULLTEXT KEY `ft_index` (`name`,`description`) WITH PARSER ngram;

--
-- Indexes for table `productinlist`
--
ALTER TABLE `ProductInList`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_ProductInList_List1_idx` (`listId`),
  ADD KEY `productId` (`productId`);

--
-- Indexes for table `user`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `categorylist`
--
ALTER TABLE `CategoryList`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用表AUTO_INCREMENT `categoryproduct`
--
ALTER TABLE `CategoryProduct`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- 使用表AUTO_INCREMENT `comment`
--
ALTER TABLE `Comment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- 使用表AUTO_INCREMENT `list`
--
ALTER TABLE `List`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- 使用表AUTO_INCREMENT `log`
--
ALTER TABLE `Log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- 使用表AUTO_INCREMENT `permission`
--
ALTER TABLE `Permission`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- 使用表AUTO_INCREMENT `product`
--
ALTER TABLE `Product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `productinlist`
--
ALTER TABLE `ProductInList`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- 限制导出的表
--

--
-- 限制表 `comment`
--
ALTER TABLE `Comment`
  ADD CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`listId`) REFERENCES `List` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `list`
--
ALTER TABLE `List`
  ADD CONSTRAINT `fk_List_CategoryList1` FOREIGN KEY (`categoryList`) REFERENCES `CategoryList` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `list_ibfk_1` FOREIGN KEY (`ownerId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `log`
--
ALTER TABLE `Log`
  ADD CONSTRAINT `log_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `Product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `log_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `permission`
--
ALTER TABLE `Permission`
  ADD CONSTRAINT `permission_ibfk_1` FOREIGN KEY (`listId`) REFERENCES `List` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `permission_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `productinlist`
--
ALTER TABLE `ProductInList`
  ADD CONSTRAINT `productinlist_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `Product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `productinlist_ibfk_2` FOREIGN KEY (`listId`) REFERENCES `List` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
