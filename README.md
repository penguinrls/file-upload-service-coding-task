# Getting Started

# Running the application

# Requirements
    Docker DeskTop Running 4.37.0
    Spring boot 3.3.6
    JDK17
    

The application relies on a database running on port 1433 of Azure type

In order to do this goto the database localdb directory under database module and run

Run 'docker compose up'

This will run the creation of a database script and create a container with an empty db in docker

The app can be run using 'bootRun' gradle configuration on the main class FileUploadService in intellij

Ensure you have SPRING_PROFILES_ACTIVE=localstack in the config for intellij

Once the application runs up for the first time it will create the database schema for the application using liquibase

To stop the delete container from docker and start again 

Stop the process

Run 'docker compose rm'





