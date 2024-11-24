FROM openjdk:17-jdk-slim
COPY ./target/pokebet-0.1.jar /usr/local/lib/pokebet.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/pokebet.jar"]