FROM openjdk:9-jdk-slim-buster

ENV LDASERVER_HOME /usr/local/lda-server
ENV PATH $LDASERVER_HOME/bin:$PATH
RUN mkdir -p "$LDASERVER_HOME"
RUN mkdir -p "$LDASERVER_HOME/bin"
RUN mkdir -p "$LDASERVER_HOME/target"
WORKDIR $LDASERVER_HOME

COPY ./target/lda-server-standalone.jar $LDASERVER_HOME/target
COPY ./bin/start $LDASERVER_HOME/bin/start
COPY ./pipes.pip $LDASERVER_HOME/
COPY ./lda.mdl $LDASERVER_HOME/

EXPOSE 7777
CMD ["/usr/local/lda-server/bin/start"]
