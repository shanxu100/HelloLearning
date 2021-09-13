# View相关

## 三部曲
三个核心步骤：Measure、Layout、Draw




## 滑动冲突
滑动冲突的场景：3种
制定滑动的规则，从策略上解决冲突：即确定在什么时候响应哪一个控件的滑动事件

**解决滑动冲突：**

- 外部拦截法——在**父容器中进行拦截判断**，即重写onInterceptTouchEvent方法。根据上面制定的滑动策略，需要父容器拦截时，返回true，反之返回false。
- 内部拦截法——父容器不拦截事件，**全部交给子view处理**。如果子view不消耗该事件，最后交给父容器处理。

**内部拦截法的实现比较复杂，一般采用外部拦截法**


## 简述Activity、Window、WindowManager、View、ViewRootImpl的作用和相互之间的关系
- Activity不负责视图的控制，而是交给Window。这个Window本质上是一个PhoneWindow，被windowmanager管理。

- Window中有decorview，decorview是当前视图的底层View，是setContentView所设置View的父View
View是所有控件的基类。

- ViewRoot对应ViewRootImpl，它是连接WindowManager和DecorView的纽带。绘制的三大流程都是在ViewRootImpl中完成的：从ViewRootImpl中的performTraversals开始，有三个方法performMeasure, performLayout, prformDraw分别对应measure，layout，draw三个流程，完成对顶级View的绘制。

- 在父View的Measure过程中，会调用子View的Measure过程，如此反复，完成对整个View树的遍历。同理，在Layout和Draw中也是如此。

## ListView的优化
button点击


## ListView的RecycleBin原理
卡顿？
异步加载？

## RecyclerView

- 优点：
	- 封装了ViewHolder
	- 与ListView相比，耦合性更低、更加灵活：根据viewType设置不同的布局
	- 设置LayoutManager，实现ListView的功能和GridView的功能（支持 LinearLayoutManager 和 GridLayoutManager）
	- 支持局部刷新：notifyItemChanged()方法 （Listview用的BaseAdapter只有notifyDataSetChanged()方法）

- 缺点：
	- 使用更加复杂
	- 没有onItemClickListener()、setOnItemLongClickListener()方法，只有OnItemTouchListener()方法

- RecyclerView.Adapter
	- onCreateViewHolder()方法：产生一个ViewHolder对象，该对象中封装了view
	- onBindViewHolder()方法：根据传入的ViewHolder对象，显示数据
	- getItemViewType()方法：根据情况，返回不同的viewType，方便后续显示不同的布局和业务处理


### 自定义View
1、继承View，override onDraw()方法。需要手动处理wrap_content 、padding布局
	主要用于实现一些不规则效果

2、继承ViewGroup，实现自定义Layout。
	实现比较复杂,需要实现measure和layout两个过程

3、继承特定的view，比如TextView和LinearLayout
	用于拓展某种已有的功能

4、最好举例来说明

创建一个xml文件，为自定义控件设置一些自定义属性


# webView

## 加载
1. 提高渲染的优先级

	webSettings.setRenderPriority(RenderPriority.HIGH);

2. 把图片加载放在最后来加载渲染

	webSettings.setBlockNetworkImage(true);

3. 使用硬件加速，该功能在Android 3.0 (API level 11)才加入。
	硬件加速可以在一下四个级别开启或关闭：Application、Activity、Window、View
	比如，在AndroidManifest.xml中添加android:hardwareAccelerated属性；关闭view的硬件加速myView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

4. 开启缓存
	设置websetting


## js和java对象交互
1.	获取webview控件的websetting
2.	设置websetting.setJavascriptEnabled( true )
3.	将一个对象暴露给JavaScript：webview.addJavascriptInterface。这个对象包含了JS调用的方法，这些方法用@JavascriptInterface修饰

4.	JS通过这些方法与Android交互


## 防止OOM
1. 在代码中动态地将webview设置到布局中，而不是直接写到xml文件中；
2. 在Activity的onDestory中销毁webview