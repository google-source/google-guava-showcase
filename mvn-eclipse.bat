@echo off
echo genrate eclipse project for all subprojects.

cd %~dp0

call mvn eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true

cd %~dp0

pause