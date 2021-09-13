# Dynamic Loader相关

## 1. Java中的Class文件与ClassLoader


-----------------
## 2. 一个HelloWorld的apk中有哪些文件？有几个dex文件？apk安装过程与启动过程？

### 2.1 应用安装涉及到如下几个目录：
- system/app:
	系统自带的应用程序，获得adb root权限才能删除
- data/app:
	用户程序安装的目录，安装时把apk 文件复制到此目录
- data/data:
	存放应用程序的数据,例如cache, database、lib、shared_perfs等
- data/dalvik-cache:
	将apk中的dex文件安装到dalvik-cache目录下(dex文件是dalvik虚拟机的可执行文件,其大小约为原始apk文件大小的四分之一)

### 2.2 安装Apk的步骤：
- 复制APK安装包到/data/app目录下 ***（完整的apk文件，重命名为 包名+xxx.apk??? ）***
- 解压：把其中的classes.dex 拷贝到/data/dalvik-cache ***（其命名规则是 apk路径+classes.dex, 如： data/app/com.test.demo2.apk/classes.dex）***
- 在/data/data目录下创建对应的应用数据目录，目录名称为 **包名**  
- 要注意的是， 安装过程并没有把资源文件, assets目录下文件拷贝出来，他们还在apk包里面呆着。所以，当应用要访问资源的时候，其实是从apk包里读取出来的。
  其过程是，首先加载apk里的resources.arsc(这个文件是存储资源Id与值的映射文件)，根据资源id读取加载相应的资源。

***向资源管理器注入APK资源，解析AndroidManifest文件，并，然后针对Dalvik/ART环境优化dex文件，保存到dalvik-cache目录，将AndroidManifest文件解析出的组件、权限注册到PackageManagerService并发送广播***

## 3. dex文件、class文件、apk文件的区别

**apk中包含多个dex文件**：
Android5.0之前，Dalvik虚拟机包含1个dex文件；
5.0之后，ART虚拟机天生支持包含多个dex文件。它在APK安装时，会预先扫描所有的classes(..N).dex文件，把它们编译合成一个.oat的可执行文件
**classes.dex文件存的是什么？classes2.dex文件存的是什么？以此类推classesN.dex文件存的是什么**



## 4. Android系统中的类加载机制

- ClassLoader
	- BootClassLoader：负责预加载。由Zygote进程创建的；继承ClassLoader，是ClassLoader的内部类（单例模式）
	- URLClassLoader：用于加载内部存储的jar文件（Android动态加载一般用不到）
	- BaseDexClassLoader
		- DexClassloader：加载外部存储的jar、dex、apk类型的文件
		- PathClassLoader：在应用启动时创建，加载dex文件和app安装路径下的apk文件（应用当前的ClassLoader）
```java
          BaseDexClassLoader
          |                 |
    PathCLassLoader     DexClassLoader
```
  - PathClassLoader: 只能加载已经安装应用的dex或者Apk文件。一般用来加载已经安装到设备上的.apk，因为应用在安装的时候已经对 apk 文件中的 dex 进行了优化，并且会输出到 /data/dalvik-cache 目录下（android M 在这目录下找不到，应该是改成了 /data/app/com.example.app-x/oat 目录下），所以它不需要指定优化后 dex 的输出路径。PathClassLoader不能主动从zip包中释放出dex，因此只支持直接操作dex格式文件，或者已经安装的apk（因为已经安装的apk在cache中存在缓存的dex文件）
  **为什么??** **什么叫“已经安装应用的dex或者Apk文件”？？**
  - DexClassLoader:可以加载jar/apk/dex，也可以从SD卡中加载，可以加载已安装的APK，也可以加载未安装的APK,需要指定优化后的 dex 文件的输出路径；也是插件化和热修复的基础，在不需要安装的情况下完成dex的加载。


**BaseDexClassLoader的四个参数**

  - String dexPath：dex 文件的路径，多个路径用 : 隔开
  - File optimizedDirectory：优化后的 dex 存放目录。这个路径只能为app的私有空间
    补充：dex文件被虚拟机优化后形成odex（optimized dex）。这个odex是机器相关的优化。
  - String librarySearchPath：库文件的的搜索路径，一般来说是 .so 文件的路径
  - ClassLoader parent：父加载器，一般可以通过 Context#getClassLoader()到应用程序的类加载器然后把它传进去



