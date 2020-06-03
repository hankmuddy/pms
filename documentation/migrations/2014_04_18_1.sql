--liquibase formatted sql

--changeset 2014-04-18_1_15:10
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_04_18(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);
  ALTER TABLE baseroomuse DROP CONSTRAINT IF EXISTS baseroomuse_check;
  ALTER TABLE baseroomuse ADD CHECK ((startDate < endDate) AND (type != 'ROOM_USE' OR source IN ('FRONT_DESK', 'BOOKING_BUTTON') OR rcode IS NOT NULL));
  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_04_18');
--rollback