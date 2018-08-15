ALTER TABLE `mydb`.`Product`
  ADD FULLTEXT (`name`, `description`) WITH PARSER ngram
