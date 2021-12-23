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


#### 2.4 在修改表的过程中， MODIFY 和 CHANGE 的区别是啥
- MODIFY
可以修改表的字段的约束和类型，不能重命名

- CHANGE
重命名一个表的字段
不能修改约束和类型？？？？存疑，经过验证是可以的

### 3、DML语言
增、删、改






