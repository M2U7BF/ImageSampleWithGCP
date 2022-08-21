/* DB作成 */
--DROP DATABASE IF EXISTS orderdb;
--CREATE DATABASE orderdb CHARACTER SET utf8 COLLATE utf8_general_ci;
--
--use orderdb;

DROP TABLE IF EXISTS image_data;

/* ユーザーマスタ作成 */
CREATE TABLE image_data
(
 id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
 name VARCHAR(30)
)