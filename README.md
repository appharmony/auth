Postgres

Manage postgres
brew services start postgresql
brew services stop postgresql
brew services list

Connect to postgres: 
$ psql postgres

Operations
\l -> List all databases
\c dbname -> connect to dynamo
\d -> list all tables
\d tablename -> describe table

Run the application
./gradlew bootRun