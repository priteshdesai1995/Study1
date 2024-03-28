# Humaine Data Transactional Middleware

## Features
This service is designed and developed for avail metrics data for the
FrontEnd Application by running saveral cron jobs.

Different Cron jobs are shdeuled as below.




## Tech Stack
- Java 11+
- Spring Boot
- Maven 3
- postgreSql 13 +



## Setup Steps

**1.Clone the repository :**

```sh
git clone https://bitbucket.org/humaine-tech-development/transactional-middleware.git
```

**2. specify  the active profile in src/main/resources/application.properties file.(default will be development)**

**3. specify  update database connection properties in respected profile properties file.(for ex. src/main/resources/application-development.properties)**

**4. Run the app using maven**

```bash
cd spring-boot-file-upload-download-rest-api-example
mvn spring-boot:run
```

That's it! The application can be accessed at `http://localhost:8082`.

You may also package the application in the form of a jar and then run the jar file like so -

```bash
mvn clean package
java -jar target/data-transactional-middleware.jar
```

#### Build with docker

**1. Build Docker Image :**

```sh
 docker build -t data-transactional-middleware:1.1 .
```

**2. Create container and run it :**


```sh
 docker run -d --name=data-transactional-middleware -it data-transactional-middleware:1.1
```


## Deployment


## License

MIT

