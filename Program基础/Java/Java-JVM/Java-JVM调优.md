## JVM调优

### 1、 调优步骤
1. 分析 GC 日志及 dump 文件，判断是否需要优化。
2. 依次针对内存、时延、吞吐量等方面，确定 JVM 调优量化目标和 JVM调优参数
3. 反复对比测试，找到最优参数
   
注意：调优的优先级：满足内存使用需求>满足时延需求>满足吞吐量需求

### 2、调优参数
`-XX` 参数被称为**不稳定参数**，此类参数的设置很容易引起 JVM 性能上的差异：如果此类参数设置合理将大大提高JVM的性能及稳定性，反之，则会降低性能及稳定性。
常见的不稳定参数包括**布尔类型参数值、数字类型参数值和字符串类型参数值**。
- `布尔类型参数值`分为两种：`-XX:+` 和 `-XX:-`，“+” 表示启用该选项；“-” 表示禁用该选项；
- `数字类型参数`的格式是：-XX: ，给选项设置一个数字类型值，在数值后面可增加单位，例如：“m” 或 “M” 表示兆字节、“k” 或 “K” 表示千字节；
- `字符串类型参数`的格式和数字类型参数的格式是相同的，只是后面所跟的数值不同，字符串类型参数设置的是一个字符串类型值，通常用于指定一个文件、路径或一系列命令列表。例如：-XX:HeapDumpPath=xxx/xxx/xxx（文件路径）。

### 3、举例
-Xms2g：JVM启动初始化堆大小为2g，Xms的默认是物理内存的1/64但小于1G。


-Xmx2g：JVM最大的堆大小为2g，Xmx默认是物理内存的1/4但小于1G；将-Xms和-Xmx的值配置为一样，可以避免每次垃圾回收完成后对JVM堆大小进行重新的调整。


`-Xmn512M`：堆中的新生代大小为512M

`-Xss128K`：每个线程的堆栈大小为128K

`-XX:PermSize=128M`：JVM永久的初始化大小为128M

`-XX:MaxPermSize=128M`：JVM永久代的最大大小为128M

`-XX:NewRatio=4`：JVM堆的新生代和老年代的大小比例为1：4

`-XX:SurvivorRatio=4`：新生代Surivor区（新生代有2个Surivor区）和Eden区的比例为2：4

`-XX:MaxTenuringThreshold=1`：新生代的对象经过几次垃圾回收后（如果还存活），进入老年代。如果该参数设置为0，这表示新生代的对象在垃圾回收后，不进入survivor区，直接进入老年代





