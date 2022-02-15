## Web基础

### 1、Web应用基本目录结构（以部署到Tomcat为例）

```java
- webapps               // Tomcat服务器的web目录（可以存放多个web项目）
    - Root
    - FirstWeb          // 网站的目录名，也是不同web项目的目录
        - WEB-INF
            - classes   // 本项目Java编译的产物，以及resources中的配置文件
            - lib       // Web应用所依赖的jar包，比如pom中依赖的第三方的sdk
            - web.xml   // Web配置文件
        - index.html    // 默认的首页
        - static        // 存放静态资源
            - css
            - js
            - img
    - ...   // 其他目录
```
- FirstWeb 目录表示一个Web应用
- `web.xml`文件是web程序的主要配置文件
- `WEB-INF`目录下的资源不能直接被浏览器访问的，FirstWeb 目录下的html、jsp等文件可以直接被浏览器访问
- `lib`目录用来存放Web应用所依赖的jar包
- `classes`目录用来存放Web应用编译出来的classes文件

```web.xml```文件（重要）:  
略...

JavaWeb项目目录结构：  
略...


### 3、标签

#### 3.1 欢迎页标签
```xml
<welcome-file-list>
    <!-- 可以指定为自己的欢迎页 -->
    <welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

#### 3.2 `<servlet/>`和`<servlet-mapping>`标签

一个`<servlet/>`标签可以经过多个`<servlet-mapping/>`标签进行映射

**正确使用通配符**
通配符的优先级：
- 有固定路径的优先
- 若无匹配的，则走默认的

具体使用，见下面几个例子：

- 基本使用：
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

- **特殊的例子：** 设置默认的请求路径，原默认的```index.jsp```将不再起作用
```xml
    <servlet-mapping>
        <servlet-name>first-servlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
```


## 12、FAQ：

### Web服务器和Web容器的区别

### 资源放置到`WEB-INF`文件夹中和不放在这个文件夹中有什么去区别

