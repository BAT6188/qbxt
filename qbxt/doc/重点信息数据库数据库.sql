/*
SQLyog Ultimate v11.42 (32 bit)
MySQL - 5.5.23 : Database - majorinfo
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`majorinfo` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `majorinfo`;

/*Table structure for table `T_CITY` */

DROP TABLE IF EXISTS `T_CITY`;

CREATE TABLE `T_CITY` (
  `XZQHDM` varchar(255) NOT NULL,
  `CC` varchar(255) DEFAULT NULL,
  `JC` varchar(255) DEFAULT NULL,
  `XZQHMC` varchar(255) DEFAULT NULL,
  `ALPHABET` varchar(255) DEFAULT NULL,
  `PID` varchar(255) DEFAULT NULL,
  `wid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`XZQHDM`),
  KEY `FK94B15E768BC48F41` (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_CITY` */

insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('220400','2','吉林省辽源市','辽源市',NULL,'22','220400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('220500','2','吉林省通化市','通化市',NULL,'22','220500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('220600','2','吉林省白山市','白山市',NULL,'22','220600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('220700','2','吉林省松原市','松原市',NULL,'22','220700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('220800','2','吉林省白城市','白城市',NULL,'22','220800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('222400','2','吉林省延边朝鲜族自治州','延边朝鲜族自治州',NULL,'22','222400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('23','1','黑龙江省','黑龙江省','h','0','230000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230100','2','黑龙江省哈尔滨市','哈尔滨市',NULL,'23','230100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230200','2','黑龙江省齐齐哈尔市','齐齐哈尔市',NULL,'23','230200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230300','2','黑龙江省鸡西市','鸡西市',NULL,'23','230300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230400','2','黑龙江省鹤岗市','鹤岗市',NULL,'23','230400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230500','2','黑龙江省双鸭山市','双鸭山市',NULL,'23','230500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230600','2','黑龙江省大庆市','大庆市',NULL,'23','230600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230700','2','黑龙江省伊春市','伊春市',NULL,'23','230700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230800','2','黑龙江省佳木斯市','佳木斯市',NULL,'23','230800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('230900','2','黑龙江省七台河市','七台河市',NULL,'23','230900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('231000','2','黑龙江省牡丹江市','牡丹江市',NULL,'23','231000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411500','2','河南省信阳市','信阳市',NULL,'41','411500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411600','2','河南省周口市','周口市',NULL,'41','411600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411700','2','河南省驻马店市','驻马店市',NULL,'41','411700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('419000','2','河南省省直辖县级行政区划','省直辖县级行政区划',NULL,'41','419000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('42','1','湖北省','湖北省','h','0','420000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420100','2','湖北省武汉市','武汉市',NULL,'42','420100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430300','2','湖南省湘潭市','湘潭市',NULL,'43','430300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430400','2','湖南省衡阳市','衡阳市',NULL,'43','430400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430500','2','湖南省邵阳市','邵阳市',NULL,'43','430500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430600','2','湖南省岳阳市','岳阳市',NULL,'43','430600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430700','2','湖南省常德市','常德市',NULL,'43','430700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430800','2','湖南省张家界市','张家界市',NULL,'43','430800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430900','2','湖南省益阳市','益阳市',NULL,'43','430900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('431000','2','湖南省郴州市','郴州市',NULL,'43','431000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('431100','2','湖南省永州市','永州市',NULL,'43','431100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('431200','2','湖南省怀化市','怀化市',NULL,'43','431200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('431300','2','湖南省娄底市','娄底市',NULL,'43','431300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('433100','2','湖南省湘西土家族苗族自治州','湘西土家族苗族自治州',NULL,'43','433100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320700','2','江苏省连云港市','连云港市',NULL,'32','320700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320800','2','江苏省淮安市','淮安市',NULL,'32','320800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320900','2','江苏省盐城市','盐城市',NULL,'32','320900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('321000','2','江苏省扬州市','扬州市',NULL,'32','321000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('321100','2','江苏省镇江市','镇江市',NULL,'32','321100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('321200','2','江苏省泰州市','泰州市',NULL,'32','321200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('321300','2','江苏省宿迁市','宿迁市',NULL,'32','321300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('33','1','浙江省','浙江省','z','0','330000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330100','2','浙江省杭州市','杭州市',NULL,'33','330100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330200','2','浙江省宁波市','宁波市',NULL,'33','330200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340400','2','安徽省淮南市','淮南市',NULL,'34','340400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340500','2','安徽省马鞍山市','马鞍山市',NULL,'34','340500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340600','2','安徽省淮北市','淮北市',NULL,'34','340600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340700','2','安徽省铜陵市','铜陵市',NULL,'34','340700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340800','2','安徽省安庆市','安庆市',NULL,'34','340800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341000','2','安徽省黄山市','黄山市',NULL,'34','341000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341100','2','安徽省滁州市','滁州市',NULL,'34','341100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341200','2','安徽省阜阳市','阜阳市',NULL,'34','341200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341300','2','安徽省宿州市','宿州市',NULL,'34','341300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341400','2','安徽省巢湖市','巢湖市',NULL,'34','341400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341500','2','安徽省六安市','六安市',NULL,'34','341500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341600','2','安徽省亳州市','亳州市',NULL,'34','341600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341700','2','安徽省池州市','池州市',NULL,'34','341700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('341800','2','安徽省宣城市','宣城市',NULL,'34','341800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('35','1','福建省','福建省','f','0','350000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350100','2','福建省福州市','福州市',NULL,'35','350100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350200','2','福建省厦门市','厦门市',NULL,'35','350200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350300','2','福建省莆田市','莆田市',NULL,'35','350300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350400','2','福建省三明市','三明市',NULL,'35','350400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350500','2','福建省泉州市','泉州市',NULL,'35','350500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350600','2','福建省漳州市','漳州市',NULL,'35','350600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411100','2','河南省漯河市','漯河市',NULL,'41','411100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411200','2','河南省三门峡市','三门峡市',NULL,'41','411200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411300','2','河南省南阳市','南阳市',NULL,'41','411300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411400','2','河南省商丘市','商丘市',NULL,'41','411400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460200','2','海南省三亚市','三亚市',NULL,'46','460200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('469000','2','海南省省直辖县级行政区划','省直辖县级行政区划',NULL,'46','469000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('50','1','重庆市','重庆市','c','0','500000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500100','2','重庆市','重庆市',NULL,'50','500100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500101','3','重庆市万州区','万州区',NULL,'500100','500101');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500102','3','重庆市涪陵区','涪陵区',NULL,'500100','500102');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500103','3','重庆市渝中区','渝中区',NULL,'500100','500103');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500104','3','重庆市大渡口区','大渡口区',NULL,'500100','500104');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500105','3','重庆市江北区','江北区',NULL,'500100','500105');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500106','3','重庆市沙坪坝区','沙坪坝区',NULL,'500100','500106');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500107','3','重庆市九龙坡区','九龙坡区',NULL,'500100','500107');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500108','3','重庆市南岸区','南岸区',NULL,'500100','500108');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500109','3','重庆市北碚区','北碚区',NULL,'500100','500109');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500110','3','重庆市万盛区','万盛区',NULL,'500100','500110');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500111','3','重庆市双桥区','双桥区',NULL,'500100','500111');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500112','3','重庆市渝北区','渝北区',NULL,'500100','500112');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500113','3','重庆市巴南区','巴南区',NULL,'500100','500113');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500114','3','重庆市黔江区','黔江区',NULL,'500100','500114');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500115','3','重庆市长寿区','长寿区',NULL,'500100','500115');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500116','3','重庆市江津区','江津区',NULL,'500100','500116');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500117','3','重庆市合川区','合川区',NULL,'500100','500117');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500118','3','重庆市永川区','永川区',NULL,'500100','500118');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500119','3','重庆市南川区','南川区',NULL,'500100','500119');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500222','3','重庆市綦江县','綦江县',NULL,'500200','500222');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500223','3','重庆市潼南县','潼南县',NULL,'500200','500223');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500224','3','重庆市铜梁县','铜梁县',NULL,'500200','500224');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500225','3','重庆市大足县','大足县',NULL,'500200','500225');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500226','3','重庆市荣昌县','荣昌县',NULL,'500200','500226');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500227','3','重庆市璧山县','璧山县',NULL,'500200','500227');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500228','3','重庆市梁平县','梁平县',NULL,'500200','500228');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500229','3','重庆市城口县','城口县',NULL,'500200','500229');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500230','3','重庆市丰都县','丰都县',NULL,'500200','500230');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500231','3','重庆市垫江县','垫江县',NULL,'500200','500231');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500232','3','重庆市武隆县','武隆县',NULL,'500200','500232');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500233','3','重庆市忠县','忠县',NULL,'500200','500233');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500234','3','重庆市开县','开县',NULL,'500200','500234');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500235','3','重庆市云阳县','云阳县',NULL,'500200','500235');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500236','3','重庆市奉节县','奉节县',NULL,'500200','500236');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500237','3','重庆市巫山县','巫山县',NULL,'500200','500237');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500238','3','重庆市巫溪县','巫溪县',NULL,'500200','500238');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500240','3','重庆市石柱土家族自治县','石柱土家族自治县',NULL,'500200','500240');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500241','3','重庆市秀山土家族苗族自治县','秀山土家族苗族自治县',NULL,'500200','500241');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500242','3','重庆市酉阳土家族苗族自治县','酉阳土家族苗族自治县',NULL,'500200','500242');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('500243','3','重庆市彭水苗族土家族自治县','彭水苗族土家族自治县',NULL,'500200','500243');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('51','1','四川省','四川省','s','0','510000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510100','2','四川省成都市','成都市',NULL,'51','510100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('654600','2','新疆维吾尔自治区图木舒克市','图木舒克市',NULL,'65','654600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('654500','2','新疆维吾尔自治区阿拉尔市','阿拉尔市',NULL,'65','654500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('654400','2','新疆维吾尔自治区石河子市','石河子市',NULL,'65','654400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510300','2','四川省自贡市','自贡市',NULL,'51','510300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('654700','2','新疆维吾尔自治区五家渠市','五家渠市',NULL,'65','654700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510400','2','四川省攀枝花市','攀枝花市',NULL,'51','510400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510500','2','四川省泸州市','泸州市',NULL,'51','510500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510600','2','四川省德阳市','德阳市',NULL,'51','510600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510700','2','四川省绵阳市','绵阳市',NULL,'51','510700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510800','2','四川省广元市','广元市',NULL,'51','510800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('510900','2','四川省遂宁市','遂宁市',NULL,'51','510900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511000','2','四川省内江市','内江市',NULL,'51','511000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511100','2','四川省乐山市','乐山市',NULL,'51','511100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511300','2','四川省南充市','南充市',NULL,'51','511300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511400','2','四川省眉山市','眉山市',NULL,'51','511400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511500','2','四川省宜宾市','宜宾市',NULL,'51','511500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511600','2','四川省广安市','广安市',NULL,'51','511600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511700','2','四川省达州市','达州市',NULL,'51','511700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('520400','2','贵州省安顺市','安顺市',NULL,'52','520400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('522200','2','贵州省铜仁地区','铜仁地区',NULL,'52','522200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('522300','2','贵州省黔西南布依族苗族自治州','黔西南布依族苗族自治州',NULL,'52','522300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('522400','2','贵州省毕节地区','毕节地区',NULL,'52','522400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('522600','2','贵州省黔东南苗族侗族自治州','黔东南苗族侗族自治州',NULL,'52','522600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('522700','2','贵州省黔南布依族苗族自治州','黔南布依族苗族自治州',NULL,'52','522700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('53','1','云南省','云南省','y','0','530000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530100','2','云南省昆明市','昆明市',NULL,'53','530100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530300','2','云南省曲靖市','曲靖市',NULL,'53','530300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530400','2','云南省玉溪市','玉溪市',NULL,'53','530400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530500','2','云南省保山市','保山市',NULL,'53','530500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530600','2','云南省昭通市','昭通市',NULL,'53','530600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530700','2','云南省丽江市','丽江市',NULL,'53','530700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530800','2','云南省普洱市','普洱市',NULL,'53','530800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('530900','2','云南省临沧市','临沧市',NULL,'53','530900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('532300','2','云南省楚雄彝族自治州','楚雄彝族自治州',NULL,'53','532300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('532500','2','云南省红河哈尼族彝族自治州','红河哈尼族彝族自治州',NULL,'53','532500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511800','2','四川省雅安市','雅安市',NULL,'51','511800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('511900','2','四川省巴中市','巴中市',NULL,'51','511900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('512000','2','四川省资阳市','资阳市',NULL,'51','512000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('513200','2','四川省阿坝藏族羌族自治州','阿坝藏族羌族自治州',NULL,'51','513200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('513300','2','四川省甘孜藏族自治州','甘孜藏族自治州',NULL,'51','513300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360200','2','江西省景德镇市','景德镇市',NULL,'36','360200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360300','2','江西省萍乡市','萍乡市',NULL,'36','360300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360400','2','江西省九江市','九江市',NULL,'36','360400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360500','2','江西省新余市','新余市',NULL,'36','360500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360600','2','江西省鹰潭市','鹰潭市',NULL,'36','360600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360700','2','江西省赣州市','赣州市',NULL,'36','360700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360800','2','江西省吉安市','吉安市',NULL,'36','360800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360900','2','江西省宜春市','宜春市',NULL,'36','360900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('361000','2','江西省抚州市','抚州市',NULL,'36','361000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('361100','2','江西省上饶市','上饶市',NULL,'36','361100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('37','1','山东省','山东省','s','0','370000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370100','2','山东省济南市','济南市',NULL,'37','370100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370200','2','山东省青岛市','青岛市',NULL,'37','370200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('423100','2','湖北省天门市','天门市',NULL,'42','423100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('423200','2','湖北省神农架林区','神农架林区',NULL,'42','423200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460300','2','海南省五指山市','五指山市',NULL,'46','460300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460400','2','海南省琼海市','琼海市',NULL,'46','460400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460800','2','海南省东方市','东方市',NULL,'46','460800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460700','2','海南省万宁市','万宁市',NULL,'46','460700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460600','2','海南省文昌市','文昌市',NULL,'46','460600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460500','2','海南省儋州市','儋州市',NULL,'46','460500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370300','2','山东省淄博市','淄博市',NULL,'37','370300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370400','2','山东省枣庄市','枣庄市',NULL,'37','370400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370500','2','山东省东营市','东营市',NULL,'37','370500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370600','2','山东省烟台市','烟台市',NULL,'37','370600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370700','2','山东省潍坊市','潍坊市',NULL,'37','370700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370800','2','山东省济宁市','济宁市',NULL,'37','370800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('370900','2','山东省泰安市','泰安市',NULL,'37','370900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371000','2','山东省威海市','威海市',NULL,'37','371000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371100','2','山东省日照市','日照市',NULL,'37','371100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371200','2','山东省莱芜市','莱芜市',NULL,'37','371200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371300','2','山东省临沂市','临沂市',NULL,'37','371300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371400','2','山东省德州市','德州市',NULL,'37','371400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371500','2','山东省聊城市','聊城市',NULL,'37','371500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('423000','2','湖北省潜江市 ','潜江市',NULL,'42','423000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371600','2','山东省滨州市','滨州市',NULL,'37','371600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('371700','2','山东省菏泽市','菏泽市',NULL,'37','371700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('41','1','河南省','河南省','h','0','410000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410100','2','河南省郑州市','郑州市',NULL,'41','410100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410200','2','河南省开封市','开封市',NULL,'41','410200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410300','2','河南省洛阳市','洛阳市',NULL,'41','410300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410400','2','河南省平顶山市','平顶山市',NULL,'41','410400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410500','2','河南省安阳市','安阳市',NULL,'41','410500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410600','2','河南省鹤壁市','鹤壁市',NULL,'41','410600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410700','2','河南省新乡市','新乡市',NULL,'41','410700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410800','2','河南省焦作市','焦作市',NULL,'41','410800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('410900','2','河南省濮阳市','濮阳市',NULL,'41','410900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('411000','2','河南省许昌市','许昌市',NULL,'41','411000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('451100','2','广西壮族自治区贺州市','贺州市',NULL,'45','451100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('451200','2','广西壮族自治区河池市','河池市',NULL,'45','451200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('451300','2','广西壮族自治区来宾市','来宾市',NULL,'45','451300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('451400','2','广西壮族自治区崇左市','崇左市',NULL,'45','451400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('46','1','海南省','海南省','h','0','460000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('460100','2','海南省海口市','海口市',NULL,'46','460100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('632600','2','青海省果洛藏族自治州','果洛藏族自治州',NULL,'63','632600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('632700','2','青海省玉树藏族自治州','玉树藏族自治州',NULL,'63','632700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('632800','2','青海省海西蒙古族藏族自治州','海西蒙古族藏族自治州',NULL,'63','632800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('64','1','宁夏回族自治区','宁夏回族自治区','n','0','640000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('640100','2','宁夏回族自治区银川市','银川市',NULL,'64','640100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('640200','2','宁夏回族自治区石嘴山市','石嘴山市',NULL,'64','640200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('640300','2','宁夏回族自治区吴忠市','吴忠市',NULL,'64','640300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('640400','2','宁夏回族自治区固原市','固原市',NULL,'64','640400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('640500','2','宁夏回族自治区中卫市','中卫市',NULL,'64','640500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('65','1','新疆维吾尔自治区','新疆维吾尔自治区','w','0','650000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('650100','2','新疆维吾尔自治区乌鲁木齐市','乌鲁木齐市',NULL,'65','650100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('650200','2','新疆维吾尔自治区克拉玛依市','克拉玛依市',NULL,'65','650200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('652100','2','新疆维吾尔自治区吐鲁番地区','吐鲁番地区',NULL,'65','652100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('611000','2','陕西省商洛市','商洛市',NULL,'61','611000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('62','1','甘肃省','甘肃省','g','0','620000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620100','2','甘肃省兰州市','兰州市',NULL,'62','620100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620200','2','甘肃省嘉峪关市','嘉峪关市',NULL,'62','620200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620300','2','甘肃省金昌市','金昌市',NULL,'62','620300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620400','2','甘肃省白银市','白银市',NULL,'62','620400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620500','2','甘肃省天水市','天水市',NULL,'62','620500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620600','2','甘肃省武威市','武威市',NULL,'62','620600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620700','2','甘肃省张掖市','张掖市',NULL,'62','620700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620800','2','甘肃省平凉市','平凉市',NULL,'62','620800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('620900','2','甘肃省酒泉市','酒泉市',NULL,'62','620900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110100','2','北京市','北京市',NULL,'11','110100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('652200','2','新疆维吾尔自治区哈密地区','哈密地区',NULL,'65','652200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('652300','2','新疆维吾尔自治区昌吉回族自治州','昌吉回族自治州',NULL,'65','652300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('652700','2','新疆维吾尔自治区博尔塔拉蒙古自治州','博尔塔拉蒙古自治州',NULL,'65','652700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('652800','2','新疆维吾尔自治区巴音郭楞蒙古自治州','巴音郭楞蒙古自治州',NULL,'65','652800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('652900','2','新疆维吾尔自治区阿克苏地区','阿克苏地区',NULL,'65','652900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('653000','2','新疆维吾尔自治区克孜勒苏柯尔克孜自治州','克孜勒苏柯尔克孜自治州',NULL,'65','653000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('653100','2','新疆维吾尔自治区喀什地区','喀什地区',NULL,'65','653100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('653200','2','新疆维吾尔自治区和田地区','和田地区',NULL,'65','653200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('654000','2','新疆维吾尔自治区伊犁哈萨克自治州','伊犁哈萨克自治州',NULL,'65','654000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('654200','2','新疆维吾尔自治区塔城地区','塔城地区',NULL,'65','654200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('654300','2','新疆维吾尔自治区阿勒泰地区','阿勒泰地区',NULL,'65','654300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('71','1','台湾省','台湾省','t','0','710000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('81','1','香港特别行政区','香港特别行政区','x','0','810000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('82','1','澳门特别行政区','澳门特别行政区','a','0','820000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('532600','2','云南省文山壮族苗族自治州','文山壮族苗族自治州',NULL,'53','532600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('532800','2','云南省西双版纳傣族自治州','西双版纳傣族自治州',NULL,'53','532800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('532900','2','云南省大理白族自治州','大理白族自治州',NULL,'53','532900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('533100','2','云南省德宏傣族景颇族自治州','德宏傣族景颇族自治州',NULL,'53','533100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('533300','2','云南省怒江傈僳族自治州','怒江傈僳族自治州',NULL,'53','533300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('533400','2','云南省迪庆藏族自治州','迪庆藏族自治州',NULL,'53','533400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('54','1','西藏自治区','西藏自治区','x','0','540000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('540100','2','西藏自治区拉萨市','拉萨市',NULL,'54','540100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('542100','2','西藏自治区昌都地区','昌都地区',NULL,'54','542100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('542200','2','西藏自治区山南地区','山南地区',NULL,'54','542200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('542300','2','西藏自治区日喀则地区','日喀则地区',NULL,'54','542300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('542400','2','西藏自治区那曲地区','那曲地区',NULL,'54','542400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('542500','2','西藏自治区阿里地区','阿里地区',NULL,'54','542500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('542600','2','西藏自治区林芝地区','林芝地区',NULL,'54','542600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('61','1','陕西省','陕西省','s','0','610000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610100','2','陕西省西安市','西安市',NULL,'61','610100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610200','2','陕西省铜川市','铜川市',NULL,'61','610200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610300','2','陕西省宝鸡市','宝鸡市',NULL,'61','610300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610400','2','陕西省咸阳市','咸阳市',NULL,'61','610400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610500','2','陕西省渭南市','渭南市',NULL,'61','610500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610600','2','陕西省延安市','延安市',NULL,'61','610600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610700','2','陕西省汉中市','汉中市',NULL,'61','610700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610800','2','陕西省榆林市','榆林市',NULL,'61','610800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('610900','2','陕西省安康市','安康市',NULL,'61','610900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('621000','2','甘肃省庆阳市','庆阳市',NULL,'62','621000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('621100','2','甘肃省定西市','定西市',NULL,'62','621100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('621200','2','甘肃省陇南市','陇南市',NULL,'62','621200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('622900','2','甘肃省临夏回族自治州','临夏回族自治州',NULL,'62','622900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('623000','2','甘肃省甘南藏族自治州','甘南藏族自治州',NULL,'62','623000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('63','1','青海省','青海省','q','0','630000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('630100','2','青海省西宁市','西宁市',NULL,'63','630100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('632100','2','青海省海东地区','海东地区',NULL,'63','632100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('632200','2','青海省海北藏族自治州','海北藏族自治州',NULL,'63','632200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('632300','2','青海省黄南藏族自治州','黄南藏族自治州',NULL,'63','632300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('632500','2','青海省海南藏族自治州','海南藏族自治州',NULL,'63','632500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('513400','2','四川省凉山彝族自治州','凉山彝族自治州',NULL,'51','513400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('52','1','贵州省','贵州省','g','0','520000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('520100','2','贵州省贵阳市','贵阳市',NULL,'52','520100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('520200','2','贵州省六盘水市','六盘水市',NULL,'52','520200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('520300','2','贵州省遵义市','遵义市',NULL,'52','520300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('44','1','广东省','广东省','g','0','440000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440100','2','广东省广州市','广州市',NULL,'44','440100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440200','2','广东省韶关市','韶关市',NULL,'44','440200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440300','2','广东省深圳市','深圳市',NULL,'44','440300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440400','2','广东省珠海市','珠海市',NULL,'44','440400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440500','2','广东省汕头市','汕头市',NULL,'44','440500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440600','2','广东省佛山市','佛山市',NULL,'44','440600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440700','2','广东省江门市','江门市',NULL,'44','440700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440800','2','广东省湛江市','湛江市',NULL,'44','440800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('440900','2','广东省茂名市','茂名市',NULL,'44','440900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441200','2','广东省肇庆市','肇庆市',NULL,'44','441200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441300','2','广东省惠州市','惠州市',NULL,'44','441300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441400','2','广东省梅州市','梅州市',NULL,'44','441400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441500','2','广东省汕尾市','汕尾市',NULL,'44','441500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441600','2','广东省河源市','河源市',NULL,'44','441600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441700','2','广东省阳江市','阳江市',NULL,'44','441700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441800','2','广东省清远市','清远市',NULL,'44','441800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('441900','2','广东省东莞市','东莞市',NULL,'44','441900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('442000','2','广东省中山市','中山市',NULL,'44','442000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('445100','2','广东省潮州市','潮州市',NULL,'44','445100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('445200','2','广东省揭阳市','揭阳市',NULL,'44','445200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('445300','2','广东省云浮市','云浮市',NULL,'44','445300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('45','1','广西壮族自治区','广西壮族自治区','g','0','450000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450100','2','广西壮族自治区南宁市','南宁市',NULL,'45','450100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450200','2','广西壮族自治区柳州市','柳州市',NULL,'45','450200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450300','2','广西壮族自治区桂林市','桂林市',NULL,'45','450300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450400','2','广西壮族自治区梧州市','梧州市',NULL,'45','450400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450500','2','广西壮族自治区北海市','北海市',NULL,'45','450500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450600','2','广西壮族自治区防城港市','防城港市',NULL,'45','450600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450700','2','广西壮族自治区钦州市','钦州市',NULL,'45','450700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450800','2','广西壮族自治区贵港市','贵港市',NULL,'45','450800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('450900','2','广西壮族自治区玉林市','玉林市',NULL,'45','450900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('451000','2','广西壮族自治区百色市','百色市',NULL,'45','451000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('231100','2','黑龙江省黑河市','黑河市',NULL,'23','231100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('231200','2','黑龙江省绥化市','绥化市',NULL,'23','231200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('232700','2','黑龙江省大兴安岭地区','大兴安岭地区',NULL,'23','232700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('31','1','上海市','上海市','0','0','310000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310100','2','上海市','上海市',NULL,'31','310100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310101','3','上海市黄浦区','黄浦区',NULL,'310100','310101');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310103','3','上海市卢湾区','卢湾区',NULL,'310100','310103');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310104','3','上海市徐汇区','徐汇区',NULL,'310100','310104');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310105','3','上海市长宁区','长宁区',NULL,'310100','310105');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310106','3','上海市静安区','静安区',NULL,'310100','310106');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310107','3','上海市普陀区','普陀区',NULL,'310100','310107');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310108','3','上海市闸北区','闸北区',NULL,'310100','310108');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310109','3','上海市虹口区','虹口区',NULL,'310100','310109');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310110','3','上海市杨浦区','杨浦区',NULL,'310100','310110');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310112','3','上海市闵行区','闵行区',NULL,'310100','310112');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310113','3','上海市宝山区','宝山区',NULL,'310100','310113');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310114','3','上海市嘉定区','嘉定区',NULL,'310100','310114');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310115','3','上海市浦东新区','浦东区',NULL,'310100','310115');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310116','3','上海市金山区','金山区',NULL,'310100','310116');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310117','3','上海市松江区','松江区',NULL,'310100','310117');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310118','3','上海市青浦区','青浦区',NULL,'310100','310118');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310119','3','上海市南汇区','南汇区',NULL,'310100','310119');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310120','3','上海市奉贤区','奉贤区',NULL,'310100','310120');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('310230','3','上海市崇明县','崇明县',NULL,'310100','310230');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('32','1','江苏省','江苏省','j','0','320000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320100','2','江苏省南京市','南京市',NULL,'32','320100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320200','2','江苏省无锡市','无锡市',NULL,'32','320200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320300','2','江苏省徐州市','徐州市',NULL,'32','320300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320400','2','江苏省常州市','常州市',NULL,'32','320400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320500','2','江苏省苏州市','苏州市',NULL,'32','320500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('320600','2','江苏省南通市','南通市',NULL,'32','320600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('11','1','北京市','北京市','b','0','110000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140400','2','山西省长治市','长治市',NULL,'14','140400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140500','2','山西省晋城市','晋城市',NULL,'14','140500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140600','2','山西省朔州市','朔州市',NULL,'14','140600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140700','2','山西省晋中市','晋中市',NULL,'14','140700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140800','2','山西省运城市','运城市',NULL,'14','140800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140900','2','山西省忻州市','忻州市',NULL,'14','140900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('141000','2','山西省临汾市','临汾市',NULL,'14','141000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('141100','2','山西省吕梁市','吕梁市',NULL,'14','141100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('15','1','内蒙古自治区','内蒙古自治区','n','0','150000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150100','2','内蒙古自治区呼和浩特市','呼和浩特市',NULL,'15','150100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150200','2','内蒙古自治区包头市','包头市',NULL,'15','150200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110101','3','北京市东城区','东城区',NULL,'110100','110101');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110102','3','北京市西城区','西城区',NULL,'110100','110102');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110103','3','北京市崇文区','崇文区',NULL,'110100','110103');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110104','3','北京市宣武区','宣武区',NULL,'110100','110104');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110105','3','北京市朝阳区','朝阳区',NULL,'110100','110105');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110106','3','北京市丰台区','丰台区',NULL,'110100','110106');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110107','3','北京市石景山区','石景山区',NULL,'110100','110107');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110108','3','北京市海淀区','海淀区',NULL,'110100','110108');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110109','3','北京市门头沟区','门头沟区',NULL,'110100','110109');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110111','3','北京市房山区','房山区',NULL,'110100','110111');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110112','3','北京市通州区','通州区',NULL,'110100','110112');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110113','3','北京市顺义区','顺义区',NULL,'110100','110113');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110114','3','北京市昌平区','昌平区',NULL,'110100','110114');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110115','3','北京市大兴区','大兴区',NULL,'110100','110115');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110116','3','北京市怀柔区','怀柔区',NULL,'110100','110116');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110117','3','北京市平谷区','平谷区',NULL,'110100','110117');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110228','3','北京市密云县','密云县',NULL,'110100','110228');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('110229','3','北京市延庆县','延庆县',NULL,'110100','110229');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('12','1','天津市','天津市','t','0','120000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120100','2','天津市','天津市',NULL,'12','120100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120101','3','天津市和平区','和平区',NULL,'120100','120101');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120102','3','天津市河东区','河东区',NULL,'120100','120102');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120103','3','天津市河西区','河西区',NULL,'120100','120103');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120104','3','天津市南开区','南开区',NULL,'120100','120104');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120106','3','天津市红桥区','红桥区',NULL,'120100','120106');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120107','3','天津市塘沽区','塘沽区',NULL,'120100','120107');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120108','3','天津市汉沽区','汉沽区',NULL,'120100','120108');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120109','3','天津市大港区','大港区',NULL,'120100','120109');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120110','3','天津市东丽区','东丽区',NULL,'120100','120110');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120111','3','天津市西青区','西青区',NULL,'120100','120111');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120112','3','天津市津南区','津南区',NULL,'120100','120112');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120113','3','天津市北辰区','北辰区',NULL,'120100','120113');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120114','3','天津市武清区','武清区',NULL,'120100','120114');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120115','3','天津市宝坻区','宝坻区',NULL,'120100','120115');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120221','3','天津市宁河县','宁河县',NULL,'120200','120221');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120223','3','天津市静海县','静海县',NULL,'120200','120223');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('120225','3','天津市蓟县','蓟县',NULL,'120200','120225');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('13','1','河北省','河北省','h','0','130000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130100','2','河北省石家庄市','石家庄市',NULL,'13','130100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130200','2','河北省唐山市','唐山市',NULL,'13','130200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130300','2','河北省秦皇岛市','秦皇岛市',NULL,'13','130300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130400','2','河北省邯郸市','邯郸市',NULL,'13','130400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130500','2','河北省邢台市','邢台市',NULL,'13','130500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130600','2','河北省保定市','保定市',NULL,'13','130600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130700','2','河北省张家口市','张家口市',NULL,'13','130700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130800','2','河北省承德市','承德市',NULL,'13','130800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('130900','2','河北省沧州市','沧州市',NULL,'13','130900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('131000','2','河北省廊坊市','廊坊市',NULL,'13','131000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('131100','2','河北省衡水市','衡水市',NULL,'13','131100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('14','1','山西省','山西省','s','0','140000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140100','2','山西省太原市','太原市',NULL,'14','140100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140200','2','山西省大同市','大同市',NULL,'14','140200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('140300','2','山西省阳泉市','阳泉市',NULL,'14','140300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150300','2','内蒙古自治区乌海市','乌海市',NULL,'15','150300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150400','2','内蒙古自治区赤峰市','赤峰市',NULL,'15','150400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150500','2','内蒙古自治区通辽市','通辽市',NULL,'15','150500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150600','2','内蒙古自治区鄂尔多斯市','鄂尔多斯市',NULL,'15','150600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150700','2','内蒙古自治区呼伦贝尔市','呼伦贝尔市',NULL,'15','150700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150800','2','内蒙古自治区巴彦淖尔市','巴彦淖尔市',NULL,'15','150800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('150900','2','内蒙古自治区乌兰察布市','乌兰察布市',NULL,'15','150900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('152200','2','内蒙古自治区兴安盟','兴安盟',NULL,'15','152200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('152500','2','内蒙古自治区锡林郭勒盟','锡林郭勒盟',NULL,'15','152500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('152900','2','内蒙古自治区阿拉善盟','阿拉善盟',NULL,'15','152900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('21','1','辽宁省','辽宁省','l','0','210000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210100','2','辽宁省沈阳市','沈阳市',NULL,'21','210100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350700','2','福建省南平市','南平市',NULL,'35','350700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350800','2','福建省龙岩市','龙岩市',NULL,'35','350800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('350900','2','福建省宁德市','宁德市',NULL,'35','350900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('36','1','江西省','江西省','j','0','360000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('360100','2','江西省南昌市','南昌市',NULL,'36','360100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330300','2','浙江省温州市','温州市',NULL,'33','330300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330400','2','浙江省嘉兴市','嘉兴市',NULL,'33','330400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330500','2','浙江省湖州市','湖州市',NULL,'33','330500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330600','2','浙江省绍兴市','绍兴市',NULL,'33','330600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330700','2','浙江省金华市','金华市',NULL,'33','330700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330800','2','浙江省衢州市','衢州市',NULL,'33','330800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('330900','2','浙江省舟山市','舟山市',NULL,'33','330900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('331000','2','浙江省台州市','台州市',NULL,'33','331000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('331100','2','浙江省丽水市','丽水市',NULL,'33','331100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('34','1','安徽省','安徽省','a','0','340000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340100','2','安徽省合肥市','合肥市',NULL,'34','340100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420200','2','湖北省黄石市','黄石市',NULL,'42','420200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420300','2','湖北省十堰市','十堰市',NULL,'42','420300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420500','2','湖北省宜昌市','宜昌市',NULL,'42','420500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420600','2','湖北省襄樊市','襄樊市',NULL,'42','420600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420700','2','湖北省鄂州市','鄂州市',NULL,'42','420700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420800','2','湖北省荆门市','荆门市',NULL,'42','420800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('420900','2','湖北省孝感市','孝感市',NULL,'42','420900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('421000','2','湖北省荆州市','荆州市',NULL,'42','421000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('421100','2','湖北省黄冈市','黄冈市',NULL,'42','421100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('421200','2','湖北省咸宁市','咸宁市',NULL,'42','421200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('421300','2','湖北省随州市','随州市',NULL,'42','421300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('422800','2','湖北省恩施土家族苗族自治州','恩施土家族苗族自治州',NULL,'42','422800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('422900','2','湖北省仙桃市 ','仙桃市 ',NULL,'42','422900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('43','1','湖南省','湖南省','h','0','430000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430100','2','湖南省长沙市','长沙市',NULL,'43','430100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('430200','2','湖南省株洲市','株洲市',NULL,'43','430200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340200','2','安徽省芜湖市','芜湖市',NULL,'34','340200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('340300','2','安徽省蚌埠市','蚌埠市',NULL,'34','340300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210200','2','辽宁省大连市','大连市',NULL,'21','210200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210300','2','辽宁省鞍山市','鞍山市',NULL,'21','210300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210400','2','辽宁省抚顺市','抚顺市',NULL,'21','210400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210500','2','辽宁省本溪市','本溪市',NULL,'21','210500');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210600','2','辽宁省丹东市','丹东市',NULL,'21','210600');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210700','2','辽宁省锦州市','锦州市',NULL,'21','210700');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210800','2','辽宁省营口市','营口市',NULL,'21','210800');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('210900','2','辽宁省阜新市','阜新市',NULL,'21','210900');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('211000','2','辽宁省辽阳市','辽阳市',NULL,'21','211000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('211100','2','辽宁省盘锦市','盘锦市',NULL,'21','211100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('211200','2','辽宁省铁岭市','铁岭市',NULL,'21','211200');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('211300','2','辽宁省朝阳市','朝阳市',NULL,'21','211300');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('211400','2','辽宁省葫芦岛市','葫芦岛市',NULL,'21','211400');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('22','1','吉林省','吉林省','j','0','220000');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('220100','2','吉林省长春市','长春市',NULL,'22','220100');
insert  into `T_CITY`(`XZQHDM`,`CC`,`JC`,`XZQHMC`,`ALPHABET`,`PID`,`wid`) values ('220200','2','吉林省吉林市','吉林市',NULL,'22','220200');

/*Table structure for table `T_COLOUMN_MODEL` */

DROP TABLE IF EXISTS `T_COLOUMN_MODEL`;

CREATE TABLE `T_COLOUMN_MODEL` (
  `ID` varchar(32) NOT NULL,
  `COLOUM_ALIGN` varchar(255) DEFAULT NULL,
  `DATA_INDEX` varchar(32) DEFAULT NULL,
  `DATA_TYPE` varchar(32) DEFAULT NULL,
  `NAME` varchar(32) DEFAULT NULL,
  `PID` varchar(255) DEFAULT NULL,
  `SORT` int(11) DEFAULT NULL,
  `COLOUM_WIDTH` int(11) DEFAULT NULL,
  `PERSON_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `T_COLOUMN_MODEL` */

/*Table structure for table `T_DEPARTMENT` */

DROP TABLE IF EXISTS `T_DEPARTMENT`;

CREATE TABLE `T_DEPARTMENT` (
  `ID` varchar(32) NOT NULL,
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `NAME` varchar(60) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `ORGANIZ_ID` varchar(32) DEFAULT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK16A0C81DE1B9942D` (`PARENT_ID`),
  KEY `FK16A0C81D3E094E4F` (`ORGANIZ_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_DEPARTMENT` */

insert  into `T_DEPARTMENT`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`,`ORGANIZ_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654380002','2016-01-20 10:39:04','总队',1,'26818283525ce64b01525ce6542d0001',NULL);
insert  into `T_DEPARTMENT`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`,`ORGANIZ_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654390004','2016-01-20 10:39:04','一支队',1,'26818283525ce64b01525ce6542d0001',NULL);
insert  into `T_DEPARTMENT`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`,`ORGANIZ_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654390005','2016-01-20 10:39:04','三支队',1,'26818283525ce64b01525ce6542d0001',NULL);
insert  into `T_DEPARTMENT`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`,`ORGANIZ_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654390006','2016-01-20 10:39:04','五支队',1,'26818283525ce64b01525ce6542d0001',NULL);
insert  into `T_DEPARTMENT`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`,`ORGANIZ_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654390007','2016-01-20 10:39:04','七支队',1,'26818283525ce64b01525ce6542d0001',NULL);
insert  into `T_DEPARTMENT`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`,`ORGANIZ_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654390008','2016-01-20 10:39:04','技术中心',1,'26818283525ce64b01525ce6542d0001',NULL);

/*Table structure for table `T_MAJOR_DEPT` */

DROP TABLE IF EXISTS `T_MAJOR_DEPT`;

CREATE TABLE `T_MAJOR_DEPT` (
  `focusDeptID` varchar(32) NOT NULL,
  `action` int(11) NOT NULL,
  `activityAddr` varchar(200) NOT NULL,
  `background` varchar(200) NOT NULL,
  `collectFirstTime` datetime DEFAULT NULL,
  `collectName` varchar(20) NOT NULL,
  `collectUnit` varchar(20) NOT NULL,
  `collectUpdateTime` datetime DEFAULT NULL,
  `DID` varchar(60) NOT NULL,
  `email` varchar(100) NOT NULL,
  `fax` varchar(50) NOT NULL,
  `fixTel` varchar(50) NOT NULL,
  `focusDeptName` varchar(100) NOT NULL,
  `mailingAddr` varchar(200) NOT NULL,
  `OID` varchar(60) NOT NULL,
  `postalCode` varchar(20) NOT NULL,
  `regAddr` varchar(200) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `UID` varchar(60) NOT NULL,
  PRIMARY KEY (`focusDeptID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_MAJOR_DEPT` */

/*Table structure for table `T_MAJOR_MEMBER` */

DROP TABLE IF EXISTS `T_MAJOR_MEMBER`;

CREATE TABLE `T_MAJOR_MEMBER` (
  `focusMemberID` varchar(32) NOT NULL,
  `IDCard` varchar(50) DEFAULT NULL,
  `accountAddr` varchar(200) NOT NULL,
  `action` int(11) NOT NULL,
  `background` varchar(200) NOT NULL,
  `beforeName` varchar(50) DEFAULT NULL,
  `collectFirstTime` datetime DEFAULT NULL,
  `collectName` varchar(20) NOT NULL,
  `collectUnit` varchar(20) NOT NULL,
  `collectUpdateTime` datetime DEFAULT NULL,
  `DID` varchar(60) NOT NULL,
  `focusMemberName` varchar(50) NOT NULL,
  `homeTel` varchar(50) NOT NULL,
  `mailingAddr` varchar(200) NOT NULL,
  `mobilePhone` varchar(50) NOT NULL,
  `OID` varchar(60) NOT NULL,
  `postalCode` varchar(20) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `resident` varchar(200) NOT NULL,
  `UID` varchar(60) NOT NULL,
  `unitAddr` varchar(200) NOT NULL,
  `unitTel` varchar(50) NOT NULL,
  `workUnit` varchar(200) NOT NULL,
  PRIMARY KEY (`focusMemberID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_MAJOR_MEMBER` */

/*Table structure for table `T_MARK_RECORD` */

DROP TABLE IF EXISTS `T_MARK_RECORD`;

CREATE TABLE `T_MARK_RECORD` (
  `hwCode` varchar(32) NOT NULL,
  `action` int(11) DEFAULT NULL,
  `caseNo` varchar(60) DEFAULT NULL,
  `collectDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `collectFirstTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `collectName` varchar(60) DEFAULT NULL,
  `collectUnit` varchar(60) DEFAULT NULL,
  `collectUpdateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `DID` varchar(60) DEFAULT NULL,
  `hwDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `hwIdCard` varchar(60) DEFAULT NULL,
  `hwImagePath` longtext,
  `hwMobile` varchar(60) DEFAULT NULL,
  `hwName` varchar(60) DEFAULT NULL,
  `hwSource` longtext,
  `hwType` varchar(60) DEFAULT NULL,
  `imageFormate` varchar(30) DEFAULT NULL,
  `OID` varchar(60) DEFAULT NULL,
  `remark` longtext,
  `UID` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`hwCode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_MARK_RECORD` */

/*Table structure for table `T_MENU` */

DROP TABLE IF EXISTS `T_MENU`;

CREATE TABLE `T_MENU` (
  `ID` varchar(32) NOT NULL,
  `CLASS_NAME` varchar(200) DEFAULT NULL,
  `ICON` varchar(60) DEFAULT NULL,
  `NAME` varchar(60) DEFAULT NULL,
  `URL` varchar(60) DEFAULT NULL,
  `MODULE_ID` varchar(32) DEFAULT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK94B5DA6A5B5D27C5` (`MODULE_ID`),
  KEY `FK94B5DA6A72E0A7A` (`PARENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_MENU` */

insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e20053','','MaiorInfos','重点信息','Ushine.majorInfo.MaiorInfos','26818283525ce64b01525ce654db0009',NULL);
insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e20054','Ushine.majorInfo.MarkRecord','','笔迹档案管理','','26818283525ce64b01525ce654db0009','26818283525ce64b01525ce654e20053');
insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e30059','Ushine.majorInfo.MajorMember','','重点人员管理','','26818283525ce64b01525ce654db0009','26818283525ce64b01525ce654e20053');
insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e3005f','Ushine.majorInfo.MajorDept','','重点组织管理','','26818283525ce64b01525ce654db0009','26818283525ce64b01525ce654e20053');
insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e5006f','','SystemSet','系统设置','Ushine.system.Operations','26818283525ce64b01525ce654db0009',NULL);
insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e50070','Ushine.system.OrganizManage','','组织信息管理','','26818283525ce64b01525ce654db0009','26818283525ce64b01525ce654e5006f');
insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e7007e','Ushine.system.RoleManage','','角色信息管理','','26818283525ce64b01525ce654db0009','26818283525ce64b01525ce654e5006f');
insert  into `T_MENU`(`ID`,`CLASS_NAME`,`ICON`,`NAME`,`URL`,`MODULE_ID`,`PARENT_ID`) values ('26818283525ce64b01525ce654e80085','Ushine.system.log.Log','','日志信息管理','','26818283525ce64b01525ce654db0009','26818283525ce64b01525ce654e5006f');

/*Table structure for table `T_MESSAGE` */

DROP TABLE IF EXISTS `T_MESSAGE`;

CREATE TABLE `T_MESSAGE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CATEGORY` int(11) DEFAULT NULL,
  `CONTENT` varchar(200) DEFAULT NULL,
  `CREATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `DATA_ID` varchar(32) DEFAULT NULL,
  `RECEIVER_ID` varchar(32) DEFAULT NULL,
  `SENDER_ID` varchar(32) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_MESSAGE` */

/*Table structure for table `T_MODULE` */

DROP TABLE IF EXISTS `T_MODULE`;

CREATE TABLE `T_MODULE` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_MODULE` */

insert  into `T_MODULE`(`ID`,`NAME`) values ('26818283525ce64b01525ce654db0009','寄递物流侦控系统');

/*Table structure for table `T_OPERATION` */

DROP TABLE IF EXISTS `T_OPERATION`;

CREATE TABLE `T_OPERATION` (
  `ID` varchar(32) NOT NULL,
  `CODE` varchar(60) DEFAULT NULL,
  `NAME` varchar(60) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_OPERATION` */

insert  into `T_OPERATION`(`ID`,`CODE`,`NAME`,`TYPE`) values ('26818283525ce64b01525ce65514008b','0x0000','禁用',0);
insert  into `T_OPERATION`(`ID`,`CODE`,`NAME`,`TYPE`) values ('26818283525ce64b01525ce65515008c','0x0001','启用',0);
insert  into `T_OPERATION`(`ID`,`CODE`,`NAME`,`TYPE`) values ('26818283525ce64b01525ce65516008d','1x0000','禁止读取',1);
insert  into `T_OPERATION`(`ID`,`CODE`,`NAME`,`TYPE`) values ('26818283525ce64b01525ce65517008e','1x0001','读取全部',1);
insert  into `T_OPERATION`(`ID`,`CODE`,`NAME`,`TYPE`) values ('26818283525ce64b01525ce65518008f','1x0010','所属组织',1);
insert  into `T_OPERATION`(`ID`,`CODE`,`NAME`,`TYPE`) values ('26818283525ce64b01525ce655190090','1x0011','所属部门',1);
insert  into `T_OPERATION`(`ID`,`CODE`,`NAME`,`TYPE`) values ('26818283525ce64b01525ce6551a0091','1x0100','个人数据',1);

/*Table structure for table `T_ORGANIZ` */

DROP TABLE IF EXISTS `T_ORGANIZ`;

CREATE TABLE `T_ORGANIZ` (
  `ID` varchar(32) NOT NULL,
  `CONTACTS` varchar(60) DEFAULT NULL,
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `NAME` varchar(60) DEFAULT NULL,
  `REGION` varchar(200) DEFAULT NULL,
  `TEL` varchar(60) DEFAULT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1604FDF72E9363A7` (`PARENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_ORGANIZ` */

insert  into `T_ORGANIZ`(`ID`,`CONTACTS`,`CREATE_DATE`,`NAME`,`REGION`,`TEL`,`PARENT_ID`) values ('26818283525ce64b01525ce6542d0001','张三','2016-01-20 10:39:04','部中心','全国','13812345678',NULL);

/*Table structure for table `T_PERMIT` */

DROP TABLE IF EXISTS `T_PERMIT`;

CREATE TABLE `T_PERMIT` (
  `ID` varchar(32) NOT NULL,
  `OPERATION_ID` varchar(32) DEFAULT NULL,
  `RESOURCE_ID` varchar(32) DEFAULT NULL,
  `ROLE_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK43C92E66DDDB5A45` (`ROLE_ID`),
  KEY `FK43C92E66993475C5` (`RESOURCE_ID`),
  KEY `FK43C92E666D35D6EF` (`OPERATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_PERMIT` */

insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5jOnEQiOVqF4F3E3x83ZL2dbssVNXF2n','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd0018','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('tdsELkc4jnS6aR71ANuMAOkiSpKpLCMq','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd0019','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('loX1gkpJZ5XGmtM6VV1d3BB2xAsOGxM2','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dd001a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('GSOY6SzEABXNzN5zRU0bP2oSJadannQB','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd001b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('MAGaIw4WmiQRnp7qX7Kr1gcYTNgAEdfC','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654de001c','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xY1iLOlOAdgkccaNuzrM1ChwiSTEK9xv','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('8WHosMengyx0MYdO7bCZNddsGbuROL39','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654db000b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('LrGzPi9JUyhZCQ4gXewFjxtoCUhaDOx4','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654db000c','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Xta4KMgGTcm9VOdE6Cr01OqEPdvwSoMq','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dc000f','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('dnAw3xNlQhj0Sb3hkm1ZtoOiOdtKjePh','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dc0010','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('gMVwdZn6f6cU1DHqmgmnq238IEjm8SJe','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dc0011','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('mnomKI7LexuMik5itIztHdnb3qU1a5iE','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dc0012','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2czN6pYSRPbY1E0WzidKpVvrFNB1CJbY','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dc000d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('XjiYzvAq4MDKcvdqIQIe0BW43MKETi0k','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dc000e','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('KM0xaLD0QcriR9sDvQW6fb1s8izWDBx9','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dc0013','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('vJRGMJcntzS3QvBnw84bm9IXEK131QBM','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0014','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('VrU4Rz0RBnG3XkC77Qnq2jO9jXEqozS9','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0015','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('l4qBZQ7CRriUdSmWLRSGxeN3FWstI7Ha','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0016','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('6TZWhnwIZmU6Fvb70HjMrPvScgfXNtDi','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0026','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('CzMG7KYmaaX1IeKMUvjg6VxPnUdlgDR2','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0027','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('dO2dOwSImVO1gQh5sGgQZqS7Iy5APDXd','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654df0028','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('PIHbk64yeOQ8qQq5kw16d0wVU3STVfbk','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0029','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('LszLCdlRJ1XQ5iviGcNw8IzLo9dIsy3d','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('dH3virShRsv2dAvGiEQgsrXo5U86nWgy','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('GnaKO2Eqwj6Mw3ZSWBc68Lk75K03KBAg','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e70071','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('T7iYVkLp855UOG05Cs4cAAZKBCITE24r','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e70072','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('EkbpYKQX3ULp0L3DDaKSfIQ33Fc3JmBd','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70073','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ddRvPir5BzEZtcMIx2PMpkzJQHKJlo7L','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e70074','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('UtVPRjXajuBBSTMbv6BoLkZT5qih4SsN','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70075','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('MDienoarXK9KqXCdkyVNqZfvMjJwH6bH','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e70076','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('B6IN1ylVyC8bF8Y4HXoxxfa5ZnomLunU','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70077','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5mqSQZx0TTah4uzEAekIDyYyubtfE5eK','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70079','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Fo4YV0vIVNpbG1iwcyqvbqbH2cfePVk8','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YWSmaMyqJCQ7eTpriKrDkCUxIDyX3B5i','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80087','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jX2gjZfRmajykdRpt5eyT7BM2C4hLhEq','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70078','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('p07AB2q86fMinx1XGzSd1WhK3Bf8LCbF','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('8u79aQ0GmkcgiAxU9IVostn4KKDMNZ6d','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007c','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('mleGFd1YQ2JJ58zMLernKdolYCcrAIPR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sEDkLXlT36qPWrpmCy00DI2DF4GtJUW4','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e8007f','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('AuAuxi9X9Ak0bsV5aDrSNO5BgN5DTXet','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80080','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('PI1TSaSBwk69e2bEka80M8uFdC0nuahK','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80081','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('yGoQ2PEK1xbNUMRuOxiuDg1qTDVstslB','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80082','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('gdeO36A1dXV7iBsv7pnJVgy3CWRJgqtr','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80083','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5WJaF7idleHySGDJkxKO5pifq74UkDhW','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80084','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('9Nl9FNEHx2VgcmMp1UWNvQLY0YBVRPaJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80086','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YT3vyRUhqpwIxDVTqrl27NaeSpRiQE1A','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654df002d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('9suBPXNWx3oahAebKd3mf6mzhZJowRHX','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002e','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YaDh5i9TIO5JoJTNhwo2P1XymHaRevy4','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e0002f','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('8PjFfaoFrtFWSEtWbMm4NV5S5AWb16UR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00030','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sUdg8RZvCUhPqnMxn6KLSGkvuaHAtcOw','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00031','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xAssTajfWYpHRzlIF7r2m3p3MHQHAwIU','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e00032','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('AgkKNuhxHgI3kR3bXodiZ2VqGH05Xa6F','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00033','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5Z9zAn8U7GFB6PcSaJKrJjsnQuwATLOY','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00034','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YrAr0KXqV6Z6IjVgrHFTjfXn5Xiwwawb','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00035','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('7k6sXDuLwB9sO2cQxT3uU5WIvgcxnOon','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00037','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1wv2LSjc834OOmMh1cEB3OoiFsuHVsJ0','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e00038','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ranAm30XsiYf1XudPk3nV7MCnSXZXMdr','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00039','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('bDfuYwOayUsXcXSywAwt2dJgiIUd1sqR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kZCRmuoEnOXNEDZRQVhkvT35621vNc0c','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('HrgWG10hHSWxUGJISNFMkKrABHVHcaVa','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003c','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('tYhrK1pBDRDMiqTUCEkXWML28bcPe09P','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ClmJW3Z4oFmth5bUjPcNRLdO7xZMiVzM','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e40067','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xmunoE6as0gHMzSSWxFTZhAA0jGwI7Hz','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e40068','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('vGqtgLillUrEz77ol5vpJofP5O59wRjm','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e40069','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('I7vAwUHuAM2xDKCEjQD3SYGvCyMG37wy','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e4006a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('7f2uOQnI3P1zQvMOqJ2t6r0LB9X4zJWa','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('IisCf5msa51omjlrzB1SHUQJ1kzriWr5','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e4006d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Fb0iA8gOrZjus6YVMF7zduNcAI2zB5Xx','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006e','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('LXaBG26OR6WUGyxvh9iqcZ92P5SqAwyd','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80089','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kUQOu2YtVRbI1gtLwo6ImtG0ZI2YHiHw','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e8008a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('h4oPnfDXA6LTgk1IdXzzwLBvKjF1Czbu','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001f','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('QD2ubsPaOsJ5wYNxbn1ItrkH2XFYKT7t','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de0020','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('dcFISmraL0rTkt6MideVRHmr4QOaFC6O','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654de0021','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('OpyQmfyPAmyPPkd4BYud7HI2NQ9jqoAg','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0022','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('mdGozWobANSRm91FS1zTWN2HaMWyKAb6','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0023','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('CM3HgCQDIOKEWt9Vb7tFhWHvSOWjWbdq','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0024','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('PmZIuoZlPFHW4SzsWa55fWJtk66s4xPe','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e1003f','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('qqtlgNolJFeGZP67oAvq4MQxPZ8lgUJD','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10040','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('NcwIPiC4TleAd0xUmNauhHnInSp5wM0I','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10041','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('l9B2YZrF6RdnymFMVkVg43FFrZhgQYAL','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10042','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('UuRCPKEGyhLkBvhXHw5ZNXVR92Cezvbg','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10043','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wqBxZpy57z2FDNwP7CoXBclnLsrapCs0','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10044','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('VoIwD6S7NThhEO4M49GxmdT6aU0sQLC2','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10045','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('UEHAx8kiRVn81ZHLhNSdoBAAI7FEqP89','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10047','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('zPE66x2TWizv55QuliLmoLVRsyYeIiMO','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10048','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('u5Fnjy4dOAKp3jf6bS8VUcOw1AAmWLtz','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20049','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('4vhIFzKiWEoPr7BleCOO0BekGqaw1d1N','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e2004a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kOhCX4skUJIhQEmaLWZ2pFYnqdbzqiV8','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('c9bxUeb0K8z8nmteySxJiXoQlqs8LpOd','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004c','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('z8haefcen7BwuITaCJawjJdwbWRDhuUn','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5QsQwLC0ZLdL4lBplovz1NkU82LmbulO','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004e','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('VF41fwexb51G7FeaAMKfYpGJ8PFEH4Oe','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004f','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wGm4vVuzOtIrYcxJxwLWpwsRWF2C6snj','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20050','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wKEA152VYn8XpXyHJvC9SSckNzpWkeSR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20051','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('WXy8AOOK6gP7pNZgnMzijDJ0Z17YENks','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20052','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('aMIS7wNsMJPddoTlILqeoHo0U7NitsQG','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10046','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Ksiz7m571m9HGaIcOSb7XsOIq0vtH0G8','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e20055','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('e0sBIQYRtQL6r9IovGB3WsTzcu5XQEJ9','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30057','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('4hmPKdPRZc2F1Q5UWBdQpHK4pWxWglPx','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20056','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('JIg995cKtYuGeUNpEr55K9o4bZtTFJ8n','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e31234','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('D4RPTWZVR1Jvby1XnJP6RARwVNmN40Fp','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005a','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('9yHTtnFb9mxegrGBVrJrt7skROl07FOC','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e3005b','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Ud4lnwfRn1tvxfyriBtyOY2OXxOZHCxx','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005c','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('464hzntcblWKjLtOESL0Ui6O9iHHLF3t','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005d','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('7H4Agywf5d73Lk1lNUQiZh7RbafzCPW5','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005e','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('KgFeDJWaz1qiGbaeZTO272rv5dnrlzMZ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30060','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('FCx5BFCH6duSF9Bv9czN8Qf5Id9Lqgyt','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e30061','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('h6ilCdNGGNe5vJ1F16M2qjk3enhL4J2e','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30062','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2ePltJHWrmzjOWodfs8D211LIv25ODMJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30063','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('HB6RHthHjaTQTVPFOB4tnosqdcrKUTht','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30064','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('OU3bbApaMpl56MNfFELnpMHDlAe9kzcy','null','null','26818283525ce64b01525ce6551c0092');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('yqD4Bd3Eo8WSnyU7EFDxXCK7SvsSh4dh','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654db000b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('8J7WN7ZaIiTEBifmUctKa4OeR90j7Ao1','26818283525ce64b01525ce65514008b','26818283525ce64b01525ce654db000c','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('8VFWhYH1dei7wQS7umC872qKY7HyIiOt','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dc000d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('4qK8H5XZNsF5qtPOVpdMdGFKqxD7BVn4','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dc000e','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('oQi2uOP5qB3N2IGOtq0hSpaYHhNFSSh7','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dc000f','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jZJXMQr25eunAuJYJXjUmaDlOc55DFZV','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dc0010','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('nxAAtCS4v6tym3Qb2ip8YjuHozwdFHRV','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dc0011','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('9pafWxv2mbBLSf5aPhSIdqfI9FePqs39','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dc0012','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('0gR1vJBVGTAJH5M7N4Q1nL17ooxQvhgt','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dc0013','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('JzJnFAf1TLR94wZaDHeY1O2J85lNPQ9d','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0014','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('M3gODgwa1cuCWpBzvxiEt1GTUI4toozA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0015','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('buKKgf7ymPYGcgEmhygxJrhTHDLGOEWo','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0016','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('hG48UwWquSdZeqIqIsURce60S3rYE5XI','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd0018','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('6cUuS7CjgxNhBFvkQwt0nVNiJj2EOMQY','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654dd0019','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('K8ERowFH5v3JjC1r7mReW3jMVym5Jr61','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dd001a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kSdDGSBbmOpyhKKFtjMQKY2g1bsPVlds','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd001b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('3zcLU8Hsk8rNtPujF4A95M834Y383iBT','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654de001c','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('R7iuR1MoiWjFHbMUKkHWUeFuQKQsQwq5','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('cJ0e7SQhuQl3HtX7JIhiFQbv1Kmx6TIJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001f','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('MeDJIpSnMPpYagn2IxdL0uNX857RkEBf','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de0020','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('cBjSDjTFYxxrQkeG4Kih6i469yN0goiS','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654de0021','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('enffZGKI1SyVHIUELa6K4f9vFCJvze1G','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0022','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('eyu6rAdbIRaXuFS2zXjn2HmMA9EGket9','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0023','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('AKsZtlXiQYphjiWt5aooxJuA5eRSOFa8','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0024','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sGXcoaqMG79C6lKugJm4mNkR2xnmkh4H','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0026','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jSW9aOjTtIVEtU9pxBdSTXw2wVDmUkh7','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0027','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wwW9gblWQcaT43jn0FZ8gnh5kEvheY6c','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654df0028','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('lp4Wlfxh2YYWePI9kvatrNxu1G5726rM','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0029','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('S357Z7EmlA9BMJciDFTueCfwCXfIpULs','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('tXmWJnhbsTIHHQh2cq2Df9eN7O8q9D1l','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YxmqwOWA1I9CXXXB3l8noFn8qvZVOpQe','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654df002d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YI4tHZ1A23HKSunJz1OxuQSBHLHbsvJA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002e','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('nmJLmwlHtVbwJeV9vA0uNDn0LVZI4wkE','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e0002f','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('s8GSpglVXM26Hl90QrkI5keC1czMwp8v','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00030','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('UmUH2miNwVSlCOP68ZJV8Y7lzMEMzyXS','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00031','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('nI8Kjpwsj9PjVQzAxFuBnD2sfrQloYd9','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e00032','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1IUmj8gluJ0gVtGUtBKZA7tGXo6w9gmQ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00033','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('FvhIOGP94XB0g4xZXXBZ3B8Au2VKJ2D3','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00034','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2qzWFXDLOS5lhefy9lXW713uUmUvGgZG','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00035','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('C6ZrWJJAXQfHskSMeICTZ2I6zYjO8RoH','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00037','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('klKdijvpmIEY7qehMw5me5iy9Id0F0fz','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e00038','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('QQRppVe2z8UDJB1i2OACwnsgoW4hNy95','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00039','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('RaAtdn8uFiSzydvRZg8abnML2twioflZ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('6ERT2NZ8RDE6WaaIt6egwjYigOr9BkCR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1aHqFfoIAgHIIKuJZpG5HYo0C1872bXQ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003c','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YsMGKdY5WCVWVn8OLwIlY7YJXaLO1dJk','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wl506jpr8GgMTex8YtqMpOGtRt4Czjg4','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20052','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('88Eh9aS7CDH42II291cvYPiRXqNxDpjA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20051','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5ULV7PNsgv8TjUIh0TUmL7gTNU1fidRt','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20050','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('XRxe3ZZRr6KezPYh2232kifMlOzuPtjk','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004f','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('LkHkeuOcPBLl6XRVELx785lf3HFmyVHE','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004e','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('O8CJwrcEjicGTfl1oWVjTogZIJcxFA6U','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('9uUdEnN2gVeKgibxoys6lodQyLPKIO1W','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004c','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('WPPE8K4VDKxj3EQZVaGQtW15hV4syE7M','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wfkHAD9kGAGi8Sj3IP0lZdg5vBwaLfuf','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e2004a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('K4L81Q1QpSf0oC0dVdG0eRAhCUxXBdf8','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10048','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('MDONX5Dg1w1Dz291vz65vbmD4KbHvqNZ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10047','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('caxMUYfPPtGB2ZPa1OXtIkDEH7dHeTSi','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10046','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('gWvyPZ0LqNefMYR0J21bEoNYITkT3DOh','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10045','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('D7sVtbVhgVVs5TwKhXs4gFQdXN4Jv4vP','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10044','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('NySXz0QNenoFWWyz1VbVPGsIbSDY4epz','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10043','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('C8N3UAr5cSofBPKe1cVy0nc9iJQMO1RK','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10042','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('i6YdSlxGewccVrvrLC3gTMKnAHprhH9Z','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10041','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('FvpInePQ1wODM1TLoVQndUV97A39uKJZ','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10040','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('e1h4S8kPrgylnHT63zA9mTRV0QwQV2Yc','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e1003f','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1wFRJtixFZfIzrfqLWYcmOU6jXswKoC9','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e20055','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('8zNa6Epaj7pBndthQPuGsiBeImbqOSXO','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20056','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('arktC4N4hujNFdXLL29fjJCWYMbChrJD','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30057','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('iP7k00rQhAX5L6xOn9NzFsvaMVGhzAcq','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30058','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('DfJg6BqfW80AETsNlE7Ol1ecVn3Q8xkQ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('A3z6oA9Hwkycicu8VEX3WxYevC1t6vxY','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e3005b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('4zzQF5RgFCy0ZlJjQoRMCEJxKH65MgiP','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005c','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('550TqT8Ob57RSo4U9dk24tiq2dE9ZOiS','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('9pmbOypfjXLspL3fjPZsSJELcTH5jftL','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005e','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('TeMjSRfM0fWrzcbnxmnr7Hio7wIjE3eb','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30060','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Wbd7MPw0UijETwv4k0KmhOHHj8vdYHps','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e30061','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('QV3wdNxMJZfVSeKzv0BTMPJBpfgWhH1Q','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30062','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('KahDJMIR3Ji3hyKZKUCXgcpy7TMRjkvK','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30063','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ZqEQa8P9Krj00MKaz9FPmRT4MuyngytG','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30064','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xgwBL5CQIFEvqw20v3QuG1V5Z26piIJJ','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e40067','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sbrmRVmABhCRcJG2CfwWC7U1Q0PGLVDT','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e40068','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('eGLuG56WYAN8T1ZXSa1NmDTHFIplGKi9','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e40069','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ErpsD8xJDJxhwWAyyepZB9Gi7An56ALT','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e4006a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2nXeisQGlqZe4nj9DHRQWyvmc0V8QgeM','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2vYxPx4AzPPiP2yHXPHrO0fXk7CWZlQ1','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e4006d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('C2ZA5NJNTbve48jCM6VhsUJVXk5iNWFm','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006e','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('HdqCXQ8T7bo3Vq34aQX0uGgLFuyTg1rW','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70071','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('3GJyFahouduEOYRt8UcnAC6REHoKYEUt','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70072','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jlVkvKDIMpDI7ymwFqJXklJApmg0PJ14','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70073','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5Rr6jkSfCIjTR7lvFK8wIpVNjzMNWJYv','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70074','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('QZvS7MmkqxZMJ2e7vk5F4q8SbmcXU6y1','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70075','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('3KzyDQ5b0uR7SMi8oxaCp5nkwqYhztfl','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70076','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('lVWJsb07tAkZl8iZLbTWN2Q5DdtIa2DS','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70077','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('buaHG4KinYZ1Y9PYhbphWliQIxTBJeVw','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70078','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('hzkFrJVzTkweh4eydwq3lkTLb5KMaWRI','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70079','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('T2lt7MR1YGW6it9WdDfrbRVyd2iM7gdW','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Tbx4ZlYwxFBGKULnKjJcbz7Te5ZsYZoA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007b','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ojHjXBRHg3Lh4TXeoWmqySRmdq4yBmDz','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007c','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1sfICrPBRJ8vvTgUlTDEeFKl7dVYpH8T','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007d','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('zQh0Qm9tZF21Wp4DyyXBuhGq8Emryk5N','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e8007f','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5ogDjV4pMZ1mwff0IXxYvtxHmoZGeXzr','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80080','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('METHtpU7SrabVNtEqVt50hwMyezpV0Ah','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80081','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1u9Ee2sPpUeObmijpJPEBtgN8kIxtlqF','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80082','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('yd1COeJxisKtHeY3HxfpAbdfhO2fZ9pQ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80083','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('QL3jwQgJSS4mlaif0qrxZ49zdwVKNVyX','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80084','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('QwYMnU99YnDylSSbSCDsOw2knR9ws5mg','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80086','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('oRcvlMzCdr4zL4lgxsKKWTCfE5m81WLY','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80087','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xpd3ZeaA1PZyFJkdRYWWYNimzsQYKWf4','26818283525ce64b01525ce65514008b','26818283525ce64b01525ce654e80089','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wCEjI6zlkaAjrtKs4FZahc4e4k5dHTe9','26818283525ce64b01525ce65514008b','26818283525ce64b01525ce654e8008a','26818283525ce6b801525ce9f7bf0002');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Pfh4gF65M4Gxr7BMMfJWv1kopzUhbIhM','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('i6MnQjNe1ziS1SFOrFUKn6FHyrPpdBQR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007c','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kRbqMrngP9ZG3BSH8X702piRKmrwglfb','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1dXVgtswZeGRpgHAdXgayQVxdAGBSrfe','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e7007a','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('6cUGoIFhdYWjrAAfFYI3Ow63pJW8gZfV','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70079','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('LzBEQ5HZ01axYlo0MLi5oTaV2vAjXkv1','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70078','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('yt6asIOzLePMNADoBI6hxHO7KGYCmILj','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70077','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('v4J5AaG1qYmgZVsWZhSCTUKcP3VYh49V','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70076','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('TwMUemHXU7DOQ4GVCdACRsMuwa5JgMK9','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70075','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sOcjrIhL2Zj2qxtevbBB5PMtPqjTy3gA','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70074','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('WoJAmr51LkuIBioF9jqqs9tuASNdkqnA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e70073','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('MmRun2yjVuT4e5rzMEg8aQJgJ0oFlEXc','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70072','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('loQb4ez8DC0TgaWaW7LRMqwUMoYzW3hT','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654e70071','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('n8wBJqWLogo5Zdq6EnJRLzk71GEUcCzy','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e8007f','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('XkZH4i2vsAufJObUPI1jG4xOihLLnhsJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80080','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kRMw0Qe8UcTirp4LUoGmDIwsPTmZvDLz','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80081','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('awHphFAyH1Y57wSWDxwMujtl3xGM3NzJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80082','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YB8SodwpeCLlElQM55Sd1CX0Gu3XLkbF','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80083','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('BdNbp0lOBdsx4dEsspp7dTV0NosCWMq7','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80084','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('RzDTF2zZUXddz31GZNS0XkKdUNFGe3tO','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80086','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('JnPJFp0oqbYGOOisCYtCGurr1x7ClTgv','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e80087','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('hCkTLcKpzL5YRULrreviuGWxLOeK0pAh','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e4006d','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Ljmh3iwPwHf4YaYND1dlNdXciRc2dJeJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006e','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('NbjseX6gaK0byFUqweVpIPl1AUbvTQUL','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e40067','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jCQEG9elsgnwhp0TrRakE6nS9AxGfEdy','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e40068','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('IIiNR2HHoUYpgul6S2DjHIEvn4ofcxEL','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e40069','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('CkcbG9fmMgPtURYmJNt10Zf9Pf9h015D','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e4006a','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('yLAC2w0MDioLkCUeiYfMH7eUZ2genJCA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006b','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('58TBMH7c2xOwL2mbx7YvEdK4bXicsOWp','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30060','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('oqIZNILdOTuteptpH0wUOuwyhhMrDk7H','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e30061','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('4SFqOvKlCPX72Y7G8jjvp8tP9Br8RWGm','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30062','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('B7HwQknqgnop3CJ4CbN2vAmBxFj4sJCC','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30063','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('BNkW7jRBYncfiZoSdSEBzdV9EwMXK2Vn','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30064','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Qpgv4ejYDz2rolBE5WGaE3ngDpgUIQaK','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005a','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('tI5aeQC1bWzl5LtMBytMJzyNNBiRKhSp','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e3005b','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1yO21jQoIICvHfGxeDjhr436wBl6eNg8','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005c','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('qeTgWtBnie4FAsNBItP5RIRqiyQ70QwY','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005d','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('k1LRgi210wks5u9LbLtpTPa3Px5Lm5sD','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005e','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Ielk3I4D13PLerovtmRUZ9KtU5Su4xNN','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e20055','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('WHjAvUM6aLIRTTcSaGuV9IpBd48MNFgz','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20056','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jObQP3xtItrp4DmZ1qSwW75F8fDVnLcU','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30057','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('YjuM0UCQacG6LLsYSdnWXfkJeJePddhL','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30058','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('hlLeJo13qjyCqZAYw2yHJjXN9QzuAf5f','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e1003f','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jvz4dc8ww2k1Ie4BVHIz8TlmRDYSueng','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10040','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('RAsPe1qZ2Jmlkfsjh6qumsmAN05HJ9WS','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10041','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xeVMPchEAXKNu73UNcAni1hA6268yG9U','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10042','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5pD1grRfkDcWj5jHF2lmpbYHJbP5aSvx','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10043','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('x2jr4UrTeTgqQvjNoEYp82hhczynijOr','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10044','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('wyOSZFdyD95xAju2CKj0RGKkpaqROdcj','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10045','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kVRaMS99YKV3b7JnDa8F4prplSgNcqCp','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10046','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('9W2mlHNdVXSgAPOfKSnEMwlo70SajIdT','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10047','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('rokAwpKfssfdScaaGzu238BHPVlACrQ0','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10048','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('uyHIvslY7AZoCDCSrcdTU1kQBD4wcPv5','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20049','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('VSMyHtz8YWNZUpvf1BCDEzjRfdm7rTis','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e2004a','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5U8eOJ8kNWhUUn1z1E1xOD6FXWzjUYuj','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004b','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('d9la0LsWpajMLYqnfUNibFjZaPRJptTg','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004c','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('GyRKb9MPSuvNVzSdNHajANx59mL8ZUSS','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004d','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('BbecjS6FGylFAPZR9lgIKYszuhe9jR5H','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004e','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('caJsZs8Md7qWaQnUGmVhhXA2sHkmgJrV','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004f','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sF5CmyuYyMq8ajcjKquDsqVh2AkMf5Yr','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20050','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('jRZhQX01EggLkcXmj8Vp4mMKl5FTnIpF','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20051','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('GCawjWpgBDxLyLRAU4xebPzpruwd2Uhu','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20052','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('envyvXvNpAnaPKI6ZeswGtxAqv7AVTPm','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00037','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('UcN50lXvk5OwRdYByNrx9LTGmQtF1oar','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e00038','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('0E8g8HR97fRdkYwNJglizO8IRoi08ZjL','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00039','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('fjm34ag72QlQjDLpmEImFIecxvVw0pqo','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003a','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('40gafslpratgJTRgOJRxKpKgI1P2KlRA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003b','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('c8i8rzH6Ui1skC3XtKJM42lT8vwYCYu6','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003c','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('dgsSu2HhQQ8c7NaSBPew5tMrfv8Yc0aL','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003d','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('03mxxql4xdSne2V4YnwR4PVDivh4WMAE','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654df002d','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1VDmftlQrf0LrivIUlpgxFxFqyEYog5r','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002e','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sMiGeQbbVKvASOOXFZOKwcAS1cfqydLP','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e0002f','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('7sjQAgDfWq4DOxSz9Gs54LR84x8KUXz1','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00030','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xy00xO9RgoyqyhRW7pdUc1PWPL1KDjKk','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00031','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('hsMDyhFqFrJWompso33jJIGNzbpxNgJ3','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e00032','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Z7UrLw0ilwKbH3kCowsNcZC7jykDbLk6','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00033','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('HMOXvkmGEjXXO1wruLw3zleuXvJuYpoS','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00034','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('g5SHcYLnD0MXBYa3He4xyaXLllTboKbV','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00035','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('y0khThDbM7pAgfe2joPiWcJi942nrKIH','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0026','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('l7H77DUu3JtH7oSyozYr8XqdwkaoDLgE','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0027','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('iSOMzaOWCOkzssEKI5S6bDFDGF2mjt6r','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654df0028','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('SPPhfqthFkOcET4YFTHzB3UDTuMah3hY','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0029','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('B2MkDAOtMwNctROqc9Z8XiVGqMRpTQhB','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002a','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('E2G3jlQv3Ow3hqb8PksaLfhic2Sj1BHg','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002b','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('vLUEiQjIafy3kOH6CrE1hiMiQIDgiGod','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001f','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('95o3JKdx8MWZuIS9ZuiuNW3MpbjsL2GO','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de0020','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('4Rg00NL0kj1GTTvZnYA5qkWKu5RFhHWU','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654de0021','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('mnJogX02hzNDItgayoiKbd4vdUEiduj6','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0022','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Zv6q4tqmtOGijgaZoUerKol9aBXiCWhd','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0023','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('pxaDffh7RFwQgWlI1FAEiFhio2E7J1mA','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0024','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('lvfZ6Kcyk2cwQdZyVjvEkGbpaOxpLnqn','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd0018','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('DGfpxGDSLqQpFYeYbD366u1q2D0hlx5f','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dd0019','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('g4MFQIwhjuW9Zh1RquBnFFYMKx3lk1Lf','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dd001a','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('VPKhkreE16UYNHgWIgW3bY3DfQiXJw2I','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd001b','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('0k9dtiJH6PwoqD2kxh5d70Bk3zwggMrI','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654de001c','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('r2Vqzc15Af4MSdITFgJg4A0c3tlaqjsj','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001d','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('u4YzkPqIe8TqluL4cpip5lZV9Ki1mRls','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654db000b','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('lVYfx1hseo9rWy3ABcpT8YLdDbaqO36B','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654db000c','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('RKGciibgBFVu2hMKyLHMPqkkuwD95iA1','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dc000d','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('b3byW4joQMuaVfyFkGMxS7jhlQsnzGqF','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dc000e','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('olA57FylyX0v1ZzjudszFRH83fTrh18T','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dc000f','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('1S0ESe4e6JHl1H17LuuPSADZI5phhYPl','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dc0010','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('RlkKIx1jw8WwmURsS13ldJd8A7ZUI3Ec','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dc0011','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ZFPuAj6842xid9fdCRXVvfFGO8wLqXNo','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dc0012','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('UMMbjx20cDmjAEkVuWGrpjw2HsEKXxVx','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654dc0013','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2exYx3aH90VbJF4nr4vwEQuuLBNQakU3','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0014','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('DoChJrgFBkZoZ4YCav4zKpwb1rYMrV23','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0015','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('OxOZqLHVGvAIc0cuR6KVrtxTqgdzh68t','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0016','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('leLABcXK2cIM3HqvUmegD7YjxYW2KsEj','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654db000b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('64paGVLKDhlW1JG8dCoeb532iF8hDzb8','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654db000c','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('uetYk6rsJ71GDwibpvGPbxUoh3W7ZDrQ','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dc000d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('D1nJYJg88NrFlIVReVVb3ObnIUaDvAAa','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dc000e','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('8ILXapMPM1XSj8ErOOaSsibfCDzqsocR','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dc000f','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('UcDMtmA0HqpRNOJIL8lcUlVrdmzuEjL7','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dc0010','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('mhDLLbl6auzTzdD6mdYVPfHpA6tzktld','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dc0011','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('fC6DFwee2BjbtsPf2vjru6Bl204vlYPy','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dc0012','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('m2H5yV53xlnqrTSnOeAnWHXX4dR1BCFf','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dc0013','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('DWYbffWhUmSgZ03cvkXG9cn9ZZ6OpVYV','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0014','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sJ93MSp4enmgWk0fpi6D10NONHTCohvn','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0015','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('j2vi4J9wQnHc9HDwIz1aLGgvm86UUQL7','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dc0016','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('TOO6RLHYnsL5LvzgfJVbsrMlNPsu00Jb','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('cwLdnb16T8sRvnxQf4Ur3jnA9Jfaw1gc','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654de001c','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('yIEk0qfpPDi5L6vKtgDixZnWejYHhiGL','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd001b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('pNQ2L8FTSsRzWEeUTClT7MzUadqIzmby','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654dd001a','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('m9yCMszvz755kkk6buSUmSCrnC0h9q4b','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654dd0019','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('I3nlhhYo2W8b6zxjDCvxBqGyeOuyjcrR','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654dd0018','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('89FybOvZgPu3zAZnl4IIk1AwoqfiseeU','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de001f','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('7YlH5C6UhcfWRKj7ScwOaYWvbLKIzJof','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654de0020','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('kN5Q1jOgjItz5dmjFM1pkzzZ894MZjuj','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654de0021','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('OWDST9eYs1Sh2TFz2jyylPc6YQ7Ho6BJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0022','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('SQWlgoFJYruNHUv48u6BWdZSgHLIgEl5','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0023','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('uNTeWgzNzqvpeduxWHVUasxZWwDsIs0E','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0024','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('t5Tq7vJgNByOQF4WqDUkzTQqHaXkduLu','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0026','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('nNF0823P4gw9pNDH38trUYOymBUDmVBa','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0027','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('yLQ7WVAEuUCp25KDfp2oppOwNLjwmKZA','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654df0028','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('44Bt5Qq5mxV6SZDZDeFQ4P215Nnxqcgd','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df0029','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('fP1uQaqCiFMao2jJP6YMkdX4P4P4Ubzu','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002a','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('p3R5gHuvwwMalpmKltY9aS5NFFmJ61fK','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('7f8F14PiBJEvJnzmYEZrowQp4LkNTd5v','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654df002d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('BMeyxpLHSutIQ5xTuRhHgYqpM3ZM65I5','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654df002e','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('FJwKIW719NKCS9dIcppeXb9IV0gxqENE','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e0002f','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('5rqZCwLPR71Y9UXi7NVZ3Nj7RCx23MLX','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00030','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('RXv4qBHsvWWPWH1bDDrwMhl3fDalufav','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00031','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('BF3djfquYsQS21IsNXBJs8dmUjkEYgil','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e00032','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('eSiCbCgPilBTWTHC7ClTgmoz803UtSO4','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00033','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('tH0xCy7CyH4djONDdGdHDDNyK0LiqzHV','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00034','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('oreNMilOf0g5gXJx3puukdMSlMHLDo1p','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00035','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('meunP0Wy6bfpbdiQEVLfQddxWqiI6dzs','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00037','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('q5IOuQeN1AH1RDEHDYz1yKnLNPTdiCcn','26818283525ce64b01525ce6551a0091','26818283525ce64b01525ce654e00038','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('t7rgUduZe1C1fwyhVKokWOHATGza98c1','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e00039','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('SifuUQnWS41C4lMoLyjI2d3w4F3v1Lp7','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003a','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('0ThIHR30Wl9VsrhvnDSr8dlldoWRsqXJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('B61met7Hd2H5st42bwHwbffbtpxZZywR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003c','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Np4Dwa264hNclS2rBvTBJiPWeYBRaQni','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e1003d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('0w3tfjv4xyadosdnpZ04yEOobUfaa9Xo','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e1003f','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('EDWcJ2BDliNWhccaYxLt4QhffgXlgYBZ','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10040','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('occX457hi5tU69KxqDwk5MvftNnsbdk8','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10041','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('22gZIKRtn5mHgKoxVQTcjB9MdRMIaJZq','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10042','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('b2URMUbQS90b0wUI2Zb6Rq1aLkVsAedq','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10043','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('XamTbEzQKHigVIygUDDk4CpgIgqptDk1','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10044','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('ZyrV6kkieJlGyBqEXzKTON9slHI4vJhO','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10045','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('BSLb81x9OEZo3OTUnLZ0OoaZ5hlrSsI7','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10046','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('zBrZYasxfA7Nc7VXw7Mtzjk3yzwMhXM1','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e10047','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2QtEVRI82ImGAP8sJGxE6s04Tjrx1VsJ','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e10048','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('MCrIXW4epipf1G5W6vzGf1CorWwQqFbp','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20049','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('SHWU4lDDusnKDMBK6wZkKGaJXf8tzPZi','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e2004a','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('qaNq59vbWPfmYj3BaiIQiKURRD0afkYY','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Em3OJ1zga1GkmKhvsSOdn25mZ9WbSyJm','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004c','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('zFwsrUlK0uDHucAGmq5vIeirpJVzMP15','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('HRflIUsLgwWywfrPO1PEpUVJyOSNyvMR','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004e','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('DGpj9kcHv7kK7CE53lTtAKrOHHfuAjYJ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e2004f','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('HxA3jYF3iJyMznbpYOLpp61OvW0Z4eHq','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20050','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('VEVKE4qRn7YQDy82K85yzNgNHqW5fqhW','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20051','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('NV2QgCh9MSukZfKy0K5CDCHrWOCwPmap','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20052','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('xtXemUxORj7iL37PLMlilVoA83K5lajK','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654e20055','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('6ekCQNUxPzCGQNLDooVzAJkP3pWfWSP6','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e20056','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Etq9D0u7Gy7kxyQ7RBM5m2RMEefhJsFQ','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30057','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('whHwaYBTwTPtN02x16NswrJZZ1mTtxet','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30058','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('JpCoAWHxV7o0yvyG3VEIptOLnlC0oMV2','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005a','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('2lSPrWVe2Nyq6kORqRbNXzSErbkaKgDL','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654e3005b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('SzwXcMVCpEWAbTjnTmtnqBX0Ql5Q0Aia','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005c','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('E9ZvJMzKRrGLEwp7BQXG72bOlJVB3pAC','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('uK6mqn6iIpZXsWzwt96bqZ3wLHMBq5Gk','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e3005e','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('0pnFDL1uxScH1QT15Ynwl1n0CwIaFJFF','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30060','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('VwrtWrFlWcsS1h8fqzBJIx33gxqaJpaN','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654e30061','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('sXklHKOlhcSy0DyvOewXidDNR530nLQg','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30062','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('h70ZcGqthB45AglICiqsZrfqKl5hKXHY','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30063','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('IMWpAAdsX4ZUCzmjx3qYddn0O83ejOwj','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30064','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('NoGjmDW4JPsZLgarNR7glUnE2AdafVle','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654e4006d','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('QYTyzQ07rzQVdtD87NHOPxKXqEwzERFw','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006e','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('69ltwgVsnzN1xAVV04pYF8jao7CuoaOG','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654e40067','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('WCmeTFlfYflFnq9G2Xi7Mt76sz9PFZrg','26818283525ce64b01525ce65517008e','26818283525ce64b01525ce654e40068','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('hgxSX3ozEwyOOWc5Aw1wGo1rOJ49uJ0G','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e40069','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('i46lr9GJ9PMVJOXeOSjLPcmkl6H3tilN','26818283525ce64b01525ce655190090','26818283525ce64b01525ce654e4006a','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('58FIyfDMuWrOvyqj065uaqCVNb0ueEYX','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e4006b','26818283525ce6b801525cea22d60003');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('IabJz8au6EgcAnYDY3nFQEHuQ42WJBjh','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70072','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('aJ53FYZ7Knvs76IgBov9i7QnC9UjECaX','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70074','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('r8WVJN7KhT3Od23X29B3BgZ6NsntYCtR','26818283525ce64b01525ce65518008f','26818283525ce64b01525ce654e70076','26818283525ce6b801525cea48360004');
insert  into `T_PERMIT`(`ID`,`OPERATION_ID`,`RESOURCE_ID`,`ROLE_ID`) values ('Y3ic4kewExvlofKfLf8l9OSGGalGvgcv','26818283525ce64b01525ce65515008c','26818283525ce64b01525ce654e30058','26818283525ce64b01525ce6551c0092');

/*Table structure for table `T_PERSON` */

DROP TABLE IF EXISTS `T_PERSON`;

CREATE TABLE `T_PERSON` (
  `ID` varchar(32) NOT NULL,
  `IDCARD` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `IP` varchar(60) DEFAULT NULL,
  `LOGIN_LAST_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LOGIN_LAST_IP` varchar(20) DEFAULT NULL,
  `NUMBER` varchar(60) DEFAULT NULL,
  `PASSWORD` varchar(60) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `TEL` varchar(60) DEFAULT NULL,
  `TRUE_NAME` varchar(60) DEFAULT NULL,
  `USER_NAME` varchar(60) DEFAULT NULL,
  `DEPT_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK43C945A0C2FBF4B2` (`DEPT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_PERSON` */

insert  into `T_PERSON`(`ID`,`IDCARD`,`CREATE_DATE`,`IP`,`LOGIN_LAST_DATE`,`LOGIN_LAST_IP`,`NUMBER`,`PASSWORD`,`STATUS`,`TEL`,`TRUE_NAME`,`USER_NAME`,`DEPT_ID`) values ('26818283525ce64b01525ce654390003',NULL,'2016-01-20 10:40:20',NULL,'2016-04-12 10:28:56','127.0.0.1','no:0001','e10adc3949ba59abbe56e057f20f883e',0,'13812345678','张三','zhang3','26818283525ce64b01525ce654380002');

/*Table structure for table `T_PERSON_ROLE` */

DROP TABLE IF EXISTS `T_PERSON_ROLE`;

CREATE TABLE `T_PERSON_ROLE` (
  `PERSON_ID` varchar(32) NOT NULL,
  `ROLE_ID` varchar(32) NOT NULL,
  KEY `FK86A26FB5EF529325` (`PERSON_ID`),
  KEY `FK86A26FB5DDDB5A45` (`ROLE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_PERSON_ROLE` */

insert  into `T_PERSON_ROLE`(`PERSON_ID`,`ROLE_ID`) values ('26818283525ce64b01525ce654390003','26818283525ce64b01525ce6551c0092');

/*Table structure for table `T_RESOURCE` */

DROP TABLE IF EXISTS `T_RESOURCE`;

CREATE TABLE `T_RESOURCE` (
  `ID` varchar(32) NOT NULL,
  `CLASS_NAME` varchar(60) DEFAULT NULL,
  `CODE` varchar(60) DEFAULT NULL,
  `ICON` varchar(60) DEFAULT NULL,
  `INTECEPTOR` varchar(200) DEFAULT NULL,
  `LINK` varchar(60) DEFAULT NULL,
  `NAME` varchar(60) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `MENU_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK47D00399C46CC5A5` (`MENU_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_RESOURCE` */

insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e31234','','MK0001','','/findMarkRecord.do','','查看笔迹档案',1,'26818283525ce64b01525ce654e20054');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e20056','','MK0002','','/delMaiorInfo.do','','删除笔迹档案',0,'26818283525ce64b01525ce654e20054');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e30057','','MK0003','','/saveMaiorInfo.do','','新增笔迹档案',0,'26818283525ce64b01525ce654e20054');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e30058','','MK0004','','/updateMaiorInfo.do','','修改笔迹档案',0,'26818283525ce64b01525ce654e20054');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e3005a','','MM0001','','/saveMajorMember.do','','添加人员信息',0,'26818283525ce64b01525ce654e30059');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e3005b','','MM0002','','/findMajorMember.do','','查询人员信息',1,'26818283525ce64b01525ce654e30059');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e3005c','','MM0003','','/removeMajorMember.do','','删除人员信息',0,'26818283525ce64b01525ce654e30059');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e3005d','','MM0004','','/modifyMajorMember.do','','修改人员信息',0,'26818283525ce64b01525ce654e30059');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e3005e','','MM0005','','/moveBlackMember.do','','人员转黑名单',0,'26818283525ce64b01525ce654e30059');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e30060','','MD0001','','/saveMajorDept.do','','添加组织信息',0,'26818283525ce64b01525ce654e3005f');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e30061','','MD0002','','/findMajorDept.do','','查询组织信息',1,'26818283525ce64b01525ce654e3005f');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e30062','','MD0003','','/removeMajorDept.do','','删除组织信息',0,'26818283525ce64b01525ce654e3005f');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e30063','','MD0004','','/modifyMajorDept.do','','修改组织信息',0,'26818283525ce64b01525ce654e3005f');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e30064','','MD0005','','/moveBlackDept.do','','组织转黑名单',0,'26818283525ce64b01525ce654e3005f');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70071','','A0001','','/getOrgs.do','','查看组织列表',1,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70072','','A0002','','/getOrgsTree.do','','查看组织菜单',1,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70073','','A0003','','/addOrg.do','','添加组织信息',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70074','','A0004','','/getDepts.do','','查看部门信息',1,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70075','','A0005','','/addDept.do','','添加部门信息',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70076','','A0006','','/getPersons.do','','查看人员信息',1,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70077','','A0007','','/addPerson.do','','添加人员信息',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70078','','A0011','','/updatePerson.do','','修改人员信息',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e70079','','A0008','','/delPerson.do','','删除人员信息',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e7007a','','A0009','','/getPersonsRoles.do','','设置人员角色',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e7007b','','A00012','','/deleteOrgAndDempt.do','','删除部门和组织',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e7007c','','A00013','','/updateOrg.do','','修改组织',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e7007d','','A00014','','/updateDept.do','','修改部门',0,'26818283525ce64b01525ce654e50070');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e8007f','','D0001','','/findMenuTree.do','','查看所有菜单',0,'26818283525ce64b01525ce654e7007e');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e80080','','D0002','','/role/findAll.do','','查看所有角色',0,'26818283525ce64b01525ce654e7007e');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e80081','','D0003','','/role/addRole.do','','添加角色',0,'26818283525ce64b01525ce654e7007e');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e80082','','D0004','','/role/delRole.do','','删除角色信息',0,'26818283525ce64b01525ce654e7007e');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e80083','','D0005','','/getres.do','','查看权限',0,'26818283525ce64b01525ce654e7007e');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e80084','','D0006','','/setPermit.do','','保存权限',0,'26818283525ce64b01525ce654e7007e');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e80086','','L0001','','/findLog.do','','查询日志',0,'26818283525ce64b01525ce654e80085');
insert  into `T_RESOURCE`(`ID`,`CLASS_NAME`,`CODE`,`ICON`,`INTECEPTOR`,`LINK`,`NAME`,`TYPE`,`MENU_ID`) values ('26818283525ce64b01525ce654e80087','','A0010','','/systemMenus.do','','查看系统设置菜单',0,'26818283525ce64b01525ce654e80085');

/*Table structure for table `T_ROLE` */

DROP TABLE IF EXISTS `T_ROLE`;

CREATE TABLE `T_ROLE` (
  `ID` varchar(32) NOT NULL,
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `NAME` varchar(60) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `T_ROLE` */

insert  into `T_ROLE`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`) values ('26818283525ce64b01525ce6551c0092','2016-01-20 10:39:04','系统管理员',1);
insert  into `T_ROLE`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`) values ('26818283525ce6b801525ce9f7bf0002','2016-01-20 10:43:03','组织管理员',0);
insert  into `T_ROLE`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`) values ('26818283525ce6b801525cea22d60003','2016-01-20 10:43:14','部门管理员',0);
insert  into `T_ROLE`(`ID`,`CREATE_DATE`,`NAME`,`STATUS`) values ('26818283525ce6b801525cea48360004','2016-01-20 10:43:23','个人',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
