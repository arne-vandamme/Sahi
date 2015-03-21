@echo off
if exist %SAHI_HOME% goto checkuserdata
set SAHI_HOME=..

:checkuserdata
if exist %SAHI_USERDATA_DIR% goto setuserdatadir 
set SAHI_USERDATA_DIR_TMP=%SAHI_HOME%\userdata
goto startsahi

:setuserdatadir
set SAHI_USERDATA_DIR_TMP=%SAHI_USERDATA_DIR%

:startsahi
set SAHI_CLASS_PATH=%SAHI_HOME%/lib/*

echo --------
echo SAHI_HOME: %SAHI_HOME%
echo SAHI_USERDATA_DIR: %SAHI_USERDATA_DIR_TMP%
echo SAHI_EXT_CLASS_PATH: %SAHI_EXT_CLASS_PATH%
echo --------

java -classpath %SAHI_EXT_CLASS_PATH%;%SAHI_CLASS_PATH% net.sf.sahi.Proxy "%SAHI_HOME%" "%SAHI_USERDATA_DIR_TMP%"
