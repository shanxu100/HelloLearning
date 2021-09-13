# 四大组件：
[toc]


## 1、Activity：
- 生命周期：

### 1.1 启动模式：
- **standard**
默认启动模式，每次激活Activity时都会创建Activity，并放入任务栈中，永远不会调用onNewIntent()。
- **singleTop 栈顶复用模式** 
如果在任务的栈顶正好存在该Activity的实例， 就重用该实例，并调用其onNewIntent()，否者就会创建新的实例并放入栈顶(即使栈中已经存在该Activity实例，只要不在栈顶，都会创建实例，而不会调用onNewIntent()，此时就跟standard模式一样)。
- **singleTask 栈内复用模式** 
如果在栈中已经有该Activity的实例，就重用该实例(会调用实例的onNewIntent())。重用时，会让该实例回到栈顶，因此在它上面的实例将会被移除栈。如果栈中不存在该实例，将会创建新的实例放入栈中（此时不会调用onNewIntent()）。
- **singleInstance**
在一个新栈中创建该Activity实例，并让多个应用共享改栈中的该Activity实例。一旦改模式的Activity的实例存在于某个栈中，任何应用再激活改Activity时都会重用该栈中的实例，其效果相当于多个应用程序共享一个应用，不管谁激活该Activity都会进入同一个应用中。

**设置启动模式**
- 在 Manifest.xml中指定Activity启动模式
```略...```

- 启动Activity时。在Intent中指定启动模式去创建Activity
通过Intent的addFlags方法去动态指定一个启动模式
```java
Intent intent = new Intent();
intent.setClass(context, MainActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(intent);
```
- **差异**
 **优先级**：动态指定方式即另外一种比第一种优先级要高，若两者同一时候存在，以方式二为准。
 **限定范围**：方式一无法为Activity直接指定 ```FLAG_ACTIVITY_CLEAR_TOP``` 标识；方式二无法为Activity指定 ```singleInstance``` 模式。

-  任务栈：前台任务栈、后台任务栈
-  TaskAffinity + singleTask
-  使用adb查看任务栈信息

- onNewIntent()调用时机

### 1.2 启动（跳转）方式：
- 显式：intent.setClass()
- 隐式：设置过滤信息：```action```、```category```、```data```类别，且同时匹配上述三类
- 路由跳转：参考ARouter
**安全跳转**
一旦发生隐式Intent找不到合适的调用组件的情况，系统就会抛出ActivityNotFoundException的异常。
所以，在发出这个隐式Intent之前调用resolveActivity做检查。
```java
Intent intent = new Intent(mContext, SecondActivity.class);
if (intent.resolveActivity(getPackageManager()) != null) {
    startActivity(intent);
}
```
```java
activity.startIntentSenderForResult
```

### 1.3 四种状态：
- Active/Running： 它处于可见并可和用户交互的激活状态。
- Paused： 仍然可见，但它已经失去了焦点故不可与用户交互。比如当 Activity 被另一个透明或者 Dialog 样式的 Activity 覆盖时的状态。
- Stoped： 当 Activity 被另外一个 Activity 覆盖、失去焦点并不可见时处于 Stoped 状态。
- Killed： Activity被系统杀死回收或者没有被启动时处于 Killed 状态。
### activity中的table是什么？


--------------

## 2、Service：

### 2.1 生命周期：

### 2.2 关键方法

startService()：启动服务，并让服务无限期运行
stopService()
stopSelf()：在service中调用，停止自己

context.bindService() ：绑定服务
context.unbindService()：解绑服务（context需要是同一个对象）
您可以同时将多个客户端连接到服务。但是，系统会缓存 IBinder 服务通信通道。换言之，**只有在第一个客户端绑定服务时，系统才会调用服务的 onBind() 方法来生成 IBinder**。然后，系统会将同一 IBinder 传递至绑定到相同服务的所有其他客户端，无需再次调用 onBind()。



