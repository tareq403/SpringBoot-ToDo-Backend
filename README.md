# SpringBoot ToDo Backend
This is a simple project demonstrate how to prepare a RESTful CRUD backend server
with SpringBoot framework. In this project, we prepare a simple **_Todo_** model with
few fields and expose API for read all instances, read single instance by _id_,
create a new instance, update an instance by _id_ and deleting an instance by _id_.

We have allowed CORS to accept API requests from different origin, which is useful
for the [frontend project](https://github.com/tareq403/VueJS-ToDo-Frontend).

We have used [MongoDB](https://www.mongodb.com/) as the database of this project.

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
