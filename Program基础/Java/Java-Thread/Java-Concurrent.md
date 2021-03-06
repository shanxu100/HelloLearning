# JDK中的concurrent包的应用

## 1、java.util.concurrent包中的常见类
- atomic
    - AtomicReference
    - AtomicBoolean 
- locks
    - ReentrantLock

- BlockingQueue\BlockingDeque (dequeue双端队列，"double ended queue")
- LinkedBlockingQueue\ArrayBlockingQueue：阻塞队列。应用：生产者消费者
- ConcurrentHashMap
- ConcurrentLinkedQueue
- ConcurrentLinkedDeque
- CopyOnWriteArrayList
- CopyOnWriteArraySet

- Exchanger
- CyclicBarrier
- CountDownLatch
- Phaser
- Semaphore

- Executors
- ScheduledExecutorService
- ScheduledThreadPoolExecutor
- ThreadPoolExecutor
- ThreadFactory
- FutureTask
- Callable



-------------
## 2、队列

### 2.1 BlockingQueue的原理（有兴趣可以深入）
[《LinkedBlockingQueue与ConcurrentLinkedQueue的区别》](https://blog.csdn.net/lzxlfly/article/details/86710382)

### 2.2 阻塞队列
- 概念：从队列中取元素时，若队列为空，则会阻塞，直到有新的元素入队（同步生产者与消费者）。
- 使用场景：`LinkedBlockingQueue`、`ArrayBlockingQueue`、`PriorityBlockingQueue`是线程安全的阻塞队列，是生产者消费者模式的首选。
- 性能约束：如果消费者频繁的被阻塞，会导致用户态与内核态切换频繁，消耗系统资源。

### 2.2.1 常用的方法区差异
- 插入

| method | 队列未满 | 队列已满 |
| -- | -- | -- |
| `boolean add(E e)` | 返回true | 抛出 `java.lang.IllegalStateException: Queue full` |
| `boolean offer(E e)`| 返回true | 返回false |
| `boolean offer(E e,long timeout,TimeUnit unit)` | 返回true | 先阻塞，超时后返回false |
| `void put(E e)` | 返回true | 阻塞当前线程 |


- 移除（获取）

| method | 队列非空 | 队列为空 |
| -- | -- | -- |
| `E remove()` | 移除并返回队首元素 | 抛出`java.util.NoSuchElementException` |
| `E poll()` | 移除并返回队首元素 | null |
| `E poll(long timeout,TimeUnit unit)` | 移除并返回队首元素 | 先阻塞，超时后返回null 
| `E take()` | 移除并返回队首元素 | 阻塞当前线程 |

- 检查

| method | 队列有数据 | 队列为空 |
| -- | -- | -- | 
| `E element()` | 队首元素 | 抛出`java.util.NoSuchElementException`
| `E peek()` | 队首元素 | null


### 2.3 非阻塞队列
- 概念：从队列中取元素时，若队列为空，则会立即返回空，不会阻塞。
- 场景：`ConcurrentLinkedQueue`：是线程安全的队列，更适用于单线程插入，多线程取出，即**单个生产者与多个消费者**。



-----------
## 3、多线程协同工具

### 3.1 `CountdownLatch`计数器  
`CountDownLatch`属于concurrent包，就是一个倒计数器。
我们把初始计数值设置为3，当 D 运行时，先调用 `countDownLatch.await(`) 检查计数器值是否为 0，若不为 0 则保持等待状态；当A B C 各自运行完后都会利用`countDownLatch.countDown()`，将倒计数器减 1，当三个都运行完后，计数器被减至 0；此时立即触发唤醒D，结束 `await()` ，继续向下执行。
- 创建一个计数器，设置初始值，`CountdownLatch countDownLatch = new CountDownLatch(2);`
- 在 等待线程 里调用 `countDownLatch.await()` 方法，该线程进入等待状态，直到计数值变成 0；
- 在 其他线程 里，调用 `countDownLatch.countDown()` 方法，该方法会将计数值减小 1；
- 当 其他线程 的 `countDown()` 方法把计数值变成 0 时，等待线程 里的 `countDownLatch.await()` 立即退出，继续执行下面的代码。

