# Android Build相关

[《7个你应该知道的Gradle实用技巧》](https://juejin.cn/post/6947675376835362846)


## 1、build-tool\targetSDKVersion\compileSDKVersion\minSDKVersion的关系？




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




