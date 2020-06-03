--liquibase formatted sql

--changeset 2014_05_12_11:16
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_12(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  DELETE FROM rolepermission;

  INSERT INTO rolepermission (role_id, permission) VALUES
    (1, 'SETTINGS_SAVE'),
    (1, 'USER_CREATE'),
    (1, 'USER_LIST'),
    (1, 'USER_VIEW'),
    (1, 'USER_UPDATE'),
    (1, 'USER_DELETE');

  CREATE UNIQUE INDEX "uk_role_name" ON "role" ("name");

  INSERT INTO role (id, active, name) VALUES
    (2, TRUE, 'role.booking_dept'),
    (3, TRUE, 'role.front_desk'),
    (4, TRUE, 'role.accountant');

  UPDATE role
  SET name = 'role.admin'
  WHERE name = 'Hotel admin';

  UPDATE hibernate_sequences
  SET sequence_next_hi_value = 2
  WHERE sequence_name = 'role';

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_12');
--rollback