--liquibase formatted sql

--changeset 2014_06_06_14:18
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_06_06(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE baseroom ADD COLUMN defaultbaseroom BOOL DEFAULT FALSE;
  ALTER TABLE plan ADD COLUMN defaultplan BOOL DEFAULT FALSE;

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_06_06');
--rollback