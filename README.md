
# 环境
jdk: 1.8

# 环境启动步骤
## 产品工作目录和jdbc.conf准备
注意：此处提及的工作目录，另外创建一个工作目录，用于产品，并非eclipse的工作目录或idea的工作目录。注意区分。
创建一个工作目录路径如：C:/develop/workdir/abistudy
在工作目录的路径下创建conf目录，并放入jdbc.conf

[Oracle创建表空间创建用户和赋权](http://192.168.1.200/devwiki/pages/viewpage.action?pageId=392331478)

```conf

#charset UTF-8
#Thu May 21 15:52:56 CST 2020
#catalog=
# (日志的级别，设置为debug时，是调试模式，会把sql语句打印到控制台，启动较慢)
logLevel=ERROR 
maxWait=10000
maxActive=20
url=jdbc\:oracle\:thin\:@127.0.0.1\:1521\:orcl
driverClassName=oracle.jdbc.driver.OracleDriver
# 账号密码改成自己的数据库账号密码，注意：不要用sys、scott账号 
username=abistudy
password=abistudy
```

## 启动redis
启动redis，即启动安装目录下的redis-server.exe。 【参考[Redis安装](http://192.168.1.200/devwiki/pages/viewpage.action?pageId=127729691) ，安装Redis-x64-3.2.100.msi和客户端redis-desktop-manager-0.9.3.817.exe】

## 启动nacos
启动注册中心nacos，即运行nacos\bin下的启动脚本（windows环境如startup.cmd）。
nacos下载地址 http://home.esensoft.com/edoc/doc/os/down.do?rid=EDOC$22$853663360cc144f5990a7271158f4630
解压缩 nacos-server-2.0.3.zip，即运行nacos\bin下的启动脚本（windows环境如startup.cmd）。访问http://localhost:8848/nacos/ 。账号nacos/nacos

## 启动服务

1、调整eplatform工程的start-base.bat中的ABIDIR 【根据个人电脑实际情况配置工作目录的路径，形如set ABIDIR=C:/develop/workdir/abistudy】（这里的文件夹即上述提到的产品工作目录）

2、调整eplatform工程的start-base.bat中JARDIR 【根据个人电脑实际情况配置maven仓库的.m2目录的路径，形如set JARDIR=C:/.m2/repository】

3、调整eplatform工程的start-base.bat中PRODUCTID【配置产品名称，形如set PRODUCTID=abi】

***产品名称，约定ABI是abi，睿治是edg。***

***这里配置的产品名称，必须跟后面启动各服务的XXApplication的JVM参数com.esen.productid的值中保持一致。***

**这里是以 ABI 产品来启动的，所以都应当配置为-Dcom.esen.productid=abi。**

4、检查eplatform工程的start-egateway.bat配置，如-Dserver.servlet.context-path配置的是上下文根的路径，如无问题启动bat文件，启动完成后可以在nacos服务列表看到

5、启动各服务
启动下面的服务。注意下面这些XXApplication启动前均需配置JVM参数，形如

```properties
-Desen.ecore.workdir=C:/develop/workdir/abistudy
-Dcom.esen.productid=abi
-Dspring.cloud.nacos.discovery.enabled=true
-Deureka.client.enabled=false
-Dcom.esen.deploy.mode=microservice
-Dcom.esen.feign.loadbalance.enabled=true
-Dcom.esen.sys.enableservice=true
-Dspring.redis.host=127.0.0.1
-Dspring.redis.port=6379
-Dspring.redis.password=
-Xmx800m
```

**idea如果启动失败，注意看wiki http://192.168.1.200/devwiki/pages/viewpage.action?pageId=526188632 中的评论 **