### 2.3 如何提高service的生存率
1.  在Service中调用```startforeground()```，将service变成前台服务（需配合状态栏Notification使用）
2.	在```onStartCommand()```中```return START_STICKY;```(作用不大)
3.	注册静态BroadcastReceiver，监听系统广播，然后判断Service状态
4.	守护进程

------------------

## 3、BroadcastReceiver：
### 3.1 广播的分类
- 前台广播和后台广播


- 无序广播
- 有序广播：
	- 接收者按照预先声明的优先级依次接收Broadcast；
	- 可以把数据存入结果对象中，传递给下一个接收者；
	- 可以被一种某一个接收者终止。


- 系统广播：系统内置的一些广播，比如监听网络变化、打开相机等等
- 本地广播（APP内）Local Broadcast


BroadReceiver同样运行在主线程，不能直接执行耗时操作，不然会出现ANR(应用程序无响应)。
也不能用子线程来做，因为每次广播来的时候都会创建一个Reveiver对象，并且调用onReceiver。执行完之后 ，对象会立刻被销毁，子线程也没了？？？
要做耗时操作的话，应该通过发送Intent给Service，由Service来完成。
若使用动态注册广播，当不需要时需要进行unregister（如在onDestory中）

### 3.2 动态与静态
#### 3.2.1 静态注册 
- 在AndroidManifest.xml中设置，程序不用启动亦可接收。 典型代表：很多开机启动的APP，都是接收开机启动广播带起服务的。
**静态广播接收器**由```PackageManagerService```负责：当手机启动时（或者新安装了应用），PMS负责扫描手机中所有已安装的APP应用（，将AndroidManifest.xml中 有关注册广播的信息 解析出来，存储至一个全局静态变量当中```mReceivers```。
#### 3.2.2 动态注册 
- 在代码中注册广播，程序未启动时，无法接收广播。 
 **动态广播接收器**由```ActivityManagerService```负责：当APP的服务或者进程起来之后，执行了注册广播接收的代码逻辑，最后会存储在一个全局静态变量```mReceivers```中。
#### 3.2.3 广播接收顺序
- 当广播为普通广播时：
	1. 无视优先级，动态广播接收器优先于静态广播接收器
	2. 静态：先扫描的大于后扫描的，动态：先注册的大于后注册的。

- 有序广播：
	1. 优先级高的先接收
	2. 同优先级的动静态广播接收器，动态优先于静态
	3. 若优先级相同，则静态：先扫描的大于后扫描的，动态：先注册的大于后注册的。


### 3.3 permission权限限制

先说一下有关android权限的一些东西，android有4种权限：
- ```normal```：低风险权限，只要申请了就可以使用（在AndroidManifest.xml中添加<uses-permission>标签），安装时不需要用户确认；
- ```dangerous```：高风险权限，安装时需要用户的确认才可使用；
- ```signature```：只有当申请权限的应用程序的数字签名与声明此权限的应用程序的数字签名相同时（如果是申请系统权限，则需要与系统签名相同），才能将权限授给它；
- ```signatureOrSystem```：签名相同，或者申请权限的应用为系统应用（在system image中）。
> https://blog.csdn.net/topsecrethhh/article/details/81976509


以下场景：
1. 一些敏感的广播并不想让第三方的应用收到（即发送带权限的广播）。
2. 要限制自己的Receiver接收某广播来源，避免被恶意的同样的ACTION的广播所干扰（即接收带权限的广播）。

**场景一：一些敏感的广播并不想让第三方的应用收到（即发送带权限的广播）**
1. 定义权限
在Manifest文件中，定义新的权限
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.chan.hms.sample">
	......
	//声明权限
    <permission android:name="com.example.myapp.permission.dynamic_install" android:protectionLevel="normal" />
	//使用权限（必须）
	<uses-permission android:name="com.example.myapp.permission.dynamic_install" />
	... ...
