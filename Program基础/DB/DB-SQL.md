## SQL 举例

### 1、创建数据库

#### 1.1 数据库
```sql
CREATE DATABASE IF NOT EXISTS `school` CHARACTER SET utf8 COLLATE utf8_general_ci;
```

#### 1.2 已知一个数据库，查询创建这个数据库的SQL语句
```sql
show create database `DBName`
```

### 2、表

#### 2.1 创建一张数据表
```sql
-- 创建
CREATE TABLE `Students` (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'id hao',
  `name` varchar(50) NOT NULL DEFAULT '匿名' COMMENT 'xing ming',
  `pwd` varchar(50) NOT NULL DEFAULT '123456' COMMENT 'mi ma',
  `age` int(4) NOT NULL DEFAULT '0' COMMENT '年龄',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

```

#### 2.2 已知一张表，查询创建该表的SQL语句
```sql
-- 查询
show create table `tableName`
```

#### 2.3 修改一张表
```sql
-- 修改表的名称
ALTER TABLE `Students` RENAME AS `student`

-- 增加表的字段
ALTER TABLE `Students` ADD `addition` VARCHAR(50) NOT NULL DEFAULT 'my addition'

-- 修改表的字段
-- 将 addition 列，由 VARCHAR 修改为 INT
ALTER TABLE `Students` MODIFY `addition` INT(10)
-- 将 addition 列重命名
ALTER TABLE `Students` CHANGE `addition` `addition1` VARCHAR(50)

-- 删除表的字段
ALTER TABLE `Students` DROP `addition`

```

#### 2.4 删除一个表
```sql
-- 删除一张表
DROP TABLE IF EXISTS `Students` 
```

#### 2.5 在修改表的过程中， MODIFY 和 CHANGE 的区别是啥
- MODIFY
可以修改表的字段的约束和类型，不能重命名

- CHANGE
重命名一个表的字段
不能修改约束和类型？？？？存疑，经过验证是可以的

### 3、DML语言
增、删、改

#### 3.1 插入
insert
```sql
-- 插入一行或者多行，每个字段都赋值
-- 此时，value中的值需要与表中的字段一一匹配
INSERT INTO `students` VALUES 
(2,'gsx1','123456',8,'ggggg'),
(4,'gsx2','123456',8,'ggggg')

-- 插入一行或者多行，每个字段都赋值
-- 自增的列若未指定，则自增
INSERT INTO `students`(`name`,`pwd`,`age`) VALUES 
('gsx1','123456',8),
('gsx2','123456',8)

```


#### 3.2 更新
update
```sql
-- 更新指定条件的数据
UPDATE `students` SET `name`="zhangsan",`age` = 10 WHERE id = 1

-- 更新表中所有数据
UPDATE `students` SET `name`="zhangsan",`age` = 10

-- 读取一个变量然后赋值
UPDATE `students` SET `birthday`=current_time ,`age` = 10

```

#### 3.3 删除
- delete
```sql
-- 清空整张表
delete from `xxx`

-- 删除指定数据
delete from `xxx` where id=123
```
- truncate
清除一张表的数据，保留表的结构和索引不变
```sql
-- 清空整张表
truncate table `xxx`
```

##### 3.3.1 delete 和 truncate 的区别
**相同点：**
都可以用于删除表的数据，
不会影响表的结构

**不同点：**
- 使用`truncate`清空表后，自增列计数器重置；使用`delete`清空表后，自增计数器不重置
- 不影响事务

##### 3.3.2 使用delete删除数据后，重启数据库的现象？？？？（存疑）
- InnoDB，自增从1开始（存在内存中，断电即失）
- MyISAM，继续从上一个自增量开始（存在文件中，不会丢失）


### 4、where条件判断

| 操作符 | 含义 |
| -- | -- |
=           | 等于
<> 或 !=    | 不等于
\>          | 大于
<           | 小于
\>=         | 大于等于
<=          | 小于等于
between and | 闭区间
and         | 与(多条件同时满足)
or          | 或（多条件满足一个即可）





