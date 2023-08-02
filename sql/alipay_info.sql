/*
 Navicat Premium Data Transfer

 Source Server         : czq
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : api-third-party

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 02/08/2023 19:44:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alipay_info
-- ----------------------------
DROP TABLE IF EXISTS `alipay_info`;
CREATE TABLE `alipay_info`  (
  `orderNumber` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单id',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易名称',
  `totalAmount` float(10, 2) NOT NULL COMMENT '交易金额',
  `buyerPayAmount` float(10, 2) NOT NULL COMMENT '买家付款金额',
  `buyerId` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '买家在支付宝的唯一id',
  `tradeNo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付宝交易凭证号',
  `tradeStatus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易状态',
  `gmtPayment` datetime(0) NOT NULL COMMENT '买家付款时间',
  PRIMARY KEY (`orderNumber`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
