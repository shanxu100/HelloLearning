# Future

```Future```接口：提供一个异步运算结果的功能。它可以让我们把耗时的操作从我们本身的调用线程中释放出来，只需要完成后再进行回调



## 1、FutureTask
参考：
[FutureTask的用法及两种常用的使用场景](https://cloud.tencent.com/developer/article/1041329)
[Java多线程之FutureTask的用法及解析](https://blog.csdn.net/chenliguan/article/details/54345993)

概述：
- ```FutureTask```可用于异步获取执行结果或取消执行任务的场景。
- FutureTask还可以确保即使调用了多次run方法，它都**只会执行一次```Runnable```或者```Callable```任务**，
- 主线程可以在完成自己的任务后再去获取结果，也通过```cancel```取消FutureTask的执行等。

### 1.1 初始化
- Callable方式
```java
Callable<String> callable = new Callable<String>() {
    @Override
    public String call() throws Exception {
        Thread.sleep(2000);
        // 设置任务的返回值
        return "I am awake";
    }
};

FutureTask<String> futureTask = new FutureTask<String>(callable);
```

- Runnable方式
```java
Runnable runnable =new Runnable() {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // runnable 没有返回值
    }
};

// 此处第二个参数为runnable执行完后的 期望返回值
FutureTask<String> futureTask = new FutureTask<String>(runnable,"I am runnable awake");
```

### 1.2 执行
### 1.2.1 使用线程池
```java
ExecutorService es = Executors.newSingleThreadExecutor();
es.submit(futureTask);

// 任务执行完后记得shutdown线程池
// es.shutdown();
```
### 1.2.2 结束
```java
futureTask.cancel(true);
```
如果futureTask中的任务尚未执行完毕就提前终止，需要调用```cancel()```接口主动结束。
如果只是``shutdown()`` ThreadPool并不能终止futureTask的执行，而是需要调用```shutdownNow()```


### 1.3、关键API

#### 1.3.1 ```V get()``` 与 ```V get(timeout, timeunit)```

#### 1.3.2 ```cancel(boolean mayInterruptIfRunning)```
取消子任务的执行.
1. 如果这个子任务已经执行结束，或者已经被取消，或者不能被取消，这个方法就会执行失败并返回false；
2. 如果子任务还没有开始执行，那么子任务会被取消，不会再被执行；
3. 如果子任务已经开始执行了，但是还没有执行结束：根据```mayInterruptIfRunning```的值，
    - 如果```mayInterruptIfRunning = true```，那么会**中断**执行任务的线程，然后返回true；
    - 如果参数为```mayInterruptIfRunning = false```，会返回true，允许正在进行的任务执行完成。
    这里所谓的**中断**，其实就是```thread.interrupt()```，向子线程发一个中断。所以**需要子线程在任务中响应这个中断再才能结束**。


#### 1.3.3 ```boolean isCancelled()```

判断任务是否被取消，如果任务执行结束（正常执行结束和发生异常结束，都算执行结束）前被取消。
也就是调用了cancel()方法，并且cancel()返回true，则该方法返回true，否则返回false。


#### 1.3.4 ```boolean isDone()```
判断任务是否执行结束，正常执行结束，或者发生异常结束，或者被取消，都属于结束，该方法都会返回true。



### 1.4 示例代码
```java
class MyTest {
    public static void main(String[] args) {

        MyTest inst = new MyTest();
        // 创建任务集合
        List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
        // 创建线程池
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            // 传入Callable对象创建FutureTask对象
            FutureTask<Integer> ft = new FutureTask<Integer>(new ComputeTask(i, "" + i));
            taskList.add(ft);
            // 提交给线程池执行任务，也可以通过exec.invokeAll(taskList)一次性提交所有任务;
            exec.submit(ft);
        }

        System.out.println("所有计算任务提交完毕, 主线程接着干其他事情！");

        // 按照List中的顺序，开始get统计各线程计算结果
        Integer totalResult = 0;
        for (FutureTask<Integer> ft : taskList) {
            try {
                //FutureTask的get方法会自动 阻塞 ,直到获取计算结果为止
                totalResult = totalResult + ft.get();
                System.out.println("get task result :"+ft.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 关闭线程池
        exec.shutdown();
        System.out.println("多任务计算后的总结果是:" + totalResult);

    }

    private static class ComputeTask implements Callable<Integer> {

        private Integer result = 0;
        private String taskName = "";

        public ComputeTask(Integer iniResult, String taskName) {
            result = iniResult;
            this.taskName = taskName;
            System.out.println("生成子线程计算任务: " + taskName);
        }

        public String getTaskName() {
            return this.taskName;
        }

        @Override
        public Integer call() throws Exception {

            for (int i = 0; i < 100; i++) {
                result += i;
            }
            // 休眠5秒钟，观察主线程行为，预期的结果是主线程会继续执行，到要取得FutureTask的结果是等待直至完成。
            Thread.sleep( 1000);
            System.out.println("子线程计算任务: " + taskName + " 执行完成!");
            return Integer.parseInt(taskName);
        }
    }
}

```
## 2、Future原理解析

Future如何返回结果

Future如何抛异常









