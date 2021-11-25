
1. 定义主要增加的逻辑
```java
@Component
public class Log implements MethodBeforeAdvice, AfterReturningAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("this is BEFORE...method name is " + method.getName());
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("this is afterReturning ...method name is " + method.getName());
        System.out.println("this is afterReturning ...returnValue is " + returnValue);

    }
}
```

2. 配置切面

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
       ... 略
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd">

<!-- 。。略 -->


<!-- 方式一：使用原生Spring AOP接口 -->
    <aop:config>
    <!-- 切入点：注意execution的表达式 -->
        <aop:pointcut id="logPoint" expression="execution(* org.helloseries.spring.service.MyServiceImpl.*(..))"/>

        <!-- 执行环绕增强：在方法前后都增加内容 -->
        <aop:advisor advice-ref="log" pointcut-ref="logPoint"/>
    </aop:config>

</beans>

```