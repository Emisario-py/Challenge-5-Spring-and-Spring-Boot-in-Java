@echo off
title MELI-APP Profile Manager
color 0A

echo ==========================================
echo 🚀 MELI-APP – Start Script
echo ==========================================
echo.
echo Usa: start.bat [perfil]
echo o escribe el perfil cuando se te pida.
echo.
echo Ejemplos:
echo    start.bat dev
echo    start.bat test
echo    start.bat prod
echo.

:: Si no se pasa un parámetro, pedirlo al usuario
if "%~1"=="" (
    set /p SPRING_PROFILES_ACTIVE="👉 Ingresa el perfil (dev / test / prod): "
) else (
    set SPRING_PROFILES_ACTIVE=%~1
)

echo.
echo Perfil seleccionado: %SPRING_PROFILES_ACTIVE%
echo ------------------------------------------

:: Validar perfil
if /I "%SPRING_PROFILES_ACTIVE%"=="dev" goto dev
if /I "%SPRING_PROFILES_ACTIVE%"=="test" goto test
if /I "%SPRING_PROFILES_ACTIVE%"=="prod" goto prod

echo ❌ Perfil no válido. Usa 'dev', 'test' o 'prod'.
goto end


:dev
echo 🧩 Iniciando MELI-APP en modo DESARROLLO...
mvn spring-boot:run
goto end


:test
echo 🧪 Ejecutando tests en modo PRUEBAS...
mvn test
goto end


:prod
echo 🚀 Iniciando MELI-APP en modo PRODUCCIÓN...
if exist .env (
    echo Cargando variables desde .env...
    for /f "tokens=1,2 delims==" %%a in (.env) do (
        if not "%%a"=="" (
            set %%a=%%b
        )
    )
) else (
    echo ⚠️  No se encontró el archivo .env, asegúrate de tenerlo configurado.
)
mvn spring-boot:run
goto end


:end
echo.
echo ==========================================
echo ✅ Ejecución finalizada.
echo Presiona cualquier tecla para salir...
pause >nul
