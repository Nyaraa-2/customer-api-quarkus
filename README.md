![Quarkus](https://camo.githubusercontent.com/62d4f160c458785b41aa5961dbf35295843fd582cb7a931e962c77d295bd23f9/68747470733a2f2f64657369676e2e6a626f73732e6f72672f717561726b75732f6c6f676f2f66696e616c2f504e472f717561726b75735f6c6f676f5f686f72697a6f6e74616c5f7267625f3132383070785f64656661756c742e706e672367682d6c696768742d6d6f64652d6f6e6c79)




# customer-api Project
### Built With
![GitHub top language](https://img.shields.io/github/languages/top/Nyaraa-2/customer-api-quarkus)


This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Swagger UI

Swagger UI allows anyone — be it your development team or your end consumers — to visualize and interact with the API’s resources without having any of the implementation logic in place. It’s automatically generated from your OpenAPI (formerly known as Swagger) Specification, with the visual documentation making it easy for back end implementation and client side consumption.

```shell script
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/swagger-ui/.


## Health

One of the greatest things about Quarkus is the ability to get very powerful base functionality out of the box simply by adding an extension. A great example is the quarkus-smallrye-health extension. Adding this extension to the demo app will illustrate what it can do.
```shell script
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-health</artifactId>
</dependency>
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/health.

```shell script
{
    "status": "UP",
    "checks": [
        {
            "name": "Database connections health check",
            "status": "UP"
        }
    ]
}
```

## Metrics

Metrics are a must-have for any application. It's better to include them early to ensure they're available when you really need them. Luckily, there is a Quarkus extension that makes this easy.
```shell script
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/metrics.


## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/customer-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

[Project Source 👊](https://developers.redhat.com/articles/2022/02/03/build-rest-api-ground-quarkus-20#summary)

