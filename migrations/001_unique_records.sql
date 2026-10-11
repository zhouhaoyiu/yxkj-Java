-- Resolve any duplicate UUIDs before applying these indexes to an existing database.
CREATE UNIQUE INDEX workjob_uuid_unique ON workjob (workUuid);
CREATE UNIQUE INDEX recordjob_uuid_unique ON recordjob (jobUuid);
