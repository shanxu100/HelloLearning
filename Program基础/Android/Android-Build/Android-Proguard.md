# Android ProGuard

## 混淆
参考：
[《代码混淆到底做了什么》](https://juejin.cn/post/6930648501311242248)

### 1. 概念
Android中的“混淆”可以分为两部分。
1. Java 代码的优化与混淆，依靠 proguard混淆器来实现
    - **压缩**：从project和依赖的library中，移除无效的类、类成员、方法、属性等
    - **优化**：分析和优化方法的二进制代码，但可能无法保证在所有版本的虚拟机上运行
    - **混淆**：把类名、属性名、方法名替换为简短且无意义的名称
    - 预校验：Android平台不需要(无效)
    （在Android项目中我们可以选择将“优化”和“预校验”关闭，对应命令是-dontoptimize、-dontpreverify（当然，默认的 proguard-android.txt文件已包含这两条混淆命令，不需要开发者额外配置））

2. 资源压缩：将移除项目及依赖的库中未被使用的资源(资源压缩严格意义上跟混淆没啥关系，但一般我们都会放一起用)

3. 迭代
    - ProGuard：一个通用的 Java 字节码优化工具
    - R8：专为 Android 设计，编译性能和编译产物更优秀。Android Gradle Plugin 3.4以后的版本默认使用R8

### 2. 开启、关闭混淆
[ProGuard官网](https://www.guardsquare.com/zh-hans/产品介绍/proguard)

开关：
```inifyEnabled true```

**例如：**
```java
android{

    // 在 buildType 中设置
    buildTypes {
        release {
            //开启混淆
            minifyEnabled true
            /**
            * 混淆文件的位置
            * proguard-android.txt 默认的混淆文件(SDK目录/tools/proguard/)
            * proguard-rules.pro 让我们自行添加混淆规则文件(相应module的目录下)
            */
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'

            //打开资源压缩，移除无用的resource文件
            shrinkResources true
            // Zipalign优化  
            zipAlignEnabled true 
           
            signingConfig signingConfigs.config
            }
        }

}

```

### 3. 自定义混淆规则

### 4. 混淆结果
- dump.txt：描述APK文件中所有类的内部结构
- mapping.txt：提供混淆前后类、方法、类成员等的对照表。
- seeds.txt：列出没有被混淆的类和成员
- usage.txt：列出被移除的代码
**输出目录： \<module-name>/build/outputs/**

每次混淆打包后，都会覆盖之前的mapping.txt,所以最好每次发版都保存下当前的mapping.txt，并做好标注区分出版本，以便以后使用

### 5. 还原混淆代码

1. 命令号工具：retrace
**位置： \<sdk-root>/tools/proguard/**
```bash
#把需要还原的堆栈信息保存在 obfuscated_trace 文件中
retrace.bat -verbose mapping.txt obfuscated_trace.txt
```

2. GUI工具：proguardgui
**位置：\<sdk-root>/tools/proguard/bin**

---------