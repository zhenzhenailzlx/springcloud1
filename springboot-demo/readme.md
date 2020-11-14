1.拆分环境test、dev、prod   
application.properties 重命名为application.yml   
复制三份   
application.yml
```   
spring:      
  profiles:      
    active: dev
```      
application-dev.yml   
application-test.yml   
application-prod.yml    
java -jar xxx.jar --spring.profiles.active=dev    

2.热部署
```    
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-devtools</artifactId>  
    <optional>true</optional><!-- optional=true,依赖不会传递，该项目依赖devtools；之后依赖myboot项目的项目如果想要使用devtools，需要重新引入 -->   
</dependency>     
``` 
3.使用swagger 
```   
<dependency>
	<groupId>io.springfox</groupId>  
	<artifactId>springfox-swagger2</artifactId>   
	<version>2.8.0</version>   
</dependency>   
<dependency>   
	<groupId>io.springfox</groupId>   
	<artifactId>springfox-swagger-ui</artifactId>   
	<version>2.8.0</version>   
</dependency>   
``` 
APP上加註解@EnableSwagger2   

/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/controller/HelloController.java
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/condition/HelloCondition.java
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/dto/HelloDto.java
 

http://localhost:9300/springboot/swagger-ui.html#/   
   
4.返回前端的日期格式化、时区、字符集  
```  
spring:
  profiles:
    active: dev
  jackson:
    time-zone: GMT+8
    date-format: yyyy-MM-dd HH:mm:ss
    default-property-inclusion: NON_NULL    
  http:
    encoding:
      charset: UTF-8
``` 
5.使用 lombok   
maven上下载lombookjar包   
java -jar lombook。jar执行，绑定eclipse的安装路径  
```  
<dependency>   
	<groupId>org.projectlombok</groupId>   
	<artifactId>lombok</artifactId>   
</dependency>  
```  

@Data  

@Slf4j  

6.使用druid数据库连接池 
```  
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid-spring-boot-starter</artifactId>
	<version>1.1.10</version>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>				

<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
``` 

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

配置
``` 
spring:
    datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        driverClassName: com.mysql.jdbc.Driver
        druid:
            url: jdbc:mysql://localhost:3306/test?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8
            username: root
            password: root
            initial-size: 10
            max-active: 100
            min-idle: 10
            max-wait: 60000
            pool-prepared-statements: true
            max-pool-prepared-statement-per-connection-size: 20
            time-between-eviction-runs-millis: 60000
            min-evictable-idle-time-millis: 300000
            #validation-query: SELECT 1 FROM DUAL
            test-while-idle: true
            test-on-borrow: false
            test-on-return: false
            stat-view-servlet:
                enabled: true
                url-pattern: /druid/*
                login-username: admin
                login-password: admin
            filter:
                stat:
                    log-slow-sql: true
                    slow-sql-millis: 1000
                    merge-sql: false
                wall:
                    config:
                        multi-statement-allow: true
``` 
 
 监控路径  
http://localhost:9300/springboot/druid   

7.代码中的常量使用枚举类型   
``` 
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/constants/ResultEunm.java
```    

8.配置跨域 
```   
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/filter/HeadersCORSFilter.java
```    

9.使用BeanValidator 

```
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/utils/BeanValidatorUtil.java
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/condition/HelloCondition.java
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/controller/HelloController.java
```   

10. 封装result和统一异常处理  
```  
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/result/Result.java
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/exception/ExceptionHandle.java
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/exception/SprintBootDemoException.java
```  


11.使用aop统一处理参数和返回值日志  
```  
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/aspect/ControllerMethodExecutionLogAspect.java
```   


12.配置包名和跳过测试   

```  
<build>   
    <finalName>sprintbootdemo</finalName>   
	<plugins>   
		<plugin>   
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-surefire-plugin</artifactId>
			<configuration>
				<skipTests>true</skipTests>
			</configuration>
		</plugin>
	</plugins>
</build>

``` 

13.使用mabatis

``` 
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.3.1</version>
</dependency>

<plugin>
	<groupId>org.mybatis.generator</groupId>
	<artifactId>mybatis-generator-maven-plugin</artifactId>
	<version>1.3.5</version>
	<dependencies>
		<dependency>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-core</artifactId>
			<version>1.3.5</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.35</version>
			<scope>runtime</scope>
		</dependency>
	</dependencies>
	<executions>
		<execution>
			<id>mybatis generator</id>
			<phase>package</phase>
			<goals>
				<goal>generate</goal>
			</goals>
		</execution>
	</executions>
	<configuration>
		<configurationFile>${basedir}/src/main/resources/generator/generatorConfig.xml</configurationFile>
		<!--只使用一次-->
		<overwrite>false</overwrite>
		<verbose>false</verbose>
	</configuration>
</plugin>


<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="DB2Tables"  targetRuntime="MyBatis3">
        <!--数据库链接URL，用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql://localhost:3306/demo" userId="root" password="root">
        </jdbcConnection>
        <!-- 生成模型的包名和位置-->
        <javaModelGenerator targetPackage="com.zhenzhen.demo.springboot.model" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!-- 生成映射文件的包名和位置-->
        <sqlMapGenerator targetPackage="mapping" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
        <!-- 生成DAO的包名和位置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.zhenzhen.demo.springboot.mapper" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
        <!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
        <table tableName="user" domainObjectName="User" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false"></table>
    </context>
</generatorConfiguration>
	
``` 
14.baseDao

``` 
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/dao/BaseDao.java
/springboot-demo/src/main/java/com/zhenzhen/demo/springboot/common/dao/JsonRowMapper.java
``` 

15.logback
``` 
/springboot-demo/src/main/resources/logback-spring.xml

使用logback 保存到数据库
在jar包中找到sql建表语句
logback-classic-1.2.3-sources.jar
ch.qos.logback.classic.db.script.mysql.sql
``` 
16.quartz
https://blog.csdn.net/huiyanshizhen21/article/details/85756911

17.prometheus
https://blog.csdn.net/huiyanshizhen21/article/details/109692773