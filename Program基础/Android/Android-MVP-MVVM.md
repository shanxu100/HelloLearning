# 架构：本质上是一种思想

## MVC
其中M层处理数据，业务逻辑等；V层处理界面的显示结果；C层起到桥梁的作用，来控制V层和M层通信
视图层(View)：一般采用XML文件进行界面的描述，这些XML可以理解为AndroidApp的View。
控制层(Controller)：Android中由Activit、Fragment承担，负责逻辑处理
模型层(Model)：提供数据，从进行数据库或者网络的操作。

缺点：在Android开发中，Activity并不是一个标准的MVC模式中的Controller，它的首要职责是加载应用的布局和初始化用户界面，接受并处理来自用户的操作请求，进而作出响应，**既是view层，又是controller层。随着界面及其逻辑的复杂度不断提升，Activity类的职责不断增加，以致变得庞大臃肿。**


## MVP
- MVP框架由3部分组成：
	- Model：提供数据，从进行数据库或者网络的操作
	- View：对应于Activity/Fragment等View，主要负责UI显示
	- Presenter：是Model和View之间的桥梁，进行逻辑处理。**View并不能直接对Model进行操作**

- 优点：将在Activty中的大量逻辑操作放到Presenter控制层中，避免Activity的臃肿。

- 缺点：MVP模式需要多写许多新的接口；过于复杂的逻辑会使得Presenter臃肿

- 实现方法：
	- 定义IView接口，Activity实现IView接口，然后在方法中更新UI；
	- 在Presenter中维持IView的一个引用；
	- 在Activity中实例化Presenter，然后将IView的实例（即this）赋值给Presenter。
	- 在Model中做具体的操作，Presenter获取具体的结果，通过调用所因为的View的方法，更新UI。

## MVVM
- Model，View和ViewModel
	- Model：提供数据，从进行数据库或者网络的操作
	- View：应于Activity/Fragment等View，主要负责UI显示；
	- ViewModel是负责逻辑处理；Model提供数据。ViewModel和View之间通过绑定，使得耦合度进一步降低

## AAC(Android Architecture Components,架构组件)

- LiveData：
	- 使用观察者模式，可以与控件绑定，监听数据的改变刷新UI。
	- 可以感知控件的生命周期，在控件销毁时自动取消注册，因此也不会产生内存泄漏

- ViewModel：将视图的数据和逻辑从具有生命周期特性的实体（如 Activity 和 Fragment）中剥离开来。比如 AndroidViewModel（ViewModle的子类）
	用来存储和管理UI相关的数据，这样在配置发生变化的情况下（比如旋转屏幕）时，数据不会丢失。viewmodel的生命周期独立于Activity和Fragment

- Room：官方数据库框架，对原生的SQLite API进行了一层封装。

	- 与SQLite相比：对于复杂的数据库结构，SQL使用复杂，代码冗长、管理困难；Room，使用简单、易于管理

## MVVM和AAC

**个人理解：MVVM是一种思想，AAC提供多种工具。利用AAC中的工具实现MVVM的思想**

View：

ViewModel：

Model：

- 橘黄色框的Repository及其下都是Model层。一个Repository数据仓库负责通过不同方式获取同类型的数据。
- 数据来源有：
	- 本地存储数据，如数据库，文件，SharedPreferences（本质也是文件）
	- 内存的缓存或临时数据
	- 通过各种网络协议获取的远程数据
- ViewModel在从Repository获取数据时，不需关注数据具体是怎么来的。


![](https://upload-images.jianshu.io/upload_images/1412608-fac3f62e45a39669.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

----------