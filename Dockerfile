# JAR and JRE BUILDER
FROM maven:3-eclipse-temurin-11 as builder

COPY ./pom.xml /usr/src/mymaven/
COPY ./src /usr/src/mymaven/src/
WORKDIR /usr/src/mymaven
RUN mvn clean install

RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.xml \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# ACTUAL IMAGE
FROM debian:bullseye-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
# copy jre
COPY --from=builder /javaruntime $JAVA_HOME
# copy app
COPY --from=builder /usr/src/mymaven/target/modbuspal-runtime.jar /opt/app/
# copy samples
COPY ./samples/*.xmpp /projects/
EXPOSE 502
CMD ["java", "-jar", "/opt/app/modbuspal-runtime.jar"]