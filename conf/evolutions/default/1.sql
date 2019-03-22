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

create table profile_traveller_type (
  profile_id                    bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_profile_traveller_type primary key (profile_id,traveller_type_id)
);

create table traveller_type (
  id                            bigint auto_increment not null,
  traveller_type                varchar(255),
  description                   varchar(255),
  img_url                       varchar(255),
  constraint pk_traveller_type primary key (id)
);

create index ix_passport_profile_id on passport (profile_id);
alter table passport add constraint fk_passport_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_passport_nationality_id on passport (nationality_id);
alter table passport add constraint fk_passport_nationality_id foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;

create index ix_profile_nationality_profile on profile_nationality (profile_id);
alter table profile_nationality add constraint fk_profile_nationality_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_nationality_nationality on profile_nationality (nationality_id);
alter table profile_nationality add constraint fk_profile_nationality_nationality foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;

create index ix_profile_traveller_type_profile on profile_traveller_type (profile_id);
alter table profile_traveller_type add constraint fk_profile_traveller_type_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_traveller_type_traveller_type on profile_traveller_type (traveller_type_id);
alter table profile_traveller_type add constraint fk_profile_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;


create table destination (
  id                            integer not null,
  name                          varchar(255),
  type                          integer,
  district                      varchar(255),
  latitude                      double not null,
  longitude                     double not null,
  country                       varchar(255),
  constraint ck_destination_type check ( type in (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97)),
  constraint pk_destination primary key (id)
);

create table profile (
  id                            integer not null,
  username                      varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  middle_name                   varchar(255),
  last_name                     varchar(255),
  gender                        varchar(255),
  date_of_birth                 date,
  date_of_creation              timestamp,
  constraint pk_profile primary key (id)
);


# --- !Downs

drop table if exists destination;

drop table if exists profile;

