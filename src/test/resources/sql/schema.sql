create schema if not exists "testdb";
set schema "testdb";
create table if not exists courier
(
    id         integer     not null auto_increment primary key,
    first_name varchar(45) not null,
    last_name  varchar(45) not null,
    phone      varchar(16) not null
);
create table if not exists address
(
    id           integer     not null auto_increment primary key,
    country_name varchar(45) not null,
    city_name    varchar(45) not null,
    street_name  varchar(45) not null,
    house_number smallint    not null,
    courier_id   int         not null,
    foreign key (courier_id)
        references courier (id)
        on delete no action
        on update no action
);


