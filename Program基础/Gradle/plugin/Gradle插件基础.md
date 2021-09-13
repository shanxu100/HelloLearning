## Gradle插件基础
[toc]
### 1、插件
插件：就是用来定义并具体执行这些Task 的东西。

#### 1.1 ```apply函数```

```apply``` 是一个函数，可以加载打包好的插件或者其他的gradle文件。
  ```java
  void apply(Closure closure);
  void apply(Action<? super ObjectConfigurationAction> action);

  //常用
  void apply(Map<String, ?> options);
  ```
```apply```函数在参数map中可以包含三类键值对：plugin、from、to
  - from：应用一个脚本到本地
  - plugin：应用一个插件
  - to：应用到某个对象

1. Java 相关的Gradle插件
  ```java
  apply plugin：'java'
  apply plugin: 'java-library'
  
  // 另一种写法
  plugins {
    id 'java-library'
  }
  ```
2. Android 相关的Gradle插件
  ```java
  apply plugin: 'com.android.application'
  apply plugin: 'com.android.library'
  apply plugin: 'com.android.test'
  ```

3. 其他
  ```java
  apply plugin: 'com.android.dynamic-feature'
  apply plugin: 'maven'
  //从指定路径中加载某个脚本
  apply from: rootProject.getRootDir().path + "/xxx/xxx"
  //只有？？一个模块加载了 apply plugin: 'com.android.library'插这个模块才能发布打包成aar或者jar
  ```

-----------
### 2、常见插件
#### 2.1 apply plugin: 'com.android.application' 插件
```java
//android{ }是工程中的Android app配置项的的入口
android{
  compileSdkVersion 28
  buildToolsVersion '28.0.3'
  //其中的一个配置块，负责默认配置，比如包名和版本号等
  defaultConfig {
      applicationId "com.example.myapp"
      minSdkVersion 23
      targetSdkVersion 29
      versionCode 1
      versionName "1.0"
      testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
      release {
          minifyEnabled false
          proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
      }
  }
}

// 引入依赖
// TODO需要继续考证，参考 https://baijiahao.baidu.com/s?id=1628320215436928396&wfr=spider&for=pc
dependencies{
}
```

//========================================================

#### 2.2 apply plugin: 'maven'插件

- uploadArchives：是一个发布类库到中央仓库的Task，我们需要为它指定本地仓库路径以及类库的一些信息，在类库模块的build.gradle中添加task

[官方文档](https://docs.gradle.org/current/userguide/maven_plugin.html#header)

```java
  //应用插件
  apply plugin: 'maven'

  uploadArchives {
      repositories {
          mavenDeployer {
            // 配置仓库路径，项目根目录下的repository目录中

            // 可以是本地仓库也可以是远程仓库
            //当version 后缀为'-SNAPSHOT'时，会自动使用snapshotRepository TODO 实现源码，没有找到源码？？
            repository(url: uri('../repository')){
              authentication(userName: 'username', password: 'pwd')
            }


            snapshotRepository(url:'http://xxxxxx.com'){
              authentication(userName: 'username', password: 'pwd')
            }

            // 使用 pom.project 这个builder，可以将任何与Maven相关的POM Reference 元素添加进去
            // http://maven.apache.org/pom.html
              pom.project {
                  version rootProject.ext.sdk_version_name + '-SNAPSHOT'
                  artifactId 'common'
                  groupId 'com.example.mylib'
                  packaging 'aar'
              }
          }
      }
  }
```


#### 2.3 apply plugin: 'maven-publish'插件
maven-publish是一个Gradle插件，可以将输出的artifacts发布到Maven仓库中。
[Maven Publish Plugin官网](https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven)
使用：
1. 复写publishing节点
2. 在该节点中包含两个配置：publications 和 repositories

```java
//应用插件
apply plugin: 'maven-publish'

ext {
    DIR = "build/outputs/aar/"
}

publishing {
    repositories {
        maven {
            // url uri('../maven_repo')
            url = 'http://www.example.com/library'
            // 仓库的用户名和密码
            credentials {
              username myusername
              password mypwd
            }
        }  
    }

    publications {
        customName(MavenPublication) {
           // 唯一标识（通常为模块包名，也可以任意）
            groupId 'com.example.mylib'
            // 项目名称（通常为类库模块名称，也可以任意）
            artifactId project.name
            // 版本号
            // version = "1.0.0"
            version '1.2.3.300'
        }
    }
}

```