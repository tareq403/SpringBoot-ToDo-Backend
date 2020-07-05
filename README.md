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

### Adding a *Repository* interface to communicate with the database
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

### Preparing the RESTful API with *Controller* class
Spring Boot provides a very powerful decorator *RestControl*, which is very handy to prepare API endpoints.
We initiate a controller class named `ToDoController` like this:
```java
@RestController
public class ToDoController {
    private final ToDoRepository repository;

    public ToDoController(ToDoRepository repository) {
        this.repository = repository;
    }
}
```
Here, we set an instance of `ToDoRepository` as *repository* field to so that it is used for communicating with
the database.

#### Preparing the API endpoints
We add methods in the `ToDoController` class to prepare the API endpoints. We will prepare API to perform the
following things:
* Get all objects by calling `/todo` in a GET request.
* Get a single object by calling `/todo/<id>` in a GET request with the *id* of a `ToDo` instance.
* Create a new `ToDo` instance by submitting an object in `/todo` in a POST request.
* Update an existing `ToDo` instance by calling `/todo/<id>` in a PUT request.
* Delete an existing `ToDo` instance by calling `/todo/<id>` in a DELETE request.

Spring Boot provides decorators such as *GetMapping*, *PostMapping*, *PutMapping*, and *DeleteMapping* for
creating endpoints of each of these request methods. 

##### Get all objects
We add the following method in `ToDoController` class which allows us to get all objects by 
calling `/todo` in a GET request.
```java
    @GetMapping("/todo")
    List<ToDo> findAll() {
        return this.repository.findAll();
    }
```

##### Get a single object by *id*
We add the following method in `ToDoController` class which allows us to get a single object by 
calling `/todo/<id>` in a GET request with the *id* of a `ToDo` instance.
```java
    @GetMapping("/todo/{id}")
    ToDo findById(@PathVariable String id) {
        return this.repository.findById(id).orElseThrow(() -> new RuntimeException("Not found."));
    }
```
Note that, we are throwing a *RuntimeException* if a `ToDo` instance does not exist with the given *id*.

##### Creating a new instance
We add the following method in `ToDoController` class which allows us to
create a new `ToDo` instance by submitting an object in `/todo` in a POST request.
```java
    @PostMapping("/todo")
    ToDo save(@RequestBody ToDo toDo) {
        return this.repository.save(toDo);
    }
```

##### Updating an existing instance
We add the following method in `ToDoController` class which allows us to
update an existing `ToDo` instance by calling `/todo/<id>` in a PUT request.
```java
    @PutMapping("/todo/{id}")
    ToDo replaceById(@RequestBody ToDo newToDo, @PathVariable String id) {
        return this.repository.findById(id)
                .map(toDo -> {
                    toDo.setName(newToDo.getName());
                    toDo.setDone(newToDo.isDone());
                    toDo.setDetails(newToDo.getDetails());
                    return repository.save(toDo);
                })
                .orElseGet(() -> {
                    newToDo.setId(id);
                    return this.repository.save(newToDo);
                });
    }
```
Note that, if we find an existing `ToDo` instance with the provided *id*, we are updating it with the
object submitted as the request body. But if there is no such `ToDo` instance is found, a new instance is
created instead.

##### Delete an instance by *id*
We add the following method in `ToDoController` class which allows us to
delete an existing `ToDo` instance by calling `/todo/<id>` in a DELETE request.
```java
    @DeleteMapping("/todo/{id}")
    void deleteById(@PathVariable String id) {
        this.repository.deleteById(id);
    }
```

### Enable CORS support
We are going to run this Spring Boot project as a standalone backend server and there will be frontend
client applications which will be consuming the APIs. Therefore, it is necessary to enable CORS support so
that, applications of other origins can access the API.

To enable CORS, we add the following code snippet in the `TodobackendApplication` class:
```java
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins("*");
            }
        };
    }
```

