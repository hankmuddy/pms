--liquibase formatted sql

--changeset 2014_07_08_12:23
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_07_08(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE bankdetails ADD COLUMN iban VARCHAR(255);

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_07_08');
--rollback
