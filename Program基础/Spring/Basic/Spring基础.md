# Spring基础
参考：
[《spring bean是什么》](https://www.awaimai.com/2596.html)
[《Spring Boot、Spring MVC 和 Spring 有什么区别》](https://juejin.cn/post/7024855197931274276)
[《Servlet 到 Spring MVC 的简化之路》](https://juejin.cn/post/6844903570681135117)
[《Nice！终于有人把SpringMVC讲明白了》](https://juejin.cn/post/6992383622342770695)

## 1、什么是```Bean```
在 Spring 中，构成应用程序主干并由Spring IoC容器管理的对象称为 Bean。Bean 是一个根据bean规范编写出来的类并由Spring IoC容器实例化、组装和管理的对象。

概念简单明了，我们提取处关键的信息： 
- bean是Java对象，一个或者多个不限定
- bean由Spring中一个叫IoC容器的东西管理
- Bean容器，或称Spring Ioc容器，主要用来管理对象和依赖，以及依赖的注入
- 我们的应用程序由一个个bean构成
- POJO泛指普通的Java对象。Bean可以简单理解为，满足特定编写规范的Java对象

## 什么是```Servlet```
 官方解说: Servlet 是运行在 Web 服务器上的程序，它是作为来自 HTTP 客户端的请求和 HTTP 服务器上的应用程序之间的中间层。
 Servlet 需要在Web容器中运行，并且由Web容器维护其生命周期。
 web容器默认是采用单Servlet实例多线程的方式处理多个请求的

 ## 什么是 SSH框架 和 SSM框架
 SSH = Structs2 + Spring + Hibernate
 SSM = SpringMVC + Spring + Mybatis

 

## 2、什么是控制反转IoC
控制反转英文全称：Inversion of Control，简称就是IoC，是一种思想。 
**控制**，即由谁来控制对象的创建和管理；**反转**，即由传统的程序进行控制，变成了由Spring容器进行控制。
所以，控制反转就是由Spring容器控制Bean的创建、管理和装配，而程序本身负责接收并使用对象即可。
实现IoC的方法有很多，依赖注入（DI）方式是其中之一


## Bean的作用域
参考：https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-scopes
- singleton：单例模式，默认。Spring容器中只有一个对象
- prototype：原型模式，每次从Spring容器中get时，都会返回不同的bean对象
如：```<bean id="myBean" class="org.helloseries.spring.dao.myBean" scope="prototype"/>```
- request
- session
- application
- websocket


## 自动装配
参考：
[《初识Spring —— Bean的装配（一）》](https://juejin.cn/post/6844903618567471112)

在Spring中有几种装配Bean的方式：

1. XML装配：在XML中显式配置：
创建 ```spring-bean.xml```（配置文件名字随意）
```xml
<beans>
    ...略

    <!-- 例1： -->
    <!-- 添加配置  id：Bean的唯一标识；class：Bean的完整名称 -->
    <!-- name: Bean的别名，可以同时取多个别名（空格 逗号 分好进行分割），等同于 <alias/>标签 -->
    <bean id="xxx" class="xxx.xxx.xxx" name="myAliasName,myAliasName2"/>

    <!-- 例2： -->
    <bean id="myBean" class="org.helloseries.spring.service.MyBeanImpl">
        <!-- ref : 赋值为Spring 容器中定义的bean对象 -->
        <property name="dao" ref="myDao"/>
        <!-- value : 赋值为基本数据类型 -->
        <property name="dao" value="123"/>
    </bean>

    <!-- 为Bean指定一个别名，这样就可以通过别名进行访问 -->
    <alias name="myBean" alias="myAliasName"/>
</beans>
``` 

2. Java装配：在Java中显式的配置


3. 自动装配

3.1 方式一：

- byName：需要保证Bean的id值唯一
- byType：需要保证Bean的class值唯一

根据名称或者类型进行匹配。这些Bean要先定义，后面Bean装配的时候才能使用

```xml
<beans>
    ...略

    <!-- 在Spring容器中查找，匹配 “Bean Id” 和 “Set方法中指定属性的名称”，然后装配给这个Bean-->
    <bean id="xxx" class="xxx.xxx.xxx" autowire="byName"/>

    <!-- 在Spring容器中查找，匹配 “Bean的类型” 和 “Set方法中指定属性的类型”，然后装配给这个Bean-->
    <bean id="xxx" class="xxx.xxx.xxx" autowire="byType"/>


</beans>
``` 

3.2 方式二：注解的自动装配

```xml
<beans>
    ...略

    <!-- 添加配置 -->
    <context:component-scan base-package="xxx.xxx.xxx"/>
</beans>
```

1. ```@ComponentScan```方式
范例：[《Spring bean的装配-自动化装配》](https://juejin.cn/post/6999531046195298334)
疑问：
1. 同一个接口有两个实现类，Spring该如何决策












## 常用注解
### Bean相关
```@Component```：修饰一个bean
```@ComponentScan```：
如：```@ComponentScan(basePackages = "org.helloseries.firstshow.firstshow")```

```@Autowired```：自动装配

```@ContextConfiguration```：
如：```@ContextConfiguration(classes = CDPlayerConfig.class)```

### 读取属性
- 方式一：作用于整个类，为相同名称的字段赋值（该字段需要Getter和Setter）
```@ConfigurationProperties(prefix = "custom.const")```

- 方式二：作用于某一个具体的属性，单独为该属性赋值
```@Value("${xxx.xxx.xxx}")```

### 其他
@Configuration

### Controller 相关
案例参考：[《Controller方法返回值以及部分注解的使用》](https://zhuanlan.zhihu.com/p/42790384)
- ```@Controller```：修饰类和方法
- ```@ResponseBody```：修饰类和方法
- ```@RestController```：等于 @Controller + @ResponseBody

### RequestMapping 相关
- ```@RequestMapping```
  这个注解可以作用在方法上或者是类上，用来指定请求路径

- ```@PutMapping```



## 处理响应

### ModelAndView 

### 返回String


## Spring中的配置文件
参考案例：[《spring配置文件》](https://www.jianshu.com/p/ab809c13c8a8)

### Spring的配置文件<import/>标签
作用：引入多个配置文件，合并到一个总的配置文件中
如：
- applicationContext.xml
```xml
<beans>
    <import resource="myConfig1.xml" />
    <import resource="myConfig2.xml" />
    <import resource="myConfig3.xml" />
</beans>
```
- myConfig1.xml
- myConfig2.xml
- myConfig3.xml

若多个配置文件中出现相同的条目，则如何覆盖？？？

### web.xml
1、web.xml文件是我们开发Web程序的一项很重要的配置项，里面包含了我们各种各样的配置信息，比如欢迎页面，过滤器，监听器，启动加载级别等等。
2、在tomcat容器启动后，会寻找项目中的web.xml文件，加载其中的信息，并创建一个ServletContext上下文对象，以后再web应用中可以获得其中的值。

web.xml中的加载顺序是：context-param -> listener -> filter > servlet；

### applicationContext.xml

### springMvc.xml
web项目启动时，读取web.xml配置文件，首先解析的是applicationContext.xml文件，其次才是spingMvc.xml文件。
spingMvc.xml文件中主要的工作是：启动注解、扫描controller包注解；静态资源映射；视图解析（defaultViewResolver）；文件上传（multipartResolver）;返回消息json配置。


https://www.zhihu.com/people/alan-78-96/posts?page=3




## FAQ
### Spring 中的7大模块


