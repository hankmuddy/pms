--liquibase formatted sql

--changeset 2014_04_11_10:57
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_04_11(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);
  ALTER TABLE bill ADD COLUMN rawtotal BIGINT NOT NULL DEFAULT 0;
  UPDATE bill
  SET rawtotal = total;

  ALTER TABLE bill DROP CONSTRAINT IF EXISTS bill_check;
  ALTER TABLE bill ADD CHECK ((discount between 0 and rawtotal and totalPaid <= total) and (roomUse_id IS NULL OR group_id IS NULL) AND
                              (roomUse_id IS NOT NULL OR group_id IS NOT NULL));

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_04_11');
--rollback