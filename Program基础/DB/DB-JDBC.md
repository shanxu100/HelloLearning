## JDBC

JDBC：Java Data Base Connectivity，Java数据库连接。JAVA与数据库的连接的桥梁或者插件，用JAVA代码就能操作数据库的增删改查、存储过程、事务等

### 1、简单例子

```java
// 0、导入jar包
// mysql-connector-java

// 1.加载驱动
Class.forName("com.mysql.jdbc.Driver");

// 2. 创建 连接字符串
// 记住这三个参数: useUnicode=true   characterEncoding=utf8   useSSL=true
String url = "jdbc:mysql://localhost:3306/school?useUnicode=true&characterEncoding=utf8&useSSL=false";
String username = "root";
String pwd = "123456";

// 3、连接数据库
Connection conn = DriverManager.getConnection(url, username, pwd);

// 4、创建 SQL执行对象
Statement statement = conn.createStatement();

// 5、执行SQL，并获取结果
String sql = "select * from students";
ResultSet ret = statement.executeQuery(sql);
while (ret.next()) {
    StringBuilder sb = new StringBuilder();
    sb.append("id=").append(ret.getInt("id")).append("\n");
    sb.append("name=").append(ret.getString("name")).append("\n");
    sb.append("pwd=").append(ret.getString("pwd")).append("\n");
    sb.append("age=").append(ret.getInt("age")).append("\n");
    System.out.println(sb);
}

// 6、释放
ret.close();
statement.close();
conn.close();

```


