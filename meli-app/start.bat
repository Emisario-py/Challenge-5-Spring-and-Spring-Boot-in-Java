@echo off
title MELI-APP Profile Manager
color 0A

echo ==========================================
echo 🚀 MELI-APP – Start Script
echo ==========================================
echo.
echo Use: start.bat [profile]
echo or write the script when it's required.
echo.
echo Examples:
echo    start.bat dev
echo    start.bat test
echo    start.bat prod
echo.

if "%~1"=="" (
    set /p SPRING_PROFILES_ACTIVE="👉 Add the profile (dev / test / prod): "
) else (
    set SPRING_PROFILES_ACTIVE=%~1
)

echo.
echo Profile Selected: %SPRING_PROFILES_ACTIVE%
echo ------------------------------------------

if /I "%SPRING_PROFILES_ACTIVE%"=="dev" goto dev
if /I "%SPRING_PROFILES_ACTIVE%"=="test" goto test
if /I "%SPRING_PROFILES_ACTIVE%"=="prod" goto prod

echo ❌ Invalid profile. Use 'dev', 'test' o 'prod'.
goto end


:dev
echo 🧩 Running MELI-APP in DEVELOP mode...
mvn spring-boot:run
goto end


:test
echo 🧪 Running tests in TEST mode...
mvn test
goto end


:prod
echo 🚀 Running MELI-APP in PRODUCTION mode...
if exist .env (
    echo Cargando variables desde .env...
    for /f "tokens=1,2 delims==" %%a in (.env) do (
        if not "%%a"=="" (
            set %%a=%%b
        )
    )
) else (
    echo ⚠️  The .env file is not found, make sure that it exist and you have it configured.
)
mvn spring-boot:run
goto end


:end
echo.
echo ==========================================
echo ✅ Execution Finalized.
echo Press any bottom to exit...
pause >nul
