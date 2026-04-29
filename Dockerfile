FROM openjdk:11-slim

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