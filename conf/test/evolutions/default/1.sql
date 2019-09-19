/*
 * Script to populate the database schema for tests. This is the first level, which is used for setting up the test app.
 */
# --- !Ups

create table achievement_tracker (
  id                            bigint auto_increment not null,
  points                        integer not null,
  current_streak                integer not null,
  owner_id                      bigint,
  constraint uq_achievement_tracker_owner_id unique (owner_id),
  constraint pk_achievement_tracker primary key (id)
);

create table badge (
  id                            bigint auto_increment not null,
  action_to_achieve             varchar(29) not null,
  name                          varchar(255),
  bronze_breakpoint             integer not null,
  silver_breakpoint             integer not null,
  gold_breakpoint               integer not null,
  how_to_progress               varchar(255),
  constraint ck_badge_action_to_achieve check ( action_to_achieve in ('DESTINATION_CREATED','TRIP_CREATED','QUEST_CREATED','HINT_CREATED','RIDDLE_SOLVED','CHECKED_IN','POINTS_GAINED','LOGIN_STREAK','INTERNATIONAL_QUEST_COMPLETED','LARGE_QUEST_COMPLETED','DISTANCE_QUEST_COMPLETED','QUEST_COMPLETED')),
  constraint uq_badge_action_to_achieve unique (action_to_achieve),
  constraint pk_badge primary key (id)
);

create table badge_progress (
  id                            bigint auto_increment not null,
  badge_id                      bigint,
  achievement_tracker_id        bigint,
  progress                      integer not null,
  constraint pk_badge_progress primary key (id)
);

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

create table hint (
  id                            bigint auto_increment not null,
  message                       varchar(255),
  up_votes                      integer not null,
  down_votes                    integer not null,
  objective_id                  bigint,
  creator_id                    bigint,
  constraint pk_hint primary key (id)
);

create table nationality (
  id                            bigint auto_increment not null,
  nationality                   varchar(255),
  country                       varchar(255),
  constraint pk_nationality primary key (id)
);

create table nationality_profile (
  nationality_id                bigint not null,
  profile_id                    bigint not null,
  constraint pk_nationality_profile primary key (nationality_id,profile_id)
);

create table objective (
  id                            bigint auto_increment not null,
  destination_id                bigint,
  owner_id                      bigint,
  riddle                        varchar(255),
  radius                        double,
  quest_using_id                bigint,
  constraint pk_objective primary key (id)
);

create table passport (
  id                            bigint auto_increment not null,
  country                       varchar(255),
  constraint pk_passport primary key (id)
);

