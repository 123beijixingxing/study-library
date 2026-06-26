$mysqlClient = if ($env:MYSQL_CLIENT) { $env:MYSQL_CLIENT } else { 'C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe' }
$dbName = if ($env:DEV_DB_NAME) { $env:DEV_DB_NAME } else { 'study_library' }
$dbUsername = if ($env:DEV_DB_USERNAME) { $env:DEV_DB_USERNAME } else { 'root' }
$dbPassword = if ($env:DEV_DB_PASSWORD) { $env:DEV_DB_PASSWORD } else { 'root123' }

& $mysqlClient -u$dbUsername -p$dbPassword -e "CREATE DATABASE IF NOT EXISTS $dbName DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;"
Get-Content 'sql/init/001_init_schema.sql' | & $mysqlClient -u$dbUsername -p$dbPassword $dbName
Get-Content 'sql/init/002_seed_demo_data.sql' | & $mysqlClient -u$dbUsername -p$dbPassword $dbName
