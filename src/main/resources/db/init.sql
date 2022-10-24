/*

 Source Server         : crm local
 Source Server Type    : PostgreSQL
 Source Server Version : 140004
 Source Host           : localhost:5432
 Source Catalog        : test_sample
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140004
 File Encoding         : 65001

 Date: 24/10/2022 22:29:21
*/
-- DROP DATABASE IF EXISTS test_sample;
-- CREATE DATABASE test_sample;
-- ----------------------------
-- Sequence structure for refresh_token_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "refresh_token_id_seq";
CREATE SEQUENCE "refresh_token_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for roles_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "roles_id_seq";
CREATE SEQUENCE "roles_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for tokens_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "tokens_id_seq";
CREATE SEQUENCE "tokens_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for users_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "users_id_seq";
CREATE SEQUENCE "users_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Table structure for refresh_token
-- ----------------------------
DROP TABLE IF EXISTS "refresh_token";
CREATE TABLE "refresh_token"
(
    "id"           int8         NOT NULL DEFAULT nextval('refresh_token_id_seq'::regclass),
    "expiry_date"  timestamp(6) NOT NULL,
    "token"        text NOT NULL,
    "user_id"      int8,
    "created_by"   int8         NOT NULL DEFAULT 0,
    "created_date" timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
    "modified_by"  int8,
    "updated_date" timestamp(6)
)
;

-- ----------------------------
-- Records of refresh_token
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS "roles";
CREATE TABLE "roles"
(
    "id"           int8         NOT NULL DEFAULT nextval('roles_id_seq'::regclass),
    "name"         varchar(255),
    "user_id"      int8,
    "created_by"   int8         NOT NULL DEFAULT 0,
    "created_date" timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
    "modified_by"  int8,
    "updated_date" timestamp(6)
)
;

-- ----------------------------
-- Records of roles
-- ----------------------------

-- ----------------------------
-- Table structure for tokens
-- ----------------------------
DROP TABLE IF EXISTS "tokens";
CREATE TABLE "tokens"
(
    "id"               int8         NOT NULL DEFAULT nextval('tokens_id_seq'::regclass),
    "token"            text,
    "refresh_token_id" int8,
    "created_by"       int8         NOT NULL DEFAULT 0,
    "created_date"     timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
    "modified_by"      int8,
    "updated_date"     timestamp(6)
)
;

-- ----------------------------
-- Records of tokens
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "users";
CREATE TABLE "users"
(
    "id"           int8         NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    "email"        varchar(255),
    "password"     varchar(255) NOT NULL,
    "username"     varchar(255) NOT NULL,
    "created_by"   int8         NOT NULL DEFAULT 0,
    "created_date" timestamp(6) NOT NULL DEFAULT CURRENT_DATE,
    "modified_by"  int8,
    "updated_date" timestamp(6)
)
;

-- ----------------------------
-- Records of users
-- ----------------------------

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "refresh_token_id_seq"
    OWNED BY "refresh_token"."id";
SELECT setval('refresh_token_id_seq', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "roles_id_seq"
    OWNED BY "roles"."id";
SELECT setval('roles_id_seq', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "tokens_id_seq"
    OWNED BY "tokens"."id";
SELECT setval('tokens_id_seq', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "users_id_seq"
    OWNED BY "users"."id";
SELECT setval('users_id_seq', 2, false);

-- ----------------------------
-- Uniques structure for table refresh_token
-- ----------------------------
ALTER TABLE "refresh_token"
    ADD CONSTRAINT "uk_r4k4edos30bx9neoq81mdvwph" UNIQUE ("token");

-- ----------------------------
-- Primary Key structure for table refresh_token
-- ----------------------------
ALTER TABLE "refresh_token"
    ADD CONSTRAINT "refresh_token_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table roles
-- ----------------------------
ALTER TABLE "roles"
    ADD CONSTRAINT "roles_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tokens
-- ----------------------------
ALTER TABLE "tokens"
    ADD CONSTRAINT "tokens_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE "users"
    ADD CONSTRAINT "users_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table refresh_token
-- ----------------------------
ALTER TABLE "refresh_token"
    ADD CONSTRAINT "refresh_token_fk_1" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table roles
-- ----------------------------
ALTER TABLE "roles"
    ADD CONSTRAINT "roles_fk_1" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table tokens
-- ----------------------------
ALTER TABLE "tokens"
    ADD CONSTRAINT "tokens_fk_1" FOREIGN KEY ("refresh_token_id") REFERENCES "refresh_token" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;


-- insert data to users
INSERT INTO users(username, "password", email)
VALUES ('admin', '$2a$10$pMLslcjCbbFNO0layBSfzOOIIOotpyb.vB1yEEEeJUd0/DNtv8ubG', 'admin@email.com')