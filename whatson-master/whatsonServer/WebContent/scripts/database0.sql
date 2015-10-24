-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 25, 2014 at 02:59 PM
-- Server version: 5.5.35
-- PHP Version: 5.3.10-1ubuntu3.11

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `whatson`
--

-- --------------------------------------------------------

--
-- Table structure for table `emotions`
--

CREATE TABLE IF NOT EXISTS `emotions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emotion` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=26 ;

-- --------------------------------------------------------

--
-- Table structure for table `externe_tags`
--

CREATE TABLE IF NOT EXISTS `externe_tags` (
  `id_external_content` int(11) NOT NULL,
  `id_emotion` int(11) NOT NULL,
  `intensity` float NOT NULL,
  KEY `id_external_content` (`id_external_content`,`id_emotion`),
  KEY `id_emotion` (`id_emotion`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `externe_text_content`
--

CREATE TABLE IF NOT EXISTS `externe_text_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `felt_emotions`
--

CREATE TABLE IF NOT EXISTS `felt_emotions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `notation`
--

CREATE TABLE IF NOT EXISTS `notation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_felt_emotion` int(11) NOT NULL,
  `id_external_content` int(11) NOT NULL,
  `note` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_felt_emotion` (`id_felt_emotion`,`id_external_content`),
  KEY `id_external_content` (`id_external_content`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `photos`
--

CREATE TABLE IF NOT EXISTS `photos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=25 ;

-- --------------------------------------------------------

--
-- Table structure for table `photo_tags`
--

CREATE TABLE IF NOT EXISTS `photo_tags` (
  `id_photo` int(11) NOT NULL,
  `id_emotion` int(11) NOT NULL,
  `intensity` float NOT NULL,
  KEY `id_photo` (`id_photo`),
  KEY `id_emotion` (`id_emotion`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`) VALUES
(1, 'Alice', 'alicecaron94@gmail.com', 'alice'),
(2, 'Jeremy', 'jeremy.sayag@telecom-paristech.fr', 'jeremy'),
(3, 'Minh', 'minh.thao_chan@telecom-sudparis.eu', 'minh'),
(4, 'Mehdi', 'mehdi.zougmid@telecom-em.eu', 'mehdi'),
(5, 'Claire', 'claire_mucchielli@hotmail.com', 'claire'),
(6, 'Dengke', 'dengke.li@telecom-bretagne.eu', 'dengke');

-- --------------------------------------------------------

--
-- Table structure for table `users_historics`
--

CREATE TABLE IF NOT EXISTS `users_historics` (
  `id_felt_emotion` int(11) NOT NULL,
  `id_emotion` int(11) NOT NULL,
  `intensity` int(11) NOT NULL,
  KEY `id_felt_emotion` (`id_felt_emotion`),
  KEY `id_emotion` (`id_emotion`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `externe_tags`
--
ALTER TABLE `externe_tags`
  ADD CONSTRAINT `externe_tags_ibfk_2` FOREIGN KEY (`id_emotion`) REFERENCES `emotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `externe_tags_ibfk_1` FOREIGN KEY (`id_external_content`) REFERENCES `externe_text_content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `felt_emotions`
--
ALTER TABLE `felt_emotions`
  ADD CONSTRAINT `felt_emotions_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `notation`
--
ALTER TABLE `notation`
  ADD CONSTRAINT `notation_ibfk_2` FOREIGN KEY (`id_external_content`) REFERENCES `externe_text_content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `notation_ibfk_1` FOREIGN KEY (`id_felt_emotion`) REFERENCES `emotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photo_tags`
--
ALTER TABLE `photo_tags`
  ADD CONSTRAINT `photo_tags_ibfk_1` FOREIGN KEY (`id_photo`) REFERENCES `photos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `photo_tags_ibfk_2` FOREIGN KEY (`id_emotion`) REFERENCES `emotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `users_historics`
--
ALTER TABLE `users_historics`
  ADD CONSTRAINT `users_historics_ibfk_1` FOREIGN KEY (`id_felt_emotion`) REFERENCES `felt_emotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `users_historics_ibfk_2` FOREIGN KEY (`id_emotion`) REFERENCES `emotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
