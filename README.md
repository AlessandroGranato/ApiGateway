# Api gateway
Api gateway module with Spring boot

## Using Docker to deploy
The steps to run the application in docker are the following:

### Ensure you have a working Docker instance
Ensure that Docker Desktop or similar are active.

### Ensure you have a docker network already up
If you don't have it, run the following command:
```
docker network create boogle-network
```

### Create docker images
To create the docker images of the db and the application, go on the root of the project and run:
```
mvn clean install -Plocal-image
```

### Run app docker container

To run a docker app container, run the following command and populate the variables as needed.

```
docker run --name boogle-api-gateway --network=boogle-network -e "SPRING_CONFIG_ADDITIONAL_LOCATION=/config/external-props.yml" -v C:\Users\PyroSandro\Desktop\PublicRepos\boogle-extra\api-gateway-external-props.yml:/config/external-props.yml -dp 127.0.0.1:8080:8080 pyrosandro/boogle-api-gateway-image:0.0.1-SNAPSHOT
```

Command explanation:
1. **docker run:** This is the command used to run a Docker container.
2. **--name boogle-api-gateway:** This option sets the name of the container to "boogle-api-gateway". The --name flag allows you to assign a custom name to the container instead of Docker generating a random one.
3. **--network=boogle-network:** This option specifies the network to which the container should be attached. It connects the container to the Docker network named "boogle-network".
4. **-e "SPRING_CONFIG_ADDITIONAL_LOCATION=/config/external-props.yml":** This option sets an environment variable within the container. It defines an additional location for Spring configuration properties (external-props.yml). This environment variable allows the application inside the container to load configuration from an external file.
5. **-v C:\Users\PyroSandro\Desktop\PublicRepos\boogle-extra\api-gateway-external-props.yml:/config/external-props.yml:** This option mounts a volume from the host machine to the container. It maps the local file external-props.yml located on the host machine's desktop (C:\Users\PyroSandro\Desktop\PublicRepos\boogle-extra\api-gateway-external-props.yml) to the container's /config/external-props.yml path. This volume mounting allows the containerized application to access configuration files from the host machine.
6. **-dp 127.0.0.1:8080:8080:** This option specifies the port mapping for the container. It maps port 8080 on the container to port 8080 on the host machine (127.0.0.1). The -d flag runs the container in detached mode (in the background), and the -p flag specifies the port mapping.
7. **pyrosandro/boogle-api-gateway-image:0.0.1-SNAPSHOT:** This part of the command specifies the Docker image to use for creating the container. It specifies the image "pyrosandro/boogle-api-gateway-image" with the tag "0.0.1-SNAPSHOT".

Note: The external-props.yml file should contain the values of the variables needed in application.yml file. For an example, you can see the file application-localdev.yml 