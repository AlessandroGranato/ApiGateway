# Api Gateway
Api Gateway module with Spring boot

## Using Docker to simplify development

Ensure that Docker Desktop or similar are active.

To start a postgresql database in a docker container, run:

```
docker-compose -f application/src/main/docker/postgresql.yml up -d
```

To start a kafka cluster in a docker container, run:

```
docker-compose -f application/src/main/docker/kafka.yml up -d
```

To stop a container (in this example postgres), run:

```
docker-compose -f application/src/main/docker/postgresql.yml stop

```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
