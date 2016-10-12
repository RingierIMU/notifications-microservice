# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table blob (
  id                            bigint not null,
  user_id                       bigint,
  data                          varchar(255),
  constraint pk_blob primary key (id)
);
create sequence blob_seq;

create table device (
  id                            bigint not null,
  user_id                       bigint,
  operating_system              varchar(255),
  operating_system_version      varchar(255),
  manufacturer                  varchar(255),
  model                         varchar(255),
  instance_id                   varchar(255),
  constraint pk_device primary key (id)
);
create sequence device_seq;

create table user (
  id                            varchar(255) not null,
  receive_push_notifications    boolean,
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists blob;
drop sequence if exists blob_seq;

drop table if exists device;
drop sequence if exists device_seq;

drop table if exists user;

