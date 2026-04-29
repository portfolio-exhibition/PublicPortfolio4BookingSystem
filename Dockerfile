# ベースイメージを指定
FROM maven:3.8.3-openjdk-17-slim AS build

# 作業ディレクトリを指定
WORKDIR /app

# Maven Wrapperのセットアップ
COPY .mvn .mvn

# Mavenプロジェクトをビルド
COPY pom.xml .
RUN mvn -B dependency:go-offline

# アプリケーションのビルド
COPY src src
RUN mvn -B package -DskipTests

# JREベースイメージを設定
FROM openjdk:17-jdk-slim

# アプリケーションのJARファイルをコピー
COPY --from=build /app/target/*.jar /app/app.jar

#デフォルトポート番号である10000番に合わせた
EXPOSE 10000

# アプリケーションの実行
CMD ["java", "-jar", "/app/app.jar"]