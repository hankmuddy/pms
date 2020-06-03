#!/bin/bash
#1) Before installing postgres make sure brew is up to date and healthy:
brew update
brew doctor

#2)
brew install postgresql

#3) Time to create a cluster:
#$ initdb <your-cluster-dir>/<your-cluster-name> -E utf8
initdb /usr/local/var/postgres -E utf8

#4) Start server:
#pg_ctl -D <your-cluster-name> -l logfile start
pg_ctl -D /usr/local/var/postgres -l logfile start

#5) Create a database inside a cluster:
createdb pms -E utf8

#6) Connect:
psql pms

#7) Create a user:
CREATE USER root WITH PASSWORD 'postgres';

#8) Grant permissions:
GRANT ALL PRIVILEGES ON DATABASE pms to root;

#9)PROFIT:
\q

#...
#n) Stopping server:
#pg_ctl -D <your-cluster-name> stop


ALTER USER root WITH SUPERUSER;
CREATE USER postgres WITH PASSWORD 'postgres';
ALTER USER postgres WITH SUPERUSER;
GRANT ALL PRIVILEGES ON DATABASE pms to postgres;

#backup command example:
#pg_dump --host 88.198.41.177 --port 5432 --username "postgres" --role "postgres" --no-password  --format tar --section pre-data --section data --section post-data --encoding UTF8 --verbose --file "/Users/vomel/Documents/2014-04-06-232600" "pms"
#pg_dump  --port 5432 --username "postgres" --role "postgres" --no-password  --format tar --section pre-data --section data --section post-data --encoding UTF8 --verbose --file "/var/lib/postgresql/backup/2014-04-07-141600.tar" "pms"
#pg_restore --host localhost --port 5432 --username "vomel" --dbname "pms" --role "postgres" --no-password  --section pre-data --section data --section post-data --verbose "/Users/vomel/IdeaProjects/pms/2014-04-07-141600.tar"
