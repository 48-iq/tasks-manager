create table authorities();
alter table if exists authorities
    add column if not exists name text primary key not null

insert into authorities(name) values
    ('ADD_USERS'),
    ('DELETE_USERS'),
    ('CHANGE_ROLE'),
    ('CREATE_TOPIC'),
    ('DELETE_GROUP') on conflict do nothing

create table roles();
alter table if exists roles
    add column if not exists name text primary key not null

insert into roles(name) values
    ('ROLE_GROUP_ADMIN'),
    ('ROLE_GROUP_MODER'),
    ('ROLE_GROUP_USER') on conflict do nothing

create table if not exists roles_authorities();
alter table if exists roles_authorities
    add column if not exists role_name not null,
    add column if not exists authority_name not null,
    drop constraint if exists roles_authorities_pkey,
    add primary key (role_name, authority_name)

insert into roles_authorities(role_name, authority_name) values
    ('ROLE_GROUP_USER', 'CREATE_TOPIC'),

    ('ROLE_GROUP_MODER', 'CREATE_TOPIC'),
    ('ROLE_GROUP_MODER', 'ADD_USERS'),
    ('ROLE_GROUP_MODER', 'DELETE_USERS),

    ('ROLE_GROUP_ADMIN', 'CREATE_TOPIC'),
    ('ROLE_GROUP_ADMIN', 'ADD_USERS'),
    ('ROLE_GROUP_ADMIN', 'DELETE_USERS'),
    ('ROLE_GROUP_ADMIN', 'CHANGE_ROLE'),
    ('ROLE_GROUP_ADMIN', 'DELETE_GROUP')


create table if not exists users();
alter table if exists users
    add column if not exists id text primary key not null;

create table if not exists groups();
alter table if exists groups
    add column if not exists id text primary key not null,
    add column if not exists name text,
    add column if not exists description,
    add column if not exists created_at timestamp not null;


create table if not exists users_groups();
alter table if exists users_groups
    add column if not exists user_id text not null references users(id),
    add column if not exists group_id text not null references groups(id),
    add column if not exists group_role not null references roles(name),
    drop constraint if exists users_groups_pkey,
    add primary key(user_id, group_id);

create table if not exists topics();
alter table if exists topics
    add column if not exists id text primary key not null,
    add column if not exists name text not null,
    add column if not exists description not null,
    add column if not exists created_at timestamp not null,
    add column if not exists creator_id text not null references users(id);

create table if not exists topics_groups();
alter table if exists topics_groups
    add column if not exists topic_id text not null,
    add column if not exists group_id text not null,
    drop constraint if exists topics_groups_pkey,
    add primary key(topic_id, group_id);







