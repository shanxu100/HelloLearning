# SHELL 脚本

## zip 
```bash
# 将文件或者文件夹压缩至myzip.zip中
zip -r myzip.zip file1 dir1 

```

## if
> https://www.cnblogs.com/kaishirenshi/p/9729800.html
```bash

# 基本
if [ command ]; then
     符合该条件执行的语句
fi

# 高级
if [ command ]; then
     符合该条件执行的语句
elif [ command ]; then
     符合该条件执行的语句
else
     符合该条件执行的语句
fi
```
- 文件/目录判断：
```bash
# 常用的：
[ -a FILE ] 如果 FILE 存在则为真。
[ -d FILE ] 如果 FILE 存在且是一个目录则返回为真。
[ -e FILE ] 如果 指定的文件或目录存在时返回为真。
[ -f FILE ] 如果 FILE 存在且是一个普通文件则返回为真。
[ -r FILE ] 如果 FILE 存在且是可读的则返回为真。
[ -w FILE ] 如果 FILE 存在且是可写的则返回为真。（一个目录为了它的内容被访问必然是可执行的）
[ -x FILE ] 如果 FILE 存在且是可执行的则返回为真。
```

- 字符串判断
```bash
[ -z STRING ] 如果STRING的长度为零则返回为真，即空是真
[ -n STRING ] 如果STRING的长度非零则返回为真，即非空是真
[ STRING1 ]　 如果字符串不为空则返回为真,与-n类似
[ STRING1 == STRING2 ] 如果两个字符串相同则返回为真
[ STRING1 != STRING2 ] 如果字符串不相同则返回为真
[ STRING1 < STRING2 ] 如果 “STRING1”字典排序在“STRING2”前面则返回为真。
[ STRING1 > STRING2 ] 如果 “STRING1”字典排序在“STRING2”后面则返回为真。

```

- 逻辑判断
```bash
[ ! EXPR ] 逻辑非，如果 EXPR 是false则返回为真。
[ EXPR1 -a EXPR2 ] 逻辑与，如果 EXPR1 and EXPR2 全真则返回为真。
[ EXPR1 -o EXPR2 ] 逻辑或，如果 EXPR1 或者 EXPR2 为真则返回为真。
[ ] || [ ] 用OR来合并两个条件
[ ] && [ ] 用AND来合并两个条件
```

## cp
```bash
cp [options] <source file or directory> <target file or directory>
# 或
cp [options] source1 source2 source3 …. directory
```
- 参数选项
- f 删除已经存在目标文件而不提示。
- r 若源文件是一目录文件，此时cp将递归复制该目录下所有的子目录和文件。当然，目标文件必须
为一个目录名。
- v 显示详情（进度）

```bash
cp -rvf dir1 dir2
复制文件夹dir1到dir2，显示进度

cp -rf dir1 dir 
复制文件夹dir1到dir2，不显示进度
```

## 重定向
```bash
# 标准输出 重定向到out.file文件
commond >out.file

# 标准输出 重定向到out.file文件
# 同时，标准错误输出 重定向到标准输出。
# 这样两个输出都会在out.file文件里面了
commond >out.file 2>&1

```
**为何2>&1要写在后面？**
```command 2>&1 >file ```2>&1 标准错误拷贝了标准输出的行为，但此时标准输出还是在终端。>file 后输出才被重定向到file，但标准错误仍然保持在终端。


## 拆分大文件
1. 按行数来切割
```split -l 20000 log.txt newlog```

2. 按大小来切割
```split -b 200m log.txt newlog```

生成的新文件的命名规则：```xaa，xab，xac``` 以此类推

注：合并文件：```cat newlog* > log.txt```


## grep
grep命令最重要的功能就是进行字符串数据的对比，然后将符合用户需求的字符串打印出来。

- 参数：  
`--color=auto` 将找到的关键字显色  
`-n` : 显示匹配行及行号。  
`-i` : 不区分大小写（只适用于单字符）。  
`-v` : 显示不包含匹配文本的所有行。  
`-E` : 切换为 egrep ( grep 的扩充版本), 改良了许多传统 grep 不能或不便的操作

- 正则表达式：

`^` 在`[]`中使用 和`-v`意思相同，表示不匹配什么

`^`表示匹配行开头 注意意义不同于[]中的^

`$` 表示匹配行尾

`.` 表示任意字符

`*` 重复字符

- 举例：
1. 过滤日志中的包含“rtc_”或“RTCMediaSDK”关键字的行
```grep -E 'rtc_|RTCMediaSDK' xxx.log ```



