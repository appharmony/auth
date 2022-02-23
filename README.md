## About

The Auth application provides the functionalities of authentication and authorization. The application is implemented in Java using Spring Boot. It uses Spring Security to implement JWT-based authentication and to enforce role-based authorization. Upon successful user registration with the user credentials and one or more of the available roles, the user information is persisted in Postgres database. A registered user can then login to the system by providing valid credentials with the login API, which returns the JSON Web Token (JWT) and the role(s) of the user from the database, so that the consumers of the application can use the JWT as a bearer token to access protected resources and customize their application based on the user roles.

The application is packaged using Docker, making use of Docker Compose tool, which optimizes defining and running multi-container applications. The Spring Boot application and Postgres database each run in their own containers and communicate with each other using the Docker Compose configuration. The docker image for Auth application is pushed to Dockerhub.

The containers are pushed to AWS Elastic Container Service (ECS), and the application is hosted in AWS. ECS enables users to run highly secure, reliable, and scalable containers. ECS provides managed container hosting and provides seamless autoscalling through AWS Fargate. Native support through AWS and Docker CLI tools facilitate pushing images to ECS.

### Instructions to run the application locally using Docker:

0. Install Docker desktop (which includes support for Docker and Docker compose)
   https://www.docker.com/products/docker-desktop
1. Clone the project
2. Build it using
   `./gradlew build`
   This command should take care of installing gradle and java runtime
3. Build and run the project in Docker
   `docker-compose up --build`
   This command downloads the Docker image for Postgres DB, builds and runs both the Database container and the Auth application with appropriate connectivity. The console should log that the database system is up and that the Spring Boot application is up.
