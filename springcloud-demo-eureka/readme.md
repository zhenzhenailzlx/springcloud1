## 1.pom文件添加依赖

    <dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-eureka-server</artifactId>
    </dependency>

## 2.使用@EnableEurekaServer注解

    @SpringBootApplication
    @EnableEurekaServer
    public class Application {
    
        public static void main(String[] args) {
            new SpringApplicationBuilder(Application.class).web(true).run(args);
        }
    
    }

## 3.配置application.yml 独立模式

    server:
      port: 8761
    
    eureka:
      instance:
        hostname: localhost
      client:
        registerWithEureka: false
        fetchRegistry: false
        serviceUrl:
          defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      

## 4.配置application.yml 对等模式

    ---
    spring:
      profiles: peer1
    eureka:
      instance:
        hostname: peer1
      client:
        serviceUrl:
          defaultZone: http://peer2/eureka/
    
    ---
    spring:
      profiles: peer2
    eureka:
      instance:
        hostname: peer2
      client:
        serviceUrl:
          defaultZone: http://peer1/eureka/
      

## 5.高可用模式

    eureka:
      client:
        serviceUrl:
          defaultZone: http://peer1/eureka/,http://peer2/eureka/,http://peer3/eureka/
    
    ---
    spring:
      profiles: peer1
    eureka:
      instance:
        hostname: peer1
    
    ---
    spring:
      profiles: peer2
    eureka:
      instance:
        hostname: peer2
    
    ---
    spring:
      profiles: peer3
    eureka:
      instance:
        hostname: peer3

## 6.首选ip地址 Prefer IP Address

    eureka:
      instance:
        prefer-ip-address: true

7.访问

    http://localhost:8761

## 8.注册地址

    http://localhost:8761/eureka/
    
    
## 9.启动脚本

	java -jar eureka.jar 

	java -jar eureka.jar --spring.profiles.active=peer1
	java -jar eureka.jar --spring.profiles.active=peer2

	java -jar eureka.jar --spring.profiles.active=eureka0
	java -jar eureka.jar --spring.profiles.active=eureka1
	java -jar eureka.jar --spring.profiles.active=eureka2
    
