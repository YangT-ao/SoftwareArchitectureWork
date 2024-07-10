/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : eladmin

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 11/07/2024 00:38:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for big_changes
-- ----------------------------
DROP TABLE IF EXISTS `big_changes`;
CREATE TABLE `big_changes`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `e_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '涉及企业名称',
  `e_level` int NULL DEFAULT NULL COMMENT '涉及企业层级',
  `risk_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '风险事件名称',
  `time` datetime NULL DEFAULT NULL COMMENT '事件发生时间',
  `is_abroad` tinyint NULL DEFAULT NULL COMMENT '是否境外',
  `is_lawsuit` tinyint NULL DEFAULT NULL COMMENT '是否涉诉',
  `r_category` tinyint NULL DEFAULT NULL COMMENT '风险类别',
  `r_sub_type` tinyint NULL DEFAULT NULL COMMENT '风险子类别',
  `money` int NULL DEFAULT NULL COMMENT '损失金额',
  `progress` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '处置进展情况',
  `s_description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '当期情况描述',
  `belong_progress` tinyint NULL DEFAULT NULL COMMENT '所属的changes',
  `sub_time` datetime NULL DEFAULT NULL COMMENT '续报提交时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sname`(`e_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of big_changes
-- ----------------------------
INSERT INTO `big_changes` VALUES (1, '长沙市政府', 1, '2024年财务数据不准确', '2024-03-13 00:00:00', 1, 1, 1, 1, 50000, '正在处理', '好', 1, '2024-04-13 00:00:00');
INSERT INTO `big_changes` VALUES (2, '长沙市中医院', 1, '2024年财务数据不准确', '2024-03-19 00:00:00', 1, 1, 1, 2, 30000, '正在处理', '好', 1, '2024-03-13 00:00:00');
INSERT INTO `big_changes` VALUES (3, '长沙企业联合会', 3, '2024年财务数据不准确', '2024-03-26 00:00:00', 1, 1, 1, 3, 25000, '待处理', '好', 1, '2024-05-13 00:00:00');
INSERT INTO `big_changes` VALUES (4, '长沙市中心医院', 1, '2024年财务数据不准确', '2024-04-05 00:00:00', 1, 1, 1, 1, 40000, '正在处理', '好', 1, '2024-06-13 00:00:00');
INSERT INTO `big_changes` VALUES (5, '长沙市社保局', 2, '2024年财务数据不准确', '2024-04-09 00:00:00', 1, 1, 1, 2, 35000, '正在处理', '好', 1, '2024-07-13 00:00:00');
INSERT INTO `big_changes` VALUES (6, '长沙市人力资源局', 1, '2024年财务数据不准确', '2024-04-18 00:00:00', 1, 1, 1, 3, 15000, '待处理', '好', 1, '2024-08-13 00:00:00');

SET FOREIGN_KEY_CHECKS = 1;
