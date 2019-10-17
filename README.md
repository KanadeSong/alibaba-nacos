# alibaba-nacos
下载地址：https://github.com/alibaba/nacos
官方文档：https://nacos.io/zh-cn/docs/what-is-nacos.html

安装和启动nacos-server（bin目录下直接双击执行startup.cmd文件）
访问：http://127.0.0.1:8848/nacos/index.html ，默认的账号密码为nacos/nacos

Nacos 能够帮助我们发现、配置和管理微服务。其提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。

## 1.服务的生产者
nacos-provide项目
## 2.服务的消费者
nacos-consumer项目
## 3.配置文件

Data ID：它的命名规则为：${prefix}-${spring.profile.active}.${file-extension}

- prefix 默认为 spring.application.name 的值，也可以通过配置项 spring.cloud.nacos.config.prefix 来配置。
- spring.profile.active 即为当前环境对应的 profile，可以通过配置项 spring.profile.active 来配置。
- file-exetension 为配置内容的数据格式，可以通过配置项 spring.cloud.nacos.config.file-extension 来配置。目前只支持 properties 和 yaml 类型。

注意：当 spring.profile.active 为空时，对应的连接符 - 也将不存在，dataId 的拼接格式变成 {prefix}.prefix.{file-extension}

这里我创建Data Id 为nacos-config.yml的配置文件，其中Group为默认的DEFAULT_GROUP，配置文件的格式也相应的选择yaml，其内添加配置nacos.config=hello_nacos

注：springboot中配置文件的加载是存在优先级顺序的，bootstrap优先级高于application


## 4.多环境下如何“读取”Nacos中相应的配置


### 1) 通过其中的spring.profile.active属性可进行多环境下配置文件的读取

Data ID：它的命名规则为：${prefix}-${spring.profile.active}.${file-extension}

创建配置文件Data ID为：nacos-config-dev.yml 和 nacos-config-test.yml (后缀一定要加.yml)

通过Idea启动nacos-config项目，并指定spring.profiles.active(Active profiles: dev/test),通过不同的环境进行启动

### 2) Group方案

- 创建配置文件

Data ID为：nacos-config.yml, Group为：DEV_GROUP 和 nacos-config.yml, Group为：TEST_GROUP

- 修改项目中的配置文件bootstrap.yml

在spring:cloud:nacos:config:group，指定配置文件所在的group，可配置为DEV_GROUP或TEST_GROUP

注：这种方式不太推荐，切换不灵活，需要切换环境时要改Gruop配置。Group的合理用法应该是配合namespace进行服务列表和配置列表的隔离和管理。

### 3) Namespace方案

Namespace命名空间进行环境隔离也是官方推荐的一种方式。Namespace的常用场景之一是不同环境的配置的区分隔离，例如：开发测试环境和生产环境的资源（如配置、服务）隔离等。

- 创建命名空间DEV和TEST,不同的命名空间会生成相应的UUID

- 在命名空间DEV和TEST下创建DataID为：nacos-config.yml,Group为默认值的配置

- 修改项目中的配置文件bootstrap.yml

1) 在spring:cloud:nacos:config:namespace，指定当前配置所在的命名空间ID。注意是命名空间ID!!!(例：bf386f19-8e80-4272-a291-0e00f601a242)

Namespace是官方推荐的环境隔离方案，确实有他的独到之处，使用namespace这种方案，同时可以与DataID+profile的方式结合，释放Group的限制，大大提高多环境配置管理的灵活性。

### 总结

通过上面三种方案的介绍，想必大家对于多环境下的配置读取方式应该有所选择

- DataID: 适用于项目不多，服务量少的情况。
- Group：实现方式简单，但是容易与DataID方案发生冲突，仅适合于本地调试
- Namespace：实现方式简单，配置管理简单灵活，同时可以结合DataID共同使用，推荐这种方案


## 5.Nacos共享配置

### 1) 场景描述
一个项目中服务数量增加后，配置文件相应增加，多个配置文件中会存在相同的配置，那么我们可以将相同的配置独立出来，作为该项目中各个服务的共享配置文件，每个服务都可以通过Nacos进行共享配置的读取

- demo工程：nacos-config-share

- 配置文件：nacos-config-share.yml

