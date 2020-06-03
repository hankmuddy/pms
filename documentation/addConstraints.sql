CREATE OR REPLACE FUNCTION public.add_check_constraints(tenantId TEXT)
  RETURNS TEXT AS $$
BEGIN
  EXECUTE FORMAT('SET search_path TO %I;', $1);
  ALTER TABLE baseroomuse ADD CHECK ((startDate < endDate) AND
                                     (type != 'ROOM_USE' OR source IN ('FRONT_DESK', 'BOOKING_BUTTON') OR rcode IS NOT NULL));
  ALTER TABLE bill ADD CHECK ((discount BETWEEN 0 AND rawTotal AND totalPaid <= total) AND (roomUse_id IS NULL OR group_id IS NULL) AND
                              (roomUse_id IS NOT NULL OR group_id IS NOT NULL));
  ALTER TABLE customergroup ADD CHECK (customer_id IS NOT NULL OR company_id IS NOT NULL);
  ALTER TABLE living ADD CHECK ((plan_id IS NULL OR season_id IS NULL) AND (plan_id IS NOT NULL OR season_id IS NOT NULL) AND room_id IS NOT NULL);
  ALTER TABLE refund ADD CHECK ((roomUse_id IS NULL OR group_id IS NULL) AND (roomUse_id IS NOT NULL OR group_id IS NOT NULL));
  ALTER TABLE roomtypevalue ADD CHECK (roomsAvailable >= 0);
  ALTER TABLE season ADD CHECK (start <= until);
  RETURN $1;
END;
$$
LANGUAGE plpgsql
VOLATILE
COST 20;