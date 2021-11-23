## Dependency injection (DI) 

依赖，即Bean的创建和管理依赖于Spring容器；注入，即由Spring容器来注入Bean中的所有属性

### 1、构造器注入
> 参考：https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-constructor-injection

在`<bean/>`标签中配置子标签`<constructor-arg/>`标签，可调用有参构造函数创建Bean
- 如匹配下标、类型或者参数名称等方式，指定构造函数的参数值


### 2、 set注入
#### 2.1 使用配置XML的方式
在`<bean/>`标签中配置子标签`<property/>`，可以为Bean中的属性字段赋值，即**调用对应属性字段的Set方法**

针对不同的数据结构，可以配置不同的标签。
参考：https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-properties-detailed

```xml
<beans>
    ...略

    <!-- 例1： -->
    <!-- 添加配置  id：Bean的唯一标识；class：Bean的完整名称 -->
    <!-- name: Bean的别名，可以同时取多个别名（空格 逗号 分好进行分割），等同于 <alias/>标签 -->
    <bean id="xxx" class="xxx.xxx.xxx" name="myAliasName,myAliasName2"/>

    <!-- 例2： -->
    <bean id="myBean" class="org.helloseries.spring.service.MyBeanImpl">
        <!-- 1 普通注入：value : 赋值为基本数据类型 -->
        <property name="dao" value="123"/>

        <!-- 2 Bean注入：ref : 赋值为Spring 容器中定义的bean对象 -->
        <property name="dao" ref="myDao"/>

        <!-- 3 array注入 -->
        <property name="dao">
            <array>
                <value>FirstEle</value>
                <value>SecondEle</value>
            </array>
        </property>

        <!-- 4 List注入 -->
        <property name="dao">
            <list>
                <value>FirstEle</value>
                <value>SecondEle</value>
            </list>
        </property>

        <!-- 5 map注入 -->
        <property name="dao">
            <map>
                <entry key="myKey1" value="myValue1"/>
                <entry key="myKey2" value="myValue2"/>
            </map>
        </property>

        <!-- 6 set集合注入 -->
        <property name="dao">
            <set>
                <value>FirstEle</value>
                <value>SecondEle</value>
            </set>
        </property>

        <!-- 7 null注入 -->
        <property name="dao">
            <null/>
        </property>

        <!-- 8 props注入 -->
        <property name="dao">
            <props>
                <prop key="username">ggggg</prop>
                <!-- ....略 -->
            </props>
        </property>

    </bean>

    <!-- 为Bean指定一个别名，这样就可以通过别名进行访问 -->
    <alias name="myBean" alias="myAliasName"/>
</beans>
``` 
**在使用时：**
```java
// 获取Context（构造函数可以指定对个xml配置文件）
ApplicationContext context = new ClassPathXmlApplicationContext("spring-bean.xml");
// 获取Bean
MyBeanImpl myBean = (MyBeanImpl) context.getBean("myBean");
// 调用Bean中的方法
System.out.println(myBean.getDao().getName());

```
**分析创建：**
- Spring启动时，XML配置文件中指定的所有Bean就被创建了
- Spring容器默认使用无参构造函数创建Bean

#### 2.2 使用注解的方式

1. 开启扫描包  

    方式一：在xml中开启扫描  
    ```xml
    <beans>
        ...略
        <!-- 添加配置：指定扫描的包的路径 -->
        <context:component-scan base-package="xxx.xxx.xxx"/>
    </beans>
    ```
    方式二：  
    使用```@ComponentScan```注解：  
    如：
    ```java
    @ComponentScan(basePackages = "org.helloseries.firstshow.firstshow")
    ```    

2. 注册Bean
```@Component```：修饰一个类，表示将当前类定义为一个bean，由Spring容器进行管理。  
按照Spring MVC 的架构，可以对```@Component```进行衍生：
Controller层：使用`@Controller`
Service层：使用`@Service`
Dao层：使用`@Repository`  

3. 设置作用域
```@Scope("singleton")```；和`@Component`注解一起使用，可以设置Bean的作用域  

4. 属性注入
`@Value("xxx")`：修饰一个**String类型的属性**或者**该属性的Setter方法**，表示为该属性赋值
该注解的作用相当于`<bean/>`标签中的`<property/>`子标签


### 3、拓展方式注入

`p`命令名空间：简化Set注入的```<property/>```标签。
参考：https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-p-namespace

```xml
<beans
    // 引入
    xmlns:p="http://www.springframework.org/schema/p"
    // 其他，略
    >

    <bean name="myBean" class="com.example.ExampleBean"
        // 赋值
        p:email="someone@somewhere.com"/>

</beans>
```
`c`命名空间：简化构造器注入
参考：
```xml
<beans 
    // 引入
    xmlns:c="http://www.springframework.org/schema/c"
    // 其他，略
    >

    <!-- c-namespace declaration with argument names -->
    <bean id="myBean" class="x.y.ThingOne" c:name="myName"
        c:age="1" c:email="something@somewhere.com"/>

</beans>

```