## Testing the Rest API endpoints
We are using [Postman](https://www.postman.com/) to test our API endpoints. Postman is a very nice
tool to test and document your API and share across your team.

Please note that, we are running our server in `localhost:8070`. You can change the port from
`src/main/resources/application.properties` file. We have set the port property by this line:
```
server.port=8070
```

#### First, check getting all ToDo entries:
* **Request URL:** http://localhost:8070/todo
* **Request Method:** GET
* **Request Header:** {"Content-Type": "application/json"}

We get the following response from this request:
* **Status:** 200 OK
* **Response Body:**
```json
[]
```
It indicates that the API is working. It returned an empty array because we did not create any ToDo yet.

#### Let's create a couple of ToDo entries:
* **Request URL:** http://localhost:8070/todo
* **Request Method:** POST
* **Request Header:** {"Content-Type": "application/json"}
* **Request Body:**
```json
{
	"name": "Task 1",
	"details": "I have to complete task 1.",
	"done": false
}
```
Response of the above request:
* **Status:** 200 OK
* **Response Body:**
```json
{
    "id": "5f01822da0a607141dc94d30",
    "name": "Task 1",
    "details": "I have to complete task 1.",
    "done": false
}
```

#### The second one:
* **Request URL:** http://localhost:8070/todo
* **Request Method:** POST
* **Request Header:** {"Content-Type": "application/json"}
* **Request Body:**
```json
{
	"name": "Task 2",
	"details": "I have to complete task 2, too.",
	"done": false
}
```
Response of the above request:
* **Status:** 200 OK
* **Response Body:**
```json
{
    "id": "5f018241a0a607141dc94d31",
    "name": "Task 2",
    "details": "I have to complete task 2, too.",
    "done": false
}
```

#### Now, let's check again to read all ToDos:
* **Request URL:** http://localhost:8070/todo
* **Request Method:** GET
* **Request Header:** {"Content-Type": "application/json"}

We get the following response from this request:
* **Status:** 200 OK
* **Response Body:**
```json
[
    {
        "id": "5f01822da0a607141dc94d30",
        "name": "Task 1",
        "details": "I have to complete task 1.",
        "done": false
    },
    {
        "id": "5f018241a0a607141dc94d31",
        "name": "Task 2",
        "details": "I have to complete task 2, too.",
        "done": false
    }
]
```

#### Checking retrieval of individual ToDo by id:
* **Request URL:** http://localhost:8070/todo/5f01822da0a607141dc94d30
* **Request Method:** GET
* **Request Header:** {"Content-Type": "application/json"}

We get the following response from this request:
* **Status:** 200 OK
* **Response Body:**
```json
{
    "id": "5f01822da0a607141dc94d30",
    "name": "Task 1",
    "details": "I have to complete task 1.",
    "done": false
}
```

#### Try updating a ToDo instance:
* **Request URL:** http://localhost:8070/todo/5f01822da0a607141dc94d30
* **Request Method:** PUT
* **Request Header:** {"Content-Type": "application/json"}
* **Request Body:**
```json
{
    "name": "Task 1",
    "details": "I have to complete task 1.",
    "done": true
}
```
We get the following response from this request:
* **Status:** 200 OK
* **Response Body:**
```json
{
    "id": "5f01822da0a607141dc94d30",
    "name": "Task 1",
    "details": "I have to complete task 1.",
    "done": true
}
```

#### Checking if deletion by id is working:
* **Request URL:** http://localhost:8070/todo/5f018241a0a607141dc94d31
* **Request Method:** DELETE
* **Request Header:** {"Content-Type": "application/json"}

We get the following response from this request:
* **Status:** 200 OK

#### Finally, checking if things went as expected by reading all the ToDo entries again:
* **Request URL:** http://localhost:8070/todo
* **Request Method:** GET
* **Request Header:** {"Content-Type": "application/json"}

We get the following response from this request:
* **Status:** 200 OK
* **Response Body:**
```json
[
    {
        "id": "5f01822da0a607141dc94d30",
        "name": "Task 1",
        "details": "I have to complete task 1.",
        "done": true
    }
]
```

Thus, we have confirmed that, all our API endpoints are working as expected.