create table if not exists users();

alter table if exists users
    add column if not exists id text primary key not null,
    add column if not exists role text not null,
    add column if not exists username text not null unique,
    add column if not exists password text not null;

