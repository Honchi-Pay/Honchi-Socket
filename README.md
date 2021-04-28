# Honchi-Socket
Honchi-Socket은 Honchi-Pay에서 사용될 소켓 부분을 따로 레포로 분리해둔 것입니다.  
Socket.io를 활용하여 프로젝트를 진행하였습니다.  
socket에 대한 이해 및 socket.io에 대한 이해가 부족하여 코드의 질이 떨어지는 상태입니다.

## Technology used
- Java 8
- SpringBoot
- socket.io
- MySQL 8.x

## Folder Structure
```
📦Honchi-Socket
├─ .github
│  └─ workflows
│     └─ CI-CD.yml
├─ .gitignore
├─ Dockerfile
├─ README.md
├─ build.gradle
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ src
   └─ main
      ├─ java
      │  └─ com
      │     └─ honchi
      │        └─ socket
      │           ├─ SocketApplication.java
      │           ├─ config
      │           ├─ controller
      │           ├─ domain
      │           │  ├─ chat
      │           │  │  ├─ enums
      │           │  │  └─ repository
      │           │  ├─ message
      │           │  │  ├─ enums
      │           │  │  └─ repository
      │           │  ├─ post
      │           │  │  ├─ enums
      │           │  │  └─ repository
      │           │  └─ user
      │           │     ├─ enums
      │           │     └─ repository
      │           ├─ payload
      │           ├─ security
      │           └─ service
      └─ resources
```