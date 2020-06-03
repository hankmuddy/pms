--liquibase formatted sql

--changeset 2014_05_06_11:34
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_06(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  CREATE TABLE planrestriction
  (
    type              VARCHAR(31)        NOT NULL,
    id                BIGINT PRIMARY KEY NOT NULL,
    active            BOOL               NOT NULL,
    createddate       TIMESTAMP,
    updateddate       TIMESTAMP,
    name              VARCHAR(255),
    pid               BIGINT,
    closed            INT,
    closedtodeparture BOOL,
    maxstay           INT,
    minstay           INT,
    minstayarrival    INT,
    createdby         BIGINT,
    updatedby         BIGINT
  );

  ALTER TABLE plan ADD COLUMN type VARCHAR(31) NOT NULL DEFAULT 'plan';
  ALTER TABLE plan ADD COLUMN value BIGINT;
  ALTER TABLE plan ADD COLUMN variation INT;
  ALTER TABLE plan ADD COLUMN plan_id BIGINT;

  ALTER TABLE plan ADD FOREIGN KEY (plan_id) REFERENCES plan (id);
  ALTER TABLE planrestriction ADD FOREIGN KEY (updatedby) REFERENCES application_user (id);
  ALTER TABLE planrestriction ADD FOREIGN KEY (createdby) REFERENCES application_user (id);

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_06');
--rollback