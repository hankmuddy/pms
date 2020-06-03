--liquibase formatted sql

--changeset 2014_06_05_16:17
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_06_05_1(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  CREATE TABLE bookingbuttonsettingsvalues
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    key         VARCHAR(255),
    value       VARCHAR(255),
    createdby   BIGINT,
    updatedby   BIGINT,
    bbs_id      BIGINT             NOT NULL
  );

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_06_05_1');
--rollback