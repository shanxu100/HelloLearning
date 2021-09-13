# Memory 内存监控与内存泄漏
参考：  
[《dumpsys 查看内存分配》](https://developer.android.com/studio/command-line/dumpsys#ViewingAllocations)
[《常见导致内存问题的案例》](https://juejin.cn/post/6956183330622701576)

## 1、查看Android对App的内存使用限制
命令：
```bash
adb shell heapgrowthlimit | grep heap
```
结果：
```
[dalvik.vm.heapgrowthlimit]: [384m]
```

## 2、查看特定时间点的内存使用情况
### 2.1 命令：
```bash
adb shell dumpsys meminfo $package_name|$pid
```
### 2.2 输出
```bash
# adb shell dumpsys meminfo com.xxx.xxx.demo

Applications Memory Usage (in Kilobytes):
Uptime: 399173389 Realtime: 3109146405

** MEMINFO in pid 21024 [com.xxx.xxx.demo] **
                   Pss  Private  Private  SwapPss     Heap     Heap     Heap
                 Total    Dirty    Clean    Dirty     Size    Alloc     Free
                ------   ------   ------   ------   ------   ------   ------
  Native Heap    31035    30984        0        0    90112    35250    54861
  Dalvik Heap     4980     4940        0        0     9793     3649     6144
 Dalvik Other     1475     1472        0        0
        Stack       44       44        0        0
       Ashmem        2        0        0        0
    Other dev       12        0       12        0
     .so mmap    18282     1464    14248       23
    .jar mmap      687        0       12        0
    .apk mmap      394        0       92        0
    .ttf mmap      303        0      172        0
    .dex mmap     5168      484     4684        0
    .oat mmap      767        0      108        0
    .art mmap     2445     2200        0       21
   Other mmap      484      144        0        0
    GL mtrack    10360    10360        0        0
      Unknown     2326     2308        0        0
        TOTAL    78808    54400    19328       44    99905    38899    61005

 App Summary
                       Pss(KB)
                        ------
           Java Heap:     7140
         Native Heap:    30984
                Code:    21264
               Stack:       44
            Graphics:    10360
       Private Other:     3936
              System:     5080

               TOTAL:    78808       TOTAL SWAP PSS:       44

 Objects
               Views:      307         ViewRootImpl:        1
         AppContexts:       10           Activities:        2
              Assets:        5        AssetManagers:        0
       Local Binders:       25        Proxy Binders:       43
       Parcel memory:       19         Parcel count:       78
    Death Recipients:        1      OpenSSL Sockets:        1
            WebViews:        0

 SQL
         MEMORY_USED:      191
  PAGECACHE_OVERFLOW:       48          MALLOC_SIZE:      117

 DATABASES
      pgsz     dbsz   Lookaside(b)          cache  Dbname
         4       32             54         7/31/6  /data/user_de/0/com.xxx.xxx.demo/databases/client_xxx.db
```
### 2.3 字段解读

**title**  
`Uptime`：记录运行时间  
`Realtime`：实际运行时间  

**x轴**  
`Pss Total`：实际使用的物理内存总量  
`Private Dirty`：私有被写入的内存  
`Private Clean`：私有未被写入的内存  
`SwapPss Dirty`：交换内存  
`Heap Size`：堆大小  
`Heap Alloc`：堆中已被分配内存  
`Heap Free`：堆中未被分配内存  

**y轴**  
`Native Heap`：Linux进程堆，即Native层内存分配情况  
`Dalvik Heap`：VM堆，即Java层的内存分配情况  


## 3、查看一段时间内的的内存使用情况
命令：
```bash
# 查看指定包名app，在最近的N个小时的内存占用情况
adb shell dumpsys procstats [--hours $N] [$package_name]
```

## 4、内存泄漏
### 4.1 what：内存溢出 和 内存泄漏 的区别

内存溢出（OutOfMemory）：程序使用的空间大于原本系统给它申请的空间，即申请不到足够的内存；
内存泄露（MemoryLeak）：在new了对象之后，没有使用这个对象了，但是又没有被回收，一直占用着内存；
两者关系：```内存泄露 → 剩余内存不足 → 后续申请不到足够内存 →内存溢出```

### 4.2 how：内存泄漏的原因
- 静态变量持有Activity或Context的引用
- 单例模式中持有Activity或Context的引用
- 非静态内部类或匿名内部类的静态实例（默认持有外部类的引用）持有Activity或Context的引用
- 资源未关闭：file、stream、bitmap等  
总结：**A类实例引用B类实例，而A类实例的生命周期大于B类实例。那么当B类生命周期结束后不再使用时，由于A类持有引用，导致不能及时回收B类实例。**

### 4.3 Handler造成内存泄漏

- 原因：使用 **（匿名）内部类** 实例化handler，默认持有context（或Activity）引用，message持有Handler的引用 **（通过 Message.target 进行了指定）** 。这样三者就形成了```message – handler – activity```引用链。如果**存在被延时处理的```Message```**，那么就会导致 Activity 的泄露。
- 避免：
  - 静态内部类、Activity在onDestroy的时候；
  - 清空handler未处理的消息（ mHandler.removeCallbacksAndMessages(null) ）

### 4.4 ThreadLocal造成内存泄漏
详见：[《ThreadLocal原理简介》](../../Java/Java-Thread/Java-ThreadLocal.md)

### 4.5 WebView造成内存泄漏



## 5、内存泄漏检测工具
常用的检测内存泄漏的工具
光凭肉眼我们其实只能找出比较明显的内存泄露点，还有许多隐藏得比较深的内存泄露。那么我们如何找到这些点呢？当然是利用工具。
- Android Lint：Android Studio提供的代码扫描分析工具
- Leakcanary： Square 公司开源的“Android 和 Java 的内存泄漏检测库”




