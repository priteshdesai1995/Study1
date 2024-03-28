# Humaine Portal Transaction API

## Features
This Service is design to retrive , store Humaine AI Portal transaction data.


## Tech Stack
- Java 11+
- Spring Boot
- Maven 3
- postgreSql 13 +



## Setup Steps

**1.Clone the repository :**

```sh
git clone https://bitbucket.org/humaine-tech-development/portal-transactional-api.git
```

**2. specify  the active profile in src/main/resources/application.properties file.(default will be development)**

**3. specify  update database connection properties in respected profile properties file.(for ex. src/main/resources/application-development.properties)**

That's it! The application can be accessed at `http://localhost:8083`.

You may also package the application in the form of a jar and then run the jar file like so -

```bash
mvn clean package
java -jar target/portal-transaction-api.jar
```
#### Build with docker

**1. Build Docker Image :**

```sh
 docker build -t portal-transaction-api:1.1 .
```

**2. Create container and run it :**


```sh
 docker run -d -p 8083:8083 --name=portal-transaction-api -it portal-transaction-api:1.1
```


## Deployment


## License

MIT

