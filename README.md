# TO-DO LIST APP

A simple backend system for a to-do list app, with a RESTful API and a database.
This is a quick and dirty proof of concept, to prove to me the concept that I am able to do this from scratch in a short amount of time.
There is an infinite list of improvements that this can undergo, and an even more infinite (if possible) list of way-too-dirty things. Oh well.

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
* Git
* Intellij 2023
* Postgres 12 for Ubuntu (to install, follow https://www.postgresql.org/download/linux/ubuntu/)

## Build and run

To build and run the app with gradle, do `./gradlew run assemble` in the root directory of the project.

## Test

 To run all project tests, do `./gradlew clean test`.

## Database

From command line, type `sudo systemctl restart postgresql.service` in case the service is not already up and running.
Switch to system admin from command line with `sudo -i -u postgres`.
This user can create new databases, users, etc.

To create a new local database instance, do `createdb myDbName`.

You can access the new database via the interactive terminal program, by doing `psql myDbName`.

You can also connect Intellij to your database (ToDoListDB in this case), by using the right-hand side menu in the IDE (credentials are in the application.conf file).

The application is now connected to the new database.

## Spec requirements

## Containerize

## Logging
