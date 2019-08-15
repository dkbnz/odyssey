# --- !Ups

create table destination (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  type_id                       bigint,
  district                      varchar(255),
  latitude                      double not null,
  longitude                     double not null,
  country                       varchar(255),
  owner_id                      bigint,
  is_public                     boolean,
  constraint pk_destination primary key (id)
);

create table destination_personal_photo (
  destination_id                bigint not null,
  personal_photo_id             bigint not null,
  constraint pk_destination_personal_photo primary key (destination_id,personal_photo_id)
);

create table destination_traveller_type (
  destination_id                bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_destination_traveller_type primary key (destination_id,traveller_type_id)
);

create table destination_proposed_traveller_type_add (
  destination_id                bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_destination_proposed_traveller_type_add primary key (destination_id,traveller_type_id)
);

create table destination_proposed_traveller_type_remove (
  destination_id                bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_destination_proposed_traveller_type_remove primary key (destination_id,traveller_type_id)
);

create table nationality (
  id                            bigint auto_increment not null,
  nationality                   varchar(255),
  country                       varchar(255),
  constraint pk_nationality primary key (id)
);

create table objective (
  id                            bigint auto_increment not null,
  destination_id                bigint,
  owner_id                      bigint,
  riddle                        varchar(255),
  radius                        double,
  constraint pk_objective primary key (id)
);

create table passport (
  id                            bigint auto_increment not null,
  country                       varchar(255),
  constraint pk_passport primary key (id)
);

create table personal_photo (
  id                            bigint auto_increment not null,
  photo_id                      bigint,
  profile_id                    bigint,
  is_public                     boolean,
  constraint pk_personal_photo primary key (id)
);

