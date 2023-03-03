# recipe-website-backend

## Synopsis

This is the first of what is expected to be multiple backends, implemented in various languages, for the recipe-website.

This particular version uses Java 17, Spring Boot, Spring Data JPA (Hibernate) and REST Controllers, with Maven
controlling the build process.

The main purpose of the recipe-website-backend is to expose the recipe data etc as JSON, to be consumed by the
front-end.

## Installation

### Checkout

* `git clone git@github.com:lstierney/recipe-website-backend.git`

### Configuration

As this is a Spring Boot application configuration is controlled by a series of `application.properties` files

* `application.properties` - common properties
* `application-dev.properties` - development properties. Uses an in memory H2 database
* `application-live.properties` - development properties. Used a mySQL installation (somewhere). You would set your DB
  details in here.

### SQL

SQL Files are provided which will generate the schema AND insert required meta-data.

* `/src/main/resources/schema.sql` - this will create the schema
* `/src/main/resources/data.sql` - this will insert the meta-data

*Note*: these scripts will automatically execute when the application properties are set to `dev` i.e. against the H2 DB
but will not automtically execute in the `live` environment.

### Build executable JAR

* Build JAR (currently int tests run as part of *normal* build): `mvn clean package`
* The built JAR can be found in `$project.baseDir/target/`
* *Hint*: make sure you use the *executable* JAR that is generated.

### Installing the JAR

* *Hint*: make sure that before you install a new version of the Application you stop any previous running version (see
  below)
* As we are using an executable JAR, for use on a Linux system, the application can be installed as an `init.d` service.
  Instructions here: <http://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html>
* *Hint*: make sure the `init.d` link you create has the correct permissions!

#### Setting the correct Spring profile in .conf

* The Application defaults to running under "dev" profile (see below).
* Whilst running on a server it should use profile "live"
* In order to configure the Application to use one of these profiles create a file, in the same directory as the JAR,
  with the same filename as the JAR but with extension ".conf" instead of ".jar".
* The contents of the conf file should be:

```
JAVA_OPTS="-Dspring.profiles.active=dev|live"
``` 

* `dev` profile will work via the JAR but is expected to be used in development environment.

### Starting/Stopping the Service

* Once the JAR/Service has been installed it can be controlled with the
  familiar: ```sudo service recipe-website-backend start/stop/restart/status``` etc.
* *Note*: if the Application has stopped itself due to catching an *Unrecoverable* Exception (see below) then there is a
  chance a stale PID file will be left on the operating system. This can cause `service recipe-website-backend status`
  to return "running" even when the application has stopped. Under these conditions it is safe to "restart".

## REST Endpoints

SAMPLE DATA TO BE PROVIDED!!!!

### GET /recipes

Gets ALL the data for all the recipes

### GET /recipes/id

e.g. `/recipes/123`. Gets all the data for a specified recipe

### POST /recipes

Add/create a recipe (Add a sample here)

### GET /units

Gets ALL the "Unit of Measurement" meta-data


