# Handler机制
**参考：** 
- [《一文读懂 Handler 机制全家桶》](https://juejin.cn/post/6901682664617705485)  
- [《换个姿势，带着问题看Handler》](https://juejin.cn/post/6844904150140977165)  
- [《MessageQueue原理分析》](https://www.jianshu.com/p/f00c3fa9e6c0)  
- [《Android Handler Looper机制》](https://blog.dreamtobe.cn/2016/03/11/android_handler_looper/)

**简介：**
Android是一个事件驱动的模型，只有源源不断的事件产生与处理才能推动应用的进行。
Handler是Android提供的一套线程通信的方式，是一种 **“单线程消息队列机制”** 。主要由 ```Handler、MessageQueue、Looper``` 组成：每一个消息通过Handler的post或send接口进入MessageQueue。Looper本质是一个死循环，不断的从MessageQueue中取消息处理。当没有新的消息（或未到唤醒时间）时，```messageQueue.next()```接口就会阻塞。有消息处理时，则会从MessageQueue中取出来，交给Handler处理


## 1、 Looper

### 1.1 ```Looper.prepare()```
初始化当前线程的```Looper```，然后**保存到ThreadLocal中**。一个线程只能调用一次```Looper.prepare()```接口
核心代码如下（伪代码）：
```java
private static void prepare(boolean quitAllowed) {
    if (sThreadLocal.get() != null) {
        throw new RuntimeException("Only one Looper may be created per thread");
    }
    sThreadLocal.set(new Looper(quitAllowed));
}
```

### 1.2 ```Looper.loop()```
```Looper.loop()``` 开启无限循环。安卓是基于消息处理机制的，核心代码如下（伪代码）：

```java
for (;;) {
	Message msg = queue.next(); // might block
	if (msg == null) {
		// No message indicates that the message queue is quitting.
		return;
	}
	msg.target.dispatchMessage(msg);
	msg.recycleUnchecked();
}
```
```queue.next()```：如果MessageQueue没有消息，该```next()```方法会阻塞；否则返回需要处理的msg。**如果next()方法返回null，表示结束循环，退出loop。**

```queue.next()```方法执行时会调用到JNI的Native方法的```nativePollOnce()```的方法。当MessageQueue为空时，上述方法会阻塞，Looper循环被阻塞，即线程被阻塞，此时会释放CPU资源，等待事件（比如输入事件）的唤醒。这里涉及Linux pipe/epoll机制。

### 1.3 消息分发


### 1.4 其它接口
- ```Looper.myLooper()```：获取当前线程的looper
- ```Looper.getMainLooper()```：获取主线程的looper
- ```looper.quit()```: 停止当前线程的looper，此方法为实例方法，非静态方法

## 2、MessageQueue

### 2.1 Message

Message有以下几个关键字段：
- ```next```：指向下一个Message
- ```when```：表示触发时间
- ```target```：表示处理该Message的Handler对象

使用```Message.obtain()```或```handler.obtainMessage()```来获取一个消息。在使用中**不推荐用new的方式直接创建一个Message**。
这两种方法本质是一样的，只不过```handler.obtainMessage()```中设置的```target```已经是指定的handler了
```java
// 推荐：获取一个Message对象
Message msg = Message.obtain();

// 推荐：获取一个Message对象
handler.obtainMessage();

// 不推荐：获取一个Message对象
Message msg = new Message();
```

### 2.2 原理
```MessageQueue```本质上是一个**单链表List**，不是```Queue```。元素就是```Message```。

```enqueueMessage()```方法是将消息插入一条队列，```next()```方法是一个无限循环的方法：如果有消息，则取出，如果没有，就阻塞。
**单链表是有顺序的，```when```越小，就越靠前**

- 加入消息（入队）： ```enqueueMessage()```
	加入链表的时候按```when```字段从小到大排序，然后判断是否需要唤醒：如果是第一个那么根据mBlocked判断是否需要唤醒线程，如果不是第一个一般情况下不需要唤醒

- 获取消息：```next()```  **未完成**
	```next()```方法是个死循环，先判断msg是否已经到时间：若到时间了，则return该msg，结束循环；否则计算等待时间，再下次循环时交给native层方法阻塞
	如果没有符合条件的消息，会处理一些不紧急的任务（IdleHandler），再次进入下次循环
	

## 3、 Handler

### 3.1 常用方法
1. 发送数据接口
```java
// 发送Message
handler.sendMessage(msg);
handler.sendEmptyMessageDelayed(msg, delay);

// 发送runnable
mainHandler.post(runnable);
mainHandler.postDelayed(runnable, token, delay);
mainHandler.postAtTime(runnable, token, uptimeMillis);

```
- post()系列接口会将runnable对象封装成一个Message。
- 上述接口，**最终都会进去handler的```enqueueMessage()```方法，然后将msg的```target```指定为自己这个handler，然后调用 ```queue.enqueueMessage()``` “入队”**

2. 指定Callback回调
handler对象可以指定一个```callback```对象，用于处理Message。
```java
Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }
});
```
3. Override复写```handleMessage()```方法
   可以自定义个类继承Handler，然后override复写handleMessage()方法，用于处理Message。
```java
private static final class MyHandler extends Handler{
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
    }
}
```

### 3.2 消息分法
Looper通过MessageQueue的next方法获取到需要处理的Message。然后通过Message的target字段找到处理该消息的Handler。接着，调用handler对象的 ```dispatchMessage()```方法可以走到开发者定义的消息处理逻辑。  
Handler可以通过三个地方指定消息处理的逻辑，分别是：
1. post()系列接口指定的```Runnable```对象
2. 实例化Handler时指定的```Callback```对象
3. 集成Handler并Override的```handleMessage()```方法
```java
public void dispatchMessage(@NonNull Message msg) {
    if (msg.callback != null) {
        handleCallback(msg);
    } else {
        if (mCallback != null) {
            if (mCallback.handleMessage(msg)) {
                return;
            }
        }
        handleMessage(msg);
    }
}
```

从上述代码可以看到，这三个地方是有先后顺序的，即：**```1 > 2 > 3```**



## 4 HandlerThread

本质上是一个继承了**Thread的线程类**。
通过创建HandlerThread**获取looper对象**，传递给Handler对象，执行异步任务。在HandlerThread中通过**Looper.prepare()**来创建消息队列，并通过**Looper.loop()**来开启消息循环。创建HandlerThread后必须先调用start()方法，才能调用getLooper()获取Looper对象。

**HandlerThread封装了Looper对象，使我们不用关心Looper的开启和释放的细节问题**。如果不用HandlerThread的话，需要手动Thread子类并且去调用Looper.prepare()和Looper.loop()这些方法。

#### 3.1.4 FAQ：

1. 主线程Handler的实现细节？
	- Looper.loop()方法开启了一个死循环，不断调用MessageQueue的next()方法，即```queue.next()```
	- handler发送消息：post系列方法 --> send系列方法 --> 向MessageQueue中插入数据。
	- handler处理消息：Looper在loop()方法中通过```queue.next()```取出message，然后**交给与之关联的Handler处理**,即调用```msg.target.dispatchMessage()```（target就是handler对像）。
	- dispatchMessage：如果msg的callback不为null，就处理。否则，handler的callback不为null，就让其处理。否则调用handleMessage()方法。
	- Handler的子类必须override handleMessage()方法，负责处理message

2. 





## FAQ

### Looper.prepare()做了什么事情

### 为什么推荐使用```Message.obtain()```或```handler.obtainMessage()``` 方法创建对象，而不直接new一个对象

### 如何知道将msg交给哪个Handler处理？
调用handler的post和postDelay等方法的时候，最终都会走到handler的```enqueueMessage```方法。在这个方法中，msg的target将被赋值为对应的handler，然后再调用 ```queue.enqueueMessage``` “入队”。这样msg就与handler关联了起来

### Android UI更新机制(GUI) 为何设计成了单线程的？
Android 需要在主线程才能更新UI。 如果多个线程都能更新UI，势必要「加锁」。还不如采用「单线程消息队列机制」

### 一个Looper关联了多个handler，会怎样？

### handler机制会不会引起内存泄漏
原因：
由于Handler有可能会被Looper#mQueue#mMessages#target引用，而很有可能由于消息还未到达处理的时刻，导致引用会被长期持有。  
如果Handler是一个非静态内部类，就会持有一个外部类实例的引用，进而导致外部类很有可能出现无法及时gc的问题。

解决:
直接静态化内部类，这样内部类Handler就不再持有外部类实例的引用。  
再在Handler的构造函数中以弱引用(当所指实例不存在强引用与软引用后，GC时会自动回弱引用指向的实例)传入外部类供使用即可。

### Handler的postDelay操作是如何实现的(腾讯面试)


### runOnUiThread()原理(腾讯面试)
本质上利用的Handler。在dispatchMessage()方法里面，先判断msg的callback是否为null。这里的callback就是runOnUiThread()的参数runnable对象。如果不为null，则调用run()。



