## Java Exception

### 1、概念
在Java中，Throwable是所有错误和异常的父类。

```
Throwable
	|
	|--Error（非受检）
	|
	|--Exception
			|
			|-- SQLException
            |
            |-- ....略
            |
            |-- IOException
			|		|
			|		|--EOFException
			|		|--FileNotFoundException
			|
			|-- RuntimeException（非受检）
					|
					|--NullPointerException
					|--ClassNotFoundException
					|--ArrayIndexOutOfBoundsException
                    |-- ...略
```
- **```Error```**:表示运行应用程序中出现较严重问题。并且程序不应该尝试去捕获。常见的```Error```子类有```OutOfMemoryError```和```StackOverflowError```（这两个Error的直接父类是```VirtualMachineError```）。遇到这样的错误，一般建议让程序终止。

- **```Exception```**：表示程序可以并且建议处理的异常，然后使程序恢复运行。
。

- **Checked Exception**:Java要求在编译时必须处置的异常，称之为“受检异常”。这类异常是由于外部的一些偶然因素引起的。Java编译器要求强制处理，即必须得到捕获或抛出。即当程序中可能出现这类异常，要么用try-catch语句捕获它，要么用throws子句声明抛出它，否则编译不会通过。  
在Throwable中，**除去```RuntimeException```和```Error```以及两者的子类**，剩下的都是Checked Exception。比如```IOException```，或者```继承Exception的自定义Exception```。

- **Unchecked Exception**:编译器不要求强制处置的异常。这类异常由程序员逻辑错误导致，应该人为承担责任。Java在编译时不要求强制处理，即可以捕获或抛出，也可以不捕获或抛出。非受检异常包括```RuntimeException```和```Error```，以及两者的子类。

### 2、敏感异常：

| exception | 描述 |
| -- | -- |
java.io.FileNotFoundException | 泄露文件系统结构和文件名列举
java.util.jar.JarException | 泄露文件系统结构
java.util.MissingResourceException  | 资源列举
java.security.acl.NotOwnerException  | 所有人列举
java.util.ConcurrentModificationException  | 可能提供线程不安全的代码信息
javax.naming.InsufficientResourcesException |  服务器资源不足（可能有利于DoS攻击）
java.net.BindException | 当不信任客户端能够选择服务器端口时造成开放端口列举
java.lang.OutOfMemoryError  | DoS
java.lang.StackOverflowError  | DoS
java.sql.SQLException |  数据库结构，用户名列举

### 3、throw,throws,Throwable的区别
- throw：可以抛出异常 
- throws：用在方法上，表示这个方法会抛出异常，但不处理异常
- Throwable：是Error和Exception的父类

### 4、线程中抛异常

**4.1 线程不允许抛出未捕获的checked exception，即在线程内部解决checked exception。**

**4.2 在程序运行时发生Unchecked Exception，可以使用```UncaughtExceptionHandler```**

> **作用**：当一个线程因为一个未捕获的异常而导致自身运行中断时，可以回调这个接口进行通知**

- 为当前线程设置UncaughtExceptionHandler。
	即可以调用```myThread.setUncaughtExceptionHandler()```获取异常信息。
- 获取当前线程的UncaughtExceptionHandler
	```myThread.getUncaughtExceptionHandler()```
- 为**当前程序中所有线程设置默认的**UncaughtExceptionHandler，
	 ```Thread.setDefaultUncaughtExceptionHandler()```
- 获取**当前程序中所有线程默认的**UncaughtExceptionHandler
	```Thread.getDefaultUncaughtExceptionHandler()```

**捕获顺序：**
1. 线程中自己的 try-catch
2. 线程自己的 myThread.setUncaughtExceptionHandler
3. 默认的 Thread.setDefaultUncaughtExceptionHandler ，这里设置的handler由当前进程的所有线程共享。
**对于3，在sdk中慎用**。因为上层业务也可能设置默认的handler，所以可能造成sdk和app相互覆盖自己handler。
或者在设置之前先 Thread.getDefaultUncaughtExceptionHandler 判断其他业务有没有设置 默认的handler。


-----
## 5、try-catch-finally用法
### 5.1 基本原则
- try中没有抛出异常
执行过程：try代码块-finally代码块-其他语句

- try中抛出异常
执行过程：try代码块-catch代码块-finally代码块 (将不再执行finally后面的“其他语句”)

### return的情况：
**在finally中禁止写return语句，详见《Java语言通用编程规范-V4.6-7.1》**
```java
try {
    System.out.println("this is try");
    throw new RuntimeException("my Exception");
    // return 0;
} catch (Exception e) {
    System.out.println("this is catch");
    return -1;
} finally {
    System.out.println("this is finally");
    // return 1;
}
// return 2;
```
- try中没有抛出异常

|代码块| try代码块 | finally代码块| 其他代码块 |结果 |
| :----: | :----: | :----: | :----: |:----:|
| return语句 | 没有 | 没有 | 有，return 2 | 2
| return语句 | 有，return 0 | 没有 | --- | 0
| return语句 | 没有/有，return 0 | 有，return 1 | --- | 1

- try中有抛出异常

|代码块| try代码块 | catch代码块 | finally代码块 |结果 |
| :----: | :----: | :----: | :----: |:----:|
| return语句 | 没有/有 | 没有/有 | 有，return 1 | 1
| return语句 | 必须有，return 0 | 必须有，return -1 | 没有| -1

### 5.2 Java语法糖 : ```try-with-resources```
简化了try-catch-finally的使用，例如：

- 使用```try-catch-finally```
```java
static void copy(String src, String desc) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(desc);
        byte[] bytes = new byte[1024];
        int n;
        try {
            while ((n = in.read(bytes)) != -1) {
                out.write(bytes, 0, n);
            }
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
```

- 使用```try-with-resources```
```java
static void copy(String src, String desc) {
    try (InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(desc)) {
        byte[] bytes = new byte[1024];
        int n;
        while ((n = in.read(bytes)) != -1) {
            out.write(bytes, 0, n);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



