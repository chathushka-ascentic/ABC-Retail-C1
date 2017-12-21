-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2017 at 08:59 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `abc-fashions`
--

-- --------------------------------------------------------

--
-- Table structure for table `branch`
--

CREATE TABLE IF NOT EXISTS `branch` (
  `branchId` int(10) NOT NULL AUTO_INCREMENT,
  `branchName` varchar(30) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `address` varchar(40) NOT NULL,
  `contactNo` varchar(10) NOT NULL,
  PRIMARY KEY (`branchId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `branch`
--

INSERT INTO `branch` (`branchId`, `branchName`, `latitude`, `longitude`, `address`, `contactNo`) VALUES
(1, 'Nugegoda', 6.866104, 79.892577, 'No.20, High Level Road, Nugegoda', '0112435545'),
(2, 'Battaramulla', 6.882441, 79.940086, 'No.20 C, Battaramulla', '0112344454'),
(3, 'Colombo-3', 6.899182, 79.854084, 'No. 6, Alfread House Garden', '0112323343'),
(4, 'Panadura', 6.712329299999999, 79.90755190000004, 'Bekkegama, Panadura', '0114567787'),
(5, 'Maharagama', 6.8522148, 79.92486689999998, 'No.60, High Level Road', '0112327789'),
(6, 'Galle', 6.0535185, 80.22097729999996, 'Galle', '0112435565'),
(7, 'Matara', 5.95492, 80.55495610000003, 'No. 30 A, Deniyaya, Matara', '0119566676');

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `categoryId` int(10) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(30) NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`categoryId`, `categoryName`) VALUES
(1, 'Skirts'),
(2, 'Dresses'),
(3, 'Women Jeans'),
(4, 'Women Shorts');

-- --------------------------------------------------------

--
-- Table structure for table `offer`
--

CREATE TABLE IF NOT EXISTS `offer` (
  `offerId` int(10) NOT NULL AUTO_INCREMENT,
  `stockId` int(10) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  `discountPerc` float NOT NULL,
  `discountPrice` float NOT NULL,
  `desc` varchar(60) NOT NULL,
  `desc1` varchar(50) NOT NULL,
  `branchId` int(10) NOT NULL,
  PRIMARY KEY (`offerId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `offer`
--

INSERT INTO `offer` (`offerId`, `stockId`, `startDate`, `endDate`, `discountPerc`, `discountPrice`, `desc`, `desc1`, `branchId`) VALUES
(1, 1, '2017-12-17', '2017-12-31', 0.05, 0, '5% off on all women''s dresses!!!', 'Offer valid till 31st of December', 1),
(2, 4, '2017-12-25', '2017-12-31', 0.1, 0, '10% off on floral dresses!!!', 'Offer valid till 31st of December', 1),
(4, 1, '2017-12-17', '2017-12-31', 0.05, 0, '5% off on all women''s dresses!!!', 'Offer valid till 31st of December', 2),
(5, 4, '2017-12-25', '2017-12-31', 0.1, 0, '10% off on floral dresses!!!', 'Offer valid till 31st of December', 2),
(6, 7, '2017-12-25', '2017-12-28', 0.4, 0, '40% off on midi dresses!!!', 'Offer valid till 28th of December', 2),
(7, 7, '2017-12-01', '2017-12-28', 0.4, 0, '40% off on midi dresses!!!', 'Offer valid till 28th of December', 3);

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE IF NOT EXISTS `order` (
  `orderId` int(10) NOT NULL AUTO_INCREMENT,
  `userId` int(10) NOT NULL,
  `orderDate` date NOT NULL,
  `orderTotal` float NOT NULL,
  `payId` int(10) NOT NULL,
  `branchId` int(10) NOT NULL,
  PRIMARY KEY (`orderId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`orderId`, `userId`, `orderDate`, `orderTotal`, `payId`, `branchId`) VALUES
(19, 1, '2017-12-21', 118, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `orderdetail`
--

CREATE TABLE IF NOT EXISTS `orderdetail` (
  `detailId` int(10) NOT NULL AUTO_INCREMENT,
  `orderId` int(10) NOT NULL,
  `productId` int(10) NOT NULL,
  `stockId` int(10) NOT NULL,
  `qty` float NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`detailId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `orderdetail`
--

INSERT INTO `orderdetail` (`detailId`, `orderId`, `productId`, `stockId`, `qty`, `price`) VALUES
(15, 19, 1, 2, 1, 10),
(16, 19, 3, 7, 3, 30),
(17, 19, 4, 10, 1, 18);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE IF NOT EXISTS `payment` (
  `payId` int(10) NOT NULL AUTO_INCREMENT,
  `methodName` varchar(10) NOT NULL,
  PRIMARY KEY (`payId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`payId`, `methodName`) VALUES
(1, 'Cash'),
(2, 'Card');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE IF NOT EXISTS `product` (
  `productId` int(10) NOT NULL AUTO_INCREMENT,
  `productName` varchar(100) NOT NULL,
  `description` varchar(200) NOT NULL,
  `qrCode` varchar(30) NOT NULL,
  `price` float NOT NULL DEFAULT '0',
  `categoryId` int(10) NOT NULL,
  `imageUrl` varchar(300) NOT NULL,
  PRIMARY KEY (`productId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`productId`, `productName`, `description`, `qrCode`, `price`, `categoryId`, `imageUrl`) VALUES
(1, 'ASOS Mini Cross Front Origami Dress', 'Color - Red\r\nV - Neck', '1111', 10, 2, 'https://cdnb.lystit.com/photos/2011/10/11/asos-collection-red-asos-petite-exclusive-origami-pleated-dress-product-1-2186184-841596785.jpeg'),
(2, 'PrettyLittleThing Floral Skater Dress\r\n', 'COLOUR: Black\r\nFloral print', '2222', 28, 2, 'http://images.asos-media.com/products/prettylittlething-floral-skater-dress/8909231-1-black?$XXL$&wid=513&fit=constrain'),
(3, 'Midi Tea Dress With Frill Cuff', 'COLOUR: Red\r\nV-neck\r\n', '3333', 30, 2, 'https://i.pinimg.com/736x/8c/9d/36/8c9d36ba182c96b1c4e27e24bc3080bb--bardot-midi-dress-pencil-dresses.jpg'),
(4, 'ASOS Tailored A-Line Mini Skirt', 'COLOUR: Black', '4444', 18, 1, 'http://images.asos-media.com/products/asos-tailored-a-line-mini-skirt/7270793-1-black?$XXL$&wid=513&fit=constrain'),
(5, 'ASOS Tailored Mini Pleated Skirt', 'COLOUR: Pink', '5555', 22, 1, 'https://images.prod.meredith.com/product/83a42a6082d290b45adc8849d758b67a/1507880779972/l/asos-mini-pleated-skirt-pink'),
(6, 'High Waist Skinny Jeans In Optic White', 'All-white', '6666', 25, 3, 'http://images.asos-media.com/products/asos-ridley-high-waist-skinny-jeans-in-optic-white/7947734-1-opticwhite?$XXL$&wid=513&fit=constrain'),
(7, 'High Waist Skinny Jeans in Clean Black', 'COLOUR: Black', '7777', 25, 3, 'http://images.asos-media.com/products/asos-ridley-high-waist-skinny-jeans-in-clean-black/7753468-2?$XXL$&wid=513&fit=constrain');

-- --------------------------------------------------------

--
-- Table structure for table `size`
--

CREATE TABLE IF NOT EXISTS `size` (
  `sizeId` int(10) NOT NULL AUTO_INCREMENT,
  `sizeName` varchar(10) NOT NULL,
  PRIMARY KEY (`sizeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `size`
--

INSERT INTO `size` (`sizeId`, `sizeName`) VALUES
(1, 'S'),
(2, 'M'),
(3, 'L'),
(4, 'XL'),
(5, 'XXL'),
(6, 'UK 8'),
(7, 'UK 10'),
(8, 'UK 12');

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE IF NOT EXISTS `stock` (
  `stockId` int(10) NOT NULL AUTO_INCREMENT,
  `branchId` int(10) NOT NULL,
  `productId` int(10) NOT NULL,
  `sizeId` int(10) NOT NULL,
  `count` int(10) NOT NULL,
  PRIMARY KEY (`stockId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`stockId`, `branchId`, `productId`, `sizeId`, `count`) VALUES
(1, 1, 1, 1, 1000),
(2, 1, 1, 2, 1999),
(3, 1, 2, 2, 2000),
(4, 1, 2, 1, 3000),
(5, 1, 5, 1, 1500),
(6, 1, 5, 3, 2500),
(7, 1, 3, 1, 1497),
(8, 1, 4, 1, 1500),
(9, 1, 3, 2, 1500),
(10, 1, 4, 2, 1499),
(11, 2, 1, 1, 1000),
(12, 2, 1, 2, 2000),
(13, 2, 2, 2, 2000),
(14, 2, 2, 1, 3000),
(15, 2, 5, 1, 1500),
(16, 2, 5, 3, 2500),
(17, 2, 3, 1, 1500),
(18, 2, 4, 1, 1500),
(19, 2, 3, 2, 1500),
(20, 2, 4, 2, 1500);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `contactNo1` text NOT NULL,
  `contactNo2` text NOT NULL,
  `address` varchar(60) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(10) NOT NULL,
  `fullname` varchar(60) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `contactNo1`, `contactNo2`, `address`, `email`, `password`, `fullname`) VALUES
(1, '0777969244', '', 'No.20B, Nugegoda', 'aparnahansanee@gmail.com', '12345', 'Aparna Prasad');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
