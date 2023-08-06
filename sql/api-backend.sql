/*
 Navicat Premium Data Transfer

 Source Server         : czq
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : api-backend

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 02/08/2023 19:43:03
*/

use api_backend;
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for interface_charging
-- ----------------------------
DROP TABLE IF EXISTS `interface_charging`;
CREATE TABLE `interface_charging`  (
                                       `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `interfaceId` bigint(0) NOT NULL COMMENT '接口id',
                                       `charging` float(255, 2) NOT NULL COMMENT '计费规则（元/条）',
                                       `availablePieces` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口剩余可调用次数',
                                       `userId` bigint(0) NOT NULL COMMENT '创建人',
                                       `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                       `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                       `isDelete` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否删除(0-删除 1-正常)',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of interface_charging
-- ----------------------------
INSERT INTO `interface_charging` VALUES (1, 3, 0.52, '977', 1, '2023-07-24 14:33:49', '2023-08-02 19:31:39', 1);

-- ----------------------------
-- Table structure for interface_info
-- ----------------------------
DROP TABLE IF EXISTS `interface_info`;
CREATE TABLE `interface_info`  (
                                   `id` bigint(0) NOT NULL AUTO_INCREMENT,
                                   `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
                                   `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口描述',
                                   `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口地址',
                                   `method` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求类型',
                                   `requestHeader` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求头',
                                   `responseHeader` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '响应头',
                                   `status` tinyint(0) NULL DEFAULT 1 COMMENT '接口状态 0 关闭，1启用',
                                   `isDelete` int(0) NULL DEFAULT 1 COMMENT '逻辑删除 0 删除，1正常',
                                   `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
                                   `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
                                   `userId` bigint(0) NULL DEFAULT NULL,
                                   `requestParams` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
                                   `sdk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口对应的SDK类路径',
                                   `parameterExample` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数示例',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interface_info
-- ----------------------------
INSERT INTO `interface_info` VALUES (3, 'getName', '获取用户名', 'http://localhost:8123/api/interface/name/user', 'post', '{\"Content-Type\":\"application/json\"}', '{\"Content-Type\":\"application/json\"}', 1, 1, '2023-07-08 20:00:14', '2023-07-08 20:00:14', 1, '\n{\"name\":\"name\",\"type\":\"string\"}\n', 'com.czq.apiclientsdk.client.NameApiClient', '{ \"name\": \"czq\"}');
INSERT INTO `interface_info` VALUES (4, 'getRandomWork', '随机文本', 'http://localhost:8123/api/interface/random/word', 'get', NULL, NULL, 1, 1, '2023-07-29 20:33:55', '2023-07-29 20:33:55', 1, '', 'com.czq.apiclientsdk.client.RandomApiClient', NULL);
INSERT INTO `interface_info` VALUES (5, 'getRandomImageUrl', '随机动漫图片地址', 'http://localhost:8123/api/interface/random/image', 'post', NULL, NULL, 1, 1, '2023-07-29 21:51:08', '2023-07-29 21:51:08', 1, NULL, 'com.czq.apiclientsdk.client.RandomApiClient', NULL);
INSERT INTO `interface_info` VALUES (6, 'getDayWallpaperUrl', '每日壁纸URL', 'http://localhost:8123/api/interface/day/wallpaper', 'post', NULL, NULL, 1, 1, '2023-07-29 22:20:10', '2023-07-29 22:20:10', 1, NULL, 'com.czq.apiclientsdk.client.DayApiClient', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `userAccount` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
                         `userPassword` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
                         `unionId` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信开放平台id',
                         `mpOpenId` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号openId',
                         `userName` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
                         `userAvatar` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
                         `userProfile` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户简介',
                         `userRole` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
                         `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                         `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                         `isDelete` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否删除',
                         `accessKey` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ak',
                         `secretKey` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'sk',
                         `phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
                         `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ邮箱',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1677647739169431556 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'xiaochen', '37c639816ab8a12a4e9d8d65d86d04c7', NULL, NULL, 'xiaochen', NULL, NULL, 'admin', '2023-07-05 20:04:29', '2023-08-02 16:29:09', 1, '8817b9099f85326da4e633054ff19d93', '7132425bf5aac84e7174342df5dae754', NULL, '');
INSERT INTO `user` VALUES (2, 'chen', '37c639816ab8a12a4e9d8d65d86d04c7', NULL, NULL, 'chen', NULL, NULL, 'user', '2023-07-08 19:56:04', '2023-08-02 15:20:00', 1, '163c5da70c75caf832d3109e0753db93', 'f15a031ee34532019827b0283d725e1b', NULL, '');
INSERT INTO `user` VALUES (1677647739169431556, 'test', '37c639816ab8a12a4e9d8d65d86d04c7', NULL, NULL, 'test', NULL, NULL, 'user', '2023-08-02 15:52:45', '2023-08-02 16:33:50', 1, '36b13efe755a0df7250a780cab4d7766', 'f47a668788954af7e09ad4396b0bbdb7', NULL, '2473221807@qq.com');

-- ----------------------------
-- Table structure for user_interface_info
-- ----------------------------
DROP TABLE IF EXISTS `user_interface_info`;
CREATE TABLE `user_interface_info`  (
                                        `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `userId` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
                                        `interfaceInfoId` bigint(0) NULL DEFAULT NULL COMMENT '调用接口id',
                                        `totalNum` int(0) NULL DEFAULT 0 COMMENT '接口的总调用次数',
                                        `leftNum` int(0) NULL DEFAULT NULL COMMENT '接口剩余调用次数',
                                        `isDelete` int(0) NULL DEFAULT 1 COMMENT '逻辑删除 0 删除 1 正常',
                                        `status` int(0) NULL DEFAULT 1 COMMENT '0 禁止调用 1 允许调用',
                                        `createTime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                        `updateTime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                        `version` int(0) NULL DEFAULT 0 COMMENT '乐观锁版本号',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_interface_info
-- ----------------------------
INSERT INTO `user_interface_info` VALUES (1, 1, 3, 2, 98, 1, 1, '2023-08-02 17:11:46', '2023-08-02 17:11:46', 17);
INSERT INTO `user_interface_info` VALUES (7, 2, 3, 2, 22, 1, 1, '2023-08-02 17:11:48', '2023-08-02 17:11:48', 6);
INSERT INTO `user_interface_info` VALUES (8, 1, 4, 1, 189, 1, 1, '2023-08-02 17:10:02', '2023-08-02 17:10:02', 11);
INSERT INTO `user_interface_info` VALUES (9, 1, 5, 1, 98, 1, 1, '2023-08-02 17:10:34', '2023-08-02 17:10:34', 2);
INSERT INTO `user_interface_info` VALUES (10, 1, 6, 0, 99, 1, 1, '2023-08-02 17:10:20', '2023-08-02 17:10:20', 1);
INSERT INTO `user_interface_info` VALUES (11, 2, 4, 0, 99, 1, 1, '2023-08-02 17:10:20', '2023-08-02 17:10:20', 1);
INSERT INTO `user_interface_info` VALUES (12, 2, 5, 0, 98, 1, 1, '2023-08-02 17:10:22', '2023-08-02 17:10:22', 2);
