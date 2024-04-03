/*
creating a schema for the project:
*/

create database if not exists myappdata;
use myappdata;

/*
creating a table for Courier class:
*/

drop table if exists courier;
create table courier (
id int(11) not null auto_increment,
first_name varchar(45) not null,
last_name varchar(45) not null,
phone varchar(16) not null,
primary key(id) )
engine=InnoDB auto_increment=1
default charset=utf8mb4;

/*
creating a table for Address class:
*/

drop table if exists address;
create table address (
id int(11) not null auto_increment,
country_name varchar(45) not null,
city_name varchar(45) not null,
street_name varchar(45) not null,
house_number smallint(5) not null,
courier_id int(11) not null,
primary key (id),
foreign key (courier_id)
references courier (id)
on delete no action on update no action)
engine=InnoDB auto_increment=1
default charset=utf8mb4;

/*
creating a table for users:
*/

drop table if exists users;
create table users (
id int(11) not null auto_increment,
first_name varchar(45) not null,
last_name varchar(45) not null,
email varchar(45) not null,
username varchar(45) not null,
password char(68) not null,
enabled int(11) not null,
primary key(id))
engine=InnoDB auto_increment=1
default charset=utf8mb4;

/*
creating a new table for authorities:
*/

drop table if exists authorities;
create table authorities (
id int(11) not null auto_increment,
username varchar(45) not null,
authority char(45) not null,
user_id int(11) not null,
primary key(id),
foreign key (user_id)
references users (id))
engine=InnoDB auto_increment=1
default charset=utf8mb4;

/*
populating the "users" table with initial users and passwords:
(users table should be populated first based on the current relation)
*/

insert into users (id, first_name, last_name, email, username, password, enabled) values
(1, 'Mary', 'Sue', 'manager@server.com', 'manager', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', 1),
(2, 'Alex', 'Smith', 'admin@server.com', 'admin', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', 1),
(3, 'Harry', 'Potter', 'harry@server.com', 'courier1', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', 1),
(4, 'Frodo', 'Baggins', 'frodo@server.com','courier2', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', 1);

/*
populating the "authorities" table with initial login credentials:
(authorities table should be populated second based on the current relation)
*/

insert into authorities (id, username, authority, user_id) values
  (1, 'manager', 'ROLE_MANAGER', 1),
  (2, 'admin', 'ROLE_ADMIN', 2),
  (3, 'courier1', 'ROLE_COURIER', 3),
  (4, 'courier2', 'ROLE_COURIER', 4);

/*
creating a table for files:
*/

drop table if exists files;
create table files (
id int(11) not null auto_increment,
name varchar(45) not null,
type varchar(45) not null,
data blob not null,
primary key(id))
engine=InnoDB auto_increment=1
default charset=utf8mb4;

/*
populating the "files" table with files containing path and distance:
*/

insert into files values (1, '1.txt', 'text/plain', LOAD_FILE('/var/lib/mysql-files/1.txt'));
insert into files values (2, '2.txt', 'text/plain', LOAD_FILE('/var/lib/mysql-files/2.txt'));
insert into files values (3, '3.txt', 'text/plain', LOAD_FILE('/var/lib/mysql-files/3.txt'));