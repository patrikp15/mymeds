FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app

COPY pom.xml ./
COPY ./.mymeds/shared-utilities /root/.m2/repository/com/mymeds/shared-utilities

RUN mvn dependency:go-offline -Dmaven.test.skip=true

COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

# for app
EXPOSE 8020
# for debug
EXPOSE 5005

#CMD ["java", "-jar", "user-service.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "user-service.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}"]
