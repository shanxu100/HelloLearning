# 线程相关
[《Android HandlerThread 总结使用》](https://www.cnblogs.com/zhaoyanjun/p/6062880.html)  
[《Android HandlerThread 完全解析》](https://blog.csdn.net/lmj623565791/article/details/47079737/)  
[《Handler一定要在主线程实例化吗?new Handler()和new Handler(Looper.getMainLooper())的区别》](https://blog.csdn.net/thanklife/article/details/17006865)  
[《15个顶级Java多线程面试题及回答》](http://ifeve.com/15-java-faq/)  
[《5000字、24张图带你彻底理解Java中的21种锁》](https://mp.weixin.qq.com/s/o6MWPz-asKswWM5cyGeDYg)  

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



### 3.2 `IntentService`

- 原理：`IntentService`是一个**抽象类**，封装了`HandlerThread`和`Handler`，负责处理耗时的任务。任务执行完毕后会自行停止。在`onCreate()`方法中开启了一个`HandlerThread`线程，之后通过`HandlerThread`的Looper初始化了一个Handler，负责处理耗时操作。通过`startService()`方法启动，在handler中调用**抽象方法`onHandleIntent()`**，该方法执行完成后自动调用`stopself()`方法停止

- override `onHandleIntent()` 方法

- 优点：一方面不需要自己去创建线程，另一方面不需要考虑在什么时候关闭该Service



## 4 `HandlerThread`详解

`HandlerThread`本质上是一个继承了**Thread的线程类**。
通过创建HandlerThread**获取looper对象**，传递给Handler对象，执行异步任务。在HandlerThread中通过`Looper.prepare()`来创建消息队列，并通过`Looper.loop()`来开启消息循环。创建HandlerThread后必须先调用`start()`方法，才能调用`getLooper()`获取Looper对象。

**HandlerThread封装了Looper对象，使我们不用关心Looper的开启和释放的细节问题**。如果不用HandlerThread的话，需要手动Thread子类并且去调用`Looper.prepare()`和`Looper.loop()`这些方法。

```java
public class HandlerThread extends Thread {

	// 略...
    
    protected void onLooperPrepared() {
    }

    @Override
    public void run() {
        mTid = Process.myTid();
		Looper.prepare();

		// 关键点：1
        synchronized (this) {
            mLooper = Looper.myLooper();
            notifyAll();
        }
        Process.setThreadPriority(mPriority);
        onLooperPrepared();
        Looper.loop();
        mTid = -1;
    }
    
    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }
        
        // If the thread has been started, wait until the looper has been created.
        synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
    }

    /**
     * @hide
     */
    @NonNull
    public Handler getThreadHandler() {
		// 略
    }

    public boolean quit() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quit();
            return true;
        }
        return false;
    }


    public boolean quitSafely() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quitSafely();
            return true;
        }
        return false;
    }

    public int getThreadId() {
        return mTid;
    }
}

```

### 4.1 为什么在`run()`方法里面当mLooper创建完成后有个`notifyAll()`，`getLooper()`中有个`wait()`?
因为使用HandlerThread时，`run()`方法在新创建中的Thread中执行的，此处暂称为T1；
在调用HandlerThread的`getLooper()`方法时，是在另一个线程中执行（如主线程），此处暂称为T2；
面对上述T1和T2的同步问题，为了保证T2线程中调用`getLooper()`方法需要保证Looper已经创建完成，所以增加了`synchronized`、`notifyAll()`、`wait()`。


