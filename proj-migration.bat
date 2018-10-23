@echo off

call mvn clean package -DskipTests

call mvn com.to8to:migration-maven-plugin:1.0-SNAPSHOT:migrate