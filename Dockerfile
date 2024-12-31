FROM amazoncorretto:21-alpine

COPY ./target/booking.jar /opt/booking.jar

WORKDIR /opt/

CMD java -jar booking.jar

EXPOSE 8080

RUN adduser --disabled-password docker-user
USER docker-user
