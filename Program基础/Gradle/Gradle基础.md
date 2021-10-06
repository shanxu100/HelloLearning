## 1、Gradle重要概念

[TOC]

- 参考：
[官方文档](https://docs.gradle.org/current/userguide/userguide.html)
[《Android Gradle 完整指南》](https://www.cnblogs.com/laughingQing/p/5855774.html) 
 
- Gradle 是一个构建工具，或者理解为一个编程框架。Gradle 基于Groovy语言，Groovy 又基于Java。
  与Maven相比，**Gradle是可编程的**。

- 设置选项
  ```GRADLE_HOME```: 环境变量，告诉操作系统 Gradle 的安装目录。
  ```GRADLE_USER_HOME```:  环境变量，用于存放 Gradle 运行时的一些通用配置, 缓存等等。 默认值是 $HOME/.gradle, 一般不用单独配置。
  ```gradle.properties```：是配置文件，可以在这里添加gradle构建项目时用到的参数
  ```gradle-wrapper.gradle：```

- Project：
  Gradle中每一个待编译的工程都叫一个Project，所以可以有多个Project，每个project可以打包成jar、war等等，即看成一个独立的模块。
  每个Project都会有一个build文件（每一个build.gradle 会转换成一个Project 对象）

- 额外属性
  使用ext为Project和Task添加额外属性。Project 和Gradle 对象都可以设置 ext 属性
  定义了ext，就可以实现属性的跨脚本调用了。
  ```java
  ext.pwd="abc"
  ext {
      url = "www.baidu.com"
      username = "zhangsan"
      flag = true
  }
  ```
- 项目同步
  在修改gradle文件后，需要执行同步操作。使用Android Studio，可以通过```Sync Project with Gradle Files```触发。
  其本质上是执行了```generateDebugSouces```任务。
## 2、输入选项
- ```-P```：在编译Android项目时增加 gradle的参数 
  可以在Terminal中输入构建命令，也可以在AndroidStudio 的Setting的```Command-line Options``` 选项中，将参数写入。
  携带多个参数时，依次追加即可：```-Pparam1=123 -Pparam2=456 ...```
  然后读取参数：
  ```java
  sdk_active = project.hasProperty('sdkactive') ? project.property('sdkactive') : DEFAULT_SDK_ACTIVE
  ```
## 3、构建选项
- 构建是否使用 daemon 进程，默认 true。  
	在gradle.properties中设置参数```org.gradle.daemon=false```

## 4、Gradle工作流程
说得太简单，需要深入理解
1. **初始化**：Gradle 会分析有哪些 module 将要被构建，为每个 module 创建对应的 project 实例。这个时候settings.gradle 会执行。
2. **配置**：在这个阶段，每个Project 都会被解析，并不会执行其中的任务，只会**评估(Evaluate)任务的依赖性**，根据其依赖性创建任务的有向无环图
3. **执行**：在gradle xxx 中指定什么任务，gradle 就会将**这个xxx任务链上的所有任务**全部按依赖顺序执行一遍！

## 5、Gradle中的对象
### 5.1 Gradle对象：当执行gradle命令的时候，会默认构造一个Gradle对象。并且在整个执行过程中只会产生一个对象
```java
//代码
println( gradle.getGradleHomeDir() )
println( gradle.getGradleUserHomeDir() )

//-------------------------
//执行结果
C:\Users\xxx\.gradle\wrapper\dists\gradle-5.1.1-all\97z1ksx6lirer3kbvdnh7jtjg\gradle-5.1.1
C:\Users\xxx\.gradle
```
### 5.2 Project对象：每个build.gradle都对转换成一个Project对象
```Java
println(project.rootDir)
println(project.projectDir)

//-------------------------
//执行结果
D:\Projects\MyApp
D:\Projects\MyApp\utillib  
```
### 5.3 Setting对象：setting.gradle会转换成一个Setting对象

- 以Android项目为例进行介绍
  假设项目结构抽象后如下所示：
```
MyApp                     （Root Project）
  |
  |——app                  （module 1）
  |   |——build.gradle
  |
  |——utillib              （module 2）
  |   |——build.gradle
  |
  |——build.gradle
  |——settings.gradle
  |——gradle.properties
  |——gradle-wrapper.gradle
```
从上面的结构可以看出，Root Project中包含两个子project，分别为app和utillib。
- build.gradle：每个Project均需要一个build.gradle文件  
- settings.gradle：告诉Root Project这个项目中一个包含几个子Project  
```java
include ':app', ':utillib'
```
使用gradle命令查看
```bash
# 命令
gradlew projects

# 结果
> Task :projects

------------------------------------------------------------
Root project
------------------------------------------------------------

Root project 'MyApp'
+--- Project ':app'
\--- Project ':utillib'

```


## 6、补充：

### 6.1 ``subprojects``和```allprojects```
在rootProject中可以对一个childProject进行统一的配置：```subprojects```和```allprojects```
  - ```allprojects```是对所有project的配置，包括Root Project。
  - ```subprojects```是对所有Child是对所有Child Project的配置
  - buildscript
  ```java
  buildscript {

    repositories {
        google()
        jcenter()

      }
      dependencies {
        classpath 'com.android.tools.build:gradle:3.4.2'
      }
    }

  ```

### 6.2 release和snapshot区别
  - Snapshot版本代表不稳定、尚处于开发中的版本，快照版本。（理解为抢先版）

  - Release版本则代表稳定的版本，发行版本。

  - 生成release版本的aar：
    再AS的Terminal中输入命令 gradlew assembleRelease

### 6.3 使用 idea + Gradle 构建项目，出现乱码

**方法一：**
```java
tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}
```

**方法二：**
(执行main函数时，控制台乱码)
在idea中，Help - Edit Custom VM Option 增加配置
```bash
-Dfile.encoding=UTF-8
```
此文件所在路径为：```C:\Users\xxxxx\AppData\Roaming\JetBrains\IntelliJIdeaxxxxx```

或：

打开```setting > Build,Execution,Deployment > Compiler > Java Compiler```， 设置 ```Additional command line parameters```选项为 ```-encoding utf-8```，然后rebuild下，重新运行


**方法三：**
在在idea中，File--Settings--file Encodings 设置编码格式


