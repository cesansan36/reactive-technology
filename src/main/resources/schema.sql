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
  UNIQUE INDEX `captechrel` (`technology_id` ASC, `capability_id` ASC) INVISIBLE);
