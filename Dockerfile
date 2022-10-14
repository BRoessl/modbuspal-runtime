FROM eclipse-temurin:11 as jre-build

# Create slim JRE
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.xml \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Copy slim JRE
FROM debian:bullseye-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

# Copy App and Samples
COPY ./project01.xmpp /test/
COPY ./project02.xmpp /test/
COPY ./target/modbuspal-runtime.jar /opt/app/
EXPOSE 502
CMD ["java", "-jar", "/opt/app/modbuspal-runtime.jar"]