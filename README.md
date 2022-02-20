To run the application using Docker:

0. Install Docker desktop (which includes support for Docker and Docker compose)
   https://www.docker.com/products/docker-desktop
1. Clone the project
2. Build it using
   `./gradlew build`
   This command should take care of installing gradle and java runtime
3. Build and run the project in Docker
   `docker-compose up --build`
   This command downloads the Docker image for Postgres DB, builds and runs both the Database container and the Auth application with appropriate connectivity. The console should log that the database system is up and that the Spring Boot application is up.

---

Postgres Management
Start and Stop the DB server
brew services start postgresql
brew services stop postgresql
brew services list

Connect to postgres:
$ psql postgres

Operations
\l -> List all databases
\c dbname -> connect to database dbname
\d -> list all tables
\d tablename -> describe table

After starting the application, go inside the postgres docker container and insert the roles with these steps:

Find the container ID:
docker ps

Connect to container:
docker exec -it 05b3a3471f6f bash

Connect to the DB inside the container:
root@05b3a3471f6f:/# psql -U postgres

Insert the roles:
postgres-# INSERT INTO roles(name) VALUES('ROLE_VIEWONLY') ON CONFLICT DO NOTHING;
postgres-# INSERT INTO roles(name) VALUES('ROLE_FULLACCESS') ON CONFLICT DO NOTHING;
postgres-# INSERT INTO roles(name) VALUES('ROLE_CARDHOLDER') ON CONFLICT DO NOTHING;
postgres-# \q
