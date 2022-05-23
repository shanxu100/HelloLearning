# Java IO

- [《Java socket字节流传输的示例》](https://www.cnblogs.com/fomeiherz/p/5878936.html)
- [《设计模式 | 装饰者模式及典型应用》](https://blog.csdn.net/wwwdc1012/article/details/82764333)
- [《基本功：超全面 IO 流教程，小白也能看懂》](https://mp.weixin.qq.com/s/ljwuyJHVas1entgieFIpeA)

## 基本结构

input和output的区别：
- input/输入：数据从数据源加载到内存。
- output/输出：数据从内存写回到数据源。

字节流和字符流的使用场景：
- 对于一些二进制数据（如图片，mp4等）比较适合用字节流的方式进行加载
- 字符流通常比较适合用于读取一些文本数据（如txt）。因为这类资源通常都是以字符类型数据进行存储，所以使用字符流要比字节流更加高效。


- javaIO(注意：装饰者模式)
	- 字节流
		- InputStream
			- FileInputStream：被装饰者
			- PipedInputStream：被装饰者
			- ByteArrayInputStream：被装饰者
			- FilterInputStream：抽象的装饰类
				- BufferedInputStream：**具体的装饰类**
				- DataInputStream：**具体的装饰类**

		- OutputStream（同上）

	- 字符流
		- reader
			- FileReader
			- CharArrayReader
			- PipedReader
			- StringReader
			- BufferedReader：**具体的装饰类**
			- InputStreamReader：**具体的装饰类**

		- writer（同理）

--------------------

## File

公司安全编码规定，禁止使用```file.getAbsolutePath()```应该使用```file.getCanonicalPath()```

- ```file.getPath();```
返回的是File构造方法里的路径，是什么就是什么，不增不减
- ```file.getAbsolutePath()```
返回的其实是user.dir+getPath()的内容
- ```file.getCanonicalPath()```
返回的就是标准的将符号完全解析的路径

```java
try {
    String filePath="../abc";
    File file=new File(filePath);
    System.out.println("file.getPath()="+file.getPath());
    System.out.println("file.getAbsolutePath()="+file.getAbsolutePath());
    System.out.println("file.getCanonicalPath()="+file.getCanonicalPath());
} catch (IOException e) {
    e.printStackTrace();
}

// 结果
// file.getPath()=..\abc
// file.getAbsolutePath()=D:\Projects\TestJava\..\abc
// file.getCanonicalPath()=D:\Projects\abc
```

- file1.renameTo( file2 )
重命名、移动
```java
File oldName = new File("E:\\hello\\test\\1.txt");
File newName = new File("E:\\hello1\\test1\\2.txt");
System.out.println(oldName.renameTo(newName));//true
```
> 此方法行为的许多方面都是与平台有关的：**重命名操作无法将一个文件从一个文件系统移动到另一个文件系统**，该操作不是不可分的
> 如果已经存在具有目标抽象路径名的文件，那么该操作可能无法获得成功。**应该始终检查返回值，以确保重命名操作成功**。

- File[] fils = file.listFiles()
获取file目录下的文件。所以可以用来遍历file目录下的文件

- FileDescriptor
文件描述符（File descriptor），是计算机科学中的一个术语，是一个用于表述指向文件的引用的抽象化概念，可以对文件进行操作。
文件描述符在形式上是一个非负整数。实际上，它是一个索引值，指向内核为每一个进程所维护的记录表——该记录表记录了该进程打开的文件。当程序打开一个现有文件或者创建一个新文件时，内核向进程返回一个文件描述符。
在程序设计中，一些涉及底层的程序编写往往会围绕着文件描述符展开。但是文件描述符这一概念往往只适用于UNIX、Linux这样的操作系统
参考：[《一文帮你搞懂 Android 文件描述符》](https://juejin.cn/post/6935234302187667492)

## 数据持久化
```java
// 仅仅写到缓冲区
outputStream.write()

// 目的是将缓冲区的内容将写入底层输出流。
// 开发者可以手动调用
// 在调用close()时,JDK也会自动触发flush()方法
outputStream.flush();

// 会触发系统调用 fsync，将数据落盘
fileOutputStream.getFD().sync() 

// 强制文件数据与元数据落盘
outputStream.getChannel().force(true);
```

## Path

### 创建Path
```java
//使用绝对路径
Path path= Paths.get("c:\\data\\myfile.txt");
//使用相对路径
Path path = Paths.get("/home/jakobjenkov/myfile.txt");

Path path = FileSystems.getDefault().getPath("c:\\data\\myfile.txt");
```

### Path、File和URI
```java
File file = new File("C:/my.ini");
Path p1 = file.toPath();
p1.toFile();
file.toURI();
```

### 获取Path的相关信息
```java
System.out.println("文件名：" + path.getFileName());
System.out.println("名称元素的数量：" + path.getNameCount());
System.out.println("父路径：" + path.getParent());
System.out.println("根路径：" + path.getRoot());
System.out.println("是否是绝对路径：" + path.isAbsolute());
//startsWith()方法的参数既可以是字符串也可以是Path对象
System.out.println("是否是以为给定的路径D:开始：" + path.startsWith("D:\\") );
System.out.println("该路径的字符串形式：" + path.toString());
```

--------------

## Files 和 Paths
两个工具类

所在包名：```java.nio.file```

```java
Path path = Paths.get("tmp.txt");
Files.exists(path);
```



