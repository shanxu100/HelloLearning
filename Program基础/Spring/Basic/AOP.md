## AOP

### 0、Basic

#### 静态代理与动态代理
核心思想、优缺点、常用的动态代理框架

#### AOP的核心概念与关键词
- 横切关注点：在应用程序中跨越多个模块的方法获功能，如日志，安全、缓存等
- 切面（Aspect）：将 横切关注点 模块化的类
- 通知（Advice）：切面中的一个个方法，用于插入到切入点中
- 切入点（PointCut）：目标程序中的某个点，用于通知切面，执行插入的逻辑
- 目标（Target）
- 代理（Proxy）
- 连接点（JoinPoint）


### 1、使用Spring的API接口

#### 1.1 定义主要增加的逻辑
实现```Advice```系列的接口，并注册为Bean
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

#### 1.2 配置切面

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 配置命名空间 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
       ... 略
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd">

<!-- ...略 -->

    <!-- 方式一：使用原生Spring AOP接口 -->
    <aop:config>
        <!-- 切入点：注意execution的表达式 -->
        <aop:pointcut id="logPoint" expression="execution(* org.helloseries.spring.service.MyServiceImpl.*(..))"/>

        <!-- 执行环绕增强：在方法前后都增加内容 -->
        <aop:advisor advice-ref="log" pointcut-ref="logPoint"/>
    </aop:config>

</beans>

```

### 2、自定义类实现AOP

#### 2.1 自定义类，并注册为Bean

```java
@Component
public class DiyLog {

    public void myBeforeFun(){
        System.out.println("this is BEFORE...");
    }

    public void myAfterFun() {
        System.out.println("this is afterReturning ...");
    }
}
```

#### 2.2 配置切面


```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 配置命名空间 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
       ... 略
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd">

<!-- ...略 -->

    <!-- 方式二：使用自定义类 -->
    <aop:config>
        <!-- 自定义切面：ref 指向封装切面的类，即该类中有待插入的逻辑-->
        <aop:aspect ref="diyLog">
            <!--切入点：确定被切入的位置-->
            <aop:pointcut id="logPoint" expression="execution(* org.helloseries.spring.service.MyServiceImpl.*(..))"/>
            <!-- 通知：在切入点前后指定需要增加的逻辑-->
            <aop:before method="myBeforeFun" pointcut-ref="logPoint"/>
            <aop:after method="myAfterFun" pointcut-ref="logPoint"/>
        </aop:aspect>

    </aop:config>

</beans>

```

### 3、使用注解实现AOP
注册为Bean
注册为切面
定义一个或多个通知（Advicer）

```java

@Aspect
@Component
public class DiyAspectLog {

    /**
     * 用以下三个方法为例：@Before 、 @After 、 @Around
     * <p>
     * 执行顺序是：
     * 1、@Around
     * 2、@Before
     * 3、被代理类真正执行
     * 4、 @After
     * 5、@Around
     */

    @Before("execution(* org.helloseries.spring.service.MyServiceImpl.*(..))")
    public void myBeforeFunc() {
        System.out.println("=======before=======");
    }

    @After("execution(* org.helloseries.spring.service.MyServiceImpl.*(..))")
    public void myAfterFunc() {
        System.out.println("=======after=======");

    }

    @Around("execution(* org.helloseries.spring.service.MyServiceImpl.*(..))")
    public int myAroundFunc(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("=======around before=======");

        // 获取方法签名
        Signature signature = joinPoint.getSignature();
        System.out.println("=======joinPoint.getSignature() signature is  =======" + signature);

        // 重要：执行真正的方法
        // 只有调用 proceed 方法后，被代理类才真正执行了
        Object result = joinPoint.proceed();
        System.out.println("=======joinPoint.proceed() result is  =======" + result);

        System.out.println("=======around after=======");
        return (int) result + 100;
    }

}
```

#### 3.2 开启注解支持
```xml
<beans>

<!-- ... 略 -->

    <!--  开启AOP的注解支持： proxy-target-class：false-表示使用jdk的动态代理（默认），true-表示使用cglib的动态代理-->
    <aop:aspectj-autoproxy proxy-target-class="false"/>
</beans>

```





