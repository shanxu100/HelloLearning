
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
