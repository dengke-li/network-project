-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 30, 2014 at 04:35 AM
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

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
  `url` text NOT NULL,
  `author` text NOT NULL,
  `title` text NOT NULL,
  `typeContent` integer NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `externe_text_content`
--

INSERT INTO `externe_text_content` (`id`, `source`, `url`, `author`, `title`, `typeContent`) VALUES
(1, 'Le Monde', 'http://mobile.lemonde.fr/europeennes-2014/article/2014/05/25/les-francais-ont-vote-en-majorite-en-fonction-d-enjeux-europeens_4425697_4350146.html', 'John Doe', 'Les europÃ©ennes',0),
(2, 'Le Monde', 'http://mobile.lemonde.fr/europeennes-2014/article/2014/05/25/les-francais-ont-vote-en-majorite-en-fonction-d-enjeux-europeens_4425697_4350146.html', 'Isaac Newton', 'Les forces naturelles',0),
(3, 'Le Monde', 'http://mobile.lemonde.fr/coupe-du-monde/article/2014/07/09/bresil-2014-pays-bas-argentine-un-duel-de-faux-jumeaux_4454087_1616627.html', 'Isaac Newton', 'Brésil 2014 : Pays-Bas - Argentine, un duel de faux jumeaux',0),
(4, 'Le Monde', 'http://mobile.lemonde.fr/economie/article/2014/07/09/greve-a-la-sncm-les-negociations-pietinent-les-professionnels-corses-manifestent-contre-le-blocage_4453875_3234.html', 'Isaac Newton', 'SNCM : vers une reprise du travail?',0),
(5, 'Spotify', 'Akon_track1.mp3', 'Akon', 'Track 1', 1),
(6, 'Spotify', 'Akon_track2.mp3', 'Akon', 'Track 2', 1),
(7, 'Netflix', '8_miles.mp4', 'Curtis Hanson', '8 Miles',2),
(8, 'Netflix', 'argo.mp4', 'Ben Affleck', 'Argo',2),
(9, 'Netflix', 'avatar.mp4', 'James Cameron', 'Avatar',2),
(10, 'Netflix', 'dikkenek.mp4', 'Olivier Van Hoofstadt', 'Dikkenek',2),
(11, 'Netflix', 'death_at_a_funeral.mp4', 'Frank Oz', 'Death at a funeral',2),
(12, 'Netflix', 'into_the_wild.mp4', 'Sean Penn', 'Into the wild',2),
(13, 'Netflix', 'law_abiding_citizen.mp4', 'F. Gary Gray', 'Law abiding citizen',2),
(14, 'Netflix', 'psychose.mp4', 'A. Hitchcock', 'Psychose',2),
(15, 'Netflix', 'the_artist.mp4', 'Michel Hazanavicius', 'The Artist',2),
(16, 'Netflix', 'the_hangover.mp4', 'Todd Philips', 'The Hangover',2),
(17, 'New York Times', 'http://mobile.nytimes.com/2014/07/10/world/americas/humiliation-and-heartbreak-brazil-processes-the-unthinkable.html', 'Simon Romero', 'Brazilians Grumble and Take Stock After Crushing World Cup Loss',0),
(18, 'New York Times', 'http://mobile.nytimes.com/2014/07/10/world/europe/edward-snowden-asks-russia-to-extend-asylum.html', 'Niel MacFarquhar', 'Snowden Asks Russia to Extend Asylum',0),
(19, 'New York Times', 'http://mobile.nytimes.com/2014/07/10/world/europe/german-police-conduct-searches-in-2nd-possible-spy-case.html', 'Alison Smale', 'New Case of Spying Is Alleged by Germany',0),
(20, 'New York Times', 'http://mobile.nytimes.com/2014/05/15/business/retirementspecial/a-funny-name-a-serious-sport-pickleball-anyone.html', 'Peter T. Kilborn', 'A funny Name, a Serious Sport. Pickleball, Anyone ?',0),
(21, 'New York Times', 'http://mobile.nytimes.com/2012/03/17/nyregion/he-helped-split-a-jackpot-but-his-share-is-still-zero.html', 'James Baron', 'He Helped Split a Jackpot, but His Share Is Still Zero',0),
(22, 'New York Times', 'http://mobile.nytimes.com/2014/07/13/travel/bad-trip-travels-misadventures.html', 'Neil Genzlinger', 'Bad Trip : Travel s Misadventures',0),
(23, 'New York Times', 'http://mobile.nytimes.com/2012/07/08/travel/36-hours-in-helsinki-finland.html', 'Ingrid K. Williams', '36 Hours in Helsinki, Finland',0),
(24, 'New York Times', 'http://mobile.nytimes.com/2012/12/11/technology/huawei-to-open-research-center-in-finland.html', 'Eric Pfanner', 'Huawei to Open Research Center in Finland',0);
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


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

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


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
