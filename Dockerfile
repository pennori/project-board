# OpenJDK 17을 기반으로 한 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 생성 및 설정
WORKDIR /app

# 프로젝트 빌드 산출물을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 컨테이너 외부에 노출할 포트를 기술 (기본 포트 8080)
EXPOSE 8080

# 컨테이너 시작 시 jar 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]