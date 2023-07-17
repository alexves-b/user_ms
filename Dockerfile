FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/usersMicroservice-0.0.1-SNAPSHOT.jar
WORKDIR users
COPY ${JAR_FILE} users_microservice.jar
EXPOSE 8085
CMD ["java","-jar", "users_microservice.jar"]