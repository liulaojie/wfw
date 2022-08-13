@echo off
set ABIDIR=D:\esendev\workdir\abi551
set JARDIR=C:\Users\ESENSOFT\.m2\repository
set PRODUCTID=abi
rem 注意与环境变量中配置的路径保持一致
set JAVA_HOME=D:\esendev\jdk1.8
set CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;
set PATH=%JAVA_HOME%\bin;
