FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app

COPY pom.xml ./
COPY ./.mymeds/shared-utilities /root/.m2/repository/com/mymeds/shared-utilities

RUN mvn dependency:go-offline -Dmaven.test.skip=true

COPY src ./src

RUN mvn package -Dmaven.test.skip=true

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/target/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar

# for app
EXPOSE 8000
# for debug
EXPOSE 5004

#CMD ["java", "-jar", "api-gateway.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5004", "-jar", "api-gateway.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}"]