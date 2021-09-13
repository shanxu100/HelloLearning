# Android-第三方库
[TOC]
## 1、响应式编程 RxJava/RxAndroid

基于观察者模式，可以方便地以流的方式处理异步事件

### 1、 创建：
Observable.create/just/from

### 2、 Schedulers线程调度

- 在不指定线程的情况下， RxJava 遵循的是线程不变的原则
- subscribeOn():
	- 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
	- subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的。
- observeOn():
	- Subscriber 所运行在的线程，或者叫做事件消费的线程。
	-  指定的是它之后的操作所在的线程，以此实现线程的多次调用
- 举例：
```java
AndroidSchedulers.mainThread();
Schedulers.single();
Schedulers.newThread();
Schedulers.computation();
Schedulers.io();
```

### 3、 变换 操作符
1. map()：将发射的每一项数据都用一个函数进行变换
2. flapMap()：

### 4、 背压
在异步场景下，被观察者的发送速度远远大于观察者的处理速度。

- Observable不支持背压

- Flowable支持背压：
	- 背压策略：MISSING、ERROR、BUFFER、DROP、LATEST

## 2、Retrofit
是一个网络请求框架，底层依赖OkHttp

- 首先，Retrofit将Http请求封装为Java接口；
- 然后，网络请求交给OkHttp处理；
- 最后，OkHttp将返回的结果交给Retrofit解析

支持两种模式：callback、RxJava/RxAndroid



## 3、LeakCanary的原理

ReferenceQueue（引用队列）简介
当gc（垃圾回收线程）准备回收一个对象时，如果发现它还仅有软引用(或弱引用，或虚引用)指向它，就会在回收该对象之前，把这个软引用（或弱引用，或虚引用）加入到与之关联的引用队列（ReferenceQueue）中。如果一个软引用（或弱引用，或虚引用）对象本身在引用队列中，就说明**该引用对象所指向的对象**被回收了。



## 4、设计一个图片加载类
- 图片同步、异步加载方式
- 图片压缩（使用BitmapFactory） —— **降低OOM的风险**
- 缓存 —— 内存缓存LruCache、磁盘缓存DiskLruCache。**两级缓存降低了网络访问次数，减少了流量消耗**
- 网络拉取 —— 从网络加载图片。
- View复用
- 调用过程：从内存缓存中读取 —— 从磁盘缓存中读取 —— 从网络中拉取

## 5、画出一个项目中网络请求的流程图


## 6、第三方库源码解析：
OkHttp


## 7、说说XML、JSON、GSON有什么样的联系
XML全称叫做可扩展标记语言，它的结构相对简单，数据共享比较方便。但是对于一些比较复杂的数据，XML文件格式复杂，解析的代价大。
JSON的数据格式比较简单，易于读写。但是目前还没有XML应用广泛。
GSON的Google的一个开源库。这个开源库可以很方便地将JSON数组转换为对象，这在开发中简化了将JSON的字段转换为属性的步骤。

-------------

## 8、APM

### Android性能信息
- CPU信息：**/proc/cpuinfo**
- Memory 信息：**/proc/meminfo**

### 异常捕获：
UncaughtExceptionHandler：
android全局异常捕获器——在Application中调用**Thread.setDefaultUncaughtExceptionHandler
(uncaughtExceptionHandler)**，定制自己的错误日志系统。比如在发生Exception时，记录exception的堆栈信息。

### 监控Activity生命周期

在application中，调用**registerActivityLifecycleCallbacks()**，通过实现了ActivityLifecycleCallbacks接口的实例，完成对Activity各个生命周期信息的采集。比如哪个Activity在什么时间处于onCreate、onStop等等，进而统计出Activity的使用时间和使用次数。

### 对于Fragment的信息采集
SDK提供不同接口，分别对应Fragment的各个生命周期，进而采集信息。使用时，需要用户在Fragment的生命周期中的各个环节中，调用对应的接口。
会不会使用麻烦？会的，解决方法就是：在SDK中封装一个Fragment的子类，在这个子类中按照上述方法采集信息。用户在使用SDK过程中，可以直接继承使用这个子类，而不是继承使用Fragment。

### 对HTTP接口的监测
采用插装的办法：SDK提供两个接口，用户在发起HTTP请求时，调用第一个接口，可以记录下url和时间。在结束HTTP时，调用第二个接口，记录下url、时间和返回码。这两组记录就完成了对HTTP接口的数据采集？
会不会麻烦？会的，但是这种方法很通用，其他商用的SDK中也是这种方法。或者使用Gradle插件，在编译的时候，将上述代码自动插入项目中。

### 错误信息上报机制：
方案一：发现错误后立即上报。优点：实时性好；缺点：不能保证每一条信息都能上报成功。
方案二：发生错误后，记录在本地，当第二次启动App的时候，上报上一次的信息。


### 接口加密、token原理

-------


