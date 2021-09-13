# Gradle-Task

参考：  
[《组件化Gradle语法》](https://juejin.cn/post/6933100499767918606)

Task 可以理解为 Gradle 的执行单元，是project对象的一个函数。这个Task属于该project对象。一个Project中可以包含多个Task。

## Task定义
- 方式一：
```java
//定义Task
task myTask {

  println("doConfig---")

  doFirst {
      println("doFirst1---")
  }
  doFirst {
      println("doFirst2---")
  }
  doLast {
      println("doLast1---")
  }
    doLast {
      println("doLast2---")
  }
}
```
注意：
- ```doConfig---```在配置阶段执行。因为每一个task执行，都会对project进行一遍完整的配置，所以任何一个task执行，都会打印```doConfig---``
- 一个 Task 包含多个 Action，这里的 Action 就是完成一个 Task 需要的具体操作。
- 不想在配置阶段执行的代码，需要写在 ```doFirst``` 和```doFirst```中

- 方式二：




## 执行结果

控制台输出

```java
// 配置阶段执行的输出
> Configure project :utillib
project.name=utillib
curPath:D:\Projects\personal\MyApp
doConfig---

// 任务执行的输出
> Task :utillib:myTask
doFirst2---
doFirst1---
doLast1---
doLast2---
```
注意：
- ```doConfig---```这句代码是在配置阶段执行的
- doFirst：添加到task的Action队列头部，**所以```doFirst2---```在```doFirst1---```前面执行**
- doLast：添加到task的Acticon队列尾部，***所以```doLast2---```在```doLast1---```后面执行***
- 操作符```<<```是doLast的段标记形式(**已过时，但可以了解一下**)

## 任务执行
```bash
#语法格式
gradlew task名称

比如：
  #执行清理任务
  gradlew clean 
  #查看属性信息
  gradlew properties 
  #清理项目中的build文件夹
  gradlew check 
  #检查依赖并编译打包（包括debug和release）
  gradlew build 
  #编译打包（包括debug和release），可以看做是build的子集
  gradlew assemble  
  #编译并打Release的包
  gradlew assembleRelease 
  #编译并打Debug包
  gradlew assembleDebug 
```
**注意：Task和Task之间是有依赖关系的。意思是说一个task的执行可能依赖于另一个task的先执行**

总结：定义属性的方法
- 参数写在ext代码块
- 参数写在gradle.properties文件
- 调用gradle构建命令，使用 -P 参数
  ```bash
  #uploadArchives是一个Task
  gradlew -Psdkactive=release uploadArchives
  ```

## 查看任务列表
```bash
gradlew tasks
```