**动态加载：**
1. 将插件（apk）复制到应用app的内部文件夹中，使用PathClassLoader加载该插件，获得一个ClassLoader。
2. 然后利用该 ClassLoader的loadClass()方法加载得到class对象。
3. 最后利用class对象和反射技术，调用相关的方法。


------
## Android源码
### Android SDK
在开发项目时，使用的Android SDK是以provided（compileOnly）的方式依赖的。
Android SDK自带的Source源码包很小，并没有包括所有的Android Framework的源码，仅仅提供给应用开发参考用，一些系统类的源码并没有给出。
但这些API在ROM中是实际存在的。
所以，需要通过其他方式查看Android的源码。

### 查看Android源码
- 在线查看
    > https://source.android.com/
    > https://www.androidos.net.cn/

- sourceInside工具查看

- 通过IDE查看源码
RuntimeException(“Stub!”)表示实际运行时的逻辑会由Android ROM里面相同的类代替执行。

------
## Native Library
Native Library，一般我们译为本地库或原生库，是由C/C++编写的动态库(.so)，并通过JNI(Java Native Interface)机制为java层提供接口。
> https://www.jianshu.com/p/4aaa66c62fef

------------




## 动态加载apk
- dpi类型apk：
> AssetManager
> Activity(context) - mResources - mResourcesImpl - mAssets - mApkAssets

- abi类型apk：
Activity(context) - shadow$_klass_ - classLoader - pathList - nativeLibraryDirectories

- 字节码apk：
Activity(context) - shadow$_klass_ - classLoader - pathList - dexElements

## 
对象下面没有了之前加载的apk

----------------

## 安装nativeCode（so文件）
### v26(8.x/9.0)
0. 获取classloader对象中的```pathList```对象（以下反射操作均调用该对象中的field和method）
1. so文件父路径去重：将存放so文件的**父路径**```List<File> parentsFiles```值，与反射得到的```List<File> nativeLibraryDirectories```比较，然后从```List<File> parentsFiles```中删除重复的元素。
2. 调用方法```makePathElements()```：以去重后的```List<File> parentsFiles```为参数，调用上述方法，得到Native数组```Object[] addedElements```
3. 反射```nativeLibraryPathElements```变量，得到系统原有的Native数组```originElements```.
4. 将两个Native数组```originElements```和```addedElements``` 拼接成一个数组，写回```nativeLibraryPathElements```变量中

### V23(6.0/7.x)
与V26的方法一致。只是在第二步```makePathElements()```方法有较多的参数

### v14(5.x)
修改数组类型变量```nativeLibraryDirectories```：同样需要将存放so文件的**父路径**```List<File> nativeDirs```值，去重后与原有的```nativeLibraryDirectories```值组成新的数组，写回到```nativeLibraryDirectories```中去。




## 安装字节码（dex文件）

### v26
0. 获取classloader对象中的```pathList```对象（以下操作均反射该对象中的field和method）
1. 从```pathList```对象中反射数组类型的属性```dexElements```。对于该数组中的每个元素，找到```path```属性（File类型）。注意：如果要安装的split apk（该apk中包含dex文件？）已经存在（即与path相等），则不安装这个split apk。如果这个split apk需要优化（optimized），同样不安装。
3. 调用```pathList```对象中的```makePathElements```方法，得到新的数组。
4. 这个新数组插入到```pathList```对象的```dexElements```数组后面，完成动态加载。

### v23
与v14相同，只不过是 最后调用 ```makePathElements```方法产生新的数组，再追加到旧的数组后面

### v14
1. 获取classloader对象中的```pathList```对象（属性）
2. 从```pathList```对象中反射数组类型的属性```dexElements```。对于该数组中的每个元素，找到```zip```属性（File类型）。注意：如果要安装的split apk（该apk中包含dex文件？）已经存在（即与zip相等），则不安装这个split apk。如果这个split apk需要优化（optimized），同样不安装。
3. 调用```pathList```中的```makeDexElements```方法，得到新的数组。
4. 这个新数组插入到```pathList```对象的```dexElements```数组后面，完成动态加载。

## 安装资源文件（语言包和图片等资源文件）

0. 获取```AssetManager```对象，
1. 反射得到```addAssetPath```方法
2. 调用该方法，以要加载的apk路径为参数，得到一个int型返回值。

------------------

## 未完成：

## 5. so库的动态加载


## 6. Android中的R文件和resources.arsc文件之间的关系

Activity中的 attachBaseContext()方法在生命周期中的位置

如何判断文件新旧

super()和super.的区别？


