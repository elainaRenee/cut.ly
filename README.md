# cut.ly

A URL shortener written in Java using Spring Boot and MongoDB

## Requirements

Develop an HTTP-based RESTful API for managing Short URLs and redirecting
clients similar to bit.ly or goo.gl. Be thoughtful that the system must eventually support millions
of short urls. 

A Short Url:
1. Has one long url
2. Permanent; Once created
3. Is Unique; If a long url is added twice it should result in two different short urls.
4. Not easily discoverable; incrementing an already existing short url should have a low
probability of finding a working short url.

The solution must support:

1. Generating a short url from a long url
2. Redirecting a short url to a long url within 10 ms.
3. Listing the number of times a short url has been accessed in the last 24 hours, past
week and all time.
4. Persistence (data must survive computer restarts)

Shortcuts

1. No authentication is required
2. No html, web UI is required
3. Transport/Serialization format is your choice, but the solution should be testable via curl
4. Anything left unspecified is left to your discretion

## Design Decisions


## Getting Started

To build and run the application with Docker:

    docker-comopose up

To build and run the application without Docker, you will need to install MongoDB.
You can find instructions here on how to install MongoDB: https://docs.mongodb.com/manual/administration/install-community/.
Once MongoDB is up and running, you can build and run the application with the following commands: 
 
    mvn clean install
    cd target/
    java -jar cutly-1.0.0.jar
    
## REST APIs

The web server will run on port 8080 by default.

### Create short url
```
POST /api/v1/urls
```

##### Description: 

generates a unique hash from a long url

##### Request: 
```
{
  "longUrl" : "www.google.com"
}
```
##### Response:
```
{
  "hash": "jR"
}
```
### Get long url   
```
GET /api/v1/urls/{hash}
```
##### Description: 

redirects user to a long url

### Get all-time url usage
```
GET /api/v1/urls/{hash}/usage
```
##### Description: 

returns all dates a short url has been visited and the total count
##### Response:
```
{
  "totalUsage": "1",
  "accessDates": ["05/30/2018 02:35:05"]
}
```
### Get url usage within the last x days
```
GET /api/v1/urls/{hash}/usage?days=7
```
##### Description: 

returns the dates a short url has been visited within the last x days and the total count
     
##### Response:
```
{
  "totalUsage": "2",
  "accessDates": ["05/30/2018 02:35:05", "05/30/2018 02:35:05"]
}
```

## Testing

To insert dummy data into MongoDB for performance testing, run the DataLoadTest.

    mvn -Dtest=DataLoadTest#insertDocuments -Dmaven.test.skip=false test
    
This will generate 3,000,000 short urls. A hash that can be used for testing is kqwWY. 
To view redirect time, see the log output. Initial testing shows redirect time is typically between 5ms to 10ms.

![Alt text](logOutput.png?raw=true)f


