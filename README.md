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
docker run --name boogle-api-gateway --network=boogle-network -e "SPRING_CONFIG_ADDITIONAL_LOCATION=/config/external-props.yml" -v C:\Users\PyroSandro\Desktop\PublicRepos\boogle\boogle-extra\api-gateway-external-props.yml:/config/external-props.yml -dp 127.0.0.1:8080:8080 pyrosandro/boogle-api-gateway-image:0.0.1-SNAPSHOT
```

Command explanation:
1. **docker run:** This is the command used to run a Docker container.
2. **--name boogle-api-gateway:** This option sets the name of the container to "boogle-api-gateway". The --name flag allows you to assign a custom name to the container instead of Docker generating a random one.
3. **--network=boogle-network:** This option specifies the network to which the container should be attached. It connects the container to the Docker network named "boogle-network".
4. **-e "SPRING_CONFIG_ADDITIONAL_LOCATION=/config/external-props.yml":** This option sets an environment variable within the container. It defines an additional location for Spring configuration properties (external-props.yml). This environment variable allows the application inside the container to load configuration from an external file.
5. **-v C:\Users\PyroSandro\Desktop\PublicRepos\boogle\boogle-extra\api-gateway-external-props.yml:/config/external-props.yml:** This option mounts a volume from the host machine to the container. It maps the local file external-props.yml located on the host machine's desktop (C:\Users\PyroSandro\Desktop\PublicRepos\boogle\boogle-extra\api-gateway-external-props.yml) to the container's /config/external-props.yml path. This volume mounting allows the containerized application to access configuration files from the host machine.
6. **-dp 127.0.0.1:8080:8080:** This option specifies the port mapping for the container. It maps port 8080 on the container to port 8080 on the host machine (127.0.0.1). The -d flag runs the container in detached mode (in the background), and the -p flag specifies the port mapping.
7. **pyrosandro/boogle-api-gateway-image:0.0.1-SNAPSHOT:** This part of the command specifies the Docker image to use for creating the container. It specifies the image "pyrosandro/boogle-api-gateway-image" with the tag "0.0.1-SNAPSHOT".

Note: The external-props.yml file should contain the values of the variables needed in application.yml file. For an example, you can see the file application-localdev.yml

## Deploy artifacts and docker images

### Deploy artifacts on github packages
To deploy artifacts on github packages, ensure that in pom.xml you have set up the distribution management that allows you to specify to which repo you will push your artifact
```
<distributionManagement>
    <repository>
        <id>my-github-repos</id>
        <name>GitHub alessandrogranato api gateway repo</name>
        <url>https://maven.pkg.github.com/alessandrogranato/ApiGateway</url>
    </repository>
</distributionManagement>
```

To deploy the artifacts, simply run the following command:

```
mvn clean deploy
```

### Deploy docker images on dockerhub
To deploy application and db docker images, go on parent pom folder and launch the following command:
```
mvn clean deploy -Plocal-image -Pdeploy-docker-image
```
This command will create the docker images from packager and packager-db submodules using the profile local-image, then they will be deployed using the profile deploy-docker-image.

### Deploy artifacts and docker images all at once
Since with mvn clean deploy we push artifacts on github packages and adding -Plocal-image and -Pdeploy-doker-image we add profiles to create and push docker images on docker repos, we can simply go in parent folder (where there is parent pom.xml file) and launch the following command to upload everything together. (Yes, it's equal to the previous command)
```
mvn clean deploy -Plocal-image -Pdeploy-docker-image
```