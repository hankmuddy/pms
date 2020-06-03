--liquibase formatted sql

--changeset 2014_05_16_13:38
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_16(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  ALTER TABLE refund ADD COLUMN bankdetails_id BIGINT;
  ALTER TABLE refund ADD FOREIGN KEY (bankdetails_id) REFERENCES bankdetails (id);

  UPDATE refund
  SET bankdetails_id = (SELECT
                          b.id
                        FROM bankdetails b
                        WHERE b.defaultdetails = TRUE
                        LIMIT 1)
  WHERE total > 0;

  ALTER TABLE refund DROP CONSTRAINT IF EXISTS refund_check;
  ALTER TABLE refund ADD CHECK ((roomUse_id IS NULL OR group_id IS NULL) AND (roomUse_id IS NOT NULL OR group_id IS NOT NULL) AND
                                (total = 0 OR bankDetails_id IS NOT NULL));

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_16');
--rollback