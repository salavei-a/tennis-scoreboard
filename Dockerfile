FROM maven:3 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package

FROM tomcat:jre17
COPY --from=build /app/target/*.war $CATALINA_HOME/webapps/ROOT.war
CMD ["catalina.sh", "run"]