# Android Flavor

## 1、Build Type和Product Flavor

### 1.1 概述
作用：配置“版本类型”和“产品特性”
参考：
[《配置构建变体》](https://developer.android.com/studio/build/build-variants)
项目的输出将会拼接所有可能的Build Type和Product Flavor的组合。

## 2、配置举例：
**默认buildType只设置debug和release两种类型**


### 2.1 flavor只有一维
在项目中project的android{}闭包中添加如下代码
```java
    flavorDimensions "d1"
    productFlavors {
        big {
            dimension "d1"
        }
        small {
            dimension "d1"
        }
    }
```
会产生的build命令：
（以assemble命令为例，bundle命令是一样的）
```bash
# 4个小命令，每个命令生成一个apk
gradlew assembleBigDebug
gradlew assembleSmallDebug
gradlew assembleBigRelease
gradlew assembleSmallRelease

# 最大的命令，相当于同时执行4个小命令，生成4个apk
gradlew assemble

# 只确定一个维度，可以同时生成俩apk

# 生成Big类型的Debug和Release俩apk。其他同理
gradlew assembleBig
gradlew assembleSmall
gradlew assembleDebug
gradlew assembleRelease

```


### 2.2 flavor有二维
在项目中project的android{}闭包中添加如下代码
```java
    //创建产品特性的维度，不可多，不可少
    flavorDimensions "d1","d2"

    productFlavors {
        big {
            //每一个产品特性都必须“与且只与”一个维度相关联
            dimension "d1"
        }
        small {
            dimension "d1"
        }
        newp{
            dimension "d2"
        }
        oldp{
            dimension "d2"
        }
    }
```
- 如上面代码所示，为flavor添加了两个维度：d1和d2。
- 立足于这两个维度，可以将已有的flavor分为两个阵营：
    - d1阵营包括big和small，描述大小
    - d2阵营包括newp和oldp，描述新旧
- flavor这两个阵营相互结合可以形成4个flavor，分别为:
    - big-newp\big-oldp\small-newpp\small-oldp
- **最后，flavor会与buildType相结合，形成完全的输出类型。即，包括m\*n\*k个变体**

### 输出举例：
```bash
# 8个小命令，每个命令生成一个apk
gradlew assembleBigNewpDebug
gradlew assembleBigNewpRelease
...
gradlew assembleSmallOldpRelease
# 2*2*2=8 个apk文件
outputs\apk\bigNewp\debug\app-big-newp-debug.apk
outputs\apk\bigNewp\release\app-big-newp-release.apk
outputs\apk\bigOldp\debug\app-big-oldp-debug.apk
outputs\apk\bigOldp\release\app-big-oldp-release.apk
outputs\apk\smallNewp\debug\app-small-newp-debug.apk
outputs\apk\smallNewp\release\app-small-newp-release.apk
outputs\apk\smallOldp\debug\app-small-oldp-debug.apk
outputs\apk\smallOldp\release\app-small-oldp-release.apk
# 同理，有8个bundle小命令，生成8 个aab文件
outputs\bundle\bigNewpDebug\app-big-newp-debug.aab
outputs\bundle\bigNewpRelease\app-big-newp-release.aab
...
#注意：apk和bundl两个文件中的命名规律不同

# 一个大命令，生成8个apk

```

### 2.3 flavor有三维
同理


## 3、过滤变体flavor
Gradle 会为您配置的产品类型和 Build 类型的每种可能组合创建构建变体。但是，某些构建变体可能并不是您需要的，或者对于您的项目来说没有意义。您可以通过在模块级 build.gradle 文件中创建变体过滤器来移除某些构建变体配置。

```java
variantFilter { variant ->
    def names = variant.flavors*.name
    // To check for a certain build type, use variant.buildType.name == "<buildType>"
    if (names.contains("local")) {
        // Gradle ignores any variants that satisfy the conditions above.
        setIgnore(true)
    }
}
```

## 4、 FAQ
### 1.
项目中如果没有定义flavor同样也会有Build Variant，只是使用的是默认的flavor和配置。default(默认)的flavor/config是没有名字的，所以生成的Build Variant列表看起来就跟Build Type列表一样

### 2.
1. buildTypes不能设置 applicationId
2. productFlavors不能设置 minifyEnabled
3. 如果需要同时设置混淆和applicationId,需要flavor和buildType组合
