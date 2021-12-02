## SpringMVC

### MVC 思想


### SpringMVC 执行原理


#### DispatcherServlet
前端控制器：SpringMVC核心组件，主要步骤为：
1. 从用户发来的请求都会走到这个前端控制器
2. 前端控制器找到对应的Controller
3. 交给Controller执行具体业务
4. 将Controller返回的ModelAndView传给ViewResolver进行解析和渲染

与前端控制器相配合的三个组件：
1. 处理映射器
   如: BeanNameUrlHandlerMapping
2. 处理适配器
   如: SimpleControllerHandlerAdapter
3. 视图解析器
```xml
<!--    视图解析器-->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
    <!-- 前缀：注意结尾的 /  -->
    <property name="prefix" value="/WEB-INF/jsp/"/>
    <!-- 后缀: -->
    <property name="suffix" value=".jsp"/>
</bean>
```

#### ViewResolver
视图解析器：负责解析`DispatcherServlet`传过来的`ModelAndView`
1. 获取`ModelAndView`中的数据
2. 解析`ModelAndView`中的视图`View`名称
3. 拼接视图的名称，找到对应的视图：前缀+名称+后缀
4. 将数据渲染到这个视图上




### FAQ：
#### 配置前端控制器
```xml
<web-app >

<!-- 略 -->

    <servlet>
        <servlet-name>my-spring-servlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 绑定spring的配置文件 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-config.xml</param-value>
        </init-param>
        <!--   配置启动级别（可选）：1：应用启动时自动加载     -->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--
      在SpringMVC中：
      /  :表示只匹配所有请求，不去匹配jsp页面
      /* :匹配所有请求，也匹配jsp页面
      -->
    <servlet-mapping>
        <servlet-name>my-spring-servlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>

```


