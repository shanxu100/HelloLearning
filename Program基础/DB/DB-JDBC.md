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

// 3、连接数据库（connection就抽象看做一个已连接的数据库）
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

// 6、释放（重要且必须）
ret.close();
statement.close();
conn.close();

```

### 2、其他常用例子

```java
// Connection conn：表示已连接的数据库

// 1、事务相关

// 设置自动提交
conn.setAutoCommit(true/false);
// 提交
conn.commit();
// 回滚
conn.rollback();


// 2、执行Sql

// 2.1 普通的
Statement statement = conn.createStatement();
// 执行查询，并返回结果集 ResultSet
statement.executeQuery(sql);
// 执行 增删改，并返回受影响的行数
statement.executeUpdate(sql);
// 执行 所有sql
statement.execute();

// 2.2 预编译sql，不执行，使用问号?占位符
// 防止SQL注入漏洞
PreparedStatement statement = conn.prepareStatement(sql);
String sql2 = "INSERT INTO `students` VALUES (?,?,?,?)";
PreparedStatement ps = conn.prepareStatement(sql2);
// 填充参数，占位符的下标从1开始类推
ps.setInt(1, 005);
ps.setString(2, "gsx5");
ps.setString(3, "123456");
ps.setInt(4, 11);
// 执行，并返回受影响的行数
int resultRow = ps.executeUpdate();

// 3、结果集


```


### 3、PreparedStatement可以防止sql注入的原理

特殊字符进行转义


### 4、CallableStatement




