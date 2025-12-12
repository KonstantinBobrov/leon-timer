create schema if not exists leon;

create table timer
(
    ID    serial primary key,
    VALUE TIME(6) not null
);