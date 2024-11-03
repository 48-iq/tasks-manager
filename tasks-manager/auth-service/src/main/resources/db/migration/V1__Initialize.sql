create table if not exists authorities();
alter table if exists authorities
    add column if not exists name text primary key not null;

insert into authorities(name) values
    ('BAN_USERS'),
    ('USE_SERVICES'),
    ('GET_STATISTICS');

create table if not exists roles();

alter table if exists roles
    add column if not exists name text primary key not null;

insert into roles(name) values
	('ROLE_USER'), ('ROLE_ADMIN') on conflict do nothing;

create table roles_authorities();

alter table if exists roles_authorities
    add column if not exists role_name text not null references roles(name),
    add column if not exists authority_name text not null references authorities(name),
    drop constraint if exists roles_authorities_pkey,
    add primary key (role_name, authority_name);

insert into roles_authorities(role_name, authority_name) values
    ('ROLE_USER', 'USE_SERVICES'),

    ('ROLE_ADMIN', 'USE_SERVICES'),
    ('ROLE_ADMIN', 'BAN_USERS'),
    ('ROLE_ADMIN', 'GET_STATISTICS');

create table if not exists accounts();

alter table if exists accounts
    add column if not exists id text primary key not null,
    add column if not exists username text unique not null,
    add column if not exists password text not null;


create table if not exists accounts_roles();

alter table if exists accounts_roles
    add column if not exists account_id text not null references accounts(id),
    add column if not exists role_name text not null references roles(name),
    drop constraint if exists accounts_roles_pkey,
    add primary key(account_id, role_name);


