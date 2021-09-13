# Java Compile
## javac

```javac <options> <source files>```



### -g 参数：
```
 -g:none                    不生成任何调试信息
 -g:{lines,vars,source}     只生成某些调试信息
    source 源文件调试信息
    lines 行号调试信息
    vars 本地变量调试信息
```
举例：
```bash
javac -g:source,lines,vars TestJava.java
```

### 禁止屏蔽编译告警 
- 禁止使用
```bash
-nowarn   不生成任何警告
-Xlint:none  禁用所有编译告警
-Xlint:${name}  禁用指定告警名称的编译告警
-g:none 不生成任何调试信息
-g:[keyword list]

如：
javac -nowarn或-Xlint:none

```

- 必须使用
```bash
-source 
-Xlint   启用所有推荐的编译告警。
-Xlint:all  启用所有推荐的编译告警。
-target

# 如： 
javac -source 8 -Xlint:all
```

### -X参数
输出非标准选项的提要

### -Werror
出现警告时终止编译





