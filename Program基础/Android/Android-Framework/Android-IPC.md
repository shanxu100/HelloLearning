# IPC：跨进程通信

## 1、基础
- 开启多进程的方式：给四大组件在Menifest文件中，添加process属性，指定进程名称
- Android为每个进程分配一个**独立的虚拟机实例，即有不同的Application和地址空间**。一个App对应一个或多个进程。
- 不同进程访问同一个类的对象会有不同的副本。因此静态成员和单例模式失效、线程同步失效、sharedPreference可靠性降低。

### 1.1 Linux中IPC的方式
- 管道：内核创建虚拟文件，调用文件读写函数来进行数据通信。匿名管道只能单向，具名管道并发问题；命名管道
- 消息队列：消息队列是消息的链表，进程可通过消息类型来访问和写入消息，由内核负责消息发送和接受之间的同步。
- 信号量：
- 共享内存：通过命令在内存中单独开辟的一段内存空间，可通过映射到被多个进程使用。无同步控制，通常与信号量配合使用，使用复杂。
- socket：基于TCP/IP协议的网络通信。开销大，效率低，主要用于不同机器或跨网络的通信


### 1.2 序列化
对象是不能直接跨进程传输的。对象的跨进程传输，其本质是序列化和反序列化的过程。详见[《Android-序列化》](../Android-序列化.md)
- Serializable接口：Java的序列化接口，使用简单，但开销大。
- Parcelable接口：是Android的序列化方式，使用复杂，但效率高。设计**让其支持跨进程通信的功能**，因此它不具备类似Serializable的版本功能，所以Parcelable 不适合永久存储。

## 2、Binder原理
Binder是Android特有的一种跨进程通讯方式
### 2.1 基本原理：
Android特有的IPC、客户端-服务器C/S的模式、

四个角色：```Client```、```Server```、```ServiceManager```、```BinderDriver```

调用过程：
1. ```Server```向```ServiceManager```注册
2. ```Client```通过```ServiceManager```获取```Server```的引用（或者代理）
3. ```Client```向代理对象发起请求，该请求通过BinderDriver发送给```Server```处理
4. ```Server```通过```BinderDriver```返回处理结果

- 注意：客户端调用服务端的方法，被调用的方法**运行在服务端的Binder线程池**中，此时**客户端被挂起**。因此此时需要避免```ANR```。（```AIDL```和```Messager```同理）

- C/S概念:
    首先要理清一个概念：client拥有自己Binder的实体，以及Server的Binder的引用；Server拥有自己Binder的实体，以及Client的Binder的引用。
    也就是说，我们在建立了C/S通路后，无需考虑谁是Client谁是Server，只要理清谁是发送方谁是接收方，就能知道Binder的实体和引用在哪边。

### 2.2 Binder连接池

在一个应用有多个使用AIDL的场景，无需为每一个AIDL创建自己的Service。而是使用一个Service,创建并返回一个Binder连接池的Binder对象。Activity在使用AIDL的时候，可以通过该Binder连接池对象，获取不同的Binder对象（类似于工厂模式）

### 2.3 为什么使用Binder，而不使用Linux已有的IPC？
腾讯的技术二面。小心该问题可能引出来的坑：比如比较一下Linux的多种IPC和Binder异同。
- 性能
 Binder 只需要一次数据拷贝，性能上仅次于共享内存。管道、消息队列和Socket需要两次拷贝。（消息队列和管道采用存储-转发方式，即数据先从发送方缓存区拷贝到内核开辟的缓存区中，然后再从内核缓存区拷贝到接收方缓存区，至少有两次拷贝过程。）
- 稳定性
 共享内存没有同步控制功能，往往需要配合信号量使用。所以Binder的易用性更好。
- 安全性
 传统的进程通信方式对于通信双方的身份并没有做出严格的验证，只有在上层协议上进行架设；而Binder机制从协议本身就支持对通信双方做身份校检(UID)，因而大大提升了安全性。


## 3、Android的IPC机制

**5.4 Android  应用程序之间  是通过哪些方式共享数据的？**
```Bundle(Intent)、文件共享(File)，Sqlite，ContentProvider，BroadCastReceiver，Socket，AIDL，Messager```，同个Application内部的话还可以通过```静态变量```共享数据。

- 四大组件间，把数据封装到```Bundle```。在一个进程中开启另一个进程的Activity或者Service，就可以通过```Intent```把```Bundle```传递过去。其中，封装在```Bundle```中的数据需要能够被序列化

- 使用文件共享方式，多进程读写一个相同的文件，获取文件内容进行交互。

- 使用```ContentProvider```，常用于应用间的共享数据，比如系统的相册，音乐等，我们也可以通过```ContentProvider```访问到

- 使用```Socket```传输数据。服务端（比如一个进程中运行了一个Service）创建一个```ServerSocket```对象，监听本地的端口；客户端（比如另一个进程中运行的Activity）通过Socket连接本地的那个接口。经过TCP的三次握手后，建立连接。接着可以发送数据。使用```Socket```不仅可以实现进程间通信，也可以实现设备间通信。

### 3.1 AIDL
- 使用流程：以Activity（进程1）和Service（进程2）通信为例
	- 创建AIDL接口，Build一下，产生相关代码
	- 创建IBinder实例，即实例化```xxx.Stub()```抽象内部类，override抽象方法
	- 创建Service，在onBind()中，把上述IBinder实例返回
	- 在Activity中调用bindService启动Service，然后在ServiceConnection中的onServiceConnected方法回调中获得该IBinder实例:```xxx.Stub.asInterface(iBinder)```。
	- Activity调用该实例的方法，实现通信

- 同步异步
	- AIDL的调用过程是同步的。client调用IBinder实例的方法（即定义的接口）时，client会挂起。因此不要通过AIDL执行耗时操作。
	- 在子线程中执行AIDL的耗时操作，然后再更新UI，实现异步操作。

- AIDL Service通信方法：
https://www.cnblogs.com/xqz0618/p/aidl_service.html

### 3.2 Messager

- 一种轻量级的跨进程通讯方案，底层使用AIDL实现。

- 是一种**串行的通信**，即服务端需要一个一个处理消息。因此，在大量并发请求的情况下，用Messager就不太合适。

- 使用流程：以Activity（进程1）和Service（进程2）通信为例
	1. 在Service中new一个Messenger（这个Messenger需要指定Handler）
	2. 然后在onBind函数中，返回messenger的Binder对象（messenger.getBinder()）
	3. 在Activity中，通过bindService启动service，通过ServiceConnection获取到Binder对象。
	4. 通过这个Binder对象实例化一个Messenger，然后messenger.send(message)进行通信

----------






