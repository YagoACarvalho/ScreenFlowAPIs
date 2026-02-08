-- Adiciona coluna fingerprint (nullable primeiro pra não quebrar se já tiver linhas)
alter table devices
  add column if not exists fingerprint varchar(64);

-- Backfill
--    Preenche com um valor único temporário
update devices
set fingerprint = substr(md5(random()::text || clock_timestamp()::text), 1, 32)
where fingerprint is null;

-- Agora força NOT NULL
alter table devices
  alter column fingerprint set not null;

-- Unique: dentro do tenant, fingerprint não pode repetir
create unique index if not exists ux_devices_tenant_fingerprint
  on devices (tenant_id, fingerprint);

-- Deixar o nome da coluna coerente com o que você está salvando
do $$
begin
  if exists (
    select 1
    from information_schema.columns
    where table_name = 'devices'
      and column_name = 'refresh_token_hash'
  ) then
    alter table devices rename column refresh_token_hash to refresh_token;
  end if;
end $$;

-- Garantir tamanho do refresh_token
alter table devices
alter column refresh_token type varchar(255);
