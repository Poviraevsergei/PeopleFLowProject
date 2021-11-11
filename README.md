# PeopleFlow project
Descriptions:

The service makes it possible to store information about employees and also monitor their status.


# Tests
Before starting to use the application, it is recommended to run tests to check its functionality.

To run tests:
> mvn test

# Database
The in memory database h2 is connected to the project. You can connect to it by going to "http://localhost:8080/h2-database/".


# Employees capabilities
Available endpoints:

- "http://localhost:8080/employee/add" – create new employee.

Example:( POST method: "http://localhost:8080/employee/add" )
To do this, you need to pass json format like:( {
"firstname": "Bill",
"lastname": "Gates",
"email": "Genius@mail.ru",
"age":65
} )


- "http://localhost:8080/employee/changeStage" – change stage for employee.

Example: ( POST method: "http://localhost:8080/employee/{id}}" )
To do this, you need to pass json format:( {
"id": 1,
"event": "INCHECK_EVENT"
} )


- "http://localhost:8080/employee/{id}}" – check stage for employee.

Example: ( GET method: "http://localhost:8080/employee/{id}}" )


# Swagger
You can use swagger http://localhost:8080/swagger-api


# How to run the application using docker:
1. > mvn package -DskipTests

2. >docker-compose build

3. >docker-compose up -d


