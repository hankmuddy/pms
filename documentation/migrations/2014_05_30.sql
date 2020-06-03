--liquibase formatted sql

--changeset 2014_05_30_10:49
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_30(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE room ADD COLUMN position INT NOT NULL DEFAULT 0;

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_30');
--rollback