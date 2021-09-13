# Java IO

## 基本结构

 > https://blog.csdn.net/wwwdc1012/article/details/82764333

- javaIO(注意：装饰者模式)
	- 字节流
		- InputStream
			- FileInputStream：具体的组件，是被装饰者
			- PipedInputStream：被装饰者
			- ByteArrayInputStream：被装饰者
			- FilterInputStream：抽象的装饰类
				- BufferedInputStream：具体的装饰类
				- DataInputStream：具体的装饰类

		- OutputStream（同理）

	- 字符流
		- reader
			- BuffedReader
			- StringReader
			- InputStreamReader
			- ByteArrayReader
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
文件描述符，对文件进行操作
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



