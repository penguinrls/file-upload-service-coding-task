CREATE SCHEMA ms_file_upload_history;
CREATE LOGIN uploadHistoryUser WITH PASSWORD = 'uploadHistoryUser@ssword'
CREATE USER uploadHistoryUser FOR LOGIN uploadHistoryUser
GRANT CONTROL ON SCHEMA :: ms_file_upload_history TO uploadHistoryUser;
GRANT CREATE TABLE TO uploadHistoryUser;
ALTER USER uploadHistoryUser WITH DEFAULT_SCHEMA = ms_file_upload_history;
