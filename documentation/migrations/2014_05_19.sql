--liquibase formatted sql

--changeset 2014_05_19_10:47
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_19(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE baseroom ADD COLUMN description VARCHAR(1000);

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_19');
--rollback