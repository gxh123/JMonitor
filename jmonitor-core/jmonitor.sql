CREATE DATABASE `jmonitor` ;

USE `jmonitor`;


-- ----------------------------
-- Table structure for hourly_report
-- ----------------------------
DROP TABLE IF EXISTS `hourly_report`;
CREATE TABLE `hourly_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reportType` varchar(20) NOT NULL COMMENT '报表名称',
  `contentFormat` tinyint(4) NOT NULL COMMENT '报表内容格式 默认1',
  `content` longblob NOT NULL COMMENT '二进制报表内容',
  `startTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '报表时间段',
  `createTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '报表创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED COMMENT='用于存放实时报表信息，处理之后的结果';

-- ----------------------------
-- Table structure for monthly_report
-- ----------------------------
DROP TABLE IF EXISTS `monthly_report`;
CREATE TABLE `monthly_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reportType` varchar(20) NOT NULL COMMENT '报表名称',
  `contentFormat` tinyint(4) NOT NULL COMMENT '报表内容格式 默认1',
  `content` longblob NOT NULL COMMENT '二进制报表内容',
  `startTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '报表时间段',
  `createTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '报表创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED COMMENT='用于存放实时报表信息，处理之后的结果';


-- ----------------------------
-- Table structure for weekly_report
-- ----------------------------
DROP TABLE IF EXISTS `weekly_report`;
CREATE TABLE `weekly_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reportType` varchar(20) NOT NULL COMMENT '报表名称',
  `contentFormat` tinyint(4) NOT NULL COMMENT '报表内容格式 默认1',
  `content` longblob NOT NULL COMMENT '二进制报表内容',
  `startTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '报表时间段',
  `createTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '报表创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED COMMENT='用于存放实时报表信息，处理之后的结果';

-- ----------------------------
-- Table structure for daily_report
-- ----------------------------
DROP TABLE IF EXISTS `daily_report`;
CREATE TABLE `daily_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reportType` varchar(20) NOT NULL COMMENT '报表名称',
  `contentFormat` tinyint(4) NOT NULL COMMENT '报表内容格式 默认1',
  `content` longblob NOT NULL COMMENT '二进制报表内容',
  `startTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '报表时间段',
  `createTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '报表创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED COMMENT='用于存放实时报表信息，处理之后的结果';


-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `producer` varchar(20) NOT NULL COMMENT '任务创建者ip',
  `consumer` varchar(20) DEFAULT NULL COMMENT '任务执行者ip',
  `failureCount` tinyint(4) NOT NULL COMMENT '任务失败次数',
  `reportName` varchar(20) NOT NULL COMMENT '报表名称, transaction, problem...',
  `reportPeriod` datetime NOT NULL COMMENT '报表时间',
  `status` tinyint(4) NOT NULL COMMENT '执行状态: 1/todo, 2/done 3/failed',
  `taskType` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0表示小时任务，1表示天任务',
  `creationDate` datetime NOT NULL COMMENT '任务创建时间',
  `startDate` datetime DEFAULT NULL COMMENT '开始时间, 这次执行开始时间',
  `endDate` datetime DEFAULT NULL COMMENT '结束时间, 这次执行结束时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_period_name_type` (`reportPeriod`,`reportName`,`taskType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台任务';

-- ----------------------------
-- Table structure for ips
-- ----------------------------
DROP TABLE IF EXISTS `machines`;
CREATE TABLE `machines` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) NOT NULL COMMENT 'message产生的机器的ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `machines_ip` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用于记录所有产生信息的机器的IP';

