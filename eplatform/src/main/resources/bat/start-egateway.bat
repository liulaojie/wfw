@echo off
call start-base.bat
java -Dcom.esen.deploy.mode="microservice" -Dcom.esen.route="%cd%\routeconfig.properties" -Desen.ecore.workdir="%ABIDIR%" -Dcom.esen.productid="%PRODUCTID%" -jar %JARDIR%\com\esen\excutable\egateway\1.5.1-SNAPSHOT\egateway-1.5.1-SNAPSHOT.jar
pause