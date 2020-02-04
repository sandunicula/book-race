# Description

Prototype application that implements the book racing web service.
Users will register to the system, add the books to the read list.

## Tech stack

- http4s as http server/client
- doobie as JDBC layer
- pureconfig for configuration files
- circe for json serialization
- cats-effect for pure side effects

## Run

> sbt run

## Test 

> sbt test