--liquibase formatted sql

--changeset 2014_06_05_12:19
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_06_05(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE bookingbuttonsettings
  ADD COLUMN backgroundColor VARCHAR(255);

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_06_05');
--rollback