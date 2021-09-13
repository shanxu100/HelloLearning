# JDBC

## ACID
当事务处理系统创建事务时，将确保事务有某些特性。组件的开发者们假设事务的特性应该是一些不需要他们亲自管理的特性。这些特性称为ACID特性。
ACID就是：
原子性(Atomicity )、一致性( Consistency )、隔离性( Isolation)和持久性(Durabilily)。


## JDBC中用于调用存储过程的对象是 CallableStatement


## PreparedStatement和Statement
> Statement不对sql语句作处理，直接交给数据库；而PreparedStatement支持预编译，会将编译好的sql语句放在数据库端，相当于缓存。对于多次重复执行的sql语句，使用PreparedStatement可以使得代码的执行效率更高。
> Statement的sql语句使用字符串拼接的方式，容易导致出错，且存在sql注入的风险；PreparedStatement使用“?”占位符提升代码的可读性和可维护性，并且这种绑定参数的方式，可以有效的防止sql注入。


2.【科目二】以下有关PreparedStatement的描述错误的是 B

A. PreparedStatement继承自Statement，都是接口

B. PreparedStatement中void setString(int parameterIndex, String x) throws SQLException方法从0开始计算替换点位符

C. PreparedStatement可以通过Connection对象的prepareStatement()方法创建

D. PreparedStatement可以使用占位符，是预编译的，可以有效防止SQL注入



## setAutoCommit 

## execute,exeucteQuery,executeUpdate
- execute(String sql)
    可执行任何SQL语句，返回一个布尔值，表示是否返回ResultSet,是executeQuery和executeUpdate的综合。
    它允许执行查询语句、更新语句、DDL语句。返回值为true时，表示执行的是查询语句，可以通过getResultSet方法获取结果；返回值为false时，执行的是更新语句或DDL语句
- executeQuery
    用来执行 SELECT 语句，产生单个结果集的语句，并且永远不为null。
     @return a <code>ResultSet</code> object that contains the data produced by the query; never <code>null</code>
- executeUpdate
    用于执行 INSERT\UPDATE 或 CREATE\DELETE 语句以及 SQL DDL（数据定义语言）语句。
    executeUpdate 的返回值是一个整数，表示受影响的行数（INSERT\UPDATE）; 
    CREATE\DELETE等不操作行语句，总返回0；



-----------
## 例题：

4.【科目二】关于JDBC Statement的execute,exeucteQuery,executeUpdate的区别，说法错误的是 C
A. execute(String query)方法用来执行任意的SQL查询，如果查询的结果是一个ResultSet，这个方法就返回true，如果结果不是ResultSet，比如insert或者update查询，它就会返回false

B. executeQuery(String query)接口用来执行select查询，并且返回ResultSet，即使查询不到记录返回的ResultSet也不会变null

C. executeQuery(String query)如果传进来的是insert或者update语句，仍然会执行，返回结果ResutlSet是null。

D.executeUpdate(String query)方法exe的返回值是一个整数，指示受影响的行数（即更新计数），对于CREATE TABLE或者DROP TABLE 待不操作行的语句，executeUpdate的返回值总为0




4.【科目二】当我们做简单业务，通过数据库连接池，获得一个数据库连接Connection，执行一个Statement，获得一个ResultSet后，就结束业务了，那么下面错误的是？(多选，最多 4 项)ABCD

A．我们可以通过关闭Connection，就顺带着关闭ResultSet和Statement，不需要在代码中显式地关闭ResultSet和Statement，保持代码简单和简洁。

B．我们需要先关闭Connection，接着显式地关闭ResultSet和Statement。

C．我们需要先关闭Connection，接着显式地关闭Statement和ResultSet。

D．我们需要先关闭ResultSet和Statement，然后Connection就自动被关闭了，不需要显式地关闭Connection。
