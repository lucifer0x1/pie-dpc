FROM azul/zulu-openjdk-alpine
COPY ./*.jar /usr/local/application/app.jar
WORKDIR /usr/local/application/
#ENV JAVA_OPTS="-Xmx1024M -Xms512"
ENTRYPOINT ["sh", "-c", "source /etc/profile && java $JAVA_OPTS -jar app.jar"]