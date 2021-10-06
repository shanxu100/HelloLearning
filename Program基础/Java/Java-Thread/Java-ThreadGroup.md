# ThreadGroup

线程组（```ThreadGroup```）简单来说就是一个线程集合。线程（```Thread```）就是```ThreadGroup```中的元素。  
线程组的出现是为了更方便地管理线程。

线程组是一个树形结构：每个线程都隶属于一个线程组，线程组又有自己的父线程组
```
System线程组
|
|- main线程组
|   |
|   | - main线程
|   | - subgroup1
|           |
|           | - thread1
|           | - thread2
|   | - subgroup2
```
注：
1. ```main()```方法执行后，JVM创建自动创建```system线程组```和```main线程组```，**main方法所在线程存放在main线程组中**
2. system线程组用来处理JVM的系统任务，例如对象的销毁等。
3. main线程组是system线程组的直接子线程组。这个线程组至少包含一个main线程，用于执行main方法。
4. main线程组的子线程组就是应用程序创建的线程组。




ThreadGroup 与 ThreadPool的区别







