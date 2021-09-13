# JDK Tool
> https://my.oschina.net/feichexia/blog/196575



## jstack
通过pid查询JVM中某个进程内的线程堆栈信息，如线程的运行状况，包括:
- 死循环
- Object.wait()情况
- 死锁
- 等待IO
> https://www.cnblogs.com/chenpi/p/5377445.html

当程序出现死锁的时候，使用命令：jstack 进程ID > jstack.log，然后在jstack.log文件中，搜索关键字“BLOCKED”，定位到引起死锁的地方。


## jconsole
一个java GUI监视工具，可以以图表化的形式显示各种数据


## jinfo
jinfo全称Java Configuration Info，
- 可以用来查看正在运行的 java 应用程序的扩展参数，包括Java System属性和JVM命令行参数
- 也可以动态的修改正在运行的 JVM 一些参数
- 系统崩溃时，jinfo可以从core文件里面知道崩溃的Java应用程序的配置信息

## jstat
> https://blog.csdn.net/zhaozheng7758/article/details/8623549
全称“Java Virtual Machine statistics monitoring tool”
监视JVM内存工具，显示本地或远程虚拟机进程中的类装载、内存、GC、JIT编译等运行数据
比如，是否存在内存问题、判断JVM垃圾回收是否正常等。
Linux 的Top命令主要监控的是总体的系统资源，很难定位到java应用程序。
如：
```bash
# 查看该进程进行gc的记录
jstat -gcutil pid
```
- 列表含义：
```
S0C、S1C、S0U、S1U：Survivor 0/1区容量（Capacity）和使用量（Used）
EC、EU：Eden区容量和使用量
OC、OU：年老代容量和使用量
PC、PU：永久代容量和使用量
YGC、YGCT：年轻代GC次数和GC耗时
FGC、FGCT：Full GC次数和Full GC耗时
GCT：GC总耗时
```
**注意：查看该进程 FGC 一列，是不是在增长，即Full GC频繁发生**。如果频繁Full GC，则使用jmap查看该进程堆内存中的对象详细信息。



## jmap
- 通过pid查询JVM中**某个进程内的堆内存使用状况**，即所有‘对象’的情况，如：产生那些对象，及其数量。
    - 打印进程的类加载器和类加载器加载的持久代对象信息，输出：类加载器名称、对象是否存活（不可靠）、对象地址、父类加载器、已加载的类大小等信息
    - 查看进程堆内存使用情况，包括使用的GC算法、堆配置参数和各代中堆内存使用情况
    - 查看堆内存中的对象数目、大小统计直方图
- 它可以生成java进程（使用pid）的 dump 文件，再结合结合jhat分析
如：
```bash
# 查看指定进程中 堆内存的情况，比如那些类的实例占用内存较多
jmap -histo pid


```
- 结果
```
num     #instances   #bytes  classname
-------------------------------------------
1:          xxxxx     xxxx      java.lang.String
2:

```

