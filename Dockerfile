FROM eclipse-temurin:11-jre-alpine

# 作業ディレクトリを設定
WORKDIR /app

# Javaファイルをコピー
COPY BookingSystemApplication.java /app

# Javaファイルをコンパイル
RUN javac BookingSystemApplication.java

# ポート8080を公開
EXPOSE 8080

# アプリケーションを実行
CMD ["java", "BookingSystemApplication"]