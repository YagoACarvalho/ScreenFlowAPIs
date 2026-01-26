create table tenants (
  id uuid primary key,
  name varchar(150) not null,
  status varchar(30) not null
);

create table users (
  id uuid primary key,
  tenant_id uuid not null references tenants(id),
  email varchar(255) not null unique,
  password_hash varchar(255) not null,
  role varchar(30) not null,
  created_at timestamp not null
);

create table screens (
  id uuid primary key,
  tenant_id uuid not null references tenants(id),
  name varchar(150) not null,
  location varchar(255),
  status varchar(30) not null,
  created_at timestamp not null
);

create table activation_codes (
  code varchar(12) primary key,
  tenant_id uuid not null references tenants(id),
  screen_id uuid not null references screens(id),
  expires_at timestamp not null,
  used_at timestamp null,
  created_at timestamp not null
);

create table devices (
  id uuid primary key,
  tenant_id uuid not null references tenants(id),
  screen_id uuid not null references screens(id),
  refresh_token_hash varchar(255) not null,
  status varchar(20) not null,
  last_seen_at timestamp null,
  created_at timestamp not null
);

create index idx_users_tenant_id on users(tenant_id);
create index idx_screens_tenant_id on screens(tenant_id);

create index idx_activation_codes_screen_id on activation_codes(screen_id);
create index idx_activation_codes_tenant_id on activation_codes(tenant_id);

create index idx_devices_tenant_id on devices(tenant_id);
create index idx_devices_screen_id on devices(screen_id);
create index idx_devices_status on devices(status);
