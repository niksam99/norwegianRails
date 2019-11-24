-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 24, 2019 at 08:54 AM
-- Server version: 5.7.28-0ubuntu0.16.04.2
-- PHP Version: 7.0.33-0ubuntu0.16.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rails`
--

-- --------------------------------------------------------

--
-- Table structure for table `ticket`
--

CREATE TABLE `ticket` (
  `id` int(11) NOT NULL,
  `unique_id` varchar(50) NOT NULL,
  `_from` varchar(50) NOT NULL,
  `_to` varchar(50) NOT NULL,
  `time_from` varchar(50) NOT NULL,
  `time_to` varchar(50) NOT NULL,
  `travel_time` varchar(50) NOT NULL,
  `track` varchar(50) NOT NULL,
  `price` varchar(50) NOT NULL,
  `_date` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ticket`
--

INSERT INTO `ticket` (`id`, `unique_id`, `_from`, `_to`, `time_from`, `time_to`, `travel_time`, `track`, `price`, `_date`) VALUES
(53, '5dd9c93b5c2d66.45450728', 'Oslo S', 'Oslo Lufthavn', '1:20', '1:40', '20', 'Track 1', '70', '24-11-2019'),
(55, '5dd9c93b5c2d66.45450728', 'Oslo S', 'Oslo Lufthavn', '2:20', '2:40', '20', 'Track 1', '70', '24-11-2019'),
(56, '5dd9c93b5c2d66.45450728', 'Oslo S', 'Oslo Lufthavn', '10:20', '10:40', '20', 'Track 1', '70', '24-11-2019');

-- --------------------------------------------------------

--
-- Table structure for table `train`
--

CREATE TABLE `train` (
  `id` int(11) NOT NULL,
  `place` varchar(100) NOT NULL,
  `track_1` varchar(100) NOT NULL,
  `track_2` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `train`
--

INSERT INTO `train` (`id`, `place`, `track_1`, `track_2`) VALUES
(1, 'Nationaltheatret', '10', '60'),
(2, 'Oslo S', '20', '50'),
(3, 'Lillestrom', '30', '40'),
(4, 'Oslo Lufthavn', '40', '30'),
(5, 'Eidsvoll', '50', '20'),
(6, 'Eidsvoll Verk', '60', '10');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `unique_id` varchar(23) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `updated_at`) VALUES
(1, '5dc21fac2efec0.40540270', 'nikhil', 'niksamatya@gmail.com', 'tHorqazT7ImdfiFTmpLDYkOmKfNmNjU1ZDM5ZTQ1', 'f655d39e45', '2019-11-06 01:19:40', NULL),
(2, '5dcab469655da9.33771224', 'suraj', 'suraj@gmail.com', 'fOdicHpuGzxYkjs7Xmbqdy/rS5s4NWQ4YjQ2MGM0', '85d8b460c4', '2019-11-12 13:32:25', NULL),
(8, '5dcd3f118261c4.62611733', 'test', 'test@gmail.com', 'DFdVbP853BAU7SWYnzwtsKwf9c85YWU1ZTE1Njk4', '9ae5e15698', '2019-11-14 11:48:33', NULL),
(14, '5dd9c93b5c2d66.45450728', 'nikhil', 'nikhil@gmail.com', '2SEFJVvmEyMrm/Wdhy6PdFn4EjU5NjFmY2Y2OTkz', '961fcf6993', '2019-11-24 00:05:15', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `wallet`
--

CREATE TABLE `wallet` (
  `id` int(11) NOT NULL,
  `unique_id` varchar(23) NOT NULL,
  `wallet_amount` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`id`, `unique_id`, `wallet_amount`) VALUES
(10, '5dc21fac2efec0.40540270', 1800),
(11, '5dcab469655da9.33771224', 1215),
(12, '5dcd3f118261c4.62611733', 0),
(15, '5dd9c3bb7903d3.29707761', 0),
(16, '5dd9c4324de9a3.67637747', 0),
(17, '5dd9c520a54d25.92733841', 0),
(18, '5dd9c5bdf30d54.47706577', 0),
(19, '5dd9c6123a5eb8.09694144', 70),
(20, '5dd9c93b5c2d66.45450728', 70);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ticket`
--
ALTER TABLE `ticket`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `train`
--
ALTER TABLE `train`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_id` (`unique_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `wallet`
--
ALTER TABLE `wallet`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_id` (`unique_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ticket`
--
ALTER TABLE `ticket`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;
--
-- AUTO_INCREMENT for table `train`
--
ALTER TABLE `train`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT for table `wallet`
--
ALTER TABLE `wallet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
