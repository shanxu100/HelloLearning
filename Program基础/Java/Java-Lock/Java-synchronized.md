# ```synchronized```的使用

## 1、语法	
```sychronized```可以作用于**变量、方法和类，但是不能作用于null。**

1. 修饰普通方法
```java
public synchronized void testFun() {        
}
```

2. 修饰静态方法
```java
public static synchronized void testFun() {        
}
```

3. 修饰普通变量
```java
synchronized (myObject) {
}

synchronized (this) {
}

synchronized (MyClass.class) {
}
```

4. 修饰静态变量
```java
private static final Object MY_STATIC_OBJ = new Object();

synchronized (MY_STATIC_OBJ) {
}
```


## 2、“对象锁”与“类锁”

### 2.1 对象锁：
当一个对象中有```synchronized method``` 或```synchronized block``` 的时候，调用此对象的同步方法或进入其同步区域时，就必须先获得对象锁。如果此对象的对象锁已被其他调用者占用，则需要等待此锁被释放。java的所有对象都含有1个互斥锁，这个锁由JVM自动获取和释放。线程进入```synchronized```方法的时候获取该对象的锁，当然如果已经有线程获取了这个对象的锁，那么当前线程会等待；```synchronized```方法正常返回或者抛异常而终止，JVM会自动释放对象锁。
- 理解：**用来控制同一个实例在不同线程中的同步**
	- 修饰普通方法：即方法中增加```synchronized```关键字。
	- ```synchronized(this)```或者```synchronized(某一个实例)```

**两个线程中分别访问同一个对象的同步方法，会互斥；不同对象的同步方法，不会互斥**

### 2.2 类锁：
由于一个class不论被实例化多少次，其中的静态方法和静态变量在内存中都只有一份。所以，一旦一个静态的方法被申明为```synchronized```。此类所有的实例化对象在调用此方法，共用同一把锁，我们称之为类锁。所谓的类锁，不过是Class对象的锁而已。
- 理解：**是用来控制静态方法（或静态变量互斥体）之间的同步**
	- 修饰静态方法（或静态代码块）
	- synchronized(xx.class)

两个线程中调用同一个类的两个不同的同步方法，会互斥
 

### 2.3 换一个角度划分：显式与隐式
- 隐式：
  - synchronize 修饰方法 
  - synchronize(this)

- 显式：
  - synchronize(具体的对象)



## 3、注意

### 3.1  ```sychronized```对于同一个监视锁可以获取多次
```java
// Example
class Test {
  public static void main(String[] args) {
    Test t = new Test();
    synchronized (t) {
      synchronized (t) {
        System.out.println("made it!");
      }
    }
  }
}

// This program produces the output:
made it!

```


### 3.2 <font color=red>禁止</font>基于可被重用的对象进行同步
错误示例：
```java
private final Boolean lock = Boolean.FALSE;
private final Integer lock = 0;
private final String lock = "guan";
private final String lock = new String("guan").intern();

// 语法不对，synchronized的锁只能是对象，而不是基本数据类型
private final int lock = 0;

// 加锁
synchronized (lock) {
	// 具体事务......
}

```

正确示例：
```java
// -128-127之间的是Integer的缓存对象
private final Integer lock = 128; // 这种方式确实有效，但不知道是否符合编程规范
private final Integer lock = new Integer(0);
private final String lock = new String("guan");
private final Object lock = new Object();
// 加锁
synchronized (lock) {
	// 具体事务......
}
```

