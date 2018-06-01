# cut.ly

A URL shortener written in Java using Spring Boot and MongoDB

## Requirements

## Design Decisions


## Getting Started

To build and run the application with Docker:

    docker-comopose up

To build and run the application without Docker, you will need to install MongoDB.
You can find instructions here: https://docs.mongodb.com/manual/administration/install-community/
Once MongoDB intstalled, you can build and run the application with the following commands: 
 
    mvn clean install
    cd target/
    java -jar cutly-1.0.0.jar
    
## REST APIs

```
POST /api/v1/urls

{
  "longUrl" : www.google.com
}
```

| resource             | request method | description                       |
|:---------------------|:---------------|:----------------------------------|
| `/api/v1/urls`       |  POST          | returns a new short url           |

## Testing

To insert dummy data into MongoDB for performance testing, run the DataLoadTest

    mvn -Dtest=DataLoadTest#insertDocuments -Dmaven.test.skip=false test
    
This will generate 3,000,000 documents. For redirect time, see the log output. 
Initial testing shows redirect time is typically between 5ms to 10ms




