# 如何编写Gradle插件

[toc]

## 一、AndroidStudio、Gradle 和 Android Gradle Plugin之间的关系
### AndroidStudio 
简称AS ——IDE
### Gradle
是项目自动化构建开源工具，作用可以类比于Ant、Maven等。
   - Gradle Wrapper:
        简化Gradle本身的安装、部署。不同版本的项目可能需要不同版本的Gradle，手工部署的话比较麻烦，而且可能产生冲突，所以需要Gradle Wrapper帮你搞定这些事情。Gradle Wrapper是Gradle项目的一部分。
   - 配置：gradle-wrapper.properties
        ```
        zipStoreBase和zipStorePath组合在一起，是下载的gradle-4.4-all.zip所存放的位置。
        zipStorePath是zipStoreBase指定的目录下的子目录。
        distributionBase和distributionPath组合在一起，是解压gradle-4.4-all.zip之后的文件的存放位置。
        distributionPath是distributionBase指定的目录下的子目录。
        distributionUrl：Gradle下载地址
        ```

   - 下载安装目录：C:\Users\\${username}\\.gradle\wrapper\dists


### Android Gradle Plugin
应用在AS中，是一堆适合Android开发的Gradle插件的**集合**，主要由Google的Android团队开发。Android Gradle Plugin添加了几项专用于构建 Android 应用的功能，可独立与Androi Studio运行。
换句话说，这个插件是AS和Gradle之间的桥梁。它一边调用 Gradle本身的代码和批处理工具来构建项目，一边调用Android SDK的编译、打包功能。
```java
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        // 引入插件
        classpath 'com.android.tools.build:gradle:3.5.0'
    }
}

apply plugin: 'com.android.application'
```
**注意：Android Gradle Plugin 插件和Gradle的版本是有一定的对应关系的 **
https://developer.android.google.cn/studio/releases/gradle-plugin

------------

## 二、Gradle插件的分类
>http://liuwangshu.cn/application/gradle/5-plugins.html

### 2.1 脚本插件
适用：需求简单，功能不复杂的情况。
比如为了达成某种目的（例如下载，copy或重命名等），在xxx.gradle文件中写了几个task，构成了一个脚本。
这种情况称之为“脚本”更合适，而不能说是“插件”

### 2.2 对象插件
实现了org.gradle.api.plugins<Project>接口，并编译成jar包供其他人使用的插件。这种插件可以实现更加复杂的逻辑，具有更强大的功能。本文主要介绍如何开发一个HelloWorld插件。

-------------

## 三、如何编写Gradle插件
参考：
> https://www.jianshu.com/p/66b8eafc2d04
> https://www.jianshu.com/p/3191c3955194

### 3.1 创建项目
**使用AS或者IDEA创建和开发插件项目并无本质区别。只要按照结构定义好，即使使用记事本依然可以开发**   
- 如果使用IDEA创建项目，选择```New-Project-Gradle项目-Grovvey+Java插件```创建
目录结构如下所示：   
```bash
├── build.gradle
└── src
    └── main
        ├── groovy
        │   └── com.demon.plugin
        │                   ├── MyPlugin.groovy
        │                   └── Mytask.groovy
        |
        └── resources
            └── META-INF
                └── gradle-plugins
                    └── gradlepluginid.properties
```

### 3.2 在build.gradle中引入groovy插件
```java
apply plugin: 'groovy'

dependencies {
    implementation gradleApi()
    implementation localGroovy()

    implementation 'com.android.tools.build:gradle:3.2.1'
}
```

### 3.3 创建配置文件
在 ```resources/META-INF/gradle-plugins/``` 路径下放置着配置文件```xxxxx.properties```。该配置文件里面指向了源码文件夹中的Plugin接口具体的实现类
```java
implementation-class=com.example.android.dynamicfeature.plugin.MyPlugin
```
- **该路径结构是强制要求的，否则不能识别成插件**
- **xxxxx.properties配置文件的文件名就是插件名。即 ```apply plugin: 'xxxxx'```的名字**
    
### 3.4 创建<font color="red">groovy类</font>文件，实现接口 Plugin ，override方法 apply()
```java
//可以是Java类，就是语法会有些差别
class DynLangPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 打印日志
        project.getLogger().info("using xxxx plugin");
        //创建extentsion,并赋值给TestPluginExtension类
        project.extensions.create("${EXTENSION_NAME}", TestPluginExtension)
        // 创建task，指定task名称 和 具体的task类
        def task = project.task(taskName, type: MyTask)
    }

}
```

### 3.5 创建groovy类文件MyTask，继承DefaultTask类
- 在MyTask中做一些初始化操作
- 使用 ```@TaskAction``` 注解，定义task需要完成的动作
```java
public class MyTask extends DefaultTask {

    MyTask() {
        //构造函数
    }

    @TaskAction
    run() {
        //定义具体的事情
    }
}
```
### 3.6 定义task需要用到的属性   
实现宿主项目构建过程中Project向task传递的参数   
```java
//EXTENSION_NAME 属性名，在宿主项目中使用的属性名称
//TestPluginExtension 是和属性对应的实体类
project.extensions.create("ExtName", TestPluginExtension)

// 获取赋值的属性
TestPluginExtension ext = project.ExtName
```

- 如何实现嵌套闭包？？

-------------
## 四、 project中一些常用的属性或方法

```java

(DomainObjectCollection<BaseVariant>) project.android.applicationVariants

android.applicationVariants.all { variant ->
                def variantName = variant.name.capitalize()

                //buildType
                variant.buildType.name

                //签名
                signConfig = variant.variantData.variantConfiguration.signingConfig

            }


// 判断task是否已经被创建
project.tasks.findByPath(taskName)

project.logger.info()
project.logger.error()

project.rootDir
project.projectDir

```




--------------



# FAQ

## 1、outputs文件夹下的生成的apk或者aab文件名称
- gradle插件版本在3.5.0以下
    在debug或者release文件夹下产生的文件是 app.abb

- gradle插件版本在3.5.0及以上
    在debug文件夹下产生的文件是 app-debug.abb/app-debug.apk
    在release文件夹下产生的文件是 app-release.abb/app-release.apk

## 2、 开发插件工程模板
[详见：HelloSeries系列工程中helloplugin模块](https://github.com/shanxu100/HelloSeries)

## 3、AndroidStudio 设置Gradle插件日志打印级别
AndroidStudio Build窗口默认只显示Error级别的日志。想看其他级别的日志，执行命令时需要带上参数，如
```bash
./gradlew --debug assembleDebug。
```
可以通过设置AndroidStudio更方便的修改打印的日志级别：
在```File - settings - Build,Execution,Deployment - Compiler - Command-line Options```中输入```--info```



