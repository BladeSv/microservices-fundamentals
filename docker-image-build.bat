@echo off 
setlocal
IF "%1"=="-v" SET version=%2
./gradlew bootBuildImage -Dimage_version=%version%
endlocal