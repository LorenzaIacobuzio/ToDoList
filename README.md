# TO-DO LIST APP

A simple backend system for a to-do list app, with a RESTful API and a database.

## Tech stack

* OS: Linux Ubuntu 22.04
* IDE: Intellij (https://www.jetbrains.com/idea/download/?section=linux)
* Language: Kotlin
* Framework: Ktor
* Server: Netty engine
* Database: Postgres
* Build automation: Gradle
* Testing framework: TestNG
* Infrastructure and cloud: AWS
* Container: Docker
* Version control platform: GitHub
* CI/CD: GitHub actions
* Issue tracker: Jira (https://lorenzaiacobuzio.atlassian.net/jira/software/projects/TOD/boards/1)
* Document platform: Confluence (https://lorenzaiacobuzio.atlassian.net/wiki/spaces/TA/pages/294914/To-do+list+app)

## Prerequisites

* OpenJDK version 17 or later (to run Ktor)
* Docker 24 or later
* Docker compose 2.24 or later (to install, https://www.cherryservers.com/blog/how-to-install-and-use-docker-compose-on-ubuntu-20-04#:~:text=%23-,Step%202%3A%20Install%20Docker%20Compose%20And%20Related%20Packages,-Now%20that%20you)
* Git
* Intellij 2023
* Postgres 12 for Ubuntu (to install, follow https://www.postgresql.org/download/linux/ubuntu/)

## Build and run

To build and run the app with gradle, do `./gradlew run assemble` in the root directory of the project.

## Test

 To run all project tests, do `./gradlew clean test`.

## Database

Switch to system admin from command line with `sudo -i -u postgres`.
This user can create new databases, users, etc.

To create a new local database instance, do `createdb myDbName`.

You can access the new database via the interactive terminal program, by doing `psql myDbName`.

You can also connect Intellij to your database (ToDoListDB in this case), by using the right-hand side menu in the IDE (credentials are in the application.conf file).

The application is now connected to the new database.

## CI/CD pipeline

In the root directory of the project, the `.github/workflows` folder contains the GitHub Actions file needed to run the CI/CD pipeline on GitHub, whenever a new pull request is created.

## Spec requirements

In the root directory of the project, the `docs` folder contains the POstman collection and environment, and the openAPI spec file.

## Containerize

To build the app image, complete with all needed services and containers (Postgres),
run `docker compose up`. You should see the server responding as usual.

## Logging

The Ktor Logger plugin takes care of logging both to console and to file, based on the config file `logback.xml` found in the `resources` folder.