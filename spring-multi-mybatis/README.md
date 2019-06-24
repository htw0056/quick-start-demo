# Spring-Mybatis多数据源配置

本教程提供Mybatis多数据源配置示例。

### 准备

在开始前，请先准备好Mysql环境：

本教程依赖两个mysql库，可通过[`schema.sql`](https://raw.githubusercontent.com/htw0056/spring-multi-mybatis/master/schema.sql)脚本快速搭建mysql环境。

```shell
# ip,port,user,password等信息根据实际情况填写
mysql -h127.0.0.1 -P 3306 -uroot < schema.sql -p
```





