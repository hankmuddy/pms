--liquibase formatted sql

--changeset 2014_06_18_14:14
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_06_18(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE baseroomuse ALTER COLUMN room_id DROP NOT NULL;

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_06_18');
--rollback
