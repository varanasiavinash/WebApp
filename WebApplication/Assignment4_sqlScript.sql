SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS NBAD3;
CREATE DATABASE NBAD3;

-- ----------------------------
--  Table structure for `Answer`
-- ----------------------------
DROP TABLE IF EXISTS `Answer`;
CREATE TABLE `Answer` (
  `StudyID` INT NOT NULL,
  `QuestionID` INT NOT NULL,
  `UserName` varchar(40) NOT NULL DEFAULT '',
  `Choice` varchar(40) DEFAULT NULL,
  `DateSubmitted` datetime DEFAULT NULL,
  PRIMARY KEY (`StudyID`,`QuestionID`,`UserName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `Question`
-- ----------------------------
DROP TABLE IF EXISTS `Question`;
CREATE TABLE `Question` (
  `QuestionID` INT NOT NULL AUTO_INCREMENT,
  `StudyID` INT DEFAULT NULL,
  `Question` varchar(50) DEFAULT NULL,
  `AnswerType` varchar(10) DEFAULT NULL,
  `Option1` varchar(40) DEFAULT NULL,
  `Option2` varchar(40) DEFAULT NULL,
  `Option3` varchar(40) DEFAULT NULL,
  `Option4` varchar(40) DEFAULT NULL,
  `Option5` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`QuestionID`),
  KEY `StudyID` (`StudyID`),
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`StudyID`) REFERENCES `Study` (`StudyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `Reported`
-- ----------------------------
DROP TABLE IF EXISTS `Reported`;
CREATE TABLE `Reported` (
  `QuestionID` INT NOT NULL,
  `StudyID` INT NOT NULL,
  `Date` datetime DEFAULT NULL,
  `NumParticipants` int(15) DEFAULT NULL,
  `Status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`StudyID`,`QuestionID`),
  KEY `QuestionID` (`QuestionID`),
  CONSTRAINT `reported_ibfk_1` FOREIGN KEY (`QuestionID`) REFERENCES `Question` (`QuestionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reported_ibfk_2` FOREIGN KEY (`StudyID`) REFERENCES `Study` (`StudyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `Study`
-- ----------------------------
DROP TABLE IF EXISTS `Study`;
CREATE TABLE `Study` (
  `StudyID` INT NOT NULL AUTO_INCREMENT,
  `StudyName` varchar(40) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Username` varchar(50) DEFAULT NULL,
  `DateCreated` datetime DEFAULT NULL,
  `ImageURL` mediumblob DEFAULT NULL,
  `ReqParticipants` int(15) DEFAULT NULL,
  `ActParticipants` int(15) DEFAULT NULL,
  `SStatus` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`StudyID`),
  KEY `Username` (`Username`),
  CONSTRAINT `study_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `User` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `User`
-- ----------------------------
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `Username` varchar(50) NOT NULL DEFAULT '',
  `Password` varchar(50) DEFAULT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `Studies` int(15) DEFAULT NULL,
  `Participation` int(15) DEFAULT NULL,
  `Coins` int(15) DEFAULT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-----------------------------------------
--
-----------------------------------------

ALTER TABLE Reported DROP PRIMARY KEY,ADD COLUMN USERNAME varchar(40) NOT NULL DEFAULT '',
ADD PRIMARY KEY(StudyID,QuestionID,USERNAME);


ALTER TABLE Question ADD COLUMN NOOFANSWERS int(15) DEFAULT NULL;

alter table User add Name varchar(50) default null;


Create Table TempUser(
UName VARCHAR(40),
Email VARCHAR(50),
Password VARCHAR(50),
IssueDate datetime,
Token VARCHAR(50));

use NBAD3;
Create Table ForgotPwdUser(
Email VARCHAR(50),
Token VARCHAR(50));


alter table User add SALT varchar(64) default null;

ALTER TABLE User CHANGE COLUMN Password Password varchar(64) DEFAULT NULL;