- 共享配置文件：shareconfig1.yml，shareconfig2.yml

1) 创建项目nacos-config-share

2) 修改该项目的配置文件bootstrap.yml

从配置文件可以看出，通过shared-dataids属性来指定要读取共享配置文件的DataID,多个文件用,分隔
使用refreshable-dataids指定共享配置文件支持自动刷新

3) 新建配置文件nacos-config-share.yml,共享配置文件 shareconfig1.yml 和 shareconfig2.yml

### 2) 需求变更
假设现在要读取shareconfig3.yml和shareconfig4.yml文件但是它的Group为SHARE3_GROUP和SHARE4_GROUP, 即共享配置文件与项目自身配置文件不在同一Group中（上边的例子是全都在DEFAULT_GROUP分组） 那如果继续用上边的方法，就无法读取共享配置文件

这时可以使用另一个配置ext-config，它可以由用户自定义指定需要加载的配置DataID、Group以及是否自动刷新,并且ext-config是一个集合（List），支持多个配置文件的指定。

1) 新建共享配置文件

先创建配置配置文件shareconfig3.yml和shareconfig4.yml，注意他们的Group属性

注：必须在public下新建共享配置文件

2) 修改项目配置文件

修改bootstrap.yml,增加ext-config配置

### 3) 总结

shared-dataids方式：

- 适合于共享配置文件与项目默认配置文件处于相同Group时，直接两条命令就可以搞定
- 优点：配置方便
- 缺点：只能在同一Group中

ext-config方式：

- 它可以由开发者自定义要读取的共享配置文件的DataId、Group、refresh属性，这样刚好解决了shared-dataids存在的局限性。
- 优点：可以与shared-dataids方案结合使用，用户自定义配置。灵活性强
- 缺点：配置容易出错，要熟悉YAML语法


## 6.Nacos持久化

使用默认配置启动Nacos时，所有配置文件都被Nacos保存在了内置的数据库中。如果使用内嵌数据库，注定会有存储上限，0.7版本增加了支持mysql数据源能力。

### 1) Nacos的数据库脚本文件在我们下载Nacos-server时的压缩包中就有

进入conf目录，初始化文件：nacos-mysql.sql,新建数据库然后执行初始化脚本，成功后会生成 11 张表。

### 2) 修改配置文件

修改Nacos-server的配置文件。Nacos-server其实就是一个Java工程或者说是一个Springboot项目，他的配置文件在conf目录下，名为 application.properties，在文件底部添加数据源配置：

```properties
# 修改数据库配置
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/mynacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=root
```

### 3) 总结

Nacos通过集中式存储来保证数据的持久化，同时也为Nacos集群部署奠定了基础。试想一下，如果我们以之前的方式启动Nacos，如果想组建Nacos集群，那各个节点中的数据唯一性就是最大的问题。Nacos采用了单一数据源，直接解决了分布式和集群部署中的一致性问题。


## 7.Nacos集群部署和遇到的问题

###  Nacos文档中提供了三种集群部署方案

- 1.http://ip1:port/openAPI 直连ip模式：
    ip+端口进行部署，客户端直接连接Nacos的ip

- 2.http://Vip:port/openAPI 挂载虚拟IP模式：
    1) 配合KeepAlive，Nacos真实ip都挂载虚拟Ip下
    2) 客户端访问Vip发起请求
    3) 当主Nacos宕机后，备用Nacos接管，实现高可用，

