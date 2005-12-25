@echo off
java -showversion -Xmx128m -cp easy-decal.jar;lib\simple-log.jar;lib\gui-commands-1.1.42.jar;lib\explicitTableBuilder-0.1.24.jar;lib\elcore.jar;lib\foxtrot.jar;lib\jdic.jar;lib\jdic_stub.jar;lib\jdic-native;lib\debug org.grlea.games.hl.decal.EasyDecal %*

IF "%ERRORLEVEL%" == "0" goto good
pause
:good
