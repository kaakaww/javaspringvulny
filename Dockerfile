FROM openjdk:11.0.4-jdk

RUN mkdir /javavulny /app
COPY . /javavulny/

RUN cd /javavulny \
&& ./gradlew --no-daemon build \
&& cp build/libs/java-spring-vuly-0.1.0.jar /app/ \
&& rm -Rf build/ \
&& cd / \
&& rm -Rf /javavulny /root/.gradle/

WORKDIR /app
COPY ./db ./db

ENV PWD=/app
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/java-spring-vuly-0.1.0.jar"]
