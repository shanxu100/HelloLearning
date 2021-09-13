# Java基础

## 语法

- 静态方法和普通方法
	- 静态函数是在类加载的时候就在内存中加载完成，可以直接运行的函数。
	- 非静态方法需要在类加载完成后，通过new一个对象调用运行。
	- 静态方法不能override

- 静态代码块和普通代码块
静态代码块和静态变量一样，在类加载的时候执行，即在main函数调用之前就已经执行了。
静态代码块和静态变量是先声明的先执行，并且只执行一次。
类被加载了不一定就会执行静态代码块，只有一个类被主动使用的时候，静态代码才会被执行。

- final类和final方法
final类不能继承；final方法可以继承，但不能override

- StringBuilder和StringBuffer
	每次操作字符串，String会生成一个新的对象
	StringBuffer是线程安全的
	StringBuilder是非线程安全的

- String变量是不可变的
	- String 类是 final 类型的，String 不能被继承。
	- 其 value字段存储了具体的字符串，即 char[]，其值被定义成 private final 的。
		private final char value[];
	- 每次“变更字符串”，实际上是变更了字符串引用

- 堆、栈、段
栈：存放的是局部变量（在方法和语句块内部定义的变量）：当方法和语句块一结束，空间马上释放
堆：存放的是成员变量（也叫属性，在方法外，在类里，定义的变量）：随着对象而产生，随对象销毁而销毁。
数据段（方法区）：存放的是类变量（也叫静态变量，和成员变量相比多加上关键字static）：长驻内存，创建好以后，不会消失

- 名词
静态变量（类变量）
实例变量



---------------------
## 遍历与删除
``foreach``循环List中删除元素,一般会抛出```ConcurrentModificationException```
        **删除倒数第2个**特殊 不会抛出异常，详解可参考JDK源码
例如：已知如下一个list
```java
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
list.add("c");
list.add("d");
```
- 这样删除会报```ConcurrentModificationException```异常：
```java
for (String value : list) {
    if ("b".equals(value)) {
        list.remove(value);
    }
}
```
- 这样删除不会出现异常
```java
for (String value : list) {
    if ("c".equals(value)) {
        list.remove(value);
    }
}
```





----------------------




### 什么是强引用、软引用，弱引用，虚引用？有什么区别？
**软引用是内存不足的时候就回收，弱引用是gc线程扫描到就回收，虚引用是不决定对象的生命周期**。
PhantomReference必须要和ReferenceQueue联合使用，SoftReference和WeakReference可以选择和ReferenceQueue联合使用也可以不选择，这使他们的区别之一。

- 强引用
**一直活着**：类似“Object obj=new Object()”这类的引用，只要强引用还存在，垃圾收集器永远不会回收掉被引用的对象实例。

- 弱引用
**回收就会死亡**：被弱引用关联的对象实例只能生存到下一次垃圾收集发生之前。当垃圾收集器工作时，无论当前内存是否足够，都会回收掉只被弱引用关联的对象实例。在JDK 1.2之后，提供了WeakReference类来实现弱引用。

- 软引用
**有一次活的机会**：软引用关联着的对象，**在系统将要发生内存溢出（OOM）异常之前**，将会把这些对象实例列进回收范围之中进行第二次回收。如果这次回收还没有足够的内存，才会抛出内存溢出异常。在JDK 1.2之后，提供了SoftReference类来实现软引用。

- 虚引用
也称为幽灵引用或者幻影引用，它是最弱的一种引用关系。一个对象实例是否有虚引用的存在，完全不会对其生存时间构成影响，也无法通过虚引用来取得一个对象实例。为一个对象设置虚引用关联的唯一目的就是**能在这个对象实例被收集器回收时收到一个系统通知**。在JDK 1.2之后，提供了PhantomReference类来实现虚引用。

### hashCode()和equals()

== 表示对两个对象的地址的比较
equals 表示对两个内容的比较，这个可以通过override equals()方法实现。Object类中的equal()默认是对地址的比较

hashCode() 的作用是获取哈希码，也称为散列码；它实际上是返回一个int整数。这个哈希码的作用是确定该对象在哈希表中的索引位置。也就是说：hashCode() 在散列表中才有用，在其它情况下没用，比如HashSet和HashMap中add该对象。

override equals()方法，也一定要override hashCode()方法。
- 因为equals()为true的情况下，两个hashCode()方法的返回值必须相等；equals()为false的情况下，两个hashCode()方法的返回值可以相等也可以不相等。
- 反过来说，两个对象hashCode()返回值不相等，那么equals()一定为false；如果两个对象hashCode()返回值相等，那么equals()可能为true或false

### 同时override  equals() 和 hashCode()方法
假如只override equals()方法，使得两个实例p1、p2通过equals()方法返回true。那么在放入同一个HashSet中时，该set可以同时存在两个实例p1和p2。与上述矛盾。


### Comparable和Comparator的区别

Comparable内比较器：实现了Comparable接口的类有一个特点，就是这些类是可以和自己比较的。需要override compareTo()方法实现比较。

Comparator外比较器：假如两个对象没有实现Comparable接口，又想进行比较。那么就需要使用Comparator


# 软件测试：
### 白盒测试和黑盒测试
黑盒测试又叫功能测试：主要关注被测软件的功能实现，而不是内部逻辑。被测试对象的内部结构对测试人员是不可见的。
白盒测试：要求测试人员对被测试对象的内部结构有一个清楚地了解。
灰盒测试：一般在白盒测试中交叉使用黑盒测试的方法，在黑盒测试中交叉使用白盒测试的方法。

### 单元测试(Unit Testing)
是对软件基本组成单元进行的测试，如函数或是一个类的方法。这里的单元，就是软件设计的最小单位。

### 测试用例(Test Case)
是为某个特殊目标而编制的一组测试输入、执行条件以及预期结果，以便测试某个程序路径或核实是否满足某个特定需求。

--------------------------
# 案例

## 是否遇到过OOM，如何处理的
- 背景：war包部署到tomcat，运行过程中发生错误
- 排查：
	- 查看tomcat中log文件夹下的日志，查找崩溃信息，发现是OOM
- 措施
	- 优化JVM参数：增加heap堆大小
	- 开启JVM远程监控端口，使用jCOnsole监控JVM运行状态
	- 配置JVM，发生崩溃时导出堆信息，并执行指定脚本（重启服务，并发邮件通知）

----------------
# OkHttp

## 拦截器：
可以一次性的修改请求和响应，可以做统一的日志记录

- Application interceptors：
是在请求执行刚开始，还没有执行OkHttp的核心代码前进行拦截
适用于在请求前统一添加一些公共参数；
总是被调用一次，即使HTTP响应结果是从缓存中获取的

- Network Interceptors：
在连接网络之前进行拦截
可以获取到最终发送的 request 请求和最初收到的 response 响应

----------------
# 三级缓存

**应用场景**：图片加载
**核心**：LRU算法
> 一级：内存 -- 二级：硬盘 -- 三级：网络
	LruCash：内存缓存
	DiskLruCash：硬盘缓存





为什么Java程序的启动函数一定是main？