### 3.2 `CyclicBarrier`
实现线程间互相等待这种需求，我们可以利用 `CyclicBarrier` 数据结构，它的基本用法是：
- 先创建一个公共 `CyclicBarrier` 对象，设置 同时等待 的线程数，`CyclicBarrier cyclicBarrier = new CyclicBarrier(3);`
- 这些线程同时开始自己做准备，自身准备完毕后，需要等待别人准备完毕，这时调用 `cyclicBarrier.await();` ，**本线程开始阻塞**，即可开始等待别人；
- 当指定的 同时等待 的线程数都调用了 `cyclicBarrier.await();`时，意味着这些线程都准备完毕好，然后**这些线程才 同时继续执行**。
- `CyclicBarrier`构造函数中可以指定 `barrierAction`。当所有线程都ready后，会执行该`barrierAction`（一般用户合并多线程计算结果）。

### 3.3 Phaser
支持任务在多个点进行同步。支持动态调整注册任务的数量
`phaser.arriveAndAwaitAdvance();`

### 3.4 Exchanger
两个线程之间进行数据交换。A线程调用了`exchange()`方法后进入阻塞状态，直到B线程调用`exchange()`方法。然后双方通过线程安全的方式进行数据交换，之后AB继续运行

### 3.5 如何形象的比如上述4个工具的差别
- `CountdownLatch`就像一个倒计时器，阻塞一个线程。待满足条件后，该线程再继续执行
- `CyclicBarrier`就像一个发令枪，阻塞一组线程。待条件满足后，这一组线程再继续执行。（或者比喻成“人满发车”）


--------------

## 4、原子操作

## AtomicReference

>https://www.jianshu.com/p/5521ae322743

## AtomicInteger & AtomicIntegerArray
在使用Integer的时候，必须加上synchronized保证不会出现并发线程同时访问的情况，
在AtomicInteger中却不用加上synchronized，在**这里AtomicInteger是提供原子操作的**
```java
AtomicReference<Integer> reference = new AtomicReference<>();
reference.set(5);
int refR0 = reference.get();
// 原始值与expect比较，如果相等，则将原始值设置为update
boolean refR1 = reference.compareAndSet(5, 7);
int refR2 = reference.get();
System.out.println(refR0 + "  " + refR1 + "  " + refR2);

AtomicIntegerArray array = new AtomicIntegerArray(10);
array.set(1, 10);
int r0 = array.get(1);
// 返回下标为i的值，然后将取到的值加上delta再更新到array中
int r1 = array.getAndAdd(1, 5);
int r2 = array.get(1);
System.out.println(r0 + " " + r1 + "  " + r2);

AtomicInteger integer = new AtomicInteger(2);
int r3 = integer.get();
// 返回原始值，然后将原始值增加1后更新到integer中
int r4 = integer.getAndIncrement();
int r5 = integer.get();
        System.out.println(r3 + "  " + r4 + "  " + r5);


// 打印
5  true  7
10 10  15
2  2  3
```

`public final boolean compareAndSet( expect,  update)`
如果当前Atomic对象的值与expect相等，那么我们就去更新值为update，并且返回true，否则返回false

`public final boolean getAndSet(boolean newValue)` 
获取旧值并设置新值

`public final int addAndGet(delta)`
再旧值的基础上先加上delta，再返回新值

`public final int incrementAndGet()`
先 ++ ，再返回新值

`public final int decrementAndGet()`
先 -- ，再返回新值

## 6、 读写锁
`ReentrantReadWriteLock`
- 实现了`ReadWriteLock`接口，主要方法`readLock()`方法、`writeLock()`方法
- 如果一个线程获取了读锁，其他线程也可以同时获取读锁；如果一个线程获取了读锁，其他线程获取写锁时需要阻塞（因为要保证读写数据的一致和可见）；如果一个线程获取了写锁，其他线程的读和写请求只能阻塞。总结：**读读共享、写写互斥、读写互斥**
- 场景：
- 
```java
// 创建 读写锁
ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
Lock readLock = reentrantReadWriteLock.readLock();
Lock writeLock = reentrantReadWriteLock.writeLock();

// 获取读锁
readLock.lock();
// 释放读锁
readLock.unlock();

// 获取写锁
writeLock.lock();

// 释放写锁
writeLock.unlock();
```

