To run the application using Docker:

0. Install Docker desktop (which includes support for Docker and Docker compose)
   https://docs.docker.com/compose/install/
1. Clone the project
2. Build it using
   `./gradlew build`
   This command should take care of installing gradle and java runtime
3. Build and run the project in Docker
   `docker-compose up --build`
   This command downloads the Docker image for Postgres DB, builds and runs both the Database container and the Auth application with appropriate connectivity. The console should log that the database system is up and that the Spring Boot application is up.

---

Postgres Management in the
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
