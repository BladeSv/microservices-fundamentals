@echo off 
setlocal
IF "%1"=="-v" SET version=%2
./gradlew jibDockerBuild -Dimage_version=%version%
endlocal