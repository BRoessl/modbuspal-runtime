FROM eclipse-temurin:11
COPY ./testproject.xmpp /opt/config/
COPY ./target/modbuspal-runtime.jar /opt/app/
CMD ["java", "-jar", "/opt/app/modbuspal-runtime.jar", "-f=/opt/config/testproject.xmpp"]