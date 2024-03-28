# Using AWS backed docker image for java workloads
FROM amazoncorretto:11-alpine-jdk

# Copying the jar file
COPY target/data-collection-api.jar data-collection-api.jar

# Copying entrypoint script
COPY ./cicd/entry.sh ./cicd/entry.sh

# Setting entry.sh as executable
RUN apk add jq && \
    chmod +x ./cicd/entry.sh

# Setting entry.sh as entrypoint of the container
ENTRYPOINT ["./cicd/entry.sh"]