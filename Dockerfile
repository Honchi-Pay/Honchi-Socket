FROM openjdk:8-jre-slim
COPY ./build/libs/*.jar honchi_socket.jar
ENTRYPOINT ["java", "-Xmx200m", "-jar", "-Duser.timezone=Asia/Seoul", "/honchi_socket.jar"]
EXPOSE 3000
