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
create table destination (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  type_id                       bigint,
  district                      varchar(255),
  latitude                      double not null,
  longitude                     double not null,
  country                       varchar(255),
  constraint pk_destination primary key (id)
);

create table destination_type (
  id                            bigint auto_increment not null,
  destination_type              varchar(255),
  constraint pk_destination_type primary key (id)
);

create table nationality (
  id                            bigint auto_increment not null,
  nationality                   varchar(255),
  country                       varchar(255),
  constraint pk_nationality primary key (id)
);

create table passport (
  id                            bigint auto_increment not null,
  country                       varchar(255),
  constraint pk_passport primary key (id)
);

create table photo (
  id                            bigint auto_increment not null,
  photo_filename                varchar(255),
  thumbnail_filename            varchar(255),
  upload_date                   date,
  profile_id                    bigint,
  constraint pk_photo primary key (id)
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
  is_admin                      tinyint(1),
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

create table profile_passport (
  profile_id                    bigint not null,
  passport_id                   bigint not null,
  constraint pk_profile_passport primary key (profile_id,passport_id)
);

create table profile_photo (
  id                            bigint auto_increment not null,
  is_public                     tinyint(1),
  constraint pk_profile_photo primary key (id)
);

create table traveller_type (
  id                            bigint auto_increment not null,
  traveller_type                varchar(255),
  description                   varchar(255),
  img_url                       varchar(255),
  constraint pk_traveller_type primary key (id)
);

create table trip (
  id                            bigint auto_increment not null,
  profile_id                    bigint not null,
  name                          varchar(255),
  constraint pk_trip primary key (id)
);

create table trip_destination (
  id                            bigint auto_increment not null,
  start_date                    date,
  end_date                      date,
  list_order                    integer not null,
  trip_id                       bigint,
  destination_id                bigint,
  constraint pk_trip_destination primary key (id)
);

create index ix_destination_type_id on destination (type_id);
alter table destination add constraint fk_destination_type_id foreign key (type_id) references destination_type (id) on delete restrict on update restrict;

create index ix_photo_profile_id on photo (profile_id);
alter table photo add constraint fk_photo_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_nationality_profile on profile_nationality (profile_id);
alter table profile_nationality add constraint fk_profile_nationality_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_nationality_nationality on profile_nationality (nationality_id);
alter table profile_nationality add constraint fk_profile_nationality_nationality foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;

create index ix_profile_traveller_type_profile on profile_traveller_type (profile_id);
alter table profile_traveller_type add constraint fk_profile_traveller_type_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_traveller_type_traveller_type on profile_traveller_type (traveller_type_id);
alter table profile_traveller_type add constraint fk_profile_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_profile_passport_profile on profile_passport (profile_id);
alter table profile_passport add constraint fk_profile_passport_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_passport_passport on profile_passport (passport_id);
alter table profile_passport add constraint fk_profile_passport_passport foreign key (passport_id) references passport (id) on delete restrict on update restrict;

create index ix_trip_profile_id on trip (profile_id);
alter table trip add constraint fk_trip_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_trip_destination_trip_id on trip_destination (trip_id);
alter table trip_destination add constraint fk_trip_destination_trip_id foreign key (trip_id) references trip (id) on delete restrict on update restrict;

create index ix_trip_destination_destination_id on trip_destination (destination_id);
alter table trip_destination add constraint fk_trip_destination_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;


# --- !Downs

alter table destination drop foreign key fk_destination_type_id;
drop index ix_destination_type_id on destination;

alter table photo drop foreign key fk_photo_profile_id;
drop index ix_photo_profile_id on photo;

alter table profile_nationality drop foreign key fk_profile_nationality_profile;
drop index ix_profile_nationality_profile on profile_nationality;

alter table profile_nationality drop foreign key fk_profile_nationality_nationality;
drop index ix_profile_nationality_nationality on profile_nationality;

alter table profile_traveller_type drop foreign key fk_profile_traveller_type_profile;
drop index ix_profile_traveller_type_profile on profile_traveller_type;

alter table profile_traveller_type drop foreign key fk_profile_traveller_type_traveller_type;
drop index ix_profile_traveller_type_traveller_type on profile_traveller_type;

alter table profile_passport drop foreign key fk_profile_passport_profile;
drop index ix_profile_passport_profile on profile_passport;

alter table profile_passport drop foreign key fk_profile_passport_passport;
drop index ix_profile_passport_passport on profile_passport;

alter table trip drop foreign key fk_trip_profile_id;
drop index ix_trip_profile_id on trip;

alter table trip_destination drop foreign key fk_trip_destination_trip_id;
drop index ix_trip_destination_trip_id on trip_destination;

alter table trip_destination drop foreign key fk_trip_destination_destination_id;
drop index ix_trip_destination_destination_id on trip_destination;

drop table if exists destination;

drop table if exists destination_type;

drop table if exists nationality;

drop table if exists passport;

drop table if exists photo;

drop table if exists profile;

drop table if exists profile_nationality;

drop table if exists profile_traveller_type;

drop table if exists profile_passport;

drop table if exists profile_photo;

drop table if exists traveller_type;

drop table if exists trip;

drop table if exists trip_destination;

