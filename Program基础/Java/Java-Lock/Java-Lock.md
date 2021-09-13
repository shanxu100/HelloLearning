# Lock的使用
参考：
- [重入锁ReentranLock详解、实战、及与对比Synchronized](https://mp.weixin.qq.com/s?__biz=MzUzNDE2MDY4MQ==&mid=2247483979&idx=1&sn=e5ad405c04194a17c547bb1921494c21)
- [lock.lock() 写在 try 代码块内部行吗？](https://blog.csdn.net/u013568373/article/details/98480603)

## 1、 ```Lock```基本使用

### 1.1 语法
```Lock```**接口**，```ReentranLock```就是```Lock```接口的实现类。

```java
// 实例化一个对象（默认是非公平锁）
Lock lock = new ReentrantLock();
lock.lock();
try {
    // ..
} finally {
    //一定在finally中释放锁
    lock.unlock();
}

```
注意：
1. **在 ```finally``` 块中释放锁，目的是保证在获取到锁之后，最终能够被释放，否则会造成死锁**  
2. **不要将获取锁的过程写在 try 块中**：因为如果在```lock.lock()```获取锁）时发生了异常（即try中异常抛出），那么就会在finally中进行 ```lock.unlock()```。 此时，由于获取锁时失败，那么释放锁时就会导致 ```IllegalMonitorStateException```异常。


### 1.2 常用API  
- ```lock.lock()```：获取锁，如果锁被暂用则一直等待  
- ```lock.tryLock()```: 获取锁，立即返回，不等待：如果获取锁的时候锁被占用就返回false，否则返回true  
- ```Lock.tryLock(long time, TimeUnit unit) throw InterruptedException```:同```lock.tryLock()```只是多了获取锁的超时时间。  
- ```Lock.lockInterruptibly()```:获取锁时可以相应中断
- ```lock.unlock()```:释放锁

#### 1.2.1 Lock的Condition
接口：```public interface Condition```  
创建：```Condition condition = lock.newCondition()```  
 
它用来替代传统的Object的wait()、notify()

|   Condition   |   Object  |   描述    |
|   --          |   --      |   --      |
|   await()     |   wait()  |   阻塞    |
|   signal()    |   notify()|   唤醒    |
|   signalAll() |   notifyAll() |       |

## 2、使用举例
[《【每日一道算法题】设计有限阻塞队列》](https://juejin.cn/post/6959509346803777550) 











