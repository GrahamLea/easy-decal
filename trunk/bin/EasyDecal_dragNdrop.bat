@echo off

if ""%1""=="" goto usage

java -showversion -Xmx128m -jar %0\..\easy-decal.jar %*
goto end

:usage

echo usage: EasyDecal_dragNdrop.bat image_file [image_file ...]
echo.
echo           OR
echo.
echo        Simply drag image files and drop onto the batch file

:end

echo.
pause
