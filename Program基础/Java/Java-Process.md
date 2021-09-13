
## Runtime和Process

### 调用外部程序的方法
```java
// 方式一：
// 如：Runtime.getRuntime().exec("ping 192.168.0.125"); 
Runtime runtime = Runtime.getRuntime();
Process p = runtime.exec(cmd);


// 方式二：
// 如：new ProcessBuilder("ping","192.168.0.125");  
Process p=new ProcessBuilder(cmd).start();

```

### process是一个抽象的类，它包含6个抽象的方法
```java
// 从内存输出到外部程序，即外部程序的输入流
abstract public OutputStream getOutputStream();

// 从外部程序输入到内存，即外部程序的输出流
abstract public InputStream getInputStream();

// 从外部程序输入到内存，即外部程序的错误流
abstract public InputStream getErrorStream();

// 等待外部程序运行结束，并获取运行结果
abstract public int waitFor() throws InterruptedException;

// 获取运行结果
abstract public int exitValue();


abstract public void destroy();

```


### 解析

```java.lang.Runtime```类的```exec()```方法与相关联的```ProcessBuilder.start()```方法可以被用来调用外部程序进程。这些外部程序运行时由```java.lang.Process```对象描述。

这个对象包含一个输入流，输出流，以及一个错误流。

因为这个进程对象可被Java程序用来与外部程序通信，外部进程的输入流是一个```OutputStream```对象，可以通过```Process.getOutputStream()```方法获取。
同样的，外部进程的输出流和错误流都可以由输入流对象来代表，分别通过```Process.getInputStream()```与```Process.getErrorStream()```方法来获取。
这些进程可能需要通过其输入流对其提供输入，并且其输出流、错误流或两者同时会产生输出。
不正确地处理这些外部程序可能会导致一些意外的异常、DoS，及其他安全问题。一个进程如果试图从一个空的输入流中读取输入，则会一直阻塞，直到为其提供输入。
因此，在调用这样的进程时，必须为其提供输入。一个外部进程的输出可能会耗尽该进程输出流与错误流的缓冲区。

当发生这种情况时，Java 程序可能会阻塞外部进程，同时阻碍Java程序与外部程序的继续运行。注意，许多平台限制了输出流可用的缓冲区大小。
因此，在运行一个外部进程时，如果此进程往其输出流发送任何数据，则必须将其输出流清空。类似的，如果进程会往其错误流发送数据，其错误流也必须被清空。


## FAQ：

### 如何区分外部进程的输入流和输出流




