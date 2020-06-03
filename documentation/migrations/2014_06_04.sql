--liquibase formatted sql

--changeset 2014_06_04_15:30
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_06_04(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  UPDATE room
  SET position = id;

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_06_04');
--rollback