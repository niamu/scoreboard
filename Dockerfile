FROM clojure:lein-2.7.1-alpine

COPY . /usr/src/scoreboard/
WORKDIR /usr/src/scoreboard/

RUN apk update && \
    apk add librsvg ffmpeg

RUN lein test
RUN lein uberjar

RUN mkdir -p resources/out/

CMD ["script/run.sh"]
