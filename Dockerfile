FROM openjdk:11-slim
WORKDIR /srv
COPY build/menu-generation.jar menu-generation.jar
CMD java -jar menu-generation.jar
