# Android Build相关


## 配置签名信息

### 1、方式一：可视化配置

1. Project Structure —— Modules —— 选择一个module —— Signing Configs
        —— 根据情况添加若干个配置（release、debug、other等等，名称任意）


2. Project Structure —— Build Variants —— 选择一个module —— Build Type ——
        —— 根据情况添加若干种配置（release、debug、other等等，名称任意）
        —— 选择一种配置 —— Singing Config —— 选择步骤1中添加的签名配置

### 2、方式二：编辑gradle进行配置
1. **在module中的build.gradle中的android{}闭包中**根据情况添加签名配置信息
```java
    signingConfigs {
        release {
            storeFile file('D:\\Test\\key\\TestReleaseKey.jks')
            storePassword '123456'
            keyAlias = 'key0'
            keyPassword '123456'
        }
        debug {
            //省略...
        }
        other {
           
        }
    }
```
2. **在module中的build.gradle中的android{}闭包中**根据情况配置buildType
```java
    buildTypes {
        release {
            //省略...
            signingConfig signingConfigs.release
        }
        debug{
            signingConfig signingConfigs.debug
        }
        // 名称可以任意
        other1 {
            signingConfig signingConfigs.other
        }
    }

```

### 3、查看签名
```bash
方法一：
jar xf demo.apk META-INF/CERT.RSA
keytool -printcert -file META-INF/CERT.RSA

方法二：
keytool -list -printcert -jarfile xxx.apk　
```
---------------