CREATE OR REPLACE FUNCTION migrate_tenant_11_03_2014(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);
  CREATE TABLE groupmembertoroomuse
  (
    id             BIGINT PRIMARY KEY NOT NULL,
    active         BOOL               NOT NULL,
    createddate    TIMESTAMP,
    updateddate    TIMESTAMP,
    createdby      BIGINT,
    updatedby      BIGINT,
    groupmember_id BIGINT,
    roomuse_id     BIGINT,
    FOREIGN KEY (updatedby) REFERENCES application_user (id),
    FOREIGN KEY (createdby) REFERENCES application_user (id),
    FOREIGN KEY (roomuse_id) REFERENCES baseroomuse (id),
    FOREIGN KEY (groupmember_id) REFERENCES groupmember (id)
  );
  INSERT INTO groupmembertoroomuse (id, active, createddate, updateddate, createdby, updatedby, groupmember_id, roomuse_id)
    SELECT
      row_number()
      OVER (),
      TRUE,
      gm.createddate,
      gm.updateddate,
      gm.createdby,
      gm.updatedby,
      gm.id,
      gm.roomuse_id
    FROM groupmember gm
    WHERE gm.roomuse_id IS NOT NULL;

  ALTER TABLE groupmember DROP COLUMN roomuse_id;

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;

CREATE OR REPLACE FUNCTION migration_11_03_2014()
  RETURNS TEXT AS $$
DECLARE
  rec      RECORD;
  tenantid VARCHAR(255);
BEGIN
  FOR rec IN SELECT
               *
             FROM logins.hotel
  LOOP
    tenantid := rec.tenantid;
    PERFORM
      public.migrate_tenant_11_03_2014(tenantid);
  END LOOP;
  RETURN 'OK';
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;

SELECT
  public.migration_11_03_2014();
