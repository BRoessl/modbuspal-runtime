# modbuspal-runtime
modbus tcp simulator - headless docker runtime for modbuspal configurations

fork of https://sourceforge.net/projects/modbuspal/ but heavily modfied to extract just a headless docker runtime. The idea is to use original modbuspal as UI tool to export a project files which you can then use to feed in to the runtime.

__major changes to the original modbuspal:__

- removed all GUI elements to get rid of Swing, AWT, JFreeChart dependencies
- code for rxtx and jpython is disabled and removed, so only modbus tcp will work and only linear, random and sine generators. __scripts and serial won't work__.
- converted to maven project with java 11 and java module system support
- added docker support
- record feature is enabled and ouputs to console, with human readable timestamps
- no support for learn feature
- always starts on port 502 (if other port is needed, use it as docker container and map it accordingly)

## Java
### how to build
this project uses maven as build tool, just
`mvn clean install`
and you get an executable jar under /target/modbuspal-runtime.jar.

### how to run
you can add your xmpp project from modbuspal as first argument:

`java -jar .\target\modbuspal-runtime.jar .\samples\project01.xmpp`

or you can set MODBUSPAL_PROJECT environment variable as path to your project file and then just run

`java -jar .\target\modbuspal-runtime.jar`


## Docker
The included Docker file is a multistage build which builds the jar, a slim jre and copies jre+jar to a debian-slim image.
### how to build
`docker build -t modbuspalruntime .`
### how to run
you can either run with a included sample project

`docker run -p502:502 -e MODBUSPAL_PROJECT=/projects/project01.xmpp modbuspalruntime`

or mount your project and refer to it via environment variables

`docker run -p502:502 -v ${path-to-my-projects}:/projects -e MODBUSPAL_PROJECT=/projects/my-project01.xmpp modbuspalruntime`
