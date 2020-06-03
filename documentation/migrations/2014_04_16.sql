ALTER TABLE logins.authentication ADD COLUMN supervisor_id BIGINT;
ALTER TABLE logins.hotel ADD COLUMN manager_id BIGINT;
ALTER TABLE logins.hotel ADD COLUMN supervisor_id BIGINT;
ALTER TABLE logins.authentication ADD FOREIGN KEY (supervisor_id) REFERENCES authentication (id);
ALTER TABLE logins.hotel ADD FOREIGN KEY (supervisor_id) REFERENCES authentication (id);
ALTER TABLE logins.hotel ADD FOREIGN KEY (manager_id) REFERENCES authentication (id);