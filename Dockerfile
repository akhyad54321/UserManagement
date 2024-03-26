FROM anapsix/alpine-java:8
MAINTAINER TABADUL
COPY target/UserManagement.jar /opt/spring-cloud/lib/

# RUN apk add --no-cache tzdata
ENV TZ=Asia/Riyadh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


ADD http://tfsagentdev1.etabadul.com/elastic-apm-agent.jar /opt/spring-cloud/lib/
ENTRYPOINT ["sh", "-c", "java   $JAVA_OPTS -jar /opt/spring-cloud/lib/UserManagement.jar"]

EXPOSE 8188