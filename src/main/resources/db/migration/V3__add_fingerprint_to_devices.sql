

-- Adiciona coluna fingerprint (começa nullable pra não quebrar se já tiver linhas)
alter table devices
  add column if not exists fingerprint varchar(64);

-- Backfill (se já houver devices no banco):
--    Aqui eu preencho com um valor único temporário usando md5(random + clock)
--    (Serve só pra passar a NOT NULL + UNIQUE sem estourar)
update devices
set fingerprint = substr(md5(random()::text || clock_timestamp()::text), 1, 32)
where fingerprint is null;

-- Agora força NOT NULL
alter table devices
  alter column fingerprint set not null;

-- Unique: dentro do tenant, fingerprint não pode repetir
create unique index if not exists ux_devices_tenant_fingerprint
  on devices (tenant_id, fingerprint);