</manifest>
```
2. 发送广播
```java
mContext.sendBroadcast(intent, "com.example.myapp.permission.dynamic_install");  
```

**场景二：限制自己的Receiver接收某广播来源（即接收带权限的广播）**
1. 定义权限
在Manifest文件中，定义新的权限
```xml
	//声明权限（自己app声明或者其他app先声明）
    <permission android:name="com.example.myapp.permission.dynamic_install" android:protectionLevel="normal" />
	//使用权限（必须）
	<uses-permission android:name="com.example.myapp.permission.dynamic_install" />
```
**注意：一定要声明权限。**
若不声明权限，那么注册广播的时候，会出现 **“未知权限”** 问题，导致注册失败。比如：
```2019-11-21 17:37:13.177 1256-1593/system_process W/PackageManager: Cannot whitelist unknown permission: com.example.myapp.permission.dynamic_install```
**例外：如果该权限在本app安装之前已经被其他app声明过了，那么自己app可以直接使用，不用声明（这种方式不好）。一定是要在安装之前声明**

2. 注册广播，添加权限
```java
mContext.registerReceiver(mBrocastReceiver,mIntentFilter,
			"com.example.myapp.permission.dynamic_install",null);
```
**注意：使用的权限必须写在Manifest文件中，用标签```uses-permission```。**

-------------------

## 4、ContentProvider：

```ContentProvider```通过uri来标识其它应用要访问的数据，使用```ContentResolver```的增、删、改、查方法实现对共享数据的操作

```ContentProvider```为不同的应用之间数据共享，提供统一的接口

```ContentProvider```使用和URI讲解:https://www.jianshu.com/p/5e13d1fec9c9

```java
context.grantUriPermission(pkgName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
```

--------

## 5、“第五大组件”——Fragment
- fragment可以看做Activity的一个模块，有自己的生命周期
- 一个fragment必须总是嵌入在一个activity中，同时fragment的生命周期受activity的影响

### Fragment与Activity的交互
Activity --> Fragment:
- Bundle：将数据封装到Bundle中，在activity中调用fragment.setArguments(bundle);Fragment中的onCreatView/onStart()方法中，通过getArgments()方法，获取到bundle对象
- getActivity()方法获取Activity对象

Fragment --> Activity
- 在Fragment中定义回调接口，Activity实现改接口。在Fragment中获得该Activity，然后调用定义的方法

Fragment --> Fragment
- 在Activity中定义公共数据部分，供多个Fragment共同访问

### 补充：回调是java不同对象之间数据交互的通用方法

--------

## Application

### attachBaseContext()方法有什么作用？什么时候调用

 首先执行attachBaseContext，然后才会执行onCreate方法。很多有自定义Application的渠道SDK，就是在attachBaseContext方法或者onCreate方法中进行了一些特殊化的操作。
 一样的逻辑，都是先反射创建对象，而且都是无参数的，然后调用attach方法，attach里面调用attachBaseContext来设置Context。

 ---------

## Activity/Service获取调用者的信息
参考：
[《Android中Activity/Service获取调用者的信息（FIDO UAF Client获取调用者的信息）》](https://cloud.tencent.com/developer/article/1396278)

### Activity
目标Activity获取调用自己的组件（即调用者）的信息
```java
getCallingActivity()
getCallingPackage()
```
**注意：只有调用者使用的是```startActivityForResult()```，且Intent不设置```Intent.FLAG_ACTIVITY_NEW_TASK```时才可以获取，调用```startActivity()```得到的是null。**

### Service
目标Service获取调用自己的组件（即调用者）的信息
```java
Binder.getCallingUid()
Binder.getCallingPid()
```
注意：
1. 当AIDL使用的是oneway（异步）声明时，Binder.getCallingPid()返回的是0 。
2. 获取到Uid之后，使用getPackageManager().getPackagesForUid(uid)获取到对应的包名。如果多个apk使用了shareUserId的话，返回值将会是多个包，这时候就没办法知道具体是哪个package调用的了。不过使用shareUserId的前提是使用相同的签名文件签名，而UAF要求是得到调用者apk签名的hash，这样的话哪个package调用结果都一样了。


