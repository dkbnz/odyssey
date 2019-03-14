# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table nationality (
  id                            integer not null,
  nationality                   varchar(255),
  country                       varchar(255),
  constraint pk_nationality primary key (id)
);

create table passport (
  id                            integer not null,
  issue_country                 varchar(255),
  profile_id                    integer,
  nationality_id                integer,
  constraint pk_passport primary key (id),
  foreign key (profile_id) references profile (id) on delete restrict on update restrict,
  foreign key (nationality_id) references nationality (id) on delete restrict on update restrict
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

create table profile_nationality (
  profile_id                    integer not null,
  nationality_id                integer not null,
  constraint pk_profile_nationality primary key (profile_id,nationality_id),
  foreign key (profile_id) references profile (id) on delete restrict on update restrict,
  foreign key (nationality_id) references nationality (id) on delete restrict on update restrict
);


# --- !Downs

drop table if exists nationality;

drop table if exists passport;

drop table if exists profile;

drop table if exists profile_nationality;

