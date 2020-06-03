CREATE OR REPLACE FUNCTION public.migrate(migration TEXT)
  RETURNS TEXT AS $$
DECLARE
  rec      RECORD;
  tenantid VARCHAR(255);
  call     VARCHAR(255);
BEGIN
  FOR rec IN SELECT
               *
             FROM logins.hotel
  LOOP
    tenantid := rec.tenantid;

    EXECUTE 'SELECT public.' || migration || '(' || quote_literal(tenantid) || ');';

  END LOOP;
  SET search_path TO PUBLIC;
  RETURN 'OK';
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;