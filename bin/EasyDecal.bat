@echo off
IF ""%1""=="""" (
	start javaw -showversion -Xmx128m -jar %0\..\easy-decal.jar %*
) ELSE (
	java -showversion -Xmx128m -jar %0\..\easy-decal.jar %*
)
