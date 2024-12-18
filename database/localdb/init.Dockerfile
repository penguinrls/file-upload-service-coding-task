FROM mcr.microsoft.com/mssql/server:2017-latest

COPY init.sql init.sql

CMD /opt/mssql-tools/bin/sqlcmd -S $DB_HOST,$DB_PORT -d master -U sa -P $SA_PASSWORD -i init.sql
