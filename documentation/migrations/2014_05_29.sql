--liquibase formatted sql

--changeset 2014_05_29_17:56
CREATE OR REPLACE FUNCTION public.migrate_tenant_2014_05_29(tenantId TEXT)
  RETURNS TEXT AS $$
DECLARE
  roleId BIGINT;
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);

  DELETE FROM rolepermission;

  roleId := r.id FROM ROLE r WHERE r.name = 'role.admin';

  INSERT INTO rolepermission (role_id, permission) VALUES
    (roleId, 'SETTINGS_SAVE'),
    (roleId, 'USER_CREATE'),
    (roleId, 'USER_LIST'),
    (roleId, 'USER_VIEW'),
    (roleId, 'USER_UPDATE'),
    (roleId, 'USER_DELETE');

  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;
SELECT
  public.migrate('migrate_tenant_2014_05_29');
--rollback
