FROM amazoncorretto:11
COPY target/*.jar app.jar
# меняем TZ при тестировании через Postman:
# ENV TZ="Europe/Moscow"
ENTRYPOINT ["java","-jar","/app.jar"]