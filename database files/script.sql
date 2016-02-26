BEGIN TRANSACTION;
CREATE TABLE tour_tourists(tourist_id INTEGER PRIMARY KEY,tour_id INTEGER,tourist_name TEXT, tourist_descr TEXT);
INSERT INTO `tour_tourists` VALUES (1,1,'Огранизтор','я');
CREATE TABLE tour_items(id INTEGER PRIMARY KEY,tour_id INTEGER,tourist_id INTEGER,tour_article_id INTEGER,tour_curr_id INTEGER,tour_curr_amount FLOAT,tour_day INTEGER,tour_date DATETIME DEFAULT CURRENT_TIMESTAMP,tour_descr TEXT, tour_item_type INTEGER DEFAULT 0);
INSERT INTO `tour_items` VALUES (1,1,1,1,1,5555.0,0,'2014-12-25 17:36:39','eda',0);
INSERT INTO `tour_items` VALUES (2,1,1,1,2,5.0,0,'2014-12-25 17:36:49','yyyy',0);
INSERT INTO `tour_items` VALUES (3,1,1,2,2,25.0,0,'2014-12-25 17:37:03','huooo',0);
INSERT INTO `tour_items` VALUES (4,1,1,3,1,5000.0,0,'2014-12-25 17:37:14','uuuuu',0);
INSERT INTO `tour_items` VALUES (5,1,1,3,2,34.0,0,'2014-12-25 17:37:24','4444',0);
CREATE TABLE tour_curr(id INTEGER PRIMARY KEY,name TEXT,digit_code INTEGER, word_code TEXT);
INSERT INTO `tour_curr` VALUES (1,'Руб.',810,'RUR');
INSERT INTO `tour_curr` VALUES (2,'Дол. США',840,'USD');
INSERT INTO `tour_curr` VALUES (3,'Евро',978,'EUR');
INSERT INTO `tour_curr` VALUES (4,'Нельские руппи',524,'NPR');
INSERT INTO `tour_curr` VALUES (5,'Норвежская крона',578,'NOK');
INSERT INTO `tour_curr` VALUES (6,'Лари',981,'GEL');
CREATE TABLE tour_buy_article(id INTEGER PRIMARY KEY,name TEXT);
INSERT INTO `tour_buy_article` VALUES (1,'еда');
INSERT INTO `tour_buy_article` VALUES (2,'жилье');
INSERT INTO `tour_buy_article` VALUES (3,'дорога');
INSERT INTO `tour_buy_article` VALUES (4,'пермиты');
INSERT INTO `tour_buy_article` VALUES (5,'персонал');
INSERT INTO `tour_buy_article` VALUES (6,'другое');
CREATE TABLE tour(id INTEGER PRIMARY KEY,name TEXT,tourist_cnt INTEGER,date_begin DATETIME DEFAULT CURRENT_TIMESTAMP, date_end DATETIME DEFAULT CURRENT_TIMESTAMP ,tour_type INTEGER DEFAULT 0);
INSERT INTO `tour` VALUES (1,'tour1',1,'2014-12-25 00:00:00',NULL,0);
CREATE TABLE curr_rates(currate_id INTEGER PRIMARY KEY,tour_id INTEGER,curr1_id INTEGER,curr2_id INTEGER, val1_id FLOAT, val2_id FLOAT, currrate_date DATETIME DEFAULT CURRENT_TIMESTAMP);
INSERT INTO `curr_rates` VALUES (1,1,1,2,54.0,1.0,'2014-12-25 17:36:14');
CREATE TABLE android_metadata (locale TEXT);
INSERT INTO `android_metadata` VALUES ('en_US');
COMMIT;