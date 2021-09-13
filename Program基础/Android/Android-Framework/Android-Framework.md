# Android-Framework

[toc]


## 1、启动流程

### 1.2 Android开机流程
```
init进程
	启动servicemanager
	创建zygote进程
		SystemServer进程
			启动各种ManagerService（AMS，PMS，WMS）
			启动launcher程序
```
### 1.3 App启动流程
参考：  
[《慢~再来梳理一遍Activity的启动流程》](https://juejin.cn/post/6940155030578135048)

launcher-startActivity-AMS-(pause)-zygote-新进程ActivityThread-(main函数)-向AMS注册-AMS通知ActivityThread创建Activity并执行生命周期

1. 应用程序launcher调用startActivityForResult()，```通过Binder通信```通知AMS，它要启动一个新的Activity，并且准备要启动的Activity的相关信息；
2. AMS通过binder通知，使launcher进入pause状态。
3. 然后AMS通过 ```socket 通信```告知 Zygote 进程 fork 子进程（app进程），然后在子进程中启动```ActivityThread```，并执行```main()```函数。
4. AMS通过Binder（ApplicationThread）通知相应的```ActivityThread```，真正执行Activity的启动操作了：比如通过ClassLoader导入相应的Activity类，通过Instrumentation初始化Activity和调用Activity的生命周期

### 1.4 App内Activity启动流程
Activity1-startActivity-AMS-(pause)-在同一个```ActivityThread```-加载Activity2类，执行生命周期

1. 应用程序的MainActivity.startActivityForResult()通过Binder通知AMS，它要启动一个新的Activity；
2. AMS通过Binder（ApplicationThread）通知旧的Activity进入pause状态。
3. 然后AMS然后判断是 “需要创建新的进程启动Activity” 还是 “在旧的Activity所在的进程中启动Activity” 。  
   注：步骤2和3中，AMS进行了在ActivityStackSupervisor和ActivityStack之间一系列复杂的调用链:
4. AMS通过Binder（ApplicationThread）通知相应的```ActivityThread```，真正执行Activity的启动操作了：比如加载Activity的类，初始化Activity和调用Activity的生命周期
5. 
**ActivityManagerService 和 Instrument 的区别**

**```ActivityThread```的作用和地位**

## 2、Activity的finish 流程

### 2.1 为什么```Activity.finish()```之后延迟xx秒才回调onDestroy?
> 参考：https://www.jianshu.com/p/479b78235361
> 参考：https://juejin.cn/post/6898588053451833351

> 从调用finish后，到回调onStop和onDestroy之间的整个正常流程。这中间经历了多次App侧和AMS侧的进程间通信，以及两端各自进程内通信。正常流程中较为关键的一点是，要在**新的Activity显示（onResume）之后，且App侧的主线程空闲下来**才会通知AMS执行后续流程，将关闭的Activity销毁。

> 由于**要关闭的Activity B或者将要打开的Activity A往主线程的MessageQueue中连续不断的post了大量的msg**，导致主线程一直在不断的进行消息循环处理这些消息而没有得到停歇。因此，ActivityThread中Idler.queueIdle方法没有被调用的机会，App侧也就不能向ActivityManagerService(AMS)发起IPC告知自己有空闲时间来处理AMS侧的任务，也就是AMS向App侧发起IPC来进行Activity B的销毁的正常流程被阻断了。所以，finish Activity B后，onDestroy不会被及时回调

> 要想使得Activity的onStop和onDestroy尽快得到回调，我们就该在写代码的时候，**及时关闭、清理、移除不必要的主线程消息，并且尽可能的保证每个消息处理时间不要太长**。


## 4、Android进程（等级）

1. foreground process 前端进程

	前端进程就是目前显示在屏幕上和用户交互的进程

	比如说：
	1. 顶层可交互的activity（已执行onResume）；
	2. 有个Service，并绑定到跟用户正在交互的activity；
	3. 在Service里调用了startForground函数；
	4. 正在执行onReceive函数的BroadCastReceiver

2. visible process 可见进程

	没有任何前台组件,但是仍然能影响用户在屏幕上看到东西。
	比如：
	- 如果一个activity在一个对话框运行之后仍然是可视的；
	- 输入法的弹出时。

3. Service process 服务进程
	服务进程不会直接为用户所见
	比如在后台播放mp3或者从网上下载东西

4. background process 后台进程
	比如：Activity执行了onStop

5. empty process 空进程


## 5、数据持久化

### 5.1 SQLight：

	- SQLite是一个轻量级的数据库，支持基本的SQL语法
	- SQLiteDatabase的类，封装了一些操作数据库的api
		1. context.openOrCreateDatabase()方法创建SQLiteDatabase实例
		2. SQLiteDatabase实例调用insert()方法插入数据
		3. 调用query()方法查询数据
		4. 调用execSQL()方法执行SQL语句

### 5.2 SharedPreference：
	- 是一种轻量级的数据存储方式，采用简直对的方式来存储数据。
	- 其本质就是一个xml文件，一般位于/data/data/包名/shared_prefs/目录下。
	- 由于内存中存在sharedPreference文件的缓存，所以在多进程的环境下，系统对它的读写不可靠。因此不建议用在IPC中

### 5.3 ContentProvider：
	- Android系统中能实现不同应用间共享的一种数据存储方式。例如音频，视频，图片和通讯录，一般都可以采用此种方式进行存储
	- 每个Content Provider都会对外提供一个公共的URI，应用程序通过这个URI来对数据进行操作。
	- Content Provider天生支持跨进程访问，因此可以用于IPC




## 6、性能及优化

### apk包大小
1、减少不必要的jar包依赖
2、优先使用代码来设置UI效果
3、去除没用到的资源文件，压缩其他资源文件的大小，不用适配所有尺寸的设备
4、尽量重用代码，避免代码的冗余（公用的组件封装到一个子模块中供其他模块调用）
5、限制app支持的cpu架构的数目：在当前的Android 生态系统中，让你的app支持 armabi 和 x86 架构就够了
6、动态加载资源、so文件、功能模块等（Dynamic Loader）


### 方法数越界 multiDex方案

如果 miniSdkVersion 在 20或者更低，就需要在 build.gradle 脚本添加配置 multiDexEnabled true 启用 MultiDex，并添加 MultiDex 支持库。

- what：dex是Android平台上(Dalvik虚拟机)的**可执行文件**, 相当于Windows平台中的exe文件, 每个Apk安装包中都有dex文件。

- 单个dex文件所包含的**最大方法数是65536**，包含Android Framwork、依赖的jar包，以及应用本身的所有方法。

- 解决方法数越界：
	1. 删除无用的代码和第三方库
	2. 采用插件化机制，动态加载dex。这是一个重量级的方案
	3. multiDex方案——可以从apk中加载多个dex文件

- 基本使用：
	1. 配置Gradle，添加  multiDexEnabled true
	2. 添加multiDex依赖
	3. 在Application中添加  MultiDex.install(this) 代码


### 其他
#### 目标：
	- 快：流畅
	- 稳：稳定
	- 省：省电、省流量
	- 小：安装包小

#### 优化方案：
- 布局优化：
	- 减少View树的层数
	- 合理使用优先使用FrameLayout和LinerLayout，减少使用RelativeLayout
	- 布局复用，使用<include>标签


- OOM优化

- ANR优化

- ListView（GridView）优化
	- 使用viewholder，进行view复用
	- 不要在getview()中进行耗时操作

- Bitmap优化
	- 图片压缩
	- 缓存（核心）：内存缓存和磁盘缓存、LRU算法

- apk体积优化


----------



```<uses-library>``` 含义与使用

