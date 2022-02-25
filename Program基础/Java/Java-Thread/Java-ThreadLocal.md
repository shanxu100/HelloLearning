# ThreadLocal原理简介
参考：  
1. [《内存泄露的原因找到了，罪魁祸首居然是Java TheadLocal》](https://mp.weixin.qq.com/s/tNpP30MFXFt4Z_p9iuyYCQ)
2. [《ThreadLocal-hash冲突与内存泄漏》](https://blog.csdn.net/Summer_And_Opencv/article/details/104632272)
3. [《图解分析ThreadLocal的原理与应用场景》](https://juejin.cn/post/6858570628900126728)
4. [《被大厂面试官连环炮轰炸的ThreadLocal （吃透源码的每一个细节和设计原理）》](https://juejin.cn/post/6844903974454329358)
5. [《ThreadLocal使用与原理》](https://juejin.cn/post/6959333602748268575)

**需求**：为线程创建自己的“局部变量”，这样可以避免多个线程对同一个变量的竞争，保证线程安全（ThreadLocal目的并不是为了实现对一个变量的互斥访问）

**作用**：针对同一个变量，```ThreadLocal`类可以为每一个线程创建一个副本。**多线程中，每条线程都需要拥有一个同名变量，该变量在不同线程中独立存取，互不影响。**

**原理**：在`ThreadLocal`类中定义了一个静态内部类`ThreadLocalMap`。每一个Thread都有一个`ThreadLocalMap`类型的成员变量`threadLocals`。Thread就是用threadLocals来存储每一个线程中变量的副本。threadLocals本质是一个 `Entry数组`，每个数组元素是一个键值对：key为threadLocal对象，vlaue为存储的值，即我们根据threadLocal实例，来找到对应线程的变量副本。


## 1. ThreadLocal的使用
**场景：**
- 当需要存储线程私有变量的时候。
- 当需要实现线程安全的变量时。
- 当需要减少线程资源竞争的时候。

常用使用方法如下：
```java
// new
ThreadLocal<BigObject> threadLocal = new ThreadLocal<>();
// set存
threadLocal.set(new BigObject());
// get取
BigObject bo = threadLocal.get();
// remove删
threadLocal.remove();
```

## 2、 ThreadLocal的数据结构
- **<font color="red"> 每个线程是一个```Thread```实例，每个线程实例都对应一个```TheadLocalMap```实例</font>**，即```threadLocals```。```TheadLocalMap```本质是```Entry[] table```数组。数组中的元素```Entry```是```WeakReference```弱引用的子类。
	```java
	Thread : ThreadLocal.ThreadLocalMap threadLocals
	```

- ```ThreadLocal```本身并不是一个容器，我们存取的value实际上存储在```ThreadLocalMap```中。```ThreadLocal```只是作为```TheadLocalMap```的key，最终存储在```Entry[] table```数组中。
	```java
	ThreadLocalMap map = Thread.currentThread().threadLocals;
	map.put(this,value);
	map.get(this);
	```

- 通过对```ThreadLocal```进行 **<font color=red>hash</font>** 获取到```Entry[] table```数组的下标。其中解决哈希冲突的策略是 **<font color=red>开放地址法</font>**：hash冲突的情况下，则后挪一位。原因：
	- Thread中使用```ThreadLocalMap```中存储的键值对不会特别多
	- 相对于链地址法（拉链法）而言节省了存储指针的空间   

- 其中```TheadLocalMap```中的```Entry```是一对**key-value**，```Entry```为```WeakReference```的子类。在```Entry```的键值对中，```key```为弱引用，```value```为强引用
	```java
	static class Entry extends WeakReference<ThreadLocal<?>>{
		/** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
	}
	```

## 3、内存泄漏
### 3.1 原因：  
1. 如果线程Thread执行完销毁了，那么```ThreadLocalMap```会整个销毁，也就不会有内存泄漏的问题了。  
2. 从```ThreadLocal```的数据结构中可以看到，```Thread```持有了```ThreadLocalMap```实例，所以```ThreadLocalMap```的生命周期跟```Thread```一样长。
3. 在```ThreadLocalMap```中的Entry中，**Key是弱引用，Value是强引用**。如果没有手动删除key-value，就会导致内存泄漏：GC时弱引用Key会被回收，而Value不会回收，所以存在内存泄漏。    
4. 但是，ThreadLocal做了优化：<font color=red>```ThreadLocalMap```调用``set``、```get```、```remove```的时候，会检查Entry。所以存在内存泄漏的Value，会在下一次操作时被清除。</font>

### 3.2 那么怎么避免内存泄漏呢？
每次使用完ThreadLocal，建议调用它的remove()方法，清除数据。

### 4、FAQ

#### 4.1 常见的Hash冲突解决算法
1. 开放定址法
   当关键字key使用p=H(key)出现冲突时，以p为基础，按照某种规则，产生另一个哈希地址p1、p2...
   常用的方法有：线行探查法、平方探查法和双散列函数探查法等

2. 再哈希法：
   同时构造多个不同的哈希函数，当使用 H(key) 发生冲突时，使用准备好的 H2(key)、H3(key)...重新计算哈希值

3. 链地址法：
   将哈希值相同的元素构成一个同义词的单链表，并将单链表的头指针存放在哈希表的第i个单元中；查找、插入和删除主要在同义词链表中进行。链表法适用于经常进行插入和删除的情况

