# Android项目中使用```apply plugin: 'maven'```上传maven仓库

--------
## 基本使用：

### 一、使用 upload.gradle 完成动态化版本发布
[具体实现（源代码)](./script/upload.gradle)

### 1.1 目标：
**1. 通过引入一个upload.gradle文件，可以在构建过程中根据输入的运行参数，实现动态灵活的多版本（release和snapshot）发布**

### 1.2 问题场景
**开发人员在完成编码后需要将SDK进行打包并发布到Maven仓库。在打包过程中需要进行相关的配置。**

比如：
- 测试的时候可能需要发布到本地文件夹（local），
- 具有新功能的“抢先版”或者尚未稳定的版本需要发布到SNAPSHOT的仓库
- 稳定的版本需要发布到RELEASE仓库

**在这种情况下，就需要开发人员需要创建多个gradle文件，并且进行多次类似的操作。**
**但是，在发布release版本时，可能由于操作繁琐或者一时疏忽，很容易发布错误的版本**
**因此，需要将原来的发布过程进行优化，可以根据运行时的参数进行自动选择并发布。同时需要精简gradle文件的数量，防止出现代码冗余的情况。**

### 1.3 使用步骤
- **步骤1：**

  - 复制 **upload.gradle** 文件到项目根目录
  - 在项目根目录的 **build.gradle** 文件中添加以下代码
    ```Java
    ext {

        //项目发布的版本号，根据实际情况填写
        sdk_version_name = "1.2.3.400"

        subprojects {
            apply from: rootProject.getRootDir().path + '/upload.gradle'
        }
    }
    ```

- **步骤2：**

  - 根据实际情况修改 **upload.gradle** 文件中的值，比如url、groupId、artifactId等。示例代码：
  ```Java
  ext {
      /**
       * Maven仓库的地址、访问用户名和密码
       */
      MAVEN_LOCAL_URL = "D:\\Test\\repository\\local"
      MAVEN_RELEASE_URL = "D:\\Test\\repository\\release"
      MAVEN_SNAPSHOT_URL = "D:\\Test\\repository\\snapshot"
      MAVEN_USERNAME = "123"
      MAVEN_PASSWORD = "321"

      /**
       * groupId : 唯一标识（通常为模块包名，也可以任意）
       */
      DEFAULT_GROUP_ID = 'com.example.mylib'

      /**
       * artifactId : 项目名称（通常为类库模块名称，也可以任意）
       */
      DEFAULT_ARTIFACT_ID = project.name

      /**
       * version : 版本号
       */
      DEFAULT_PUBLISH_VERSION = rootProject.ext.sdk_version_name
      DEFAULT_PUBLISH_VERSION_SNAPSHOT = rootProject.ext.sdk_version_name + '-SNAPSHOT'
  }
  ```

- **步骤3：**

  - 进入指定模块下，添加运行参数，开始发布
  ```bash
  #在该模块下使用命令（注意：windows系统下要用反斜杠——反人类的设计）
  gradlew -Psdkactive=release modulename:uploadArchives
  ```
### 1.4优点举例：
1. 引入upload.gradle文件后，可以在构建过程中根据输入的运行参数，实现动态灵活的多版本（release和snapshot）发布

-------

## 二、进阶使用

[具体实现（源代码)](./script/upload.pro.gradle)

### 2.1 使用一：
#### 2.1.1 目标
在upload.gradle的基础上，实现以下功能：指定多个artifact并上传；过滤其中某个artifact不上传；在pom文件中自动补充相关依赖

#### 2.1.2 问题场景
在项目中需要指定某个jar包上传到maven仓库中，同时将默认的aar过滤掉，在这种情况下还要保持相关依赖不丢失

#### 2.1.3 实现
**步骤一：**
引入文件 ```upload.pro.gradle```
**步骤二：**
指定需要上传的artifact
```java
artifacts {
    //指定task之后的产物
    archives project.tasks.findByName("generateJar4InjectKit")
    //指定产物的具体路径
    archives(name: artifact-id, file: file("$buildDir/outputs/apk/debug/framework-runtime-debug.apk"))
}
```

**步骤三：**
增加过滤器filter
```java
    repositories {
        mavenDeployer {
            //....
            //当有多个artifact时，可以通过filter进行过滤，最终上传需要的artifact
            addFilter('framework-runtime-jar') { artifact, file ->
                // 对artifact进行过滤，需要上传maven仓库的返回true，反之false
                return true
            }.withXml {
                // 可以编辑pom文件，补充其中的依赖
            
            }.project {
                // 补充发布信息中的其他信息
                version uploadObject.version
                groupId uploadObject.groupId
                artifactId uploadObject.artifactId
            }
        }
    }
```

### 2.2 使用二：
#### 2.2.1 目标：将项目build出来的产物（比如debug或者release版本的sdk），上传到maven仓。
**注意：与2.1小结所说的“执行产物的task或指定产物的路径”不同，这里仅需要将默认的release改为可灵活指定的release或者debug类型**

#### 2.2.2 原理
在project中的 ```build.gradle```文件中的```android{}```闭包中增加配置```defaultPublishConfig```。然后给该值指定```release```和```debug```。这样maven插件打包的时候，就可以取到release或者debug类型的产物了。

#### 2.2.3 步骤
```java
//根据执行脚本时传递的参数，指定release和debug。例如 gradlew xxxxx -Ppublishcfg=debug
def publish_config= project.hasProperty('publishcfg') ? project.property('publishcfg') : "release"

android {
    ...
    //指定maven发布的类型
    defaultPublishConfig publish_config
    ...
}

```



