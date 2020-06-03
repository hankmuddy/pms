--liquibase formatted sql

--changeset 2013-09-04_15:13
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_04_09(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);
  ALTER TABLE bankdetails DROP CONSTRAINT IF EXISTS bankdetails_check;
  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_04_09');
--rollback