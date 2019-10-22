FROM adoptopenjdk/openjdk11:alpine-jre
RUN apk --no-cache add curl
WORKDIR /srv
COPY build/menu-generation.jar menu-generation.jar
CMD java -jar menu-generation.jar
