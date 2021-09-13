# TouchEvent 触摸事件

参考：  
[《Android 触摸事件分发机制（一）从内核到应用 一切的开始》](https://www.viseator.com/2017/09/14/android_view_event_1/)  
[《Android 触摸事件分发机制（二）原始事件消息传递与分发的开始》](https://www.viseator.com/2017/10/07/android_view_event_2/)  


内核是以处理中断的方式处理用户的输入的。触摸事件作为一种特殊的输入事件，同样需要使用这种方式。
1. 触摸事件最初来源于用户点击屏幕硬件，然后引起中断，接着由这些硬件的驱动程序进行处理。这些事情都发生在系统内核中。  
2. 当采集到这些原始的触摸事件后，```InputManagerService```这个Android中的系统服务就开始将其封装成一个```Msg```，然后通过**跨进程通信的方式(IPC，此处用的是socket)**发给App的UI线程进行处理。  
3. 触摸事件来到App中，先到Native层进行处理，进入App的主线程的MessageQueue中，然后通过JNI的```CallVoidMethod()```方法来调用java层的```dispatchInputEvent()```方法。  

## Java层View触摸事件分发

- ```dispatchTouchEvent()``` 负责touch事件的分发
- ```onInterceptTouchEvent()``` 负责拦截touch事件
- ```onTouchEvent()``` 最终处理touch事件

这三者用伪代码可以表示为：

```java
// Touch事件分发
public boolean dispatchTouchEvent(MotionEvent ev){
    boolean consume = fasle;
    // 1. 先判断自己是否需要拦截
    if (onInterceptTouchEvent(ev)){

        if (mOnTouchListener != null){
            // 1.1 如果设置了 mOnTouchListener，则交给其处理
            consume = mOnTouchListener.onTouch(ev);
        } else {
            // 1.2 否则
            onTouchEvent(ev);
        }
        
    } else {
        // 2. 否则传递给 子view 进行处理
        for (View child : children){
            // 如果 子view在播放动画 && 坐标在子view内
            if("坐标在子view内"){
                consume = child.dispatchTouchEvent(ev);
            }
            // 如果 子view消费了这个事件，则跳出循环
            if(consume)
                break;
        }
    }
    return consume;
}


public boolean onTouchEvent(MotionEvent ev){

}

```

**过程**：

首先调用的是应用根View（首先传递给Activity，Activity再传递给Window，然后Window再传递给顶级View），即DecorView。DecorView是ViewGroup的子类。

接着DecorView就进行后续的遍历式的分发到子view。如果子view拦截并且消耗了这个事件，那么过程结束；否则再遍历子view进行分发。如此反复，直到最低级的view。如果这个事件被该View消耗，那么事件的传递就此结束。如果该View没有消耗这个事件，那么这个事件会依次向父View传递，直到消耗了这个事件。如果没有View消耗这个事件，那么该事件就会被传递给Activity处理。整个事件流向是一个类U型图。



