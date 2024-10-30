create table if not exists accounts();

alter table if exists accounts
    add column if not exists id text primary key not null,
    add column if not exists username text unique not null,
    add column if not exists password text not null;

create table if not exists roles();

alter table if exists roles
    add column if not exists name text primary key not null;

create table if not exists accounts_roles();

alter table if exists accounts_roles
    add column if not exists account_id text references accounts(id),
    add column if not exists role_name text references roles(name),
    drop constraint if exists accounts_roles_pkey,
    add primary key(account_id, role_name);

insert into roles(name) values
	('ROLE_USER'), ('ROLE_ADMIN') on conflict do nothing