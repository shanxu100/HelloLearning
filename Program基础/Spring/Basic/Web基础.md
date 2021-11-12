# Web基础

## Web基本目录结构（以部署到Tomcat为例）

```java
- webapps   // Tomcat服务器的web目录（可以存放多个web项目）
    - Root
    - FirstWeb  // 网站的目录名，也是不同web项目的目录
        - WEB_INF
            - classes   // 本项目Java编译的产物
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

## web.xml
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




