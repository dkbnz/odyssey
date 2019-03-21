# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE c_fk_name CHAR(255);
  DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
      AND REFERENCED_TABLE_NAME IS NOT NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN curs;

  read_loop: LOOP
    FETCH curs INTO c_fk_name;
    IF done THEN
      LEAVE read_loop;
    END IF;
    SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP FOREIGN KEY ', c_fk_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
  END LOOP;

  CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
  SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP COLUMN ', p_column_name);
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
END
$$
create table nationality (
  id                            bigint auto_increment not null,
  nationality                   varchar(255),
  country                       varchar(255),
  constraint pk_nationality primary key (id)
);

create table passport (
  id                            bigint auto_increment not null,
  issue_country                 varchar(255),
  profile_id                    bigint,
  nationality_id                bigint,
  constraint pk_passport primary key (id)
);

create table profile (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  middle_name                   varchar(255),
  last_name                     varchar(255),
  gender                        varchar(255),
  date_of_birth                 date,
  date_of_creation              datetime(6),
  constraint pk_profile primary key (id)
);

create table profile_nationality (
  profile_id                    bigint not null,
  nationality_id                bigint not null,
  constraint pk_profile_nationality primary key (profile_id,nationality_id)
);

create index ix_passport_profile_id on passport (profile_id);
alter table passport add constraint fk_passport_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_passport_nationality_id on passport (nationality_id);
alter table passport add constraint fk_passport_nationality_id foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;

create index ix_profile_nationality_profile on profile_nationality (profile_id);
alter table profile_nationality add constraint fk_profile_nationality_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_nationality_nationality on profile_nationality (nationality_id);
alter table profile_nationality add constraint fk_profile_nationality_nationality foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;


# --- !Downs

alter table passport drop foreign key fk_passport_profile_id;
drop index ix_passport_profile_id on passport;

alter table passport drop foreign key fk_passport_nationality_id;
drop index ix_passport_nationality_id on passport;

alter table profile_nationality drop foreign key fk_profile_nationality_profile;
drop index ix_profile_nationality_profile on profile_nationality;

alter table profile_nationality drop foreign key fk_profile_nationality_nationality;
drop index ix_profile_nationality_nationality on profile_nationality;

drop table if exists nationality;

drop table if exists passport;

drop table if exists profile;

drop table if exists profile_nationality;

