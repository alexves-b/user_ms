FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/usersMicroservice-0.0.1-SNAPSHOT.jar
WORKDIR users
EXPOSE 8085
COPY ${JAR_FILE} users_microservice.jar
CMD ["java","-jar", "users_microservice.jar"]
