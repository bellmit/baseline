/*
 Navicat Premium Data Transfer

 Source Server         : localhost-pg
 Source Server Type    : PostgreSQL
 Source Server Version : 110008
 Source Host           : localhost:5432
 Source Catalog        : testdb
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 110008
 File Encoding         : 65001

 Date: 17/11/2020 17:35:50
*/


-- ----------------------------
-- Table structure for ge
-- ----------------------------
DROP TABLE IF EXISTS "public"."ge";
CREATE TABLE "public"."ge" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "v" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "deleted" bool,
  "v1" int4,
  "v3" int8,
  "v2" jsonb
)
;
COMMENT ON COLUMN "public"."ge"."id" IS '主键id';
COMMENT ON COLUMN "public"."ge"."v" IS '值域';
COMMENT ON COLUMN "public"."ge"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ge"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ge"."deleted" IS '逻辑删除标记';
COMMENT ON TABLE "public"."ge" IS '这是表注释';

-- ----------------------------
-- Primary Key structure for table ge
-- ----------------------------
ALTER TABLE "public"."ge" ADD CONSTRAINT "ge_pkey" PRIMARY KEY ("id");