create table passport_profile (
  passport_id                   bigint not null,
  profile_id                    bigint not null,
  constraint pk_passport_profile primary key (passport_id,profile_id)
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

create table point_reward (
  id                            bigint auto_increment not null,
  name                          varchar(29) not null,
  value                         integer not null,
  constraint ck_point_reward_name check ( name in ('DESTINATION_CREATED','TRIP_CREATED','QUEST_CREATED','HINT_CREATED','RIDDLE_SOLVED','CHECKED_IN','POINTS_GAINED','LOGIN_STREAK','INTERNATIONAL_QUEST_COMPLETED','LARGE_QUEST_COMPLETED','DISTANCE_QUEST_COMPLETED','QUEST_COMPLETED')),
  constraint uq_point_reward_name unique (name),
  constraint pk_point_reward primary key (id)
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
  last_seen_date                timestamp,
  date_of_creation              timestamp,
  profile_picture_id            bigint,
  constraint uq_profile_profile_picture_id unique (profile_picture_id),
  constraint pk_profile primary key (id)
);

create table quest (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  start_date                    timestamp,
  end_date                      timestamp,
  owner_id                      bigint,
  constraint pk_quest primary key (id)
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

create table traveller_type_profile (
  traveller_type_id             bigint not null,
  profile_id                    bigint not null,
  constraint pk_traveller_type_profile primary key (traveller_type_id,profile_id)
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

create table vote (
  id                            bigint auto_increment not null,
  voter_id                      bigint,
  target_hint_id                bigint,
  is_up_vote                    boolean default false not null,
  constraint pk_vote primary key (id)
);

alter table achievement_tracker add constraint fk_achievement_tracker_owner_id foreign key (owner_id) references profile (id) on delete restrict on update restrict;

create index ix_badge_progress_badge_id on badge_progress (badge_id);
alter table badge_progress add constraint fk_badge_progress_badge_id foreign key (badge_id) references badge (id) on delete restrict on update restrict;

create index ix_badge_progress_achievement_tracker_id on badge_progress (achievement_tracker_id);
alter table badge_progress add constraint fk_badge_progress_achievement_tracker_id foreign key (achievement_tracker_id) references achievement_tracker (id) on delete restrict on update restrict;

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

create index ix_hint_objective_id on hint (objective_id);
alter table hint add constraint fk_hint_objective_id foreign key (objective_id) references objective (id) on delete restrict on update restrict;

create index ix_hint_creator_id on hint (creator_id);
alter table hint add constraint fk_hint_creator_id foreign key (creator_id) references profile (id) on delete restrict on update restrict;

create index ix_nationality_profile_nationality on nationality_profile (nationality_id);
alter table nationality_profile add constraint fk_nationality_profile_nationality foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;

create index ix_nationality_profile_profile on nationality_profile (profile_id);
alter table nationality_profile add constraint fk_nationality_profile_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_objective_destination_id on objective (destination_id);
alter table objective add constraint fk_objective_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_objective_owner_id on objective (owner_id);
alter table objective add constraint fk_objective_owner_id foreign key (owner_id) references profile (id) on delete restrict on update restrict;

create index ix_objective_quest_using_id on objective (quest_using_id);
alter table objective add constraint fk_objective_quest_using_id foreign key (quest_using_id) references quest (id) on delete restrict on update restrict;

create index ix_passport_profile_passport on passport_profile (passport_id);
alter table passport_profile add constraint fk_passport_profile_passport foreign key (passport_id) references passport (id) on delete restrict on update restrict;

create index ix_passport_profile_profile on passport_profile (profile_id);
alter table passport_profile add constraint fk_passport_profile_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_personal_photo_photo_id on personal_photo (photo_id);
alter table personal_photo add constraint fk_personal_photo_photo_id foreign key (photo_id) references photo (id) on delete restrict on update restrict;

create index ix_personal_photo_profile_id on personal_photo (profile_id);
alter table personal_photo add constraint fk_personal_photo_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_photo_upload_profile_id on photo (upload_profile_id);
alter table photo add constraint fk_photo_upload_profile_id foreign key (upload_profile_id) references profile (id) on delete restrict on update restrict;

alter table profile add constraint fk_profile_profile_picture_id foreign key (profile_picture_id) references personal_photo (id) on delete restrict on update restrict;

create index ix_quest_owner_id on quest (owner_id);
alter table quest add constraint fk_quest_owner_id foreign key (owner_id) references profile (id) on delete restrict on update restrict;

create index ix_quest_attempt_attempted_by_id on quest_attempt (attempted_by_id);
alter table quest_attempt add constraint fk_quest_attempt_attempted_by_id foreign key (attempted_by_id) references profile (id) on delete restrict on update restrict;

create index ix_quest_attempt_quest_attempted_id on quest_attempt (quest_attempted_id);
alter table quest_attempt add constraint fk_quest_attempt_quest_attempted_id foreign key (quest_attempted_id) references quest (id) on delete restrict on update restrict;

create index ix_traveller_type_profile_traveller_type on traveller_type_profile (traveller_type_id);
alter table traveller_type_profile add constraint fk_traveller_type_profile_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_traveller_type_profile_profile on traveller_type_profile (profile_id);
alter table traveller_type_profile add constraint fk_traveller_type_profile_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_trip_profile_id on trip (profile_id);
alter table trip add constraint fk_trip_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_trip_destination_trip_id on trip_destination (trip_id);
alter table trip_destination add constraint fk_trip_destination_trip_id foreign key (trip_id) references trip (id) on delete restrict on update restrict;

create index ix_trip_destination_destination_id on trip_destination (destination_id);
alter table trip_destination add constraint fk_trip_destination_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

create index ix_vote_voter_id on vote (voter_id);
alter table vote add constraint fk_vote_voter_id foreign key (voter_id) references profile (id) on delete restrict on update restrict;

create index ix_vote_target_hint_id on vote (target_hint_id);
alter table vote add constraint fk_vote_target_hint_id foreign key (target_hint_id) references hint (id) on delete restrict on update restrict;


# --- !Downs

alter table achievement_tracker drop constraint if exists fk_achievement_tracker_owner_id;

alter table badge_progress drop constraint if exists fk_badge_progress_badge_id;
drop index if exists ix_badge_progress_badge_id;

alter table badge_progress drop constraint if exists fk_badge_progress_achievement_tracker_id;
drop index if exists ix_badge_progress_achievement_tracker_id;

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

alter table hint drop constraint if exists fk_hint_objective_id;
drop index if exists ix_hint_objective_id;

alter table hint drop constraint if exists fk_hint_creator_id;
drop index if exists ix_hint_creator_id;

alter table nationality_profile drop constraint if exists fk_nationality_profile_nationality;
drop index if exists ix_nationality_profile_nationality;

alter table nationality_profile drop constraint if exists fk_nationality_profile_profile;
drop index if exists ix_nationality_profile_profile;

alter table objective drop constraint if exists fk_objective_destination_id;
drop index if exists ix_objective_destination_id;

alter table objective drop constraint if exists fk_objective_owner_id;
drop index if exists ix_objective_owner_id;

alter table objective drop constraint if exists fk_objective_quest_using_id;
drop index if exists ix_objective_quest_using_id;

alter table passport_profile drop constraint if exists fk_passport_profile_passport;
drop index if exists ix_passport_profile_passport;

alter table passport_profile drop constraint if exists fk_passport_profile_profile;
drop index if exists ix_passport_profile_profile;

alter table personal_photo drop constraint if exists fk_personal_photo_photo_id;
drop index if exists ix_personal_photo_photo_id;

alter table personal_photo drop constraint if exists fk_personal_photo_profile_id;
drop index if exists ix_personal_photo_profile_id;

alter table photo drop constraint if exists fk_photo_upload_profile_id;
drop index if exists ix_photo_upload_profile_id;

alter table profile drop constraint if exists fk_profile_profile_picture_id;

alter table quest drop constraint if exists fk_quest_owner_id;
drop index if exists ix_quest_owner_id;

alter table quest_attempt drop constraint if exists fk_quest_attempt_attempted_by_id;
drop index if exists ix_quest_attempt_attempted_by_id;

alter table quest_attempt drop constraint if exists fk_quest_attempt_quest_attempted_id;
drop index if exists ix_quest_attempt_quest_attempted_id;

alter table traveller_type_profile drop constraint if exists fk_traveller_type_profile_traveller_type;
drop index if exists ix_traveller_type_profile_traveller_type;

alter table traveller_type_profile drop constraint if exists fk_traveller_type_profile_profile;
drop index if exists ix_traveller_type_profile_profile;

alter table trip drop constraint if exists fk_trip_profile_id;
drop index if exists ix_trip_profile_id;

alter table trip_destination drop constraint if exists fk_trip_destination_trip_id;
drop index if exists ix_trip_destination_trip_id;

alter table trip_destination drop constraint if exists fk_trip_destination_destination_id;
drop index if exists ix_trip_destination_destination_id;

alter table vote drop constraint if exists fk_vote_voter_id;
drop index if exists ix_vote_voter_id;

alter table vote drop constraint if exists fk_vote_target_hint_id;
drop index if exists ix_vote_target_hint_id;

drop table if exists achievement_tracker;

drop table if exists badge;

drop table if exists badge_progress;

drop table if exists destination;

drop table if exists destination_personal_photo;

drop table if exists destination_traveller_type;

drop table if exists destination_proposed_traveller_type_add;

drop table if exists destination_proposed_traveller_type_remove;

drop table if exists hint;

drop table if exists nationality;

drop table if exists nationality_profile;

drop table if exists objective;

drop table if exists passport;

drop table if exists passport_profile;

drop table if exists personal_photo;

drop table if exists photo;

drop table if exists point_reward;

drop table if exists profile;

drop table if exists quest;

drop table if exists quest_attempt;

drop table if exists traveller_type;

drop table if exists traveller_type_profile;

drop table if exists trip;

drop table if exists trip_destination;

drop table if exists destination_type;

drop table if exists vote;
