# 线程相关

## 1、Linux线程基础
- 线程与进程的区别
- 线程同步
- Linux线程通讯方式

## 2、ANR
	- what
		- Activity 10s无响应，BroadcastReceiver 10s无响应，未响应输入事件5s
		- /data/anr/traces.txt 文件记录了ANR的信息
	- why
	- how

### 2.1 在主线程执行耗时操作一定会产生ANR么？（主线程的Looper是死循环，为什么不会造成ANR）
对于子线程，执行完任务后，生命周期便会终止并退出。而对于主线程，我们更希望的是能够一直存活。所以Android主线程是一个Looper死循环。   
“在主线程做了耗时操作”并不一定会引起ANR的发生，而是**用户点击或者触摸界面（或其他用户交互）时，UI线程在执行耗时操作，没有及时响应用户操作**才会造成ANR的。   

### 2.1 BlockCanary检测ANR原因
> https://www.jianshu.com/p/e58992439793

主线程里会创建Looper对象，不断轮询MessageQueue，然后调用dispatchMessage，交给Handler处理msg。所以发生ANR，就一定是在然后调用dispatchMessage()方法中执行了耗时操作。所以统计dispatchMessage方法执行的时间，如果超出阈值，表示发生卡顿，则dump出各种信息，提供开发者分析性能瓶颈。


## 3、耗时任务或者线程间通讯
- AsyncTask
	- 本质上是对 ThreadPool 和 Handler 的一个封装
	- 默认是串行的执行任务，可以调用executeOnExecutors()方法并行执行任务
- Handler
- IntentService


### 3.1 Handler机制：
Handler + MessageQueue + Looper



### 3.2 IntentService

- 原理：IntentService是一个**抽象类**，封装了HandlerThread和Handler，负责处理耗时的任务。任务执行完毕后会自行停止。在onCreate()方法中开启了一个HandlerThread线程，之后通过HandlerThread的Looper初始化了一个Handler，负责处理耗时操作。通过startService()方法启动，在handler中调用**抽象方法onHandleIntent()**，该方法执行完成后自动调用stopself()方法停止

- override onHandleIntent() 方法

- 优点：一方面不需要自己去创建线程，另一方面不需要考虑在什么时候关闭该Service








