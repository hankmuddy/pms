UPDATE logins.authentication
SET usertype = 'user'
WHERE usertype = 'USER';
UPDATE logins.authentication
SET usertype = 'admin'
WHERE usertype = 'ADMIN';
UPDATE logins.authentication
SET usertype = 'manager'
WHERE usertype = 'MANAGER';
UPDATE logins.authentication
SET usertype = 'managerSupervisor'
WHERE usertype = 'MANAGER_SUPERVISOR';