1.调整start-base.bat文件的工作目录ABIDIR，仓库根目录JARDIR和jdk目录JAVA_HOME
2.工作目录ABIDIR中需要增加conf文件夹及jdbc.conf文件配置好数据库，
3.启动redis服务器
4.注册中心nacos下载地址http://home.esensoft.com/edoc/doc/os/down.do?rid=EDOC$22$853663360cc144f5990a7271158f4630，启动完成后可以nacos/nacos（账号名密码）访问http://localhost:8848/nacos/index.html 
5.注册中心启动后，启动网关start-egateway.bat和平台服务EplatformAppliction
6.平台服务EplatformAppliction需配置环境变量如下
-Dcom.esen.deploy.mode=microservice -Desen.ecore.workdir=D:\esendev\workdir\esysmgr
其他各需要单独启动的服务，也要配置上述环境变量
7.启动abiweb前端服务
8.访问localhost:8080/welcome.do进行访问主页
9.如果增加了服务，可在routeconfig.properties文件中增加网关映射，路径和服务的映射，增加的服务同样需要配置-Dcom.esen.deploy.mode=microservice -Desen.ecore.workdir=启动参数

开发时多个节点连入到同一个微服务环境（把自己的服务连入其他开发者机器上的微服务环境），需要注意：
（1）多个服务节点的DB数据库配置（工作目录下conf\jdbc.conf）需要与微服务整体配置保持一致
（2）多个服务节点的Redis需要与微服务整体配置保持一致，默认不配置的话是localhost:6379无密码，其他节点需要在application.properties中配置spring.redis.host/spring.redis.port为微服务所共用的上Reids配置信息
（3）多个服务节点的Jvm启动参数需要配置-Dcom.esen.deploy.mode=microservice -Desen.ecore.workdir=服务器自己的工作目录
（4）多个服务节点的Nacos配置需要与微服务整体配置保持一致的指向地址，spring.cloud.nacos.discovery.server-addr=localhost:8848

如何添加上下文根？
开发环境：
在网关工程egateway的Jvm启动参数中，加入配置：-Dserver.servlet.context-path=abi551

开发时，同一个服务，如何在同一个节点上启动多个实例？
(Eclipse中)Debug configuration中复制一份启动配置，然后在Jvm启动参数中配置一个新的端口-Dserver.port=XXXX，端口号需要唯一，与机器上已开启的端口（含本节点启动的其他服务端口不同）

Windows控制台黑窗口经常报Timeout超时错？
Windows控制台会阻塞IO导致中断进程执行，需要在控制台窗口标题上右键修改属性标签页“选项”中的“编辑选项”中，取消勾选“快速插入模式”
