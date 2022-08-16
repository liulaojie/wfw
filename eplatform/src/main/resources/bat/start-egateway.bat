@echo off
call start-base.bat
java -Dcom.esen.deploy.mode="microservice" -Dcom.esen.route="%cd%\routeconfig.properties" -Desen.ecore.workdir="%ABIDIR%" -Dcom.esen.productid="%PRODUCTID%"  -Dspring.cloud.nacos.discovery.enabled=true -Dspring.cloud.nacos.discovery.server-addr=localhost:8848 -Deureka.client.enabled=false -Dserver.servlet.context-path=/abistudy -jar %JARDIR%\com\esen\excutable\egateway\1.5.1-SNAPSHOT\egateway-1.5.1-SNAPSHOT.jar
pause