
## DB 基础

### 1、安装MySql
1. 下载解压zip包
2. 配置环境变量
   - 定义 ```MYSQL_HOME```
   - 将 ```%MYSQL_HOME%\bin``` 加入到```Path```中
3. 在解压的个目录中创建```my.ini```文件
```ini
[mysqld]
# 设置mysql的安装目录
basedir=D:/Tools/mysql-5.7.35-winx64/
 # 设置mysql数据库的数据的存放目录，必须是以data结尾（data文件夹不需要自己手动创建）
datadir=D:/Project/DB/data
port=3306
skip-grant-tables
```
4. 在管理员模式下开启终端，进入bin目录下，执行下面的命令
```bash
# 安装，成功后会提示 successfully
mysqld --install

# 初始化，成功后会在上面指定的data目录下生成一堆文件
mysqld --initialize-insecure --user=mysql
```
5. 启动并进入mysql服务
```bash
# 启动服务
net start mysql
# 停止服务
net stop mysql

# 进入: 若没有密码，-p后面不要加任何字符，包括空格
mysql -u root -p

# 更新密码：最后的分号不可省略
update mysql.user set authentication_string=password('123456') where User='root' and Host='localhost';

# 刷新权限
flush privileges
```
6. 重新修改```my.ini```
```ini
# 删掉这一行配置
# skip-grant-tables
```
7. 删除```MySql```服务
```bash
sc delete mysql
```
8. 若缺少dll，需要安装`Microsoft Visual C++ Redistributable`  
https://docs.microsoft.com/en-US/cpp/windows/latest-supported-vc-redist?view=msvc-170

### 2、Basic
#### 2.1 什么是关系型数据库和非关系型数据库



### 3、常用sql命令
```sql

-- 列出所有数据库
SHOW DATABASES

-- 查询数据库版本
SELECT VERSION();

-- 创建数据库
-- create database DBName
CREATE DATABASE `school` CHARACTER SET utf8 COLLATE utf8_general_ci;
create database if not exists school

-- 删除数据库
-- drop DBName
drop database school
drop database if exists school

-- 指定当前数据库(切换数据库)
--use databaseName
use school;

-- 列出当前数据库的所有表
show tables;

-- 查询某个表的所有信息
-- describe tableName
describe student;

-- 退出链接
exit;

```


### 4、数据库的列类型
#### 4.1 数值
| 类型 | 占用空间 | 描述 |
|  --  |  --  |  --  |
tinyint     | 1个字节   | 十分小的整数
smallint    | 2个字节   | 较小的整数
smediumint  | 3个字节   | 中等小的整数
int         | 4个字节   | 一般的整数
bigint      | 8个字节   | 较大的整数
float       | 4个字节   | 浮点数
double      | 8个字节   | 双精度浮点数
decimal     |字符串形式的浮点数（适用于金融）

#### 4.2 字符串
| 类型 | 占用空间 | 描述 |
|  --  |  --  |  --  |
char     | 0-255     | 固定大小的字符串 
varchar  | 0-65535   | 可变字符串
tinytext | 0-2^8-1     | 微型文本，适用于博客等文章
text     | 0-2^16-1    | 适用于大文本

#### 4.3 日期与时间
| 类型 | 描述 |
|  --  |  --  |
data        | 日期，YYYY-MM-DD
time        | 时间，HH:mm:ss
year        | 年份
datatime    | YYYY-MM-DD HH:mm:ss，最常用
timestamp   | 时间戳，毫秒

#### 4.4 null
- 没有值，未知
- 不要使用null进行运算，如果强行使用，结果还是为null


### 5、数据库的字段属性（重点）
1. `unsigned`
  - 表示无符号整数
  - 不能为负数
2. `zerofill`
   - 0填充：不足的为数，使用0来填充
3. 自增`auto_increment`
   - 一张表只能有一列是自增的，且只能修饰主键
   - there can be only one auto column and it must be defined as a key
   - 可以设置自增初始值以及自增步长
4. 空/非空 `NULL` / `NOT NULL`
   - `NOT NULL`: 如果该字段没有值，则报错
   - `NULL`： 如果该字段没有值，则自动填充`NULL`
5. default
   - 设置默认的值。即如果该字段没有值，则自动填充默认值

### 5、数据库引擎 
常用 `InnoDB`（默认使用） 和  `MyISAM`（早期使用）

#### 5.1 比较
| 特性 | InnoDB | MyISAMM |
| -- | -- | -- | 
| 事务 | 支持 | 不支持
| 数据行锁定 | 支持 | 不支持
| 外键约束 | 支持 | 不支持
| 全文索引 | 不支持 | 支持
| 表空间大小 | 较大，约为MyISAM的两倍 | 较小 

使用场景：  
- MyISAM：节约空间，速度较快  
- InnoDB：安全性高，支持事务的处理，支持多表多用户操作  

### 5.2 两种引擎对应存储文件的区别
对于MySql数据库，所有的数据文件均存放在 `my.ini` 文件中指定的`data`目录中  
每一个数据库DB都对应`data`目录中的一个子文件夹

- `InnoDB`  
每一张表对应数据库文件夹中的 `*.frm` 文件，以及上级目录的 `ibdata1` 文件

