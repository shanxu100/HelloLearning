# Android-JNI与NDK
[toc]

由于Java的跨平台的特性，导致Java同本地交互的能力不够强大，一些和操作系统相关的操作无法完成。通过JNI可以调用C和C++的代码，提高自己的本地交互能力
[《Android JNI(一)——NDK与JNI基础》](https://www.jianshu.com/p/87ce6f565d37)
[《JNI编程如何巧妙获取JNIEnv》](https://cloud.tencent.com/developer/article/1650769)
[《java - 调用JNI方法时如何获取Android上下文实例？》](https://www.coder.work/article/1495537)
[《Android JNI 使用总结》](http://blog.guorongfei.com/2017/01/24/android-jni-tips-md/)
[《JNI 提示》](https://developer.android.com/training/articles/perf-jni.html)
[《JNI多线程调用关键总结》](https://iamlwl.me/2016/05/23/16052301/)

## 1、JNI与NDK

> 参考：https://blog.csdn.net/carson_ho/article/details/73250163

### 1.1 JNI
- **定义：** ```Java Native Interface```，即 Java本地接口
- **作用：** 使得Java 与 本地其他类型语言（如C、C++）交互；JNI 是 Java 调用 Native 语言的一种特性；JNI 是属于 Java 的，与 Android 无直接关系
- **步骤：**
    1. 在Java中声明Native方法（即需要调用的本地方法）
    2. 使用javac编译上述Java源文件，得到 ```.class```文件
    3. 针对上述```.class```文件，通过```javah -jni java文件全名```命令导出JNI的头文件（.h文件）
    4. 编写```本地代码```:实现在 Java中声明的Native方法（如 Java 需要与 C++ 交互，那么就用C++实现 Java的Native方法）。
    5. 将本地代码编译成**动态库**（Windows系统下是```.dll```文件，如果是Linux系统下是```.so```文件，如果是Mac系统下是```.jnilib```）
    6. 通过Java命令执行 Java程序，最终实现Java调用本地代码

### 1.2 NDK
- **定义：** ```Native Development Kit```，是 Android的一个工具开发包。
- **作用：** 可以帮助开发者在 Android的场景下 使用JNI，把C/C++编译为```.so```文件。Gradle可以自动将so和应用一起打包成 APK。NDK是属于 Android 的，与Java并无直接关系。

## 2、在Android项目中使用

### 2.1 构建
- 指定CMakeLists
```java
externalNativeBuild {
    cmake {
        path "src/main/cpp/CMakeLists.txt"
        version "3.10.2"
    }
}
```
- 编辑CMakeLists
[编译脚本和相关参数太过丰富...略](../CPP/cpp-CMake.md)

### 2.2 集成
- 项目中默认存放路径
```
<project root>
    └── app
        ├──libs(可放置本地jar包)
        | 
        └── src
            └── main
                ├── jniLibs(放置so)
                    ├── arm64-v8a
                    │   └── libxxx.so
                    ├── armeabi-v7a
                    │   └── libxxx.so
                    ├── x86
                    │   └── libxxx.so
                    └── x86_64
                        └── libxxx.so
```
- 修改项目中存放路径
```java
sourceSets.main.jniLibs.srcDirs = ['src/main/libs']
// 或
android {
    ...
    sourceSets {
        ...
        main.jni.srcDirs = []
        main.jniLibs.srcDirs = ['libs']
    }
}
```

- apk安装后so的解压路径
```bash
# 真正路径
/data/app/包名+随机后缀/lib/arm/

# 软连接
/data/data/包名/lib/arm
```
- 代码中获取so的解压路径
```java
context.getApplicationInfo().nativeLibraryDir;
```

- 针对指定abi进行构建
```java
android {
    defaultConfig {
        ndk {
            abiFilters 'armeabi', 'armeabi-v7a'
        }
    }
}
```

- 排除指定文件不参与构建
```java
android {
    packagingOptions {
        exclude 'pom.xml'
        exclude '.classpath'
        exclude '.project'

        exclude '**/libtup_im_basemessage.so'
        exclude '**/libtup_im_clib.so'
    }
}
```


### 2.3 加载
[Android 的 so 文件加载机制](https://juejin.im/post/5bc832f1e51d450e6973c0be)
- System.loadLibrary ();

    1. 只需要写so文件的名称。  
        例如，加载 libabc.so ，那么名称就应该为 abc
    2. 查找路径：
    ```
     /system/lib 
     /data/data/包名/lib 
    ```
- System.load()
    1. 需要写so文件的路径

- 调用so中的方法
    1. 使用native方法
    2. native方法所的**包名、类名以及方法名**均要和**so中的方法名**体现的一致


## 3 Native方法调用Java(HelloSeries 项目中有详细例子)
1. 先使用```FindClass```方法获取指定类class；
2. 在使用```GetStaticMethodID```方法或者```GetMethodID```获取静态和非静态的方法id。
3. 在使用```CallObjectMethod```或者```CallStaticObjectMethod```方法调用静态非静态方法，对于没有返回值的方法，函数为```CallStaticVoidMethod```形式。

https://juejin.cn/post/6844903886633828360
https://cloud.tencent.com/developer/article/1650769
https://www.coder.work/article/1495537
http://blog.guorongfei.com/2017/01/24/android-jni-tips-md/


## 4、 JNI中的关键接口
### 4.1 JavaVM
```JavaVM``` 是JNI定义的两大核心数据接口之一，每个进程只允许有一个JavaVM的实例。通过```System.loadLibrary(xxx)```方式加载so的时候，```jint JNI_OnLoad(JavaVM* vm, void* reserved);```函数就会被调用。

### 4.2 JNIEnv
```JNIEnv``` 是JNI定义的另一个核心数据接口。
1. 如果从Java中定义的Native方法，那么实现该Native接口的时候，```JNI```就会将``JNIEnv``传递到Native代码中。
2. 如果一个Native代码调用到Java代码，那么可以通过```jvm->GetEnv()```方法获取``JNIEnv``对象。
3. 如果一个纯Native代码调用到Java代码，那么需要通过```jvm->AttachCurrentThread()```方法先将当前线程Attach到JavaVM中；当执行完时，需要通过```jvm->DetachCurrentThread()```方法将当前线程解绑，否则线程退出就会有问题。
   
### 4.3  jclass
调用JNIEnv::FindClass可以获取对于的 class 的实例:
```c++
jclass clazz = env->FindClass("full/name/of/your/class");
```

### 4.4 jmethodID
获取一个方法的引用需要调用JNIEnv::GetMethodID相关方法。需要额外注意的地方是，构造函数的函数名称为```<init>```
```c++
// 构造方法
jmethodID = env->GetMethodID(clazz, "<init>", "()V");

// 实例方法:以 void method(){} 为例
jmethodID = env->GetMethodID(clazz, "method", "()V");

// 静态方法:以 static void method(){} 为例
jmethodID = env->CallStaticVoidMethod(clazz, "method", "()V");
```

### 4.5 jfieldID
类比```jmethodID```，略:
```c++
// 以 String fieldName; 为例
jfieldID = env->GetFieldID(clazz, "fieldName", "Ljava/lang/String;");
// 以 static String fieldName; 为例
jfieldID = env->GetStaticFieldID(clazz, "fieldName", "Ljava/lang/String;");
```
### 4.6 NewObject
在Native中直接创建一个java对象，调用 JNIEnv::NewObject即可。调用该函数需要 ```jclass``` 、构造函数的 ```jmethod```以及构造函数的参数
```c++
jobject instance = env->NewObject(clazz, jinitMethodID,params....);
```
### 4.7 调用方法

```c++
// 调用静态方法
env->CallStaticVoidMethod(clazz,jmethodID,params1, params2);

// 调用实例方法
env->CallVoidMethod(instance, methodID, args);

// 调用各种返回值的方法...略
// CallxxxxMethod, CallStaticxxxxMethod
```

### 4.8 局部引用与全局引用
**所有从Java传递到native函数中的参数和从JNI函数中返回的对象都是局部引用**。
这条规则对于所有的 jobject 的子类（包括 jclas， jstring，jarray）都是适用的。

如果想要引用保持有效，需要调用```JNIEnv::NewGlobalRef```来获取一个全局的引用。
```c++
global_jobject = env->NewGlobalRef(my_jobject);
```
如果想要删除全局引用，需要调用```JNIEnv::DeleteGlobalRef```。
```c++
 env->DeleteGlobalRef(global_jobject);
```

## 5、在So中动态添加版本号

### 5.1 在gradle中配置cmake参数
```java
// build.gradle

android {

    // ...略

    defaultConfig {
        minSdkVersion rootProject.ext.minSdkVersion
        targetSdkVersion rootProject.ext.targetSdkVersion
        versionCode rootProject.ext.rtsa_4c_sdk_version_code
        versionName rootProject.ext.rtsa_4c_sdk_version_name

        // This block is different from the one you use to link Gradle
        // to your CMake or ndk-build script.
        externalNativeBuild {
            // For ndk-build, instead use ndkBuild {}
            cmake {
                // 在这里定义参数 VERSION_NAME ，指向gradle中原有的versionName字段
                arguments "-DVERSION_NAME=\"${versionName}\""

            }
        }
        // ...略
    }
    // ...略
}
```

### 5.2 在```CMakeLists.txt```中读取参数，并定义宏
```shell
# 读取gradle中指定的版本号
ADD_DEFINITIONS(-DMY_VERSION_NAME=${VERSION_NAME})
```

### 5.3 在项目中使用宏```MY_VERSION_NAME```
```c
// Version.h 头文件

#ifndef MY_VERSION_H
#define MY_VERSION_H

#ifdef MY_VERSION_NAME

// 使用指定的版本号
#define MY_VERSION_STR "MY SDK " MY_VERSION_NAME

#else

// 不指定版本号时，使用默认值
#define MY_VERSION_STR "MY SDK 1.0.0.100"

#endif

#endif
```

### 5.4 在项目中随时使用宏 MY_VERSION_STR 即可


## FAQ

1. 纯Native线程通过JNI调用FindClass失败

> https://blog.csdn.net/Tencent_Bugly/article/details/78898975

如果在Java层调用到native层，会携带栈桢(stack frame)信息，其中包含此应用类的Class Loader，因此场景下JNI能通过此应用类加载器获取类信息。 而在使用自己创建并Attach到虚拟机的线程时，因为没有栈桢(stack frame)信息，此场景下虚拟机会通过另外的系统类加载器寻找应用类信息，但此类加载器并未加载应用类，因此FindClass返回空。









