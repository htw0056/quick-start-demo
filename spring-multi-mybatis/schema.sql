create database test1;
use test1;
drop table if exists user;
create table if not exists user(id int, name text);
insert into user (id,name) values (1,'hello');

create database test2;
use test2;
drop table if exists user;
create table if not exists user(id int, name text);
insert into user (id,name) values (2,'world');
