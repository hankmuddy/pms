--liquibase formatted sql

--changeset 2014_05_27_16:03
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_27_2(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE baseroomuse ADD COLUMN acode VARCHAR;

  ALTER TABLE baseroomuse DROP CONSTRAINT IF EXISTS baseroomuse_check;

  UPDATE baseroomuse
  SET source = 'WUBOOK_BUTTON'
  WHERE source = 'BOOKING_BUTTON';

  ALTER TABLE baseroomuse ADD CHECK ((startDate < endDate) AND
                                     (type != 'ROOM_USE' OR source IN ('FRONT_DESK', 'BOOKING_BUTTON') OR rcode IS NOT NULL) AND
                                     (type != 'ROOM_USE' OR source NOT IN ('BOOKING_BUTTON') OR acode IS NOT NULL));
  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_27_2');
--rollback