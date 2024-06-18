FROM openjdk:17

LABEL source="https://github.com/coelhocaique/booking-api"\
      maintainer="Caique Coelho"

ADD ./build/distributions/*.zip /booking-api.zip

RUN unzip booking-api.zip && \
   rm -rf *.zip && \
   mv booking-api-* booking-api

EXPOSE 80 443

ENTRYPOINT [ "/booking-api/bin/booking-api" ]