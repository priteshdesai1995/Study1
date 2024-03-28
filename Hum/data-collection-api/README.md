# Humaine Data Collection API

## Features
Customers using the platform will need to implement our trackers into their website. 
These trackers will event information to our platform and will power different analysis and serve as part of the input features for our algorithms.

The different User Events that will be captured by the Javascript Tracker are:
- User Site Visit / Login (Begin Session)
- User Page Navigation
- Products Details View
- Add to Cart /  Wishlist
- Product Sale (Buy Product)
- User leaves a Comment / Review for a Product
- User Logout /  Close Window (End Session)



## Tech Stack
- Java 11+
- Spring Boot
- Maven 3
- postgreSql 13 +



## Setup Steps

**1.Clone the repository :**

```sh
git clone https://bitbucket.org/humaine-tech-development/data-collection-api.git
```

**2. specify  the active profile in src/main/resources/application.properties file.(default will be development)**

**3. specify  update database connection properties in respected profile properties file.(for ex. src/main/resources/application-development.properties)**

**4. Run the app using maven**

```bash
cd spring-boot-file-upload-download-rest-api-example
mvn spring-boot:run
```

That's it! The application can be accessed at `http://localhost:8081`.

You may also package the application in the form of a jar and then run the jar file like so -

```bash
mvn clean package
java -jar target/data-collection-api.jar
```
#### Build with docker

**1. Build Docker Image :**

```sh
 docker build -t data-collection-api:1.1 .
```

**2. Create container and run it :**


```sh
 docker run -d -p 8081:8081 --name=data-collection-api -it data-collection-api:1.1
```

#### Check Service Status

To check Service status whether it is up or not call below API endpoint.


```sh
 http://<Host>:<Port>/humaine/collection/api/actuator/health
```

## Deployment


## License

MIT

