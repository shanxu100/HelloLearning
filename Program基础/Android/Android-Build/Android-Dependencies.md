# Android Dependencies相关
参考：
[添加构建依赖项](https://developer.android.com/studio/build/dependencies)
[Gradle 提示与诀窍](https://developer.android.com/studio/build/gradle-tips?hl=zh-cn#shrink-your-code)


## 1、依赖方式
### 1.1 Android Gradle Plugin支持的依赖类型
1. **implementation**
在编译时隐藏自己使用的依赖（但是在运行时这个依赖对所有模块是可见的），**只能在内部使用此模块**。比如我在一个libiary中使用implementation依赖了gson库，然后我的主项目依赖了libiary，那么，我的主项目就无法访问gson库中的方法。这样的好处是**编译速度会加快。**   
**扩展：**
- releaseImplementation：仅针对Release 模式的编译和最终的Release apk打包。
- debugImplementation：只在debug模式的编译和最终的debug apk打包时有效
- testImplementation：只在单元测试代码的编译以及最终打包测试apk时有效。
- androidTestImplementation

2. **api**
依赖项在编译时对模块可用，并且在编译时和运行时还对模块的消费者可用
    （作用与compile完全相同，compile现在已弃用）。一般情况下，您应当仅在库模块中使用它。 应用模块应使用 implementation，除非您想要将其 API 公开给单独的测试模块。

3. **compileOnly**
只在编译时有效，不会参与打包。应用场景：运行时不需要，例如仅源代码注解或注释处理器；编译时仅需要其API，但具体实现由别的module实现。
compileOnly**经常用于解决依赖冲突等问题**，一般第三方库中，比较常用的依赖，如support、gson、Eventbus等等。

4. **runtimeOnly**
只在生成apk的时候参与打包，编译时不会参与，很少用。依赖项仅在运行时对模块及其消费者可用

### 1.2 Maven支持的依赖类型
1. **compile**
编译依赖范围，此为默认值，对编译、测试和运行三种环境变量都有效。
2. **test**
测试依赖范围，仅对测试环境变量有效。
3. **provided**
已提供的依赖范围，对编译和测试环境变量都有效。
4. **runtime**
运行依赖范围，对测试和运行环境变量都有效。
5. **system**
系统依赖范围，对编译和测试环境变量都有效，但由于此依赖不是通过 Maven 仓库解析的，而且往往与本机系统绑定，可能造成构建的不可移植，因此应该谨慎使用。
6. **import**
导入依赖范围，并不会对上面的三种环境变量产生实际的影响。

| 依赖范围(scope) | 编译环境变量有效 | 测试环境变量有效 | 运行环境变量有效
| -- | -- | -- | -- |
compile | Y | Y | Y
test | -- | Y | N
provided | Y | Y | --
runtime | -- | Y | Y
system | Y | Y | --

## 2 依赖冲突解决原则
### 2.1 最短路径优先原则
举例： 
A -> B -> Y(2.0)
A -> C -> D -> Y(1.0)
此时Y使用2.0

### 2.2 同路径优先声明原则
举例： 
A -> B -> Y(2.0)
A -> C -> Y(1.0)
因为B在C前面声明，所以Y使用2.0

> https://developer.android.com/studio/build/dependencies?agpversion=4.0#dependency-order


## 3、语法
### 3.1 依赖打包
- **情况一**：依赖本地包
    将SDK所依赖的jar包放在```(project)/libs```目录下
    ```java
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    // 或
    implementation files('libs/jxl-2.6.12.jar')
    ```
    将SDK所依赖的jar包放在其他目录下，然后使用如下的方式集成
    ```java
    implementation files('src\\main\\libs\\xxx.aar')
    ```
    **使用这种方式打出的sdk里面，不包含libs目录下的包。如果发布到maven仓，pom文件中也没有该依赖。**  
    **此处写的相对路径，均指<font color="red">相对于该gradle文件</font>所在的相对目录。**

- **情况二**：依赖本地project
    ```java
    // 项目依赖
    implementation project(':mysdk_lib')
    ```

- **情况三**：依赖Maven仓库包
    配置build.gradle，使用implementation等方式引入依赖，打出来的SDK包没有依赖，pom文件中存在该依赖项的条目
    - 简写
    ```implementation 'com.example.sdk:mysdk:1.0.0.0'```
    - 完整：
    ```implementation group: 'com.example.sdk', name: 'mysdk', version: '1.0.0.0'```


### 3.2 依赖排除
**问题一：项目中依赖了两个不同版本的jar包，产生冲突**

```java
implementation ('com.dou361.update:jjdxm-update:1.0.3'){
    exclude group: 'com.dou361.download',module:'jjdxm-download'
}
```
**问题二：两个不同的jar中，有相同的“包名+文件名”，导致某个文件冲突**




### 3.3 Flavor+BuildType 的依赖

**关键：必须在 configurations 代码块中初始化配置名称**
```java
configurations {
     // Initializes a placeholder for the productDebugImplementation dependency configuration.
    productDebugImplementation {}
}

dependencies {
    productDebugImplementation ("com.example.sdk:mysdk:1.0.0.100")
}
```

### 3.5 强制使用指定版本
- 在根目录下的build.gradle 文件中配置
```java
allprojects {
    configurations.all {
        resolutionStrategy {
            //强制使用某些版本的依赖
            force 'com.android.support:support-v4:26.1.0','com.android.support:appcompat-v7:26.1.0'
        }
    }
}
```

### 3.6 打印依赖树
- 方式一：
```gradlew 模块名:dependencies```
可以将其输出到文件中查看

- 方式二：
``` gradle build --scan ```
后会生成 HTML 格式的分析文件的分析文件。分析文件会直接上传到Scan官网，命令行最后会给出远程地址

### 3.7 Kotlin + buildSrc：更好的管理Gadle依赖

[《Kotlin + buildSrc：更好的管理Gadle依赖》](https://juejin.cn/post/6844903615346245646#heading-0)






