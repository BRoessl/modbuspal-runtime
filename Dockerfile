FROM eclipse-temurin:11
COPY ./testproject.xmpp /opt/app/sampleprojects/
COPY ./unittestv2.xmpp /opt/app/sampleprojects/
COPY ./target/modbuspal-runtime.jar /opt/app/
EXPOSE 502
CMD ["java", "-jar", "/opt/app/modbuspal-runtime.jar"]