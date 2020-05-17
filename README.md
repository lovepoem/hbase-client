
# hbase 客户端

## 1 maven依赖
```xml
<dependency>
    <groupId>io.wangxin</groupId>
    <artifactId>hbase-client</artifactId>
    <version>${hbase-client.version}</version>
</dependency>
```      
## 2 版本说明
### V1.0.1-SNAPSHOT
版本号：
```xml 
0.1.0-SNAPSHOT
```
功能： 
   * 添加基本java调用方式
   
   
### V1.0.0-SNAPSHOT
版本号：
```xml 
0.1.0-SNAPSHOT
```
功能： 
   * 2.1.1 使用连接池方式连接hbase
   
## 3 使用说明
 
### 3.1 标准java调用方式 
https://github.com/lovepoem/hbase-client/blob/master/src/test/java/io/wangxin/hbase/client/HbaseClientStdTest.java

### 3.2 spring配置数据源调用方式
* 3.2.1 spring配置
 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
    <!-- hbase 数据源配置 -->
    <bean id="hbasePoolConfig" class="io.wangxin.hbase.client.pool.HbasePoolConfig">
        <property name="maxTotal" value="20"/>
        <property name="maxIdle" value="5"/>
        <property name="maxWaitMillis" value="1000"/>
        <property name="testOnBorrow" value="true"/>
    </bean>

    <!-- hbase 数据源连接池 -->
    <bean id="hbasePool" class="io.wangxin.hbase.client.HbasePool">
        <!--zkQuorum-->
        <constructor-arg index="0" type="java.lang.String" value="${hbase.zkQuorum}"/>
        <!--zkClientPort-->
        <constructor-arg index="1" type="java.lang.String" value="${hbase.zkClientPort}"/>
        <constructor-arg index="2" ref="hbasePoolConfig"/>
    </bean>
</beans>
```
 

 
