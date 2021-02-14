# Prerequisites

* Docker installed
* Docker Compose
* Java (11 Source Compatibility)

# How to run

1. Run RabbitMQ container

```shell script
$ docker-compose up -d
```

2. Run pubApp
 ```shell script
$ cd pubApp/
$ ./gradlew bootRun
 ```
 
3. Run secApp 
 ```shell script
$ cd secApp/
$ ./gradlew bootRun
 ```

Alternatively, you can build the JAR (e.x.):
 ```shell script
$ ./gradlew build
$ java -jar build/libs/gs-rest-service-0.1.0.jar
```