- 3.http://www.nacostest.com:port/openAPI 挂载虚拟IP+域名模式：
    为虚拟ip绑定一个域名，当Nacos集群迁移时，客户端配置无需修改。
    (https://github.com/kanateSong/alibaba-nacos/blob/master/png/vip.png)
    
### 本文以第一种ip+端口的方式为大家介绍集群部署方式

当然ip+端口也有多种部署方式

- 1ip+n端口+Nginx：普通玩家部署方式，没有过多服务器，单台服务器启动多个nacos实例，仅适合测试使用

- nip+n端口+Nginx：RMB玩家部署方式，服务器资源充足，组建完美集群，实现更好的容灾与隔离

无论怎么部署，部署方式都是一样的，这里我以1ip+3端口+Nginx的方式进行集群搭建

### 1.修改配置

- 修改Nacos-server目录conf/下的application.properties文件，添加mysql数据源

-  修改集群配置
修改conf/下的cluster.conf.example文件，将其命名为cluster.conf 例：
```
# ip:port
ip:8849
ip:8850
ip:8851
```

注：一定要记得将配置文件重命名为cluster.conf, 一定要使用实际的服务器ip，而非127.0.0.1，否则会出现问题

- 修改启动脚本
我们要在单台服务器上启动多个Nacos实例，要保证三个实例为不同的端口，这里我们可以修改启动脚本
定位到export FUNCTION_MODE="all"这一行，修改脚本内容，使其支持以-p传入端口参数
```
# linux 修改startup.sh文件
export MODE="cluster"
export FUNCTION_MODE="all"
# 新加
export SERVER_PORT="8848"
while getopts ":m:f:p:" opt
do
    case $opt in
        m)
            MODE=$OPTARG;;
        f)
            FUNCTION_MODE=$OPTARG;;
        # 新加
        p)
            SERVER_PORT=$OPTARG;;
        ?)
        echo "Unknown parameter"
        exit 1;;
    esac
done
# 新加
JAVA_OPT="${JAVA_OPT} -Dserver.port=${SERVER_PORT}"
```

```text
# window 修改startup.sh文件

set SERVER_PORT=8848

set SERVER_PORT_INDEX=-1
for里面
if "%%a" == "-p" ( set /a SERVER_PORT_INDEX=!i!+1 )
for里面
if %SERVER_PORT_INDEX% == !i! ( set SERVER_PORT="%%a" )

set "JAVA_OPT=%JAVA_OPT% -Dserver.port=%SERVER_PORT%"

```

相应的，修改shutdown脚本，使其可接收参数

```
# 新加内容
PORT=$1
if [ ! $PORT ]; then
  echo "please select stop port!" >&2
  exit 1
fi

# 添加PORT过滤
pid=`ps ax | grep -i 'nacos.nacos' |grep java |grep ${PORT} | grep -v grep | awk '{print $1}'`

# 后边省略...
```

### 2.启动Nacos

```
# linux
bash startup.sh -p 8849
bash startup.sh -p 8850
bash startup.sh -p 8851

#window
startup.cmd -m a -p port
```

如果你的机器不能同时启动3个实例，检查是否内存不够了，可以适当调整JVM参数 

调整启动脚本中JAVA_OPT="${JAVA_OPT} -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m中的-Xms -Xmx -Xmn

启动成功后会打印如下一句话：

nacos is starting，you can check the /usr/local/nacos/nacos/logs/start.out

查看cmd可以看到上面3个的ip地址

多节点的集群雏形已经搭建好了，可以试着访问Nacos后台,使用实际ip

http://ip:8849/nacos/index.html
http://ip:8850/nacos/index.html
http://ip:8851/nacos/index.html

访问到Nacos控制台可以看到集群节点共有三个，其中随机端口为leader

### 3.配置Nginx

完成上面的配置后，已经基本完成集群搭建的90%了。这里我们可以通过Nginx配置，为Nacos提供统一的入口，来实现一个简单的负载均衡

```text
upstream nacos-server {
  server 127.0.0.1:8849;
  server 127.0.0.1:8850;
  server 127.0.0.1:8851;
}

server {
  listen 8848;
  server_name  localhost;
  location /nacos/ {
    proxy_pass http://nacos-server/nacos/;
  }
}
```

执行命令 sudo nginx启动nginx

通过8848端口访问Nacos后台，此时Nginx会将请求分发至nacos-server下的地址中，这里默认的分发策略是线性轮询

### 4.客户端测试
- 修改项目nacos-config的配置,请使用实际ip
```text
spring:
  application:
    name: nacos-config
  cloud:
    nacos:
      discovery:
        server-addr: ip:8848
      config:
        server-addr: ip:8848
        prefix: ${spring.application.name}
        file-extension: yml
```
注：主要是修改注册中心和配置中新的地址，记得替换成你的服务器地址哦

- 启动前确保已经向Nacos中添加配置文件

### 5.总结
Nacos的集群部署基本就介绍完了，官方推荐的三种方式，他们的基本部署思路和方式都大同小异，只不过在高可用上有所不同，挑选你适合的方式动手搭建集群试试吧

