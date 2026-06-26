$env:DEV_DB_URL = if ($env:DEV_DB_URL) { $env:DEV_DB_URL } else { 'jdbc:mysql://127.0.0.1:3306/study_library?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai' }
$env:DEV_DB_DRIVER = if ($env:DEV_DB_DRIVER) { $env:DEV_DB_DRIVER } else { 'com.mysql.cj.jdbc.Driver' }
$env:DEV_DB_USERNAME = if ($env:DEV_DB_USERNAME) { $env:DEV_DB_USERNAME } else { 'root' }
$env:DEV_DB_PASSWORD = if ($env:DEV_DB_PASSWORD) { $env:DEV_DB_PASSWORD } else { 'root123' }
$env:DEV_SQL_INIT_MODE = if ($env:DEV_SQL_INIT_MODE) { $env:DEV_SQL_INIT_MODE } else { 'never' }

mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=28080
