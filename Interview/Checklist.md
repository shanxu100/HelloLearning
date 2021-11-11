# Interview Checklist

## 1、Java

### 1.1 Basic

==、equle、hashCode  
    三者区别  
    为什么override equals()方法，也一定要override hashCode()方法  


Serializable与Parcelable

final、finally与finalize

String转换成Integer的方式与原理

OOP：封装继承多态；重写和重载，接口和抽象类的区别

深拷贝、浅拷贝

泛型，```<? extends T>```和```<? super T>```的区别

ArrayList与LinkedList：数据结构，插入、查找和删除的效率

### 1.2 JVM

1. JVM内存模型（运行时数据区域），并且哪些是线程私有的

2. Java内存模型

3. 堆和栈的区别，数据段是什么

4. 如何判断对象需要回收，以及有哪些回收算法

5. 类加载的过程

6. 类加载的模型（双亲委派模型，使用组合而非继承）

例题：
- ```public static int a = 1;```被赋值几次

- ```public static final int a = 1;```被赋值几次

7. *class文件的格式

### 1.3 Thread

进程与线程的区别

线程池的种类：```Executors```类中的常用接口,以及各个线程池中线程的数量。

```wait()```和```sleep()```的区别

```wait()\notify()\notifyAll()```的理解与使用

如何保证线程安全：原子性、可见性和有序性——通过加锁保证

开启线程的方式、run()与start()的区别、如何优雅的结束线程

线程和进程的数据结构

线程池如何复用线程

```volatile```的作用和原理
例题：
- ```volatile```修饰boolean变量时，能否保证原子性
    答：volatile关键字能禁止指令重排序，因此volatile能保证有序性。volatile关键字禁止指令重排序有两个含义

- 为什么自增操作不是原子性操作？
    答：因为自增操作分三个步骤：读值，+1，写内存。只要有多线程就有可能阻塞。

```synchronized```关键字修饰不同对象或方法的作用（类锁与对象锁）

如何控制某个方法允许并发访问的线程数（Semaphore）？

```ThreadLocal```原理与内存泄漏

*Java中锁的原理？？
https://mp.weixin.qq.com/s/YblzdWmzJHAv-VDLzOIOnA

### 1.4 List、Set、Map

unsafe包：
ConcurrentHashMap
- 实现原理：put、初始容量、扩容
- 会出现modifyCountException么

### 1.5 Reflect 反射

反射原理

反射的常用方法

提高反射的效率

### 1.6 Annotation 注解

### 1.7 




## 2、Android

### 2.1 四大组件
Activity
- 生命周期、启动模式
- App和Activity的启动流程

Service
- 生命周期
- start()与bind()

Broadcast

ContentProvider



例题：
- 多个Activity bind()同一个Service，然后一个Activity destroy(或unbindservice)，此时该Service的状态


### 2.2 Handler机制

#### 2.2.1 Handler

#### 2.2.2 Looper
- looper.prepare干了啥。

#### 2.2.3 MessageQueue：**重中之重**
数据结构
入队```enqueueMessage()```（排序）
出队```next()```（有则出队，无则阻塞）


### 2.3 OOM、ANR、Crash

内存泄漏与内存溢出

Android中常见的内存泄漏对象及其原因

ANR的原因
在主线程执行耗时操作一定会产生ANR么

Exception

Java Crash与捕获

Native Crash与捕获原理

C/C++编译的产物：.o .a .so 


### 2.4 IPC

Linux有哪些IPC方式

Android有哪些IPC方式

Android为什么要设计Binder机制

Binder的基本原理

Aidl的使用

SharedPreference是否是线程安全的，为什么不能用于IPC？


### 2.5 构建、ADB与Gradle插件


### 2.6 Framework
Android开机流程

Launcher

App启动流程

Activity启动流程


### 2.7 Apk签名
v1、v2、v3的区别


### 2.8 内存
查看内存占用

如何判断发生了内存泄漏

Android常见的内存泄漏案例及其原因

检测内存泄漏的工具及其原理

*内存优化

### 2.9 动态加载

dex（字节码）文件的加载过程

PathClassLoader 和 DexClassLoader


### 2.6 绘制流程、事件分发、滑动冲突

Android有哪几种绘画

事件分发原理解析

RecyclerView原理

### 2.7 缓存机制、性能优化、体积优化

体积优化：APK瘦身方案


### 2.8 MVC、MVP、MVVM

### 2.9 Android AAC组件*



## 3、Design Pattern

### 3.1 工厂模式

### 3.2 策略模式

### 3.3 责任链模式

### 3.4 装饰者模式

### 3.6 观察者模式、单例模式、状态模式

例题：
- 装饰着模式和代理模式的区别

- 每个设计模式需要举个例子

- 单例模式的优点和缺点

- okhttp用到的设计模式


## 4、Network

TCP/IP五层网络模型、OSI七层网络模型

### 4.1 TCP三次握手

### 4.2 TCP四次挥手

### 4.3 流量控制

### 4.4 拥塞控制

### 4.5 描述一次完整的网络请求

### 4.6 HTTPS的原理和流程
HTTP常见错误码

HTTPS和HTTP的区别

HTTPS的建立过程

HTTPS不安全的地方（一定可靠么）？如何防范中间人攻击

对称加密算法：DES、3DES、ARS

非对称加密算法：RSA

对称和非对称加密算法的基本原理

秘钥交换

数字签名

CIA


### 4.7 WebSocket

与HTTP一样属于应用层协议，建立在TCP基础上全双工通信协议。

建立过程（升级过程）



## 5、DataStruts

### 5.1 哈弗曼树、平和二叉树、红黑树


### 5.2 双向链表的插入与删除

### 5.3 手撕排序算法
冒泡、快排

### 5.4 手撕单链表翻转

### 5.5 Dijkstra算法、Prime算法


## 6、 OS

### 6.1 64位OS和32位的OS的区别


## 7、Case

### 7.1 Computer
- Linux IO模型

### 7.2 Android动态加载
字节码、资源、so文件

### 7.3 设计题
- 设计一个Buffer
- 设计一个埋点上报库，包括哪些模块，哪些接口

### 7.4 智力题
xxxx只老鼠，xxx个瓶子，1瓶毒药……




