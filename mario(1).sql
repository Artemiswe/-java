/*
 Navicat Premium Data Transfer

 Source Server         : 3306
 Source Server Type    : MySQL
 Source Server Version : 50728 (5.7.28)
 Source Host           : localhost:3306
 Source Schema         : mario

 Target Server Type    : MySQL
 Target Server Version : 50728 (5.7.28)
 File Encoding         : 65001

 Date: 09/12/2024 17:37:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for game_records
-- ----------------------------
DROP TABLE IF EXISTS `game_records`;
CREATE TABLE `game_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `score` int(11) DEFAULT '0',
  `blood` int(11) DEFAULT '3',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `level` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of game_records
-- ----------------------------
BEGIN;
INSERT INTO `game_records` (`id`, `username`, `score`, `blood`, `created_at`, `level`) VALUES (1, '2', 0, 3, '2024-12-08 21:48:54', 1);
COMMIT;

-- ----------------------------
-- Table structure for game_results
-- ----------------------------
DROP TABLE IF EXISTS `game_results`;
CREATE TABLE `game_results` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `username` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `player1_score` int(11) NOT NULL,
  `player1_blood` int(11) NOT NULL,
  `player2_score` int(11) NOT NULL,
  `player2_blood` int(11) NOT NULL,
  `result` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of game_results
-- ----------------------------
BEGIN;
INSERT INTO `game_results` (`id`, `userid`, `username`, `player1_score`, `player1_blood`, `player2_score`, `player2_blood`, `result`, `timestamp`) VALUES (1, 1, 'Player1', 0, 0, 0, 3, 'Mario is dead', '2024-11-30 06:12:31');
INSERT INTO `game_results` (`id`, `userid`, `username`, `player1_score`, `player1_blood`, `player2_score`, `player2_blood`, `result`, `timestamp`) VALUES (2, 3, 'Player1', 2, 2, 5, 1, 'Success', '2024-12-04 10:04:47');
INSERT INTO `game_results` (`id`, `userid`, `username`, `player1_score`, `player1_blood`, `player2_score`, `player2_blood`, `result`, `timestamp`) VALUES (3, 4, 'Player1', 1, 0, 0, 3, 'Mario is dead', '2024-12-09 04:58:02');
INSERT INTO `game_results` (`id`, `userid`, `username`, `player1_score`, `player1_blood`, `player2_score`, `player2_blood`, `result`, `timestamp`) VALUES (4, 4, 'Player1', 3, 3, 11, 3, 'Success', '2024-12-09 05:44:09');
INSERT INTO `game_results` (`id`, `userid`, `username`, `player1_score`, `player1_blood`, `player2_score`, `player2_blood`, `result`, `timestamp`) VALUES (5, 5, 'Player1', 1, 3, 14, 3, 'Success', '2024-12-09 05:48:48');
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `salt` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`id`, `username`, `password_hash`, `created_at`, `salt`) VALUES (1, 'zhangsan', 'V+hrLaAjA+Blsg1CoPq42WSfPZyqRWgsXZGC12g3dXs=', '2024-11-30 04:53:53', 'P8W9rFj6i19tV8pDhSASPw==');
INSERT INTO `users` (`id`, `username`, `password_hash`, `created_at`, `salt`) VALUES (2, 'lisi', 'fYa03ObumsxUGCLohSXgyenSSDUSlFa94BnWaA5CC4k=', '2024-11-30 05:37:13', 'TKB8Z3E98JyKYgr3AqwO4A==');
INSERT INTO `users` (`id`, `username`, `password_hash`, `created_at`, `salt`) VALUES (3, '11', 'NB2HjdX0FzMIv6gk3mali6WImYlo6yq0ZEzkqoaKenQ=', '2024-12-04 10:01:18', '3zttD153mjET2s2id/PKVw==');
INSERT INTO `users` (`id`, `username`, `password_hash`, `created_at`, `salt`) VALUES (4, '1', 'ILtc4IkkxV7Ar8xoi2TuQz08uat6rMIod0KrMVw9l20=', '2024-12-09 04:52:19', 'm1lP6kRvvtwBZ8Q0cyBseA==');
INSERT INTO `users` (`id`, `username`, `password_hash`, `created_at`, `salt`) VALUES (5, '2', 'FHrAPRfVJRGHjUR6mI7nRN6BrV90pKC1eVQWrlcsy1o=', '2024-12-09 05:46:18', 'TxwZCtjcincHn4yMlLlkhA==');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
