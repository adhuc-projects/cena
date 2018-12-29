FROM openjdk:11-slim
RUN apt-get update && apt-get install -y --no-install-recommends curl
WORKDIR /srv
COPY build/menu-generation.jar menu-generation.jar
CMD java -jar menu-generation.jar
