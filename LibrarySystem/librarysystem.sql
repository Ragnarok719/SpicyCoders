-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jun 06, 2014 at 08:20 AM
-- Server version: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `librarysystem`
--

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE IF NOT EXISTS `author` (
  `name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE IF NOT EXISTS `book` (
  `isbn` bigint(20) NOT NULL DEFAULT '0',
  `title` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `currentQuantity` int(11) DEFAULT NULL,
  `totalQuantity` int(11) DEFAULT NULL,
  `publisherYear` int(11) DEFAULT NULL,
  `idNumber` int(11) NOT NULL,
  `typeName` varchar(20) NOT NULL,
  PRIMARY KEY (`isbn`),
  KEY `idNumber` (`idNumber`),
  KEY `typeName` (`typeName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `booktype`
--

CREATE TABLE IF NOT EXISTS `booktype` (
  `typeName` varchar(20) NOT NULL DEFAULT '',
  `maxReservation` int(11) DEFAULT NULL,
  `overdueFee` int(11) DEFAULT NULL,
  PRIMARY KEY (`typeName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `name` varchar(20) DEFAULT NULL,
  `idNumber` int(11) NOT NULL DEFAULT '0',
  `superCategoryId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idNumber`),
  KEY `superCategoryId` (`superCategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `checkout`
--

CREATE TABLE IF NOT EXISTS `checkout` (
  `isbn` bigint(20) NOT NULL DEFAULT '0',
  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `cardNumber` int(11) NOT NULL DEFAULT '0',
  `idNumber` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`isbn`,`start`,`end`,`cardNumber`,`idNumber`),
  KEY `cardNumber` (`cardNumber`),
  KEY `idNumber` (`idNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hasauthor`
--

CREATE TABLE IF NOT EXISTS `hasauthor` (
  `isbn` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`isbn`,`name`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `haspublisher`
--

CREATE TABLE IF NOT EXISTS `haspublisher` (
  `isbn` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`isbn`,`name`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hassearchgenre`
--

CREATE TABLE IF NOT EXISTS `hassearchgenre` (
  `isbn` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`isbn`,`name`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `librarian`
--

CREATE TABLE IF NOT EXISTS `librarian` (
  `idNumber` int(11) NOT NULL DEFAULT '0',
  `name` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `patron`
--

CREATE TABLE IF NOT EXISTS `patron` (
  `cardNumber` int(11) NOT NULL DEFAULT '0',
  `name` varchar(20) DEFAULT NULL,
  `phone` int(11) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `unpaidFees` int(11) DEFAULT NULL,
  PRIMARY KEY (`cardNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `publisher`
--

CREATE TABLE IF NOT EXISTS `publisher` (
  `name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `returnin`
--

CREATE TABLE IF NOT EXISTS `returnin` (
  `isbn` bigint(20) NOT NULL DEFAULT '0',
  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `cardNumber` int(11) NOT NULL DEFAULT '0',
  `returned` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `checkoutId` int(11) NOT NULL DEFAULT '0',
  `returnId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`isbn`,`start`,`end`,`cardNumber`,`checkoutId`,`returnId`),
  KEY `cardNumber` (`cardNumber`),
  KEY `checkoutId` (`checkoutId`),
  KEY `returnId` (`returnId`),
  KEY `start` (`start`,`end`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `searchgenre`
--

CREATE TABLE IF NOT EXISTS `searchgenre` (
  `name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `book_ibfk_2` FOREIGN KEY (`typeName`) REFERENCES `booktype` (`typeName`) ON UPDATE CASCADE,
  ADD CONSTRAINT `book_ibfk_1` FOREIGN KEY (`idNumber`) REFERENCES `category` (`idNumber`) ON UPDATE CASCADE;

--
-- Constraints for table `category`
--
ALTER TABLE `category`
  ADD CONSTRAINT `category_ibfk_1` FOREIGN KEY (`superCategoryId`) REFERENCES `category` (`idNumber`) ON UPDATE CASCADE;

--
-- Constraints for table `checkout`
--
ALTER TABLE `checkout`
  ADD CONSTRAINT `checkout_ibfk_3` FOREIGN KEY (`idNumber`) REFERENCES `librarian` (`idNumber`) ON UPDATE CASCADE,
  ADD CONSTRAINT `checkout_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`) ON UPDATE CASCADE,
  ADD CONSTRAINT `checkout_ibfk_2` FOREIGN KEY (`cardNumber`) REFERENCES `patron` (`cardNumber`) ON UPDATE CASCADE;

--
-- Constraints for table `hasauthor`
--
ALTER TABLE `hasauthor`
  ADD CONSTRAINT `hasauthor_ibfk_2` FOREIGN KEY (`name`) REFERENCES `author` (`name`) ON UPDATE CASCADE,
  ADD CONSTRAINT `hasauthor_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`) ON UPDATE CASCADE;

--
-- Constraints for table `haspublisher`
--
ALTER TABLE `haspublisher`
  ADD CONSTRAINT `haspublisher_ibfk_2` FOREIGN KEY (`name`) REFERENCES `publisher` (`name`) ON UPDATE CASCADE,
  ADD CONSTRAINT `haspublisher_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`) ON UPDATE CASCADE;

--
-- Constraints for table `hassearchgenre`
--
ALTER TABLE `hassearchgenre`
  ADD CONSTRAINT `hassearchgenre_ibfk_2` FOREIGN KEY (`name`) REFERENCES `searchgenre` (`name`) ON UPDATE CASCADE,
  ADD CONSTRAINT `hassearchgenre_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`) ON UPDATE CASCADE;

--
-- Constraints for table `returnin`
--
ALTER TABLE `returnin`
  ADD CONSTRAINT `returnin_ibfk_4` FOREIGN KEY (`returnId`) REFERENCES `librarian` (`idNumber`) ON UPDATE CASCADE,
  ADD CONSTRAINT `returnin_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`) ON UPDATE CASCADE,
  ADD CONSTRAINT `returnin_ibfk_2` FOREIGN KEY (`cardNumber`) REFERENCES `patron` (`cardNumber`) ON UPDATE CASCADE,
  ADD CONSTRAINT `returnin_ibfk_3` FOREIGN KEY (`checkoutId`) REFERENCES `librarian` (`idNumber`) ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
