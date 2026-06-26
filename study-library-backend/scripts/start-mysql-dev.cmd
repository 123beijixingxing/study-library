@echo off
setlocal

if "%DEV_DB_URL%"=="" set "DEV_DB_URL=jdbc:mysql://127.0.0.1:3306/study_library?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai"
if "%DEV_DB_DRIVER%"=="" set "DEV_DB_DRIVER=com.mysql.cj.jdbc.Driver"
if "%DEV_DB_USERNAME%"=="" set "DEV_DB_USERNAME=root"
if "%DEV_DB_PASSWORD%"=="" set "DEV_DB_PASSWORD=root123"
if "%DEV_SQL_INIT_MODE%"=="" set "DEV_SQL_INIT_MODE=never"

call mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=28080
