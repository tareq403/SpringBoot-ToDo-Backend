# SpringBoot ToDo Backend
This is a simple project demonstrate how to prepare a [RESTful](https://restfulapi.net/) CRUD backend server
with [SpringBoot](https://spring.io/) framework. In this project, we prepare a simple **_Todo_** model with
few fields and expose API for read all instances, read single instance by _id_,
create a new instance, update an instance by _id_ and deleting an instance by _id_.

We have allowed CORS to accept API requests from different origin, which is useful
for the [frontend project](https://github.com/tareq403/VueJS-ToDo-Frontend).

We have used [MongoDB](https://www.mongodb.com/) as the database of this project.

### Before you start
You will need Java Development Toolkit and MongoDB installed in your system to implement this project.
Official Spring website (https://spring.io/) recomends [AdoptOpenJDK](https://adoptopenjdk.net/).
You will have detailed instruction to install and set up MongoDB in your system [here](https://docs.mongodb.com/manual/installation/).

## Initializing the project
The project is initiated using [Spring Initializr](https://start.spring.io/). Go to
Spring Initializr and select the following options:

* You can choose either of the Maven or Gradle project based on your liking. We have
used Gradle here.
* As language, we used Java.
* We used Spring Boot version 2.3.1.
* Used Java 8.
* As dependency, we have used Spring Web and Spring Data MongoDB.
* Provide project name and package name as your like.
* Hit "Generate".

Your project will be initiated and a Zip archive will be downloaded. You can unzip the
package and work on it.

## Project structure
The project initiated with Spring Initializr is instantly runnable. The project directory will contain
the following files or directories:
* *src* directory to accommodate your source codes.
  * *main* directory to accommodate your project source codes.
  * *test* directory to accommodate your test classes.
* *build.gradle* will hold your build configurations.
* *gradlew* / *gradlew.bat* to run the project depending on your OS.
* *settings.gradle* to hold the settings for gradle.

You can run the project by the following command:
```
./gradlew bootRun
```

## Explanation of the source code
The initialized project will contain an `Application` class, like this:

```java
@SpringBootApplication
public class TodobackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodobackendApplication.class, args);
    }

}
``` 

The `SpringBootApplication` decorator adds the additional functionality for the class to
be run as a Spring Boot application.

### Creating a `ToDo` model
To initiate a data entity (can be database table or collection), you will need to create a *model*
class. In our case, we create a class named `ToDo`. The class has been initiated like this:

```java
public class ToDo {
    @Id
    private String id;
    private String name;
    private String details;
    private boolean done;
}
```
This model class is independent of your database engine. Spring Boot will use this class to handle
the `ToDo` objects in code layer. Also, note the `Id` decorator. Spring Boot will try to use the `id`
field as the database table/collection id depending on your database engine. 

### Adding a *Repository* interface to communicate with database
Spring Boot separates code layer from database layer, so that you can migrate from one database engine
to another database engine with minimal change in your project. This is possible because communication
with the database is handles with some special interfaces called *Repository* interfaces. We have a *Repository*
interface for our `ToDo` model class named `ToDoRepository`. Which looks like this:
```java
public interface ToDoRepository extends MongoRepository<ToDo, String> {
    //
}
```
Importantly, our `ToDoRepository` extends from the *MongoRepository* interface. *MongoRepository* interface
has the support for communicating with the *MongoDB*, which will be useful for our case. Also note that, we
are mapping our `ToDo` objects by *String* because the *id* field in `ToDo` class is a *String* field.
