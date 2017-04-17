:: Batch file to build and run PML Test

@echo off
cls
title PML Test
echo Desktop Gradle Build for PML Test
set SEPARATOR================================================
echo %SEPARATOR%
:: Set paths to directories used by this program
set CURRENT_DIR=%~dp0\
set DESKTOP_ROOT=%CURRENT_DIR%desktop\
set JAR_PATH=%DESKTOP_ROOT%build\libs\desktop-1.0.jar
echo.
:: If a built jar already exists delete it
IF EXIST %JAR_PATH% (
goto deleteJar
) ELSE (
goto build
)

:deleteJar
echo The jar already exists, deleting it...
del %JAR_PATH%
IF EXIST %JAR_PATH% (
goto failedToDelete
) ELSE (
goto build
)

:failedToDelete
:: Failed to delete jar, just exit
echo Failed to delete jar file, aborting build...
pause >NUL
exit

:build
:: Start a gradle build with the gradle wrapper
echo Starting gradle build...
echo.
echo %SEPARATOR%
call gradlew.bat desktop:dist
:: Check if the build was successful by checking for the final jar
IF EXIST %JAR_PATH% (
goto launch
) ELSE (
echo Failed to build jar file, aborting....
)
pause >NUL
exit

:launch
:: Launch the built jar file
echo.
echo %SEPARATOR%
call java -jar %JAR_PATH%
echo %SEPARATOR%
echo.
echo Program ran successfully!
exit