## Handler + postDelayed 实现周期性的定时任务

### 1、初始化
```java
private HandlerThread handlerThread;
private ScheduleJobHandler scheduleJobHandler;

handlerThread = new HandlerThread("testHandler");
handlerThread.start();
scheduleJobHandler = ScheduleJobHandler.newInstance(handlerThread.getLooper());

```

### 2、开始start
```java
scheduleJobHandler.start(2000, new ScheduleCallback() {
    @Override
    public void onRepeat() {
        Log.e(TAG, "do my own job..sleep");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Test runtime exception");
    }

    @Override
    public void onStop() {
        Log.e(TAG, "do my own job...stop");
    }
});
```

### 3、结束stop
```java
scheduleJobHandler.stop();
```




