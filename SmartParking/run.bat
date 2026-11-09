@echo off
REM Compile and run the Campus Smart Parking Simulator (Windows)
if not exist bin mkdir bin
javac -d bin src\mypack\*.java src\*.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b 1
)
java -cp bin Main %*
pause
