# Android Build相关

[《7个你应该知道的Gradle实用技巧》](https://juejin.cn/post/6947675376835362846)
[《详解compileSdkVersion、minSdkVersion、targetSdkVersion及buildToolsVersion，让选择不再迷茫》](https://www.kaelli.com/26.html)


## 1、build-tool\targetSDKVersion\compileSDKVersion\minSDKVersion的关系？
- `compileSdkVersion`:指定了Gradle编译你的App时使用的Android API版本，你的App可以使用该版本或者更低版本的API特性。简单来说，如果你用了一些比较新的API特性，那么你的`compileSdkVersion`就不能低了。通常它应该是Android最新的稳定版本。
- `targetSdkVersion`:如果没有设置，则默认值`为minSdkVersion`。当你设置了`targetSdkVersion`的时候，表示你已经充分测试过了你的App在该目标版本的运行情况，系统不应该启用任何兼容性行为来保持你的App与目标版本的向前兼容性。
- `minSdkVersion`:指定了App运行所需最低的API级别，比如你设置它为23（Android 6.0），那么该App不能在低于Android6.0版本的设备上安装。
- `buildToolsVersion`:指定了Gradle在编译App时使用的SDK build tools、命令行、程序、编译器等的版本。一般来说，选择跟`compileSdkVersion`同一个大版本下的最新稳定版本即可，

### 2 buildConfig中增加属性
例如： 
```java
android{

    // 在 buildType 中设置
    buildTypes {
        release {
            // 注意引号
            buildConfigField "boolean", "LOG_DEBUG", "false" 
            buildConfigField("String", "API_HOST", "${API_DEV_HOST}")
            buildConfigField("String", "flavorcfg", "\"${rootProject.ext.uconference_flavor_cfg}\"")
        }
    }

    // 在 flavor 中设置
    flavorDimensions "env"
    productFlavors {
        envRoot {
            // 默认环境
            dimension "env"
            buildConfigField("String", "flavorcfg", "\"root\"")
        }
        envUnite {
            dimension "env"
            buildConfigField("String", "flavorcfg", "\"unite\"")
        }
        // ...略
    
    }

}

```




