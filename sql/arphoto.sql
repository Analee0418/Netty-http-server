/*
SQLyog v10.2 
MySQL - 5.7.18 : Database - arphoto
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `albumunit` */

CREATE TABLE `albumunit` (
  `id` bigint(200) DEFAULT NULL,
  `picId` bigint(200) DEFAULT NULL COMMENT '照片',
  `videoId` varchar(200) DEFAULT NULL COMMENT '视频id，例如:1,2,3'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `albumunit` */

insert  into `albumunit`(`id`,`picId`,`videoId`) values (10000001,10000001,'10000001'),(10000002,10000002,'10000002'),(10000003,10000003,'10000003'),(10000004,10000004,'10000004'),(10000005,10000005,'10000005'),(10000006,10000006,'10000006');

/*Table structure for table `c3p0testtable` */

CREATE TABLE `c3p0testtable` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c3p0testtable` */

/*Table structure for table `pic` */

CREATE TABLE `pic` (
  `id` bigint(200) DEFAULT NULL,
  `fileName` varchar(200) DEFAULT NULL,
  `downloadPath` varchar(500) DEFAULT NULL COMMENT '下载地址',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pic` */

insert  into `pic`(`id`,`fileName`,`downloadPath`,`createTime`) values (10000001,'1000001_1000000146536574.jpg','http://47.93.100.23/1000001_1000000146536574.jpg','2017-07-14 11:44:40'),(10000002,'1000001_1000000234704701.jpg','http://47.93.100.23/1000001_1000000234704701.jpg','2017-07-14 12:47:45'),(10000003,'1000001_1000000323201252.jpg','http://47.93.100.23/1000001_1000000323201252.jpg','2017-07-14 18:14:02'),(10000004,'1000001_1000000423377725.jpg','http://47.93.100.23/1000001_1000000423377725.jpg','2017-07-18 17:38:41'),(10000005,'1000001_1000000511750601.jpg','http://47.93.100.23/1000001_1000000511750601.jpg','2017-07-18 17:55:03'),(10000006,'1000001_1000000607515335.jpg','http://47.93.100.23/1000001_1000000607515335.jpg','2017-07-18 17:56:37');

/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` bigint(64) DEFAULT NULL COMMENT '唯一id',
  `userName` varchar(50) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `nikeName` varchar(50) DEFAULT NULL COMMENT '昵称',
  `sex` bit(1) DEFAULT NULL COMMENT '性别:0女,1男',
  `homePicUrl` varchar(500) DEFAULT NULL COMMENT '主页背景图',
  `platformId` int(10) DEFAULT NULL COMMENT '平台id',
  `platformName` varchar(10) DEFAULT NULL COMMENT '平台名称，小米，腾讯',
  `createTime` datetime DEFAULT NULL,
  `lastLoginTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`userName`,`password`,`nikeName`,`sex`,`homePicUrl`,`platformId`,`platformName`,`createTime`,`lastLoginTime`) values (1000001,'13651027213','123','游客42775703','\0',NULL,0,NULL,'2017-06-08 10:15:03','2017-07-20 13:10:38');

/*Table structure for table `useralbum` */

CREATE TABLE `useralbum` (
  `id` bigint(200) DEFAULT NULL,
  `userId` bigint(200) DEFAULT NULL,
  `data` text COMMENT '数据 : 例如相册数据的 id1,id2,id3',
  `friendsData` text COMMENT '好友分享过来的相册数据 : 例如相册数据的 id1,id2,id3'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `useralbum` */

insert  into `useralbum`(`id`,`userId`,`data`,`friendsData`) values (10000001,1000001,'10000001,10000002,10000003,10000004,10000005,10000006',NULL);

/*Table structure for table `video` */

CREATE TABLE `video` (
  `id` bigint(200) DEFAULT NULL,
  `fileName` varchar(200) DEFAULT NULL,
  `downloadPath` varchar(500) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=dec8;

/*Data for the table `video` */

insert  into `video`(`id`,`fileName`,`downloadPath`,`createTime`) values (10000001,'1000001_1000000154532536.jpg','http://47.93.100.23/1000001_1000000154532536.jpg','2017-07-14 11:44:40'),(10000002,'1000001_1000000263414242.jpg','http://47.93.100.23/1000001_1000000263414242.jpg','2017-07-14 12:47:45'),(10000003,'1000001_1000000344173271.jpg','http://47.93.100.23/1000001_1000000344173271.jpg','2017-07-14 18:14:02'),(10000004,'1000001_1000000475230750.jpg','http://47.93.100.23/1000001_1000000475230750.jpg','2017-07-18 17:38:41'),(10000005,'1000001_1000000567417774.jpg','http://47.93.100.23/1000001_1000000567417774.jpg','2017-07-18 17:55:03'),(10000006,'1000001_1000000654753073.jpg','http://47.93.100.23/1000001_1000000654753073.jpg','2017-07-18 17:56:37');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
