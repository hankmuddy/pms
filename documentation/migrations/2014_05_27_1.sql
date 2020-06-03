--liquibase formatted sql

--changeset 2014_05_06_11:34
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_27_1(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  CREATE TABLE bookingbuttonsettings
  (
    id          BIGINT PRIMARY KEY NOT NULL,
    active      BOOL               NOT NULL,
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    createdby   BIGINT,
    updatedby   BIGINT,
    name        VARCHAR(255),
    color       VARCHAR(255),
    currency    VARCHAR(255),
    language    VARCHAR(255)
  );
  ALTER TABLE bookingbuttonsettings ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE bookingbuttonsettings ADD FOREIGN KEY (createdby) REFERENCES application_user (id);

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_27_1');
--rollback