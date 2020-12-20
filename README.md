# kotlinのフルスタックWebアプリケーションのサンプル

# 開発環境

- JDK: amazoncorretto 11
- [IDE: IntelliJ IDEA CE(最新バージョン)](https://www.jetbrains.com/idea/download/#section=mac)
- DB: MySQL

# 使用ライブラリ

- Ktor - Kotlin async route framework
- Netty - Async route server
- exposed - Kotlin SQL framework
- HikariCP - High performance JDBC connection pooling
- kotlin-react - react.jsのKotlin wrapper

# 開発環境構築

1 . ルートディレクトリに .env ファイルを作成する

```
cp .env.local .env
```

2 . MySql をインストールする

- https://weblabo.oscasierra.net/mysql-56-homebrew-install/

```
brew install mysql@5.7
echo 'export PATH="/usr/local/opt/mysql@5.6/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
mysql.server start
mysql -uroot
```