- `MyISAM`  
每一张表对应数据库文件夹中的
  -  `*.frm` 表结构文件
  -  `*.MYD` 数据文件
  -  `*.MYI` 索引文件

### 6、事务 Transaction

- 理解：一组访问和操作数据库的程序单元，即将一组sql看成一个整体，然后执行这个整体
- 关键：要么都成功，要么都不成功

#### 6.1 ACID原则
参考：
[《MySQL 词汇表》](https://dev.mysql.com/doc/refman/8.0/en/glossary.html)  
[《一文说尽MySQL事务及ACID特性的实现原理》](https://juejin.cn/post/6844903777825193991)  

原子性(Atomicity)、一致性(Consistency)、隔离性(Isolation)、持久性(Durability)

- **原子性(Atomicity)**：一个事务内的所有操作是一个整体，**要么都执行，要么都不执行**。  
换句话说，如果事务中一个 sql 语句执行失败，则已执行的语句也必须回滚，保证数据库退回到事务执行前的状态。  
实现原理：当事务失败时，利用`undo log`回滚所有已经成功执行的 sql 语句。比如，对于每个 `insert`，回滚时会执行 `delete`；对于每个 `delete`，回滚时会执行 `insert`；对于每个 `update`，回滚时会执行一个相反的 `update`，把数据改回去。  

- **一致性(Consistency)**：事务开始前和结束后，数据库的**完整性约束没有被破坏**。即，事务执行前后，数据都是合法的。  
  
  数据库的**完整性约束**包括但不限于：  
    1. 实体完整性(如行的主键存在且唯一)  
    2. 列完整性(如字段的类型、大小、长度要符合要求)  
    3. 外键约束  
    4. 用户自定义完整性(如转账前后，两个账户余额的和应该不变)  

- 隔离性(Isolation)：

- 持久性(Durability)：一旦事务提交成功，那么数据就必须被持久化到数据库中。

#### 6.2 事务的隔离级别

[《MySQL的四种事务隔离级别》](https://www.cnblogs.com/alsf/p/9434967.html)

#### 6.2.1 事务的并发问题
1. **脏读**：当前事务A中可以读到其他**事务B未提交的数据(脏数据)**，即A读取到的数据是脏数据。
2. **不可重复读**：事务A多次读取同一数据，事务B在事务A多次读取的过程中，对数据作了更新并提交commit，导致事务A多次读取的数据结果不一致，即，在**事务A中先后两次查询数据库，两次查询结果不同**。
3. **幻读（虚读）**：在事务A中先后两次查询数据库，两**次查询结果的条数**不同。  
例如，系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这就叫幻读。

**注意**：
- 不可重复读的和幻读很容易混淆：**不可重复读侧重于修改**，**幻读侧重于新增或删除**，换句话说，**不可重复读是指事务中多次查询的结果不同，幻读是指多次查询中查询结果行数不同**。
- 解决不可重复读的问题只需**锁住满足条件的行**，解决幻读需要**锁表**

#### 6.2.2 事务的隔离级别

- 读未提交（read-uncommitted）：用查询语句不会加锁，可能会读到未提交的行	
- 读已提交（read-committed，不可重复读）：只对记录加记录锁，而不会在记录之间加间隙锁，所以允许新的记录插入到被锁定记录的附近	
- 可重复读（repeatable-read）：多次读取同一范围的数据会返回第一次查询的快照，不会返回不同的数据行	
- 可串行化（serializable）：将全部的查询语句加上共享锁	

| 隔离级别 | 脏读 | 不可重复度 | 幻读
| -- | -- | -- | -- |
| 读未提交 | 1 | 1 | 1 |
| 读已提交 | 0 | 1 | 1 |
| 可重复读 | 0 | 0 | 1 |
| 可串行化 | 0 | 0 | 0 |

**注意**：
- mysql默认的事务隔离级别为**repeatable-read**
- mysql默认开启事务的自动提交

#### 6.2.3 事务的状态
- Active：事务的初始状态，表示事务正在执行；
- Partially Commited：
- Failed：发现事务无法正常执行之后；
- Aborted：事务被回滚并且数据库恢复到了事务进行之前的状态之后；
- Commited：成功执行整个事务；


### 6、FAQ
#### 6.1 tips
1. mysql关键字不区分大小写
2. 表名、字段名也不区分大小写
3. 表的名称和字段的名称，使用反引号``包起来
4. 字符串使用单引号''包起来

#### 6.2 int(3) 和 varchar(3)，中这个3有什么意义

#### 6.4 设置整个数据库的默认编码
编辑 my.ini 文件
```ini
# 设置默认编码
character-set-server=utf8
```

#### 6.5 在实际应用中，数据库中每张表中经常存在的几个字段
| 字段名称 | 描述 |
| -- | -- |
id          | 主键
version     | 乐观锁
is_delete   | 伪删除，1表示这个记录已经在业务上被删除，但实际依然存在数据库中
gmt_create  | 创建时间
gmt_update  | 修改时间

#### 6.6 常见约束
主键约束 Primary Key
外键约束 Foreign Key
唯一索引 Unique Key
index 索引 Key


#### 6.7 SQL语句中，单引号和双引号的区别


#### 6.8 DML、DDL和DQL是什么

#### ROW_ID?

#### 6.9 约束
实体完整性？？

