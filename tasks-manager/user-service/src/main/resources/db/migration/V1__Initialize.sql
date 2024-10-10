
create table if not exists users();

alter table if exists users
    add column if not exists id text primary key not null,
    add column if not exists username text unique,
    add column if not exists email text unique,
    add column if not exists name text,
    add column if not exists surname text;