create table photo (
  id                            bigint auto_increment not null,
  main_filename                 varchar(255),
  thumbnail_filename            varchar(255),
  content_type                  varchar(255),
  upload_date                   date,
  upload_profile_id             bigint,
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
  is_admin                      boolean default false not null,
  date_of_creation              timestamp,
  profile_picture_id            bigint,
  constraint uq_profile_profile_picture_id unique (profile_picture_id),
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

create table quest (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  start_date                    timestamp,
  end_date                      timestamp,
  owner_id                      bigint,
  constraint pk_quest primary key (id)
);

create table quest_objective (
  quest_id                      bigint not null,
  objective_id                  bigint not null,
  constraint pk_quest_objective primary key (quest_id,objective_id)
);

create table quest_attempt (
  id                            bigint auto_increment not null,
  attempted_by_id               bigint,
  quest_attempted_id            bigint,
  solved_current                boolean default false not null,
  checked_in_index              integer not null,
  completed                     boolean default false not null,
  constraint pk_quest_attempt primary key (id)
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
  name                          varchar(255),
  profile_id                    bigint,
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

create table destination_type (
  id                            bigint auto_increment not null,
  destination_type              varchar(255),
  constraint pk_destination_type primary key (id)
);

create index ix_destination_type_id on destination (type_id);
alter table destination add constraint fk_destination_type_id foreign key (type_id) references destination_type (id) on delete restrict on update restrict;

create index ix_destination_owner_id on destination (owner_id);
alter table destination add constraint fk_destination_owner_id foreign key (owner_id) references profile (id) on delete restrict on update restrict;

create index ix_destination_personal_photo_destination on destination_personal_photo (destination_id);
alter table destination_personal_photo add constraint fk_destination_personal_photo_destination foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_destination_personal_photo_personal_photo on destination_personal_photo (personal_photo_id);
alter table destination_personal_photo add constraint fk_destination_personal_photo_personal_photo foreign key (personal_photo_id) references personal_photo (id) on delete restrict on update restrict;

create index ix_destination_traveller_type_destination on destination_traveller_type (destination_id);
alter table destination_traveller_type add constraint fk_destination_traveller_type_destination foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_destination_traveller_type_traveller_type on destination_traveller_type (traveller_type_id);
alter table destination_traveller_type add constraint fk_destination_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_destination_proposed_traveller_type_add_destination on destination_proposed_traveller_type_add (destination_id);
alter table destination_proposed_traveller_type_add add constraint fk_destination_proposed_traveller_type_add_destination foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_destination_proposed_traveller_type_add_traveller_type on destination_proposed_traveller_type_add (traveller_type_id);
alter table destination_proposed_traveller_type_add add constraint fk_destination_proposed_traveller_type_add_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_destination_proposed_traveller_type_remove_destination on destination_proposed_traveller_type_remove (destination_id);
alter table destination_proposed_traveller_type_remove add constraint fk_destination_proposed_traveller_type_remove_destination foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_destination_proposed_traveller_type_remove_traveller_t_2 on destination_proposed_traveller_type_remove (traveller_type_id);
alter table destination_proposed_traveller_type_remove add constraint fk_destination_proposed_traveller_type_remove_traveller_t_2 foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_objective_destination_id on objective (destination_id);
alter table objective add constraint fk_objective_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_objective_owner_id on objective (owner_id);
alter table objective add constraint fk_objective_owner_id foreign key (owner_id) references profile (id) on delete restrict on update restrict;

create index ix_personal_photo_photo_id on personal_photo (photo_id);
alter table personal_photo add constraint fk_personal_photo_photo_id foreign key (photo_id) references photo (id) on delete restrict on update restrict;

create index ix_personal_photo_profile_id on personal_photo (profile_id);
alter table personal_photo add constraint fk_personal_photo_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_photo_upload_profile_id on photo (upload_profile_id);
alter table photo add constraint fk_photo_upload_profile_id foreign key (upload_profile_id) references profile (id) on delete restrict on update restrict;

alter table profile add constraint fk_profile_profile_picture_id foreign key (profile_picture_id) references personal_photo (id) on delete restrict on update restrict;

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

create index ix_quest_owner_id on quest (owner_id);
alter table quest add constraint fk_quest_owner_id foreign key (owner_id) references profile (id) on delete restrict on update restrict;

create index ix_quest_objective_quest on quest_objective (quest_id);
alter table quest_objective add constraint fk_quest_objective_quest foreign key (quest_id) references quest (id) on delete restrict on update restrict;

create index ix_quest_objective_objective on quest_objective (objective_id);
alter table quest_objective add constraint fk_quest_objective_objective foreign key (objective_id) references objective (id) on delete restrict on update restrict;

create index ix_quest_attempt_attempted_by_id on quest_attempt (attempted_by_id);
alter table quest_attempt add constraint fk_quest_attempt_attempted_by_id foreign key (attempted_by_id) references profile (id) on delete restrict on update restrict;

create index ix_quest_attempt_quest_attempted_id on quest_attempt (quest_attempted_id);
alter table quest_attempt add constraint fk_quest_attempt_quest_attempted_id foreign key (quest_attempted_id) references quest (id) on delete restrict on update restrict;

create index ix_trip_profile_id on trip (profile_id);
alter table trip add constraint fk_trip_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_trip_destination_trip_id on trip_destination (trip_id);
alter table trip_destination add constraint fk_trip_destination_trip_id foreign key (trip_id) references trip (id) on delete restrict on update restrict;

create index ix_trip_destination_destination_id on trip_destination (destination_id);
alter table trip_destination add constraint fk_trip_destination_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;


# --- !Downs

alter table destination drop constraint if exists fk_destination_type_id;
drop index if exists ix_destination_type_id;

alter table destination drop constraint if exists fk_destination_owner_id;
drop index if exists ix_destination_owner_id;

alter table destination_personal_photo drop constraint if exists fk_destination_personal_photo_destination;
drop index if exists ix_destination_personal_photo_destination;

alter table destination_personal_photo drop constraint if exists fk_destination_personal_photo_personal_photo;
drop index if exists ix_destination_personal_photo_personal_photo;

alter table destination_traveller_type drop constraint if exists fk_destination_traveller_type_destination;
drop index if exists ix_destination_traveller_type_destination;

alter table destination_traveller_type drop constraint if exists fk_destination_traveller_type_traveller_type;
drop index if exists ix_destination_traveller_type_traveller_type;

alter table destination_proposed_traveller_type_add drop constraint if exists fk_destination_proposed_traveller_type_add_destination;
drop index if exists ix_destination_proposed_traveller_type_add_destination;

alter table destination_proposed_traveller_type_add drop constraint if exists fk_destination_proposed_traveller_type_add_traveller_type;
drop index if exists ix_destination_proposed_traveller_type_add_traveller_type;

alter table destination_proposed_traveller_type_remove drop constraint if exists fk_destination_proposed_traveller_type_remove_destination;
drop index if exists ix_destination_proposed_traveller_type_remove_destination;

alter table destination_proposed_traveller_type_remove drop constraint if exists fk_destination_proposed_traveller_type_remove_traveller_t_2;
drop index if exists ix_destination_proposed_traveller_type_remove_traveller_t_2;

alter table objective drop constraint if exists fk_objective_destination_id;
drop index if exists ix_objective_destination_id;

alter table objective drop constraint if exists fk_objective_owner_id;
drop index if exists ix_objective_owner_id;

alter table personal_photo drop constraint if exists fk_personal_photo_photo_id;
drop index if exists ix_personal_photo_photo_id;

alter table personal_photo drop constraint if exists fk_personal_photo_profile_id;
drop index if exists ix_personal_photo_profile_id;

alter table photo drop constraint if exists fk_photo_upload_profile_id;
drop index if exists ix_photo_upload_profile_id;

alter table profile drop constraint if exists fk_profile_profile_picture_id;

alter table profile_nationality drop constraint if exists fk_profile_nationality_profile;
drop index if exists ix_profile_nationality_profile;

alter table profile_nationality drop constraint if exists fk_profile_nationality_nationality;
drop index if exists ix_profile_nationality_nationality;

alter table profile_traveller_type drop constraint if exists fk_profile_traveller_type_profile;
drop index if exists ix_profile_traveller_type_profile;

alter table profile_traveller_type drop constraint if exists fk_profile_traveller_type_traveller_type;
drop index if exists ix_profile_traveller_type_traveller_type;

alter table profile_passport drop constraint if exists fk_profile_passport_profile;
drop index if exists ix_profile_passport_profile;

alter table profile_passport drop constraint if exists fk_profile_passport_passport;
drop index if exists ix_profile_passport_passport;

alter table quest drop constraint if exists fk_quest_owner_id;
drop index if exists ix_quest_owner_id;

alter table quest_objective drop constraint if exists fk_quest_objective_quest;
drop index if exists ix_quest_objective_quest;

alter table quest_objective drop constraint if exists fk_quest_objective_objective;
drop index if exists ix_quest_objective_objective;

alter table quest_attempt drop constraint if exists fk_quest_attempt_attempted_by_id;
drop index if exists ix_quest_attempt_attempted_by_id;

alter table quest_attempt drop constraint if exists fk_quest_attempt_quest_attempted_id;
drop index if exists ix_quest_attempt_quest_attempted_id;

alter table trip drop constraint if exists fk_trip_profile_id;
drop index if exists ix_trip_profile_id;

alter table trip_destination drop constraint if exists fk_trip_destination_trip_id;
drop index if exists ix_trip_destination_trip_id;

alter table trip_destination drop constraint if exists fk_trip_destination_destination_id;
drop index if exists ix_trip_destination_destination_id;

drop table if exists destination;

drop table if exists destination_personal_photo;

drop table if exists destination_traveller_type;

drop table if exists destination_proposed_traveller_type_add;

drop table if exists destination_proposed_traveller_type_remove;

drop table if exists nationality;

drop table if exists objective;

drop table if exists passport;

drop table if exists personal_photo;

drop table if exists photo;

drop table if exists profile;

drop table if exists profile_nationality;

drop table if exists profile_traveller_type;

drop table if exists profile_passport;

drop table if exists quest;

drop table if exists quest_objective;

drop table if exists quest_attempt;

drop table if exists traveller_type;

drop table if exists trip;

drop table if exists trip_destination;

drop table if exists destination_type;