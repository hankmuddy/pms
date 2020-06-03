--liquibase formatted sql

--changeset 2014_05_20_13:23
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_20(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE baseroomuse ALTER COLUMN comment TYPE TEXT;
  ALTER TABLE baseroomuse ALTER COLUMN description TYPE TEXT;

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_20');
--rollback