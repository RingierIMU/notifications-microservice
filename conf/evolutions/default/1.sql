# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table fcm_device (
  id                            bigint not null,
  user_id                       bigint,
  operating_system              varchar(255),
  operating_system_version      varchar(255),
  manufacturer                  varchar(255),
  model                         varchar(255),
  instance_id                   varchar(255),
  constraint pk_fcm_device primary key (id)
);
create sequence fcm_device_seq;


# --- !Downs

drop table if exists fcm_device;
drop sequence if exists fcm_device_seq;

