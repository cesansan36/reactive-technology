CREATE TABLE IF NOT EXISTS `technology` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `capability_technology` (
     `id` BIGINT NOT NULL AUTO_INCREMENT,
     `technology_id` BIGINT NOT NULL,
     `capability_id` BIGINT NOT NULL,
     PRIMARY KEY (`id`),
     UNIQUE INDEX `captecrel` (`technology_id` ASC, `capability_id` ASC) VISIBLE,
     CONSTRAINT `rela`
       FOREIGN KEY (`technology_id`)
       REFERENCES `wf_technology`.`technology` (`id`)
       ON DELETE RESTRICT
       ON UPDATE RESTRICT);
