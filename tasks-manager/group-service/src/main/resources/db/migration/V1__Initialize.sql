create table authorities();
alter table if exists authorities
    add column if not exists name text primary key not null

insert into authorities(name) values
    ('ADD_USERS'),
    ('DELETE_USERS'),
    ('UPDATE_GROUP'),
    ('DELETE_GROUP') on conflict do nothing

create table group_roles();
alter table if exists group_roles
    add column if not exists name text primary key not null

insert into roles(name) values
    ('ROLE_GROUP_ADMIN'),
    ('ROLE_GROUP_MODER'),
    ('ROLE_GROUP_USER') on conflict do nothing

create table if not exists group_roles_authorities();
alter table if exists roles_authorities
    add column if not exists group_role_name text not null references group_roles(name) on delete cascade,
    add column if not exists authority_name text not null references authorities(name) on delete cascade,
    drop constraint if exists roles_authorities_pkey,
    add primary key (group_role_name, authority_name)

insert into roles_authorities(role_name, authority_name) values
    ('ROLE_GROUP_USER', 'CREATE_TOPIC'),

    ('ROLE_GROUP_MODER', 'ADD_USERS'),
    ('ROLE_GROUP_MODER', 'DELETE_USERS),

    ('ROLE_GROUP_ADMIN', 'ADD_USERS'),
    ('ROLE_GROUP_ADMIN', 'DELETE_USERS'),
    ('ROLE_GROUP_ADMIN', 'UPDATE_GROUP'),
    ('ROLE_GROUP_ADMIN', 'DELETE_GROUP')


create table if not exists users();
alter table if exists users
    add column if not exists id text primary key not null;

create table if not exists groups();
alter table if exists groups
    add column if not exists id text primary key not null,
    add column if not exists title text,
    add column if not exists description text,
    add column if not exists created_at timestamp not null;


create table if not exists users_groups();
alter table if exists users_groups
    add column if not exists id text not null primary key,
    add column if not exists user_id text not null references users(id) on delete cascade,
    add column if not exists group_id text not null references groups(id) on delete cascade,
    add column if not exists group_role_name not null references roles(name) on delete cascade,
    drop constraint if exists users_groups_pkey,


create table if not exists topics();
alter table if exists topics
    add column if not exists id text primary key not null,
    add column if not exists name text not null,
    add column if not exists description integer not null,
    add column if not exists complexity integer not null,
    add column if not exists importance integer not null,
    add column if not exists theme text not null,
    add column if not exists created_at timestamp not null,
    add column if not exists creator_id text not null references users(id);
    add column if not exists group_id text not null references groups(id) on delete cascade;

create table if not exists invitations();
alter table if exists invitations
    add column if not exists id text not null primary key,
    add column if not exists group_id text not null references groups(id) on delete cascade,
    add column if not exists user_id text not null references users(id) on delete cascade;
    add column if not exists rejected boolean not null;





