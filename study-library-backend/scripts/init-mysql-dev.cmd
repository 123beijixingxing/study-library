@echo off
setlocal

if "%MYSQL_CLIENT%"=="" set "MYSQL_CLIENT=C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe"
if "%DEV_DB_NAME%"=="" set "DEV_DB_NAME=study_library"
if "%DEV_DB_USERNAME%"=="" set "DEV_DB_USERNAME=root"
if "%DEV_DB_PASSWORD%"=="" set "DEV_DB_PASSWORD=root123"

"%MYSQL_CLIENT%" -u%DEV_DB_USERNAME% -p%DEV_DB_PASSWORD% -e "CREATE DATABASE IF NOT EXISTS %DEV_DB_NAME% DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;"
"%MYSQL_CLIENT%" -u%DEV_DB_USERNAME% -p%DEV_DB_PASSWORD% %DEV_DB_NAME% < sql\init\001_init_schema.sql
"%MYSQL_CLIENT%" -u%DEV_DB_USERNAME% -p%DEV_DB_PASSWORD% %DEV_DB_NAME% < sql\init\002_seed_demo_data.sql
