FROM maven:3.9.15-eclipse-temurin-17 AS maven_upstream

FROM eclipse-temurin:11-jdk-alpine

# 作業ディレクトリを設定
WORKDIR /app

# Javaファイルをコピー
COPY src/main/java/com/example/booking_system/BookingSystemApplication.java /app

# Javaファイルをコンパイル
RUN javac src/main/java/com/example/booking_system/BookingSystemApplication.java

# ポート8080を公開
EXPOSE 8080

# アプリケーションを実行
CMD ["java", "BookingSystemApplication"]