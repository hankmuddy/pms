#!/bin/bash
./liquibase.sh --driver=org.postgresql.Driver --url=jdbc:postgresql://88.198.41.177:5432/pms --username=postgres --password=MaRYd1vkdgcO --defaultSchemaName=test1 diffChangeLog --referenceUrl=jdbc:postgresql://localhost:5432/pms --referenceUsername=root --referencePassword=postgres
