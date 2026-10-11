@echo off
where mvn >nul 2>nul
if %errorlevel%==0 (mvn %*) else (echo Maven is not installed. Open backend in IntelliJ IDEA or install Maven. & exit /b 1)
