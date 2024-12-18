CREATE DATABASE uploadhistory;
GO
USE uploadhistory;
GO

CREATE LOGIN upload_history_user WITH PASSWORD = 'uploadHistoryUser@ssword';
GO
CREATE USER upload_history_user FOR LOGIN upload_history_user;
GO
GRANT CREATE TABLE TO upload_history_user;
GO

CREATE SCHEMA ms_upload_history;
GO
GRANT CONTROL ON SCHEMA :: ms_upload_history TO upload_history_user;
GO
ALTER USER upload_history_user WITH DEFAULT_SCHEMA = ms_upload_history;
GO
GRANT CONTROL ON SCHEMA :: ms_upload_history TO upload_history_user;
GO