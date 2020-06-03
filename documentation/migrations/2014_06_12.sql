--liquibase formatted sql

--changeset 2014_06_12 _14:18
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_06_12(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  DROP TABLE bookingbuttonsettingsvalues;
  DROP TABLE bookingbuttonsettings;

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_06_12');
--rollback