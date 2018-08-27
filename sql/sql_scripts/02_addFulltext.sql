CREATE TABLE `mydb`.`stopwords` (value varchar(255) NOT NULL PRIMARY KEY);
SET GLOBAL innodb_ft_server_stopword_table = 'mydb/stopwords';

ALTER TABLE `mydb`.`Product` ADD FULLTEXT (`name`, `description`) WITH PARSER ngram;
SET GLOBAL innodb_ft_aux_table = 'mydb/Product';
