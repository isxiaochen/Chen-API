/*
 Navicat Premium Data Transfer

 Source Server         : czq
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : apiorder

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 02/08/2023 19:43:50
*/

create database api_order
-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userId` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `interfaceId` bigint(0) NULL DEFAULT NULL COMMENT '接口id',
  `count` int(0) NULL DEFAULT NULL COMMENT '购买数量',
  `totalAmount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单应付价格',
  `status` int(0) NULL DEFAULT 0 COMMENT '订单状态 0-未支付 1 -已支付 2-超时支付',
  `isDelete` int(0) NOT NULL DEFAULT 1 COMMENT '0-删除 1 正常',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `orderSn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `charging` float(255, 2) NOT NULL COMMENT '单价',
  PRIMARY KEY (`id`, `isDelete`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (15, 2, 3, 3, 1.56, 2, 1, '2023-07-25 22:07:41', '2023-07-25 22:37:41', '202307252207411121683650182', 0.52);
INSERT INTO `t_order` VALUES (16, 2, 3, 5, 2.60, 2, 1, '2023-07-26 10:54:44', '2023-07-26 11:24:44', '202307261054442441684843362', 0.52);
INSERT INTO `t_order` VALUES (17, 2, 3, 5, 2.60, 2, 1, '2023-07-27 10:41:23', '2023-07-27 11:11:23', '202307271041238361684784792', 0.52);
INSERT INTO `t_order` VALUES (18, 2, 3, 4, 2.08, 2, 1, '2023-07-27 10:49:59', '2023-07-27 11:19:59', '202307271049590321684581542', 0.52);
INSERT INTO `t_order` VALUES (19, 2, 3, 7, 3.64, 2, 1, '2023-07-27 10:54:49', '2023-07-27 11:24:49', '202307271054492741684256702', 0.52);
INSERT INTO `t_order` VALUES (20, 2, 3, 5, 2.60, 1, 1, '2023-07-27 10:58:51', '2023-07-27 10:59:13', '202307271058519231684749622', 0.52);
INSERT INTO `t_order` VALUES (21, 2, 3, 8, 4.16, 2, 1, '2023-07-27 19:59:43', '2023-07-27 20:29:43', '202307271959431931684457622', 0.52);
INSERT INTO `t_order` VALUES (22, 2, 3, 5, 2.60, 1, 1, '2023-07-27 20:04:23', '2023-07-27 20:04:52', '202307272004235901684542192', 0.52);
INSERT INTO `t_order` VALUES (23, 2, 3, 5, 2.60, 2, 1, '2023-07-29 22:48:10', '2023-07-30 16:28:34', '202307292248100371685925582', 0.52);
INSERT INTO `t_order` VALUES (24, 2, 3, 5, 2.60, 2, 1, '2023-07-29 22:52:47', '2023-07-30 16:28:34', '202307292252478741685505202', 0.52);
INSERT INTO `t_order` VALUES (25, 1677647739169431556, 3, 1, 0.52, 0, 1, '2023-08-02 19:31:39', '2023-08-02 19:31:39', '202308021931398741686669651677647739169431556', 0.52);
