# Web基础

## Web基本目录结构（以部署到Tomcat为例）

```java
- webapps   // Tomcat服务器的web目录（可以存放多个web项目）
    - Root
    - FirstWeb  // 网站的目录名，也是不同web项目的目录
        - WEB_INF
            - classes   // 本项目Java编译的产物，以及resources中的配置文件
            - lib       // Web应用所依赖的jar包，比如pom中依赖的第三方的sdk
            - web.xml   // Web配置文件
        - index.html    // 默认的首页
        - static    // 存放静态资源
            - css
            - js
            - img
    - ...   // 其他目录
```

## JavaWeb项目目录结构

## Servlet原理
- Web容器调用实现Servlet接口的类的server方法，将request和response传入进来。
- HttpServlet根据Request的Method，分别传递给doPost和doGet等方法执行
- 继承HttpServlet类的自定义类，override其中的方法，可以获取request信息，并修改response数据
- 方法执行完毕后，web容器将response发送给客户端

## ```web.xml```文件
### 标签

`<servlet></servlet>`标签

`<servlet-mapping></servlet-mapping>`标签

- 一个`<servlet/>`标签可以经过多个`<servlet-mapping/>`标签进行映射


### 使用通配符
```xml
    <servlet-mapping>
        <servlet-name>first-servlet</servlet-name>
        <url-pattern>/test/*</url-pattern>
    </servlet-mapping>
```
http://localhost:8080/m01/test  
http://localhost:8080/m01/test/xxxxx  
http://localhost:8080/m01/test/xxxxx/xxx  
使用通配符，上面的例子将访问同一个servlet  

- 自定义后缀: * 前面不能加其他的映射路径, / 也不能加
```xml
    <servlet-mapping>
        <servlet-name>first-servlet</servlet-name>
        <url-pattern>*.test</url-pattern>
    </servlet-mapping>
```
- 通配符的错误用法：在*后面还设置了路径
```xml

    <servlet-mapping>
        <servlet-name>first-servlet</servlet-name>
        <url-pattern>/*/test</url-pattern>
    </servlet-mapping>
```

通配符的优先级：
- 有固定路径的优先
- 若无匹配的，则走默认的

### 特殊的例子
- 设置默认的请求路径，原默认的```index.jsp```将不再起作用
```xml
    <servlet-mapping>
        <servlet-name>first-servlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
```

## ServletContext的应用
一个Web app只有一个ServletContext对象，即这个对象是多个Servlet之间共享的。常用的使用场景如下：

- 获取 ServletContext对象
```java
ServletContext servletContext = this.getServletContext();
```

- 共享参数
如：
```java
servletContext.setAttribute("myKey", 123456);
Object obj = servletContext.getAttribute("myKey");
```` 

- 获取初始化参数  
```xml
<context-param>
    <!--配置参数-->
    <param-name>myParam</param-name>
    <param-value>Zero-Value</param-value>
</context-param>
```
```java
// 读取参数
servletContext.getInitParameter("myParam");
````

- 请求转发
```java
// /second-servlet 为需要转发的新的路径
servletContext.getRequestDispatcher("/second-servlet").forward(request,response);
```
**路径问题：**  
必须以`/`开头，表示从**当前webapp的根路径**进行请求转发  

- 读取配置属性
在resources文件夹中创建配置文件
```
- src
    - main
        - java  // 子文件夹...略
        - webapp    // 略
        - resources
            - r1.properties
            - test
                - r2.properties
```


```java
// 关键，注意配置文件的路径
InputStream in = servletContext.getResourceAsStream("/WEB-INF/classes/config/r2.properties");
// 使用Java方法读取配置文件
Properties properties = new Properties();
properties.load(in);
String user = (String) properties.get("username");
```

## HttpServletResponse
### 重定向
```java
// 注意重定向路径
response.sendRedirect("/f1/second-servlet");
```
**路径问题：**  
如果以`/`开头，则表示从**Servlet容器（而不是当前webapp）的根路径**进行重定向  
如果没有以`/`开头，则表示从当前页面的相对路径进行重定向  

**等价于：**
```java
response.setHeader("Location","设置的路径");
response.setStatus(302);
```

## HttpServletRequest
- 获取本webApp的上下文路径
```java
request.getContextPath()
```
- 请求转发。注意和ServletContext的请求转发进行比较
```java
request.getRequestDispatcher("/second-servlet").forward(request,response);
```
**路径问题：**  
如果以`/`开头，则表示从**当前webapp的根路径**进行请求转发   
如果没有以`/`开头，则表示从当前页面的相对路径进行请求转发   

- 获取请求参数

## Cookies
```java
// 获取request中的所有cookie
Cookie[] cookies = request.getCookies();
for (Cookie cookie: cookies){
    // 获取cookie的信息
    String c = "cookie.getName() = " + cookie.getName() + ", cookie.getValue() = " + cookie.getValue();
    out.println(c);
}
Student student = new Student("Zero", 8);
// 创建并添加一个新的cookie
Cookie myCookie = new Cookie("MyCookName",student.toString());
response.addCookie(myCookie);
```
### 字符问题：
在Cookie中，某些特殊的字符，例如：空格，方括号，圆括号，等于号（=），逗号，双引号，斜杠，问号，@符号，冒号，分号都不能作为Cookie的内容。
所以，
- 在添加cookie的时候，可以将value字段通过```URLEncoder.encode(msg,"utf-8");```进行编码
- 在获取cookie的时候，可以将value字段通过```URLDecoder.decode()(msg,"utf-8");```进行解码

## Session
```java
// 获取session对象
HttpSession session = request.getSession();

// 存取session中的值
session.setAttribute("myName", student);
session.getAttribute("myName");

// 强制session失效
session.invalidate();
```
```xml
<web-app>
    <session-config>
        <!-- 设置超时时间（单位：min） -->
        <!-- 1. 如果小于等于0，表示永远不会超时 -->
        <!-- 2. 如果未指定，则使用web容器默认的值 -->
        <session-timeout>5</session-timeout>
    </session-config>
</web-app>
```
注意：
- 同一个浏览器访问webapp，使用同一个session（SessionId相同）
- 换一个浏览器、重启浏览器或者session失效后，再次访问webapp时，会重新生成一个新的Session（和之前的SessionId不同）

## FAQ：

### 告诉浏览器，x秒后重新请求一次（刷新）
```java
response.setHeader("refresh","2");
```

### 【面试】: 重定向和转发的区别


### 【面试】Cookie和Session的区别


### Cookies中的一个name为JSESSIONID的特属Cookie，与Session的关系




